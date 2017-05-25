package com.rongdu.p2psys.user.service;

import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.domain.UserCache;

/**
 * 用户基本信息
 * 
 * @author wzh
 * @version 2.0
 * @since 2014年11月4日
 */
public interface UserBaseInfoService {

	/**
	 * 获取用户基本信息
	 * 
	 * @param userId
	 * @return
	 */
	public UserBaseInfo findByUserId(long userId);

	/**
	 * 更新用户基本信息
	 * 
	 * @param userBaseInfo
	 * @return
	 */
	public void save(UserBaseInfo userBaseInfo);
	
	public void save(UserCache uc);
}
