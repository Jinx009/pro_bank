package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 集资还款创建交易接口
 * @author Administrator
 *
 */
public class TradeCreatePoolReverse extends PayModel {

	private String tradeName;
	private String payerUserId;
	private String tradeBizProductCode;
	private String product;
	private String tradeAmount;
	private String tradeType;
	private String gatheringType;
	private String currency;
	private String goods;
	private String[] paramNames = new String[]{"service","partnerId",
	         "signType","sign","orderNo","tradeName",
	         "payerUserId","tradeBizProductCode","product","tradeAmount",
	         "tradeType","gatheringType","currency","goods"};
	
	//封装返回信息
		 
	private String resultCode;
	private String resultMessage;
	private String tradeNo;
	

	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getPayerUserId() {
		return payerUserId;
	}
	public void setPayerUserId(String payerUserId) {
		this.payerUserId = payerUserId;
	}
	public String getTradeBizProductCode() {
		return tradeBizProductCode;
	}
	public void setTradeBizProductCode(String tradeBizProductCode) {
		this.tradeBizProductCode = tradeBizProductCode;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getGatheringType() {
		return gatheringType;
	}
	public void setGatheringType(String gatheringType) {
		this.gatheringType = gatheringType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
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
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	
	
}
