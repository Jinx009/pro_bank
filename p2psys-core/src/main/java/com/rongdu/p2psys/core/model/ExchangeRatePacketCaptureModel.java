package com.rongdu.p2psys.core.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.core.domain.ExchangeRatePacketCapture;

public class ExchangeRatePacketCaptureModel extends ExchangeRatePacketCapture {

	private static final long serialVersionUID = 1748249477361147101L;

	private String borrowName;

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}
	
	public static ExchangeRatePacketCaptureModel instance(ExchangeRatePacketCapture exchangeRatePacketCapture) {
		ExchangeRatePacketCaptureModel model = new ExchangeRatePacketCaptureModel();
		BeanUtils.copyProperties(exchangeRatePacketCapture, model);
		return model;
	}
	
}
