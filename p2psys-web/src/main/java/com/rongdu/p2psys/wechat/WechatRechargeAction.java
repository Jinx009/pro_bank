package com.rongdu.p2psys.wechat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.domain.PayOfflinebank;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.handler.ToPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.LianlPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderResultsRetBean;
import com.rongdu.p2psys.account.model.payment.llPay.model.SupportBank;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPay;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.PayOfflinebankService;
import com.rongdu.p2psys.account.service.PayOnlinebankService;
import com.rongdu.p2psys.account.service.PayService;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.payment.domain.ChannelConfig;
import com.rongdu.p2psys.nb.payment.model.ChannelConfigModel;
import com.rongdu.p2psys.nb.payment.service.IChannelBankService;
import com.rongdu.p2psys.nb.payment.service.IChannelConfigService;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.service.UserCacheService;

/**
 * 充值
 * @author cgw
 * @version 2.0
 * 2014-10-27
 */
@SuppressWarnings("rawtypes")
public class WechatRechargeAction extends BaseAction implements ModelDriven<AccountRechargeModel> {

	private static Logger logger = Logger.getLogger(WechatRechargeAction.class);
	private AccountRechargeModel model = new AccountRechargeModel();

	@Resource
	private AccountService theAccountService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private PayService payService;
	@Resource
	private PayOnlinebankService payOnlinebankService;
	@Resource
	private PayOfflinebankService payOfflinebankService;
	@Resource
	private UserService theUserService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private IChannelConfigService channelConfigService;
	@Resource
	private IChannelBankService channelBankService;
	@Resource
	private IOrderService orderService;
	@Resource
	private AccountCashService accountCashService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private SystemConfigService systemConfigService;
	
	private User user;
	public static final boolean isOpenAip = BaseTPPWay.isOpenApi();
	private Map<String, Object> data;
	
	/**
	 * 前往充值页面
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/nb/wechat/recharge/newRecharge",results = { @Result(name = "newRecharge", type = "ftl", location = "/nb/wechat/recharge/newRecharge.html")})
	public String newRecharge() throws Exception {
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		String operType = paramString("operType");
		if(StringUtil.isBlank(operType)){
			operType = "recharge";
		}
		session.put(Constant.RECHARGE_OPER_TYPE, operType);
		SystemConfig sc = systemConfigService.findByNid(Constant.OFFLINE_RECHARGE_MONEY);
		int minRechargeMoney = Integer.parseInt(sc.getValue());//线下充值最小金额
		request.setAttribute("minRechargeMoney", minRechargeMoney);
		logger.info("==================线下充值minRechargeMoney的最小金额111："+minRechargeMoney);
		List<PayOfflinebank> payOfflinebankList = payOfflinebankService.list(); // 线下银行列表
		request.setAttribute("payOfflinebankList", payOfflinebankList);
		double useMoney = theAccountService.getAccountUseMoney(user.getUserId());
        request.setAttribute("useMoney", useMoney);
		List<AccountBank> list = theAccountBankService.list(user.getUserId());
		
		request.setAttribute("bank_name","");
		request.setAttribute("bank_code","");
		if(list != null && list.size() > 0){
			AccountBank ab = list.get(0);
			request.setAttribute("accountBank", ab);
			request.setAttribute("bank_name", ab.getBank());
			
			String bankCode = ab.getBankCode();
		    if(StringUtil.isNullOrBlank(bankCode)){
		    	bankCode = CommonRealize.bank_code_map.get(ab.getBank());
		    }
		    ChannelConfig cc = channelConfigService.getChannelConfigByCode(bankCode);
		    String webChannelKey = cc.getWebRechargeKey();
		    String webRechargeUrl = ConstantUtil.WEB_LLPAY_RECHARGE_URL;
		    if(webChannelKey.equals(channelKey.unionpay.getValue())){
		    	webRechargeUrl = ConstantUtil.WEB_UNIONPAY_RECHARGE_URL;
		    }
		    request.setAttribute("webRechargeUrl", webRechargeUrl);//PC端充值入口
			request.setAttribute("webChannelKey", webChannelKey);
			request.setAttribute("bank_code", bankCode);
			request.setAttribute("userPayPwd", user.getPayPwd());
		}else{
			List<ChannelConfigModel> ccList =  channelConfigService.loadChannelInfo();
			request.setAttribute("bankModel", 0);
			request.setAttribute("ccList", ccList);
		}
		logger.info("==================线下充值minRechargeMoney的最小金额222："+minRechargeMoney);
		return "newRecharge";
	}
	
	@Override
	public AccountRechargeModel getModel() {
		return model;
	}
	
	/**
	 * 前往充值确认（绑卡）页面
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/nb/wechat/recharge/sureRecharge",results = { @Result(name = "checkllCode", type = "ftl", location = "/nb/wechat/recharge/checkllCode.html"),
			@Result(name = "checkylCode", type = "ftl", location = "/nb/wechat/recharge/checkylCode.html"),
			@Result(name = "line_recharge", type = "ftl", location = "/nb/wechat/recharge/line_recharge.html")})
	public String sureRecharge() throws Exception {
		saveToken("wapRechargeToken");
		int num = paramInt("rechargeType");
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		request.setAttribute("money", paramString("money"));
		UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
		session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
		if(num==1){//线上
			String retName = "checkllCode";
			String bankCode = paramString("bankCodeKey");
			ChannelConfig cc = channelConfigService.getChannelConfigByCode(bankCode);	
			String wapChannelKey = cc.getWapRechargeKey();
		    String wapRechargeUrl = ConstantUtil.WAP_LLPAY_RECHARGE_URL;
		    if(wapChannelKey.equals(channelKey.unionpay.getValue())){
		    	wapRechargeUrl = ConstantUtil.WAP_UNIONPAY_RECHARGE_URL;
		    	retName = "checkylCode";
		    }
		    String mobile = user.getMobilePhone();
		    if(StringUtil.isNullOrBlank(mobile)){
		    	mobile = user.getUserName();
		    }
		    request.setAttribute("mobile", user.getMobilePhone());
		    request.setAttribute("isBindBank", 0);
			request.setAttribute("bankCodeKey", bankCode);
			request.setAttribute("wapRechargeUrl", wapRechargeUrl);//微信端充值入口
			request.setAttribute("wapChannelKey", wapChannelKey);
			List<AccountBank> bankList = theAccountBankService.list(user.getUserId(), cc.getWapRechargeKey());
			if(bankList == null || bankList.size() == 0){
				bankList = theAccountBankService.list(user.getUserId());
				if(bankList != null && bankList.size() > 0){
					AccountBank ab = bankList.get(0);
					request.setAttribute("bindBank", ab);
					request.setAttribute("noAgree", "");
					request.setAttribute("bindId", "");
				}
			}else{
				AccountBank bank = bankList.get(0);
				request.setAttribute("bindBank", bank);
				request.setAttribute("noAgree", bank.getBindId());
				request.setAttribute("bindId", bank.getBindId());
				request.setAttribute("isBindBank", 1);
			}
			return retName;
		}else{//线下
			String realName = user.getRealName();
			request.setAttribute("realName", realName);
			request.setAttribute("cardId", user.getCardId());
			return "line_recharge";
		}
	}
	
	
	/**
	 * 连连在线充值
	 * 
	 * @throws Exception
	 */
	@Action(value="/nb/wechat/recharge/dollPayRecharge",results = { @Result(name = "dollPayRecharge", type = "ftl", location = "/nb/wechat/recharge/dollPayRecharge.html"),
			@Result(name = "resultRechargewx", type = "ftl", location = "/nb/wechat/recharge/resultRecharge_wx.html")})
	public String dollPayRecharge() throws Exception {
		String msg = checkWAPToken("wapRechargeToken");
		if(msg.equals("-1")){
			user  = (User)request.getSession().getAttribute(ConstantUtil.SESSION_USER);
			user = theUserService.getByUserId(user.getUserId());
			model.setUser(user);
			UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			
			String accid = "";
			String accname = "";
			if(null!=userIdentify && userIdentify.getRealNameStatus() == 1){
				accid = user.getCardId();
				accname = user.getRealName();
			}else{
				accid = paramString("accId");
				accname = paramString("accName");
				
				if(null!=userIdentify && StringUtil.isBlank(accid)){
					accid = userIdentify.getUser().getCardId();
				}
				if(null!=userIdentify && StringUtil.isBlank(accname)){
					accname = userIdentify.getUser().getRealName();
				}
			}
			
			String no_agree = paramString("no_agree");
			String mobile = paramString("mobile");
			String cardno = paramString("card_no").replace(" ", "");
			String name_goods = paramString("name_goods");
			String money_order = paramString("money_order");
			long productId = paramLong("productId");
			
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
				String cab = checkAddBank(user.getUserId(),accid, cardno,channelKey.llpay.getValue(),"resultRechargewx");
				if(!StringUtil.isBlank(cab)){
					saveToken("wapRechargeToken");
					return cab; 
				}
			}
			// 创建订单
			OrderInfo order = LianlPayHelper.createOrder(0,name_goods,money_order);
			// 将订单信息保存数据
			if(productId>0){
				ProductBasic pb = new ProductBasic();
				pb.setId(productId);
				order.setPb(pb);
			}
			orderService.saveOrder(user,order);
			logger.info("=============连连支付订单报文1111111：" + JSON.toJSONString(order));
			session.put(Constant.SESSION_ORDER, order.getNo_order());
			//支付参数封装
			LianlPay llpay = accountRechargeService.setllpay(request,user,no_agree,accname,accid,mobile,cardno);
			logger.info("=============连连支付支付报文1111111：" + JSON.toJSONString(llpay));
			//充值处理
			ToPay.prepositPay(1,request,llpay, order);
			return "dollPayRecharge";
		}
		request.setAttribute("goUrl", "/nb/wechat/recharge/newRecharge.html");
		if(msg.equals("0")){
			saveToken("wapRechargeToken");
			request.setAttribute("msg", "信息有误，请重试！");
		}else{
			request.setAttribute("msg", "请勿重复提交！");
		}
		request.setAttribute("rslt", "0");
		return "resultRechargewx";
	}


	/**
	 * 微信充值结果处理(连连支付)
	 * 
	 * @return
	 */
	@Action(value="/nb/wechat/recharge/resultRechargewx",results = { @Result(name = "resultRechargewx", type = "ftl", location = "/nb/wechat/recharge/resultRecharge_wx.html")})
	public String resultRechargewx() throws Exception {
		showPayResult();
		return "resultRechargewx";
	}
	
	/**
	 * 充值支付结果及业务处理
	 */
	private void showPayResult() {
		user  = (User)request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		
		String orderNo = (String) session.get(Constant.SESSION_ORDER);
		String operType = (String) session.get(Constant.RECHARGE_OPER_TYPE);

		OrderResultsRetBean ret = LianlPayHelper.queryOrder(orderNo);
		String flag0 = ret.getRet_code();//响应结果
		String flag1 = ret.getResult_pay();//充值结果
		if(flag0.equals("0000") && flag1.equals("SUCCESS")){//充值成功
			//业务处理成功后，session 用户认证信息变更
			UserIdentify identify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			session.put(Constant.SESSION_USER_IDENTIFY, identify);
			request.setAttribute("msg", "恭喜您，充值成功！");
			request.setAttribute("rslt", "1");
			if(operType.equals("attestation")){//认证
				List<ProductBasic> prodList = productBasicService.getExperienceProductList();
				if(prodList!=null && prodList.size()>0){
					request.setAttribute("goUrl", "/nb/wechat/product/productDetail.action?product_id="+prodList.get(0).getId());//去体验标详情页面
				}
			}else{
				request.setAttribute("goUrl", "/nb/wechat/account/main.html");
			}
		}else{
			request.setAttribute("msg", "抱歉，充值失败！");
			request.setAttribute("rslt", "0");
			if(operType.equals("attestation")){//认证
				request.setAttribute("goUrl", "/nb/wechat/account/experienceGold.html");
			}else{
				request.setAttribute("goUrl", "/nb/wechat/recharge/newRecharge.html");
			}
		}
		//更新充值入口信息
		accountRechargeService.updatePayMentByTradeNo(orderNo, "微信端连连充值");
	}
	
	/**
	 * 银联在线充值
	 * addBank_recharge.html
	 * @throws Exception
	 */
	@Action(value="/nb/wechat/recharge/doUnionPayRecharge",results = { @Result(name = "resultRechargewx", type = "ftl", location = "/nb/wechat/recharge/resultRecharge_wx.html"),
			@Result(name = "checkylCode", type = "ftl", location = "/nb/wechat/recharge/checkylCode.html"),
			@Result(name = "addBank_wx", type = "ftl", location = "/nb/wechat/cash/addBank_wx.html")})
	public String doUnionPayRecharge() throws Exception {
		String msg = checkWAPToken("wapRechargeToken");
		String accId = paramString("accId");
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
		if(msg.equals("-1")){
			saveToken("wapRechargeToken");
			user  = (User)request.getSession().getAttribute(ConstantUtil.SESSION_USER);
			user = theUserService.getByUserId(user.getUserId());
			model.setUser(user);
			List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
			if(bankList.size()>0){
				cardNo = bankList.get(0).getBankNo();
				request.setAttribute("isBindBank", 0);
				request.setAttribute("bindBank", bankList.get(0));
				request.setAttribute("bindId", "");
			}
			List<AccountBank> abList = theAccountBankService.list(user.getUserId(),channelKey.unionpay.getValue());
			String payment = "微信端银联充值";
			if(abList.size()>0){
				request.setAttribute("isBindBank", 1);
				request.setAttribute("bindBank", abList.get(0));
				request.setAttribute("bindId", abList.get(0).getBindId());
				// 验证手机验证码是否正确
				String yzm = paramString("code");
				Map<String, Object> map = ((Map<String, Object>) request.getSession().getAttribute("cashGetCode_code"));
				if (map == null || !yzm.equals(map.get("code").toString())){
					request.setAttribute("msg", "验证码错误！");
					return "checkylCode";
				}
				data = accountRechargeService.doUnionPay(payment,model,abList.get(0));
				rechargeOper(data);
			}else{
				UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
				
				if(userIdentify.getRealNameStatus() == 1){
					accId = user.getCardId();
					accName = user.getRealName();
					mobile = user.getMobilePhone();
				}
				if(bankList.size()>0){
					cardNo = bankList.get(0).getBankNo();
				}
				String code = paramString("code");
				
				request.setAttribute("cardNo", cardNo);
				request.setAttribute("mobile", mobile);
				request.setAttribute("accName", accName);
				
				UnionPay pay = new UnionPay(cardNo, accName, accId, mobile, code);
				String cab = checkAddBank(user.getUserId(),accId, cardNo,channelKey.unionpay.getValue(),"checkylCode");
				if(cab!=""){
					return cab;
				}
				UnionPayRet ret = SignHelper.cardBindByMC(pay);
				if(ret == null) {
					request.setAttribute("msg", "银联暂无响,请稍后重试!");
					return "checkylCode";
				}
				if("0000".equals(ret.getRetCode())){
					AccountBank nbank = bindSuccess(userIdentify, accId, accName, cardNo, mobile, ret);
					data = accountRechargeService.doUnionPay(payment,model,nbank);
					rechargeOper(data);
				}else if((ret.getRetDesc()).indexOf("短信")>=0){
					request.setAttribute("msg", "验证码错误,请重新输入!");
					return "checkylCode";
				}else if((ret.getRetDesc()).indexOf("身份认证")>=0){
					request.setAttribute("msg", "身份认证失败,请检查后重新尝试!");
					return "checkylCode";
				}else {
					//失败重新调用3.5.2 随机付款
					UnionPay pay1 = new UnionPay(cardNo, accName, accId);
					UnionPayRet ret1 = SignHelper.randomPay(pay1);
					if("0000".equals(ret1.getRetCode())){
						logger.info("=======================================发送随机认证返回结果===成功！");
						request.setAttribute("accId", accId);
						request.setAttribute("accName", accName);
						request.setAttribute("cardNo", cardNo);
						request.setAttribute("mobile", mobile);
						return "addBank_wx";
					}else if("606B".equals(ret1.getRetCode().toUpperCase())){
						logger.info("=======================================发送随机认证返回结果==="+ret1.getRetDesc());
						request.setAttribute("accId", accId);
						request.setAttribute("accName", accName);
						request.setAttribute("cardNo", cardNo);
						request.setAttribute("mobile", mobile);
						return "addBank_wx";
					}else{
						logger.info("=======================================发送随机认证返回结果==="+ret1.getRetDesc());
						request.setAttribute("msg", ret1.getRetDesc());
						return "checkylCode";
					}
				}
			}
		}else{
			if(msg.equals("0")){
				saveToken("wapRechargeToken");
				request.setAttribute("msg", "信息有误，请重试！");
			}else{
				request.setAttribute("msg", "请勿重复提交！");
			}
			request.setAttribute("rslt", "0");
			request.setAttribute("goUrl", "/nb/wechat/recharge/newRecharge.html");
		}
		return "resultRechargewx";
	}

	/**
	 * 银联充值完成业务处理
	 * @param data
	 */
	private void rechargeOper(Map<String, Object> data) {
		if((Boolean) data.get("result")){
			//充值成功
			request.setAttribute("msg", "恭喜您，充值成功！");
			request.setAttribute("rslt", "1");
			String operType = (String) session.get(Constant.RECHARGE_OPER_TYPE);
			if(operType.equals("attestation")){//认证
				List<ProductBasic> prodList = productBasicService.getExperienceProductList();
				if(prodList!=null && prodList.size()>0){
					request.setAttribute("goUrl", "/nb/wechat/product/productDetail.action?product_id="+prodList.get(0).getId());//去体验标详情页面
				}
			}else{
				request.setAttribute("goUrl", "/nb/wechat/account/main.html");
			}
		}else{
			//充值失败
			request.setAttribute("rslt", "0");
			request.setAttribute("msg", data.get("message").toString());
			request.setAttribute("goUrl", "/nb/wechat/recharge/newRecharge.html");
		}
	}
	
	/**
	 * 绑定银行卡（随机付款验证）
	 * 
	 * @return
	 */
	@Action(value="/nb/wechat/cash/addRandomBank",results = { @Result(name = "checkylCode", type = "ftl", location = "/nb/wechat/recharge/checkylCode.html"),
			@Result(name = "addBank_wx", type = "ftl", location = "/nb/wechat/cash/addBank_wx.html"),
			@Result(name = "authSuccess", type = "ftl", location = "/nb/wechat/cash/authSuccess.html"),
			@Result(name = "authError", type = "ftl", location = "/nb/wechat/cash/authError.html")})
	public String addRandomBank() throws Exception {
		String msg = checkWAPToken("wapRandomToken");
		String accId = paramString("accId");
		String accName = paramString("accName");
		String cardNo = paramString("cardNo").replace(" ", "");
		String mobile = paramString("mobile");
		
		String cashmoney = paramString("cashmoney");
		request.setAttribute("money", cashmoney);
		request.setAttribute("cardNo", cardNo);
		request.setAttribute("mobile", mobile);
		request.setAttribute("accName", accName);
		request.setAttribute("accId", accId);
		if(msg.equals("-1")){
			user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
			user = theUserService.getByUserId(user.getUserId());
			UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			model.setUser(user);
			//String orderNo = paramString("orderNo");
			double d = paramDouble("money");
			long money = (long)(d*100);
			UnionPay pay = new UnionPay(money,cardNo, accName, accId);//, orderNo);
			UnionPayRet ret = SignHelper.paymentBind(pay);
			String payment = "微信端银联充值";
			if(ret == null) {
				request.setAttribute("msg", "银联暂无响应！");
				return "addBank_wx";
			}else{
				if("0000".equals(ret.getRetCode())){
					logger.info("=======================================3.1.4绑定成功==="+ret.getRetDesc());
					AccountBank nbank = bindSuccess(userIdentify, accId, accName, cardNo, mobile, ret);
					data = accountRechargeService.doUnionPay(payment,model,nbank);
					rechargeOper(data);
					return "authSuccess";
				}else{
					logger.info("=======================================3.1.4绑定失败==="+ret.getRetDesc());
					request.setAttribute("msg", "随机金额绑卡验证失败！");
					return "addBank_wx";
				}
			}
		}else{
			if(msg.equals("0")){
				saveToken("wapRandomToken");
				request.setAttribute("msg", "信息有误，请重试！");
			}else{
				request.setAttribute("msg", "请勿重复提交！");
			}
			request.setAttribute("goUrl", "/nb/wechat/recharge/newRecharge.html");
			return "authError";
		}
	}


	/**
	 * 绑卡成功业务处理
	 * @param userIdentify
	 * @param accId
	 * @param accName
	 * @param cardNo
	 * @param mobile
	 * @param ret
	 */
	private AccountBank bindSuccess(UserIdentify userIdentify, String accId,
			String accName, String cardNo, String mobile, UnionPayRet ret) {
		user  = (User)request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		user = theUserService.getByUserId(user.getUserId());
		AccountBank ab = null;
		//先查询银行卡信息
		UnionPayRet cardInfo = SignHelper.cardInfo(cardNo);
		String bankName = cardInfo.getBankName();
		if("0000".equals(cardInfo.getRetCode())){
			ab = new AccountBank(user, cardNo, cardInfo.getBankName(), "/data/bank/mini/"+bankName+".png");
			ab.setMobile(mobile);
			ab.setBindId(ret.getBindId());
			ab.setStatus(1);
			ab.setChannelKey(channelKey.unionpay.getValue());
			ab.setBankCode(CommonRealize.bank_code_map.get(bankName));
			theAccountBankService.saveAccountBank(ab);
			//添加至通道银行卡管理表
			channelBankService.saveChannelBank(user, ab,ConstantUtil.channelKey.unionpay.getValue());
		}
		
		//该绑卡接口带有实名校验，所以绑卡成功即自动完成实名认证
		if(userIdentify.getRealNameStatus() != 1){
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
		    userCacheService.update(userCache);
			
			UserIdentify identify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			identify.setRealNameStatus(1);
			identify.setRealNameVerifyTime(new Date());
			identify.setUser(user);
			theUserIdentifyService.updateUserIdentify(identify);
			//业务处理成功后，session 用户认证信息变更
			session.put(Constant.SESSION_USER_IDENTIFY, identify);
			//发放实名红包
//			userRedPacketService.doRedPacket(RedPacket.REALNAME, user, null, null);

			// 业务处理成功后，session 用户认证信息变更
			session.put(Constant.SESSION_USER_IDENTIFY, identify);
			session.put(ConstantUtil.SESSION_USER, user);
		}
		return ab;
	}
	
	/**
	 * 线下充值
	 * 
	 * @return
	 */
	@Action(value="/nb/wechat/recharge/dolineRecharge",results = { @Result(name = "resultRechargewx", type = "ftl", location = "/nb/wechat/recharge/resultRecharge_wx.html"),
			@Result(name = "lineRecharge_success", type = "ftl", location = "/nb/wechat/recharge/lineRecharge_success.html")})
	public String dolineRecharge() throws Exception {
		try {
			user  = (User)request.getSession().getAttribute(ConstantUtil.SESSION_USER);
			model.setType(3);//线下充值
			// 创建订单
			OrderInfo order = LianlPayHelper.createOrder(0,"微信线下充值申请", String.valueOf(model.getMoney()));
			
			double offlineRechargefee = 0; // Global.getValue("online_rechargefee"),
			// 待完善
			double fee = BigDecimalUtil.mul(model.getMoney(), offlineRechargefee);
			AccountRecharge recharge = model.prototype(user, fee);
			
			// 将订单信息保存数据
			order.setNo_order(recharge.getTradeNo());
			orderService.saveOrder(user,order);
						
			recharge.setUser(user);
			recharge.setPayOfflinebank(payOfflinebankService.find(model.getPayOfflinebankId()));
			recharge.setType(3);// 线下充值
			recharge.setMoney(model.getMoney());
			recharge.setAmountIn(BigDecimalUtil.sub(model.getMoney(), fee));
			recharge.setBankNo(model.getBankNo().replace(" ", ""));
			recharge.setRealName(model.getRealName());
			recharge.setCardId(model.getCardId());
			recharge.setRemark("姓名："+model.getRealName()+",身份证号："+model.getCardId()+",银行卡号："+model.getBankNo().replace(" ", ""));
			recharge.setPayment("微信端线下充值");
			recharge.setChannelKey(ConstantUtil.OFFLINE_RECHARGE);
			accountRechargeService.add(recharge);
			// 发送确认短信到该用户注册的手机中
//			accountCashService.sendSMSNotice(NoticeConstant.RECHARGE_OFFLINE_NOTICE,user, model.getMoney());			
			//充值成功
			request.setAttribute("msg", "充值申请成功！");
			request.setAttribute("rslt", "1");
			return "lineRecharge_success";
		} catch (Exception e) {
			e.printStackTrace();
			//充值失败
			request.setAttribute("rslt", "0");
			request.setAttribute("msg", "充值申请失败！");
			request.setAttribute("goUrl", "/nb/wechat/recharge/newRecharge.html");
			return "resultRechargewx";
		}
	}
	/**
	 * 查询支持银行
	 * @return
	 */
	@Action(value="/nb/wechat/recharge/querySupportBank")
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
	 * 验证绑卡认证信息
	 * @param userId
	 * @param accid
	 * @param cardno
	 */
	private String checkAddBank(long userId,String accid, String cardno,String channelKey,String goUrl) {
		//先根据身份证号查询是否已被使用
		int userNum = theUserService.countByCardId(accid,channelKey);
		if(userNum>0){
			request.setAttribute("msg", "该身份证已被使用,请重新输入!");
			return goUrl;
		}
		UnionPayRet cardInfo = SignHelper.cardInfo(cardno);
		if(cardInfo != null) {
			if(StringUtil.isNotBlank(cardInfo.getCardType())){
				if(!cardInfo.getCardType().equals("借记卡")){
					request.setAttribute("msg", "暂不支持信用卡,请重新输入!");
					return goUrl;
				}
			}else{
				request.setAttribute("msg", "银行卡信息有误,请重新输入!");
				return goUrl;
			}
		}else{
			request.setAttribute("msg", "银行卡信息有误,请重新输入!");
			return goUrl;
		}
		return "";
	}
}
