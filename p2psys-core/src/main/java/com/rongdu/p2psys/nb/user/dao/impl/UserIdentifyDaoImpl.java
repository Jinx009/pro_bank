package com.rongdu.p2psys.nb.user.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserIdentifyModel;

@Repository("theUserIdentifyDao")
public class UserIdentifyDaoImpl extends BaseDaoImpl<UserIdentify> implements UserIdentifyDao
{

	public UserIdentify saveUserIdentify(UserIdentify userIdentify)
	{
		return save(userIdentify);
	}

	public void updateUserIdentify(UserIdentify userIdentify)
	{
		update(userIdentify);
	}

	public UserIdentifyModel getUserIdentifyByUserId(long userId)
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" select user.user_name,attestation.*   ");
		buffer.append("  from rd_user as user ,rd_user_identify as attestation ");
		buffer.append(" where user.user_id = attestation.user_id and  attestation.user_id =:userId ");
		
		String[] names = new String[] { "userId" };
		Object[] values = new Object[] { userId };
		
		return findForUniqueBySql(buffer.toString(),names,values,UserIdentifyModel.class);
	}
	public void savePcUserIdentify(User user)
	{
		UserIdentify userIdentify = new UserIdentify();
		userIdentify.setUser(user);
		userIdentify.setEmailStatus(0);
		userIdentify.setRealNameStatus(0);
		userIdentify.setEmailStatus(0);
		userIdentify.setMobilePhoneStatus(1);
		userIdentify.setVipStatus(0);
		userIdentify.setMobilePhoneVerifyTime(new Date());
		userIdentify.setVideoStatus(0);
		
		save(userIdentify);
	}
}
