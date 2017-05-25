package com.rongdu.p2psys.core.sms;

import java.util.Map;

/**
 * 短信通道
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月19日
 */
public interface SmsPortal {

	public String getSPName();

	/**
	 * 发送消息
	 * 
	 * @param phone
	 * @param content
	 * @return
	 */
	public String send(String phone, String content);

	public Map<String, Integer> getUseInfo();
}
