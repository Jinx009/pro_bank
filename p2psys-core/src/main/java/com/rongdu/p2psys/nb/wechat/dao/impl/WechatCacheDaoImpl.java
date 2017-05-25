package com.rongdu.p2psys.nb.wechat.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.wechat.dao.WechatCacheDao;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;

@Repository("wechatCacheDao")
public class WechatCacheDaoImpl extends BaseDaoImpl<WechatCache> implements WechatCacheDao
{
	@SuppressWarnings("unchecked")
	public WechatCache findByHql(String hql)
	{
		Query query = (Query) em.createQuery(hql);
		
		WechatCache wechatCache = null;
		
		List<WechatCache> list = query.getResultList();
		
		if(list!=null&&!list.isEmpty())
		{
			wechatCache = list.get(0);
		}
		
		return wechatCache;
	}
	
	public void updateWechatCache(WechatCache wechatCache)
	{
		update(wechatCache);
	}

}
