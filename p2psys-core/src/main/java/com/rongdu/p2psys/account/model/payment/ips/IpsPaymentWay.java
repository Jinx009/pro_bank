package com.rongdu.p2psys.account.model.payment.ips;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.model.payment.BasePaymentWay;
import com.rongdu.p2psys.account.model.payment.HttpRequestUtils;

public class IpsPaymentWay extends BasePaymentWay {
	
	@Override
	public BasePayment payment(HttpServletRequest request) throws Exception {
		IpsPay ipsPay = new IpsPay();
		ipsPay.init(getRecharge());
		request.setAttribute("ips", ipsPay);
		return ipsPay;
	}

	@Override
	public BasePayment callback(HttpServletRequest request) throws Exception {
		IpsPay ipsPay = (IpsPay) HttpRequestUtils.paramModel(IpsPay.class, request);
		ipsPay.doCallBack(request);
		return ipsPay;
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
