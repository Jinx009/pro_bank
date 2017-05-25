package com.rongdu.p2psys.nb.user.service;

import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.domain.UserCache;

public interface UserBaseInfoService 
{
	public UserBaseInfo saveUserBaseInfo(UserCache userCache);
}
