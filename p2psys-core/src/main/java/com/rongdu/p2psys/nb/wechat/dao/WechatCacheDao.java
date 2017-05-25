package com.rongdu.p2psys.nb.wechat.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;

/**
 * 
 * @author Jinx
 *
 */
public interface WechatCacheDao extends BaseDao<WechatCache>
{
	/**
	 * 通过hql获取缓存
	 * @param hql
	 * @return
	 */
	public WechatCache findByHql(String hql);
	/**
	 * 更新缓存值
	 * @param wechatCache
	 */
	public void updateWechatCache(WechatCache wechatCache);
}
