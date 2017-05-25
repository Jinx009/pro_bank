package com.rongdu.p2psys.nb.user.dao.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.nb.account.dao.AccountBankDao;
import com.rongdu.p2psys.nb.user.dao.UserDao;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;

@Repository("theUserDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao
{
	@Resource
	private AccountBankDao theAccountBankDao;
	
	@SuppressWarnings("unchecked")
	public User getByAttribute(String attributeName, String attributeValue,String attributeFactoryName, String attributeFactoryId)
	{
		User user = null;
		
		String hql = " from User where  "+attributeName+" = '"+attributeValue+"' and  "+attributeFactoryName+" = '"+attributeFactoryId+"'  ";
		
		Query query = em.createQuery(hql);
		
		List<User> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			return list.get(0);
		}
		
		return user;
	}

	@SuppressWarnings("unchecked")
	public List<User> getByGroupId(Long groupId)
	{
		String hql = " from User where  bindId = "+groupId+" order by bindId ";
		
		Query query = em.createQuery(hql);
		
		List<User> list = query.getResultList();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public int getUserByCardId(String cardId,String channelKey)
	{
		int count = 0;
		StringBuffer sql =new StringBuffer("select u.* from rd_user as u left join rd_user_identify i on u.user_id=i.user_id  where card_id= '"+cardId+"' and i.real_name_status=1  and u.add_time>'"+ConstantUtil.USER_ADD_TIME+"'  ");
		Query query = em.createNativeQuery(sql.toString(),User.class);
		List<User> list = query.getResultList();
		if(list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				User u = list.get(i);
				List<AccountBank> abList =theAccountBankDao.list(u.getUserId(), channelKey);
				if(abList!=null && abList.size()>0){
					count = 1;
				}
			}
		}
		return count;
	}
	
	public User getUserByCardId(String cardId)
	{
		StringBuffer sql =new StringBuffer("select u.* from rd_user as u left join rd_user_identify i on u.user_id=i.user_id  where card_id= '"+cardId+"' and i.real_name_status=1  and u.add_time>'"+ConstantUtil.USER_ADD_TIME+"'  ");
		Query query = em.createNativeQuery(sql.toString(),User.class);
		List<User> list = query.getResultList();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public User saveUser(User user)
	{
		return save(user);
	}

	public void updateUser(User user)
	{
		update(user);
	}

	@SuppressWarnings("unchecked")
	public User doLogin(String userName, String pwd)
	{
		User user = null;
		
		String hql = " from User where  userName = '"+userName+"' and pwd = '"+MD5.encode(pwd)+"' ";
		
		Query query = em.createQuery(hql);
		
		List<User> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			return list.get(0);
		}
		
		return user;
	}

	@SuppressWarnings("unchecked")
	public User getByUserName(String userName)
	{
		String hql = " from User where  userName = '"+userName+"'  ";
		
		Query query = em.createQuery(hql);
		
		List<User> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			return list.get(0);
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<User> findWechatUser(String appId)
	{
		List<User> userList = null;
		
		String hql = " from User where  wechatId = '"+appId+"' and  wechatOpenId is  not null  ";
		
		Query query = em.createQuery(hql);
		
		List<User> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			userList = list;
		}
		
		return userList;
	}


}
