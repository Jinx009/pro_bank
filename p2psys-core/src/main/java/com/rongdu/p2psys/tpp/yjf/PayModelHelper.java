package com.rongdu.p2psys.tpp.yjf;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.tpp.domain.YjfDrawBank;
import com.rongdu.p2psys.tpp.domain.YjfPay;
import com.rongdu.p2psys.tpp.yjf.model.BankCodeBindingAddInfo;
import com.rongdu.p2psys.tpp.yjf.model.BankCodeBindingRemove;
import com.rongdu.p2psys.tpp.yjf.model.BankNoQuery;
import com.rongdu.p2psys.tpp.yjf.model.ForwardConIdentify;
import com.rongdu.p2psys.tpp.yjf.model.PayModel;
import com.rongdu.p2psys.tpp.yjf.model.Recharge;
import com.rongdu.p2psys.tpp.yjf.model.TradeCreatePoolReverse;
import com.rongdu.p2psys.tpp.yjf.model.TradeCreatePoolTogether;
import com.rongdu.p2psys.tpp.yjf.model.TradePayPoolReverse;
import com.rongdu.p2psys.tpp.yjf.model.TradePayPoolTogether;
import com.rongdu.p2psys.tpp.yjf.model.TradePayerApplyPoolTogether;
import com.rongdu.p2psys.tpp.yjf.model.TradePayerQuitPoolTogether;
import com.rongdu.p2psys.tpp.yjf.model.TradePoolReceiveBorrow;
import com.rongdu.p2psys.tpp.yjf.model.TradeTransfer;
import com.rongdu.p2psys.tpp.yjf.model.VerifyFacade;
import com.rongdu.p2psys.tpp.yjf.model.YzzNewWithraw;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;

public class PayModelHelper {
	private static final Logger logger = Logger.getLogger(PayModelHelper.class);
	
	public static String doSubmit(PayModel mod) {
		String res = null;
		try {
			res = mod.submit();
		} catch (Exception e) {
			logger.error(e);
		}
		return res;
	}
	
	/**
	 * 是否开通线上环境配置。
	 * @return
	 */
	public static boolean isOnlineConfig(){
		return "1".equals(Global.getValue("config_online"));
	}
	
	/**
	 * 是否开通第三方接口
	 * @return
	 */
	public static boolean isOpenApi(){
		return "1".equals(Global.getValue("is_open_deposit"));
	}
	
	
   
    /**
	 * 易极付新注册
	 * @param user
	 * @return
	 */
    public static ForwardConIdentify userForwardRegister(UserModel user){
    	ForwardConIdentify userRegister = new ForwardConIdentify();
    	userRegister.setService("forwardConIdentify");
        userRegister.setOrderNo(OrderNoUtils.getSerialNumber());
        userRegister.setUserName(Global.getValue("webid") + user.getUserName());
        if ("1".equals(Global.getValue("company_register"))) { //开通企业注册
        	userRegister.setUserType("B");
        } else { //开通个人注册。
        	userRegister.setUserType("P");
        }
        userRegister.setRealName(user.getRealName());
        userRegister.setEmail(user.getEmail());
        userRegister.setCertNo(user.getCardId());
        userRegister.setReturnUrl(Global.getValue("weburl")+"/public/forwardRegisterReturn.html");
        userRegister.setNotifyUrl(Global.getValue("weburl")+"/public/forwardRegisterNotify.html");
        userRegister.Createsign();
    	return userRegister;
    }
    
    /**
     * 易极付充值
     * @param uc UserCache对象
     * @param money 充值金额
     * @param extra 额外参数
     * @return Recharge
     */
    public static Recharge recharge(User user, String money, String extra) {
    	String weburl = Global.getValue("weburl");
    	
    	Recharge re = new Recharge();
    	re.setService("deposit");
    	re.setReturnUrl(weburl + "/member/recharge/log.html");
        re.setNotifyUrl(weburl + "/public/rechargeNotify.html");
    	re.setOrderNo(extra);
    	// TODO zjj
    	// re.setUserId(user.getApiId() + "");
    	re.setTradeMerchantId(Global.getValue("yjf_partnerId"));
    	if (isOnlineConfig()) {
    		re.setTradeBizProductCode(Global.getValue("person_recharge")); //线上环境，个人充值收费规则。
    	} else {
    		re.setTradeBizProductCode("hjd_deposit"); //测试环境，个人充值收费规则。
    	}
    	re.setDepositAmount(money);
    	
    	re.Createsign();
    	return re;
    }
    /**
     * 易极付投标 
     * @param payerUserId 对象
     * @param money 投标金额
     * @param tradeNo 投标金额
     * @return TradePayerApplyPoolTogether
     */
    public static TradePayerApplyPoolTogether tradePayerApplyPoolTogether(String payerUserId, String money, String tradeNo){
    	TradePayerApplyPoolTogether tpp = new TradePayerApplyPoolTogether();
    	
        tpp.setService("tradePayerApplyPoolTogether");
        String number =  OrderNoUtils.getSerialNumber();
        tpp.setOrderNo(number);
        tpp.setTradeNo(tradeNo);
        tpp.setPayerUserId(payerUserId);
        tpp.setTradeAmount(money);
        tpp.setTradeName(Global.getValue("trade_name_addtender"));
        String res = doSubmit(tpp);
        Map<String, String>  map  = (Map<String, String>)JSON.parse(res);
        tpp.setResultCode(map.get("resultCode"));
        tpp.setSubTradeNo(map.get("subTradeNo"));
        tpp.setResultMessage(map.get("resultMessage"));
        return tpp;
    }
	
    /**
     * 绑卡接口
     * @param cardNo 卡号
     * @param apiId 客户号
     * @param name  真实姓名
     * @param bankType  银行卡简称
     * @return  是否绑定成功
     */
	public static BankCodeBindingAddInfo  bankCodeBindingAddInfo(String cardNo, String apiId,
			String name, String bankType) {
		BankCodeBindingAddInfo bba = new BankCodeBindingAddInfo();
		bba.setService("bankCodeBinding.addInfo");
		bba.setOrderNo(OrderNoUtils.getSerialNumber());
		bba.setBankCardNo(cardNo);
		bba.setUserId(apiId);
		bba.setName(name);
		bba.setBankType(bankType);
		String res = doSubmit(bba);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) JSON.parse(res);
		bba.setMessage(map.get("message") + "");
		bba.setResultCode(map.get("resultCode") + "");
		bba.setSuccess(map.get("success") + "");
 		return bba;
	}
	
	/**
	 * 解绑银行卡
	 * @param userId 以给付客户号，不同于我们自身user表中的主键
	 * @param bankCardNo 银行卡号
	 * @return
	 */
	public static BankCodeBindingRemove bankCodeBindingRemove(String userId, String bankCardNo) {
		BankCodeBindingRemove bb = new BankCodeBindingRemove();
		bb.setService("bankCodeBindingRemove");
		bb.setOrderNo(OrderNoUtils.getSerialNumber());
		bb.setUserId(userId);
		bb.setBankCardNo(bankCardNo);
		String res = doSubmit(bb);
		Map<String, String> map = (Map<String, String>) JSON.parse(res);
		bb.setResultCode(map.get("resultCode"));
		return bb;
	}
	
	/**
	 * 提现接口
	 * @param cash 提现对象
	 * @param uc  用户信息
	 * @param cashnum 成提现笔数
	 * @return  提现
	 */
	public static YzzNewWithraw yzzNewWithraw(AccountCash cash, User uc, int cashnum,String province, String city,String bankCode) {
		String weburl = Global.getValue("weburl");  
		YzzNewWithraw awd = new YzzNewWithraw();
		awd.setService("yzzNewWithraw");
		String orderNo = OrderNoUtils.getSerialNumber();
		awd.setOrderNo(orderNo);
		// TODO zjj
		// awd.setUserId(uc.getApiId());
		awd.setMoney(cash.getMoney() + "");
		//province  city BankCode 需要从service传递过来
		awd.setBankProvName(province);
		awd.setBankCityName(city + "市");
		awd.setBankCode(bankCode);
		awd.setBankAccountNo(cash.getBankNo() + "");
		awd.setDelay(cash.getDrawType() + "");
		awd.setNotifyUrl(weburl + "/public/cashNotify.html");
		
		//前几笔不收取现手续费。
		if (cashnum < Global.getInt("cash_num")) {
			logger.info("t+0小于等于笔数时，用户自己平台承担费用");
			awd.setPayMode("P");
		} else {
			//正常取现
			logger.info("t+0大于笔数时,用户自己承担费用");
			awd.setPayMode("U");
		}
		String res = doSubmit(awd);
		Map<String, Object> map = (Map<String, Object>) JSON.parse(res);
		awd.setResultCode(map.get("resultCode") + "");
		awd.setResultMessage(map.get("message") + "");
		return awd;
	}
	
	/**
	 * 转账
	 * @param sellerUserId 收款人
	 * @param payerUserId 付款人
	 * @param money  金额
	 * @param tradeName  交易名称
	 * @return 转账结果
	 */
	public static TradeTransfer tradeTransfer(String sellerUserId, String payerUserId,
			String money, String tradeName) {
		TradeTransfer tt = new TradeTransfer();
		tt.setOrderNo(OrderNoUtils.getSerialNumber());
		tt.setService("tradeTransfer");
		//收款人
		tt.setSellerUserId(sellerUserId); 
		// 付款人
		tt.setPayerUserId(payerUserId); 
		// 付款人
		tt.setBuyerUserId(payerUserId);
		String name =  payerUserId + "转账给" + sellerUserId + ",money:" + money;
		tt.setTradeName(tradeName);
		if (isOnlineConfig()) {
			//转账
			tt.setTradeBizProductCode(Global.getValue("person_transfer"));
		}
		tt.setProduct("transfer");
		tt.setTradeType("transfer");
		tt.setGatheringType("SERVICE_BUY");
		tt.setTradeAmount(money);
		tt.setCurrency("CNY");
		String goodStr = "[{'name': '" + name + "','price':" + "'" + money + "','quantity':'1'}]";
		tt.setGoods(goodStr);
		String res = doSubmit(tt);
		Map<String, String> map = (Map<String, String>) JSON.parse(res); // 封装返回信息
		tt.setResultCode(map.get("resultCode"));
		tt.setResultMessage(map.get("resultMessage"));
		return tt;
	}
	/**
	 * 给力标创建
	 * 集体集资交易创建接口tradeCreatePoolTogether
	 *  {"channelId":"00001d",
	 *  "orderNo":"2013060100000029",
	 *  "resultCode":"EXECUTE_SUCCESS",
	 *  "resultMessage":"执行成功",
	 *  "sign":"b36fe9d30e6ec162447798f1c70d82d3",
	 *  "signType":"MD5",
	 *  "subTradeNo":"null",
	 *  "success":"T",
	 *  "tradeNo":"20130601100055091252"}
	 */
	public static TradeCreatePoolTogether tradeCreatePoolTogether(String money, String  apiId){
		TradeCreatePoolTogether  tpt = new TradeCreatePoolTogether();
		
		tpt.setOrderNo(OrderNoUtils.getSerialNumber());
		tpt.setService("tradeCreatePoolTogether");
		tpt.setTradeName(Global.getValue("trade_name_addborrow"));
		tpt.setSellerUserId(apiId);
		if (isOnlineConfig()) {
			tpt.setTradeBizProductCode(Global.getValue("borrow_create"));  //给力标，创建产品编号
		} else  {
			tpt.setTradeBizProductCode("20130326_together");  //线下
		}
		tpt.setProduct("POOL_TOGETHER");
		tpt.setTradeAmount(money);
		tpt.setTradeType("POOL_TOGETHER");
		tpt.setGatheringType("SERVICE_BUY");
		tpt.setCurrency("CNY");
		tpt.setGoods("[{'name':'发标创建交易号" + "','price':'" + money + "','quantity':'1'}]");
	    String res = doSubmit(tpt);
	    Map<String, String> map = (Map<String, String>) JSON.parse(res); // 封装返回信息
	    tpt.setChannelId(map.get("channelId"));
	    tpt.setResultCode(map.get("resultCode"));
	    tpt.setResultMessage(map.get("resultMessage"));
	    tpt.setSuccess(map.get("success"));
	    tpt.setTradeNo(map.get("tradeNo"));
	    return tpt;
	}

	/**
	 * 创建还款交易号
	 * @param apiId 用户第三方ID
	 * @param money 资金
	 * @return TradeCreatePoolReverse
	 */
	@SuppressWarnings("unchecked")
	public static TradeCreatePoolReverse tradeCreatePoolReverse(String apiId, double money) {
		TradeCreatePoolReverse tc = new TradeCreatePoolReverse();
		
		tc.setOrderNo(OrderNoUtils.getSerialNumber());
		tc.setService("tradeCreatePoolReverse");
		String name = "trade_name_create_repay"; 
		tc.setTradeName(name);
		tc.setPayerUserId(apiId);
		if (isOnlineConfig()) {
			tc.setTradeBizProductCode(Global.getValue("borrow_repay")); //还款,产品编号
		} else {
			tc.setTradeBizProductCode("20130326_reverse");
		}
		tc.setTradeAmount(money + "");
		tc.setProduct("poolReverse");
		tc.setTradeType("POOL_REVERSE");
		tc.setGatheringType("SERVICE_BUY");
		tc.setCurrency("CNY");
		String goodStr = "[{'name':" + "'" + name + "','price':" + "'" + money + "','quantity':'1'}]";
		tc.setGoods(goodStr);
		
		String res = doSubmit(tc);
		Map<String, String> map = (Map<String, String>) JSON.parse(res);
		tc.setResultCode(map.get("resultCode"));
		tc.setResultMessage(map.get("resultMessage"));
		tc.setTradeNo(map.get("tradeNo"));
		return tc;	
	}
	/**
	 * 集资交易还款接口
	 * @param tradeNo
	 * @param payerUserId  付款人
	 * @param tenders  所有的收款人  包括 apiId  对应的  money  封装的 二维数组
	 */
	public static TradePayPoolReverse tradePayPoolReverse(String tradeNo,String payerUserId, String subOrders,String money ){
		TradePayPoolReverse tpp = new TradePayPoolReverse();
		
		tpp.setOrderNo(OrderNoUtils.getSerialNumber());
		tpp.setService("tradePayPoolReverse");
		tpp.setTradeNo(tradeNo);
		tpp.setSubOrders(subOrders);
	    String res = doSubmit(tpp);	   
	    Map<String, String> map = (Map<String, String>)JSON.parse(res);
	    tpp.setInfo(map.get("info"));
	    tpp.setResultCode(map.get("resultCode"));
	    tpp.setResultMessage(map.get("resultMessage"));
	    return tpp;
	}
	/**
	 * 流转标款付款接口,没有冻结  直接转账
	 */
	public static TradePoolReceiveBorrow tradePoolReceiveBorrow(YjfPay yjfPay){
		TradePoolReceiveBorrow tt = new TradePoolReceiveBorrow();
		tt.setService("tradePoolReceiveBorrow");
	    tt.setOrderNo(OrderNoUtils.getSerialNumber());
	    tt.setTradeNo(yjfPay.getTradeno());
	    String tenderStr = yjfPay.getUserid();
	    String[] tenders = tenderStr.split("=");  
	    StringBuffer sbf =  new StringBuffer();
		sbf.append("{");
		sbf.append("'orderNo':'" + OrderNoUtils.getSerialNumber() + "'," );
		sbf.append("'payerUserId':'" + tenders[0] + "',");
		sbf.append("'payeeUserId':'" + yjfPay.getTouserid() + "',"); //收款人
		sbf.append("'transferAmount':'" + tenders[1] + "',");
		sbf.append("'tradeName':'" + Global.getValue("trade_name_addflowtender") + "'");
		sbf.append("}");
		String subOrders = "[" + sbf.toString() + "]";
	    tt.setTradePoolSubTansferOrders(subOrders);
	    String res =  doSubmit(tt);
	    Map<String, String> map = (Map<String, String>)JSON.parse(res);
	    tt.setInfo(map.get("info"));
	    tt.setResultCode(map.get("resultCode"));
	    tt.setResultMessage(map.get("resultMessage"));
	    return tt;
	}
	/**
     *  交易借出人申请退出集资
     * @param tradeNo
     * @param subTradeNo
     * @param payApiId   交易用户的id  易极付 那边对应的用户的id
     * @return
     * {"channelId":"00001d",
     * "orderNo":"2013060100000036",
     * "resultCode":"EXECUTE_SUCCESS",
     * "resultData":"{\"SUB_TRADE_NO\":\"20130601100055091267\"}",
     * "resultMessage":"执行成功",
     * "sign":"e850827f83a6d7622e77084d4eed398c",
     * "signType":"MD5",
     * "subTradeNo":"20130601100055091267",
     * "success":"T",
     * "tradeNo":"20130601100055091252"}
     */
    public static TradePayerQuitPoolTogether tradePayerQuitPoolTogether(String tradeNo, String subTradeNo,String payApiId){
    	TradePayerQuitPoolTogether tqt = new TradePayerQuitPoolTogether();
    	
    	tqt.setService("tradePayerQuitPoolTogether");
    	tqt.setOrderNo(OrderNoUtils.getSerialNumber());
    	tqt.setPayerUserId(payApiId);
    	tqt.setTradeNo(tradeNo);
    	tqt.setTradeMemo(Global.getValue("trade_name_cancelborrow"));
    	tqt.setSubTradeNo(subTradeNo);
    	String res = doSubmit(tqt);
    	Map<String, String> map = (Map<String, String>)JSON.parse(res);
    	tqt.setResultCode(map.get("resultCode"));
    	tqt.setResultMessage(map.get("resultMessage"));
    	return tqt;
    }
    /**
   	 * 设置交易集体付款    这里只是处理  放款的过程中的手续费其他的不处理，  transferAmount 代表处理的手续费用
   	 * {"channelId":"00001d",
   		"info":"[{}]"
   		"orderNo":"2013060200000041",
   		"resultCode":"EXECUTE_SUCCESS",
   		"resultMessage":"执行成功",
   		"sign":"baa3ad8225cb40f7e9f9393078b4d0f2",
   		"signType":"MD5","subTradeNo":"null","success":"T",
   		"tradeNo":"20130602100055093491"}
   	 * tradePayPoolTogether
   	 */
   	public static TradePayPoolTogether tradePayPoolTogether(String tradeNo){
   	    TradePayPoolTogether tt = new TradePayPoolTogether();
   	    
   	    tt.setService("tradePayPoolTogether");
   	    tt.setOrderNo(OrderNoUtils.getSerialNumber());
   	    tt.setTradeNo(tradeNo);
   	    tt.setTradeMemo(Global.getValue("trade_name_verifysuccess"));
   	    String res =  doSubmit(tt);
   	    Map<String, String> map = (Map<String, String>)JSON.parse(res);
   	    tt.setInfo(map.get("info"));
   	    tt.setResultCode(map.get("resultCode"));
   	    tt.setResultMessage(map.get("resultMessage"));
   	    return tt;
   	}
   	/**
	 * 当前地区是否存在该银行
	 * @param districtName 
	 * @param bankId
	 * @return
	 */
	public static BankNoQuery bankNoQuery (String districtName, String  bankId){
		BankNoQuery bq = new BankNoQuery();
		
	    bq.setOrderNo(OrderNoUtils.getSerialNumber());
	    bq.setService("bankNoQuery");
	    bq.setBankId(bankId);
	    if(!districtName.contains("市")){
	    	bq.setDistrictName(districtName + "市");
	    }else{
	    	bq.setDistrictName(districtName);
	    }
	    String res = doSubmit(bq);
	    Map<String, Object> map = (Map<String, Object>)JSON.parse(res);
	    if ( map.get("bankLasalle") == null ){
	    	bq.setBankLasalle("");
	    } else {
	    	bq.setBankLasalle(map.get("bankLasalle") + "");
	    }
	    bq.setBranchName(map.get("branchName") + "");
		return bq;
		
	}
	/**
	 * 校验银行卡的有效性
	 */
	public static VerifyFacade verifyFacade (String accountNo  , YjfDrawBank db, User user,User uc ){
		
		VerifyFacade vf = new VerifyFacade();
		vf.setService("verifyFacade");
		String orderNo = OrderNoUtils.getSerialNumber();
		vf.setOrderNo(orderNo);
		vf.setExtendId(orderNo);
		vf.setBankCode(db.getBankCode());
		vf.setChannelApi(db.getPayChannelApi());
		vf.setAccountName(user.getRealName());
		vf.setAccountNo(accountNo);
		vf.setCardType("D"); //借记卡
		
		vf.setCertType("ID");
		vf.setCertNo(uc.getCardId());
		
		String res = doSubmit(vf);
		Map<String, Object> map = (Map<String, Object>)JSON.parse(res);
	    vf.setVerifyStatus(map.get("verifyStatus") + "");
	    vf.setResultCode(map.get("resultCode")+"");
	    vf.setResultMessage(map.get("resultMessage")+"");
		return vf;
	}
	/**
	 * 流转标
	 * 集资创建交易接口和普通标创建一样只是参数有变化  那就用同一个吧   大家写的时候要注意
	 * 
	 */
	public static TradeCreatePoolTogether tradeCreatePool(String apiId,String money){
		TradeCreatePoolTogether  tpt = new TradeCreatePoolTogether();
		
		tpt.setOrderNo(OrderNoUtils.getSerialNumber());
		tpt.setService("tradeCreatePool");
		tpt.setTradeName(Global.getValue("trade_name_addborrow"));
		tpt.setSellerUserId(apiId);
		if(isOnlineConfig()){
			tpt.setTradeBizProductCode(Global.getValue("borrow_flow_create"));  //流转标产品编号
		}else{
			tpt.setTradeBizProductCode("20130326_pool");  // 要确定
		}
		tpt.setProduct("POOL"); //流程产品（测试是随便给定值）  以后要确定
		tpt.setTradeAmount(money);
		tpt.setTradeType("POOL");
		tpt.setGatheringType("SERVICE_BUY");
		tpt.setCurrency("CNY");
		tpt.setGoods("[{'name':'流标创建交易号" +"','price':'" + money + "','quantity':'1'}]");
	    String res = doSubmit(tpt);
	    Map<String, String> map = (Map<String, String>)JSON.parse(res); // 封装返回信息
	    tpt.setChannelId(map.get("channelId"));
	    tpt.setResultCode(map.get("resultCode"));
	    tpt.setResultMessage(map.get("resultMessage"));
	    tpt.setSuccess(map.get("success"));
	    tpt.setTradeNo(map.get("tradeNo"));
	    return tpt;
	}
	
}
