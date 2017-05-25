package com.rongdu.p2psys.core.sms;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.rongdu.common.util.MD5Util;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.enums.EnumRuleNid;
import com.rongdu.p2psys.core.dao.SystemConfigDao;
import com.rongdu.p2psys.core.rule.RuleModel;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.XmlUtils;

/**
 * 大汉三通短信接口
 * @author yinliang
 * @version 2.0
 * @Date   2014年12月22日
 */
public class DaHanSanTongImpl implements SmsPortal {
	public static Logger logger = Logger.getLogger(DaHanSanTongImpl.class);
	public static String sign = ""; //短信签名
	public static String msgid = "";//短信编号（32位UUID），选填项
	public static String subcode = "";//短信签名子码，选填项
	public static String sendtime = "";//短信发送时间，为空或早于当前时间表示立即发送
	
	@Override
	public String getSPName() {
		return "santong";
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public String send(String phone, String content) {
		try {
			SystemConfigDao systemConfigDao = (SystemConfigDao) BeanUtil.getBean("systemConfigDao");
			String username = systemConfigDao.findObjByProperty("nid", "con_sms_username").getValue();
			String password = systemConfigDao.findObjByProperty("nid", "con_sms_password").getValue();
			String smsUrl = systemConfigDao.findObjByProperty("nid", "con_notice_sms").getValue();
			
			Map<String, String> params = new LinkedHashMap<String, String>();
			DaHanSanTongImpl docXml = new DaHanSanTongImpl();
			String message = docXml.DocXml(username, MD5Util.MD5Encode(password,""),
					msgid, phone, content, sign, subcode, sendtime);
			logger.info("发送短信-->" + message);
			params.put("message", message);
			String resp = doPost(smsUrl, params);
			logger.info("发送短信回调-->"+resp);
			//解析返回的xml数据
			/**
			 * 返回格式为<?xml version='1.0' encoding='UTF-8'?>
			 * <response>
			 * <result>提交结果（0代表成功）</result>
			 * <desc>状态描述（成功描述为提交成功）</desc>
			 * </response>
			 */
			Map<String, Object> map = (Map<String, Object>)XmlUtils.xml2map(resp).get("result");
			int ret = NumberUtil.getInt(map.get("result").toString());
			logger.info("短信发送返回状态--->" + ret);
			String result = "ok";
			//其他异常
			if(ret !=0 ){
				switch (ret) {
				case 1:
					result = "帐号无效";
					break;
				case 2:
					result = "密码错误";
					break;
				case 4:
					result = "错误号码/限制运营商号码";
					break;
				case 5:
					result = "手机号码个数超过最大限制(500个)";
					break;
				case 6:
					result = "短信内容超过最大限制(350字)";
					break;
				case 14:
					result = "手机号码为空";
					break;
				case 19:
					result = "用户被禁发或禁用";
					break;
				case 21:
					result = "短信内容为空";
					break;
				default:
					result = "其他异常错误";
					break;
				}
			}
			logger.info("短信发送返回信息--->" + resp);
			return result;
		} catch (Exception e) {
			logger.info("短信发送异常--->" + e.getMessage());
			return "短信发送失败";
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public Map<String, Integer> getUseInfo() {
		if (null == Global.getRule(EnumRuleNid.SANTONG_SMS.getValue())) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("usenum", 0);
			map.put("usednum", 0);

			return map;
		}
		RuleModel rule = new RuleModel(Global.getRule(EnumRuleNid.SANTONG_SMS.getValue()));

		String username = rule.getValueStrByKey("sms_username");
		String password = rule.getValueStrByKey("sms_password");
		String smsuseUrl = rule.getValueStrByKey("use_sms_url");
		String smsusedUrl = rule.getValueStrByKey("used_sms_url");

		Map<String, String> params = new LinkedHashMap<String, String>();
		String message = "<?xml version='1.0' encoding='UTF-8'?><message><account>"
				+ username
				+ "</account>"
				+ "<password>"
				+ MD5Util.MD5Encode(password,"")
				+ "</password></message>";
		params.put("message", message);
		
		/**
		 * 返回格式为：<?xml version='1.0' encoding='UTF-8'?>
		 * <response>
		 * <result>状态</result>
		 * <desc></desc>
		 * <sms>
		 * <amount>短信账户余额（三位小数）</amount>
		 * <number>短信可用条数（int）</number>
		 * <freeze>短信账户余额冻结金额（三位小数）</freeze>
		 * </sms>
		 * </response>
		 */
		String resp = doPost(smsuseUrl, params);
		logger.info("查询短信余额回调-->"+resp);
		int usenum = 0;
		int usednum = 0;
		//解析XML串
		try {
			Map<String, Object> xmlmap = XmlUtils.xml2map(resp);
			int ret = NumberUtil.getInt(((Map<String, Object>)xmlmap.get("result")).get("result").toString());
			if(ret == 0){
				usenum = NumberUtil.getInt(((Map<String, Object>)((Map<String, Object>)xmlmap.get("sms")).get("number")).get("number").toString());
			}
		} catch (DocumentException e) {
			logger.info("查询失败"+e.getMessage());
		}
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("usenum", usenum);
		map.put("usednum", usednum);
		return map;
	}
	
	/**
	 * 执行一个HTTP POST请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的HTML
	 */
	private static String doPost(String url, Map<String, String> params) {
		String response = null;
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(
				HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

		// 设置Post数据
		if (!params.isEmpty()) {
			int i = 0;
			NameValuePair[] data = new NameValuePair[params.size()];
			for (Entry<String, String> entry : params.entrySet()) {
				data[i] = new NameValuePair(entry.getKey(), entry.getValue());
				i++;
			}

			postMethod.setRequestBody(data);

		}
		try {
			client.executeMethod(postMethod);
			if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
				response = postMethod.getResponseBodyAsString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return response;
	}
	
	/**
	 * 使用document 对象封装XML
	 * 
	 * @param userName
	 * @param pwd
	 * @param id
	 * @param phone
	 * @param contents
	 * @param sign
	 * @param subcode
	 * @param sendtime
	 * @return
	 */
	public String DocXml(String userName, String pwd, String msgid,
			String phone, String contents, String sign, String subcode,
			String sendtime) {
		Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("UTF-8");
		Element message = doc.addElement("response");
		Element account = message.addElement("account");
		account.setText(userName);
		Element password = message.addElement("password");
		password.setText(pwd);
		Element msgid1 = message.addElement("msgid");
		msgid1.setText(msgid);
		Element phones = message.addElement("phones");
		phones.setText(phone);
		Element content = message.addElement("content");
		content.setText(contents);
		Element sign1 = message.addElement("sign");
		sign1.setText(sign);
		Element subcode1 = message.addElement("subcode");
		subcode1.setText(subcode);
		Element sendtime1 = message.addElement("sendtime");
		sendtime1.setText(sendtime);
		return message.asXML();

	}
	
}
