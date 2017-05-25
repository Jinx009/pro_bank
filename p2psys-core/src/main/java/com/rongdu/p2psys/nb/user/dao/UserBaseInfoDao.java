package com.rongdu.p2psys.nb.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.domain.UserCache;

public interface UserBaseInfoDao extends BaseDao<UserBaseInfo>
{
	public void savePcUserBaseInfo(UserCache userCache);
}
