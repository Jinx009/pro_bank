package com.rongdu.p2psys.nb.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;

public interface UserCacheDao extends BaseDao<UserCache>
{
	/**
	 * 更新用户缓存表
	 * 
	 * @param userCache
	 */
	public void updateUserCache(UserCache userCache);
	
	/**
	 * 保存
	 * 
	 * @param userCache
	 * @return
	 */
	public UserCache saveUserCache(UserCache userCache);
	
	public UserCache savePcUserCache(User user);
	
	/**
	 * 根据用户Id查询
	 * @param userId
	 * @return
	 */
	public UserCache loadUserCache(long userId);
}
