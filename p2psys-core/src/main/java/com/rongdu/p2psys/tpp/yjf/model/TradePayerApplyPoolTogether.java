package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 业务参数-申请冻结资金
 * @author zxc
 *
 */
public class TradePayerApplyPoolTogether extends PayModel {

	private String payerUserId; //借出人
	private String tradeAmount; //借出金额
	private String tradeNo; 
	private String tradeName; 
	
	private String[] paramNames = new String[]{"service","partnerId",
	        "signType","sign","orderNo","payerUserId","tradeAmount",
	        "tradeNo","tradeName"};
	//封装返回的信息
     private String resultCode;
     private String subTradeNo;
     private String resultMessage;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getSubTradeNo() {
		return subTradeNo;
	}
	public void setSubTradeNo(String subTradeNo) {
		this.subTradeNo = subTradeNo;
	}
	public String getPayerUserId() {
		return payerUserId;
	}
	public void setPayerUserId(String payerUserId) {
		this.payerUserId = payerUserId;
	}
	public String getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getTradeMemo() {
		return tradeMemo;
	}
	public void setTradeMemo(String tradeMemo) {
		this.tradeMemo = tradeMemo;
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
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	
}
