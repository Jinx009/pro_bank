package com.rongdu.p2psys.nb.user.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.user.dao.UserVipDao;
import com.rongdu.p2psys.nb.user.service.UserVipService;
import com.rongdu.p2psys.user.domain.UserVip;

@Service("theUserVipService")
public class UserVipServiceImpl implements UserVipService
{

	@Resource
	private UserVipDao theUserVipDao;

	@Override
	public void saveWechatUserVip(UserVip userVip)
	{
		userVip.setAddIp("");
		userVip.setAddTime(new Date());
		userVip.setApr(0);
		userVip.setInvestMoney(0);
		userVip.setLastYearInvest(0);
		userVip.setLevel(0);
		userVip.setName("普通会员");
		userVip.setUpdateTime(new Date());
		
		theUserVipDao.save(userVip);
	}
}
