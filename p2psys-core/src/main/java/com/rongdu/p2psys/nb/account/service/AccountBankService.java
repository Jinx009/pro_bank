package com.rongdu.p2psys.nb.account.service;

import java.util.List;

import com.rongdu.p2psys.account.domain.AccountBank;

public interface AccountBankService
{
	public AccountBank saveAccountBank(AccountBank accountBank);
	
	public List<AccountBank> getByUserId(long user_id);
	
	/**
	 * 提现银行卡列表
	 * 
	 * @return
	 */
	List<AccountBank> list(long userId);
	
	/**
	 * 通过ID获得银行卡信息
	 * 
	 * @param id
	 * @return
	 */
	AccountBank findById(long id);
	
	/**
	 * 统计用户银行卡数量
	 * 
	 * @param userId
	 * @return
	 */
	int count(long userId);
	
	/**
	 * 更新银行卡信息
	 * 
	 * @param bank
	 * @return
	 */
	AccountBank update(AccountBank bank);
	
	/**
	 * 删除/禁用
	 * 
	 * @param userId
	 * @param id
	 */
	String disable(long userId, long id);
	
	List<AccountBank> list(long userId,String channelKey);
	
	/**
	 * 通过银行账户和用户ID获得AccountCashModel
	 * 
	 * @param userId
	 * @param account
	 * @param channlKey
	 * @return
	 */
	AccountBank find(long userId, String account,String channlKey);
	
	/**
	 * 判断是否已存在银行卡
	 * 
	 * @param bankNo
	 * @return
	 */
	AccountBank findByBankNo(String bankNo);
}
