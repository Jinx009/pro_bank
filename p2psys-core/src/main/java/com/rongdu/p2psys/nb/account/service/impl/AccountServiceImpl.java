package com.rongdu.p2psys.nb.account.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.nb.account.dao.AccountDao;
import com.rongdu.p2psys.nb.account.service.AccountService;

@Service("theAccountService")
public class AccountServiceImpl implements AccountService
{
	@Resource
	private AccountDao theAccountDao;
	
	public void updateAccount(Account account)
	{
		theAccountDao.update(account);
	}

	public Account save(Account account)
	{
		return theAccountDao.save(account);
	}

	public Account getAccountByUserId(long user_id)
	{
		return theAccountDao.getByUserId(user_id);
	}

	public List<Account> getAccountListByGroupId(long groupId)
	{
		return theAccountDao.getAccountListByGroupId(groupId);
	}
	
	public void updateAccount(double totalVar, double useVar, double nouseVar, long userId) {
		theAccountDao.modify(totalVar,useVar,nouseVar,userId);
	}

	public double getAccountUseMoney(long userId) {
		return theAccountDao.getAccountUseMoney(userId);
	}
}
