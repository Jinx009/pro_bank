package com.rongdu.p2psys.core.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.rongdu.common.util.HttpUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.enums.EnumRuleNid;
import com.rongdu.p2psys.core.dao.SystemConfigDao;
import com.rongdu.p2psys.core.rule.RuleModel;
import com.rongdu.p2psys.core.util.BeanUtil;

public class ErongduSmsPortalImpl implements SmsPortal {
	private Logger logger = Logger.getLogger(ErongduSmsPortalImpl.class);

	@Override
	public String send(String phone, String content) {
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		/*if (null == Global.getRule(EnumRuleNid.ERONGDU_SMS.getValue())) {
			return "没有通道信息，无法发送。";
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.ERONGDU_SMS.getValue()));

		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsUrl = rule.getValueStrByKey("notice_sms");*/
		SystemConfigDao systemConfigDao = (SystemConfigDao) BeanUtil.getBean("systemConfigDao");
		String username = systemConfigDao.findObjByProperty("nid", "con_sms_username").getValue();
		String password = systemConfigDao.findObjByProperty("nid", "con_sms_password").getValue();
		String smsUrl = systemConfigDao.findObjByProperty("nid", "con_notice_sms").getValue();
		String url = smsUrl + "?username=" + username + "&password=" + password + "&mobile=" + phone + "&content="
				+ content;
		logger.debug("url:" + url);
		String s = HttpUtil.getHttpResponse(url);
		String num = s.split(",")[0];
		int ret = Integer.parseInt(num);
		String result = "ok";

		if (ret <= 0) {
			switch (ret) {
				case -1:
					result = "短信条数已用完";
					break;
				case -2:
					result = "登陆失败";
					break;
				default:
					result = "其他异常错误";
					break;
			}
		}
		return result;
	}

	@Override
	public Map<String, Integer> getUseInfo() {

		if (null == Global.getRule(EnumRuleNid.ERONGDU_SMS.getValue())) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("usenum", 0);
			map.put("usednum", 0);

			return map;
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.ERONGDU_SMS.getValue()));

		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsuseUrl = rule.getValueStrByKey("use_sms_url");
		String smsusedUrl = rule.getValueStrByKey("used_sms_url");

		String useurl = smsuseUrl + "&username=" + username + "&password=" + password;
		String usedurl = smsusedUrl + "&username=" + username + "&password=" + password;

		logger.debug("url:" + useurl);
		logger.debug("url:" + usedurl);
		String usereturn = HttpUtil.getHttpResponse(useurl);
		String usedreturn = HttpUtil.getHttpResponse(usedurl);

		int usenum = StringUtil.toInt(StringUtil.isNull(usereturn));
		int usednum = StringUtil.toInt(StringUtil.isNull(usedreturn));

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("usenum", usenum);
		map.put("usednum", usednum);

		return map;

	}

	@Override
	public String getSPName() {
		return "Erongdu";
	}

}
