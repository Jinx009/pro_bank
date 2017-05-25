package com.rongdu.p2psys.account.model.payment.unspay;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.model.payment.BasePaymentWay;
import com.rongdu.p2psys.account.model.payment.HttpRequestUtils;

public class UnspayPaymentWay extends BasePaymentWay{
    
    @Override
    public BasePayment payment(HttpServletRequest request) throws Exception {
        Unspay pay = new Unspay();
        String bankCode = request.getParameter("bankCode");
        pay.init(bankCode,getRecharge());
        request.setAttribute("unspay", pay);
        return pay;
    }

    @Override
    public BasePayment callback(HttpServletRequest request) throws Exception {
        Unspay pay = (Unspay) HttpRequestUtils.paramModel(Unspay.class, request);
        String bankCode = request.getParameter("bankCode");
        pay.init(bankCode, getRecharge());
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
