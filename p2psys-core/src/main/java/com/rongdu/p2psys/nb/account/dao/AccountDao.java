package com.rongdu.p2psys.nb.account.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.account.domain.Account;

public interface AccountDao extends BaseDao<Account>
{
	/**
	 * 保存account
	 * 
	 * @param account
	 * @return
	 */
	public Account saveAccount(Account account);
	
	/**
	 * 更新account
	 * 
	 * @param account
	 */
	public void updateAccount(Account account);
	
	/**
	 * 通过user_id获取account
	 * 
	 * @param user_id
	 * @return
	 */
	public Account getByUserId(long user_id);
	
	/**
	 * 通过groupId查找账户中心
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Account> getAccountListByGroupId(long groupId);
	
	/**
	 * 更新用户的账户信息，防止数据库幻读
	 * 
	 * @param act
	 */
	void modify(double totalVar, double useVar, double nouseVar, long userId);
	
	public void update(double totalVar, double useVar, double nouseVar,double collectVar, long userId);
	
	/**
	 * 获取用户可以金额
	 * 
	 * @param userId
	 * @return
	 */
	double getAccountUseMoney(long userId);
}
