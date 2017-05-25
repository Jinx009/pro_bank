package com.rongdu.p2psys.account.model.payment.ips;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import jxl.common.Logger;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.RechargeUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.constant.RechargeConstant;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.service.PayService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.BeanUtil;

/**
 * 环讯直连
 * @author cx
 * @version 2.0
 * 2014-10-27
 */
public class IpsPay extends BasePayment{
	private static Logger logger=Logger.getLogger(IpsPay.class); 
	private String mer_code;
	private String billno;
	private String amount;
	private String date;
	private String currency_Type;
	private String gateway_Type;
	private String lang;
	private String merchanturl;
	private String failUrl;
	private String errorUrl;
	private String attach;
	private String orderEncodeType;
	private String retEncodeType;
	private String rettype;
	private String serverUrl;
	private String signMD5;
	private String mer_key;
	private String doCredit;
	private String bankco;
	
	//callback 新增的
	private String succ;
	private String msg;
	private String ipsbillno;
	private String signature;
	
	// 提交地址
	private String submitUrl;
	
	public IpsPay() {
		super();
	}
	
    public void init(AccountRecharge recharge){
		String weburl = Global.getValue("weburl");
		this.payname = "ips"; //返回html页面
		this.orderPrefix = "H";
		PayService payService = (PayService) BeanUtil.getBean("payService");
		this.pay = payService.findByNid(RechargeConstant.IPS_PAY);
		this.mer_code = pay.getMerchantId();
		this.mer_key = pay.getGoodsKey();
		this.merchanturl = weburl + pay.getReturnUrl();
		this.failUrl = weburl + pay.getReturnUrl();
		this.serverUrl = weburl + pay.getReturnUrl();
		this.billno = RechargeUtil.generateTradeNO(recharge.getUser().getUserId(), this.orderPrefix);
		this.tranNo = billno;
		String money = String.valueOf(recharge.getMoney());
		this.amount = money;
		this.date = DateUtil.dateStr(new Date(), "yyyyMMdd");
		this.gateway_Type = "01";
		this.currency_Type = "RMB";
		this.lang = "GB";
		this.orderEncodeType = "5";
		this.retEncodeType = "17";
		this.rettype = "1";
		if (!StringUtil.isBlank(bankco)) {
			this.doCredit = "1";
		}
		this.submitUrl = pay.getGatewayUrl();
		this.signMD5 = encodeSignMD5();
    }
    
	public String encodeSignMD5(){
		cryptix.jce.provider.MD5 b=new cryptix.jce.provider.MD5();
		//订单加密的明文 billno+【订单编号】+ currencytype +【币种】+ amount +【订单金额】+
		// date +【订单日期】+ orderencodetype +【订单支付接口加密方式】+【商户内部证书字符串】 
		String value="billno"+billno +"currencytype"+currency_Type+"amount"+ this.format2Str(Double.parseDouble(amount)) + 
				"date" +date +"orderencodetype"+orderEncodeType + mer_key;
		logger.info(value);
		String signMD5 = b.toMD5(value).toLowerCase();
		logger.info("IPSPay signMD5:"+signMD5);
		return signMD5;
	}

	public String callbackSign(){
		String content="billno"+billno + "currencytype"+currency_Type+"amount"+amount
				+"date"+date+"succ"+succ+"ipsbillno"+ipsbillno+"retencodetype"+retEncodeType + mer_key;  
		return content;
	}
	
	@Override
	public String toString() {
		return "IPSPay [mer_code=" + mer_code + ", billno=" + billno
				+ ", amount=" + amount + ", date=" + date + ", currency_Type="
				+ currency_Type + ", gateway_Type=" + gateway_Type + ", lang="
				+ lang + ", merchanturl=" + merchanturl + ", failUrl="
				+ failUrl + ", errorUrl=" + errorUrl + ", attach=" + attach
				+ ", orderEncodeType=" + orderEncodeType + ", retEncodeType="
				+ retEncodeType + ", rettype=" + rettype + ", serverUrl="
				+ serverUrl + ", signMD5=" + signMD5 + ", mer_key=" + mer_key
				+ "]";
	}
	
	public void doCallBack(HttpServletRequest request){
		PayService payService = (PayService) BeanUtil.getBean("payService");
		this.pay = payService.findByNid(RechargeConstant.IPS_PAY);
		this.mer_key = pay.getGoodsKey();
		String currency_type = request.getParameter("Currency_type");
		String retencodetype = request.getParameter("retencodetype");
		this.setCurrency_Type(currency_type);
		this.setRetEncodeType(retencodetype);
	}
	

	@Override
	public boolean reponseSuccess() {
		cryptix.jce.provider.MD5 b=new cryptix.jce.provider.MD5();
		String signMD5 = b.toMD5(callbackSign()).toLowerCase();
		if (signMD5.equals(getSignature()) && "Y".equals(getSucc())) {
				return true;
		}
		return false;
	}
	
	@Override
	public String getTranNo() {
		return billno;
	}
	
	@Override
	public String getRemoteReturnSign() {
		return msg;
	}

	public String getMer_code() {
		return mer_code;
	}

	public void setMer_code(String mer_code) {
		this.mer_code = mer_code;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCurrency_Type() {
		return currency_Type;
	}

	public void setCurrency_Type(String currency_Type) {
		this.currency_Type = currency_Type;
	}

	public String getGateway_Type() {
		return gateway_Type;
	}

	public void setGateway_Type(String gateway_Type) {
		this.gateway_Type = gateway_Type;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getMerchanturl() {
		return merchanturl;
	}

	public void setMerchanturl(String merchanturl) {
		this.merchanturl = merchanturl;
	}

	public String getFailUrl() {
		return failUrl;
	}

	public void setFailUrl(String failUrl) {
		this.failUrl = failUrl;
	}

	public String getErrorUrl() {
		return errorUrl;
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOrderEncodeType() {
		return orderEncodeType;
	}

	public void setOrderEncodeType(String orderEncodeType) {
		this.orderEncodeType = orderEncodeType;
	}

	public String getRetEncodeType() {
		return retEncodeType;
	}

	public void setRetEncodeType(String retEncodeType) {
		this.retEncodeType = retEncodeType;
	}

	public String getRettype() {
		return rettype;
	}

	public void setRettype(String rettype) {
		this.rettype = rettype;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getSignMD5() {
		return signMD5;
	}

	public void setSignMD5(String signMD5) {
		this.signMD5 = signMD5;
	}

	public String getMer_key() {
		return mer_key;
	}

	public void setMer_key(String mer_key) {
		this.mer_key = mer_key;
	}

	public String getDoCredit() {
		return doCredit;
	}

	public void setDoCredit(String doCredit) {
		this.doCredit = doCredit;
	}

	public String getBankco() {
		return bankco;
	}

	public void setBankco(String bankco) {
		this.bankco = bankco;
	}

	public String getSucc() {
		return succ;
	}

	public void setSucc(String succ) {
		this.succ = succ;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getIpsbillno() {
		return ipsbillno;
	}

	public void setIpsbillno(String ipsbillno) {
		this.ipsbillno = ipsbillno;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }
}
