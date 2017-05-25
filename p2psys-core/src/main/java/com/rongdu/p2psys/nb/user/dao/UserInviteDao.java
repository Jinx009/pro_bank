package com.rongdu.p2psys.nb.user.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserInvite;

public interface UserInviteDao extends BaseDao<UserInvite> 
{
	/**
	 * 保存
	 * 
	 * @param userInvite
	 * @return
	 */
	public UserInvite saveUserInvite(UserInvite userInvite);
	
	public UserInvite getByHql(String hql);
	
	public void savePcUserInvite(User inviteUser, User user);
	
	public List<UserInvite> getListByUserId(String hql);
}
