package com.rongdu.p2psys.crowdfunding.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.crowdfunding.dao.AttentionListDao;
import com.rongdu.p2psys.crowdfunding.dao.MaterialsDao;
import com.rongdu.p2psys.crowdfunding.dao.OrderDao;
import com.rongdu.p2psys.crowdfunding.dao.ProfitRuleDao;
import com.rongdu.p2psys.crowdfunding.dao.ProjectBaseinfoDao;
import com.rongdu.p2psys.crowdfunding.dao.VirtualAccountDao;
import com.rongdu.p2psys.crowdfunding.domain.AttentionList;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.domain.VirtualAccount;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.nb.account.dao.AccountDao;
import com.rongdu.p2psys.nb.account.dao.AccountLogDao;
import com.rongdu.p2psys.nb.user.dao.UserDao;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;

@Service("cfOrderService")
public class OrderServiceImpl implements OrderService{

	@Resource
	private OrderDao cfOrderDao;
	@Resource
	private AccountDao theAccountDao;
	@Resource
	private AccountLogDao theAccountLogDao;
	@Resource
	private UserDao theUserDao;
	@Resource
	private ProjectBaseinfoDao projectBaseinfoDao;
	@Resource
	private ProfitRuleDao profitRuleDao;
	@Resource
	private VirtualAccountDao virtualAccountDao;
	@Resource
	private AttentionListDao attentionListDao;
	@Resource
	private MaterialsDao materialsDao;
	@Resource
	private NoticeService noticeService;
	
	public void saveOrder(InvestOrder order) {
		cfOrderDao.save(order);
	}

	/**
	 * 获取订单列表
	 */
	public List<OrderModel> getList(Long projectId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("  FROM InvestOrder where projectBaseinfo.id= ");
		buffer.append(projectId);
		List<InvestOrder> list = cfOrderDao.getByHql(buffer.toString());
		List<OrderModel> result = null;
		if(null!=list&&!list.isEmpty()){
			result = new ArrayList<OrderModel>();
			for(int i = 0;i<list.size();i++){
				NumberFormat nf = new DecimalFormat(",###.##");
				OrderModel orderModel = new OrderModel();
				orderModel.setLeaderStatus(0);
				ProjectBaseinfo projectBaseinfo = list.get(i).getProjectBaseinfo();
				if(null!=projectBaseinfo.getLeader()){
					Leader leader = projectBaseinfo.getLeader();
					if(leader.getUser().getUserId()==list.get(i).getUser().getUserId()){
						orderModel.setLeaderStatus(1);
					}
					if(null!=leader.getLeaderFactory()){
						if(leader.getLeaderFactory().getUserId()==list.get(i).getUser().getUserId()){
							orderModel.setLeaderStatus(1);
						}
					}
				}
				orderModel = OrderModel.instance(list.get(i));
				orderModel.setBuyMoney(nf.format(orderModel.getMoney()));
				String userName = list.get(i).getUser().getUserName().substring(0, 3) + "****"
						+ list.get(i).getUser().getUserName().substring(7, list.get(i).getUser().getUserName().length());
				orderModel.setUserName(userName);
				orderModel.setUserPic(list.get(i).getUser().getUserCache().getCardPositive());
				result.add(orderModel);
			}
		}
		return result;
	}

	public void updateOrder(InvestOrder order) {
		cfOrderDao.update(order);
	}

	public PageDataList<OrderModel> getOrderList(OrderModel model,int pageNumber, int pageSize) {
		return cfOrderDao.getOrderList(model,pageNumber,pageSize);
	}

	/**
	 * 购买产品
	 */
	public InvestOrder save(ProjectBaseinfo projectBaseinfo, Double money, User user,Integer type,Integer profitRule,double virtual) {
		//保存订单
		InvestOrder cfOrder = new InvestOrder();
		ProfitRule profit = profitRuleDao.find(profitRule);
		cfOrder.setAddTime(new Date());
		cfOrder.setProjectBaseinfo(projectBaseinfo);
		cfOrder.setMoney(money);
		cfOrder.setUser(user);
		cfOrder.setPayTime(new Date());
		cfOrder.setProfitRule(profit);
		
		projectBaseinfo.setAccount(projectBaseinfo.getAccount()+money);
		projectBaseinfoDao.update(projectBaseinfo);
		
		Double payMoney = 0.0;
		String desc = "购买产品<a href='/pro/detail.html?id="+projectBaseinfo.getId()+"' >["+projectBaseinfo.getProjectName()+"]</a>,扣除";
		//发送短信前置工作
		
		//选择投资类型
		if(1==type){
			payMoney = BigDecimalUtil.mul(projectBaseinfo.getBreach(),money);
			cfOrder.setPayMoney(payMoney);
			cfOrder.setPayStatus(1);
			desc = desc+"保障金"+payMoney+"元。";
			
			String mobilePhone = user.getUserName();
			NoticeType smsType = Global.getNoticeType("cf_a_invest", NoticeConstant.NOTICE_SMS);
			Map<String, Object> sendData = new HashMap<String, Object>();
			Notice sms = new Notice();
			sendData.put("projectName",projectBaseinfo.getProjectName());
			sms.setType(NoticeConstant.NOTICE_SMS + "");
			sms.setReceiveAddr(mobilePhone);
			sms.setNid("cf_a_invest");
			sendData.put("money",money);
			//操作发送短信
			if(smsType.getSend() == 1 && StringUtil.isNotBlank(mobilePhone)) {
				sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
				noticeService.sendNotice(sms);
			}
		}else{
			payMoney = money;
			cfOrder.setPayMoney(payMoney);
			cfOrder.setPayStatus(2);
			desc = desc+"全额款"+payMoney+"元。其中虚拟货币："+virtual+"元";
			
			String mobilePhone = user.getUserName();
			NoticeType smsType = Global.getNoticeType("cf_invest", NoticeConstant.NOTICE_SMS);
			Map<String, Object> sendData = new HashMap<String, Object>();
			Notice sms = new Notice();
			sendData.put("projectName",projectBaseinfo.getProjectName());
			sendData.put("money",money);
			sms.setType(NoticeConstant.NOTICE_SMS + "");
			sms.setReceiveAddr(mobilePhone);
			sms.setNid("cf_invest");
			//操作发送短信
			if(smsType.getSend() == 1 && StringUtil.isNotBlank(mobilePhone)) {
				sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
				noticeService.sendNotice(sms);
			}
		}
		payMoney = BigDecimalUtil.sub(payMoney,virtual);
		Account account = theAccountDao.getByUserId(user.getUserId());
		account.setTotal(BigDecimalUtil.sub(account.getTotal(),payMoney));
		account.setUseMoney(BigDecimalUtil.sub(account.getUseMoney(),payMoney));
		//虚拟货币
		if(0!=virtual){
			StringBuffer buffer = new StringBuffer();
			buffer.append(" FROM VirtualAccount WHERE  user.userId =");
			buffer.append(user.getUserId());
			List<VirtualAccount>  virtualAccount = virtualAccountDao.getByHql(buffer.toString());
			VirtualAccount virtualAccount2 = null;
			if(null!=virtualAccount){
				virtualAccount2 = virtualAccount.get(0);
				virtualAccount2.setAccount(BigDecimalUtil.sub(virtualAccount2.getAccount(),virtual));
				virtualAccount2.setUpdateTime(new Date());
				virtualAccountDao.update(virtualAccount2);
			}
		}
		
		theAccountDao.update(account);
		//添加资金日志
		AccountLog accountLog = new AccountLog();
		accountLog.setAddTime(new Date());
		accountLog.setCollection(account.getCollection());
		accountLog.setMoney(payMoney);
		accountLog.setNoUseMoney(account.getNoUseMoney());
		accountLog.setPaymentsType((byte) 0);
		accountLog.setRemark(desc);
		accountLog.setTotal(account.getTotal());
		accountLog.setToUser(theUserDao.find(1l));
		accountLog.setType("invest");
		accountLog.setUseMoney(account.getUseMoney());
		accountLog.setUser(user);
		theAccountLogDao.save(accountLog);
		cfOrder = cfOrderDao.save(cfOrder);

		return cfOrder;
	}

	public List<InvestOrder> getAlready(User user, ProjectBaseinfo projectBaseinfo) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM InvestOrder where user.userId=");
		buffer.append(user.getUserId());
		buffer.append(" AND projectBaseinfo.id = ");
		buffer.append(projectBaseinfo.getId());
		buffer.append(" AND payStatus!=0  ");
		return cfOrderDao.getByHql(buffer.toString());
	}

	/**
	 * 购买记录
	 */
	public List<OrderModel> getBuyList(User user) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM InvestOrder where user.userId =  ");
		buffer.append(user.getUserId());
		List<InvestOrder> list = cfOrderDao.getByHql(buffer.toString());
		List<OrderModel> result = null;
		if(null!=list&&!list.isEmpty()){
			result = new ArrayList<OrderModel>();
			for(int i =0;i<list.size();i++){
				OrderModel orderModel = new OrderModel();
				orderModel = OrderModel.instance(list.get(i));
				orderModel.setProjectName(list.get(i).getProjectBaseinfo().getProjectName());
				orderModel.setModelStatus(checkDate(list.get(i).getProjectBaseinfo()));
				orderModel.setProjectType(list.get(i).getProjectBaseinfo().getType());
				result.add(orderModel);
			}
		}
		return result;
	}

	/**
	 * 计算产品状态
	 * @param base
	 * @return
	 */
	private static int checkDate(ProjectBaseinfo base) {
		Date nowdate = new Date();
		Date s = base.getStartTime();
		Date e = base.getEndTime();

		boolean sflag = nowdate.before(s);
		boolean eflag = e.before(nowdate);
		if (3 == base.getStatus()) {
			return 4;
		}
		if(4 == base.getStatus()){
			return 5;
		}
		if (sflag) {
			return 1;// 预热
		}
		if (eflag) {
			return 3;// 失败
		}
		return 2;// 众筹中
	}

	/**
	 * 支付剩余款项
	 */
	public String payAll(Integer id) {
		InvestOrder cfOrder = cfOrderDao.find(id);
		if(2==cfOrder.getPayStatus()){
			return "该记录已完成全额支付!";
		}else if(0==cfOrder.getPayStatus()){
			return "该记录已经过期!";
		}else if(3==cfOrder.getPayStatus()){
			return "该记录已经取消!";
		}else{
			
			Double subMoney = BigDecimalUtil.sub(cfOrder.getMoney(),cfOrder.getPayMoney());
			Account account = theAccountDao.getByUserId(cfOrder.getUser().getUserId());
			
			if(account.getUseMoney()<subMoney){
				return "账号余额不足！";
			}else{
				account.setTotal(BigDecimalUtil.sub(account.getTotal(),subMoney));
				account.setUseMoney(BigDecimalUtil.sub(account.getUseMoney(),subMoney));
				
				theAccountDao.update(account);
				
				//资金日志添加
				AccountLog accountLog = new AccountLog();
				accountLog.setAddTime(new Date());
				accountLog.setCollection(account.getCollection());
				accountLog.setMoney(subMoney);
				accountLog.setNoUseMoney(account.getNoUseMoney());
				accountLog.setPaymentsType((byte) 0);
				accountLog.setRemark("您预约跟投的产品<a href='/pro/detail.html?id="+cfOrder.getProjectBaseinfo().getId()+"' >["+cfOrder.getProjectBaseinfo().getProjectName()+"]</a>,付全款成功。付款金额:"+subMoney);
				accountLog.setTotal(account.getTotal());
				accountLog.setToUser(theUserDao.find(1l));
				accountLog.setType("invest");
				accountLog.setUseMoney(account.getUseMoney());
				accountLog.setUser(cfOrder.getUser());
				theAccountLogDao.save(accountLog);
				
				cfOrder.setPayMoney(cfOrder.getMoney());
				cfOrder.setPayStatus(2);
				cfOrder.setPayTime(new Date());
				cfOrderDao.update(cfOrder);
				
				User user = cfOrder.getUser();
				ProjectBaseinfo projectBaseinfo = cfOrder.getProjectBaseinfo();
				String mobilePhone = user.getUserName();
				NoticeType smsType = Global.getNoticeType("cf_a_success", NoticeConstant.NOTICE_SMS);
				Map<String, Object> sendData = new HashMap<String, Object>();
				Notice sms = new Notice();
				sendData.put("projectName",projectBaseinfo.getProjectName());
				sms.setType(NoticeConstant.NOTICE_SMS + "");
				sms.setReceiveAddr(mobilePhone);
				sms.setNid("cf_a_success");
				//操作发送短信
				if(smsType.getSend() == 1 && StringUtil.isNotBlank(mobilePhone)) {
					sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
					noticeService.sendNotice(sms);
				}
				
				return ConstantUtil.SUCCESS;
			}
		}
	}

	public Double getInvestMoney(Long userId) {
		Double money = 0.0;
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM InvestOrder where user.userId =  ");
		buffer.append(userId);
		buffer.append(" AND (payStatus = 1 or payStatus =2)  ");
		List<InvestOrder> list = cfOrderDao.getByHql(buffer.toString());
		if(null!=list){
			for(int i=0;i<list.size();i++){
				money = BigDecimalUtil.add(money,list.get(i).getPayMoney());
			}
			return money;
		}
		return money;
	}

	/**
	 * 取消订单
	 */
	public String caclePay(Integer id) {
		InvestOrder cfOrder = cfOrderDao.find(id);
		if(2==cfOrder.getPayStatus()){
			return "该记录已完成全额支付!";
		}else if(0==cfOrder.getPayStatus()){  
			return "该记录已经过期!";
		}else if(3==cfOrder.getPayStatus()){
			return "该记录已经取消!";
		}else{
			ProjectBaseinfo projectBaseinfo = projectBaseinfoDao.find(cfOrder.getProjectBaseinfo().getId());
			projectBaseinfo.setAccount(BigDecimalUtil.sub(projectBaseinfo.getAccount(),cfOrder.getMoney()));
			projectBaseinfoDao.update(projectBaseinfo);
			Account account = theAccountDao.getByUserId(cfOrder.getUser().getUserId());
			account.setTotal(BigDecimalUtil.add(account.getTotal(),cfOrder.getPayMoney()));
			account.setUseMoney(BigDecimalUtil.add(account.getUseMoney(),cfOrder.getPayMoney()));
			
			theAccountDao.update(account);
			
			AccountLog accountLog = new AccountLog();
			accountLog.setAddTime(new Date());
			accountLog.setCollection(account.getCollection());
			accountLog.setMoney(cfOrder.getPayMoney());
			accountLog.setNoUseMoney(account.getNoUseMoney());
			accountLog.setPaymentsType((byte) 0);
			accountLog.setRemark("您预约跟投的产品<a href='/pro/detail.html?id="+cfOrder.getProjectBaseinfo().getId()+"' >["+cfOrder.getProjectBaseinfo().getProjectName()+"]</a>,已成功取消。退回金额:"+cfOrder.getPayMoney());
			accountLog.setTotal(account.getTotal());
			accountLog.setToUser(theUserDao.find(1l));
			accountLog.setType("invest");
			accountLog.setUseMoney(account.getUseMoney());
			accountLog.setUser(cfOrder.getUser());
			theAccountLogDao.save(accountLog);
			
			cfOrder.setPayMoney(0.0);
			cfOrder.setPayStatus(3);
			cfOrder.setPayTime(new Date());
			cfOrderDao.update(cfOrder);
			
			User user = cfOrder.getUser();
			String mobilePhone = user.getUserName();
			NoticeType smsType = Global.getNoticeType("cf_a_cacle", NoticeConstant.NOTICE_SMS);
			Map<String, Object> sendData = new HashMap<String, Object>();
			Notice sms = new Notice();
			sendData.put("projectName",projectBaseinfo.getProjectName());
			sms.setType(NoticeConstant.NOTICE_SMS + "");
			sms.setReceiveAddr(mobilePhone);
			sms.setNid("cf_a_cacle");
			//操作发送短信
			if(smsType.getSend() == 1 && StringUtil.isNotBlank(mobilePhone)) {
				sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
				noticeService.sendNotice(sms);
			}
			
			return ConstantUtil.SUCCESS;
		}
	}

	/**
	 * 定时任务 筛选违约订单
	 */
	@SuppressWarnings("static-access")
	public void changeType() {
		String hql = " FROM InvestOrder where payStatus = 1 ";
		List<InvestOrder> list = cfOrderDao.getByHql(hql);
		if(null!=list){
			for(int i =0;i<list.size();i++){
				Date nowDate = new Date();
				InvestOrder cfOrder = list.get(i);
				if(1==cfOrder.getPayStatus()){
					Date date = cfOrder.getPayTime();				
					Calendar calendar2 = new GregorianCalendar(); 
					Calendar calendar1 = new GregorianCalendar(); 
					calendar2.setTime(date); 
					calendar1.setTime(date); 
					calendar2.add(calendar2.DATE,3);
					//calendar2.add(calendar2.MINUTE,5);
					date=calendar2.getTime(); 
					calendar1.add(calendar1.DATE,2);
					//calendar1.add(calendar1.MINUTE,3);
					Date d2 = calendar1.getTime(); 
					if(nowDate.after(d2)){
						Notice notice = noticeService.findByOrderId(cfOrder.getId());
						if(null==notice){
							notice = new Notice();
							notice.setAddTime(new Date());
							notice.setNid("cf_a_warning");
							notice.setReceiveAddr(cfOrder.getUser().getUserName());
							notice.setReceiveUser(cfOrder.getUser());
							notice.setStatus(1);
							notice.setTitle(String.valueOf(cfOrder.getId()));
							notice.setType(NoticeConstant.NOTICE_SMS + "");
							NoticeType smsType = Global.getNoticeType("cf_a_warning", NoticeConstant.NOTICE_SMS);
							Map<String, Object> sendData = new HashMap<String, Object>();
							sendData.put("projectName",cfOrder.getProjectBaseinfo().getProjectName());
							notice.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
							noticeService.sendNotice(notice);
						}
					}
					if(nowDate.after(date)){
						cfOrder.setPayStatus(0);
						Account account = theAccountDao.getByUserId(cfOrder.getUser().getUserId());
						ProjectBaseinfo projectBaseinfo = projectBaseinfoDao.find(cfOrder.getProjectBaseinfo().getId());
						projectBaseinfo.setAccount(BigDecimalUtil.sub(projectBaseinfo.getAccount(),cfOrder.getMoney()));
						projectBaseinfoDao.update(projectBaseinfo);
						AccountLog accountLog = new AccountLog();
						accountLog.setAddTime(new Date());
						accountLog.setCollection(account.getCollection());
						accountLog.setMoney(cfOrder.getPayMoney());
						accountLog.setNoUseMoney(account.getNoUseMoney());
						accountLog.setPaymentsType((byte) 0);
						accountLog.setRemark("您预约跟投的产品<a href='/pro/detail.html?id="+cfOrder.getProjectBaseinfo().getId()+"' >["+cfOrder.getProjectBaseinfo().getProjectName()+"]</a>,未能按时缴款，扣除保障金:"+cfOrder.getPayMoney());
						accountLog.setTotal(account.getTotal());
						accountLog.setToUser(theUserDao.find(1l));
						accountLog.setType("invest");
						accountLog.setUseMoney(account.getUseMoney());
						accountLog.setUser(cfOrder.getUser());
						theAccountLogDao.save(accountLog);
						cfOrderDao.update(cfOrder);
					}
				}
			}
		}
	}

	public InvestOrder find(Integer id) {
		return cfOrderDao.find(id);
	}

	public List<OrderModel> getLeaderList(User user) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM InvestOrder where projectBaseinfo.leader.user.userId =  ");
		buffer.append(user.getUserId());
		List<InvestOrder> list = cfOrderDao.getByHql(buffer.toString());
		List<OrderModel> result = null;
		if(null!=list&&!list.isEmpty()){
			result = new ArrayList<OrderModel>();
			for(int i =0;i<list.size();i++){
				OrderModel orderModel = new OrderModel();
				orderModel = OrderModel.instance(list.get(i));
				orderModel.setProjectName(list.get(i).getProjectBaseinfo().getProjectName());
				orderModel.setModelStatus(checkDate(list.get(i).getProjectBaseinfo()));
				orderModel.setProjectType(list.get(i).getProjectBaseinfo().getType());
				result.add(orderModel);
			}
		}
		return result;
	}

	@Override
	public List<OrderModel> getBuyListForWechat(User user,int type) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM InvestOrder where user.userId =  ");
		buffer.append(user.getUserId());
		if(0 != type){
			buffer.append(" and projectBaseinfo.type =");
			buffer.append(type);
		}
		List<InvestOrder> list = cfOrderDao.getByHql(buffer.toString());
		List<OrderModel> result = null;
		if(null!=list&&!list.isEmpty()){
			result = new ArrayList<OrderModel>();
			for(int i =0;i<list.size();i++){
				OrderModel orderModel = new OrderModel();
				orderModel = OrderModel.instance(list.get(i));
				orderModel.setProjectName(list.get(i).getProjectBaseinfo().getProjectName());
				orderModel.setModelStatus(checkDate(list.get(i).getProjectBaseinfo()));
				orderModel.setProjectType(list.get(i).getProjectBaseinfo().getType());
				orderModel.setAttetionFlage(getAttentionFlage(user.getUserId(),list.get(i).getProjectBaseinfo().getId()));
				orderModel.setProjectPic(getProjectImg(list.get(i).getProjectBaseinfo().getId()));
				orderModel.setProjectId(list.get(i).getProjectBaseinfo().getId());
				result.add(orderModel);
			}
		}
		return result;
	}

	//获取当前用户关注列表
	public int getAttentionFlage(long userId,long projectId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM AttentionList where userId =  ");
		buffer.append(userId);
		buffer.append(" and projectId = ");
		buffer.append(projectId);
		List<AttentionList> list = attentionListDao.getByHql(buffer.toString());
		if(null!=list&&!list.isEmpty()){
			return 1;
		}else{
			return 0;
		}
	}
	//获取当前项目图片
	public String getProjectImg(long projectId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM Materials where projectBaseinfo.id =  ");
		buffer.append(projectId);
		buffer.append(" and materialCode = ");
		buffer.append("'project_img'");
		List<Materials> list = materialsDao.getByHql(buffer.toString());
		String img ="";
		if(null!=list&&!list.isEmpty()){
			img = list.get(0).getMaterialContent();
		}
		return img;
	}

	@Override
	public List<InvestOrder> getInvestOrderById(long userId, long projectId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("  FROM InvestOrder where projectBaseinfo.id= ");
		buffer.append(projectId);
		buffer.append(" and user.userId=");
		buffer.append(userId);
		List<InvestOrder> list = cfOrderDao.getByHql(buffer.toString());
		return list;
	}

	public InvestOrder saveWechatPay(ProjectBaseinfo projectBaseinfo,
			Double money, User user, int type, Integer profitRule, double virtual) {
		//保存订单
				InvestOrder cfOrder = new InvestOrder();
				ProfitRule profit = profitRuleDao.find(profitRule);
				cfOrder.setAddTime(new Date());
				cfOrder.setProjectBaseinfo(projectBaseinfo);
				cfOrder.setMoney(money);
				cfOrder.setUser(user);
				cfOrder.setPayTime(new Date());
				cfOrder.setProfitRule(profit);
				
				projectBaseinfo.setAccount(projectBaseinfo.getAccount()+money);
				projectBaseinfoDao.update(projectBaseinfo);
				
				Double payMoney = 0.0;
				String desc = "微信支付购买产品<a href='/pro/detail.html?id="+projectBaseinfo.getId()+"' >["+projectBaseinfo.getProjectName()+"]</a>,扣除";
				//选择投资类型
				if(1==type){
					payMoney = BigDecimalUtil.mul(projectBaseinfo.getBreach(),money);
					cfOrder.setPayMoney(payMoney);
					cfOrder.setPayStatus(1);
					desc = desc+"保障金"+payMoney+"元。";
				}else{
					payMoney = money;
					cfOrder.setPayMoney(payMoney);
					cfOrder.setPayStatus(2);
					desc = desc+"微信支付"+payMoney+"元。其中虚拟货币："+virtual+"元";
				}
				payMoney = BigDecimalUtil.sub(payMoney,virtual);
				Account account = theAccountDao.getByUserId(user.getUserId());
				//添加资金日志
				AccountLog accountLog = new AccountLog();
				accountLog.setAddTime(new Date());
				accountLog.setCollection(money);
				accountLog.setMoney(money);
				accountLog.setNoUseMoney(account.getNoUseMoney());
				accountLog.setPaymentsType((byte) 0);
				accountLog.setRemark(desc);
				accountLog.setTotal(account.getTotal());
				accountLog.setToUser(theUserDao.find(1l));
				accountLog.setType("invest");
				accountLog.setUseMoney(account.getUseMoney());
				accountLog.setUser(user);
				theAccountLogDao.save(accountLog);
				cfOrder = cfOrderDao.save(cfOrder);
				
				return cfOrder;
	}
	
	public static void main(String[] args) {
		Date date = new Date();			
		System.out.println(date);
		Calendar calendar2 = new GregorianCalendar(); 
		Calendar calendar1 = new GregorianCalendar(); 
		calendar2.setTime(date); 
		calendar1.setTime(date); 
		calendar2.add(calendar2.DATE,3);
		//calendar2.add(calendar2.MINUTE,5);
		date=calendar2.getTime(); 
		System.out.println(date);
		calendar1.add(calendar1.DATE,2);
		date=calendar1.getTime();
		System.out.println(date);
	}
}
