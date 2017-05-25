package com.rongdu.p2psys.nb.user.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.util.RandomUtil;
import com.rongdu.p2psys.nb.user.dao.UserPromotDao;
import com.rongdu.p2psys.nb.user.service.UserPromotService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserPromot;

@Service("theUserPromotService")
public class UserPromotServiceImpl implements UserPromotService
{
	@Resource
	private UserPromotDao theUserPromotDao;

	public void updateUserPromot(UserPromot userPromot)
	{
		theUserPromotDao.update(userPromot);
	}

	public UserPromot saveUserPromot(UserPromot userPromot)
	{
		userPromot.setCouponCode(generateCouponCode(4));
		
		return theUserPromotDao.save(userPromot);
	}

	public UserPromot getUserPromotByCode(String code)
	{
		return theUserPromotDao.getUserPromotByCode(code);
	}
	
	private String generateCouponCode(int length)
	{
		String result = "";
		do
		{
			result = RandomUtil.getSpecialRandomCode(length);
		}
		while(hasDuplicateCode(result));
		return result;
	}

	public boolean hasDuplicateCode(String code)
	{
		return theUserPromotDao.alreadyHaveCode(code);
	}

	public void checkUserPromot(User mainUser)
	{
		String hql = " from UserPromot where user.userId = "+mainUser.getUserId()+"  ";
		
		UserPromot userPromot = theUserPromotDao.findByHql(hql);
		
		if(null!=userPromot)
		{
			if(null==userPromot.getCouponCode()||"".equals(userPromot.getCouponCode()))
			{
				userPromot.setCouponCode(generateCouponCode(4));
				
				theUserPromotDao.update(userPromot);
			}
		}
		else
		{
			userPromot = new UserPromot();
			
			userPromot.setUser(mainUser);
			
			saveWechatUserPromot(userPromot);
		}
	}

	public UserPromot saveWechatUserPromot(UserPromot userPromot)
	{
		userPromot.setCouponCode(generateCouponCode(4));
		userPromot.setAddTime(new Date());
		userPromot.setUsedTimes(0);
		userPromot.setCanUseTimes(9999);
		userPromot.setRate(0);
		userPromot.setStatus(1);
		
		return theUserPromotDao.save(userPromot);
	}

	public UserPromot findUserPromotByUserId(long userId)
	{
		String hql = " from UserPromot where user.userId = "+userId+"  ";
		
		return theUserPromotDao.findByHql(hql);
	}

}
