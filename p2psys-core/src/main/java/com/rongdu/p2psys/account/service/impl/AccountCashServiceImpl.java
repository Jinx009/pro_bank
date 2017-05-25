package com.rongdu.p2psys.account.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountBankDao;
import com.rongdu.p2psys.account.dao.AccountCashDao;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountRechargeDao;
import com.rongdu.p2psys.account.dao.AccountSumDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.AccountCashBatchModel;
import com.rongdu.p2psys.account.model.AccountCashModel;
import com.rongdu.p2psys.account.model.payment.llPay.config.PartnerConfig;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.handler.Withdraw;
import com.rongdu.p2psys.account.model.payment.llPay.model.LianlPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderResultsRetBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.PayDataBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.WithdrawBean;
import com.rongdu.p2psys.account.model.payment.llPay.utils.LLPayUtil;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPay;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.borrow.dao.BorrowDao;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.payment.service.IChannelBankService;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrWebPayModel;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinaPnrType;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.tpp.domain.TppPnrPay;
import com.rongdu.p2psys.tpp.yjf.model.CashModel;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.jdbc.UserIdentifyDaoImpl;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;

@Service("accountCashService")
public class AccountCashServiceImpl implements AccountCashService {
	private final static Logger logger = Logger.getLogger(AccountCashServiceImpl.class);
	@Resource
	private AccountCashDao accountCashDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private AccountSumDao accountSumDao;
	@Resource
	private AccountRechargeDao accountRechargeDao;
	@Resource
	private BorrowDao borrowDao;
	@Resource
	private OperationLogDao operationLogDao;
	@Resource
	private VerifyLogDao verifyLogDao;
	@Resource
    private UserIdentifyDaoImpl userIdentifyDaoImpl;
	@Resource
	private UserCacheDao userCacheDao;
	@Resource
	private AccountBankDao accountBankDao;
	@Resource
	private ChinapnrService chinapnrService;
	@Resource
	private NoticeService noticeService;
	@Resource
	private UserService theUserService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private IChannelBankService channelBankService;
	@Resource
	private IOrderService orderService;

	@Override
	public AccountCash doCash(AccountCash cash,User user) {
		double supervisionMoney = cash.getMoney();//本次需提现的金额
		double use_money =0;//所有子属账号可提现余额总和
		List<Account> list = theAccountService.getAccountListByGroupId(user.getBindId());//所有子属账户列表
		Account current_account = theAccountService.getAccountByUserId(user.getUserId());//当前账号
		if(list.size() > 0){
			for (Account account_:list){
				use_money += account_.getTotal();
			}
		}
		// 所有从属账号可用余额总额大于提现金额
		if(use_money>=supervisionMoney){

				double useMoney = current_account.getUseMoney();//主账户即当前账户可用余额
				if(useMoney>=supervisionMoney){//主账户余额满足提现要求
					cash = accountCashSec(cash,user);
					cash(cash);
				}else{//主账户余额不足
					//优先从主账户提现
					cash.setMoney(useMoney);
					cash = accountCashSec(cash,user);
					cash(cash);
					double surplus = supervisionMoney - useMoney;//剩余提现金额
					for(int i = 0;i<list.size();i++){
						if(surplus>0){							
							Account account2 = list.get(i);
							User user2 = account2.getUser();
							
							//该用户不是主账户和当前用户
							if(user2.getUserId()!=user.getUserId()){								
								//可用余额最多账户足以支付剩余投资
								if(account2.getUseMoney()>=surplus){
									cash.setMoney(surplus);
									cash = accountCashSec(cash,user2);
									cash(cash);
									surplus = 0;
									break;
								}else{
									cash.setMoney(account2.getUseMoney());
									cash = accountCashSec(cash,user2);
									cash(cash);
									surplus = surplus-account2.getUseMoney();
								}
							}
						}
					}
				}
			
			//转出大额资金监管，如果转出金额大于资金监管金额，则发送短信提醒平台指定用户
			if (supervisionMoney >= Global.getDouble("supervision_money")) {
				sendSMSNotice(NoticeConstant.CASH_SUPERVISION_NOTICE,user, supervisionMoney);
			}
		}else{
			//可提现金额不足
			return null;
		}
		return cash;
	}

	/**
	 * 发送短信
	 * @param user
	 * @param supervisionMoney
	 */
	public void sendSMSNotice(String noticeKey,User user, double supervisionMoney) {
		Global.setTransfer("user", user);
		Global.setTransfer("money", supervisionMoney);
		NoticeType smsType = Global.getNoticeType(noticeKey, NoticeConstant.NOTICE_SMS);
		String receiveAddr = Global.getValue("admin_receive_phone");
		if(smsType.getSend() == 1 && StringUtil.isNotBlank(receiveAddr)) {
			Map<String, Object> sendData = new HashMap<String, Object>();
			sendData.put("user", user);
			sendData.put("addTime", new Date());
			sendData.put("money", supervisionMoney);
			Notice sms = new Notice();
			sms.setType(NoticeConstant.NOTICE_SMS + "");
			sms.setReceiveAddr(receiveAddr);
			sms.setNid(NoticeConstant.CASH_SUPERVISION_NOTICE);
			sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
			noticeService.sendNotice(sms);
		}
	}

	/**
	 * 提现业务处理
	 * @param cash
	 */
	private void cash(AccountCash cash) {
		cash.setAddTime(new Date());
		cash.setAddIp(Global.getIP());
		accountCashDao.save(cash);
		//保存记录到资金基本表
		Global.setTransfer("cash", cash);
		Global.setTransfer("user", cash.getUser());
		AbstractExecuter executer = ExecuterHelper.doExecuter("cashApplyExecuter");
		executer.execute(cash.getMoney(), cash.getUser());
	}
	
	@Override
	public Map<String, Object> doCash(AccountCash cash,String channlKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = false;
		String message = "提现失败";
		User user = cash.getUser();
		AccountBank ab = accountBankDao.find(user.getUserId(), cash.getBankNo(),channlKey);
		UnionPay pay = new UnionPay(ab.getBindId(), (long)cash.getMoney()*100, 2, "cash" + cash.getMoney());
		pay.setOrderNo(cash.getOrderNo());
		UnionPayRet ret = SignHelper.payment(pay);
		if("0000".equals(ret.getRetCode())){//调用成功
			cash = accountCashDao.findObjByProperty("orderNo", ret.getOrderNo());
			if(ret.getOrderStatus() == 2){//处理成功
				flag = true;
				cash.setStatus(1);
				accountCashDao.update(cash);
			}else if(ret.getOrderStatus() == 3){//处理失败
				flag = false;
				message = ret.getRetDesc();
				cash.setStatus(2);
				accountCashDao.update(cash);
			}
		}else{
			flag = false;
			message = ret.getRetDesc();
		}
		map.put("result", flag);
		map.put("message", message);
		return map;
	}

	@Override
	public void doCancleCash(AccountCash cash, Operator operator) {
		accountCashDao.updateStatus(cash.getId(), 2, 0);
		OperationLog log = new OperationLog(cash.getUser(), operator, "cash_fail");
		log.setOperationResult("用户名为" + operator.getUserName() + "（" + Global.getIP() + "）的操作员对用户为"
				+cash.getUser().getUserName()+"的提现ID为"+cash.getId()+"）进行取消提现操作");
		operationLogDao.save(log);
	}
	
	@Override
    public void verifyYLCash(AccountCashModel model, Operator operator){
		AccountCash cash = accountCashDao.find(model.getId());
		VerifyLog verifyLog = new VerifyLog(operator, "accountCash", cash.getId(), 2, model.getStatus(), model.getRemark());
		Global.setTransfer("cash", cash);
		if (model.getStatus() == 1) { // 审核通过
			if(cash.getStatus() != 6) {
				throw new AccountException("提现状态不正确，请查正后重新操作");
			}
			if(cash.getTransferType()!=null&&cash.getTransferType()==1)//线下充值
			{
				cash.setStatus(1);
				cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
				cash.setRemark("提现成功，实际到账金额:" + cash.getCredited() + "元。");
				accountCashDao.update(cash);
				Global.setTransfer("cash", cash);
				Global.setTransfer("user", cash.getUser());
				AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
				executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
			}
			else
			{
				cash.setStatus(1);
				checkCash(cash);
				User user = cash.getUser();
				cash.setOrderNo(OrderNoUtils.getSerialNumber());
				accountCashDao.update(cash);
				//银联处理
				AccountBank ab = accountBankDao.find(cash.getUser().getUserId(), cash.getBankNo(),ConstantUtil.channelKey.unionpay.getValue());
				if(ab == null) {
					ab = accountBankDao.find(cash.getUser().getUserId(), cash.getBankNo(),ConstantUtil.channelKey.llpay.getValue());
					if(ab == null) {
						throw new AccountException("用户提现银行卡已解绑", 2);
					}
				}
				UnionPay pay = new UnionPay(ab.getBankNo(), user.getRealName(), user.getCardId(), ab.getMobile(), 
						(long)(BigDecimalUtil.mul(BigDecimalUtil.sub(cash.getMoney(), 0),100)), 2, "cash" + cash.getMoney());
				pay.setOrderNo(cash.getOrderNo());
				UnionPayRet ret = SignHelper.paymentNoBind(pay);
				if(ret == null) {
					throw new AccountException("银联暂无响应，请稍后重试",2);
				}
				if("0000".equals(ret.getRetCode())){//调用成功
					cash = accountCashDao.findObjByProperty("orderNo", ret.getOrderNo());
					if(ret.getOrderStatus() == 2){//处理成功
						cash.setStatus(1);
						cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
						cash.setRemark("提现成功，实际到账金额:" + cash.getCredited() + "元。");
						accountCashDao.update(cash);
						Global.setTransfer("cash", cash);
						Global.setTransfer("user", cash.getUser());
						AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
						executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
					}else if(ret.getOrderStatus() == 3){//处理失败
						throw new AccountException("银联处理失败:" + ret.getRetDesc(),2);
					}else if(ret.getOrderStatus() == 1){//银联处理中
						cash.setStatus(5);
						cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
						cash.setRemark("银联处理中");
						accountCashDao.update(cash);
						//TODO 启动一个定时线程去查询银联处理结果
						selectYLCashResult(cash);
					}
				}else{
					throw new AccountException("银联处理失败:" + ret.getRetDesc(),2);
				}
			}
			OperationLog operationLog = new OperationLog(cash.getUser(), operator, Constant.CASH_SUCCESS);
			operationLog.setOperationResult("（IP:" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
					+ "的操作员审核后台提现" + cash.getCredited() + "元成功");
			operationLogDao.save(operationLog);
		} else if (model.getStatus() == 2) {
			if(cash.getStatus() != 7) {
				throw new AccountException("提现状态不正确，请查正后重新操作");
			}
			cash.setStatus(2);
			cash.setCredited(0);
			accountCashDao.update(cash);
			Global.setTransfer("cash", cash);
			Global.setTransfer("user", cash.getUser());
			AbstractExecuter executer = ExecuterHelper.doExecuter("cashFailExecuter");
			executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
		} else {//撤回
			if(cash.getStatus() != 6 && cash.getStatus() != 7) {
				throw new AccountException("提现状态不正确，请查正后重新操作");
			}
			cash.setStatus(0);
			accountCashDao.update(cash);
		}
		verifyLogDao.save(verifyLog);
	}
	//存放请求查询提现结果线程信息
	HashMap<Object, Thread> cashThreadsMap = new HashMap<Object, Thread>();
	/**
	 * 查询银联提现处理结果线程
	 * @param cash
	 */
	public void selectYLCashResult (final AccountCash cash){
		Thread t = new Thread() {
			@SuppressWarnings("deprecation")
			public void run() {
				try {
					//TODO 请求查询银联提现的结果
					boolean bool = true;
			        while(bool){
						UnionPayRet ret = SignHelper.queryOrder(cash.getOrderNo());
						int orderStatus = cash.getStatus();
						logger.info("================银联提现前==订单状态："+orderStatus);
						if (ret.getOrderStatus() == 2){//处理成功
							logger.info("============银联提现处理成功，订单号："+cash.getOrderNo());
							if(orderStatus!=1){				
								logger.info("============银联提现处理成功，进行成功提现操作");
								cash.setStatus(1);
								cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
								cash.setRemark("提现成功，实际到账金额:" + cash.getCredited() + "元。");
								accountCashDao.update(cash);
								Global.setTransfer("cash", cash);
								Global.setTransfer("user", cash.getUser());
								AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
								executer.execute(cash.getMoney(), cash.getUser(), new Operator(1L), new User(1));
							}
							bool = false;
							this.stop();
						} else if(ret.getOrderStatus() == 3){//处理失败
							logger.info("============银联提现处理失败，订单号："+cash.getOrderNo());
							if(orderStatus!=2){
								logger.info("============银联提现处理失败，进行失败提现操作");
								//处理失败，解冻资金
								cash.setStatus(2);
								cash.setRemark("银联处理失败");
								accountCashDao.update(cash);
								Global.setTransfer("cash", cash);
								Global.setTransfer("user", cash.getUser());
								AbstractExecuter executer = ExecuterHelper.doExecuter("cashFailExecuter");
								executer.execute(cash.getMoney(), cash.getUser(), new Operator(1L), new User(1));
							}
							bool = false;
							this.stop();
						} else if(ret.getOrderStatus() == 1){//银联处理中
							logger.info("============银联提现处理中，订单号："+cash.getOrderNo());
							Thread.sleep(60*1000);//间隔1分钟后执行
						}
			        }
				} catch (Exception e) {
					this.stop();
				}
			}
		};
		t.start();
		boolean b = cashThreadsMap.containsKey(cash.getOrderNo());
		if(b){
			cashThreadsMap.get(cash.getOrderNo()).stop();
			cashThreadsMap.remove(cash.getOrderNo());
		}
		cashThreadsMap.put(cash.getOrderNo(),t);
	}
	
	public void verifyOffLineCash(AccountCashModel model, Operator operator){
		AccountCash cash = accountCashDao.find(model.getId());
		VerifyLog verifyLog = new VerifyLog(operator, "accountCash", cash.getId(), 2, model.getStatus(), model.getRemark());
		Global.setTransfer("cash", cash);
		if (model.getStatus() == 1) { // 审核通过
			if(cash.getStatus() != 6) {
				throw new AccountException("提现状态不正确，请查正后重新操作");
			}
			if(cash.getTransferType()!=null&&cash.getTransferType()==1)//线下充值
			{
				cash.setStatus(1);
				cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
				cash.setRemark("提现成功，实际到账金额:" + cash.getCredited() + "元。");
				accountCashDao.update(cash);
				Global.setTransfer("cash", cash);
				Global.setTransfer("user", cash.getUser());
				AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
				executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
				
				UserIdentify identify = userIdentifyDaoImpl.findByUserId(cash.getUser().getUserId());
				if(identify.getRealNameStatus()!=1){	
					User user = theUserService.getByUserId(cash.getUser().getUserId());
					user.setRealName(cash.getRealName());
					user.setCardId(cash.getCardId());
					theUserService.updateUser(user);
					
					identify.setRealNameStatus(1);
					identify.setRealNameVerifyTime(new Date());
					userIdentifyDaoImpl.update(identify);
				}
			}
		}else if(model.getStatus() == 2)//不通过
		{
			if(cash.getStatus() == 7)//财务初审不通过  都不通过  归还冻结金额
			{
				cash.setStatus(2);
				accountCashDao.update(cash);
				Global.setTransfer("cash", cash);
				Global.setTransfer("user", cash.getUser());
				AbstractExecuter executer = ExecuterHelper.doExecuter("cashFailExecuter");
				executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
			}else
			{
				throw new AccountException("提现状态不正确，请查正后重新操作");
			}
		}else if(model.getStatus() == 3)//撤回
		{
			if(cash.getStatus() == 7 || cash.getStatus() == 6)//财务初审不通过  退回初审
			{
				cash.setStatus(0);
				accountCashDao.update(cash);
			}else
			{
				throw new AccountException("提现状态不正确，请查正后重新操作");
			}
		}
		verifyLogDao.save(verifyLog);
	}
	
	@Override
    public void verifyLLCash(AccountCashModel model, Operator operator){
		AccountCash cash = accountCashDao.find(model.getId());
		VerifyLog verifyLog = new VerifyLog(operator, "accountCash", cash.getId(), 2, model.getStatus(), model.getRemark());
		Global.setTransfer("cash", cash);
		if (model.getStatus() == 1) { // 审核通过
			if(cash.getStatus() != 6) {
				throw new AccountException("提现状态不正确，请查正后重新操作");
			}
			if(cash.getTransferType()!=null&&cash.getTransferType()==1)//线下充值
			{
				cash.setStatus(1);
				cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
				cash.setRemark("提现成功，实际到账金额:" + cash.getCredited() + "元。");
				accountCashDao.update(cash);
				Global.setTransfer("cash", cash);
				Global.setTransfer("user", cash.getUser());
				AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
				executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
			}
			else
			{
				cash.setStatus(1);
				checkCash(cash);
				User user = cash.getUser();
//				cash.setOrderNo(OrderNoUtils.getSerialNumber());
				accountCashDao.update(cash);
				//连连处理
				AccountBank ab = accountBankDao.find(cash.getUser().getUserId(), cash.getBankNo(),channelKey.llpay.getValue());
				if(ab == null) {
					throw new AccountException("用户提现银行卡已解绑", 2);
				}
				PartnerConfig pc = new PartnerConfig();
				LianlPay llpay = new LianlPay(ab.getBankNo(), user.getRealName(), pc.getOidPartner(), "RSA", ab.getBankCode(), pc.getNotifyCUrl(),ab.getProvince(),ab.getCity(),ab.getBranch());
				//根据订单号查询信息
		        OrderInfo order = orderService.loadOrderByNo(cash.getOrderNo());
		        try {
					WithdrawBean withdrawInfo = Withdraw.withdraw(null,llpay,order);
					String cashRet = LianlPayHelper.llcash(withdrawInfo);
					HashMap<String, String> cashMap = CommonRealize.toHashMap(cashRet);//提现返回参数
					if(cashRet == null) {
						logger.info("连连暂无响应，请稍后重试");
						throw new AccountException("连连暂无响应，请稍后重试",2);
					}else{
						String retCode = cashMap.get("ret_code");//cashRet.getRet_code()
					    String retMsg = cashMap.get("ret_msg");//cashRet.getRet_msg()
						if("0000".equals(retCode)){
								//业务处理成功
								order.setStatus(1);//更改订单状态为成功
								logger.info("=======================================提现成功==="+retMsg);
								cash.setStatus(1);
								cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
								cash.setRemark("提现成功，实际到账金额:" + cash.getCredited() + "元。");
								accountCashDao.update(cash);
								Global.setTransfer("cash", cash);
								Global.setTransfer("user", cash.getUser());
								AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
								executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
						}else{
							order.setStatus(2);//更改订单状态为失败
							logger.info("=======================================提现失败==="+retMsg);
							throw new AccountException("连连处理失败:" + retMsg,2);
						}
						orderService.updateOrder(order);//更新订单状态
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			OperationLog operationLog = new OperationLog(cash.getUser(), operator, Constant.CASH_PROCESS);
			operationLog.setOrderNo(cash.getOrderNo());
			operationLog.setOperationResult("（IP:" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
					+ "的操作员审核后台提现" + cash.getCredited() + "元成功");
			operationLogDao.save(operationLog);
		} else if (model.getStatus() == 2) {
			if(cash.getStatus() != 7) {
				throw new AccountException("提现状态不正确，请查正后重新操作");
			}
			cash.setStatus(2);
			cash.setCredited(0);
			accountCashDao.update(cash);
			Global.setTransfer("cash", cash);
			Global.setTransfer("user", cash.getUser());
			AbstractExecuter executer = ExecuterHelper.doExecuter("cashFailExecuter");
			executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
		} else {//撤回
			if(cash.getStatus() != 6 && cash.getStatus() != 7) {
				throw new AccountException("提现状态不正确，请查正后重新操作");
			}
			cash.setStatus(0);
			accountCashDao.update(cash);
		}
		verifyLogDao.save(verifyLog);
	}
	
	public void operllCash(User user,int type,PayDataBean payDataBean){
		AccountCash cash = accountCashDao.getCashInfo(payDataBean.getNo_order());
		OperationLog operationLog = operationLogDao.getOperationLogInfo(payDataBean.getNo_order());
		if(type<2){//失败
			//完成之后更改OperationLog里面的状态为失败
			operationLog.setType(Constant.CASH_FAIL);
			operationLogDao.update(operationLog);
			//更改提现记录，解冻资金
			cash.setStatus(2);
			cash.setCredited(0);
			accountCashDao.update(cash);
			Global.setTransfer("cash", cash);
			Global.setTransfer("user", cash.getUser());
			AbstractExecuter executer = ExecuterHelper.doExecuter("cashFailExecuter");
			executer.execute(cash.getMoney(), cash.getUser().getUserId(), operationLog.getVerifyUser());
		}else{//成功
			logger.info("=======================================提现成功===");
			cash.setStatus(1);
			cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
			cash.setRemark("提现成功，实际到账金额:" + cash.getCredited() + "元。");
			accountCashDao.update(cash);
			Global.setTransfer("cash", cash);
			Global.setTransfer("user", cash.getUser());
			AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
			executer.execute(cash.getMoney(), cash.getUser().getUserId(), user);

			//TODO 操作记录表添加订单字段，添加根据订单查询方法，从而获取操作者信息，进行业务处理
			//完成之后更改OperationLog里面的状态为成功
			operationLog.setType(Constant.CASH_SUCCESS);
			operationLogDao.update(operationLog);
		}
	}
	@Override
	public PageDataList<AccountCashModel> list(long userId, int startPage, AccountCashModel model) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("user.userId", userId);
		  if(model != null){
	            if (StringUtil.isNotBlank(model.getStartTime())) {
	                Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
	                param.addParam("addTime", Operators.GTE, start);
	            }
	            Date nowdate = DateUtil.getDate(System.currentTimeMillis()/1000 + "");
	            if (model.getTime() == 7) {
	                param.addParam("addTime", Operators.GTE,DateUtil.rollDay(nowdate, -7));
	                param.addParam("addTime", Operators.LTE, nowdate);
	            } else if (model.getTime() > 0 && model.getTime() < 4){
	                param.addParam("addTime", Operators.GTE,DateUtil.rollMon(nowdate, -model.getTime()));
	                param.addParam("addTime", Operators.LTE, nowdate);
	            }
	            if (StringUtil.isNotBlank(model.getEndTime())) {
	                Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
	                param.addParam("addTime", Operators.LTE, end);
	            }
	            if (model.getStatus() != 9) {
	                param.addParam("status", model.getStatus());
	            }
	        }
		param.addPage(startPage);
		param.addOrder(OrderType.DESC, "addTime");
		PageDataList<AccountCash> pageDateList = accountCashDao.findPageList(param);
		PageDataList<AccountCashModel> pageDateList_ = new PageDataList<AccountCashModel>();
		List<AccountCashModel> list = new ArrayList<AccountCashModel>();
		pageDateList_.setPage(pageDateList.getPage());
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil.getBean("verifyLogDao");
		if (pageDateList.getList().size() > 0) {
			for (int i = 0; i < pageDateList.getList().size(); i++) {
				AccountCash cash = (AccountCash) pageDateList.getList().get(i);
				AccountCashModel acm = AccountCashModel.instance(cash);
				VerifyLog verifyLog = verifyLogDao.findByType(cash.getId(), "accountCash", 1);
				String remark = "";
				if (verifyLog != null && verifyLog.getRemark().length() > 0) {
					String[] r = verifyLog.getRemark().split("（");
					remark = r.length > 1 ? r[0] : "";
				}
				acm.setAddTimeStr(DateUtil.dateStr4(cash.getAddTime()));
				acm.setRemark(remark);
				list.add(acm);
			}
		}
		pageDateList_.setList(list);
		return pageDateList_;
	}

	@Override
	public AccountCashModel getCashMessage(long userId) {
		return accountCashDao.getCashMessage(userId);
	}

	
	
	/**
	 * 提现处理
	 * 
	 * @param cash
	 * @param user
	 * @return
	 */
	public AccountCash accountCashSec(AccountCash cash,User user) {
		double fee = Global.getDouble("cash_fee");
		cash.setCashFeeBear((byte)2);
		if(fee > 0) {
			cash.setCashFeeBear((byte)2);
		} else {
			cash.setCashFeeBear((byte) 1);
		}
		cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), fee)); 
		
		//BUG:#15904手续费大于本金时报错提示
		if(fee >= cash.getMoney())
		{
		    throw new AccountException("提现金额不能小于手续费" + fee+"元","/member/cash/newCash.html");
		}
		cash.setFee(fee);
		if(user!=null){
			cash.setUser(user);
		}
		return cash;
	}

	
	@Override
	public PageDataList<AccountCashModel> accountCashList(AccountCashModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(), model.getRows());
		if (model != null && !StringUtil.isBlank(model.getSearchName())){//模糊条件查询
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}else if(StringUtil.isNotBlank(model.getTimeVal())){
			if ("all".equals(model.getTimeVal())) {//全部
				
			}
			if ("today".equals(model.getTimeVal())) {//今日
				String timeStr = DateUtil.dateStr2(new Date()) + " 17:00:00";
				Date time = DateUtil.valueOf(timeStr);
				param.addParam("addTime", Operators.LTE, time);
			}
			if ("tomorrow".equals(model.getTimeVal())) {//明日
				String startTimeStr = DateUtil.dateStr2(new Date()) + " 17:00:00";
				String endTimeStr = DateUtil.dateStr2(DateUtil.rollDay(new Date(), 1)) + " 17:00:00";
				Date startTime = DateUtil.valueOf(startTimeStr);
				Date endTime = DateUtil.valueOf(endTimeStr);
				param.addParam("addTime", Operators.GT, startTime);
				param.addParam("addTime", Operators.LTE, endTime);
			}
			if (model.getStatus() != 99) {
				param.addParam("status", model.getStatus());
			}
		}else{//精确条件查询
			if (model != null && !StringUtil.isBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if (model != null && !StringUtil.isBlank(model.getRealName())) {
				param.addParam("user.realName", Operators.EQ, model.getRealName());
			}
			if (model != null && model.getStatus() != 99) {
				param.addParam("status", model.getStatus());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime());
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime());
				param.addParam("addTime", Operators.LTE, end);
			}
			if (model.getUser() != null && model.getUser().getUserCache() != null 
					&& model.getUser().getUserCache().getUserType() != 0){
				param.addParam("user.userCache.userType", Operators.EQ, model.getUser().getUserCache().getUserType());
			}
		}
		if(model!=null){
			param.addParam("type", Operators.EQ, model.getType());
		}
		param.addOrder(OrderType.DESC, "id");
		PageDataList<AccountCash> pageDataList = accountCashDao.findPageList(param);
		PageDataList<AccountCashModel> pageDataList_ = new PageDataList<AccountCashModel>();
		List<AccountCashModel> list = new ArrayList<AccountCashModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				AccountCash accountCash = (AccountCash) pageDataList.getList().get(i);
				AccountCashModel abm = AccountCashModel.instance(accountCash);
				VerifyLog verifyLog = verifyLogDao.findByType(accountCash.getId(), "verifyAccountCash", 1);
				if (verifyLog != null) {
					abm.setVerifyUserName(verifyLog.getVerifyUser().getUserName());
				}
				try{
    				abm.setUserName(accountCash.getUser().getUserName());
    			    abm.setRealName(accountCash.getUser().getRealName());
    			    if(accountCash.getType()==1){
    			    	abm.setCashType("线下提现");   
    			    }else{
    			    	abm.setCashType("线上提现");    			    	
    			    }
    				list.add(abm);
				} catch(Exception e){
		            e.printStackTrace();
		            logger.error(e);
				}
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public int count(int status) {
		return accountCashDao.count(status);
	}

	@Override
	public AccountCash find(long id) {
		return accountCashDao.find(id);
	}

	@Override
	public void kfVerifyCash(AccountCash cash, Operator operator) {
		VerifyLog verifyLog = new VerifyLog(operator, "accountCash", cash.getId(), 2, cash.getStatus(),
				"");
		Global.setTransfer("cash", cash);
		if (cash.getStatus() == 1) { // 审核通过
			checkCash(cash);
			AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
			executer.execute(cash.getCredited(), cash.getUser().getUserId(), operator);
			OperationLog operationLog = new OperationLog(cash.getUser(), operator, Constant.CASH_SUCCESS);
			operationLog.setOperationResult("（IP:" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
					+ "的操作员审核后台提现" + cash.getCredited() + "元成功");
			operationLogDao.save(operationLog);
			verifyLog.setRemark(operator.getRemark() + "（" + operator.getUserName() + "操作员提现审核通过【ID：" + cash.getId()
					+ "】）");
		} else {
			AbstractExecuter executer = ExecuterHelper.doExecuter("cashFailExecuter");
			executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
			verifyLog.setRemark(operator.getRemark()+"（"+operator.getUserName() + "操作员提现审核 不通过【ID：）" + cash.getId()+"】）");
		}
		accountCashDao.save(cash);
		verifyLogDao.save(verifyLog);
	}

	/**
	 * 验证提现是否可审核通过（可用余额+待收总额-净值标待还本息）
	 * 
	 * @param cash
	 * @return
	 */
	private void checkCash(AccountCash cash) {
		double repayTotalWithJin = borrowDao.getRepayTotalWithJin(cash.getUser().getUserId());
		Account actount = accountDao.findObjByProperty("user.userId", cash.getUser().getUserId());
		double totalMoney = BigDecimalUtil.add(actount.getUseMoney(), actount.getNoUseMoney());
		if (BigDecimalUtil.sub(totalMoney, repayTotalWithJin) <= 0) {
			throw new AccountException("审核失败！用户资金情况不满足提现要求！", 1);
		}
		if(actount.getTotal()<cash.getMoney())
		{
			throw new AccountException("账户总额有误！", 1);
		}
			
		if(actount.getNoUseMoney() < cash.getMoney()  ){
			throw new AccountException("账户冻结金额有误！", 1);
		}
	}

	@Override
	public void cwVerifyCash(AccountCash cash, Operator operator) {
		VerifyLog verifyLog = new VerifyLog(operator, "verifyAccountCash", cash.getId(), 1, cash.getStatus(), "");
		Global.setTransfer("cash", cash);
		if (cash.getStatus() == 1) { // 审核通过
			checkCash(cash);
			AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
			executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
			verifyLog.setRemark(operator.getRemark() + "（" + operator.getUserName() + "操作员提现审核通过【ID：" + cash.getId()
					+ "】）");
		} else {
			AbstractExecuter executer = ExecuterHelper.doExecuter("cashFailExecuter");
			executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
			verifyLog.setRemark(operator.getRemark() + "（" + operator.getUserName() + "操作员提现审核失败【ID：" + cash.getId()
					+ "】）");
		}
		verifyLogDao.save(verifyLog);
		/* accountCashDao.save(cash); */
	}
	@Override
    public void setAccountCash(){
	    try {
            Date d=new Date();
            long now=d.getTime();
            int dayTime=2*24*3600*1000;
            List<AccountCash> list=accountCashDao.findAll();
            for (AccountCash ac:list) {
                if((now-ac.getAddTime().getTime())>= dayTime && ac.getStatus()==0){
                    accountCashDao.updateStatus(ac.getId(), 2);;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
	}
	
	/**
	 * 提现成功或者失败的处理
	 */
	@Override
	public void cashCallBack(CashModel cashModel) {
		logger.info("---提现业务处理开始1----");
		QueryParam param=new QueryParam();
		param.addParam("orderNo", cashModel.getOrderId());
		AccountCash cash = accountCashDao.findByCriteriaForUnique(param);
		if(cash.getStatus()!=1){
			@SuppressWarnings("unused")
			byte status = 0;
			if(cashModel.isResult()){
				// 实际到账金额
				double actulMoney = StringUtil.toDouble(cashModel.getOrderAmount());
				// 手续费
				double fee = cashModel.getFeeAmt();
				// 账户变动金额
				double totleMoney = BigDecimalUtil.add(actulMoney, fee);
				// 手续费>0的情况
				if(fee > 0){
					if("M".equals(cashModel.getPayModel())){//汇付支付商户垫付手续费
						cash.setFee(fee);
						totleMoney = actulMoney;//商户垫付手续费以后本金不累加
						cash.setCashFeeBear((byte) 1);//平台垫付
					}else{
						// 账户可用金额
						double useMoney = accountDao.getAccountUseMoney(cashModel.getUserId());
						// 账户不足手续费的情况下，从提现金额中扣除手续费
						if(BigDecimalUtil.add(StringUtil.toDouble(cashModel.getOrderAmount()), fee) > useMoney){
							totleMoney = actulMoney;
							actulMoney = BigDecimalUtil.sub(actulMoney, fee);
						}
						cash.setFee(fee);
						cash.setCredited(actulMoney);
						cash.setCashFeeBear((byte) 2);//用户垫付
					}
				}
				//提现成功 扣除总金额、冻结金额  记录日志
				cash.setStatus(1);
				cash.setBankNo(cashModel.getCardNo());
				Global.setTransfer("cash", cash);
				AbstractExecuter executer = ExecuterHelper.doExecuter("cashNotifySuccessExecuter");
				executer.execute(totleMoney, cash.getUser());
				status = TppIpsPay.STATUS_SUCCESS;
			}else{
				logger.info("---提现业务处理fail----");
				//提现失败 修改资金表的冻结和可用，记录日志
				cash.setStatus(3);
				Global.setTransfer("cash", cash);
				AbstractExecuter executer = ExecuterHelper.doExecuter("cashNotifyFailExecuter");
				executer.execute(cash.getMoney(), cash.getUser());
				status = TppIpsPay.STATUS_FAIL;
			}		
		}
		
		logger.info("---提现业务处理结束2----");
		
	}
	
	public void doWebPayMoney(ChinapnrWebPayModel payModel,List<Object> taskList) {
		TppPnrPay cppModel = new TppPnrPay(ChinaPnrType.TRANSFER, payModel.getMoney(), "1",
				String.valueOf(payModel.getPayUser().getUserId()), "0", null, ChinaPnrType.WEBPAY);
		// TODO zjj
		// cppModel.setPayUserCustId(payModel.getPayUser().getApiId());
		cppModel.setUsrCustId("");
		taskList.add(cppModel);
	}
	
	@Override
	public double allCashMomeny(String startTime, String endTime) {
	    return accountCashDao.allCashMomeny(startTime, endTime);
	}
	
	@Override
	public void updateStatusByOrderNo(String orderNo, int status, byte ipsStatus) {
	    
        QueryParam param = QueryParam.getInstance();
        param.addParam("orderNo", orderNo);
	    AccountCash accountCash = accountCashDao.findByCriteriaForUnique(param);
        if(accountCash != null && accountCash.getStatus() != 1){
            accountCash.setStatus(status);
            accountCashDao.update(accountCash);
        }
	}

	@Override
	public int countMonth(long userId) {
		return accountCashDao.countMonth(userId);
	}

	@Override
	public void doCashWait() {
		QueryParam param = QueryParam.getInstance().addParam("status", 5);
		List<AccountCash> list = accountCashDao.findByCriteria(param);
		if(list != null && list.size() > 0){
			for (AccountCash cash : list) {
				UnionPayRet ret = SignHelper.queryOrder(cash.getOrderNo());
				if (ret.getOrderStatus() == 2){
					cash.setStatus(1);
					cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
					cash.setRemark("提现成功，实际到账金额:" + cash.getCredited() + "元。");
					accountCashDao.update(cash);
					Global.setTransfer("cash", cash);
					Global.setTransfer("user", cash.getUser());
					AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
					executer.execute(cash.getMoney(), cash.getUser(), new Operator(1L), new User(1));
				} else if(ret.getOrderStatus() == 3){//处理失败
					//处理失败，解冻资金
					cash.setStatus(2);
					cash.setRemark("银联处理失败");
					accountCashDao.update(cash);
					Global.setTransfer("cash", cash);
					Global.setTransfer("user", cash.getUser());
					AbstractExecuter executer = ExecuterHelper.doExecuter("cashFailExecuter");
					executer.execute(cash.getMoney(), cash.getUser(), new Operator(1L), new User(1));
				} else if(ret.getOrderStatus() == 1){//银联处理中
					cash.setStatus(5);
					cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
					cash.setRemark("银联处理中");
					accountCashDao.update(cash);
				}
			}
		}
	}

	@Override
	public void verifyCashStatus(AccountCashModel model, Operator operator) {
		//获取对应提现记录
		AccountCash cash = accountCashDao.find(model.getId());
		
		if(cash.getStatus() != 0) {
			throw new AccountException("提现状态不正确，请查正后重新操作");
		}
		
		//封装审核日志
		VerifyLog verifyLog = new VerifyLog(operator, "accountCash", cash.getId(), 1, model.getStatus(),model.getRemark());
		
		if (model.getStatus() == 1) { // 初审通过
			cash.setStatus(6);
			verifyLog.setResult(6);
			OperationLog operationLog = new OperationLog(cash.getUser(), operator, Constant.CASH_SUCCESS);
			operationLog.setOperationResult("（IP:" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
					+ "的操作员初审通过");
			operationLogDao.save(operationLog);
		} else {//初审不通过
			cash.setStatus(7);
			verifyLog.setResult(7);
			OperationLog operationLog = new OperationLog(cash.getUser(), operator, Constant.CASH_FAIL);
			operationLog.setOperationResult("（IP:" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
					+ "的操作员初审不通过");
			operationLogDao.save(operationLog);
		}
		accountCashDao.update(cash);
		verifyLogDao.save(verifyLog);
	}

	@Override
	public PageDataList<AccountCashModel> accountCashVerifyFullList(AccountCashModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(), model.getRows());
		if (model != null && !StringUtil.isBlank(model.getSearchName())){//模糊条件查询
			SearchFilter orFilter1 = new SearchFilter("user.userName", Operators.LIKE, model.getSearchName());
    		SearchFilter orFilter2 = new SearchFilter("user.realName", Operators.LIKE, model.getSearchName());
    		param.addOrFilter(orFilter1,orFilter2);
		}else{//精确条件查询
			if (model != null && !StringUtil.isBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ, model.getUserName());
			}
			if (model != null && !StringUtil.isBlank(model.getRealName())) {
				param.addParam("user.realName", Operators.EQ, model.getRealName());
			}
			SearchFilter orFilter3 = new SearchFilter("status", Operators.EQ, 6);
    		SearchFilter orFilter4 = new SearchFilter("status", Operators.EQ, 7);
    		param.addOrFilter(orFilter3,orFilter4);
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime());
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime());
				param.addParam("addTime", Operators.LTE, end);
			}
			if (model.getUser() != null && model.getUser().getUserCache() != null 
					&& model.getUser().getUserCache().getUserType() != 0){
				param.addParam("user.userCache.userType", Operators.EQ, model.getUser().getUserCache().getUserType());
			}
		}
		if(model!=null){
			param.addParam("type", Operators.EQ, model.getType());
		}
		param.addOrder(OrderType.DESC, "id");
		PageDataList<AccountCash> pageDataList = accountCashDao.findPageList(param);
		PageDataList<AccountCashModel> pageDataList_ = new PageDataList<AccountCashModel>();
		List<AccountCashModel> list = new ArrayList<AccountCashModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				AccountCash accountCash = (AccountCash) pageDataList.getList().get(i);
				AccountCashModel abm = AccountCashModel.instance(accountCash);
				VerifyLog verifyLog = verifyLogDao.findByType(accountCash.getId(), "verifyAccountCash", 1);
				if (verifyLog != null) {
					abm.setVerifyUserName(verifyLog.getVerifyUser().getUserName());
				}
				try{
    				abm.setUserName(accountCash.getUser().getUserName());
    				abm.setRealName(accountCash.getUser().getRealName());
    				list.add(abm);
				} catch(Exception e){
		            e.printStackTrace();
		            logger.error(e);
				}
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public List<AccountCash> list(String ids) {
		String[] id = ids.split(",");
		List<AccountCash> list = new ArrayList<AccountCash>();
		if(id != null && id.length > 0) {
			for (int i = 0; i < id.length; i++) {
				long cid = NumberUtil.getLong(id[i]);
				AccountCash cash = accountCashDao.find(cid);
				cash.getUser();
				list.add(cash);
			}
			return list;
		}
		return null;
	}

	@Override
	public Map<String, Object> verifyCashBatchStatus(AccountCashModel model, Operator operator, List<AccountCash> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AccountCashBatchModel> batchList = new ArrayList<AccountCashBatchModel>();
		int errorCount = 0;
		for (int i = 0; i < list.size(); i++) {
			try{
				AccountCash cash = accountCashDao.find(list.get(i).getId());
				if (cash.getStatus() != 0) {
					throw new AccountException("提现状态错误");
				}
				//封装审核日志
				VerifyLog verifyLog = new VerifyLog(operator, "accountCash", cash.getId(), 1, model.getStatus(),model.getRemark());
				
				if (model.getStatus() == 1) { // 初审通过
					cash.setStatus(6);
					verifyLog.setResult(6);
					OperationLog operationLog = new OperationLog(cash.getUser(), operator, Constant.CASH_SUCCESS);
					operationLog.setOperationResult("（IP:" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
							+ "的操作员初审通过");
					operationLogDao.save(operationLog);
				} else {//初审不通过
					cash.setStatus(7);
					verifyLog.setResult(7);
					OperationLog operationLog = new OperationLog(cash.getUser(), operator, Constant.CASH_FAIL);
					operationLog.setOperationResult("（IP:" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
							+ "的操作员初审不通过");
					operationLogDao.save(operationLog);
				}
				accountCashDao.update(cash);
				verifyLogDao.save(verifyLog);
			} catch (Exception e) {
				AccountCash cash = list.get(i);
				AccountCashBatchModel batchModel = new AccountCashBatchModel(cash.getUser().getUserName(), cash.getOrderNo(), e.getMessage());
				batchList.add(batchModel);
				errorCount ++;
			}
		}
		map.put("count", list.size());
		map.put("errorCount", errorCount);
		map.put("batchList", batchList);
		return map;
	}

	@Override
	public Map<String, Object> verifyCashBatchReview(AccountCashModel model, Operator operator, List<AccountCash> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<AccountCashBatchModel> batchList = new ArrayList<AccountCashBatchModel>();
		int errorCount = 0;
		for (int i = 0; i < list.size(); i++) {
			try{
				AccountCash cash = accountCashDao.find(list.get(i).getId());
				VerifyLog verifyLog = new VerifyLog(operator, "accountCash", cash.getId(), 2, model.getStatus(), model.getRemark());
				Global.setTransfer("cash", cash);
				if (model.getStatus() == 1) { // 审核通过
					if(cash.getStatus() != 6) {
						throw new AccountException("提现状态不正确，请查正后重新操作");
					}
					cash.setStatus(1);
					checkCash(cash);
					User user = cash.getUser();
					//银联处理
					AccountBank ab = accountBankDao.find(cash.getUser().getUserId(), cash.getBankNo(),ConstantUtil.channelKey.unionpay.getValue());
					if(ab == null) {
						ab = accountBankDao.find(cash.getUser().getUserId(), cash.getBankNo(),ConstantUtil.channelKey.llpay.getValue());
						if(ab == null) {
							throw new AccountException("用户提现银行卡已解绑");
						}
					}
					UnionPay pay = new UnionPay(ab.getBankNo(), user.getRealName(), user.getCardId(), ab.getMobile(), 
							(long)(BigDecimalUtil.mul(BigDecimalUtil.sub(cash.getMoney(), 0),100)), 2, "cash" + cash.getMoney());
					pay.setOrderNo(cash.getOrderNo());
					UnionPayRet ret = SignHelper.paymentNoBind(pay);
					if(ret == null) {
						throw new AccountException("银联暂无响应，请稍后重试");
					}
					if("0000".equals(ret.getRetCode())){//调用成功
						cash = accountCashDao.findObjByProperty("orderNo", ret.getOrderNo());
						if(ret.getOrderStatus() == 2){//处理成功
							cash.setStatus(1);
							cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
							cash.setRemark("提现成功，实际到账金额:" + cash.getCredited() + "元。");
							accountCashDao.update(cash);
							Global.setTransfer("cash", cash);
							Global.setTransfer("user", cash.getUser());
							AbstractExecuter executer = ExecuterHelper.doExecuter("cashSuccessExecuter");
							executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
						}else if(ret.getOrderStatus() == 3){//处理失败
							throw new AccountException("银联处理失败:" + ret.getRetDesc());
						}else if(ret.getOrderStatus() == 1){//银联处理中
							cash.setStatus(5);
							cash.setCredited(BigDecimalUtil.sub(cash.getMoney(), cash.getFee()));
							cash.setRemark("银联处理中");
							accountCashDao.update(cash);
						}
					}else{
						cash.setStatus(6);
						throw new AccountException("银联处理失败:" + ret.getRetDesc());
					}
					OperationLog operationLog = new OperationLog(cash.getUser(), operator, Constant.CASH_SUCCESS);
					operationLog.setOperationResult("（IP:" + operator.getLoginIp() + "）用户名为" + operator.getUserName()
							+ "的操作员审核后台提现" + cash.getCredited() + "元成功");
					operationLogDao.save(operationLog);
				} else if (model.getStatus() == 2) {
					if(cash.getStatus() != 7) {
						throw new AccountException("提现状态不正确，请查正后重新操作");
					}
					cash.setStatus(2);
					cash.setCredited(0);
					accountCashDao.update(cash);
					Global.setTransfer("cash", cash);
					Global.setTransfer("user", cash.getUser());
					AbstractExecuter executer = ExecuterHelper.doExecuter("cashFailExecuter");
					executer.execute(cash.getMoney(), cash.getUser().getUserId(), operator);
				} else {//撤回
					if(cash.getStatus() != 6 && cash.getStatus() != 7) {
						throw new AccountException("提现状态不正确，请查正后重新操作");
					}
					cash.setStatus(0);
					accountCashDao.update(cash);
				}
				verifyLogDao.save(verifyLog);
			}catch(Exception e) {
				AccountCash cash = list.get(i);
				cash.setStatus(0);
				accountCashDao.update(cash);
				AccountCashBatchModel batchModel = new AccountCashBatchModel(cash.getUser().getUserName(), cash.getOrderNo(), e.getMessage());
				batchList.add(batchModel);
				errorCount ++;
			}
		}
		map.put("count", list.size());
		map.put("errorCount", errorCount);
		map.put("batchList", batchList);
		return map;
	}

	@Override
	public double getAccountCashSumByDate(String date) {
		return accountCashDao.getAccountCashSumByDate(date);
	}
	
	@Override
	public String cashhandle(HttpServletRequest request,String bankNo, String userName, String bankcode,
			String money,String province,String city,String brabank) {
		try {
			PartnerConfig pc = new PartnerConfig();
			LianlPay llpay = new LianlPay(bankNo, userName, pc.getOidPartner(), "RSA", bankcode, pc.getUrlReturn(),province,city,brabank);
			 // 创建订单
			OrderInfo order = LianlPayHelper.createOrder(1,"提现",money);
			WithdrawBean withdrawInfo = Withdraw.withdraw(request,llpay,order);
			String ret = LianlPayHelper.llcash(withdrawInfo);
			logger.info(ret);
			return ret;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addCash(HttpServletRequest request,User user,AccountBank bank,String money) {
		// TODO Auto-generated method stub
		AccountCash cash = new AccountCash();
		String bankname = "";
		if(bank!=null && bank.getBank()!=null){
			bankname = bank.getBank();
		}
        cash.setStatus(0);
        cash.setUser(user);
        cash.setBankNo(bank.getBankNo());
        cash.setBank(bankname);
		cash.setCredited(Double.parseDouble(money));
		cash.setMoney(Double.parseDouble(money));
		cash.setAddIp(LLPayUtil.getIpAddr(request));
		cash.setAddTime(new Date());
		cash.setRemark("提现成功，实际到账金额:" + Double.parseDouble(money) + "元。");
		cash.setChannelKey(channelKey.llpay.getValue());
		accountCashDao.update(cash);
		//保存记录到资金基本表
		Global.setTransfer("cash", cash);
		Global.setTransfer("user", cash.getUser());
		AbstractExecuter executer = ExecuterHelper.doExecuter("cashApplyExecuter");
		executer.execute(cash.getMoney(), cash.getUser());
	}

	@Override
	public int getTodayCashCountByUserId(long userId) {
		return accountCashDao.getTodayCashCountByUserId(userId);
	}	
	
	/***
	 * 银联通道操作
	 * 私密方法（慎用）
	 * 对单独用户打/扣款
	 * 2015-11-18 cgw
	 */
	public String oneToCash(User user,AccountBank ab,double money){
		String msg = "";
		//银联处理
//独立对用户打款-——-开始		
//		UnionPay pay = new UnionPay(ab.getBankNo(), user.getRealName(), user.getCardId(), ab.getMobile(), 
//				(long)(BigDecimalUtil.mul(BigDecimalUtil.sub(money, 0),100)), 2, "cash" + money);
//		pay.setOrderNo(OrderNoUtils.getSerialNumber());
//		UnionPayRet ret = SignHelper.paymentNoBind(pay);
//独立对用户打款-——-结束		
		
//独立对用户扣款-——-开始
		long mon = (long)(BigDecimalUtil.mul(BigDecimalUtil.sub(money, 0),100));
		UnionPay pay = new UnionPay(ab.getBindId(), mon, "RMB" + money);
		UnionPayRet ret = null;//SignHelper.singlePay(pay);
//独立对用户扣款-——-结束
		
		if(ret == null) {
			System.out.println("==================银联暂无响应，请稍后重试");
			msg = "银联暂无响应，请稍后重试";
		}
		if("0000".equals(ret.getRetCode())){//调用成功
			if(ret.getOrderStatus() == 2){//处理成功
				System.out.println("==================处理成功");
				msg = "处理成功";
			}else if(ret.getOrderStatus() == 3){//处理失败
				System.out.println("==================银联处理失败:" + ret.getRetDesc());
				msg = "银联处理失败:" + ret.getRetDesc();
			}else if(ret.getOrderStatus() == 1){//银联处理中
				System.out.println("==================银联处理中");
				msg = "银联处理中";
			}
		}else{
			System.out.println("==================银联处理失败:" + ret.getRetDesc());
			msg = "银联处理失败:" + ret.getRetDesc();
		}
		return msg;
	}
}
