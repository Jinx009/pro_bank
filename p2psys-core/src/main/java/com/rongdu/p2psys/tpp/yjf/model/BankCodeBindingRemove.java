package com.rongdu.p2psys.tpp.yjf.model;


public class BankCodeBindingRemove extends PayModel {

	private String  userId;
	private String  bankCardNo;
	
	private String[] paramNames = new String[]{"service","partnerId",
			         "signType","sign","orderNo","userId", "bankCardNo"};
	//{"channelId":"000057","orderNo":"2013070400000029","resultCode":"EXECUTE_SUCCESS,执行成功","sign":"4895038bdca6aa4739e1f88516f3bf01","signType":"MD5","success":"T"}
	private String resultCode;
	public String getUserId() {
		return userId;
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
	
}
