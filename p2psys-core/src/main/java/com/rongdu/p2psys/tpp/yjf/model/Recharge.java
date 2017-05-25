package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 线上充值 包含信息
 * @author Administrator
 *
 */
public class Recharge extends PayModel {
	
	private String  notifyUrl;
	private String  returnUrl;
	private String  userId;
	private String  tradeBizProductCode;
	private String  depositAmount;
	private String  tradeMerchantId;
	
	
	private String[] paramNames = new String[]{"service","partnerId",
			         "signType","sign","notifyUrl","returnUrl","orderNo","userId",
			         "tradeBizProductCode","depositAmount","tradeMerchantId"};
	//下面字段 保存返回的信息。
	private String notifyTime;
	private String orderNo;	//外部订单号
	private String notifyType;	//通知类型
	private String depositId;	//充值流水号
	private String isSuccess;	//成功与否
	
	public Recharge(){
		super();
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
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
	public String getNotifyTime() {
		return notifyTime;
	}
	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}
	public String getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(String depositAmount) {
		this.depositAmount = depositAmount;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}
	public String getDepositId() {
		return depositId;
	}
	public void setDepositId(String depositId) {
		this.depositId = depositId;
	}
	public String getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getTradeBizProductCode() {
		return tradeBizProductCode;
	}
	public void setTradeBizProductCode(String tradeBizProductCode) {
		this.tradeBizProductCode = tradeBizProductCode;
	}
	public String getTradeMerchantId() {
		return tradeMerchantId;
	}
	public void setTradeMerchantId(String tradeMerchantId) {
		this.tradeMerchantId = tradeMerchantId;
	}
	

}
