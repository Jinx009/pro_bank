package com.rongdu.p2psys.nb.user.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.util.RandomUtil;
import com.rongdu.p2psys.nb.user.dao.UserPromotDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPromot;

@Repository("theUserPromotDao")
public class UserPromotDaoImpl extends BaseDaoImpl<UserPromot> implements UserPromotDao
{
	public UserPromot saveUserPromot(UserPromot userPromot)
	{
		return saveUserPromot(userPromot);
	}

	public void updateUserPromot(UserPromot userPromot)
	{
		update(userPromot);
	}

	@SuppressWarnings("unchecked")
	public UserPromot getUserPromotByCode(String code) 
	{
		String sql = " from UserPromot where couponCode = '"+code+"' ";
		
		Query query = em.createQuery(sql);
		List<UserPromot> list = query.getResultList();
		if (list.size() > 0&&!list.isEmpty())
		{
			return list.get(0);
		} 
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public boolean alreadyHaveCode(String code) 
	{
		String sql = " from UserPromot where couponCode = ?1 ";
		
		Query query = em.createQuery(sql).setParameter(1, code);
		List<UserPromot> list = query.getResultList();
		if (list.size() > 0)
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public UserPromot findByHql(String hql)
	{
		Query query = em.createQuery(hql);
		
		List<UserPromot> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			return list.get(0);
		}
		
		return null;
	}
	private String generateCouponCode(int length)
	{
		String result = "";
		do
		{
			result = RandomUtil.getSpecialRandomCode(length);
		}
		while(checkCode(result));
		return result;
	}
	@SuppressWarnings("unchecked")
	private boolean checkCode(String code) 
	{
		String sql = " from UserPromot where couponCode = ?1 ";
		
		Query query = em.createQuery(sql).setParameter(1, code);
		List<UserPromot> list = query.getResultList();
		if (list.size() > 0)
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}	
	
	public void savePcUserPromot(User user)
	{
		UserPromot userPromot = new UserPromot();
		userPromot.setUser(user);
		userPromot.setCouponCode(generateCouponCode(4));
		userPromot.setAddTime(new Date());
		userPromot.setUsedTimes(0);
		userPromot.setCanUseTimes(9999);
		userPromot.setRate(0);
		userPromot.setStatus(1);
		
		save(userPromot);
	}
}
