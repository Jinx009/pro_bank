package com.rongdu.p2psys.tpp.yjf.model;

/**
 * 第三方托管，关于取现本地业务逻辑处理所需要使用的参数封装
 * @author Administrator
 */
public class CashModel {
	private String orderId;  //取现订单号
	private String orderAmount; //取现金额
	private long userId; //取现用户Id
	private String cardNo;  //取现银行卡号
	private boolean result;  //第三方处理结果
	private double feeAmt;          // 取现手续费
	private long cashId;   //取现Id
	private double servFee; //商户收取的服务费
	/**
	 * 手续费出资方，汇付：U为用户，M为平台
	 */
	private String payModel; 
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public double getFeeAmt() {
		return feeAmt;
	}
	public void setFeeAmt(double feeAmt) {
		this.feeAmt = feeAmt;
	}
	public long getCashId() {
		return cashId;
	}
	public void setCashId(long cashId) {
		this.cashId = cashId;
	}
	public double getServFee() {
		return servFee;
	}
	public void setServFee(double servFee) {
		this.servFee = servFee;
	}
	public String getPayModel() {
		return payModel;
	}
	public void setPayModel(String payModel) {
		this.payModel = payModel;
	}
}
