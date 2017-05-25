package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 设置银行卡为默认绑定
 * @author Administrator
 *
 */
public class BankCodeBindingSetDefault extends PayModel {
	
	private String bankCardNo ;
	private String userId;
	private String[] paramNames = new String[]{"service","partnerId",
	                 "signType","sign","orderNo","bankCardNo","userId"};
	
	/*{"message":"设置银行卡为默认绑定","resultCode":"EXECUTE_SUCCESS","success":true}
	 */
	private String message;
	private String resultCode;
	private String success;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	

}
