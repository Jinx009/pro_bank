package com.rongdu.p2psys.nb.wechat.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.util.WechatMessage;
/**
 * 
 * @author Jinx
 *
 */
public interface WechatCacheService 
{
	/**
	 * 通过id查找缓存
	 * @param id
	 * @return
	 */
	public WechatCache getById(Integer id);
	/**
	 * 新增缓存对象
	 * @param wechatCache
	 */
	public void insertWechatCache(WechatCache wechatCache);
	/**
	 * 通过appId和cachaName获取缓存对象
	 * @param appId
	 * @param cacheName
	 * @return
	 */
	public WechatCache getByAppId(String appId,String cacheName);
	/**
	 * 更新缓存
	 * @param wechatCache
	 */
	public void updateWechatCache(WechatCache wechatCache);
	
	public String sendWechatMessage(WechatMessage wechatMessage)  throws ClientProtocolException, IOException;
}
