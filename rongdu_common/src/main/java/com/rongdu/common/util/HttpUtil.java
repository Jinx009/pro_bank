package com.rongdu.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 工具类-Http请求
 * 
 * @author xx
 * @version 2.0
 * @since 2014年1月28日
 */
public class HttpUtil {
	/**
	 * 发起http请求，获取响应结果
	 * 
	 * @param pageURL
	 * @return
	 */
	public static String getHttpResponse(String pageURL) {
		String pageContent = "";
		BufferedReader in = null;
		InputStreamReader isr = null;
		InputStream is = null;
		HttpURLConnection huc = null;
		try {
			URL url = new URL(pageURL);
			huc = (HttpURLConnection) url.openConnection();
			is = huc.getInputStream();
			isr = new InputStreamReader(is);
			in = new BufferedReader(isr);
			String line = null;
			while (((line = in.readLine()) != null)) {
				if (line.length() == 0)
					continue;
				pageContent += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (isr != null)
					isr.close();
				if (in != null)
					in.close();
				if (huc != null)
					huc.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pageContent;
	}
}
