package com.rongdu.p2psys.nb.user.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.nb.user.dao.UserCreditDao;
import com.rongdu.p2psys.nb.user.service.UserCreditService;
import com.rongdu.p2psys.user.domain.UserCredit;

@Service("theUserCreditService")
public class UserCreditServiceImpl implements UserCreditService
{

	@Resource
	private UserCreditDao theUserCreditDao;
	
	public UserCredit saveUserCredit(UserCredit userCredit)
	{
		return theUserCreditDao.save(userCredit);
	}

}
