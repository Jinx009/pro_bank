package com.rongdu.p2psys.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import org.apache.http.client.ClientProtocolException;

import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;

public class WechatJSSign 
{
	/**
	 * 主方法
	 * @param url
	 * @param appID
	 * @param appSecrect
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static Map<String, String> createSign(String jsapi_ticket,String url, String appID,String appSecrect) throws ClientProtocolException, IOException
	{
		Map<String, String> ret = sign(jsapi_ticket, url);

		return ret;
	}
	
	/**
	 * 注册
	 * @param jsapi_ticket
	 * @param url
	 * @return
	 */
	public static Map<String, String> sign(String jsapi_ticket, String url)
	{
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";

		string1 = "jsapi_ticket="+jsapi_ticket+"&noncestr="+ nonce_str+"&timestamp="+timestamp+"&url="+url;

		try
		{
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);

		return ret;
	}
	
	/**
	 * 去除特殊字符
	 * @param hash
	 * @return
	 */
	private static String byteToHex(final byte[] hash)
	{
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	/**
	 * 随机字符串
	 * @return
	 */
	private static String create_nonce_str()
	{
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 获取时间戳
	 * @return
	 */
	private static String create_timestamp()
	{
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	
	/**
	 * 校验数据库微信信息
	 * @param wechatCache
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String checkWechatCache(String appId,String appSecret,WechatCache wechatCache) throws ClientProtocolException, IOException
	{
		WechatCacheService wechatCacheService = (WechatCacheService) BeanUtil.getBean("wechatCacheService");
		
		String jsapi_ticket = null;
		
		if (getTimestamp(wechatCache.getLastTimestamp()))
		{
			jsapi_ticket = wechatCache.getCacheValue();
		} 
		else
		{
			int currentTimestamp = (int) (System.currentTimeMillis() / 1000);
			jsapi_ticket = WechatUtil.getJSApiTicket(appId, appSecret);

			WechatCache wechatCache2 = wechatCacheService.getById(wechatCache.getId());
			wechatCache2.setCacheValue(jsapi_ticket);
			wechatCache2.setLastTimestamp(currentTimestamp);

			wechatCacheService.updateWechatCache(wechatCache2);
		}
		
		return jsapi_ticket;
	}
	
	public static boolean getTimestamp(int timestamp)
	{
		int currentTimestamp = (int) (System.currentTimeMillis() / 1000);

		if ((currentTimestamp - timestamp) >= 7200)
		{
			return false;
		}

		return true;
	}
	
	
	
}
