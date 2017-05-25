package com.rongdu.p2psys.nb.user.service;

import com.rongdu.p2psys.user.domain.UserIdentify;

public interface UserIdentifyService
{
	public UserIdentify saveUserIdentify(UserIdentify userIdentify);
	
	public void updateUserIdentify(UserIdentify userIdentify);
	
	public UserIdentify getUserIdentifyByUserId(long user_id);

	public UserIdentify saveWechatUserIdentify(UserIdentify userIdentify);
}
