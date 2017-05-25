package com.rongdu.p2psys.account.model.payment.xspay;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.model.payment.BasePaymentWay;
import com.rongdu.p2psys.account.model.payment.HttpRequestUtils;

public class XspayPaymentWay extends BasePaymentWay {

	@Override
	public BasePayment payment(HttpServletRequest request) throws Exception {
		Xspay payment = new Xspay();
		payment.init(getRecharge());
		return payment;
	}

	@Override
	public BasePayment callback(HttpServletRequest request) throws Exception {
		Xspay pay = (Xspay) HttpRequestUtils.paramModel(Xspay.class, request);
		pay.init(getRecharge());
		return pay;
	}

	@Override
	public String responseSuccess() throws Exception {
		// TODO Auto-generated method stub
		return super.responseSuccess();
	}

	@Override
	public String responseFail() throws Exception {
		// TODO Auto-generated method stub
		return super.responseFail();
	}

}
