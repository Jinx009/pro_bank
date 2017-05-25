package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 添加银行卡绑定信息
 * @author Administrator
 *
 */
public class BankCodeBindingAddInfo extends PayModel {
	
	/**
	 * 用户在易极付 的 id   不要 和本地的   id 搞混了。
	 */
	private String userId; 
	/**
	 * 银行卡号    
	 */
	private String bankCardNo; 
	private String name;
	private String bankType;
	private String[] paramNames = new String[]{"service","partnerId",
	         "signType","sign","orderNo","bankCardNo","userId","name","bankType"};
	//封装类型
	private String message;
	private String resultCode;
	private String success;
	
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
	public String getUserId() {
		return userId;
	}
	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	
}
