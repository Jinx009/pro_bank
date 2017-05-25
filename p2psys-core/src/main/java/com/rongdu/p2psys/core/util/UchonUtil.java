package com.rongdu.p2psys.core.util;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.rongdu.common.util.StringUtil;

public class UchonUtil {

	/** 日志 */
	private static Logger logger = Logger.getLogger(UchonUtil.class);

	/**
	 * 动态口令验证接口-GET访问方式
	 * 
	 * @param url 地址
	 * @param snId 动态口令卡ID
	 * @param dymPassword 动态口令卡密码
	 * @return null
	 */
	public static String checkDymicPassword(String url, String snId, String dymPassword) {
		if (StringUtil.isNotBlank(snId) && StringUtil.isNotBlank(dymPassword)) {
			HttpClient httpClient = new HttpClient();
			GetMethod getMethod = new GetMethod(url + "/sn_id/" + snId + "/dym_password/" + dymPassword + ".html");
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
			try {
				// 执行getMethod
				int statusCode = httpClient.executeMethod(getMethod);
				if (statusCode != HttpStatus.SC_OK) {
					logger.error("checkDymicPassword2 failed: " + getMethod.getStatusLine());
				}
				byte[] responseBody = getMethod.getResponseBody();
				return new String(responseBody);
			} catch (HttpException e) {
				logger.error(e);
			} catch (IOException e) {
				// 发生网络异常
				logger.error(e);
			} finally {
				// 释放连接
				getMethod.releaseConnection();
			}
		}
		return null;
	}
}
