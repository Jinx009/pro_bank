package com.rongdu.p2psys.wechat;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.domain.SupportBank;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.account.model.AccountCashModel;
import com.rongdu.p2psys.account.model.accountlog.BaseAccountLog;
import com.rongdu.p2psys.account.model.accountlog.noac.GetCodeLog;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.utils.LLPayUtil;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPay;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.SupportBankService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.rule.ExtractCashChargeRuleCheck;
import com.rongdu.p2psys.core.service.DictService;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.payment.domain.ChannelBank;
import com.rongdu.p2psys.nb.payment.domain.ChannelConfig;
import com.rongdu.p2psys.nb.payment.service.IChannelBankService;
import com.rongdu.p2psys.nb.payment.service.IChannelConfigService;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.nb.util.GetCityCode;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.ips.model.IpsMerchentUserInfo;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserRedPacketService;

/**
 * 提现
 * 
 * @author cgw
 * @version 2.0
 * @since 2014年3月14日
 */
@SuppressWarnings("rawtypes")
public class WechatCashAction extends BaseAction implements ModelDriven<AccountCashModel> {
	private static Logger logger = Logger.getLogger(WechatCashAction.class);
	private AccountCashModel model = new AccountCashModel();

	@Override
	public AccountCashModel getModel() {
		return model;
	}

	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private AccountCashService accountCashService;
	@Resource
	private DictService dictService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private SupportBankService supportBankService;
	@Resource
	private IChannelConfigService channelConfigService;
	@Resource
	private IChannelBankService channelBankService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private UserService theUserService;
	@Resource
	private IOrderService orderService;
	@Resource
	private SystemConfigService systemConfigService;
	
	private User user;
	
	private Map<String, Object> data;
	
	/**
	 * 是否已经绑定银行卡
	 * 
	 * @return
	 */
	@Action(value="/nb/wechat/cash/checkBank")
	public void checkBank() throws Exception {
		data = new HashMap<String, Object>();
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
//		List<ChannelBank> bankList = channelBankService.list(user.getUserId(),ConstantUtil.channelKey.unionpay.getValue());
		List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
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
	@Action(value="/nb/wechat/cash/bank",results = { @Result(name = "bank", type = "ftl", location = "/nb/wechat/cash/bank.html"),
			@Result(name = "bank_firm", type = "ftl", location = "/member_borrow/cash/bank.html")})
	public String bank() throws Exception {
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		int bankNum = Global.getInt("bank_num");
		request.setAttribute("bankNum", bankNum);
		List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
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
	@Action(value="/nb/wechat/cash/addBankPage",results = { @Result(name = "addBank", type = "ftl", location = "/nb/wechat/cash/addBankPage.html"),
			@Result(name = "addBank_firm", type = "ftl", location = "/member_borrow/cash/addBankPage.html")})
	public String addBankPage() throws Exception {
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		user = theUserService.getByUserId(user.getUserId());
		saveToken("wapAddBankToken");
		UserIdentify userIdentify = getSessionUserIdentify();
		if(userIdentify!=null){			
			session.put(Constant.SESSION_USER, theUserService.getByUserId(user.getUserId()));
			session.put(Constant.SESSION_USER_IDENTIFY, theUserIdentifyService.getUserIdentifyByUserId(userIdentify.getId()));
		}
		if (user.getUserCache().getUserType() ==3 && paramInt("borrow") == 1 || user.getUserCache().getUserType() == 2) {//借款者
			return "addBank_firm";
		} 
		return "addBank";
	}
	
	/**
	 * 添加银行卡
	 * @Result(name = "pnrBindCard", type = "ftl", location = "/tpp/chinapnr/userBindCard.html"),
	 * @return
	 * @throws IOException
	 */
	@Action(value="/nb/wechat/cash/addBank",results = {@Result(name = "addBank_wx", type = "ftl", location = "/nb/wechat/cash/addBank_wx.html"),
			@Result(name = "authSuccess", type = "ftl", location = "/nb/wechat/cash/authSuccess.html"),
			@Result(name = "authError", type = "ftl", location = "/nb/wechat/cash/authError.html"),
			@Result(name = "addBankPage_wx", type = "ftl", location = "/nb/wechat/cash/addBankPage.html"),
			@Result(name = "cash_sure", type = "ftl", location = "/nb/wechat/cash/cash_sure.html"),
			@Result(name = "recharge_sure", type = "ftl", location = "/nb/wechat/recharge/recharge_sure.html"),
			@Result(name = "addBank_cash", type = "ftl", location = "/nb/wechat/cash/addBank_cash.html")
			})
	public String addBank() throws Exception {
		String money = paramString("money");
		request.setAttribute("money", money);
		String operType = paramString("operType");
		request.setAttribute("operType", operType);
		String msg = checkWAPToken("wapAddBankToken");
		if(msg.equals("-1")){	
			user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
			UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			
			String accId = "";
			String accName = "";
			if(null!=userIdentify && userIdentify.getRealNameStatus() == 1){
				accId = userIdentify.getUser().getCardId();
				accName = userIdentify.getUser().getRealName();
			}else{
				accId = paramString("accId");
				accName = paramString("accName");
			}
			
			String cardNo = paramString("cardNo").replace(" ", "");
			String mobile = paramString("mobile");
			String code = paramString("code");
			
			request.setAttribute("cardNo", cardNo);
			request.setAttribute("mobile", mobile);
			request.setAttribute("accName", accName);
			
			//先根据卡号查询是否已被绑定
			/*AccountBank ab = theAccountBankService.findByBankNo(cardNo);
			if(ab != null && ab.getStatus() == 1){
				request.setAttribute("msg", "该银行卡已绑定，请勿重复绑定！");
				return goToURL(payType);
			}*/
			String gourl = "addBankPage_wx";
			if(StringUtil.isNull(money)!=""){
				gourl = "addBank_cash";
			}
			String cab = checkAddBank(user.getUserId(),accId, cardNo,channelKey.unionpay.getValue(),gourl);
			if(!StringUtil.isBlank(cab)){
				saveToken("wapAddBankToken");
				return cab;
			}
			
			UnionPay pay = new UnionPay(cardNo, accName, accId, mobile, code);
			//校验参数
			String check = pay.checkWapAddBank();
			if(check!=null){
				request.setAttribute("msg", check);
				return addBankURL(money);
			}
//			List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
			List<ChannelBank> bankList = channelBankService.list(user.getUserId(),ConstantUtil.channelKey.unionpay.getValue());
			if(bankList != null && bankList.size() > 0) {
				request.setAttribute("msg", "已绑定银行卡，请勿重复操作！");
				return addBankURL(money);
			}
			UnionPayRet ret = SignHelper.cardBindByMC(pay);
			if(ret == null) {
				request.setAttribute("msg", "银联暂无响应，请稍后重试！");
				return addBankURL(money);
			}else{
				if("0000".equals(ret.getRetCode())){
					request.setAttribute("msg", "恭喜！绑卡成功！");
					
					String bankName = bindSuccess(userIdentify, accId, accName, cardNo, mobile, ret);
					if(StringUtil.isNull(money)!=""){
						request.setAttribute("bankName", bankName);
						if(StringUtil.isNull(operType)!="" && operType.equals("cash")){					
							return "cash_sure";
						}else if(StringUtil.isNull(operType)!="" && operType.equals("recharge")){
							return "recharge_sure";
						}
					}
					request.setAttribute("msg", "绑卡成功！");
					request.setAttribute("gotoUrl", "/nb/wechat/cash/bank.html");
					return "authSuccess";
				}else if((ret.getRetDesc()).indexOf("短信")>=0){
					request.setAttribute("msg", "验证码错误！");
					return addBankURL(money);
				}else if((ret.getRetDesc()).indexOf("身份认证")>=0){
					request.setAttribute("msg", "身份认证失败！");
					return addBankURL(money);
				}else{
					//失败重新调用3.5.2 随机付款
					UnionPay pay1 = new UnionPay(cardNo, accName, accId);
					UnionPayRet ret1 = SignHelper.randomPay(pay1);
					saveToken("wapRandomToken");
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
						return addBankURL(money);
					}
				}
			}
		}else{
			if(msg.equals("0")){	
				saveToken("wapAddBankToken");
				request.setAttribute("msg", "信息有误，请重试！");
			}else{
				request.setAttribute("msg", "请勿重复提交！");
			}
			request.setAttribute("gotoUrl", "/nb/wechat/cash/addBankPage.html");
			if(StringUtil.isNull(money)!=""){
				return "addBank_cash";
			}else{					
				return "authError";
			}
		}
	}


	/***
	 * 绑卡业务失败提示返回
	 * @param money
	 * @return
	 */
	private String addBankURL(String money) {
		saveToken("wapAddBankToken");
		if(StringUtil.isNull(money)!=""){
			return "addBank_cash";
		}else{					
			return "addBankPage_wx";
		}
	}

	/**
	 * 绑定银行卡（随机付款验证）
	 * 
	 * @return
	 */
	@Action(value="/nb/wechat/cash/addRandomBank",results = { @Result(name = "addBank", type = "ftl", location = "/nb/wechat/cash/addBank_cash.html"),
			@Result(name = "authSuccess", type = "ftl", location = "/nb/wechat/cash/authSuccess.html"),
			@Result(name = "authError", type = "ftl", location = "/nb/wechat/cash/authError.html"),
			@Result(name = "addBankPage_wx", type = "ftl", location = "/nb/wechat/cash/addBankPage.html"),
			@Result(name = "cash_sure", type = "ftl", location = "/nb/wechat/cash/cash_sure.html"),
			@Result(name = "recharge_sure", type = "ftl", location = "/nb/wechat/recharge/recharge_sure.html")})
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
			
			//String orderNo = paramString("orderNo");
			double d = paramDouble("money");
			long money = (long)(d*100);
			UnionPay pay = new UnionPay(money,cardNo, accName, accId);//, orderNo);
			UnionPayRet ret = SignHelper.paymentBind(pay);
			if(ret == null) {
				request.setAttribute("msg", "银联暂无响应！");
				request.setAttribute("gotoUrl", "/nb/wechat/cash/addBankPage_wx.html");
				return "authError";
			}else{
				if("0000".equals(ret.getRetCode())){
					logger.info("=======================================3.1.4绑定成功==="+ret.getRetDesc());
					String bankName = bindSuccess(userIdentify, accId, accName, cardNo, mobile, ret);
					if(StringUtil.isNull(cashmoney)!=""){
						request.setAttribute("bankName", bankName);
						String operType = paramString("operType");
						request.setAttribute("operType", operType);
						if(StringUtil.isNull(operType)!="" && operType=="cash"){					
							return "cash_sure";
						}else if(StringUtil.isNull(operType)!="" && operType=="recharge"){
							return "recharge_sure";
						}
					}
					request.setAttribute("msg", "绑卡成功！");
					request.setAttribute("gotoUrl", "/nb/wechat/cash/bank.html");
					return "authSuccess";
				}else{
					logger.info("=======================================3.1.4绑定失败==="+ret.getRetDesc());
					request.setAttribute("msg", "随机金额绑卡验证失败！");
					if(StringUtil.isNull(cashmoney)!=""){
						return "addBank";
					}
					return "addBankPage_wx";
				}
			}
		}else{
			if(msg.equals("0")){
				saveToken("wapRandomToken");
				request.setAttribute("msg", "信息有误，请重试！");
			}else{
				request.setAttribute("msg", "请勿重复提交！");
			}
			request.setAttribute("gotoUrl", "/nb/wechat/cash/addBankPage.html");
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
			theUserIdentifyService.updateUserIdentify(identify);
			//业务处理成功后，session 用户认证信息变更
			session.put(Constant.SESSION_USER_IDENTIFY, identify);
			//发放实名红包
			userRedPacketService.doRedPacket(RedPacket.REALNAME, user, null, null);
			//判断该用户是否有手机号，没有则添加
			if(StringUtil.isBlank(user.getMobilePhone())){
//				user.setUserName(mobile);
				user.setMobilePhone(mobile);
				theUserService.updateUser(user);
			}
		}
		return bankName;
	}


	/**
	 * 前往提现页面
	 * 
	 * @return
	 */
	@Action(value="/nb/wechat/cash/newCash",results = { @Result(name = "newCash", type = "ftl", location = "/nb/wechat/cash/mention.html")})
	public String newCash() throws Exception {
		try{
			user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
			int bankNum = Global.getInt("bank_num");
			request.setAttribute("bankNum", bankNum);
			SystemConfig sc = systemConfigService.findByNid(Constant.OFFLINE_CASH_MONEY);
			int minCashMoney = Integer.parseInt(sc.getValue());//线下提现最小金额
			request.setAttribute("minCashMoney", minCashMoney);
			ExtractCashChargeRuleCheck rule = (ExtractCashChargeRuleCheck) Global.getRuleCheck("extractCashCharge");
			if (rule != null && rule.getCash_status() == 1) {
				//最大提现金额
				double cashFeeMax = rule.getCash_fee_max();
				request.setAttribute("cashFeeMax", cashFeeMax);
			}
			long userId = user.getUserId();
	//		Account account = accountService.findByUser(userId);//获取用户的账户信息
			
			List<User> userList = theUserService.getByGroupId(user.getBindId());//获取该用户所有子账号
			double useMoney=0;
			if(userList.size() > 0){
				for (User user_:userList){
					Account account_ = theAccountService.getAccountByUserId(user_.getUserId());
					useMoney += account_.getUseMoney();
				}
			}else{
				Account account = theAccountService.getAccountByUserId(user.getUserId());
				request.setAttribute("account", account);
				useMoney = account.getTotal();
			}
			request.setAttribute("userMoney", useMoney);
			
			AccountBank accountBank = new AccountBank();
			
			List<AccountBank> bankList = theAccountBankService.list(userId);
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
						theAccountBankService.update(accountBank);
						
					}
				}
			}
			request.setAttribute("bankList", bankList);
			
			String wapCashUrl = ConstantUtil.WAP_PAY_CASH_URL; //channelConfigService.loadGatewayUrlByKey(ConstantUtil.gatewayKey.wapCash.getValue());//获取系统所用通道提现信息
			//用户绑定了连连优先采用连连平台提现
			
			List<ChannelBank> bankllList = null;
			if(userList!=null && userList.size()>0){
				for (int i = 0; i < userList.size(); i++) {
					User user2 = userList.get(i);
					bankllList = channelBankService.list(user2.getUserId(),ConstantUtil.channelKey.llpay.getValue());
					if(bankllList!=null && bankllList.size()>0){
						break;
					}
				}
			}
			request.setAttribute("wapCashUrl", wapCashUrl);//银联提现入口
		}catch(Exception e){
			e.printStackTrace();
		}
		return "newCash";
	}
	
	/**
	 * 前往提现获取验证码页面
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/nb/wechat/cash/doAllCode",results = { @Result(name = "checkAllCode", type = "ftl", location = "/nb/wechat/cash/checkAllCode.html"),
			@Result(name = "line_cash", type = "ftl", location = "/nb/wechat/cash/line_cash.html"),
			@Result(name = "addBank_cash", type = "ftl", location = "/nb/wechat/cash/addBank_cash.html")})
	public String checkAllCode() throws Exception {
		int num = paramInt("cashType");
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		request.setAttribute("money", paramString("money"));
		saveToken("wapCashToken");
		request.setAttribute("cityCodeList", getStringOfJpaObj(GetCityCode.cityList));
		if(num==1){//线上提现
			List<User> userList = theUserService.getByGroupId(user.getBindId());//获取该用户所有子账号
			if(userList!=null && userList.size()>0){
				List<AccountBank> bankList = null;//已绑卡信息
				for (int i = 0; i < userList.size(); i++) {
					User user2 = userList.get(i);
					bankList = theAccountBankService.list(user2.getUserId());
					if(bankList!=null && bankList.size()>0){
						break;
					}
				}
				if(bankList == null || bankList.size() == 0){
					saveToken("wapAddBankToken");
					//未绑卡
					return "addBank_cash";
				}else{
					//已绑卡
					request.setAttribute("mobile", user.getMobilePhone());
					AccountBank bank = bankList.get(0);
					request.setAttribute("bindBank", bank);
					String bankCode = bank.getBankCode();
				    if(StringUtil.isNullOrBlank(bankCode)){
				    	bankCode = CommonRealize.bank_code_map.get(bank.getBank());
				    }
					ChannelConfig cc = channelConfigService.getChannelConfigByCode(bankCode);
					request.setAttribute("wapCashKey", cc.getWapCashKey());
					return "checkAllCode";
				}
			}
			return "addBank_cash";
		}else{//线下提现
			String realName = user.getRealName();
			request.setAttribute("realName", realName);
			request.setAttribute("cardId", user.getCardId());
			return "line_cash";
		}
		
	}
	
	/**
	 * 提现申请
	 * 
	 * @return
	 */
	@Action(value="/nb/wechat/cash/doAllCash",results = { @Result(name = "authSuccess", type = "ftl", location = "/nb/wechat/cash/authSuccess.html"),
			@Result(name = "checkAllCode", type = "ftl", location = "/nb/wechat/cash/checkAllCode.html"),
			@Result(name = "authError", type = "ftl", location = "/nb/wechat/cash/authError.html")})
	public String doAllCash() throws Exception {
		String msg = checkWAPToken("wapCashToken");
		logger.info("===============================银联提现msg：=="+msg);
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		long userId = user.getBindId();
		if(msg.equals("-1")){			
			UserIdentify userIdentify = getSessionUserIdentify();
			if(null==userIdentify){
				userIdentify = theUserIdentifyService.getUserIdentifyByUserId(userId);
				request.getSession().setAttribute(Constant.SESSION_USER_IDENTIFY, userIdentify);
			}
			request.setAttribute("cityCodeList", getStringOfJpaObj(GetCityCode.cityList));
			String branch = paramString("branch");
			String province = paramString("province");
			String city = paramString("city");
//			List<AccountBank> bankList = theAccountBankService.list(userId);
			List<User> userList = theUserService.getByGroupId(user.getBindId());//获取该用户所有子账号
//			List<ChannelBank> bankList = null; 
			List<AccountBank> bankList = null;
			if(userList!=null && userList.size()>0){
				for (int i = 0; i < userList.size(); i++) {
					User user2 = userList.get(i);
					bankList = theAccountBankService.list(user2.getUserId());
//					bankList = channelBankService.list(user2.getUserId(),ConstantUtil.channelKey.unionpay.getValue());
					if(bankList!=null && bankList.size()>0){
						break;
					}
				}
			}
//			ChannelBank cb = bankList.get(0);
			AccountBank bank = bankList.get(0);//theAccountBankService.findById(cb.getAb().getId());
			String bankCode = bank.getBankCode();
			String channelKeys = channelKey.unionpay.getValue();
		    if(StringUtil.isNullOrBlank(bankCode)){
		    	bankCode = CommonRealize.bank_code_map.get(bank.getBank());
		    }
			ChannelConfig cc = channelConfigService.getChannelConfigByCode(bankCode);
			channelKeys =  cc.getWebCashKey();
			request.setAttribute("wapCashKey", cc.getWapCashKey());
			request.setAttribute("bindBank", bank);
			if(cc.getWapCashKey().equals(channelKey.llpay.getValue())){
				if(StringUtil.isNullOrBlank(bank.getBranch())){			
					if(StringUtil.isNullOrBlank(branch) || StringUtil.isNullOrBlank(province) || StringUtil.isNullOrBlank(city)){
						request.setAttribute("msg", "支行信息有误！");
						saveToken("wapCashToken");
						return "checkAllCode";
					}else{				
						bank.setProvince(province);
						bank.setCity(city);
						bank.setBranch(branch);
						theAccountBankService.saveAccountBank(bank);//保存支行信息
					}
				}
				//判断该用户是否绑定了连连通道，是则走连连，否则走银联
		    	List<AccountBank> llbankList = theAccountBankService.list(user.getUserId(),channelKey.llpay.getValue());
		    	if(llbankList==null || llbankList.size()==0){
		    		logger.info("===================微信端===该用户未绑定连连通道，自动切换为银联通道提现===银行卡："+bank.getBankNo()+",姓名："+user.getRealName());
		    		channelKeys = channelKey.unionpay.getValue();
		    	}
			}
			request.setAttribute("bindBank", bank);
			String yzm = paramString("code");
			if(!yzm.equals("cashsure")){				
				Map<String, Object> map = ((Map<String, Object>) request.getSession().getAttribute("cashGetCode_code"));
				if (map == null || !yzm.equals(map.get("code").toString())){
					request.setAttribute("msg", "验证码错误！");
					saveToken("wapCashToken");
					return "checkAllCode";
				}
			}
			request.setAttribute("gotoUrl", "/nb/wechat/cash/newCash.html");
			try{
				model.validCash_yl(user, userIdentify, bankList);
			} catch(AccountException ae) {
				request.setAttribute("msg", "提现申请失败！");
				return "authError";
			}
//			AccountBank bank = theAccountBankService.find(user.getUserId(), model.getBankNo());
			int cashCount = accountCashService.getTodayCashCountByUserId(user.getUserId());
			if(cashCount<=ConstantUtil.CASH_COUNT){
				AccountCash accountCash = model.prototype(bank);
				accountCash.setChannelKey(channelKeys);
				accountCash.setTransferType(0);
				accountCash.setType(0);
				AccountCash ac = accountCashService.doCash(accountCash,user);
				if(ac!=null){			
					request.setAttribute("msg", "提现申请成功！");
					request.setAttribute("gotoUrl", "/nb/wechat/account/main.html");
					return "authSuccess";
				}else{
					request.setAttribute("msg", "提现申请失败！");
					return "authError";
				}
			}else{
				request.setAttribute("msg", "您今日提现次数已满！");
				return "authError";
			}
		}else{
			if(msg.equals("0")){	
				saveToken("wapCashToken");
				request.setAttribute("msg", "信息有误，请重试！");
			}else{
				request.setAttribute("msg", "请勿重复提交！");
			}
			request.setAttribute("mobile", user.getMobilePhone());
			List<AccountBank> bankList = null;//银联已绑卡信息
			List<User> userList = theUserService.getByGroupId(user.getBindId());//获取该用户所有子账号
			for (int i = 0; i < userList.size(); i++) {
				User user2 = userList.get(i);
				bankList = theAccountBankService.list(user2.getUserId());
				if(bankList!=null && bankList.size()>0){
					break;
				}
			}
			AccountBank bank = bankList.get(0);
			String bankCode = bank.getBankCode();
		    if(StringUtil.isNullOrBlank(bankCode)){
		    	bankCode = CommonRealize.bank_code_map.get(bank.getBank());
		    }
			ChannelConfig cc = channelConfigService.getChannelConfigByCode(bankCode);
			request.setAttribute("wapCashKey", cc.getWapCashKey());
			request.setAttribute("bindBank", bank);
			request.setAttribute("cityCodeList", getStringOfJpaObj(GetCityCode.cityList));
			return "checkAllCode";
		}
	}
	
	
	/**
	 * 前往连连提现获取验证码页面
	 * @return String
	 * @throws Exception if has error
	 */
	@Action(value="/nb/wechat/cash/checkllCode",results = { @Result(name = "checkllCode", type = "ftl", location = "/nb/wechat/cash/checkllCode.html"),
			@Result(name = "line_cash", type = "ftl", location = "/nb/wechat/cash/line_cash.html")})
	public String checkllCode() throws Exception {
		int num = paramInt("cashType");
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		request.setAttribute("money", paramString("money"));
		if(num==1){//线上提现
			List<User> userList = theUserService.getByGroupId(user.getBindId());//获取该用户所有子账号
			if(userList!=null && userList.size()>0){
				List<ChannelBank> bankList = null;
				for (int i = 0; i < userList.size(); i++) {
					User user2 = userList.get(i);
					bankList = channelBankService.list(user2.getUserId(),ConstantUtil.channelKey.llpay.getValue());
					if(bankList!=null && bankList.size()>0){
						break;
					}
				}
				request.setAttribute("mobile", user.getMobilePhone());
				if(bankList!=null && bankList.size()>0){
					ChannelBank cb = bankList.get(0);
					AccountBank bank = theAccountBankService.findById(cb.getAb().getId());
					request.setAttribute("bindBank", bank);					
				}
			}
			return "checkllCode";
		}else{//线下提现
			String realName = user.getRealName();
			request.setAttribute("realName", realName);
			return "line_cash";
		}
		
	}
	
	/**
	 * 连连提现申请
	 * 
	 * @return
	 */
	@Action(value="/nb/wechat/cash/dollCash",results = { @Result(name = "authSuccess", type = "ftl", location = "/nb/wechat/cash/authSuccess.html"),
			@Result(name = "checkllCode", type = "ftl", location = "/nb/wechat/cash/checkllCode.html"),
			@Result(name = "authError", type = "ftl", location = "/nb/wechat/cash/authError.html")})
	public String dollCash() throws Exception {
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		long userId = user.getUserId();
		UserIdentify userIdentify = getSessionUserIdentify();
		if(null==userIdentify){
			userIdentify = theUserIdentifyService.getUserIdentifyByUserId(userId);
			request.getSession().setAttribute(Constant.SESSION_USER_IDENTIFY, userIdentify);
		}
		List<User> userList = theUserService.getByGroupId(user.getBindId());//获取该用户所有子账号
		List<ChannelBank> bankList = null;
		if(userList!=null && userList.size()>0){
			for (int i = 0; i < userList.size(); i++) {
				User user2 = userList.get(i);
				bankList = channelBankService.list(user2.getUserId(),ConstantUtil.channelKey.llpay.getValue());
				if(bankList!=null && bankList.size()>0){
					break;
				}
			}
		}
		ChannelBank cb = bankList.get(0);
		AccountBank bank = theAccountBankService.findById(cb.getAb().getId());
		request.setAttribute("bindBank", bank);
		String yzm = paramString("code");
		Map<String, Object> map = ((Map<String, Object>) request.getSession().getAttribute("cashGetCode_code"));
		if (map == null || !yzm.equals(map.get("code").toString())){
			request.setAttribute("msg", "验证码错误！");
			return "checkllCode";
		}
		request.setAttribute("gotoUrl", "/nb/wechat/cash/newCash.html");
		try{
			model.validCash(user, userIdentify, bankList);
		} catch(AccountException ae) {
			request.setAttribute("msg", "提现申请失败！");
			return "authError";
		}

		String money = paramString("money");
		// 创建订单
        OrderInfo order = LianlPayHelper.createOrder(1,"微信连连提现",money);//连连提现
        orderService.saveOrder(user,order);
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
		cash.setChannelKey(channelKey.unionpay.getValue());
		cash.setOrderNo(order.getNo_order());
		AccountCash ac = accountCashService.doCash(cash,user);
		if(ac!=null){			
			request.setAttribute("msg", "提现申请成功！");
			request.setAttribute("gotoUrl", "/nb/wechat/account/main.html");
			return "authSuccess";
		}else{
			request.setAttribute("msg", "提现申请失败！");
			return "authError";
		}
	}
	
	/**
	 * 线下提现申请
	 * 
	 * @return
	 */
	@Action(value="/nb/wechat/cash/doylOfflineCash",results = { @Result(name = "authSuccess", type = "ftl", location = "/nb/wechat/cash/authSuccess.html"),
			@Result(name = "authError", type = "ftl", location = "/nb/wechat/cash/authError.html")})
	public String doylOfflineCash() throws Exception {
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		double money = this.paramDouble("money");
		String realName = this.paramString("realName");
		String cardId = this.paramString("cardId");
		String bankNo = this.paramString("bankNo").replace(" ", "");
		String branch = paramString("branch");
		String province = paramString("province");
		String city = paramString("city");
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
		accountCash.setRealName(realName);
		accountCash.setCardId(cardId);
		accountCash.setType(1);//线下提现
		accountCash.setBranch(branch);
		accountCash.setProvince(province);
		accountCash.setCity(city);
		accountCash.setChannelKey(ConstantUtil.OFFLINE_CASH);
		AccountCash ac = accountCashService.doCash(accountCash,user);
		request.setAttribute("gotoUrl", "/nb/wechat/cash/newCash.html");
		if(ac!=null){			
			request.setAttribute("msg", "提现申请成功！");
			request.setAttribute("gotoUrl", "/nb/wechat/account/main.html");
			return "authSuccess";
		}else{
			request.setAttribute("msg", "提现申请失败！");
			return "authError";
		}
	}
	
	/**
	 * 检测银行卡是否已被绑定
	 * 
	 * @throws Exception
	 */
	@Action("/nb/wechat/cash/checkBankIsExist")
	public void checkBankIsExist() throws Exception {
		data = new HashMap<String, Object>();
		String bankNo = paramString("cardNo").replace(" ", "");
		AccountBank bank = theAccountBankService.findByBankNo(bankNo);
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
	@Action("/nb/wechat/cash/checkBankCanUse")
	public void checkBankCanUse() throws Exception {
		data = new HashMap<String, Object>();
		String cardNo = paramString("cardNo").replace(" ", "");
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
	@Action(value="/nb/wechat/cash/myBank")
	public void myBank() throws Exception {
		user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		String bankJosn = LianlPayHelper.queryBankcardList(user.getUserId());
		printJson(bankJosn);
	}	
	/**
	 * bin银行卡
	 * 
	 * @return
	 */
	@Action(value="/nb/wechat/cash/binBank")
	public void binBank() throws Exception {
		String cardNo = paramString("cardNo").replace(" ", "");
		String bankJosn = LianlPayHelper.queryCardBin(cardNo);
		printJson(bankJosn);
	}
	
	/**
	 * 获取提现验证码
	 * @return
	 * @throws Exception
	 */
	@Action(value="/nb/wechat/cash/getCashCode")
	public String getCashCode() throws Exception {
		logger.info("====================准备请求发送验证码==================\r\n");
		
		String tel = paramString("mobilePhone");
		
		BaseAccountLog blog = new GetCodeLog(null,"",null,tel,NoticeConstant.CASH_GET_CODE);
		blog.initCode("cashGetCode");
		blog.doEvent();

		printWebSuccess();
		logger.info("====================请求发送验证码的工作结束==================\r\n");
		return null;
	}
	
	
	/**
	 * 获取绑卡验证码（银联在线专用）
	 * @return
	 * @throws Exception
	 */
	@Action(value="/nb/wechat/cash/getAddBankCode")
	public String getAddBankCode() throws Exception {
		logger.info("====================准备请求发送验证码==================\r\n");
		UnionPayRet ret = SignHelper.sendCode(paramString("mobilePhone"));
		if(ret == null) {
			printWebResult("银联暂无响应，请稍后重试", false);
		}else{
			if("0000".equals(ret.getRetCode())){
				printWebSuccess();
			}else{
				printWebResult(ret.getRetDesc(), false);
			}
			logger.info("====================请求发送验证码的工作结束==================\r\n 返回码是："+ret.getRetCode());
		}
		return null;
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
			request.setAttribute("msg", "该身份证已被使用,请重新输入！");
			return goUrl;
		}
		UnionPayRet cardInfo = SignHelper.cardInfo(cardno);
		if(cardInfo != null) {
			if(StringUtil.isNotBlank(cardInfo.getCardType())){
				if(!cardInfo.getCardType().equals("借记卡")){
					request.setAttribute("msg", "暂不支持信用卡,请重新输入！");
					return goUrl;
				}
			}else{
				request.setAttribute("msg", "银行卡信息有误,请重新输入！");
				return goUrl;
			}
		}else{
			request.setAttribute("msg", "银行卡信息有误,请重新输入！");
			return goUrl;
		}
		return "";
	}
}
