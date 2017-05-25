package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 新提现接口
 * v1.8.0.4_u4 TGPROJECT-360  qinjun  2014-07-11  
 * @author qinjun
 *
 */
public class YzzNewWithraw extends PayModel {
	private String userId;
	private String bankProvName;
	private String bankCityName;
	private String bankAccountNo;
	private String money;
	private String bankCode;
	/**
	 * P平台商 ,U用户,手续费支付用户
	 */
	private String payMode;
	/**
	 * 取现类型  0 == T+0, 1 == T+1, 3 == T+3 ,默认为T+0
	 */
	private String delay;
	private String notifyUrl;
	
	private String[] paramNames = new String[]{"service","partnerId",
			"orderNo","sign","signType","notifyUrl","userId","bankProvName","bankCityName","bankAccountNo","outBizNo","money",
	         "bankCode","payMode","delay"};
	//封装
	private String resultCode;
	private String resultMessage;
	private String outBizNo;
	private String success;
	private String amountIn;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBankProvName() {
		return bankProvName;
	}
	public void setBankProvName(String bankProvName) {
		this.bankProvName = bankProvName;
	}
	public String getBankCityName() {
		return bankCityName;
	}
	public void setBankCityName(String bankCityName) {
		this.bankCityName = bankCityName;
	}
	public String getBankAccountNo() {
		return bankAccountNo;
	}
	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getDelay() {
		return delay;
	}
	public void setDelay(String delay) {
		this.delay = delay;
	}
	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getOutBizNo() {
		return outBizNo;
	}
	public void setOutBizNo(String outBizNo) {
		this.outBizNo = outBizNo;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getAmountIn() {
		return amountIn;
	}
	public void setAmountIn(String amountIn) {
		this.amountIn = amountIn;
	}
	
}
