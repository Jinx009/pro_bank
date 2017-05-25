package com.rongdu.p2psys.nb.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserVip;

public interface UserVipDao extends BaseDao<UserVip>
{
	public void savePcUserVip(User user);
}
