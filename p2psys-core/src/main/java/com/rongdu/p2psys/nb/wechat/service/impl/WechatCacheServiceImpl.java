package com.rongdu.p2psys.nb.wechat.service.impl;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.wechat.dao.WechatCacheDao;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.nb.wechat.util.WechatMessage;
import com.rongdu.p2psys.nb.wechat.util.WechatMessageUtil;
import com.rongdu.p2psys.nb.wechat.util.WechatUtil;

@Service("wechatCacheService")
public class WechatCacheServiceImpl implements WechatCacheService
{
	@Resource
	private WechatCacheDao wechatCacheDao;

	public WechatCache getById(Integer id)
	{
		return wechatCacheDao.find(id);
	}

	public void insertWechatCache(WechatCache wechatCache)
	{
		wechatCacheDao.save(wechatCache);
	}

	public WechatCache getByAppId(String appId,String cacheName)
	{
		String hql = "  from WechatCache where appId = '"+appId+"' and cacheName = '"+cacheName+"' ";
		
		WechatCache wechatCache = wechatCacheDao.findByHql(hql);
		
		return wechatCache;
	}

	public void updateWechatCache(WechatCache wechatCache)
	{
		wechatCacheDao.updateWechatCache(wechatCache);
	}

	/**
	 * 发送模板消息
	 */
	public String sendWechatMessage(WechatMessage wechatMessage) throws ClientProtocolException, IOException
	{
		String access_token = null;
		
		WechatCache wechatCache = getByAppId(wechatMessage.getAppId(),"accessToken");
		
		if (getTimestamp(wechatCache.getLastTimestamp()))
		{
			access_token = wechatCache.getCacheValue();
		} 
		else
		{
			int currentTimestamp = (int) (System.currentTimeMillis() / 1000);
			access_token = WechatUtil.getAccessToken(wechatMessage.getAppId(),wechatMessage.getAppSecret());

			WechatCache wechatCache2 = getById(wechatCache.getId());
			wechatCache2.setCacheValue(access_token);
			wechatCache2.setLastTimestamp(currentTimestamp);

			updateWechatCache(wechatCache2);
		}
		
		String msg = WechatMessageUtil.getMessageByType(wechatMessage);
		String status = WechatUtil.sendMoneyOrder(msg, access_token);
		
		return status;
	}
	
	
	
	public static boolean getTimestamp(int timestamp)
	{
		int currentTimestamp = (int) (System.currentTimeMillis() / 1000);

		if ((currentTimestamp - timestamp) >= 100)
		{
			return false;
		}

		return true;
	}

}
