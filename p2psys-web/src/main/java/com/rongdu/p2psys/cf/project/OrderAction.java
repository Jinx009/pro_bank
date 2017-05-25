package com.rongdu.p2psys.cf.project;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.domain.VirtualAccount;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProfitRuleService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.crowdfunding.service.VirtualAccountService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 众筹订单记录
 * @author Jinx
 *
 */
public class OrderAction extends BaseAction<OrderModel> implements ModelDriven<OrderModel> {

	@Resource
	private OrderService cfOrderService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private AccountService theAccountServie;
	@Resource
	private ProfitRuleService profitRuleService;
	@Resource
	private VirtualAccountService virtualAccountService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private NoticeService noticeService;
	
	private Map<String,Object> data;
	
	/**
	 * 收货地址
	 * @return
	 */
	@Action(value = "/cf/user/order-address",results={@Result(name="order-address",type="ftl",location="/nb/cf/pro/address.html")})
	public String address(){
		Integer id = paramInt("id");
		InvestOrder investOrder = cfOrderService.find(id);
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(investOrder.getProjectBaseinfo().getId());
		request.setAttribute("projectBaseinfo",projectBaseinfo);
		request.setAttribute("orderId",id);
		return "order-address";
	}
	
	/**
	 * 订单详情
	 * @throws IOException
	 */
	@Action(value = "/cf/user/orderDetail")
	public void orderDetail() throws IOException{
		Integer id = paramInt("id");
		InvestOrder order = cfOrderService.find(id);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,order);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 跳转购买页面
	 * @return
	 */
	@Action(value="/cf/user/buy",results={@Result(name="buy",type="ftl",location="/nb/cf/pro/buy.html")})
	public String buy(){
		Long id = paramLong("id");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(id);
		request.setAttribute("projectBaseinfo",projectBaseinfo);
		request.setAttribute("profit",paramString("profit"));
		User user = getNBSessionUser();
		String payStatus = "";
		if(null!=user){
			payStatus = StringUtil.isNull(user.getPayPwd());
		}
		Account account = theAccountService.getAccountByUserId(user.getUserId());
		VirtualAccount virtualAccount = virtualAccountService.findByUserId(user.getUserId());
		if(null!=virtualAccount){
			request.setAttribute("virtual",virtualAccount.getAccount());
		}else{
			request.setAttribute("virtual",0);
		}
		request.setAttribute("account",account.getUseMoney());
		request.setAttribute("payStatus",payStatus);
		return "buy";
	}
	
	/**
	 * 支付剩余款项
	 * @throws IOException
	 */
	@Action(value = "/cf/user/payAll")
	public void payAll() throws IOException{
		Integer id = paramInt("id");
		String payPwd = paramString("payPwd");
		User user  = getNBSessionUser();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		if(null!=user){
			if(!MD5.encode(payPwd).equals(user.getPayPwd())){
				data.put(ConstantUtil.ERRORMSG,"支付密码不正确!");
			}else{
				String result = cfOrderService.payAll(id);
				if(!ConstantUtil.SUCCESS.equals(result)){
					data.put(ConstantUtil.ERRORMSG,result);
				}else{
					cfOrderService.payAll(id);
					data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
					data.put(ConstantUtil.ERRORMSG,"支付成功!");
				}
			}
		}else{
			data.put(ConstantUtil.ERRORMSG,"用户登录失效!");
		}
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 取消预约
	 * @throws IOException
	 */
	@Action(value = "/cf/user/caclePay")
	public void caclePay() throws IOException{
		Integer id = paramInt("id");
		String payPwd = paramString("payPwd");
		User user  = getNBSessionUser();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		if(null!=user){
			if(!MD5.encode(payPwd).equals(user.getPayPwd())){
				data.put(ConstantUtil.ERRORMSG,"支付密码不正确!");
			}else{
				String result = cfOrderService.caclePay(id);
				if(!ConstantUtil.SUCCESS.equals(result)){
					data.put(ConstantUtil.ERRORMSG,result);
				}else{
					cfOrderService.payAll(id);
					data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
					data.put(ConstantUtil.ERRORMSG,"取消成功!");
				}
			}
		}else{
			data.put(ConstantUtil.ERRORMSG,"用户登录失效!");
		}
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 产品购买记录
	 * @throws IOException
	 */
	@Action(value = "/pro/buyList")
	public void orderList() throws IOException{
		Long projectId = paramLong("id");
		List<OrderModel> list = cfOrderService.getList(projectId);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 购买产品列表
	 * @return
	 */
	@Action(value = "/cf/user/buy-list",results={@Result(name="buy-list",type="ftl",location="/nb/cf/user/buy-list.html")})
	public String buyList(){
		return "buy-list";
	}
	
	/**
	 * 购买数据
	 * @throws IOException 
	 */
	@Action(value = "/cf/user/buy-list-data")
	public void buyListData() throws IOException{
		User user = getNBSessionUser();
		List<OrderModel> list = cfOrderService.getBuyList(user);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,list);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
   	
	/**
	 * 收货地址
	 * @throws IOException
	 */
	@Action(value="/cf/order/addressInfo")
	public void saveOrderInfo() throws IOException{
		String address = paramString("address");
		String mobilePhone = paramString("mobilePhone");
		Integer postNum = paramInt("postNum");
		String realName = paramString("realName");
		Integer id = paramInt("id");
		
		InvestOrder order = cfOrderService.find(id);
		order.setAddress(address);
		order.setMobilePhone(mobilePhone);
		order.setPostNum(postNum);
		order.setRealName(realName);
		
		cfOrderService.updateOrder(order);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG,"更新成功!");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 保存订单 股权众筹 每个项目 限跟投一次
	 * @throws IOException
	 */
	@Action(value = "/cf/user/order")
	public void saveCfOrder() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		Long projectId = paramLong("id");
		Double money = paramDouble("investMoney");
		String payPwd = paramString("payPwd");
		User user = getNBSessionUser();
		Integer type = paramInt("type");
		Integer profitRule = paramInt("profitRule"); 
		double virtual = paramDouble("virtual");
		
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		Account account = theAccountServie.getAccountByUserId(user.getUserId());
		VirtualAccount virtualAccount = virtualAccountService.findByUserId(user.getUserId());
		List<InvestOrder> list = cfOrderService.getAlready(user,projectBaseinfo);
		Date nowDate = new Date();
		
		if(!MD5.encode(payPwd).equals(user.getPayPwd())){
			data.put(ConstantUtil.ERRORMSG,"交易密码不正确!");
		}else if(null==projectBaseinfo){
			data.put(ConstantUtil.ERRORMSG,"该产品不存在!");
		}else if(2!=projectBaseinfo.getStatus()){
			data.put(ConstantUtil.ERRORMSG,"该产品不可跟投!");
		}else if(nowDate.before(projectBaseinfo.getStartTime())){
			data.put(ConstantUtil.ERRORMSG,"该产品未到可投时间!");
		}else if(!checkTime(projectBaseinfo)){
			data.put(ConstantUtil.ERRORMSG,"该产品不可跟投!");
		}else if(account.getUseMoney()<(money-virtual)&&0==type){
			data.put(ConstantUtil.ERRORMSG,"账户可用余额不足!");
		}
		else if(account.getUseMoney()<(BigDecimalUtil.mul(money,projectBaseinfo.getBreach())-virtual)&&1==type){
			data.put(ConstantUtil.ERRORMSG,"账户可用余额不足!");
		}
		else if(account.getUseMoney()<(money-virtual)&&0!=type&&1!=type){
			data.put(ConstantUtil.ERRORMSG,"账户可用余额不足!");
		}
		else if(null!=list){
			data.put(ConstantUtil.ERRORMSG,"您已购买过此产品!");
		}
		else if(!ConstantUtil.SUCCESS.equals(checkMoney(projectBaseinfo,money,profitRule))){
			data.put(ConstantUtil.ERRORMSG,checkMoney(projectBaseinfo,money,profitRule));
		}
		else if(null!=virtualAccount&&virtualAccount.getAccount()<virtual){
			data.put(ConstantUtil.ERRORMSG,"没有足够多的虚拟货币!");
		}else if(1!=projectBaseinfo.getType()&&0!=virtual){
			data.put(ConstantUtil.ERRORMSG,"非实物众筹不能使用虚拟货币!");
		}else if(null==virtualAccount&&0!=virtual){
			data.put(ConstantUtil.ERRORMSG,"您还未拥有虚拟货币!");
		}else{
			InvestOrder order = cfOrderService.save(projectBaseinfo,money,user,type,profitRule,virtual);
			
			data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			data.put(ConstantUtil.ERRORMSG,order);
		}
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 保存订单 实物众筹 公益众筹 可以多次购买
	 * @throws IOException
	 */
	@Action(value = "/cf/user/buyData")
	public void saveOrder() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		Long projectId = paramLong("id");
		Double money = paramDouble("investMoney");
		String payPwd = paramString("payPwd");
		User user = getNBSessionUser();
		Integer profitRule = paramInt("profitRule"); 
		double virtual = paramDouble("virtual");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		Account account = theAccountServie.getAccountByUserId(user.getUserId());
		VirtualAccount virtualAccount = virtualAccountService.findByUserId(user.getUserId());
		Date nowDate = new Date();
		Integer payType = paramInt("payType");
		if(1!=payType){
			if(!MD5.encode(payPwd).equals(user.getPayPwd())){
				data.put(ConstantUtil.ERRORMSG,"交易密码不正确!");
			}else if(null==projectBaseinfo){
				data.put(ConstantUtil.ERRORMSG,"该产品不存在!");
			}else if(2!=projectBaseinfo.getStatus()){
				data.put(ConstantUtil.ERRORMSG,"该产品不可跟投!");
			}else if(nowDate.before(projectBaseinfo.getStartTime())){
				data.put(ConstantUtil.ERRORMSG,"该产品未到可投时间!");
			}else if(!checkTime(projectBaseinfo)){
				data.put(ConstantUtil.ERRORMSG,"该产品不可跟投!");
			}else if(account.getUseMoney()<(money-virtual)){
				data.put(ConstantUtil.ERRORMSG,"账户可用余额不足!");
			}else if(!ConstantUtil.SUCCESS.equals(checkMoney(projectBaseinfo,money,profitRule))){
				data.put(ConstantUtil.ERRORMSG,checkMoney(projectBaseinfo,money,profitRule));
			}else if(null!=virtualAccount&&virtualAccount.getAccount()<virtual){
				data.put(ConstantUtil.ERRORMSG,"没有足够多的虚拟货币!");
			}else if(1!=projectBaseinfo.getType()&&0!=virtual){
				data.put(ConstantUtil.ERRORMSG,"非实物众筹不能使用虚拟货币!");
			}else if(null==virtualAccount&&0!=virtual){
				data.put(ConstantUtil.ERRORMSG,"您还未拥有虚拟货币!");
			}else{
				InvestOrder order = cfOrderService.save(projectBaseinfo,money,user,2,profitRule,virtual);
				
				data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
				data.put(ConstantUtil.ERRORMSG,order);
			}
		}else{
			if(null==projectBaseinfo){
				data.put(ConstantUtil.ERRORMSG,"该产品不存在!");
			}else if(2!=projectBaseinfo.getStatus()){
				data.put(ConstantUtil.ERRORMSG,"该产品不可跟投!");
			}else if(nowDate.before(projectBaseinfo.getStartTime())){
				data.put(ConstantUtil.ERRORMSG,"该产品未到可投时间!");
			}else if(!checkTime(projectBaseinfo)){
				data.put(ConstantUtil.ERRORMSG,"该产品不可跟投!");
			}else if(!ConstantUtil.SUCCESS.equals(checkMoney(projectBaseinfo,money,profitRule))){
				data.put(ConstantUtil.ERRORMSG,checkMoney(projectBaseinfo,money,profitRule));
			}else if(null!=virtualAccount&&virtualAccount.getAccount()<virtual){
				data.put(ConstantUtil.ERRORMSG,"没有足够多的虚拟货币!");
			}else if(1!=projectBaseinfo.getType()&&0!=virtual){
				data.put(ConstantUtil.ERRORMSG,"非实物众筹不能使用虚拟货币!");
			}else if(null==virtualAccount&&0!=virtual){
				data.put(ConstantUtil.ERRORMSG,"您还未拥有虚拟货币!");
			}else{
				InvestOrder order = cfOrderService.saveWechatPay(projectBaseinfo,money,user,2,profitRule,virtual);
				data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			}
		}
		
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 微信支付
	 * @throws IOException
	 */
	@Action(value = "/wechat/doWechatPay.action")
	public void doWechatPay() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,403);
		
		Long projectId = paramLong("id");
		Double money = paramDouble("investMoney");
		User user = getNBSessionUser();
		Integer profitRule = paramInt("profitRule"); 
		double virtual = paramDouble("virtual");
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.getProjectBaseinfoById(projectId);
		VirtualAccount virtualAccount = virtualAccountService.findByUserId(user.getUserId());
		Date nowDate = new Date();
	
		if(null==projectBaseinfo){
			data.put(ConstantUtil.ERRORMSG,"该产品不存在!");
		}else if(2!=projectBaseinfo.getStatus()){
			data.put(ConstantUtil.ERRORMSG,"该产品不可跟投!");
		}else if(nowDate.before(projectBaseinfo.getStartTime())){
			data.put(ConstantUtil.ERRORMSG,"该产品未到可投时间!");
		}else if(!checkTime(projectBaseinfo)){
			data.put(ConstantUtil.ERRORMSG,"该产品不可跟投!");
		}else if(!ConstantUtil.SUCCESS.equals(checkMoney(projectBaseinfo,money,profitRule))){
			data.put(ConstantUtil.ERRORMSG,checkMoney(projectBaseinfo,money,profitRule));
		}else if(null!=virtualAccount&&virtualAccount.getAccount()<virtual){
			data.put(ConstantUtil.ERRORMSG,"没有足够多的虚拟货币!");
		}else if(1!=projectBaseinfo.getType()&&0!=virtual){
			data.put(ConstantUtil.ERRORMSG,"非实物众筹不能使用虚拟货币!");
		}else if(null==virtualAccount&&0!=virtual){
			data.put(ConstantUtil.ERRORMSG,"您还未拥有虚拟货币!");
		}else{
			data.put(ConstantUtil.CODE,200);
		}
		
		printWebJson(getStringOfJpaObj(data));
	}

	
	/**
	 * 校验投资金额
	 * @param projectBaseinfo
	 * @param money
	 * @return
	 */
	public String checkMoney(ProjectBaseinfo projectBaseinfo,Double money,Integer profit){
		ProfitRule profitRule = profitRuleService.find(profit);
		if(0!=profitRule.getMoney().doubleValue()&&profitRule.getMoney().doubleValue()!=money.doubleValue()){
			return "投资金额与收益金额不匹配！";
		}
		if(0==projectBaseinfo.getIsExceedAccept()){
			double account = projectBaseinfo.getAccount()+money;
			if(account>projectBaseinfo.getWannaAccount()){
				return "此项目不接受超募！";
			}
		}
		if(money<projectBaseinfo.getMinMoney()){
			return "跟投金额小于最小跟投金额!";
		}else{
			if(0!=projectBaseinfo.getAddAmount()){
				Double dou = (money-projectBaseinfo.getMinMoney())/projectBaseinfo.getAddAmount();
				if(dou!=((int)dou.doubleValue())){
					return "跟投金额不满足增加规则!";
				}
			}
		}
		return ConstantUtil.SUCCESS;
	}
	
	/**
	 * 校验产品时间
	 * @param projectBaseinfo
	 * @return
	 */
	public boolean checkTime(ProjectBaseinfo projectBaseinfo){
		Date nowdate=new Date();
		Date d = projectBaseinfo.getEndTime();

		boolean flag = d.before(nowdate);
		if(flag){
			return false;
		}
		else{
			return true;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
