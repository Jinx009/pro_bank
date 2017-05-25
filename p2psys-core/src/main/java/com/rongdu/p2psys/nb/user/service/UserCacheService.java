package com.rongdu.p2psys.nb.user.service;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.p2psys.user.domain.UserCache;

public interface UserCacheService
{
	public void updateUserCache(UserCache userCache);
	
	public UserCache saveUserCache(UserCache userCache);
	
	public UserCache saveWechatUserCache(UserCache userCache);
	
	public UserCache getById(long id);

	/**
	 * 锁定
	 * 
	 * @param userId
	 * @param type
	 */
	boolean doLock(HttpServletRequest request, long userId, String type);
	
	public UserCache getByUserId(long userId);
}
