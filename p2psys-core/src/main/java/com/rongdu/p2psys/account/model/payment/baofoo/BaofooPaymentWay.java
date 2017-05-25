package com.rongdu.p2psys.account.model.payment.baofoo;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.model.payment.BasePaymentWay;
import com.rongdu.p2psys.account.model.payment.HttpRequestUtils;

/**
 * 宝付充值直联
 */
public class BaofooPaymentWay extends BasePaymentWay{

    @Override
    public BasePayment payment(HttpServletRequest request) throws Exception {
        BaoFooPay baoFooPay = new BaoFooPay();
        baoFooPay.init(getRecharge(), "");
        request.setAttribute("baofooPay", payment);
        return payment;
    }

    @Override
    public BasePayment callback(HttpServletRequest request) throws Exception {
        BaoFooPay baoFooPay = (BaoFooPay) HttpRequestUtils.paramModel(BaoFooPay.class, request);
        baoFooPay.doCallBack(request);
        return baoFooPay;
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
