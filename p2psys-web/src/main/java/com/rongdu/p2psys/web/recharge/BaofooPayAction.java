package com.rongdu.p2psys.web.recharge;

import org.apache.struts2.convention.annotation.Action;

import com.rongdu.p2psys.account.model.payment.PaymentWay;
import com.rongdu.p2psys.account.model.payment.baofoo.BaofooPaymentWay;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 宝付充值回调
 * @author Administrator
 *
 */
@SuppressWarnings("rawtypes")
public class BaofooPayAction extends BaseAction{
	
	// 宝付4.0 同步通知 （商户页面通知）
	@Action("/public/newBaoFoopay_return")
	public String baoFooPayReturn() throws Exception {
		PaymentWay way = new BaofooPaymentWay();
		way.callbackRequest(request);
		return null;
	}

	// 宝付4.0 异步通知 （后台通知
	@Action("/public/newBaoFoopay_notify")
	public String baoFooPayNotify() throws Exception {
		PaymentWay way = new BaofooPaymentWay();
		way.callbackRequest(request);
		return null;
	}
}
