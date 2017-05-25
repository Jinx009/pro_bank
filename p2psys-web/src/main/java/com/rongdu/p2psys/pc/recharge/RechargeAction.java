package com.rongdu.p2psys.pc.recharge;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.handler.ToPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.LianlPay;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderResultsRetBean;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPay;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
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
import com.rongdu.p2psys.nb.payment.service.ISupportBankService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserCacheModel;


/***
 * 充值
 * @author cgw
 * 2014-8-25
 */
@SuppressWarnings("rawtypes")
public class RechargeAction extends BaseAction implements ModelDriven<AccountRechargeModel> {

	private static Logger logger = Logger.getLogger(RechargeAction.class);
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
	private UserCacheService theUserCacheService;
	@Resource
	private IChannelConfigService channelConfigService;
	@Resource
	private IChannelBankService channelBankService;
	@Resource
	private IOrderService orderService;
	@Resource
	private ISupportBankService theSupportBankService;
	@Resource
	private SystemConfigService systemConfigService;
	
	private User user;
	private Map<String, Object> data;
	public static final boolean isOpenAip = BaseTPPWay.isOpenApi();

	/**
	 * 前往充值页面
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/nb/pc/recharge/newRecharge",results = { @Result(name = "newRecharge", type = "ftl", location = "/nb/pc/recharge/newRecharge.html")})
	public String newRecharge() throws Exception {
		saveToken("nbRechargeToken");
		return "newRecharge";
	}
	
	@Action(value="/nb/pc/recharge/addBank",results = { @Result(name = "addBank", type = "ftl", location = "/nb/pc/cash/addBank.html")})
	public String addBank() throws Exception {
		return "addBank";
	}
	
	/**
	 * 充值数据
	 * @throws IOException 
	 */
	@Action("/nb/pc/recharge/newRechargeOper")
	public void newRechargeOper() throws IOException{
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			SystemConfig sc = systemConfigService.findByNid(Constant.OFFLINE_RECHARGE_MONEY);
			int minRechargeMoney = Integer.parseInt(sc.getValue());//线下充值最小金额
			data.put("minRechargeMoney", minRechargeMoney);
			
			saveToken("nbRechargeToken");
			double useMoney = theAccountService.getAccountUseMoney(user.getUserId());
	        data.put("useMoney", useMoney);
	        data.put("nbRechargeToken", session.get("nbRechargeToken"));
			List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
			if(bankList!=null && bankList.size()>0){
				AccountBank ab = bankList.get(0);
				data.put("bankModel", ab);
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
				data.put("webRechargeUrl", webRechargeUrl);//PC端充值入口
				data.put("webChannelKey", webChannelKey);
				data.put("bankCode", bankCode);
				data.put("userPayPwd", user.getPayPwd());
				List<AccountBank> ylbank = theAccountBankService.list(user.getUserId(),channelKey.unionpay.getValue());
				if(ylbank!=null && ylbank.size()>0){
					data.put("isYLBank", "1");
				}else{
					data.put("isYLBank", "0");
				}
			}else{
//				List<NbSupportBank> nbsbList = theSupportBankService.loadSupportBankList();
				List<ChannelConfigModel> ccList =  channelConfigService.loadChannelInfo();
				data.put("bankModel", 0);
				data.put("ccList", ccList);
			}
			String realName = user.getRealName();
			String cardId = user.getCardId();
			String mobile = user.getUserName();
			data.put("realName", realName);
			data.put("cardId", cardId);
			data.put("mobile", mobile);
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}

	
	/**
	 * 银联在线充值
	 * 
	 * @throws Exception
	 */
	@Action(value="/nb/pc/recharge/doUnionPayRecharge",results = { @Result(name = "newRecharge", type = "ftl", location = "/nb/pc/recharge/newRecharge.html"),
			@Result(name = "addBank", type = "ftl", location = "/nb/pc/cash/addBank.html")})
	public String doUnionPayRecharge() throws Exception {
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			String tokenMsg = checkToken("nbRechargeToken");
			if(!StringUtil.isBlank(tokenMsg)){
				request.setAttribute(ConstantUtil.RESULT, false);
				request.setAttribute("prompt", tokenMsg);
				request.setAttribute("msgInfo", "请稍后再试！");
				return goToURL(data);
			}
			user = getNBSessionUser();
			request.setAttribute("webChannelKey", "银联支付");
			request.setAttribute("bankCodeKey", paramString("bankCodeKey"));
			List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
			if(bankList.size()>0){
				request.setAttribute("isSecond", 2);
			}else{
				request.setAttribute("isSecond", 1);
			}
			
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
			
			String payment = "PC端银联充值";
			model.setUser(user);
			List<AccountBank> abList = theAccountBankService.list(user.getUserId(),channelKey.unionpay.getValue());
			if(abList.size()>0){				
				String payPwd = MD5.encode(paramString("payPwd"));
				checkPwd(payPwd);
				data = accountRechargeService.doUnionPay(payment,model,abList.get(0));
				request.setAttribute(ConstantUtil.RESULT, data.get(ConstantUtil.RESULT));
				request.setAttribute("msg", data.get("message"));
				return goToURL(data);
			}else{
				UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
				
				if(userIdentify.getRealNameStatus() == 1){
					accId = user.getCardId();
					accName = user.getRealName();
					mobile = user.getMobilePhone();
				}
//				List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
				if(bankList.size()>0){
					String payPwd = MD5.encode(paramString("payPwd"));
					checkPwd(payPwd);
					cardNo = bankList.get(0).getBankNo();
				}
				String code = paramString("code");
				
				request.setAttribute("cardNo", cardNo);
				request.setAttribute("mobile", mobile);
				request.setAttribute("accName", accName);
				
				UnionPay pay = new UnionPay(cardNo, accName, accId, mobile, code);
				String cab = checkAddBank(user.getUserId(),accId, cardNo,channelKey.unionpay.getValue());
				if(cab!=""){
					return cab;
				}
				//校验参数
//				pay.checkAddBank();
				UnionPayRet ret = SignHelper.cardBindByMC(pay);
				if(ret == null) {
					request.setAttribute(ConstantUtil.RESULT, false);
					request.setAttribute("prompt", "银联暂无响");
					request.setAttribute("msgInfo", "请稍后重试！");
					return goToURL(data);
				}
				if("0000".equals(ret.getRetCode())){
					AccountBank nbank = bindSuccess(userIdentify, accId, accName, cardNo, mobile, ret);
					data = accountRechargeService.doUnionPay(payment,model,nbank);
					request.setAttribute(ConstantUtil.RESULT, data.get(ConstantUtil.RESULT));
					request.setAttribute("msg", data.get("message"));
					return goToURL(data);
				}else if((ret.getRetDesc()).indexOf("短信")>=0){
					request.setAttribute(ConstantUtil.RESULT, false);
					request.setAttribute("prompt", "验证码错误");
					request.setAttribute("msgInfo", "请重新输入！");
					return goToURL(data);
				}else if((ret.getRetDesc()).indexOf("身份认证")>=0){
					request.setAttribute(ConstantUtil.RESULT, false);
					request.setAttribute("prompt", "身份认证失败");
					request.setAttribute("msgInfo", "请检查后重新尝试！");
					return goToURL(data);
					
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
						request.setAttribute("data", getStringOfJpaObj(data));
						return "addBank";
					}else if("606B".equals(ret1.getRetCode().toUpperCase())){
						logger.info("=======================================发送随机认证返回结果==="+ret1.getRetDesc());
						request.setAttribute("accId", accId);
						request.setAttribute("accName", accName);
						request.setAttribute("cardNo", cardNo);
						request.setAttribute("mobile", mobile);
						request.setAttribute("data", getStringOfJpaObj(data));
						return "addBank";
					}else{
						logger.info("=======================================发送随机认证返回结果==="+ret1.getRetDesc());
						request.setAttribute(ConstantUtil.RESULT, false);
						request.setAttribute("prompt", ret1.getRetDesc());
						request.setAttribute("msgInfo", "请稍后重试！");
						return goToURL(data);
					}

				}
			}
		}else{
//			data = getErrorMap();
			request.setAttribute("isLogin", ConstantUtil.NO_LOGIN_USER);
		}
		return goToURL(data);
	}
	
	/**
	 * 根据类型返回相应页面
	 * @param payType
	 * @return
	 */
	private String goToURL(Map<String, Object> data) {
		request.setAttribute("data", data);
		saveToken("nbRechargeToken");
		request.setAttribute("nbRechargeToken", session.get("nbRechargeToken"));
		return "newRecharge";
	}
	
	/***
	 * 验证交易密码
	 * @param payPwd
	 */
	private void checkPwd(String payPwd) {
		if (null != payPwd && !payPwd.equals(user.getPayPwd())) {
			theUserCacheService.doLock(request, user.getUserId(), UserCacheModel.PAY_PWD_LOCK);
			request.setAttribute(ConstantUtil.RESULT, false);
			request.setAttribute("prompt", "支付密码不正确");
			request.setAttribute("msgInfo", "请重新输入！");
		}
	}
	
	/**
	 * 绑定银行卡（随机付款验证）
	 * 
	 * @return
	 */
	@Action(value="/nb/pc/recharge/addRandomBank",results = { @Result(name = "newRecharge", type = "ftl", location = "/nb/pc/recharge/newRecharge.html"),
			@Result(name = "addBank", type = "ftl", location = "/nb/pc/cash/addBank.html")})
	public String addRandomBank() throws Exception {
		if(hasSessionUser()){			
			user = getNBSessionUser();
			model.setUser(user);
			UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			String accId = paramString("accId");
			String accName = paramString("accName");
			String cardNo = paramString("cardNo").replace(" ", "");
			String mobile = paramString("mobile");
			
			String cashmoney = paramString("cashmoney");
			request.setAttribute("money", cashmoney);
			request.setAttribute("cardNo", cardNo);
			request.setAttribute("mobile", mobile);
			request.setAttribute("accName", accName);
			
			request.setAttribute("webChannelKey", paramString("webChannelKey"));
			request.setAttribute("bankCodeKey", paramString("bankCodeKey"));
			
			//String orderNo = paramString("orderNo");
			double d = paramDouble("money");
			long money = (long)(d*100);
			UnionPay pay = new UnionPay(money,cardNo, accName, accId);//, orderNo);
			UnionPayRet ret = SignHelper.paymentBind(pay);
			if(ret == null) {
//			throw new AccountException("银联暂无响应，请稍后重试", 1);
				request.setAttribute(ConstantUtil.RESULT, false);
				request.setAttribute("prompt", "银联暂无响应");
				request.setAttribute("msgInfo", "请稍后重试！");
				return "addBank";
			}
			String payment = "PC端银联充值";
			if("0000".equals(ret.getRetCode())){
				logger.info("=======================================3.1.4绑定成功==="+ret.getRetDesc());
				AccountBank nbank = bindSuccess(userIdentify, accId, accName, cardNo, mobile, ret);
				data = accountRechargeService.doUnionPay(payment,model,nbank);
				request.setAttribute(ConstantUtil.RESULT, data.get(ConstantUtil.RESULT));
				request.setAttribute("msg", data.get("message"));
				return goToURL(data);
			}else{
				logger.info("=======================================3.1.4绑定失败==="+ret.getRetDesc());
				request.setAttribute(ConstantUtil.RESULT, false);
				request.setAttribute("prompt", "随机金额绑卡验证失败");
				request.setAttribute("msgInfo", "请重新尝试！");
				return "addBank";
			}
		}else{
			request.setAttribute("11", ConstantUtil.NO_LOGIN_USER);
			return goToURL(data);
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
/*			//检测身份证号是否已被使用过
			int count = theUserService.countByCardId(accId);
			if(count > 0){
//				throw new AccountException("身份证号已被占用", 1);
				request.setAttribute("msg", "对不起，身份证号已被占用！");
			}*/
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
			theUserIdentifyService.updateUserIdentify(userIdentify);
			
			//发放实名红包
//			theUserRedPacketService.doRedPacket(RedPacket.REALNAME, user, null, null);
		}
		return ab;
	}

	/**
	 * 进行线下充值
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value="/nb/pc/recharge/doOfflineRecharge",results = { @Result(name = "newRecharge", type = "ftl", location = "/nb/pc/recharge/newRecharge.html")})
	public String doOfflineRecharge() throws Exception {
		if(hasSessionUser()){			
			try {
				user = getNBSessionUser();
//				UserIdentify userIdentify = theUserIdentifyService.findByUserId(user.getUserId());
//				model.validIdentifyForRecharge(userIdentify);
//				model.validNewRecharge();
				String tokenMsg = checkToken("nbRechargeToken");
				if(!StringUtil.isBlank(tokenMsg)){
					request.setAttribute(ConstantUtil.RESULT, false);
					request.setAttribute("prompt", tokenMsg);
					request.setAttribute("msgInfo", "请稍后再试！");
					return goToURL(data);
				}
				double offlineRechargefee = 0; // Global.getValue("online_rechargefee"),
				model.setType(3);//线下充值
				// 待完善
				double fee = BigDecimalUtil.mul(model.getMoney(), offlineRechargefee);
				// 创建订单
				OrderInfo order = LianlPayHelper.createOrder(0,"PC线下充值申请", String.valueOf(model.getMoney()));
				
				AccountRecharge recharge = model.prototype(user, fee);
				// 将订单信息保存数据
				order.setNo_order(recharge.getTradeNo());
				orderService.saveOrder(user,order);
				recharge.setPayOfflinebank(payOfflinebankService.find(model.getPayOfflinebankId()));
				recharge.setType(3);// 线下充值
				recharge.setAmountIn(BigDecimalUtil.sub(recharge.getMoney(), fee));
				recharge.setBankNo(recharge.getBankNo());
				String realname = recharge.getRealName();
				String cardid = recharge.getCardId();
				UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
				if(userIdentify.getRealNameStatus() == 1){
					realname = user.getRealName();
					cardid = user.getCardId();
				}
				recharge.setRealName(realname);
				recharge.setCardId(cardid);
				recharge.setRemark("姓名："+realname+",身份证号："+cardid+",银行卡号："+recharge.getBankNo());
				recharge.setPayment("PC端线下充值");
				recharge.setChannelKey(ConstantUtil.OFFLINE_RECHARGE);
				accountRechargeService.add(recharge);
//				printWebSuccess();
				request.setAttribute(ConstantUtil.RESULT, true);
				request.setAttribute("prompt", "线下充值预约已申请成功");
				request.setAttribute("msgInfo", "请及时至银行网点/网银/手机银行进行转账");
			} catch (Exception e) {
				request.setAttribute(ConstantUtil.RESULT, false);
				request.setAttribute("prompt", "线下充值预约申请失败");
				request.setAttribute("msgInfo", "请重新尝试！");
			}
		}else{
			request.setAttribute("isLogin", ConstantUtil.NO_LOGIN_USER);
		}
		return goToURL(data);
	}

	/**
	 * 充值记录页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value="/nb/pc/recharge/log",results = { @Result(name = "log", type = "ftl", location = "/nb/pc/recharge/log.html")})
	public String log() throws Exception {
		return "log";
	}
	/**
	 * 充值纪录数据
	 * @throws IOException 
	 */
	@Action("/nb/pc/recharge/logData")
	public void logData() throws IOException{
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			AccountRechargeModel recharge = accountRechargeService.getRechargeSummary(getNBSessionUser().getUserId());
			Account account = theAccountService.getAccountByUserId(user.getUserId());
			data.put("recharge", recharge);
			data.put("account", account);
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 提现记录 ajax数据接口
	 * 
	 * @return
	 */
	@Action("/nb/pc/recharge/logList")
	public void logList() throws Exception {
		user = getNBSessionUser();
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
	 * 连连在线充值
	 * 
	 * @throws Exception
	 */
	@Action(value="/nb/pc/recharge/dollPayRecharge",results = { @Result(name = "dollPayRecharge", type = "ftl", location = "/nb/pc/recharge/dollPayRecharge.html"),
			 @Result(name = "newRecharge", type = "ftl", location = "/nb/pc/recharge/newRecharge.html")})
	public String dollPayRecharge() throws Exception {
		if(hasSessionUser()){
			user = theUserService.getByUserId(getNBSessionUser().getUserId());
			String tokenMsg = checkToken("nbRechargeToken");
			if(!StringUtil.isBlank(tokenMsg)){
				request.setAttribute(ConstantUtil.RESULT, false);
				request.setAttribute("prompt", tokenMsg);
				request.setAttribute("msgInfo", "请稍后再试！");
				return goToURL(data);
			}
			model.setUser(user);
			UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			
			String accid = "";
			String accname = "";
			String mobile = paramString("mobile");
			String cardno = paramString("cardNo").replace(" ", "");;
			double money_order = paramDouble("money");
			if(userIdentify.getRealNameStatus() == 1){
				accid = user.getCardId();
				accname = user.getRealName();
				mobile = user.getMobilePhone();
			}else{
				accid = paramString("accId");
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
				request.setAttribute(ConstantUtil.RESULT, false);
				request.setAttribute("prompt", "充值信息有误");
				request.setAttribute("msgInfo", "请重新输入！");
				return goToURL(data);
			}
			String name_goods = "连连PC充值";
			// 创建订单
	        OrderInfo order = LianlPayHelper.createOrder(0,name_goods,String.valueOf(money_order));
	        orderService.saveOrder(user,order);
			logger.info("=============连连支付订单报文1111111：" + JSON.toJSONString(order));
	        session.put(Constant.SESSION_ORDER, order.getNo_order());
	        //支付参数封装
	        LianlPay llpay = accountRechargeService.setllpay(request,user,no_agree,accname,accid,mobile,cardno);
	        logger.info("=============连连支付支付报文1111111：" + JSON.toJSONString(llpay));
	        //充值处理
	        ToPay.prepositPay(0,request,llpay, order);
	        return "dollPayRecharge";
		}else{
			request.setAttribute("isLogin", ConstantUtil.NO_LOGIN_USER);
			return goToURL(data);
		}
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
			request.setAttribute(ConstantUtil.RESULT, false);
			request.setAttribute("prompt", "该身份证已被使用");
			request.setAttribute("msgInfo", "请重新输入！");
			return goToURL(data);
		}
		if(userNum>0){
			request.setAttribute(ConstantUtil.RESULT, false);
			request.setAttribute("prompt", "该身份证已被使用");
			request.setAttribute("msgInfo", "请重新输入！");
			return goToURL(data);
		}
		UnionPayRet cardInfo = SignHelper.cardInfo(cardno);
		if(cardInfo != null) {
			if(StringUtil.isNotBlank(cardInfo.getCardType())){
				if(!cardInfo.getCardType().equals("借记卡")){
					request.setAttribute(ConstantUtil.RESULT, false);
					request.setAttribute("prompt", "暂不支持信用卡");
					request.setAttribute("msgInfo", "请重新输入！");
					return goToURL(data);
				}
			}else{
				request.setAttribute(ConstantUtil.RESULT, false);
				request.setAttribute("prompt", "银行卡信息有误");
				request.setAttribute("msgInfo", "请重新输入！");
				return goToURL(data);
			}
		}else{
			request.setAttribute(ConstantUtil.RESULT, false);
			request.setAttribute("prompt", "银行卡信息有误");
			request.setAttribute("msgInfo", "请重新输入！");
			return goToURL(data);
		}
		return "";
	}


	/**
	 * 充值结果处理
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/nb/pc/recharge/resultRecharge",results = { @Result(name = "newRecharge", type = "ftl", location = "/nb/pc/recharge/newRecharge.html")})
	public String resultRecharge() throws Exception {
		//返回信息：oid_partner=201306031000001013&sign_type=MD5&sign=45935d9811ee648e99f796c58bcb1133&dt_order=20150525181110&no_order=20150525181110&oid_paybill=2015052569482353&money_order=0.01&result_pay=SUCCESS&settle_date=20150525&info_order=%E7%94%A8%E6%88%B7%E8%B4%AD%E4%B9%B0%E5%85%85%E5%80%BC&pay_type=3&bank_code=01050000
		if(hasSessionUser()){			
			payResult();
		}else{
			request.setAttribute("isLogin", ConstantUtil.NO_LOGIN_USER);
		}
		return goToURL(data);
	}


	/**
	 * 充值支付结果及业务处理
	 */
	private void payResult() {
		user = theUserService.getByUserId(getNBSessionUser().getUserId());	
		String orderNo = (String) session.get(Constant.SESSION_ORDER);
		OrderResultsRetBean ret = LianlPayHelper.queryOrder(orderNo);
		String flag0 = ret.getRet_code();//响应结果
		String flag1 = ret.getResult_pay();//充值结果
		if(flag0.equals("0000") && flag1.equals("SUCCESS")){//充值成功
			//业务处理成功后，session 用户认证信息变更
			UserIdentify identify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			session.put(Constant.SESSION_USER_IDENTIFY, identify);
			request.setAttribute(ConstantUtil.RESULT, true);
			request.setAttribute("prompt", "恭喜您，充值成功");
			request.setAttribute("msgInfo", "");
		}else{
			request.setAttribute(ConstantUtil.RESULT, true);
			request.setAttribute("prompt", "抱歉，充值失败");
			request.setAttribute("msgInfo", "请稍后重试！");
		}
		//更新充值入口信息
		accountRechargeService.updatePayMentByTradeNo(orderNo, "PC端连连充值");
	}

}
