package com.rongdu.p2psys.nb.account.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.exception.AccountException;
import com.rongdu.p2psys.nb.account.dao.AccountDao;

@Repository("theAccountDao")
public class AccountDaoImpl extends BaseDaoImpl<Account> implements AccountDao
{

	public Account saveAccount(Account account)
	{
		return save(account);
	}

	public void updateAccount(Account account)
	{
		update(account);
	}

	public Account getByUserId(long user_id)
	{
		return this.findObjByProperty("user.userId",user_id);
	}

	@SuppressWarnings("unchecked")
	public List<Account> getAccountListByGroupId(long groupId)
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(" select *  from rd_account ");
		buffer.append(" where user_id in  ");
		buffer.append("  ( select user_id from rd_user where bind_id =  "+groupId+"  ) ");
		buffer.append(" order by use_money desc  ");
		
		Query query = em.createNativeQuery(buffer.toString(),Account.class);
		
		return query.getResultList();
	}
	
	@Override
	public void modify(double totalVar, double useVar, double nouseVar, long userId) {
	   Account account = getByUserId(userId);
	   // 总金额
	   account.setTotal(BigDecimalUtil.add(account.getTotal(), totalVar));
	   // 可用金额
	   account.setUseMoney(BigDecimalUtil.add(account.getUseMoney(), useVar));
	   // 冻结金额
	   account.setNoUseMoney(BigDecimalUtil.add(account.getNoUseMoney(), nouseVar));
	   
       // 账户资金CHECK
       checkAccount(account);
		
	   // 更新
	   update(account);
	}

	/*
	 * 账户资金不能为负数
	 */
	public void checkAccount(Account account) {
		if(account != null){
			if(account.getTotal() < 0 ){
				throw new AccountException("账户总额有误！", 1);
			}
			if(account.getUseMoney() < 0 ){
				throw new AccountException("账户可用余额有误！", 1);
			}
			if(account.getNoUseMoney() < 0 ){
				throw new AccountException("账户冻结金额有误！", 1);
			}
			if(account.getCollection() < 0 ){
				throw new AccountException("账户待收总额有误！", 1);
			}
		}
	}

	public void update(double totalVar, double useVar, double nouseVar,double collectVar, long userId)
	{
		int count = updateByJpql(
				"UPDATE Account SET total=total+:total,useMoney=useMoney+:useMoney,noUseMoney=noUseMoney+:noUseMoney,collection=collection+:collect WHERE user.userId=:userId AND round(useMoney+:money)>=0",
				new String[] { "total", "useMoney", "noUseMoney","collect", "userId", "money" }, new Object[] { totalVar, useVar,
						nouseVar,collectVar,userId, useVar });
		if (count != 1) 
		{
			throw new AccountException("投资人资金有误！", 1);
		}
		
		Account account = getByUserId(userId);
		
		// 账户资金CHECK
		checkAccount(account);
		
		//更新缓存
		em.refresh(account);
	}

	@Override
	public double getAccountUseMoney(long userId) {
		
		String strSql = " select SUM(use_money) from rd_account where user_id in (select u1.user_id from rd_user u1 join rd_user u2 " +
						" on u1.bind_id = u2.bind_id where u2.user_id = " + userId + ") ";
	    Query  query = em.createNativeQuery(strSql);
	    Object obj = query.getSingleResult();
        if (obj != null) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
	}
}
