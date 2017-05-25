package com.rongdu.p2psys.web.recharge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.domain.PayOfflinebank;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.model.payment.PaymentWay;
import com.rongdu.p2psys.account.model.payment.PaymentWayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.handler.ToPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.LianlPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderResultsRetBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.SupportBank;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.account.service.PayOfflinebankService;
import com.rongdu.p2psys.account.service.PayOnlinebankService;
import com.rongdu.p2psys.account.service.PayService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.payment.service.IChannelBankService;
import com.rongdu.p2psys.nb.payment.service.IChannelConfigService;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserCacheModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 充值
 * @author cx
 * @version 2.0
 * 2014-10-27
 */
@SuppressWarnings("rawtypes")
public class RechargeAction extends BaseAction implements ModelDriven<AccountRechargeModel> {

	private static Logger logger = Logger.getLogger(RechargeAction.class);
	private AccountRechargeModel model = new AccountRechargeModel();

	@Resource
	private AccountService accountService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private PayService payService;
	@Resource
	private PayOnlinebankService payOnlinebankService;
	@Resource
	private PayOfflinebankService payOfflinebankService;
	@Resource
	private UserService userService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private IChannelConfigService channelConfigService;
	@Resource
	private IChannelBankService channelBankService;
	@Resource
	private IOrderService orderService;
	
	private User user;
	private Map<String, Object> data;
	private String payPath = "com.rongdu.p2psys.account.model.payment.";
	public static final boolean isOpenAip = BaseTPPWay.isOpenApi();

	/**
	 * 前往充值页面
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/member/recharge/newRecharge",results = { @Result(name = "newRecharge", type = "ftl", location = "/member/recharge/newRecharge.html"),
			@Result(name = "newRecharge_firm", type = "ftl", location = "/member_borrow/recharge/newRecharge.html")})
	public String newRecharge() throws Exception {
		user = getSessionUser();
		int payOfflineVal = paramInt("payOfflineVal");
		request.setAttribute("payOfflineVal", payOfflineVal);
		int minRechargeMoney = Global.getInt("OFFLINE_RECHARGE_MONEY");//线下充值最小金额
		request.setAttribute("minRechargeMoney", minRechargeMoney);
		/*request.setAttribute("is_open_deposit", Global.getValue("is_open_deposit"));
		int apiCode = TPPWay.API_CODE;
		if (!isOpenAip) {
    		Account account = accountService.findByUser(userId);
    		List<Pay> payList = payService.list(1); // 查看启用非直连的第三方支付接口
    		List<Pay> directList = payService.directList(1); // 查看启用直连的第三方支付接口
    		Pay pay = new Pay();
    		List<PayOnlinebankModel> payOnlinebankList = new ArrayList<PayOnlinebankModel>();
    		for (int i = 0; i < directList.size(); i++) {
    			pay = directList.get(i); // 可能开通不止一个接口
    			payOnlinebankList = payOnlinebankService.list(pay.getId()); // 线上银行列表
    		}
    		List<PayOfflinebank> payOfflinebankList = payOfflinebankService.list(); // 线下银行列表
    		request.setAttribute("account", account);
    		request.setAttribute("payOnlinebankList", payOnlinebankList);
    		request.setAttribute("payList", payList);
    		request.setAttribute("payOfflinebankList", payOfflinebankList);
    		if (paramInt("borrow") == 1) {
    			return "newRecharge_firm";
    		} 
		} else {
			double useMoney = accountService.getAccountUseMoney(userId);
	        request.setAttribute("useMoney", useMoney);
	        // 环讯需要充值银行
	        if (apiCode == 2){
		        List<IpsRechargeBank> ipsBankList = accountRechargeService.getIpsRechargeBankList();
		    	request.setAttribute("ipsBankList", ipsBankList);
	        }

	        if (user.getUserCache().getUserType() == 2) {//2借款人
				return "newRecharge_firm";
			} 
		}*/
		saveToken("rechargeToken");
		List<PayOfflinebank> payOfflinebankList = payOfflinebankService.list(); // 线下银行列表
		request.setAttribute("payOfflinebankList", payOfflinebankList);
		double useMoney = accountService.getAccountUseMoney(user.getUserId());
        request.setAttribute("useMoney", useMoney);
		List<AccountBank> list = accountBankService.list(user.getUserId());
		
		request.setAttribute("bank_name","");
		if(list != null && list.size() > 0)
		{
			AccountBank ab = list.get(0);
			request.setAttribute("accountBank", ab);
			request.setAttribute("bank_name", ab.getBank());
			String bankCode = ab.getBankCode();
			if(bankCode!=null && !bankCode.equals("")){				
				SupportBank sb = new SupportBank(bankCode, "13");//支付渠道类型，13PCweb端，16wap端
				String bankJosn = LianlPayHelper.querySupportBank(sb);
				JSONObject reqObj = JSONObject.parseObject(bankJosn);
				JSONArray paymentType = reqObj.getJSONArray("support_banklist");
				JSONObject info=paymentType.getJSONObject(0);
				String day_amt = info.getString("day_amt");
				String single_amt = info.getString("single_amt");
				request.setAttribute("llday_amt", day_amt);//单日额度
				request.setAttribute("llsingle_amt", single_amt);//单笔额度
			}
		}
		
//		String webRechargeUrl = channelConfigService.loadGatewayUrlByKey(ConstantUtil.gatewayKey.webRecharge.getValue());//获取系统所用通道充值信息
//		if(webRechargeUrl != null){
//			request.setAttribute("webRechargeUrl", webRechargeUrl);//PC端充值入口
//		}
//		ChannelConfig cc = channelConfigService.getChannelConfigByKey(ConstantUtil.gatewayKey.webRecharge.getValue());//获取系统所用通道充值信息
//		if(cc != null){
//			data.put("webRechargeUrl", ConstantUtil.WEB_UNIONPAY_RECHARGE_URL);//PC端充值入口
//			data.put("webChannelKey", channelKey.unionpay.getValue());//PC端充值通道key
//		}
		
//		List<ChannelBank> llBankllList = channelBankService.list(user.getUserId(),ConstantUtil.channelKey.llpay.getValue());
//		List<ChannelBank> ylBankllList = channelBankService.list(user.getUserId(),ConstantUtil.channelKey.unionpay.getValue());
		
		List<AccountBank> llBankllList = accountBankService.list(user.getUserId(), channelKey.llpay.getValue());
		List<AccountBank> ylBankllList = accountBankService.list(user.getUserId(), channelKey.unionpay.getValue());
		
		if (llBankllList.size() == 0 && ylBankllList.size() == 0) {			
			request.setAttribute("webRechargeUrl", ConstantUtil.WEB_UNIONPAY_RECHARGE_URL);//PC端银联充值入口
			request.setAttribute("webChannelKey", channelKey.unionpay.getValue());//PC端充值银联通道key
		}else if (llBankllList.size() > 0 && ylBankllList.size() == 0) {			
			request.setAttribute("webRechargeUrl", ConstantUtil.WEB_LLPAY_RECHARGE_URL);//PC端连连充值入口
			request.setAttribute("webChannelKey", channelKey.llpay.getValue());//PC端充值连连通道key
		}else if(llBankllList.size() == 0 && ylBankllList.size() > 0) {			
			request.setAttribute("webRechargeUrl", ConstantUtil.WEB_UNIONPAY_RECHARGE_URL);//PC端银联充值入口
			request.setAttribute("webChannelKey", channelKey.unionpay.getValue());//PC端充值银联通道key
		}else if (llBankllList.size() > 0 && ylBankllList.size() > 0){
			request.setAttribute("webRechargeUrl", ConstantUtil.WEB_UNIONPAY_RECHARGE_URL);//PC端银联充值入口
			request.setAttribute("webChannelKey", channelKey.unionpay.getValue());//PC端充值银联通道key
		}
		
		if (paramInt("borrow") == 1) 
		{
			return "newRecharge_firm";
		}
		return "newRecharge";
	}
	
	/**
	 * 前往盛付通充值页面
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/member/sPay")
	public String sPayRecharge() throws Exception {
		
		return "sPay";
	}

	/**
	 * 进行线上充值
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/member/recharge/doRecharge",results={@Result(name = "ipsRecharge", type = "ftl", location = "/tpp/ipsRecharge.html"),
			@Result(name = "pnrRecharge", type = "ftl", location = "/tpp/chinapnr/netSave.html"),
			@Result(name = "interface_*", type = "ftl", location = "/member/recharge/*.html")})
	public String doRecharge() throws Exception {
        UserIdentify userIdentify = userIdentifyService.findById(getSessionUserIdentify().getId());
        session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
        model.validIdentifyForRecharge(userIdentify);
        model.validNewRecharge();
        if(!isOpenAip){//标准版入口校验
        	String str="";
        	if(model.getType()==1){ //直连
        		str=model.getPayOnlinebank();
        	}else if(model.getType()==2){//线上
        		str=model.getPay();
        	}
        	payPath += str + "." + StringUtil.firstCharUpperCase(str) + "PaymentWay";
    		PaymentWay way = PaymentWayHelper.getPaymentWay(payPath);
    		BasePayment payment = way.paymentRequest(request, model);
    		AccountRecharge ar = way.getRecharge();
    		ar.setTradeNo(payment.getTranNo());
    		ar.setAmountIn(model.getMoney());
    		accountRechargeService.add(ar);
    		return "interface_"+payment.payname();
        }else{
	        user = userService.getUserById(getSessionUser().getUserId());
	        AccountRecharge ar = new AccountRecharge(user, model.getMoney(),
	                "online_recharge", model.getType(), model.getRemark());
	        accountRechargeService.add(ar);
	        String name = Global.getValue("cooperation_interface");
	        TPPWay way = TPPFactory.getTPPWay(null, user, model, ar.getTradeNo(),null);
	        Object recharge =(Object) way.doRecharge();
			request.setAttribute(name, recharge);
	        return name + "Recharge";
        }
	}
	
	/**
	 * 银联在线充值
	 * 
	 * @throws Exception
	 */
	@Action("/member/recharge/doUnionPayRecharge")
	public void doUnionPayRecharge() throws Exception {
		checkToken("rechargeToken");
		user = userService.getUserById(getSessionUser().getUserId());
		String payPwd = MD5.encode(paramString("payPwd"));
		if (null != payPwd && !payPwd.equals(user.getPayPwd())) {
			userCacheService.doLock(request, user.getUserId(), UserCacheModel.PAY_PWD_LOCK);
			throw new AccountException("支付密码不正确!！", 1);
		}
		model.setUser(user);
		String payment = "PC端银联充值";
		List<AccountBank> abList = accountBankService.list(user.getUserId(), channelKey.unionpay.getValue());
		data = accountRechargeService.doUnionPay(payment,model,abList.get(0));
		if((Boolean) data.get("result")){
			printWebSuccess();
		}else{
			printWebResult(data.get("message").toString(), false);
		}
	}

	/**
	 * 进行线下充值
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/recharge/doOfflineRecharge")
	public void doOfflineRecharge() throws Exception {
		try {
			user = getSessionUser();
			user = userService.getUserById(user.getUserId());
//			UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUserId());
//			model.validIdentifyForRecharge(userIdentify);
			model.validNewRecharge();
			checkToken("rechargeToken");
			double offlineRechargefee = 0; // Global.getValue("online_rechargefee"),
											// 待完善
			double fee = BigDecimalUtil.mul(model.getMoney(), offlineRechargefee);
			AccountRecharge recharge = model.prototype(user, fee);
			recharge.setPayOfflinebank(payOfflinebankService.find(model.getPayOfflinebankId()));
			recharge.setType(3);// 线下充值
			recharge.setAmountIn(BigDecimalUtil.sub(recharge.getMoney(), fee));
			recharge.setBankNo(recharge.getBankNo());
			recharge.setRealName(recharge.getRealName());
			recharge.setRemark("姓名："+recharge.getRealName()+",银行卡号："+recharge.getBankNo());
			recharge.setPayment("PC端线下充值");
			recharge.setChannelKey(channelKey.unionpay.getValue());
			accountRechargeService.add(recharge);
			printWebSuccess();
		} catch (Exception e) {
			throw new AccountException(e.getMessage(), 1);
		}
	}

	/**
	 * 充值记录列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value="/member/recharge/log",results = { @Result(name = "log", type = "ftl", location = "/member/recharge/log.html"),
			@Result(name = "log_firm", type = "ftl", location = "/member_borrow/recharge/log.html")})
	public String log() throws Exception {
		user = getSessionUser();
		AccountRechargeModel recharge = accountRechargeService.getRechargeSummary(getSessionUser().getUserId());
		Account account = accountService.findByUser(user.getUserId());
		request.setAttribute("recharge", recharge);
		request.setAttribute("account", account);
		if (user.getUserCache().getUserType() == 3 && paramInt("borrow") == 1 
				|| user.getUserCache().getUserType() == 2) {
			return "log_firm";
		} 
		return "log";
	}

	/**
	 * 提现记录 ajax数据接口
	 * 
	 * @return
	 */
	@Action("/member/recharge/logList")
	public void logList() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		PageDataList<AccountRechargeModel> pageDataList = accountRechargeService.list(userId, model);
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(data));
	}

	@Override
	public AccountRechargeModel getModel() {
		return model;
	}
	
	/**
	 * 连连充值密码验证
	 * 
	 * @throws Exception
	 */
	@Action("/member/recharge/dosubLLRecharge")
	public void dosubLLRecharge() throws Exception {
		checkToken("rechargeToken");
		user = userService.getUserById(getSessionUser().getUserId());
		String payPwd = MD5.encode(paramString("payPwd"));
		if (null != payPwd && !payPwd.equals(user.getPayPwd())) {
			userCacheService.doLock(request, user.getUserId(), UserCacheModel.PAY_PWD_LOCK);
			throw new AccountException("支付密码不正确!！", 1);
		}
		printWebSuccess();
	}
	
	/**
	 * 前往连连充值页面
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/member/recharge/subRecharge",results = { @Result(name = "subRecharge", type = "ftl", location = "/member/recharge/subRecharge.html"),
			@Result(name = "wxsubRecharge", type = "ftl", location = "/member/recharge/wxsubRecharge.html")})
	public String recharge() throws Exception {
		user = userService.getUserById(getSessionUser().getUserId());
		request.setAttribute("money", paramString("money"));
		int payType = paramInt("payType");
		request.setAttribute("payType", payType);
		List<AccountBank> abList = null;
		abList = accountBankService.list(user.getUserId());
		if(abList!=null && abList.size()>0){
			AccountBank ab = abList.get(0);
			request.setAttribute("bindBank", ab);
		}
		if(payType>0){
			return "wxsubRecharge";
		}
		return "subRecharge";
	}
	
	/**
	 * 连连在线充值
	 * 
	 * @throws Exception
	 */
	@Action(value="/member/recharge/dollPayRecharge",results = { @Result(name = "dollPayRecharge", type = "ftl", location = "/member/recharge/dollPayRecharge.html")})
	public String dollPayRecharge() throws Exception {
		user = userService.find(getSessionUser().getUserId());
		model.setUser(user);
		UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUserId());
		
		String accid = "";
		String accname = "";
		if(userIdentify.getRealNameStatus() == 1){
			accid = userIdentify.getUser().getCardId();
			accname = userIdentify.getUser().getRealName();
		}else{
			accid = paramString("accId");
			accname = paramString("accName");
		}
		String no_agree = paramString("no_agree");
		String mobile = paramString("mobile");
		String cardno = paramString("card_no");
		String name_goods = paramString("name_goods");
		String money_order = paramString("money_order");
		int payType = paramInt("payType");
       
		// 创建订单
        OrderInfo order = LianlPayHelper.createOrder(0,name_goods,money_order);
        orderService.saveOrder(user,order);
		logger.info("=============连连支付订单报文1111111：" + JSON.toJSONString(order));
        session.put(Constant.SESSION_ORDER, order.getNo_order());
        //支付参数封装
        LianlPay llpay = accountRechargeService.setllpay(request,user,no_agree,accname,accid,mobile,cardno);
        logger.info("=============连连支付支付报文1111111：" + JSON.toJSONString(llpay));
        //充值处理
        ToPay.prepositPay(payType,request,llpay, order);
        return "dollPayRecharge";
	}


	/**
	 * 充值结果处理
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/member/recharge/resultRecharge",results = { @Result(name = "resultRecharge", type = "ftl", location = "/member/recharge/resultRecharge.html")})
	public String resultRecharge() throws Exception {
		//返回信息：oid_partner=201306031000001013&sign_type=MD5&sign=45935d9811ee648e99f796c58bcb1133&dt_order=20150525181110&no_order=20150525181110&oid_paybill=2015052569482353&money_order=0.01&result_pay=SUCCESS&settle_date=20150525&info_order=%E7%94%A8%E6%88%B7%E8%B4%AD%E4%B9%B0%E5%85%85%E5%80%BC&pay_type=3&bank_code=01050000
		payResult();
		return "resultRecharge";
	}


	/**
	 * 充值支付结果及业务处理
	 */
	private void payResult() {
		user = userService.find(getSessionUser().getUserId());	
		String orderNo = (String) session.get(Constant.SESSION_ORDER);
		OrderResultsRetBean ret = LianlPayHelper.queryOrder(orderNo);
		String flag0 = ret.getRet_code();//响应结果
		String flag1 = ret.getResult_pay();//充值结果
		if(flag0.equals("0000") && flag1.equals("SUCCESS")){//充值成功
			//业务处理成功后，session 用户认证信息变更
			UserIdentify identify = userIdentifyService.findByUserId(user.getUserId());
			session.put(Constant.SESSION_USER_IDENTIFY, identify);
			session.put(ConstantUtil.SESSION_USER, user);
			request.setAttribute("msg", "恭喜您，充值成功！");
			
		}else{
			request.setAttribute("msg", "抱歉，充值失败！");
		}
		//更新充值入口信息
		accountRechargeService.updatePayMentByTradeNo(orderNo, "PC端连连充值");
	}
	
	/**
	 * 微信充值结果处理(连连支付)
	 * 
	 * @return
	 */
	@Action(value="/member/recharge/resultRechargewx",results = { @Result(name = "resultRechargewx", type = "ftl", location = "/member/recharge/resultRecharge_wx.html")})
	public String resultRechargewx() throws Exception {
//		payResult();
		String bindId = "793be4dfe8e62c83705a42b9d7a32128";
		SignHelper.unbind(bindId);
		return "resultRechargewx";
	}

	/**
	 * 查询支持银行
	 * @return
	 */
	@Action(value="/member/recharge/querySupportBank")
	public void querySupportBank() throws Exception {
		String bankCode = paramString("bank_code");
		SupportBank sb = null;
		if(bankCode==null || bankCode.equals("")){			
			sb = new SupportBank("13");//支付渠道类型，13PCweb端，16wap端
		}else{
			sb = new SupportBank(bankCode, "13");//支付渠道类型，13PCweb端，16wap端
		}
		String bankJosn = LianlPayHelper.querySupportBank(sb);
		printJson(bankJosn);
	}
}
