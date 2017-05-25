package com.rongdu.p2psys.nb.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.user.dao.UserInviteDao;
import com.rongdu.p2psys.nb.user.service.UserInviteService;
import com.rongdu.p2psys.user.domain.UserInvite;

@Service("theUserInviteService")
public class UserInviteServiceImpl implements UserInviteService
{
	@Resource
	private UserInviteDao theUserInviteDao;
	
	public UserInvite saveUserInvite(UserInvite userInvite)
	{
		return theUserInviteDao.save(userInvite);
	}

	public void updateUserInvite(UserInvite userInvite)
	{
		theUserInviteDao.update(userInvite);
	}

	public UserInvite getByUserId(long userId)
	{
		String hql = " from UserInvite where user.userId = "+userId+"  ";
		
		return theUserInviteDao.getByHql(hql);
	}

	@Override
	public List<UserInvite> getListByUserId(long userId)
	{
		String hql = " from UserInvite where inviteUser.userId = "+userId+"  ";
		
		return theUserInviteDao.getListByUserId(hql);
	}
	
}
