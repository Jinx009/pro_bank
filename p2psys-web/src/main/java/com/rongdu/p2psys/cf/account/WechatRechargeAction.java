package com.rongdu.p2psys.cf.account;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.handler.ToPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.LianlPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderResultsRetBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.SupportBank;
import com.rongdu.p2psys.account.model.payment.llPay.model.UserBankCardRet;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPay;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.PayOfflinebankService;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.payment.domain.ChannelConfig;
import com.rongdu.p2psys.nb.payment.domain.NbSupportBank;
import com.rongdu.p2psys.nb.payment.model.ChannelConfigModel;
import com.rongdu.p2psys.nb.payment.service.IChannelBankService;
import com.rongdu.p2psys.nb.payment.service.IChannelConfigService;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.payment.service.ISupportBankService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;

public class WechatRechargeAction extends BaseAction<AccountRechargeModel> implements ModelDriven<AccountRechargeModel>  {
	@Resource
	private ISupportBankService theSupportBankService;
	@Resource
	private SystemConfigService systemConfigService;
	@Resource
	private IChannelConfigService channelConfigService;
	@Resource
	private IChannelBankService channelBankService;
	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private IOrderService orderService;
	@Resource
	private PayOfflinebankService payOfflinebankService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private UserService theUserService;
	@Resource
	private UserCacheService theUserCacheService;
	
	private User user;
	private Map<String, Object> data;
		
	
	/**
	 * 众筹-微信充值
	 * 
	 * @return
	 */
	@Action(value = "/cf/wechat/user/recharge", results = { @Result(name = "recharge1", type = "ftl", location = "/nb/cf/wechat/recharge/recharge.html"),
			@Result(name = "recharge2", type = "ftl", location = "/nb/cf/wechat/recharge/recharge_notiecard_one.html")})
	public String recharge() {
		if(hasSessionUser()){
			return goToURL(data);
		}
		return "recharge1";
	}
	
	/**
	 * 众筹-微信充值-绑卡
	 * 
	 * @return
	 */
	@Action(value = "/cf/wechat/user/bindBank", results = { @Result(name = "bindBank", type = "ftl", location = "/nb/cf/wechat/recharge/recharge_notiecard_two.html")})
	public String bindBank() {
		if(hasSessionUser()){
			request.setAttribute("money", paramDouble("money"));
			request.setAttribute("bankCode", paramString("bankCode"));
			ChannelConfig cc = channelConfigService.getChannelConfigByCode(paramString("bankCode"));
			request.setAttribute("channelKey", cc.getWapRechargeKey());
			saveToken("cfRechargeToken");
	        request.setAttribute("cfRechargeToken", session.get("cfRechargeToken"));
		}
		return "bindBank";
	}
	
	/**
	 * 获取绑卡验证码（银联在线专用）
	 * @return
	 * @throws Exception
	 */
	@Action(value="/cf/wechat/recharge/getAddBankCode")
	public String getAddBankCode() throws Exception {
		UnionPayRet ret = SignHelper.sendCode(paramString("mobilePhone"));
		if(ret == null) {
			printWebResult("银联暂无响应，请稍后重试", false);
		}
		if("0000".equals(ret.getRetCode())){
			printWebSuccess();
		}else{
			printWebResult(ret.getRetDesc(), false);
		}
		return null;
	}
	
	
	/**
	 * 进行线下充值
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value="/cf/wechat/recharge/doOfflineRecharge",results = { @Result(name = "recharge1", type = "ftl", location = "/nb/cf/wechat/recharge/recharge.html"),
			@Result(name = "recharge2", type = "ftl", location = "/nb/cf/wechat/recharge/recharge_notiecard_one.html")})
	public String doOfflineRecharge() throws Exception {
		saveToken("cfRechargeToken");
		request.setAttribute("cfRechargeToken", session.get("cfRechargeToken"));
		if(hasSessionUser()){			
			try {
				user = getNBSessionUser();
				String tokenMsg = checkToken("cfRechargeToken");
				if(!StringUtil.isBlank(tokenMsg)){
					setRequestMsg(false,tokenMsg,"请稍后再试！");
					return goToURL(data);
				}
				boolean b = accountRechargeService.offLineRecharge(model,user,"微信端线下充值申请");
				if(b){			
					user.setRealName(model.getRealName());
					user.setCardId(model.getCardId());
					session.put(ConstantUtil.SESSION_USER, user);
					setRequestMsg(true,"线下充值预约已申请成功","请及时至银行网点/网银/手机银行进行转账");
				}else{
					setRequestMsg(false,"线下充值预约申请失败","请重新尝试！");
				}
			} catch (Exception e) {
				setRequestMsg(false,"线下充值预约申请失败","请重新尝试！");
			}
		}else{
			request.setAttribute("isLogin", ConstantUtil.NO_LOGIN_USER);
		}
		return goToURL(data);
	}
	
	/**
	 * 银联在线充值 addBank_recharge.html
	 * 
	 * @throws Exception
	 */
	@Action(value = "/cf/wechat/recharge/doUnionPayRecharge", results = {
			@Result(name = "dollPayRecharge", type = "ftl", location = "/nb/cf/wechat/recharge/dollPayRecharge.html"),
			@Result(name = "recharge1", type = "ftl", location = "/nb/cf/wechat/recharge/recharge.html"),
			@Result(name = "recharge2", type = "ftl", location = "/nb/cf/wechat/recharge/recharge_notiecard_one.html")})
	public String doUnionPayRecharge() throws Exception {
		if(hasSessionUser()){
			String accId = paramString("accId").toUpperCase();
			String accName = paramString("accName");
			String cardNo = paramString("cardNo").replace(" ", "");
			double money = paramDouble("money");
			String mobile = paramString("mobile");
	
			request.setAttribute("cardNo", cardNo);
			request.setAttribute("accId", accId);
			request.setAttribute("accName", accName);
			request.setAttribute("money", money);
			request.setAttribute("mobile", mobile);
			request.setAttribute("isBindBank", 0);
			
			String tokenMsg = checkToken("cfRechargeToken");
			if(!StringUtil.isBlank(tokenMsg)){
				setRequestMsg(false,tokenMsg,"请稍后再试！");
				return goToURL(data);
			}
			saveToken("cfRechargeToken");
			user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
			user = theUserService.getByUserId(user.getUserId());
			model.setUser(user);
			List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
			if (bankList.size() > 0) {
				cardNo = bankList.get(0).getBankNo();
				request.setAttribute("isBindBank", 0);
				request.setAttribute("bindBank", bankList.get(0));
				request.setAttribute("bindId", "");
			}
			List<AccountBank> abList = theAccountBankService.list(user.getUserId(), channelKey.unionpay.getValue());
			String payment = "微信端银联充值";
			if (abList.size() > 0) {
				request.setAttribute("isBindBank", 1);
				request.setAttribute("bindBank", abList.get(0));
				request.setAttribute("bindId", abList.get(0).getBindId());
				// 验证手机验证码是否正确
				String yzm = paramString("code");
				Map<String, Object> map = ((Map<String, Object>) request.getSession().getAttribute("cashGetCode_code"));
				if (map == null || !yzm.equals(map.get("code").toString())) {
					setRequestMsg(false,"验证码错误","请稍后再试！");
					return goToURL(data);
				}
				data = accountRechargeService.doUnionPay(payment, model, abList.get(0));
				rechargeOper(data);
				return goToURL(data);
			} else {
				UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());

				if (userIdentify.getRealNameStatus() == 1) {
					accId = user.getCardId();
					accName = user.getRealName();
					mobile = user.getMobilePhone();
				}
				String code = paramString("code");

				request.setAttribute("cardNo", cardNo);
				request.setAttribute("mobile", mobile);
				request.setAttribute("accName", accName);

				UnionPay pay = new UnionPay(cardNo, accName, accId, mobile, code);
				String cab = checkAddBank(user.getUserId(), accId, cardNo, channelKey.unionpay.getValue());
				if (cab != "") {
					return cab;
				}
				UnionPayRet ret = SignHelper.cardBindByMC(pay);
				if (ret == null) {
					setRequestMsg(false,"银联暂无响","请稍后再试！");
					return goToURL(data);
				}
				if ("0000".equals(ret.getRetCode())) {
					AccountBank nbank = bindSuccess(userIdentify, accId, accName, cardNo, mobile, ret);
					data = accountRechargeService.doUnionPay(payment, model, nbank);
					rechargeOper(data);
					return goToURL(data);
				} else if ((ret.getRetDesc()).indexOf("短信") >= 0) {
					setRequestMsg(false,"验证码错误","请稍后再试！");
					return goToURL(data);
				} else if ((ret.getRetDesc()).indexOf("身份认证") >= 0) {
					setRequestMsg(false,"身份认证失败","请稍后再试！");
					return goToURL(data);
				} else {  //失败直接切换为连连充值通道
					String no_agree = "";
					List<AccountBank> llbankList = theAccountBankService.list(user.getUserId(), channelKey.llpay.getValue());
					if (llbankList != null && llbankList.size() > 0) {
						AccountBank ab = llbankList.get(0);
						no_agree = ab.getBindId();
						mobile = ab.getMobile();
						cardNo = ab.getBankNo();
					} else {
						String cab2 = checkAddBank(user.getUserId(), accId, cardNo, channelKey.llpay.getValue());
						if (!StringUtil.isBlank(cab2)) {
							saveToken("cfRechargeToken");
							return cab2;
						}
					}
					return llpayRecharge(accId, accName, mobile, cardNo, money, no_agree);
				}
			}
		}else{
			request.setAttribute("isLogin", ConstantUtil.NO_LOGIN_USER);
			return goToURL(data);
		}
	}
	
	/**
	 * 银联充值完成业务处理
	 * 
	 * @param data
	 */
	private void rechargeOper(Map<String, Object> data) {
		if ((Boolean) data.get("result")) {
			// 充值成功
			setRequestMsg(true,"恭喜您，充值成功","");
		} else {
			// 充值失败
			setRequestMsg(false,data.get("message").toString(),"请稍后再试！");
		}
	}
	
	/**
	 * 绑卡成功业务处理
	 * 
	 * @param userIdentify
	 * @param accId
	 * @param accName
	 * @param cardNo
	 * @param mobile
	 * @param ret
	 */
	private AccountBank bindSuccess(UserIdentify userIdentify, String accId, String accName, String cardNo,
			String mobile, UnionPayRet ret) {
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		user = theUserService.getByUserId(user.getUserId());
		AccountBank ab = null;
		// 先查询银行卡信息
		UnionPayRet cardInfo = SignHelper.cardInfo(cardNo);
		String bankName = cardInfo.getBankName();
		if ("0000".equals(cardInfo.getRetCode())) {
			ab = new AccountBank(user, cardNo, cardInfo.getBankName(), "/data/bank/mini/" + bankName + ".png");
			ab.setMobile(mobile);
			ab.setBindId(ret.getBindId());
			ab.setStatus(1);
			ab.setChannelKey(channelKey.unionpay.getValue());
			ab.setBankCode(CommonRealize.bank_code_map.get(bankName));
			theAccountBankService.saveAccountBank(ab);
			// 添加至通道银行卡管理表
			channelBankService.saveChannelBank(user, ab, ConstantUtil.channelKey.unionpay.getValue());
		}

		// 该绑卡接口带有实名校验，所以绑卡成功即自动完成实名认证
		if (userIdentify.getRealNameStatus() != 1) {
			user.setRealName(accName);
			user.setCardId(accId);
			theUserService.updateUser(user);

			int length = accId.length();
			String sexNum;
			if (length == 15) {
				sexNum = accId.substring(length - 1);
			} else {
				sexNum = accId.substring(length - 2, length - 1);
			}

			// 获取性别 1:男,0:女
			int sex = Integer.parseInt(sexNum) % 2;
			UserCache userCache = user.getUserCache();
			userCache.setSex(sex);
			theUserCacheService.updateUserCache(userCache);

			UserIdentify identify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			identify.setRealNameStatus(1);
			identify.setRealNameVerifyTime(new Date());
			identify.setUser(user);
			theUserIdentifyService.updateUserIdentify(identify);

			// 业务处理成功后，session 用户认证信息变更
			session.put(Constant.SESSION_USER_IDENTIFY, identify);
			session.put(ConstantUtil.SESSION_USER, user);
		}
		return ab;
	}
	
	/**
	 * 连连在线充值
	 * 
	 * @throws Exception
	 */
	@Action(value="/cf/wechat/recharge/dollPayRecharge",results = { @Result(name = "dollPayRecharge", type = "ftl", location = "/nb/cf/wechat/recharge/dollPayRecharge.html"),
			@Result(name = "recharge1", type = "ftl", location = "/nb/cf/wechat/recharge/recharge.html"),
			@Result(name = "recharge2", type = "ftl", location = "/nb/cf/wechat/recharge/recharge_notiecard_one.html")})
	public String dollPayRecharge() throws Exception {
		if(hasSessionUser()){
			user = theUserService.getByUserId(getNBSessionUser().getUserId());
			String tokenMsg = checkToken("cfRechargeToken");
			if(!StringUtil.isBlank(tokenMsg)){
				setRequestMsg(false,tokenMsg,"请稍后再试！");
				return goToURL(data);
			}
			model.setUser(user);
			UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			
			String accid = "";
			String accname = "";
			String mobile = paramString("mobile");
			String cardno = paramString("cardNo").replace(" ", "");
			double money_order = paramDouble("money");
			if(userIdentify.getRealNameStatus() == 1){
				accid = user.getCardId();
				accname = user.getRealName();
				mobile = user.getMobilePhone();
			}else{
				accid = paramString("accId").toUpperCase();
				accname = paramString("accName");
			}
			String no_agree = "";
			List<AccountBank> bankList = theAccountBankService.list(user.getUserId(),channelKey.llpay.getValue());
			if(bankList!=null && bankList.size()>0){
				AccountBank ab = bankList.get(0);
				no_agree = ab.getBindId();
				mobile = ab.getMobile();
				cardno = ab.getBankNo();
			}else{
				List<AccountBank> abList = theAccountBankService.list(user.getUserId());
				if(abList!=null && abList.size()>0){
					AccountBank ab = abList.get(0);
					mobile = ab.getMobile();
					cardno = ab.getBankNo();
				}
				String cab = checkAddBank(user.getUserId(),accid, cardno,channelKey.llpay.getValue());
				if(!StringUtil.isBlank(cab)){
					return cab;
				}
			}
			
			request.setAttribute("webChannelKey", "连连支付");
			request.setAttribute("bankCodeKey", paramString("bankCodeKey"));
			
			request.setAttribute("cardNo", cardno);
			request.setAttribute("accId", accid);
			request.setAttribute("accName", accname);
			request.setAttribute("money", money_order);
			request.setAttribute("mobile", mobile);
			
			if(money_order<=0 || cardno.length()<=0 || accid.length()<=0 || accname.length()<=0){
				List<AccountBank> abList = theAccountBankService.list(user.getUserId());
				if(abList.size()>0){
					request.setAttribute("isSecond", 2);
				}else{
					request.setAttribute("isSecond", 1);
				}
				setRequestMsg(false,"充值信息有误","请重新输入！");
				return goToURL(data);
			}
			return llpayRecharge(accid, accname, mobile, cardno, money_order,
					no_agree);
		}else{
			request.setAttribute("isLogin", ConstantUtil.NO_LOGIN_USER);
			return goToURL(data);
		}
	}

	/**
	 * 连连充值核心模块
	 * @param accid 身份证号
	 * @param accname 姓名
	 * @param mobile 手机号
	 * @param cardno 银行卡号
	 * @param money_order 充值金额
	 * @param no_agree 协议号
	 * @return 跳转地址
	 * @author sian
	 */
	private String llpayRecharge(String accid, String accname, String mobile,
			String cardno, double money_order, String no_agree) {
		String name_goods = "连连微信充值";
		// 创建订单
		OrderInfo order = LianlPayHelper.createOrder(0,name_goods,String.valueOf(money_order));
		orderService.saveOrder(user,order);
		session.put(Constant.SESSION_ORDER, order.getNo_order());
		//支付参数封装
		LianlPay llpay = accountRechargeService.setllpay(request,user,no_agree,accname,accid,mobile,cardno);
		user = theUserService.getByUserId(user.getUserId());
		session.put(ConstantUtil.SESSION_USER, user);
		//充值处理
		ToPay.prepositPay(1,request,llpay, order);
		return "dollPayRecharge";
	}

	/**
	 * 验证绑卡认证信息
	 * @param userId
	 * @param accid
	 * @param cardno
	 */
	private String checkAddBank(long userId,String accid, String cardno,String channelKey) {
		//先根据身份证号查询是否已被使用
		int userNum = theUserService.countByCardId(accid,channelKey);
		User user = theUserService.getUserByCardId(accid);
		if(user!=null && user.getUserId()!=userId){
			setRequestMsg(false,"该身份证已被使用","请重新输入！");
			return goToURL(data);
		}
		if(userNum>0){
			setRequestMsg(false,"该身份证已被使用","请重新输入！");
			return goToURL(data);
		}
		String bankJosn = LianlPayHelper.queryCardBin(cardno);//SignHelper.cardInfo(cardno);//
		UserBankCardRet cardInfo = JSONObject.parseObject(bankJosn, UserBankCardRet.class);
//		UnionPayRet cardInfo = JSONObject.parseObject(bankJosn, UnionPayRet.class);
		if(cardInfo != null) {
			if(StringUtil.isNotBlank(cardInfo.getCard_type())){
//				if(!cardInfo.getCardType().equals("借记卡")){
				if(!cardInfo.getCard_type().equals("2")){ //2-储蓄卡  3-信用卡
					setRequestMsg(false,"暂不支持信用卡","请重新输入！");
					return goToURL(data);
				}
			}else{
				setRequestMsg(false,"银行卡信息有误","请重新输入！");
				return goToURL(data);
			}
		}else{
			setRequestMsg(false,"银行卡信息有误","请重新输入！");
			return goToURL(data);
		}
		return "";
	}
	
	/**
	 * 页面提示信息
	 * @param result 处理状态（true,false）
	 * @param title 提示标题
	 * @param msg 提示信息
	 */
	private void setRequestMsg(boolean result,String title,String msg) {
		request.setAttribute(ConstantUtil.RESULT, false);
		request.setAttribute("prompt", title);
		request.setAttribute("msgInfo", msg);
	}
	
	/**
	 * 根据类型返回相应页面
	 * @param payType
	 * @return
	 */
	private String goToURL(Map<String, Object> data) {
		request.setAttribute("data", data);
		saveToken("cfRechargeToken");
		request.setAttribute("cfRechargeToken", session.get("cfRechargeToken"));
		User user = getNBSessionUser();
		request.setAttribute("realName", user.getRealName());
		request.setAttribute("cardId", user.getCardId());
		request.setAttribute("mobile", user.getUserName());
		SystemConfig sc = systemConfigService.findByNid(Constant.OFFLINE_RECHARGE_MONEY);
		int minRechargeMoney = Integer.parseInt(sc.getValue());//线下充值最小金额
		request.setAttribute("minRechargeMoney", minRechargeMoney);
		
		double useMoney = theAccountService.getAccountUseMoney(user.getUserId());
        request.setAttribute("useMoney", useMoney);
        request.setAttribute("cfRechargeToken", session.get("cfRechargeToken"));
		List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
		if(bankList!=null && bankList.size()>0){
			AccountBank ab = bankList.get(0);
			request.setAttribute("bankModel", ab);
		    String bankCode = ab.getBankCode();
		    if(StringUtil.isNullOrBlank(bankCode)){
		    	bankCode = CommonRealize.bank_code_map.get(ab.getBank());
		    }
		    ChannelConfig cc = channelConfigService.getChannelConfigByCode(bankCode);
		    String wapChannelKey = cc.getWapRechargeKey();
		    String wapRechargeUrl = ConstantUtil.WAP_LLPAY_RECHARGE_URL;
		    if(wapChannelKey.equals(channelKey.unionpay.getValue())){
		    	wapRechargeUrl = ConstantUtil.WAP_UNIONPAY_RECHARGE_URL;
		    }
			request.setAttribute("wapRechargeUrl", wapRechargeUrl);//微信端充值入口
			request.setAttribute("wapChannelKey", wapChannelKey);
			request.setAttribute("bankCode", bankCode);
			request.setAttribute("userPayPwd", user.getPayPwd());
			List<AccountBank> ylbank = theAccountBankService.list(user.getUserId(),channelKey.unionpay.getValue());
			if(ylbank!=null && ylbank.size()>0){
				request.setAttribute("isYLBank", "1");
			}else{
				request.setAttribute("isYLBank", "0");
			}
			return "recharge1";
		}else{
			List<ChannelConfigModel> ccList =  channelConfigService.loadChannelInfo();
			request.setAttribute("bankModel", 0);
			request.setAttribute("ccList", ccList);
			return "recharge2";
		}
	}
	
	
	/**
	 * 微信充值结果处理(连连支付)
	 * 
	 * @return
	 */
	@Action(value="/cf/wechat/recharge/resultRechargewx",results = { @Result(name = "resultRechargewx", type = "ftl", location = "/nb/cf/wechat/recharge/resultRecharge_wx.html")})
	public String resultRechargewx() throws Exception {
		showPayResult();
		return "resultRechargewx";
	}
	
	/**
	 * 充值支付结果及业务处理
	 */
	private void showPayResult() {
		user  = getNBSessionUser();
		if(null!=user){			
			user = theUserService.getByUserId(user.getUserId());
			
			String orderNo = (String) session.get(Constant.SESSION_ORDER);
			
			OrderResultsRetBean ret = LianlPayHelper.queryOrder(orderNo);
			String flag0 = ret.getRet_code();//响应结果
			String flag1 = ret.getResult_pay();//充值结果
			if(flag0.equals("0000") && flag1.equals("SUCCESS")){//充值成功
				//业务处理成功后，session 用户认证信息变更
				UserIdentify identify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
				session.put(Constant.SESSION_USER_IDENTIFY, identify);
				session.put(ConstantUtil.SESSION_USER, user);
				request.setAttribute("msg", "恭喜您，充值成功！");
				request.setAttribute("rslt", "1");
				request.setAttribute("goUrl", "/cf/wechat/user/index.html");
			}else{
				request.setAttribute("msg", "抱歉，充值失败！");
				request.setAttribute("rslt", "0");
				request.setAttribute("goUrl", "/cf/wechat/user/recharge.html");
			}
			//更新充值入口信息
			accountRechargeService.updatePayMentByTradeNo(orderNo, "微信端连连充值");
		}
				
	}
	
	/**
	 * 检测该银行卡是否可以使用
	 * @throws Exception
	 */
	@Action("/cf/wechat/recharge/checkBankCanUse")
	public void checkBankCanUse() throws Exception {
		data = new HashMap<String, Object>();
		if(hasSessionUser()){	
			String cardNo = paramString("cardNo");
			boolean result = false;
			if(StringUtil.isNotBlank(cardNo)) {
				String bankJosn = LianlPayHelper.queryCardBin(cardNo);//SignHelper.cardInfo(cardNo);//
				UserBankCardRet cardInfo = JSONObject.parseObject(bankJosn, UserBankCardRet.class);
				if(cardInfo != null) {
					if(StringUtil.isNotBlank(cardInfo.getCard_type())){
						if(cardInfo.getCard_type().equals("2")){
							if(StringUtil.isNotBlank(cardInfo.getBank_name())){
								String bankCode = CommonRealize.bank_code_map.get(cardInfo.getBank_name());
								if(bankCode != null && bankCode!="") {
									NbSupportBank sb = theSupportBankService.loadSupportBankByCode(bankCode);
									data.put("supportBank", sb);
									result = true;
								} else {
									data.put("message", "不支持该银行");
								}
							} else {
								data.put("message", "未知银行卡");
							}
						}else{
							data.put("message", "不支持信用卡");
						}
					}else{
						data.put("message", "未知银行卡");
					}
				}else{
					data.put("message", "未知银行卡");
				}
			}
			data.put("result", result);
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 查询支持银行
	 * @return
	 */
	@Action(value="/cf/wechat/recharge/querySupportBank")
	public void querySupportBank() throws Exception {
		String bankCode = paramString("bank_code");
		SupportBank sb = null;
		if(bankCode==null || bankCode.equals("")){			
			sb = new SupportBank("16");//支付渠道类型，13PCweb端，16wap端
		}else{
			sb = new SupportBank(bankCode, "16");//支付渠道类型，13PCweb端，16wap端
		}
		String bankJosn = LianlPayHelper.querySupportBank(sb);
		printJson(bankJosn);
	}
	
	/**
	 * bin银行卡
	 * 
	 * @return
	 */
	@Action(value="/cf/wechat/recharge/binBank")
	public void binBank() throws Exception {
		String cardNo = paramString("cardNo").replace(" ", "");
		String bankJosn = LianlPayHelper.queryCardBin(cardNo);
		printJson(bankJosn);
	}
	
	/**
	 * 执行校验支付密码
	 * @throws IOException 
	 */
	@Action(value = "/cf/wechat/recharge/checkPayPwd" )
	public void executecheckPayPwd() throws IOException
	{
		data = new HashMap<String, Object>();
		
		if(hasSessionUser())
		{
			data.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
			
			String key = paramString("key");
			
			User user = getNBSessionUser();
			UserCache userCache = theUserCacheService.getById(user.getUserCache().getId());
			
			if(0==userCache.getPayPwdStatus())
			{
				if(MD5.encode(key).equals(user.getPayPwd()))
				{
					userCache.setPayFailTimes(0);
					theUserCacheService.updateUserCache(userCache);
					
					data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
					data.put(ConstantUtil.ERRORMSG,"验证成功!");
				}
				else
				{
					if(3==userCache.getPayFailTimes())
					{
						userCache.setPayFailTimes(4);
						theUserCacheService.updateUserCache(userCache);
						
						data.put(ConstantUtil.ERRORMSG,"交易输入错误4次，再次输错交易密码将会被锁定!");
						data.put(ConstantUtil.ERROR_CODE,4);
					}
					else if(4==userCache.getPayFailTimes())
					{
						userCache.setPayFailTimes(5);
						userCache.setPayPwdStatus(1);
						theUserCacheService.updateUserCache(userCache);
						
						data.put(ConstantUtil.ERRORMSG,"您的交易密码已被锁定，请联系客服!");
						data.put(ConstantUtil.ERROR_CODE,5);
					}
					else
					{
						userCache.setPayFailTimes(userCache.getPayFailTimes()+1);
						theUserCacheService.updateUserCache(userCache);
						
						data.put(ConstantUtil.ERRORMSG,"您的交易密码输错"+userCache.getPayFailTimes()+"次，连续输错5次交易密码将会锁定!");
						data.put(ConstantUtil.ERROR_CODE,userCache.getPayFailTimes());
					}
				}
			}
			else
			{
				data.put(ConstantUtil.ERRORMSG,"您的交易密码已被锁定，请联系客服!");
				data.put(ConstantUtil.ERROR_CODE, 5);
			}
		}
		else
		{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
}
