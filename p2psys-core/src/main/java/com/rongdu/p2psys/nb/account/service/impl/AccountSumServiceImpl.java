package com.rongdu.p2psys.nb.account.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.nb.account.dao.AccountSumDao;
import com.rongdu.p2psys.nb.account.service.AccountSumService;

@Service("theAccountSumService")
public class AccountSumServiceImpl implements AccountSumService
{

	@Resource
	private AccountSumDao theAccountSumDao;
	
	public AccountSum saveAccountSum(AccountSum accountSum)
	{
		return theAccountSumDao.save(accountSum);
	}

}
