package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 集体集资创建交易接口
 * 集体集资交易创建接口tradeCreatePoolTogether
 * @author zxc
 *
 */
public class TradeCreatePoolTogether extends PayModel {

	private String sellerUserId;
	private String tradeName;
	private String tradeBizProductCode;
	private String product;
	private String tradeAmount;
	private String tradeType;
	private String gatheringType;
	private String currency;
	private String goods; // [{'name':'jjj1','price':'1000','quantity':'10'}]
	
	private String[] paramNames = new String[]{"service","partnerId",
	         "signType","sign","orderNo","sellerUserId","tradeName",
	         "tradeBizProductCode","product","tradeAmount","tradeType",
	         "gatheringType","currency","goods"};
	//返回
	private String channelId;
	private String resultCode;
	private String resultMessage;
	private String success;
	private String tradeNo;
	
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getSellerUserId() {
		return sellerUserId;
	}
	public void setSellerUserId(String sellerUserId) {
		this.sellerUserId = sellerUserId;
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
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
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
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	
}
