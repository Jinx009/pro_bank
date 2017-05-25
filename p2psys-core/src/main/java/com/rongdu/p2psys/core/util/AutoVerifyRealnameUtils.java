package com.rongdu.p2psys.core.util;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 * 自动审核实名认证 autor : sj
 */

public class AutoVerifyRealnameUtils {

	/** 日志 */
	private static Logger logger = Logger.getLogger(AutoVerifyRealnameUtils.class);

	/**
	 * 自动审核实名
	 * 
	 * @param url url地址
	 * @return 空
	 */
	public static String checkAutoVerifyRealname(String url) {
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			// 执行getMethod
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("checkAutoVerifyRealname failed: " + getMethod.getStatusLine());
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
		return "";
	}
}
