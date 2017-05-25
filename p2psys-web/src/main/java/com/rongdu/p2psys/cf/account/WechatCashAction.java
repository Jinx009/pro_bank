package com.rongdu.p2psys.cf.account;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.model.AccountCashModel;
import com.rongdu.p2psys.account.model.payment.llPay.conn.CommonRealize;
import com.rongdu.p2psys.account.model.payment.llPay.handler.LianlPayHelper;
import com.rongdu.p2psys.account.model.payment.llPay.model.OrderInfo;
import com.rongdu.p2psys.account.model.payment.llPay.model.UserBankCardRet;
import com.rongdu.p2psys.account.model.payment.unionPay.SignHelper;
import com.rongdu.p2psys.account.model.payment.unionPay.UnionPayRet;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.SystemConfig;
import com.rongdu.p2psys.core.service.SystemConfigService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.payment.domain.ChannelConfig;
import com.rongdu.p2psys.nb.payment.domain.NbSupportBank;
import com.rongdu.p2psys.nb.payment.service.IChannelConfigService;
import com.rongdu.p2psys.nb.payment.service.IOrderService;
import com.rongdu.p2psys.nb.payment.service.ISupportBankService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.ConstantUtil.channelKey;
import com.rongdu.p2psys.nb.util.GetCityCode;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;

public class WechatCashAction extends BaseAction<AccountCashModel> implements ModelDriven<AccountCashModel> {

	@Resource
	private AccountService theAccountService;
	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private IOrderService orderService;
	@Resource
	private SystemConfigService systemConfigService;
	@Resource
	private IChannelConfigService channelConfigService;
	@Resource
	private AccountCashService accountCashService;
	@Resource
	private ISupportBankService theSupportBankService;
	
	private User user;
	private Map<String, Object> data;
	
	/**
	 * 众筹-微信提现
	 * 
	 * @return
	 */
	@Action(value = "/cf/wechat/user/cash", results = { @Result(name = "cash", type = "ftl", location = "/nb/cf/wechat/cash/cash.html") })
	public String cash() {
		SystemConfig sc = systemConfigService.findByNid(Constant.OFFLINE_CASH_MONEY);
		int minCashMoney = Integer.parseInt(sc.getValue());//线下提现最小金额
		request.setAttribute("minCashMoney",minCashMoney);
		return "cash";
	}
	
	/**
	 * 提现页面数据
	 * 
	 * @return
	 */
	@Action("/cf/wechat/cash/cash")
	public void newCash() throws Exception {
		data = new HashMap<String, Object>();
		if(hasSessionUser()){
			user = getNBSessionUser();
			saveToken("nbCashToken");
			SystemConfig sc = systemConfigService.findByNid(Constant.OFFLINE_CASH_MONEY);
			int minCashMoney = Integer.parseInt(sc.getValue());//线下提现最小金额
			data.put("minCashMoney", minCashMoney);
			data.put("nbCashToken", session.get("nbCashToken"));

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
			    String wapCashUrl = ConstantUtil.WAP_PAY_CASH_URL; 
				data.put("wapCashUrl", wapCashUrl);//微信端提现入口
				data.put("wapCashKey", cc.getWapCashKey());
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
	 * 提现申请
	 * 
	 * @return
	 */
	@Action("/cf/wechat/cash/doAllCash")
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
				String goods = "微信银联提现";
				if(bankList!=null && bankList.size()>0){
					if(userIdentify==null){
						userIdentify = new UserIdentify(user);
						userIdentify.setRealNameStatus(1);
						userIdentify.setMobilePhoneStatus(1);
						userIdentify.setMobilePhoneVerifyTime(new Date());
						theUserIdentifyService.saveUserIdentify(userIdentify);
					}
					AccountBank ab = bankList.get(0);
					String bankCode = ab.getBankCode();
					if(StringUtil.isNullOrBlank(bankCode)){
						bankCode = CommonRealize.bank_code_map.get(ab.getBank());
					}
					ChannelConfig cc = channelConfigService.getChannelConfigByCode(bankCode);
					channelKeys =  cc.getWapCashKey();
					if(channelKeys.equals(channelKey.llpay.getValue())){
						goods = "微信连连提现";
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
							channelKeys = channelKey.unionpay.getValue();
							goods = "微信银联提现";
						}
					}
				}else{
					if(userIdentify==null){
						userIdentify = new UserIdentify(user);
						userIdentify.setRealNameStatus(0);
						userIdentify.setMobilePhoneStatus(1);
						userIdentify.setMobilePhoneVerifyTime(new Date());
						theUserIdentifyService.saveUserIdentify(userIdentify);
					}
				}
				
				data = model.validCash_new(user, userIdentify, bankList);
				
				int cashCount = accountCashService.getTodayCashCountByUserId(user.getUserId());
				SystemConfig sc = systemConfigService.findByNid(Constant.CASH_COUNT);
				int cashnum = Integer.parseInt(sc.getValue());//提现次数
				if(cashCount<cashnum){
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
	@Action("/cf/wechat/cash/doAllOfflineCash")
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
				SystemConfig sc = systemConfigService.findByNid(Constant.CASH_COUNT);
				int cashnum = Integer.parseInt(sc.getValue());//提现次数
				if(cashCount<cashnum){
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
	 * 检测该银行卡是否可以使用
	 * @throws Exception
	 */
	@Action("/cf/wechat/cash/checkBankCanUse")
	public void checkBankCanUse() throws Exception {
		data = new HashMap<String, Object>();
		if(hasSessionUser()){	
			String cardNo = paramString("cardNo");
			boolean result = false;
			if(StringUtil.isNotBlank(cardNo)) {
//				UnionPayRet cardInfo = SignHelper.cardInfo(cardNo);
				String bankJosn = LianlPayHelper.queryCardBin(cardNo);
				UserBankCardRet cardInfo = JSONObject.parseObject(bankJosn, UserBankCardRet.class);
				if(cardInfo != null) {
					if(StringUtil.isNotBlank(cardInfo.getCard_type())){
						if(cardInfo.getCard_type().equals("2")){ //2-储蓄卡  3-信用卡
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
}
