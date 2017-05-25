package com.rongdu.p2psys.web.recharge;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.account.model.payment.PaymentWay;
import com.rongdu.p2psys.account.model.payment.ips.IpsPaymentWay;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 环讯接口回调
 * @author cx
 * @version 2.0
 * 2014-10-28
 */
@SuppressWarnings("rawtypes")
public class IpsPayAction extends BaseAction {

	@Action(value="/public/ipspay", results = { @Result(name = "result", type = "ftl", location = "/result.html")})
	public String ipspay() throws Exception {
		PaymentWay way = new IpsPaymentWay();
		way.callbackRequest(request);
		if(way.isSuccess()){
			request.setAttribute("msg", MessageUtil.getMessage("I20003")); 
		}else{
			request.setAttribute("msg", MessageUtil.getMessage("I20005")); 
		}
		return "result";
	}

}
