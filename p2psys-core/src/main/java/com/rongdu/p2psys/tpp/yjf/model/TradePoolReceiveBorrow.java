package com.rongdu.p2psys.tpp.yjf.model;


public class TradePoolReceiveBorrow extends PayModel {

	private String tradeNo;
	private String tradePoolSubTansferOrders;
	private String[] paramNames = new String[]{"service","partnerId",
	        "signType","sign","orderNo","tradeNo","tradePoolSubTansferOrders"};
	//封装返回的信息
	private String info;
	private String resultCode;
	private String resultMessage;
	
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
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
	public String getTradePoolSubTansferOrders() {
		return tradePoolSubTansferOrders;
	}
	public void setTradePoolSubTansferOrders(String tradePoolSubTansferOrders) {
		this.tradePoolSubTansferOrders = tradePoolSubTansferOrders;
	}
	
}
