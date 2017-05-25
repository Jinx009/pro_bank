package com.rongdu.p2psys.core.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.rongdu.common.util.HttpUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;

/**
 * 助通短信
 * 
 * @author Administrator
 * @version 1.0
 * @since 2015年1月9日
 */
public class ZtSmsPortalImpl implements SmsPortal {

	private Logger logger = Logger.getLogger(ZtSmsPortalImpl.class);

	private String[] sensitive_words = { "验证码" };

	private String productid_1 = "800800"; // 产品id1（第一通道：获取优质短信通道（验证码））676767

	private String productid_2 = "800800"; // 产品id2（第二通道：获取标、充值等操作通道）48661
/*	private String productid_1 = "888888"; // 产品id1（第一通道：获取优质短信通道（验证码））676767

	private String productid_2 = "888888"; // 产品id2（第二通道：获取标、充值等操作通道）48661
*/
	@Override
	public String send(String phone, String content) {
		try {
			content = URLEncoder.encode(content, "UTF-8");
			String username = StringUtil
					.isNull(Global.getValue("sms_username"));
			String password = StringUtil
					.isNull(Global.getValue("sms_password"));
			String smsUrl = StringUtil.isNull(Global.getValue("notice_sms"));
			logger.info("【助通】发送端账户======" + username);
			logger.info("【助通】发送端密码======" + password);
			logger.info("【助通】发送手机号======" + phone);
			logger.info("【助通】发送内容======" + content);
			String product_id = productid_2 == null ? "" : productid_2;
			for (String sensitive_word : sensitive_words) {
				if (URLDecoder.decode(content, "UTF-8").toString()
						.contains(sensitive_word)) {
					product_id = productid_1 == null ? "" : productid_1;
					break;
				}
			}
		 //content = "【众服】" + content;
			content = content+"【800众服】";
			String dstime = "";
			String xh = "";
			String url = smsUrl + "?username=" + username + "&password="
					+ password + "&mobile=" + phone + "&content=" + content
					+ "&productid=" + product_id + "&xh=" + xh + "&dstime="
					+ dstime + "";
			logger.debug("url:" + url);
			String s = HttpUtil.getHttpResponse(url);
			logger.debug("短信发送结果：" + s);
			String result = "ok";
			if ("-1".equals(s)) {
				result = "用户名或者密码不正确, " + s;
			} else if (s.contains("1,")) {
				result = "ok";
			} else if (s.contains("0,")) {
				result = "短信发送失败, " + s;
			} else if ("2".equals(s)) {
				result = "余额不足, " + s;
			} else if ("3".equals(s)) {
				result = "扣费失败（请联系客服）, " + s;
			} else if (s.contains("5,")) {
				result = "短信定时成功, " + s;
			} else if ("6".equals(s)) {
				result = "有效号码为空, " + s;
			} else if ("7".equals(s)) {
				result = "短信内容为空, " + s;
			} else if ("8".equals(s)) {
				result = "无签名，必须，格式：【签名】, " + s;
			} else if ("9".equals(s)) {
				result = "没有Url提交权限, " + s;
			} else if ("10".equals(s)) {
				result = "发送号码过多,最多支持200个号码, " + s;
			} else if ("11".equals(s)) {
				result = "产品ID异常, " + s;
			} else if ("12".equals(s)) {
				result = "参数异常, " + s;
			} else if ("13".equals(s)) {
				result = "12小时重复提交, " + s;
			} else if ("14".equals(s)) {
				result = "用户名或密码不正确，产品余额为0，禁止提交，联系客服, " + s;
			} else if ("15".equals(s)) {
				result = "Ip验证失败, " + s;
			} else if ("19".equals(s)) {
				result = "短信内容过长，最多支持500个, " + s;
			} else if ("20".equals(s)) {
				result = "定时时间不正确：格式：20130202120212(14位数字), " + s;
			}
			logger.info(result);
			return result;
		} catch (UnsupportedEncodingException e) {
			logger.info("URL转码失败");
		}
		return null;
	}
	
	@Override
	public Map<String, Integer> getUseInfo() {
		String username = StringUtil.isNull(Global.getValue("sms_username"));
		String password = StringUtil.isNull(Global.getValue("sms_password"));
		String smsUrl = StringUtil.isNull(Global.getValue("notice_sms"));
		String productId1Url = smsUrl + "balance.do?username=" + username + "&password=" + password + "&productid=" + productid_1;
		String productId2Url = smsUrl + "balance.do?username=" + username + "&password=" + password + "&productid=" + productid_2;
		String productId1Num = HttpUtil.getHttpResponse(productId1Url); // 第一通道：获取优质短信通道（验证码）
		String productId2Num = HttpUtil.getHttpResponse(productId2Url); // 第二通道：获取标、充值等操作通道
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int usenum1 = Integer.parseInt(productId1Num);
		int usenum2 = Integer.parseInt(productId2Num);
		map.put("usenum", usenum1 + usenum2);
		return map;
	}
	
	public static void main(String[] args) {
		/*String username = "shunnong";
		String password = "vPuXLY6M";
		String smsUrl = "http://www.ztsms.cn:8800/";
		String content = "测试00000";
		try {
			content = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		String phone = "*******";
		String productid_1 = "676767"; // 产品id1（第一通道：获取优质短信通道（验证码））676767
		String productid_2 = "676766";*/ // 产品id2（第二通道：获取标、充值等操作通道）
		/*String[] sensitive_words = { "验证码" };
		String product_id = productid_1 == null ? "" : productid_2;
		for (String sensitive_word : sensitive_words) {
			if (content.contains(sensitive_word)) {
				product_id = productid_1 == null ? "" : productid_1;
				break;
			}
		}
		content = content + "【顺农投资】";
		String dstime = "";
		String xh = "";
		String url = smsUrl + "sendXSms.do?username=" + username + "&password=" + password
				+ "&mobile=" + phone + "&content=" + content + "&productid="
				+ product_id + "&xh=" + xh + "&dstime=" + dstime + "";
		String s = HttpUtils.getHttpResponse(url);
		System.out.println(s);*/
		
		// 短信可用条数查询
		/*String productId1Url = smsUrl + "balance.do?username=" + username + "&password=" + password + "&productid=" + productid_1;
		String productId2Url = smsUrl + "balance.do?username=" + username + "&password=" + password + "&productid=" + productid_2;
		String productId1Num = HttpUtils.getHttpResponse(productId1Url); // 第一通道：获取优质短信通道（验证码）
		String productId2Num = HttpUtils.getHttpResponse(productId2Url); // 第二通道：获取标、充值等操作通道
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int usenum1 = Integer.parseInt(productId1Num);
		int usenum2 = Integer.parseInt(productId2Num);
		map.put("usenum", usenum1 + usenum2);
		System.out.println(usenum1);
		System.out.println(usenum1 + usenum2);
		System.out.println(usenum1 + usenum2);*/
		
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
		Date date = new Date(currentTime);
		System.out.println(formatter.format(date));
		System.out.println(System.currentTimeMillis());
	}

	@Override
	public String getSPName() {
		
		return null;
	}
}
