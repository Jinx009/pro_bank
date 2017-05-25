package com.rongdu.p2psys.nb.user.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserIdentifyModel;

@Service("theUserIdentifyService")
public class UserIdentifyServiceImpl implements UserIdentifyService
{

	@Resource
	private UserIdentifyDao theUserIdentifyDao;
	
	public UserIdentify saveUserIdentify(UserIdentify userIdentify)
	{
		return theUserIdentifyDao.save(userIdentify);
	}

	public void updateUserIdentify(UserIdentify userIdentify)
	{
		theUserIdentifyDao.update(userIdentify);
	}

	public UserIdentify getUserIdentifyByUserId(long user_id)
	{
		UserIdentifyModel um = theUserIdentifyDao.getUserIdentifyByUserId(user_id);
		UserIdentify userIdentify = UserIdentifyModel.instance(um);
		return userIdentify;
	}

	public UserIdentify saveWechatUserIdentify(UserIdentify userIdentify)
	{
		userIdentify.setEmailStatus(0);
		userIdentify.setRealNameStatus(0);
		userIdentify.setEmailStatus(0);
		userIdentify.setMobilePhoneStatus(1);
		userIdentify.setVipStatus(0);
		userIdentify.setMobilePhoneVerifyTime(new Date());
		userIdentify.setVideoStatus(0);
		
		return theUserIdentifyDao.save(userIdentify);
	}

}
