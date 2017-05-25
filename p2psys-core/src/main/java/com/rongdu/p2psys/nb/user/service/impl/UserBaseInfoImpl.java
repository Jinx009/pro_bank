package com.rongdu.p2psys.nb.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.nb.user.dao.UserBaseInfoDao;
import com.rongdu.p2psys.nb.user.service.UserBaseInfoService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.domain.UserCache;

@Service("theUserBaseInfoService")
public class UserBaseInfoImpl implements UserBaseInfoService
{

	@Resource
	private UserBaseInfoDao theUserBaseInfoDao;

	public UserBaseInfo saveUserBaseInfo(UserCache userCache)
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

		return theUserBaseInfoDao.save(baseInfo);
	}

}
