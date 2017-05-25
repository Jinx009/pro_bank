package com.rongdu.p2psys.core.sms;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.enums.EnumRuleNid;
import com.rongdu.p2psys.core.domain.Rule;
import com.rongdu.p2psys.core.rule.RuleModel;

/**
 * 短信通道-互亿无线短信接口
 * 
 * @author wcw
 */
public class YwdSmsPortalImpl implements SmsPortal {
	private static String Url = "http://106.ihuyi.com/webservice/sms.php?method=Submit";

	@Override
	public String send(String phone, String content) {
		String msg = "";
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);

		// client.getParams().setContentCharset("GBK");
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
		String sendsms_account = ""; // 发送短信账户
		String sendsms_password = ""; // 发送短信密码
		Rule rule = Global.getRule(EnumRuleNid.YWD_SMS.getValue());
		if (rule != null) {
			RuleModel ruleModel = new RuleModel(rule);
			sendsms_account = ruleModel.getValueStrByKey("sendsms_account");
			sendsms_password = ruleModel.getValueStrByKey("sendsms_password");
		}
		NameValuePair[] data = {// 提交短信
		new NameValuePair("account", sendsms_account), new NameValuePair("password", sendsms_password), // 密码可以使用明文密码或使用32位MD5加密
				new NameValuePair("mobile", phone), new NameValuePair("content", content), };

		method.setRequestBody(data);

		try {
			client.executeMethod(method);

			String SubmitResult = method.getResponseBodyAsString();

			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();

			String code = root.elementText("code");
			String message = root.elementText("msg");

			if (!code.equals("2")) {
				msg = message;
			} else {
				msg = "ok";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public Map<String, Integer> getUseInfo() {
		return null;
	}

	@Override
	public String getSPName() {
		return "Ywd";
	}

}
