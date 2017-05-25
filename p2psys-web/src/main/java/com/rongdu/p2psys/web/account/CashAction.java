package com.rongdu.p2psys.web.account;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.domain.SupportBank;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.AccountCashModel;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPay;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.account.service.SupportBankService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.rule.ExtractCashChargeRuleCheck;
import com.rongdu.p2psys.core.service.DictService;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.payment.domain.ChannelBank;
import com.rongdu.p2psys.nb.payment.service.IChannelBankService;
import com.rongdu.p2psys.nb.payment.service.IChannelConfigService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.ips.model.IpsMerchentUserInfo;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserCacheModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 提现
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月14日
 */
@SuppressWarnings("rawtypes")
public class CashAction extends BaseAction implements ModelDriven<AccountCashModel> {

	private AccountCashModel model = new AccountCashModel();

	@Override
	public AccountCashModel getModel() {
		return model;
	}

	@Resource
	private AccountService accountService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private AccountCashService accountCashService;
	@Resource
	private DictService dictService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserService userService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private SupportBankService supportBankService;
	
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private IChannelConfigService channelConfigService;
	@Resource
	private IChannelBankService channelBankService;
	
	
	private User user;
	
	private Map<String, Object> data;
	
	/**
	 * 是否已经绑定银行卡
	 * 
	 * @return
	 */
	@Action(value="/member/cash/checkBank")
	public void checkBank() throws Exception {
		data = new HashMap<String, Object>();
		user = getSessionUser();
		List<AccountBank> bankList = accountBankService.list(user.getUserId());
		if(bankList == null || bankList.size() == 0){
			data.put("result", false);
		}else{
			data.put("result", true);
		}
		printJson(getStringOfJpaObj(data));
	}	
	
	/**
	 * 是否已经在新通道下绑定银行卡
	 * 
	 * @return
	 */
	@Action(value="/member/cash/nb_checkBank")
	public void nbCheckBank() throws Exception {
		data = new HashMap<String, Object>();
		user = getSessionUser();
		List<ChannelBank> bankList = channelBankService.list(user.getUserId(),ConstantUtil.channelKey.unionpay.getValue());
		if(bankList == null || bankList.size() == 0){
			data.put("result", false);
		}else{
			data.put("result", true);
		}
		printJson(getStringOfJpaObj(data));
	}	

	
	/**
	 * 我的银行卡
	 * 
	 * @return
	 */
	@Action(value="/member/cash/bank",results = { @Result(name = "newCash", type = "ftl", location = "/member/cash/bank.html"),
			@Result(name = "bank_firm", type = "ftl", location = "/member_borrow/cash/bank.html")})
	public String bank() throws Exception {
		user = getSessionUser();
		int bankNum = Global.getInt("bank_num");
		request.setAttribute("bankNum", bankNum);
		List<AccountBank> bankList = accountBankService.list(user.getUserId());
		request.setAttribute("webChannelKey", "unionpay_channel_key");
		if(bankList != null && bankList.size() > 0){
			AccountBank ab = bankList.get(0);
			request.setAttribute("webChannelKey", ab.getChannelKey());//银行通道key
		}
		request.setAttribute("bankList", bankList);
		if (user.getUserCache().getUserType() == 3 && paramInt("borrow") == 1 || user.getUserCache().getUserType() == 2) {
			return "bank_firm";
		} 
		return "bank";
	}
	
	/**
	 * 跳转我的银行卡新增页面
	 * 
	 * @return
	 */
	@Action(value="/member/cash/addBankPage",results = { @Result(name = "addBank", type = "ftl", location = "/member/cash/addBankPage.html"),
			@Result(name = "addBank_firm", type = "ftl", location = "/member_borrow/cash/addBankPage.html")})
	public String addBankPage() throws Exception {
		user = getSessionUser();
		user = userService.getUserById(user.getUserId());
		
		UserIdentify userIdentify = getSessionUserIdentify();
		session.put(Constant.SESSION_USER, userService.getUserById(user.getUserId()));
		session.put(Constant.SESSION_USER_IDENTIFY, userIdentifyService.findById(userIdentify.getId()));
		if (user.getUserCache().getUserType() ==3 && paramInt("borrow") == 1 || user.getUserCache().getUserType() == 2) {//借款者
			return "addBank_firm";
		} 
		return "addBank";
	}
	
	/**
	 * 获取绑卡验证码（银联在线专用）
	 * @return
	 * @throws Exception
	 */
	@Action(value="/member/cash/getAddBankCode")
	public String getAddBankCode() throws Exception {
		System.out.println("====================准备请求发送验证码==================\r\n");
		UnionPayRet ret = SignHelper.sendCode(paramString("mobilePhone"));
		if(ret == null) {
			printWebResult("银联暂无响应，请稍后重试", false);
		}
		if("0000".equals(ret.getRetCode())){
			printWebSuccess();
		}else{
			printWebResult(ret.getRetDesc(), false);
		}
		System.out.println("====================请求发送验证码的工作结束==================\r\n 返回码是："+ret.getRetCode());
		return null;
	}
	
	/**
	 * 跳转随机付款页面
	 * 
	 * @return
	 */
//	@Action(value="/member/cash/inputAmount",results = { @Result(name = "inputAmount", type = "ftl", location = "/member/cash/inputAmount.html")})
//	public String inputAmount() throws Exception {
//		user = getSessionUser();
//		user = userService.find(user.getUserId());
//
//		return "inputAmount";
//	}
	
	/**
	 * 添加银行卡
	 * @Result(name = "pnrBindCard", type = "ftl", location = "/tpp/chinapnr/userBindCard.html"),
	 * @return
	 * @throws IOException
	 */
	@Action(value="/member/cash/addBank",results = {@Result(name = "addBank", type = "ftl", location = "/member/cash/addBank.html"),
			@Result(name = "addBank_wx", type = "ftl", location = "/member/cash/addBank_wx.html"),
			@Result(name = "addBankPage", type = "ftl", location = "/member/cash/addBankPage.html"),
			@Result(name = "addBankPage_wx", type = "ftl", location = "/wechat/bankcard-bind.html"),
			@Result(name = "cash_sure", type = "ftl", location = "/wechat/cash_sure.html")
			})
	public String addBank() throws Exception {
		user = getSessionUser();
		UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUserId());
		int payType = paramInt("payType");//平台类型（0pc,1微信）
		request.setAttribute("payType", payType);
		String money = paramString("money");
		request.setAttribute("money", money);
		
		String accId = "";
		String accName = "";
		if(userIdentify.getRealNameStatus() == 1){
			accId = userIdentify.getUser().getCardId();
			accName = userIdentify.getUser().getRealName();
		}else{
			accId = paramString("accId");
			accName = paramString("accName");
		}
		
		String cardNo = paramString("cardNo");
		String mobile = paramString("mobile");
		String code = paramString("code");
		
		request.setAttribute("cardNo", cardNo);
		request.setAttribute("mobile", mobile);
		request.setAttribute("accName", accName);
		
		//先根据卡号查询是否已被绑定
		/*AccountBank ab = accountBankService.findByBankNo(cardNo);
		if(ab != null && ab.getStatus() == 1){
			request.setAttribute("msg", "该银行卡已绑定，请勿重复绑定！");
			return goToURL(payType);
		}*/
		
		UnionPay pay = new UnionPay(cardNo, accName, accId, mobile, code);
		//校验参数
		pay.checkAddBank();
//		List<AccountBank> bankList = accountBankService.list(user.getUserId());
		List<ChannelBank> bankList = channelBankService.list(user.getUserId(),ConstantUtil.channelKey.unionpay.getValue());
		if(bankList != null && bankList.size() > 0) {
//			request.setAttribute("msg", "已绑定银行卡，请勿重复操作！");
			return goToURL(payType, "已绑定银行卡，请勿重复操作！");
		}
		UnionPayRet ret = SignHelper.cardBindByMC(pay);
		if(ret == null) {
//			request.setAttribute("msg", "银联暂无响应，请稍后重试！");
			return goToURL(payType, "银联暂无响应，请稍后重试！");
		}
		if("0000".equals(ret.getRetCode())){
//			request.setAttribute("msg", "恭喜！绑卡成功！");
			
			String bankName = bindSuccess(userIdentify, accId, accName, cardNo, mobile, ret);
			if(StringUtil.isNull(money)!=""){
				request.setAttribute("bankName", bankName);
				return "cash_sure";
			}
			return goToURL(payType, "恭喜！绑卡成功！");
		}else if((ret.getRetDesc()).indexOf("短信")>=0){
//			request.setAttribute("msg", "验证码错误！");
			return goToURL(payType, "验证码错误！");
		}else if((ret.getRetDesc()).indexOf("身份认证")>=0){
//			request.setAttribute("msg", "身份认证失败！");
			return goToURL(payType, "身份认证失败！");
		}else {
			//失败重新调用3.5.2 随机付款
			UnionPay pay1 = new UnionPay(cardNo, accName, accId);
			UnionPayRet ret1 = SignHelper.randomPay(pay1);
			if("0000".equals(ret1.getRetCode())){
				System.out.println("=======================================发送随机认证返回结果===成功！");
				request.setAttribute("accId", accId);
				request.setAttribute("accName", accName);
				request.setAttribute("cardNo", cardNo);
				request.setAttribute("mobile", mobile);
				if(payType>0){
					return "addBank_wx";
				}
				return "addBank";
			}else if("606B".equals(ret1.getRetCode().toUpperCase())){
				System.out.println("=======================================发送随机认证返回结果==="+ret1.getRetDesc());
				
				request.setAttribute("accId", accId);
				request.setAttribute("accName", accName);
				request.setAttribute("cardNo", cardNo);
				request.setAttribute("mobile", mobile);
				
				if(payType>0){
					return "addBank_wx";
				}
				return "addBank";
			}else{
				System.out.println("=======================================发送随机认证返回结果==="+ret1.getRetDesc());
//				request.setAttribute("msg", ret1.getRetDesc());
				return goToURL(payType,ret1.getRetDesc());
			}

		}
	}
	
	/**
	 * 绑定银行卡（随机付款验证）
	 * 
	 * @return
	 */
	@Action(value="/member/cash/addRandomBank",results = { @Result(name = "addBank", type = "ftl", location = "/member/cash/addBankPage.html"),
			@Result(name = "cash_sure", type = "ftl", location = "/wechat/cash_sure.html")})
	public String addRandomBank() throws Exception {
		user = getSessionUser();
		user = userService.getUserById(user.getUserId());
		UserIdentify userIdentify = userIdentifyService.findByUserId(user.getUserId());
		String accId = paramString("accId");
		String accName = paramString("accName");
		String cardNo = paramString("cardNo");
		String mobile = paramString("mobile");
		
		String cashmoney = paramString("cashmoney");
		request.setAttribute("money", cashmoney);
		
		request.setAttribute("cardNo", cardNo);
		request.setAttribute("mobile", mobile);
		request.setAttribute("accName", accName);
		
		//String orderNo = paramString("orderNo");
		double d = paramDouble("money");
		long money = (long)(d*100);
		UnionPay pay = new UnionPay(money,cardNo, accName, accId);//, orderNo);
		UnionPayRet ret = SignHelper.paymentBind(pay);
		if(ret == null) {
			throw new AccountException("银联暂无响应，请稍后重试", 1);
		}
		if("0000".equals(ret.getRetCode())){
			System.out.println("=======================================3.1.4绑定成功==="+ret.getRetDesc());
			String bankName = bindSuccess(userIdentify, accId, accName, cardNo, mobile, ret);
			if(StringUtil.isNull(cashmoney)!=""){
				request.setAttribute("bankName", bankName);
				return "cash_sure";
			}
			printSuccess();
		}else{
			System.out.println("=======================================3.1.4绑定失败==="+ret.getRetDesc());
			throw new AccountException("随机金额绑卡验证失败！", 1);
		}
		return null;
	}

	/**
	 * 根据类型返回相应页面
	 * @param payType
	 * @return
	 */
	private String goToURL(int payType,String msg) {
		request.setAttribute("msg", msg);
		if(payType>0){
			return "addBankPage_wx";
		}
		return "addBankPage";
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
	private String bindSuccess(UserIdentify userIdentify, String accId,
			String accName, String cardNo, String mobile, UnionPayRet ret) {
		AccountBank ab;
		//先查询银行卡信息
		UnionPayRet cardInfo = SignHelper.cardInfo(cardNo);
		String bankName = cardInfo.getBankName();
		if("0000".equals(cardInfo.getRetCode())){
			ab = new AccountBank(user, cardNo, cardInfo.getBankName(), "/data/bank/mini/"+bankName+".png");
			ab.setMobile(mobile);
			ab.setBindId(ret.getBindId());
			ab.setStatus(1);
			ab.setChannelKey(channelKey.unionpay.getValue());
			accountBankService.save(ab);
			//添加至通道银行卡管理表
			channelBankService.saveChannelBank(user, ab,ConstantUtil.channelKey.unionpay.getValue());
		}
		
		//该绑卡接口带有实名校验，所以绑卡成功即自动完成实名认证
		if(userIdentify.getRealNameStatus() != 1){
			//检测身份证号是否已被使用过
			int count = userService.countByCardId(accId);
			if(count > 0){
//				throw new AccountException("身份证号已被占用", 1);
				request.setAttribute("msg", "对不起，身份证号已被占用！");
			}
			user.setRealName(accName);
			user.setCardId(accId);
			userService.updateUserInfo(user);
			
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
			
			UserIdentify identify = userIdentifyService.findByUserId(user.getUserId());
			identify.setRealNameStatus(1);
			identify.setRealNameVerifyTime(new Date());
			userIdentifyService.update(identify);
			
			//发放实名红包
			userRedPacketService.doRedPacket(RedPacket.REALNAME, user, null, null);
		}
		return bankName;
	}

	/**
	 * 解绑/禁用银行卡
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action("/member/cash/disableBank")
	public void disableBank() throws Exception {
		user = getSessionUser();
		String message = accountBankService.disable(user.getUserId(), paramLong("id"));
		if(!"success".equals(message)){
			printWebResult(message, false);
		}else{
			printWebSuccess();
		}
	}

	/**
	 * 前往提现页面
	 * 
	 * @return
	 */
	@Action(value="/member/cash/newCash",results = { @Result(name = "newCash", type = "ftl", location = "/member/cash/newCash.html"),
			@Result(name = "newCash_firm", type = "ftl", location = "/member_borrow/cash/newCash.html")})
	public String newCash() throws Exception {
		user = getSessionUser();
		saveToken("cashToken");
		int bankNum = Global.getInt("bank_num");
		request.setAttribute("bankNum", bankNum);
		int minCashMoney = Global.getInt("OFFLINE_CASH_MONEY");//线下提现最小金额
		request.setAttribute("minCashMoney", minCashMoney);
		ExtractCashChargeRuleCheck rule = (ExtractCashChargeRuleCheck) Global.getRuleCheck("extractCashCharge");
		if (rule != null && rule.getCash_status() == 1) {
			//最大提现金额
			double cashFeeMax = rule.getCash_fee_max();
			request.setAttribute("cashFeeMax", cashFeeMax);
		}
		long userId = user.getUserId();
		Account account = accountService.findByUser(userId);
		AccountBank accountBank = new AccountBank();
		String realName = user.getRealName();
		
		List<AccountBank> bankList = accountBankService.list(userId);
		
		if (isOpenApi() && TPPWay.API_CODE == TPPWay.API_CODE_IPS) {
			TPPWay ipsWay = TPPFactory.getTPPWay(null, user, null,null,null);
			if (ipsWay != null) {
				IpsMerchentUserInfo merchentUserInfo = ipsWay.queryMerUserInfo();
				//银行名不同，银行卡号不同
				if(!accountBank.getBank().equals(merchentUserInfo.getpBankName())||!accountBank.getBankNo().equals(merchentUserInfo.getpBankCard()))
				{
					//更新银行名与卡号
					accountBank.setBank(merchentUserInfo.getpBankName());
					accountBank.setBankNo(merchentUserInfo.getpBankCard());		
					accountBankService.update(accountBank);
					
				}
			}
		}
		request.setAttribute("bankList", bankList);
		request.setAttribute("account", account);
	
		String webCashUrl = ConstantUtil.WEB_PAY_CASH_URL; //channelConfigService.loadGatewayUrlByKey(ConstantUtil.gatewayKey.webCash.getValue());//获取系统所用通道提现信息
		if(webCashUrl != null){
			request.setAttribute("webCashUrl", webCashUrl);//微信端提现入口
		}
		
		request.setAttribute("realName", realName);
		if (user.getUserCache().getUserType() == 3 && paramInt("borrow") == 1 || user.getUserCache().getUserType() == 2) {
			return "newCash_firm";
		} 
		return "newCash";
	}
	
	/**
	 * 前往银联提现获取验证码页面
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/member/cash/checkylCode",results = { @Result(name = "checkylCode", type = "ftl", location = "/wechat/checkylCode.html"),
			@Result(name = "addBank_cash", type = "ftl", location = "/wechat/addBank_cash.html")})
	public String checkylCode() throws Exception {
		user = getSessionUser();
		request.setAttribute("money", paramString("money"));
		List<ChannelBank> bankList = channelBankService.list(user.getUserId(),channelKey.unionpay.getValue());
		if(bankList.size()<=0){
			bankList = channelBankService.list(user.getUserId(),channelKey.llpay.getValue());
		}
		if(bankList == null || bankList.size() == 0){
			//未绑卡
			return "addBank_cash";
		}else{
			//已绑卡
			request.setAttribute("mobile", user.getMobilePhone());
			return "checkylCode";
		}
	}
	
	/**
	 * 银联提现申请
	 * 
	 * @return
	 *//*
	@Action(value="/member/cash/doylCash",results = { @Result(name = "authSuccess", type = "ftl", location = "/wechat/authSuccess.html")})
	public String doylCash() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		UserIdentify userIdentify = getSessionUserIdentify();
//		List<AccountBank> bankList = accountBankService.list(userId);
		List<ChannelBank> bankList = channelBankService.list(userId,ConstantUtil.channelKey.unionpay.getValue());
		try{
			model.validCash(user, userIdentify, bankList);
		} catch(AccountException ae) {
//			if(ae.getMessage().contains("密码")) {
//				userCacheService.doLock(request, userId, UserCacheModel.PAY_PWD_LOCK);
//			}
			throw new AccountException(ae.getMessage(), 1);
		}
//		AccountBank bank = accountBankService.find(user.getUserId(), model.getBankNo());
		ChannelBank cb = bankList.get(0);
		AccountBank bank = accountBankService.findById(cb.getAb().getId());
		AccountCash accountCash = model.prototype(bank);
		accountCash.setTransferType(0);
		accountCashService.doCash(accountCash,user,ConstantUtil.channelKey.unionpay.getValue());
		return "authSuccess";
	}*/
	
	/**
	 * 银联提现申请
	 * 
	 * @return
	 */
	@Action("/member/cash/doAllCash")
	public void doylCash() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		checkToken("cashToken");
		UserIdentify userIdentify = getSessionUserIdentify();
		List<AccountBank> bankList = accountBankService.list(userId,channelKey.unionpay.getValue());
		String channelKeys = channelKey.unionpay.getValue();
		if(bankList.size()<=0){
			channelKeys = channelKey.llpay.getValue();
			bankList = accountBankService.list(userId,channelKey.llpay.getValue());
		}
		try{
			model.validCash_old(user, userIdentify, bankList, MD5.encode(model.getPayPwd()));
		} catch(AccountException ae) {
			if(ae.getMessage().contains("密码")) {
				userCacheService.doLock(request, user.getUserId(), UserCacheModel.PAY_PWD_LOCK);
			}
			throw new AccountException(ae.getMessage(), 1);
		}
		AccountBank bank = accountBankService.find(user.getUserId(), model.getBankNo(),channelKeys);
		AccountCash accountCash = model.prototype(bank);
		accountCash.setChannelKey(channelKey.unionpay.getValue());
		accountCashService.doCash(accountCash,user);
		printWebSuccess();
	}
	
    /**
	 * 银联提现申请
	 * 
	 * @return
	 */
	@Action("/member/cash/doylwebCash")
	public void doylwebCash() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		UserIdentify userIdentify = getSessionUserIdentify();
//		List<AccountBank> bankList = accountBankService.list(userId);
		List<ChannelBank> bankList = channelBankService.list(userId,ConstantUtil.channelKey.unionpay.getValue());
		try{
			model.validCash(user, userIdentify, bankList);
		} catch(AccountException ae) {
//			if(ae.getMessage().contains("密码")) {
//				userCacheService.doLock(request, userId, UserCacheModel.PAY_PWD_LOCK);
//			}
			throw new AccountException(ae.getMessage(), 1);
		}
//		AccountBank bank = accountBankService.find(user.getUserId(), model.getBankNo());
		ChannelBank cb = bankList.get(0);
		AccountBank bank = accountBankService.findById(cb.getAb().getId());
		AccountCash accountCash = model.prototype(bank);
		accountCash.setChannelKey(channelKey.unionpay.getValue());
		accountCashService.doCash(accountCash,user);
		printWebSuccess();
	}
	
	/**
	 * 银联提现申请
	 * 
	 * @return
	 */
	@Action("/member/cash/doylOfflineCash")
	public void doylOfflineCash() throws Exception {
		user = getSessionUser();
		checkToken("cashToken");
		double money = this.paramDouble("money");
		String realName = this.paramString("realName");
		String bankNo = this.paramString("bankNo");
		String bankName = "";
		if(StringUtil.isNotBlank(bankNo)) {
			UnionPayRet cardInfo = SignHelper.cardInfo(bankNo);
			if(cardInfo != null) {
				if(StringUtil.isNotBlank(cardInfo.getBankName())){
					bankName = cardInfo.getBankName();
				} else {
					bankName = "";
				}
			}
		}
		AccountCash accountCash = new AccountCash(user, money);
		accountCash.setBankNo(bankNo);
		accountCash.setBank(bankName);
		accountCash.setTransferType(1);
		accountCash.setBranch("");
		accountCash.setChannelKey(channelKey.unionpay.getValue());
		accountCashService.doCash(accountCash,user);
		printWebSuccess();
	}

	/**
	 * 托管提现  较为特殊需要跳转页面
	 * @throws Exception if has error
	 */
	@Action(value="/member/cash/doCashSkip",results={@Result(name = "ipsCash", type = "ftl", location = "/tpp/ipscash.html"),
					@Result(name = "pnrCash", type = "ftl", location = "/tpp/chinapnr/cash.html")})
	public String doCashSkip() throws Exception {
		data = new HashMap<String, Object>();
		user = getSessionUser();
		long userId = user.getUserId();
        String name = Global.getValue("cooperation_interface");
        UserIdentify userAttestation = getSessionUserIdentify();
//		List<AccountBank> bankList = accountBankService.list(userId);
		List<ChannelBank> bankList = channelBankService.list(userId,ConstantUtil.channelKey.unionpay.getValue());
        model.validCash(user, userAttestation, bankList);
        ChannelBank cb = bankList.get(0);
		AccountBank bank = accountBankService.findById(cb.getAb().getId());
//		AccountBank bank = accountBankService.find(user.getUserId(), model.getBankNo());
        TPPWay way = TPPFactory.getTPPWay(null, user, null, model.getMoney()+"",null);
        AccountCash accountCash = model.prototype(bank);
        accountCash.setChannelKey(channelKey.unionpay.getValue());
        accountCash = accountCashService.doCash(accountCash,user);
        //获取用户已经成功提现次数
        int countMonth = accountCashService.countMonth(user.getUserId());
        Object cash = way.doNewCash(accountCash, user, countMonth, "", "", "");         
        request.setAttribute(name, cash);
        return name + "Cash";
	}
	
	/**
	 * 取消提现
	 * 
	 * @return
	 */
	@Action("/member/cash/cancel")
	public void cancel() throws Exception {
		user = getSessionUser();
//		accountCashService.doCancleCash(user.getUserId(), paramLong("id"));
		redirect("/member/cash/log.html");
	}

	/**
	 * 提现记录
	 * 
	 * @return
	 */
	@Action(value="/member/cash/log",results = { @Result(name = "log", type = "ftl", location = "/member/cash/log.html"),
			@Result(name = "log_firm", type = "ftl", location = "/member_borrow/cash/log.html")})
	public String log() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		Account account = accountService.findByUser(userId);
		request.setAttribute("account", account);
		if (user.getUserCache().getUserType() == 3 && paramInt("borrow") == 1 || user.getUserCache().getUserType() == 2) {
			return "log_firm";
		} 
		return "log";
	}

	/**
	 * 提现记录 ajax数据接口
	 * 
	 * @return
	 */
	@Action("/member/cash/logList")
	public void logList() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		PageDataList<AccountCashModel> pageDateList = accountCashService.list(userId, model.getPage(),model);
		model = accountCashService.getCashMessage(userId);
		data = new HashMap<String, Object>();
		data.put("data", pageDateList);
		data.put("model", model);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 检测银行卡是否已被绑定
	 * 
	 * @throws Exception
	 */
	@Action("/member/cash/checkBankIsExist")
	public void checkBankIsExist() throws Exception {
		data = new HashMap<String, Object>();
		String bankNo = paramString("cardNo");
		AccountBank bank = accountBankService.findByBankNo(bankNo);
		if(bank != null && bank.getStatus() == 1) {
			printWebJson(getStringOfJpaObj(true));
		}else {
			printWebJson(getStringOfJpaObj(false));
		}
	}

	/**
	 * 检测该银行卡是否可以使用
	 * @throws Exception
	 */
	@Action("/member/cash/checkBankCanUse")
	public void checkBankCanUse() throws Exception {
		data = new HashMap<String, Object>();
		String cardNo = paramString("cardNo");
		boolean result = false;
		if(StringUtil.isNotBlank(cardNo)) {
			UnionPayRet cardInfo = SignHelper.cardInfo(cardNo);
			if(cardInfo != null) {
				if(StringUtil.isNotBlank(cardInfo.getBankName())){
					SupportBank supportBank = supportBankService.findByName(cardInfo.getBankName());
					if(supportBank != null) {
						data.put("supportBank", supportBank);
						result = true;
					} else {
						data.put("message", "不支持该银行");
					}
				} else {
					data.put("message", "未知银行卡");
				}
			}
		}
		data.put("result", result);
		printWebJson(getStringOfJpaObj(data));
	}
	

	/**
	 * 我已经绑定银行卡
	 * 
	 * @return
	 */
	@Action(value="/member/cash/myBank")
	public void myBank() throws Exception {
		user = getSessionUser();
		String bankJosn = LianlPayHelper.queryBankcardList(user.getUserId());
		printJson(bankJosn);
	}	
	/**
	 * bin银行卡
	 * 
	 * @return
	 */
	@Action(value="/member/cash/binBank")
	public void binBank() throws Exception {
		String cardNo = paramString("card_no");
		String bankJosn = LianlPayHelper.queryCardBin(cardNo);
		printJson(bankJosn);
	}
	
	/**
	 * 银联扣款申请
	 * 
	 * @return
	 */
	@Action(value="/member/cash/singlePay",results = { @Result(name = "authSuccess", type = "ftl", location = "/wechat/authSuccess.html")})
	public String singlePay() throws Exception {		
		String bindId = "a1e6de47f0070e4f08d24f08b06bce1b";
		//2409.93
		UnionPay pay = new UnionPay();
		pay.setAmount((long)240993);
		pay.setBindId(bindId);
		UnionPayRet ret = SignHelper.singlePay(pay);
		if("0000".equals(ret.getRetCode())){//调用成功
			System.out.println("=================扣款成功！");
		}
		return "authSuccess";
	}
	
	/**
	 * 银联绑卡信息查询
	 * 
	 * @return
	 */
	@Action(value="/member/cash/queryBind",results = { @Result(name = "authSuccess", type = "ftl", location = "/wechat/authSuccess.html")})
	public String queryBind() throws Exception {		
		String bindId = "49b7f59b28279546472fd6a282651e99";
		UnionPayRet ret = SignHelper.queryBind(bindId);
		if("0000".equals(ret.getRetCode())){//调用成功
			System.out.println("=================有效！");
		}
		return "authSuccess";
	}
	
	
	/**
	 * 测试银联提现申请
	 * 
	 * @return
	 */
	@Action(value="/member/cash/ylCash",results = { @Result(name = "authSuccess", type = "ftl", location = "/wechat/authSuccess.html")})
	public String ylCash() throws Exception {	
		String bankNo = "622909413924114216";
		String relName = "叶生红";
		String cardId = "421125198307137975";
		String mobile = "18616548116";
		double money = 1;
		String orderNo = OrderNoUtils.getSerialNumber();
		
		UnionPay pay = new UnionPay(bankNo, relName, cardId, mobile, 
				(long)(BigDecimalUtil.mul(BigDecimalUtil.sub(money, 0),100)), 2, "cash" + money);
		pay.setOrderNo(orderNo);
		UnionPayRet ret = SignHelper.paymentNoBind(pay);
		if("0000".equals(ret.getRetCode())){//调用成功
			if(ret.getOrderStatus() == 2){//处理成功
				System.out.println("=================提现成功！");
			}else if(ret.getOrderStatus() == 3){//处理失败
				System.out.println("=================银联处理失败:" + ret.getRetDesc());
			}else if(ret.getOrderStatus() == 1){//银联处理中
				System.out.println("=================提现银联处理中！");
			}
		}
		return "authSuccess";
	}
}
