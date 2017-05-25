package com.rongdu.common.util.uchon;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.rongdu.common.util.StringUtil;

public class UchonHelper {

	private static Logger log = Logger.getLogger(UchonHelper.class);

	/**
	 * 动态口令验证接口
	 * 
	 * @param url
	 * @param snId
	 * @param dymPassword
	 * @return 验证结果
	 */
	public static String checkDymicPassword(String url, String snId,
			String dymPassword) {
		if (StringUtil.isNotBlank(snId) && StringUtil.isNotBlank(dymPassword)) {
			HttpClient httpClient = new HttpClient();
			GetMethod getMethod = new GetMethod(url + "/sn_id/" + snId
					+ "/dym_password/" + dymPassword + ".html");
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			try {
				// 执行getMethod
				int statusCode = httpClient.executeMethod(getMethod);
				if (statusCode != HttpStatus.SC_OK) {
					log.error("checkDymicPassword2 failed: "
							+ getMethod.getStatusLine());
				}
				byte[] responseBody = getMethod.getResponseBody();
				return new String(responseBody);
			} catch (Exception e) {
				log.error(e);
			} finally {
				// 释放连接
				getMethod.releaseConnection();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		String url = "http://key.erongdu.com/checkDymicPassword2";
		String snId = "798201003";
		String dymPassword = "375695";
		System.out.println(checkDymicPassword(url, snId, dymPassword));
		;
	}
}
