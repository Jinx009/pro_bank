package com.rongdu.p2psys.account.model;

import com.rongdu.p2psys.account.domain.Pay;

/**
 * 支付方式Model
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月10日
 */
public class PayModel extends Pay {

	private String gatewayUrl;

	private String orderInquireUrl;

	private String certPosition;

	/** 终端号（宝付4.0） */
	private String terminalID;

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public String getOrderInquireUrl() {
		return orderInquireUrl;
	}

	public void setOrderInquireUrl(String orderInquireUrl) {
		this.orderInquireUrl = orderInquireUrl;
	}

	public String getCertPosition() {
		return certPosition;
	}

	public void setCertPosition(String certPosition) {
		this.certPosition = certPosition;
	}

	public String getTerminalID() {
		return terminalID;
	}

	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}

}
