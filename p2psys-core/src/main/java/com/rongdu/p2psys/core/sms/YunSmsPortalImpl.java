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
import com.rongdu.p2psys.core.rule.RuleModel;

public class YunSmsPortalImpl implements SmsPortal {
	private Logger logger = Logger.getLogger(YunSmsPortalImpl.class);

	@Override
	public String send(String phone, String content) {
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.warn("短信内容转换失败。");
		}

		if (null == Global.getRule(EnumRuleNid.YUN_SMS.getValue())) {
			return "没有通道信息，无法发送。";
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.YUN_SMS.getValue()));

		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsUrl = rule.getValueStrByKey("notice_sms");

		StringBuilder url = new StringBuilder();
		url.append(smsUrl);
		url.append("?uid=" + username);
		url.append("&pwd=" + password);
		url.append("&mobile=" + phone);
		url.append("&content=" + content);
		url.append("&encode=utf8");
		logger.debug("url:" + url);
		String s = HttpUtil.getHttpResponse(url.toString());
		int ret = StringUtil.toInt(StringUtil.isNull(s));

		return getResult(ret);
	}

	@Override
	public Map<String, Integer> getUseInfo() {
		if (null == Global.getRule(EnumRuleNid.YUN_SMS.getValue())) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("usenum", 0);
			map.put("usednum", 0);

			return map;
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.YUN_SMS.getValue()));

		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsuseUrl = rule.getValueStrByKey("use_sms_url");

		String useurl = smsuseUrl + "&uid=" + username + "&pwd=" + password;
		String usedurl = smsuseUrl + "&uid=" + username + "&pwd=" + password + "&cmd=send";

		logger.debug("url:" + useurl);
		logger.debug("url:" + usedurl);
		String usereturn = HttpUtil.getHttpResponse(useurl);
		String[] uses = usereturn.split("\\|\\|");
		String usedreturn = HttpUtil.getHttpResponse(usedurl);
		String[] useds = usedreturn.split("\\|\\|");

		int usenum = StringUtil.toInt(StringUtil.isNull(uses[1]));
		int usednum = StringUtil.toInt(StringUtil.isNull(useds[1]));

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("usenum", usenum);
		map.put("usednum", usednum);

		return map;

	}

	public String getResult(int ret) {
		String result = "";
		if (ret == 100) {
			result = "ok";
		}
		if (ret > 100) {
			switch (ret) {
				case 101:
					result = "验证失败";
					break;
				case 102:
					result = "短信不足";
					break;
				case 103:
					result = "操作失败";
					break;
				case 104:
					result = "非法字符";
					break;
				case 105:
					result = "内容过多";
					break;
				case 106:
					result = "号码过多";
					break;
				case 107:
					result = "频率过快";
					break;
				case 108:
					result = "号码内容空";
					break;
				case 109:
					result = "账号冻结";
					break;
				case 110:
					result = "禁止频繁单条发送";
					break;
				case 111:
					result = "系统暂停发送";
					break;
				case 112:
					result = "号码错误";
					break;
				case 113:
					result = "定时时间格式不对";
					break;
				case 114:
					result = "账号被锁，10分钟后登录";
					break;
				case 115:
					result = "连接失败";
					break;
				case 116:
					result = "禁止接口发送";
					break;
				case 117:
					result = "绑定IP不正确";
					break;
				case 120:
					result = "系统升级";
					break;
				default:
					result = "其他异常错误";
					break;
			}
		}
		return result;
	}

	@Override
	public String getSPName() {
		return "Yun";
	}

}
