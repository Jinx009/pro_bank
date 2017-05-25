package com.rongdu.p2psys.web.recharge;

import org.apache.struts2.convention.annotation.Action;

import com.rongdu.p2psys.account.model.payment.PaymentWay;
import com.rongdu.p2psys.account.model.payment.xspay.XspayPaymentWay;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 国付宝（新平台）回调
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月19日
 */
@SuppressWarnings("rawtypes")
public class XspayAction extends BaseAction {

	@Action("/public/xspay")
	public String xspay() throws Exception {
		PaymentWay way = new XspayPaymentWay();
		way.callbackRequest(request);
		return null;
	}

}
