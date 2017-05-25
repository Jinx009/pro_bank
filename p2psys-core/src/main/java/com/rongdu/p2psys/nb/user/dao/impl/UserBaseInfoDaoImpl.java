package com.rongdu.p2psys.nb.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.nb.user.dao.UserBaseInfoDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.domain.UserCache;

@Repository("theUserBaseInfoDao")
public class UserBaseInfoDaoImpl extends BaseDaoImpl<UserBaseInfo> implements UserBaseInfoDao
{
	public void savePcUserBaseInfo(UserCache userCache)
	{
		User user = userCache.getUser();
		UserBaseInfo baseInfo = new UserBaseInfo(user);
		if ("".equals(userCache.getProvince())&& !"".equals(userCache.getCity())&& !"".equals(userCache.getArea()))
		{
			baseInfo.setProvince(userCache.getCity());
			baseInfo.setCity(userCache.getArea());
		}
		else if (!"".equals(userCache.getProvince())&& !"".equals(userCache.getCity()))
		{
			baseInfo.setProvince(userCache.getProvince());
			baseInfo.setCity(userCache.getCity());
		}
		if (user.getCardId() != null)
		{
			baseInfo.setBirthday(StringUtil.getBirthdayByCardid(user.getCardId()));
		}

		save(baseInfo);	
	}

}
