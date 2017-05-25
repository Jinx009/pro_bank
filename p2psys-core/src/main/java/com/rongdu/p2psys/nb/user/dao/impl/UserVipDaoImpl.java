package com.rongdu.p2psys.nb.user.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.user.dao.UserVipDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserVip;

@Repository("theUserVipDao")
public class UserVipDaoImpl extends BaseDaoImpl<UserVip> implements UserVipDao
{
	public void savePcUserVip(User user)
	{
		UserVip userVip = new UserVip();
		userVip.setUser(user);
		userVip.setAddIp("");
		userVip.setAddTime(new Date());
		userVip.setApr(0);
		userVip.setInvestMoney(0);
		userVip.setLastYearInvest(0);
		userVip.setLevel(0);
		userVip.setName("普通会员");
		userVip.setUpdateTime(new Date());
		
		save(userVip);
	}
}
