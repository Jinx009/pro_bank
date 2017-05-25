package com.rongdu.p2psys.account.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.account.domain.PayOnlinebank;

/**
 * 支付方式-网银直联(线上银行)
 * @author cx
 * 2014-10-27
 */
@SuppressWarnings("serial")
public class PayOnlinebankModel extends PayOnlinebank {

	/** 支付方式名称 */
	private String payName;
	
	public static PayOnlinebankModel instance(PayOnlinebank payOnlinebank) {
		PayOnlinebankModel model = new PayOnlinebankModel();
		BeanUtils.copyProperties(payOnlinebank, model);
		return model;
	}
	
	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

}
