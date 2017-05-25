package com.rongdu.p2psys.nb.user.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.user.dao.UserCacheDao;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.model.UserCacheModel;

@Service("theUserCacheService")
public class UserCacheServiceImpl implements UserCacheService
{
	@Resource
	private UserCacheDao theUserCacheDao;
	@Resource
	private UserService theUserService;

	public void updateUserCache(UserCache userCache)
	{
		theUserCacheDao.update(userCache);
	}

	public UserCache saveUserCache(UserCache userCache)
	{
		return theUserCacheDao.save(userCache);
	}

	public UserCache saveWechatUserCache(UserCache userCache)
	{
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
		
		return theUserCacheDao.save(userCache);
	}

	public UserCache getById(long id)
	{
		return theUserCacheDao.find(id);
	}

	@Override
	public boolean doLock(HttpServletRequest request, long userId, String type)
	{
		if (UserCacheModel.PWD_LOCK.equals(type))
		{
			return doPwdLock(request, userId);
		}
		if (UserCacheModel.PAY_PWD_LOCK.equals(type))
		{
			return doPayPwdLock(request, userId);
		}
		return false;
	}
	
	public boolean doPwdLock(HttpServletRequest request, long userId)
	{
		User user = theUserService.getByUserId(userId);
		UserCache uc = user.getUserCache();

		long timeRange = 120; // 连续输入错误时间范围
		int failMaxTimes = 5; // 最大错误次数
		int failTimes = uc.getLoginFailTimes(); // 已经错误次数
		String sessionName = user.getUserName() + "_pwd";
		if (request.getSession().getAttribute(sessionName) == null)
		{
			request.getSession().setAttribute(sessionName,
					System.currentTimeMillis());
		}
		long firstTime = (Long) request.getSession().getAttribute(sessionName);
		if ((System.currentTimeMillis() - firstTime < timeRange * 1000)
				&& (failTimes + 1 == failMaxTimes))
		{
			uc.setLoginFailTimes(failTimes + 1);
			uc.setLockTime(new Date());
			uc.setLoginPwdStatus(1);
			theUserCacheDao.update(uc);
			return true;
		} else if (System.currentTimeMillis() - firstTime > timeRange * 1000)
		{
			uc.setLoginFailTimes(1);
			uc.setLoginPwdStatus(0);
			request.getSession().setAttribute(sessionName,
					System.currentTimeMillis());
		} else
		{
			uc.setLoginFailTimes(uc.getLoginFailTimes() + 1);
		}
		theUserCacheDao.update(uc);
		return false;
	}
	

	public boolean doPayPwdLock(HttpServletRequest request, long userId)
	{
		User user = theUserService.getByUserId(userId);
		UserCache uc = user.getUserCache();

		long timeRange = 120; // 连续输入错误时间范围
		int failMaxTimes = 5; // 最大错误次数
		int failTimes = uc.getPayFailTimes(); // 已经错误次数
		String sessionName = user.getUserName() + "_payPwd";
		if (request.getSession().getAttribute(sessionName) == null)
		{
			request.getSession().setAttribute(sessionName,
					System.currentTimeMillis());
		}
		long firstTime = (Long) request.getSession().getAttribute(sessionName);
		if ((System.currentTimeMillis() - firstTime < timeRange * 1000)
				&& (failTimes + 1 == failMaxTimes))
		{
			uc.setPayFailTimes(failTimes + 1);
			uc.setLockPayTime(new Date());
			uc.setPayPwdStatus(1);
			theUserCacheDao.update(uc);
			return true;
		} else if (System.currentTimeMillis() - firstTime > timeRange * 1000)
		{
			uc.setPayFailTimes(1);
			uc.setPayPwdStatus(0);
			request.getSession().setAttribute(sessionName,
					System.currentTimeMillis());
		} else
		{
			uc.setPayFailTimes(uc.getPayFailTimes() + 1);
		}
		theUserCacheDao.update(uc);
		return false;
	}

	@Override
	public UserCache getByUserId(long userId) {
		return theUserCacheDao.loadUserCache(userId);
	}
}
