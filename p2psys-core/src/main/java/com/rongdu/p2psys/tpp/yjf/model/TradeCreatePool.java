package com.rongdu.p2psys.tpp.yjf.model;


public class TradeCreatePool extends PayModel {
	
	private String sellerUserId;
	private String tradeName;
	private String tradeBizProductCode;
	private String product;
	private String tradeAmount;
	private String tradeType;
	private String gatheringType;
	private String currency;
	private String goods;
	
	private String[] paramNames = new String[]{"service","partnerId",
	         "signType","sign","orderNo","sellerUserId","tradeName",
	         "tradeBizProductCode","product","tradeAmount","tradeType",
	         "gatheringType","currency","goods"};
}
