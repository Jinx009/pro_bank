package com.rongdu.p2psys.core.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.rongdu.common.util.HttpUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.enums.EnumRuleNid;
import com.rongdu.p2psys.core.rule.RuleModel;

/**
 * 中国短信网接口（登录地址：http://web.c123.cn）
 * 
 * @author lhm
 * @version 1.0
 * @since 2013-11-20
 */
public class ChinaSmsPortalImpl implements SmsPortal {

	@Override
	public String send(String phone, String content) {

		// 短信内容
		String strcontent = "";
		try {
			strcontent = URLEncoder.encode(content, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (null == Global.getRule(EnumRuleNid.CHINA_SMS.getValue())) {
			return "没有通道信息，无法发送。";
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.CHINA_SMS.getValue()));

		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsUrl = rule.getValueStrByKey("notice_sms");

		// 用户账号
		String uid = StringUtil.isNull(username);
		// 用户密码
		String pwd = MD5.getMD5ofStr(StringUtil.isNull(password)).toLowerCase();
		// url
		smsUrl = StringUtil.isNull(smsUrl);
		StringBuffer url = new StringBuffer();
		url.append(smsUrl).append("?uid=").append(uid).append("&pwd=").append(pwd).append("&mobile=").append(phone)
				.append("&content=").append(strcontent);
		String resultCd = HttpUtil.getHttpResponse(url.toString());

		return getResultName(StringUtil.toInt(resultCd));
	}

	@Override
	public Map<String, Integer> getUseInfo() {
		if (null == Global.getRule(EnumRuleNid.CHINA_SMS.getValue())) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("usenum", 0);
			map.put("usednum", 0);

			return map;
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.CHINA_SMS.getValue()));

		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsUrl = rule.getValueStrByKey("use_sms_url");

		// 剩余短信条数
		int remainNum = 0;
		// 已使用短信条数
		int usedNum = 0;
		// 用户账号
		String uid = StringUtil.isNull(username);
		// 用户密码
		String pwd = MD5.getMD5ofStr(StringUtil.isNull(password)).toLowerCase();
		// url
		smsUrl = StringUtil.isNull(smsUrl);

		StringBuffer remainUrl = new StringBuffer();
		// 取剩余短信条数
		remainUrl.append(smsUrl).append("?uid=").append(uid).append("&pwd=").append(pwd);
		String resultStrRemain = HttpUtil.getHttpResponse(remainUrl.toString());
		if (resultStrRemain.startsWith("100")) {
			remainNum = StringUtil.toInt(resultStrRemain.substring(5));
		}

		// 查看已使用短信条数有问题,暂时不能查看
		// // 取已使用短信条数
		// StringBuffer usedurl = new StringBuffer();
		// usedurl.append(smsUrl).append("?uid=").append(uid).append("&pwd=").append(pwd).append("&cmd=send");
		// String resultStrUsed =
		// HttpUtil.getHttpResponse(remainUrl.toString());
		// if (resultStrUsed.startsWith("100")) {
		// usedNum = StringUtil.toInt(resultStrUsed.substring(5));
		// }

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("usenum", remainNum);
		map.put("usednum", usedNum);

		return map;
	}

	/**
	 * 取得状态码说明
	 * 
	 * @param cd 状态码
	 * @return 状态码说明
	 */
	private String getResultName(int cd) {
		String resultName = "";
		switch (cd) {
			case 100:
				resultName = "ok";
				break;
			case 101:
				resultName = "验证失败";
				break;
			case 102:
				resultName = "短信不足";
				break;
			case 103:
				resultName = "操作失败";
				break;
			case 104:
				resultName = "非法字符";
				break;
			case 105:
				resultName = "内容过多";
				break;
			case 106:
				resultName = "号码过多";
				break;
			case 107:
				resultName = "频率过快";
				break;
			case 108:
				resultName = "号码内容空";
				break;
			case 109:
				resultName = "账号冻结";
				break;
			case 110:
				resultName = "禁止频繁单条发送";
				break;
			case 111:
				resultName = "系统暂定发送";
				break;
			case 112:
				resultName = "号码错误";
				break;
			case 113:
				resultName = "定时时间格式不对";
				break;
			case 114:
				resultName = "账号被锁，10分钟后登录";
				break;
			case 115:
				resultName = "连接失败";
				break;
			case 116:
				resultName = "禁止接口发送";
				break;
			case 117:
				resultName = "绑定IP不正确";
				break;
			case 120:
				resultName = "系统升级";
				break;
			default:
				resultName = "其他异常错误";
				break;
		}
		return resultName;
	}

	@Override
	public String getSPName() {
		return "China";
	}
}
