package com.rongdu.p2psys.tpp.yjf.model;



/**
 * 线上充值 包含信息
 * @author Administrator
 *
 */
public class VerifyFacade extends PayModel {
	
	private String extendId;
	private String bankCode;
	private String channelApi;
	private String accountName; //户名
	private String accountNo; //卡号
	private String cardType; //卡类型   "D"
	private String certType;//证件类型 "ID"
	private String certNo; //证件号码
	
	private String[] paramNames = new String[]{"service","partnerId",
			         "signType","sign","orderNo","extendId","bankCode","channelApi","accountName","accountNo","cardType","certType",
			         "certNo"};
	
	//返回参数封装
	private String resultCode;
	private String verifyStatus; //VS--成功  VF--失败
	private String resultMessage;
	
	public VerifyFacade(){
		super();
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String getExtendId() {
		return extendId;
	}

	public void setExtendId(String extendId) {
		this.extendId = extendId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getChannelApi() {
		return channelApi;
	}

	public void setChannelApi(String channelApi) {
		this.channelApi = channelApi;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
}
