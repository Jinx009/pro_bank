package com.rongdu.p2psys.nb.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserIdentifyModel;

public interface UserIdentifyDao extends BaseDao<UserIdentify>
{
	/**
	 * 保存
	 * 
	 * @param userIdentify
	 * @return
	 */
	public UserIdentify saveUserIdentify(UserIdentify userIdentify);
	
	/**
	 * 更新
	 * 
	 * @param userIdentify
	 */
	public void updateUserIdentify(UserIdentify userIdentify);
	
	/**
	 * 通过用户id获取用户认证状态
	 * 
	 * @param userId
	 * @return
	 */
	public UserIdentifyModel getUserIdentifyByUserId(long userId);
	
	public void savePcUserIdentify(User user);
}
