package com.rongdu.p2psys.tpp.yjf.model;

/**
 * 第三方托管充值后，本地处理业务逻辑所需要使用到的参数封装bean
 * @author Administrator
 *
 */
public class RechargeModel {
	private String orderId;   //充值订单号
	private String orderAmount;    //充值金额
	private String amountIn;    //实际到账金额
	private String gateBusiId; //充值方式，用于区别是否是B2B还是B2C
	private String gateBankId; //充值选择的银行
	private long userId;      //充值用户id
	private String result;    //第三方处理结果
	private double feeAmt;       // 充值手续费
	private String resultMsg;   //充值失败的时候返回信息
	private String serialNo;   //充值流水号
	/**
	 * 托管账号
	 */
	private String apiId;
	/**
	 * 身份证号
	 */
	private String cardId;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 银行卡号
	 */
	private String bankCode;
	/**
	 * 充值渠道
	 */
	private String channelType;
	
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
	public String getAmountIn() {
		return amountIn;
	}
	public void setAmountIn(String amountIn) {
		this.amountIn = amountIn;
	}
	public String getGateBusiId() {
		return gateBusiId;
	}
	public void setGateBusiId(String gateBusiId) {
		this.gateBusiId = gateBusiId;
	}
	public String getGateBankId() {
		return gateBankId;
	}
	public void setGateBankId(String gateBankId) {
		this.gateBankId = gateBankId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public double getFeeAmt() {
		return feeAmt;
	}
	public void setFeeAmt(double feeAmt) {
		this.feeAmt = feeAmt;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getApiId() {
		return apiId;
	}
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
    public String getChannelType() {
        return channelType;
    }
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
	
}
