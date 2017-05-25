package com.rongdu.p2psys.nb.user.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.nb.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;

@Repository("theUserCacheDao")
public class UserCacheDaoImpl extends BaseDaoImpl<UserCache> implements UserCacheDao
{

	@Override
	public UserCache loadUserCache(long userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select  ab.* from rd_user_cache ab where ab.user_id ="+userId);
		Query query = em.createNativeQuery(sb.toString(),UserCache.class);
		List<UserCache> list = query.getResultList();
		if(list != null && list.size() > 0){
			UserCache ab = list.get(0);
			return ab;
		}else{
			return null;
		}

	}
	
	
	public void updateUserCache(UserCache userCache)
	{
		update(userCache);
	}

	public UserCache saveUserCache(UserCache userCache)
	{
		return save(userCache);
	}

	public UserCache savePcUserCache(User user)
	{
		UserCache userCache = new UserCache();
		userCache.setUser(user);
		userCache.setAddIp(Global.getIP());
		userCache.setSex(0);
		userCache.setStatus(0);
		userCache.setUserType(1);
		userCache.setUserNature(1);
		userCache.setCardType(0);
		userCache.setHeadStatus(0);
		userCache.setLoginPwdStatus(0);
		userCache.setLoginFailTimes(0);
		userCache.setLoginTime(new Date());
		userCache.setPayFailTimes(0);
		userCache.setCustomerUserId(0);
		userCache.setCompanyType(0);
		userCache.setRegCapital(0);
		userCache.setInvestStatus(0);
		
		return save(userCache);
	}

	
}
