package com.rongdu.p2psys.account.model.payment.gopay;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.model.payment.BasePaymentWay;
import com.rongdu.p2psys.account.model.payment.HttpRequestUtils;

public class GopayPaymentWay extends BasePaymentWay{
    
    @Override
    public BasePayment payment(HttpServletRequest request) throws Exception {
        Gopay gopay = new Gopay();
        String bankCode = request.getParameter("bankCode");
        gopay.init("",bankCode, getRecharge());
        request.setAttribute("gopay", gopay);
        return gopay;
    }

    @Override
    public BasePayment callback(HttpServletRequest request) throws Exception {
        Gopay gopay = (Gopay) HttpRequestUtils.paramModel(Gopay.class, request);
        gopay.doCallBack(request);
        return gopay;
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
