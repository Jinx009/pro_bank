package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 交易借出人申请退出集资
 * @author Administrator
 *
 */
public class TradePayerQuitPoolTogether extends PayModel {
	private String tradeNo;  //订单号
	private String subTradeNo; //子订单号
	private String payerUserId;
	private String tradeMemo;
	private String[] paramNames = new String[]{"service","partnerId",
        "signType","sign","orderNo","tradeNo","subTradeNo","payerUserId","tradeMemo"};
	
	//封装返回信息
	private String resultCode;
	private String resultMessage;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public String getPayerUserId() {
		return payerUserId;
	}
	public void setPayerUserId(String payerUserId) {
		this.payerUserId = payerUserId;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getSubTradeNo() {
		return subTradeNo;
	}
	public void setSubTradeNo(String subTradeNo) {
		this.subTradeNo = subTradeNo;
	}
	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public String getTradeMemo() {
		return tradeMemo;
	}
	public void setTradeMemo(String tradeMemo) {
		this.tradeMemo = tradeMemo;
	}
	
}
