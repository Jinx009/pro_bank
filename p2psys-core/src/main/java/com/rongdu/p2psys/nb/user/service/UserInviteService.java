package com.rongdu.p2psys.nb.user.service;

import java.util.List;

import com.rongdu.p2psys.user.domain.UserInvite;

public interface UserInviteService
{
	public UserInvite saveUserInvite(UserInvite userInvite);
	
	public void updateUserInvite(UserInvite userInvite);
	
	public UserInvite getByUserId(long userId);
	
	public List<UserInvite> getListByUserId(long userId);
}
