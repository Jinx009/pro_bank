package com.rongdu.p2psys.cf.project;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatUtil;

@SuppressWarnings("rawtypes")
public class WechatPayAction extends BaseAction implements ModelDriven {

	@Action(value="/wechat/getPayId")
	public void getPrePay_Id() throws ClientProtocolException, IOException,
			NoSuchAlgorithmException {

		String openId = request.getParameter("openId");
		String orderId = request.getParameter("order_id");
		String ip = request.getParameter("client_ip");
		String total_fee = request.getParameter("fee");
		String sign = request.getParameter("sign");
		String nonce_str = request.getParameter("nonce_str");

		System.out.println("pay  openId:" + openId);

		String xml = "<xml>" + "<appid>"
				+ WechatData.A_APP_ID
				+ "</appid>"
				+ "<body>800众服-众筹，让天下没有沉睡的资源！</body>"
				+ "<mch_id>1304560401</mch_id>"
				+ "<nonce_str>"
				+ nonce_str
				+ "</nonce_str>"
				+ "<notify_url>http://t03.0angel.com/pay/callBack.html</notify_url>"
				+ "<openid>" + openId + "</openid>" + "<out_trade_no>"
				+ orderId + "</out_trade_no>" + "<spbill_create_ip>" + ip
				+ "</spbill_create_ip>" + "<total_fee>" + total_fee
				+ "</total_fee>" + "<trade_type>JSAPI</trade_type>" + "<sign>"
				+ sign.toUpperCase() + "</sign>" + "</xml> ";

		System.out.println(xml);

		String res = WechatUtil.getPrePayId(xml);
		Map<String, String> map = WechatUtil.parseXml(res);
		System.out.println("res=" + res);
		Map<String, String> data = new HashMap<String, String>();
		data.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG, map.get("prepay_id"));

		printWebJson(data);
	}

}
