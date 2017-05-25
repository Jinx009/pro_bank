package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 转账
 * @author Administrator
 *
 */
public class TradeTransfer extends PayModel {

	private String sellerUserId;
	private String payerUserId;
	private String buyerUserId; //和  payerUserId一样代表付款人
	private String tradeName;
	private String tradeBizProductCode;
	private String product;
	private String tradeType;
	private String gatheringType;
	private String tradeAmount;
	private String currency;
	private String goods;
	
	private String[] paramNames = new String[]{"service","partnerId",
	         "signType","sign","orderNo","sellerUserId","payerUserId","buyerUserId","tradeName","tradeBizProductCode",
	         "product","tradeType","gatheringType","tradeAmount","currency","goods"};
	
	//封装返回信息
	private String resultCode;
	private String resultMessage;
	public String getSellerUserId() {
		return sellerUserId;
	}
	public void setSellerUserId(String sellerUserId) {
		this.sellerUserId = sellerUserId;
	}
	public String getPayerUserId() {
		return payerUserId;
	}
	public void setPayerUserId(String payerUserId) {
		this.payerUserId = payerUserId;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
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
	public String getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
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
	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	public String getBuyerUserId() {
		return buyerUserId;
	}
	public void setBuyerUserId(String buyerUserId) {
		this.buyerUserId = buyerUserId;
	}

	
}