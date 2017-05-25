package com.rongdu.p2psys.core.sms;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.rongdu.common.util.HttpUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.enums.EnumRuleNid;
import com.rongdu.p2psys.core.rule.RuleModel;

public class YndSmsPortalImpl implements SmsPortal {
	private Logger logger = Logger.getLogger(ErongduSmsPortalImpl.class);

	@Override
	public String send(String phone, String content) {
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (null == Global.getRule(EnumRuleNid.YND_SMS.getValue())) {
			return "没有通道信息，无法发送。";
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.YND_SMS.getValue()));

		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsUrl = rule.getValueStrByKey("notice_sms");

		String url = smsUrl + "&account=" + username + "&password=" + password + "&mobile=" + phone + "&content="
				+ content + "&sendTime=&extno=";
		logger.debug("url:" + url);
		String s = HttpUtil.getHttpResponse(url);
		logger.debug("return:" + s);
		String result = "";
		SAXBuilder builder = new SAXBuilder();
		try {

			StringReader read = new StringReader(s);

			InputSource source = new InputSource(read);
			Document doc = builder.build(source);

			Element rootEl = doc.getRootElement();
			String returnstatus = rootEl.getChildText("returnstatus");
			String message = rootEl.getChildText("message");
			String remainpoint = rootEl.getChildText("remainpoint");
			String taskID = rootEl.getChildText("taskID");
			String successCounts = rootEl.getChildText("successCounts");

			if (returnstatus.equalsIgnoreCase("Success") && message.equalsIgnoreCase("ok")) {
				result = "ok";
			} else {
				result = message;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "未知错误";
		}
		return result;
	}

	@Override
	public Map<String, Integer> getUseInfo() {

		if (null == Global.getRule(EnumRuleNid.YND_SMS.getValue())) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("usenum", 0);
			map.put("usednum", 0);

			return map;
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.YND_SMS.getValue()));

		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsuseUrl = rule.getValueStrByKey("use_sms_url");

		String useurl = smsuseUrl + "&account=" + username + "&password=" + password;

		logger.debug("url:" + useurl);
		String s = HttpUtil.getHttpResponse(useurl);
		logger.debug("return:" + s);

		int usenum = 0;
		int usednum = 0;

		String result = "ok";
		SAXBuilder builder = new SAXBuilder();
		try {

			StringReader read = new StringReader(s);

			InputSource source = new InputSource(read);
			Document doc = builder.build(source);

			Element rootEl = doc.getRootElement();
			String returnstatus = rootEl.getChildText("returnstatus");
			String message = rootEl.getChildText("message");
			String payinfo = rootEl.getChildText("payinfo");
			String overage = rootEl.getChildText("overage");
			String sendTotal = rootEl.getChildText("sendTotal");

			if (returnstatus.equalsIgnoreCase("Sucess") && message.equalsIgnoreCase("")) {
				usenum = Integer.valueOf(overage);
			} else {
				result = message;
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("usenum", usenum);
		map.put("usednum", usednum);

		return map;
	}

	@Override
	public String getSPName() {
		return "Ynd";
	}

}
