package com.rongdu.p2psys.nb.account.service;

import java.util.List;

import com.rongdu.p2psys.account.domain.Account;

public interface AccountService
{
	public void updateAccount(Account account);
	
	public Account save(Account account);
	
	public Account getAccountByUserId(long user_id);
	
	public List<Account> getAccountListByGroupId(long groupId);
	
	void updateAccount(double totalVar, double useVar, double nouseVar, long userId);
	
	/**
	 * 获取用户可以金额
	 * 
	 * @param userId
	 * @return
	 */
	double getAccountUseMoney(long userId);
}
