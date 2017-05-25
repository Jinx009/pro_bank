package com.rongdu.p2psys.web.recharge;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.account.model.payment.PaymentWay;
import com.rongdu.p2psys.account.model.payment.gopay.GopayPaymentWay;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 国付宝（新平台）回调
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月19日
 */
@SuppressWarnings("rawtypes")
public class GopayAction extends BaseAction {

	@Action(value="/public/gopay", results = { @Result(name = "result", type = "ftl", location = "/result.html")})
	public String newgopay() throws Exception {
		PaymentWay way = new GopayPaymentWay();
		way.callbackRequest(request);
		if(way.isSuccess()){
			request.setAttribute("msg", MessageUtil.getMessage("I20003")); 
		}else{
			request.setAttribute("msg", MessageUtil.getMessage("I20005")); 
		}
		return "result";
	}

}
