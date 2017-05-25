package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 集资还款交易完成
 * @author Administrator
 *
 */
public class TradeFinishPoolReverse extends PayModel {

	private String tradeNo;
	private String[] paramNames = new String[]{"service","partnerId",
	         "signType","sign","orderNo","tradeNo"};
	
	//封装返回信息
		 
	private String resultCode;
	private String resultMessage;
	
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
	
	
}
