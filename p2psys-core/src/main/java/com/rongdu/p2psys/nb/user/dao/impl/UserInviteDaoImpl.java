package com.rongdu.p2psys.nb.user.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.user.dao.UserInviteDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserInvite;

@Repository("theUserInviteDao")
public class UserInviteDaoImpl extends BaseDaoImpl<UserInvite> implements UserInviteDao
{
	public UserInvite saveUserInvite(UserInvite userInvite)
	{
		return save(userInvite);
	}

	@SuppressWarnings("unchecked")
	public UserInvite getByHql(String hql)
	{
		Query query = em.createQuery(hql);
		
		List<UserInvite> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			return list.get(0);
		}
		
		return null;
	}
	public void savePcUserInvite(User inviteUser,User user)
	{
		UserInvite userInvite = new UserInvite();
		userInvite.setInviteTime(new Date());
		userInvite.setInviteUser(inviteUser);
		userInvite.setUser(user);
		userInvite.setGift(false);
		
		save(userInvite);
	}

	@SuppressWarnings("unchecked")
	public List<UserInvite> getListByUserId(String hql)
	{
		Query query = em.createQuery(hql);
		
		List<UserInvite> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			return list;
		}
		
		return null;
	}
}
