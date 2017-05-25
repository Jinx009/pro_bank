package com.rongdu.p2psys.pc.cash;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.model.AccountCashModel;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.SupportBankService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.service.DictService;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.payment.domain.ChannelBank;
import com.rongdu.p2psys.nb.payment.domain.ChannelConfig;
import com.rongdu.p2psys.nb.payment.domain.NbSupportBank;
import com.rongdu.p2psys.nb.payment.service.IChannelBankService;
import com.rongdu.p2psys.nb.payment.service.IChannelConfigService;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.payment.service.ISupportBankService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserRedPacketService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.nb.util.GetCityCode;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;


/***
 * 提现
 * @author cgw
 * 2015-8-25
 */
@SuppressWarnings("rawtypes")
public class CashAction extends BaseAction implements ModelDriven<AccountCashModel> {
	private static Logger logger = Logger.getLogger(CashAction.class);
	private AccountCashModel model = new AccountCashModel();

	@Override
	public AccountCashModel getModel() {
		return model;
	}

	@Resource
	private AccountService theAccountService;
	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private AccountCashService accountCashService;
	@Resource
	private DictService dictService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private UserService theUserService;
	@Resource
	private UserRedPacketService theUserRedPacketService;
	@Resource
	private UserCacheService theUserCacheService;
	@Resource
	private SupportBankService supportBankService;
	@Resource
	private ISupportBankService theSupportBankService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private IChannelConfigService channelConfigService;
	@Resource
	private IChannelBankService channelBankService;
	@Resource
	private IOrderService orderService;
	@Resource
	private SystemConfigService systemConfigService;
	
	private User user;
	
	private Map<String, Object> data;

	/**
	 * 前往提现页面
	 * 
	 * @return
	 */
	@Action(value="/nb/pc/cash/cashPage",results={@Result(name = "newCash", type = "ftl", location = "/nb/pc/cash/newCash.html")})
	public String cashPage(){
		return "newCash";
	}

	/**
	 * 提现页面数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Action("/nb/pc/cash/newCash")
	public void newCash() throws Exception {
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			saveToken("nbCashToken");
			SystemConfig sc = systemConfigService.findByNid(Constant.OFFLINE_CASH_MONEY);
			int minCashMoney = Integer.parseInt(sc.getValue());//线下提现最小金额
			data.put("minCashMoney", minCashMoney);
			data.put("nbCashToken", session.get("nbCashToken"));
			/*ExtractCashChargeRuleCheck rule = (ExtractCashChargeRuleCheck) Global.getRuleCheck("extractCashCharge");
			if (rule != null && rule.getCash_status() == 1) {
				//最大提现金额
				double cashFeeMax = rule.getCash_fee_max();
				data.put("cashFeeMax", cashFeeMax);
			}*/
			long userId = user.getUserId();
			Account account = theAccountService.getAccountByUserId(userId);
			List<AccountBank> bankList = theAccountBankService.list(userId);
			if(bankList!=null && bankList.size()>0){
				AccountBank ab = bankList.get(0);
				data.put("bankModel", ab);
			    String bankCode = ab.getBankCode();
			    if(StringUtil.isNullOrBlank(bankCode)){
			    	bankCode = CommonRealize.bank_code_map.get(ab.getBank());
			    }
			    ChannelConfig cc = channelConfigService.getChannelConfigByCode(bankCode);
			    String webCashUrl = ConstantUtil.WEB_PAY_CASH_URL; 
				data.put("webCashUrl", webCashUrl);//PC端提现入口
				data.put("webCashKey", cc.getWebCashKey());
				data.put("bankCode", bankCode);
			}else{
				data.put("bankModel", 0);
			}
			data.put("cityCodeList", getStringOfJpaObj(GetCityCode.cityList));
			data.put("account", account);
			String realName = user.getRealName();
			String cardId = user.getCardId();
			data.put("realName", realName);
			data.put("cardId", cardId);
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 是否已经绑定银行卡
	 * 
	 * @return
	 */
	@Action(value="/nb/pc/cash/checkBank")
	public void checkBank() throws Exception {
		data = new HashMap<String, Object>();
		user = getNBSessionUser();
		List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
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
	@Action(value="/nb/pc/cash/nb_checkBank")
	public void nbCheckBank() throws Exception {
		data = new HashMap<String, Object>();
		user = getNBSessionUser();
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
	@Action(value="/nb/pc/cash/bank",results = { @Result(name = "bank", type = "ftl", location = "/nb/pc/cash/bank.html")})
	public String bank() throws Exception {
		return "bank";
	}
	/**
	 * 我的银行卡数据
	 * @throws IOException 
	 */
	@Action("/nb/pc/cash/myBanks")
	public void myBanks() throws IOException{
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			int bankNum = Global.getInt("bank_num");
			data.put("bankNum", bankNum);
			List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
			if(bankList!=null && bankList.size()>0){
				AccountBank ab = bankList.get(0);
				String bankCode = ab.getBankCode();
			    if(StringUtil.isNullOrBlank(bankCode)){
			    	bankCode = CommonRealize.bank_code_map.get(ab.getBank());
			    }
			    ChannelConfig cc = channelConfigService.getChannelConfigByCode(bankCode);
			    String webChannelKey = cc.getWebRechargeKey();
				data.put("webChannelKey", webChannelKey);
				data.put("bankModel", ab);
				data.put("bankCode", bankCode);
			}else{
				data.put("bankModel", 0);
			}
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 跳转我的银行卡新增页面
	 * 
	 * @return
	 */
	@Action(value="/nb/pc/cash/addBankPage",results = { @Result(name = "addBank", type = "ftl", location = "/nb/pc/cash/addBankPage.html")})
	public String addBankPage() throws Exception {
		return "addBank";
	}
	/**
	 * 新增银行卡数据
	 * @throws IOException 
	 */
	@Action("/nb/pc/cash/addBankPageOper")
	public void addBankPageOper() throws IOException{
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			UserIdentify userIdentify = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
			session.put(Constant.SESSION_USER, theUserService.getByUserId(user.getUserId()));
			session.put(Constant.SESSION_USER_IDENTIFY, theUserIdentifyService.getUserIdentifyByUserId(userIdentify.getId()));
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 获取绑卡验证码（银联在线专用）
	 * @return
	 * @throws Exception
	 */
	@Action(value="/nb/pc/cash/getAddBankCode")
	public String getAddBankCode() throws Exception {
		logger.info("====================准备请求发送验证码==================\r\n");
		UnionPayRet ret = SignHelper.sendCode(paramString("mobilePhone"));
		if(ret == null) {
			printWebResult("银联暂无响应，请稍后重试", false);
		}
		if("0000".equals(ret.getRetCode())){
			printWebSuccess();
		}else{
			printWebResult(ret.getRetDesc(), false);
		}
		logger.info("====================请求发送验证码的工作结束==================\r\n 返回码是："+ret.getRetCode());
		return null;
	}

	/**
	 * 解绑/禁用银行卡
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action("/nb/pc/cash/disableBank")
	public void disableBank() throws Exception {
		user = getNBSessionUser();
		String message = theAccountBankService.disable(user.getUserId(), paramLong("id"));
		if(!"success".equals(message)){
			printWebResult(message, false);
		}else{
			printWebSuccess();
		}
	}
	
	
	/**
	 * 提现申请
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Action("/nb/pc/cash/doAllCash")
	public void doAllCash() throws Exception {
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			String tokenMsg = checkToken("nbCashToken");
			if(!StringUtil.isBlank(tokenMsg)){				
				data.put("title",tokenMsg);
    			data.put("txt", "");
    			data.put("flag", "fail");
    			printWebJson(getStringOfJpaObj(data));
			}else{				
				//更新用户认证状态
				UserIdentify userIdentify  = theUserIdentifyService.getUserIdentifyByUserId(user.getUserId());
				String branch = paramString("branch");
				String province = paramString("province");
				String city = paramString("city");
				
				List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
				String channelKeys = channelKey.unionpay.getValue();
				String goods = "PC银联提现";
				if(bankList!=null && bankList.size()>0){
					AccountBank ab = bankList.get(0);
					String bankCode = ab.getBankCode();
					if(StringUtil.isNullOrBlank(bankCode)){
						bankCode = CommonRealize.bank_code_map.get(ab.getBank());
					}
					ChannelConfig cc = channelConfigService.getChannelConfigByCode(bankCode);
					channelKeys =  cc.getWebCashKey();
					if(channelKeys.equals(channelKey.llpay.getValue())){
						goods = "PC连连提现";
						if(StringUtil.isNullOrBlank(ab.getBranch())){
							if(StringUtil.isNullOrBlank(branch) || StringUtil.isNullOrBlank(province) || StringUtil.isNullOrBlank(city)){
								data.put("title","支行信息有误，请检查后重试");
								data.put("txt", "");
								data.put("flag", "fail");
								printWebJson(getStringOfJpaObj(data));
							}else{				
								ab.setProvince(province);
								ab.setCity(city);
								ab.setBranch(branch);
								theAccountBankService.saveAccountBank(ab);//保存支行信息
							}
						}
						//判断该用户是否绑定了连连通道，是则走连连，否则走银联
						List<AccountBank> llbankList = theAccountBankService.list(user.getUserId(),channelKey.llpay.getValue());
						if(llbankList==null || llbankList.size()==0){
							logger.info("===================PC端===该用户未绑定连连通道，自动切换为银联通道提现===银行卡："+ab.getBankNo()+",姓名："+user.getRealName());
							channelKeys = channelKey.unionpay.getValue();
							goods = "PC银联提现";
						}
					}
				}
				
				data = model.validCash_new(user, userIdentify, bankList);
				
				int cashCount = accountCashService.getTodayCashCountByUserId(user.getUserId());
				if(cashCount<=ConstantUtil.CASH_COUNT){
					AccountCash accountCash = model.prototype(bankList.get(0));
					// 创建订单
					OrderInfo order = LianlPayHelper.createOrder(1,goods,String.valueOf(accountCash.getMoney()));//提现
					order.setNo_order(accountCash.getOrderNo());
					orderService.saveOrder(user,order);
					accountCash.setChannelKey(channelKeys);
					accountCash.setTransferType(0);
					accountCash.setType(0);
					accountCashService.doCash(accountCash,user);
					data.put("flag", "success");
				}else{
					data.put("title","您今日提现次数已满");
					data.put("txt", "请明日再提");
					data.put("flag", "fail");
					printWebJson(getStringOfJpaObj(data));
				}
			}
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
   
	/**
	 * 线下提现申请
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Action("/nb/pc/cash/doAllOfflineCash")
	public void doylOfflineCash() throws Exception {
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			String tokenMsg = checkToken("nbCashToken");
			if(!StringUtil.isBlank(tokenMsg)){				
				data.put("title",tokenMsg);
    			data.put("txt", "");
    			data.put("flag", "fail");
    			printWebJson(getStringOfJpaObj(data));
			}else{
				int cashCount = accountCashService.getTodayCashCountByUserId(user.getUserId());
				if(cashCount<=ConstantUtil.CASH_COUNT){
					double money = this.paramDouble("money");
					String realName = this.paramString("realName");
					String bankNo = this.paramString("bankNo");
					String cardId = this.paramString("cardId");
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
					accountCash.setRealName(realName);
					accountCash.setCardId(cardId);
					accountCash.setTransferType(1);
					accountCash.setType(1);
					accountCash.setBranch(branch);
					accountCash.setProvince(province);
					accountCash.setCity(city);
					accountCash.setChannelKey(ConstantUtil.OFFLINE_CASH);
					accountCashService.doCash(accountCash,user);

					data.put("flag", "success");
				}else{
					data.put("title","您今日提现次数已满");
					data.put("txt", "请明日再提");
					data.put("flag", "fail");
					printWebJson(getStringOfJpaObj(data));
				}
			}
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 提现记录页面
	 * 
	 * @return
	 */
	@Action(value="/nb/pc/cash/logPage",results = { @Result(name = "log", type = "ftl", location = "/nb/pc/cash/log.html")})
	public String logPage() throws Exception {
		return "log";
	}
	
	/**
	 * 提现记录数据
	 * 
	 * @return
	 */
	@Action("/nb/pc/cash/log")
	public void log() throws Exception {
		data = new HashMap<String, Object>();
		if(hasSessionUser()){			
			user = getNBSessionUser();
			long userId = user.getUserId();
			Account account = theAccountService.getAccountByUserId(userId);
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
	@Action("/nb/pc/cash/logList")
	public void logList() throws Exception {
		data = new HashMap<String, Object>();
		if(hasSessionUser()){	
			user = getNBSessionUser();
			long userId = user.getUserId();
			PageDataList<AccountCashModel> pageDateList = accountCashService.list(userId, model.getPage(),model);
			model = accountCashService.getCashMessage(userId);
			data = new HashMap<String, Object>();
			data.put("data", pageDateList);
			data.put("model", model);
		}else{
			data = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 检测银行卡是否已被绑定
	 * 
	 * @throws Exception
	 */
	@Action("/nb/pc/cash/checkBankIsExist")
	public void checkBankIsExist() throws Exception {
		data = new HashMap<String, Object>();
		String bankNo = paramString("cardNo");
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
	@Action("/nb/pc/cash/checkBankCanUse")
	public void checkBankCanUse() throws Exception {
		data = new HashMap<String, Object>();
		if(hasSessionUser()){	
			String cardNo = paramString("cardNo");
			boolean result = false;
			if(StringUtil.isNotBlank(cardNo)) {
				UnionPayRet cardInfo = SignHelper.cardInfo(cardNo);
				if(cardInfo != null) {
					if(StringUtil.isNotBlank(cardInfo.getCardType())){
						if(cardInfo.getCardType().equals("借记卡")){
							if(StringUtil.isNotBlank(cardInfo.getBankName())){
								String bankCode = CommonRealize.bank_code_map.get(cardInfo.getBankName());
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
	 * 我已经绑定银行卡
	 * 
	 * @return
	 */
	@Action(value="/nb/pc/cash/myBank")
	public void myBank() throws Exception {
		user = getNBSessionUser();
		String bankJosn = LianlPayHelper.queryBankcardList(user.getUserId());
		printJson(bankJosn);
	}	
	/**
	 * bin银行卡
	 * 
	 * @return
	 */
	@Action(value="/nb/pc/cash/binBank")
	public void binBank() throws Exception {
		String cardNo = paramString("card_no");
		String bankJosn = LianlPayHelper.queryCardBin(cardNo);
		printJson(bankJosn);
	}

}
