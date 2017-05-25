package com.rongdu.p2psys.account.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.account.domain.AccountBank;

/**
 * 银行卡Dao
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月6日
 */
public interface AccountBankDao extends BaseDao<AccountBank> {

	/**
	 * 提现银行卡列表
	 * 
	 * @param userId 用户ID
	 * @return 提现银行卡列表
	 */
	List<AccountBank> list(long userId);
	
	/**
	 * 提现银行卡列表
	 * 
	 * @param userId 用户ID
	 * @param channelKey 通道key
	 * @return 提现银行卡列表
	 */
	List<AccountBank> list(long userId,String channelKey);

	/**
	 * 我的银行卡列表
	 * 
	 * @param userId 用户ID
	 * @return 银行卡列表
	 */
	List<AccountBank> listAll(long userId);

	/**
	 * 统计用户银行卡数量
	 * 
	 * @param userId 用户ID
	 * @return 银行卡数量
	 */
	int count(long userId);

	/**
	 * 删除/禁用
	 * 
	 * @param userId 用户ID
	 * @param id ID
	 */
	void disable(long userId, long id);

	/**
	 * 通过银行账户和用户ID获得AccountBankModel
	 * 
	 * @param userId 用户ID
	 * @param account 银行账户
	 * @param channlKey 通道key
	 * @return AccountBank
	 */
	AccountBank find(long userId, String account,String channlKey);
	
	/**
	 * 通过用户银行卡后四位和通道获取该条信息
	 * @param userId 用户ID
	 * @param hidcard 银行卡后四位
	 * @param channlKey 通道key
	 * @return AccountBank
	 */
	AccountBank findByDDY(long userId, String hidcard,String channlKey);
	
	/**
	 * 通过银行卡后四位和银行卡所属银行获取该条信息
	 * @param userId 用户ID
	 * @param hidcard 银行卡后四位
	 * @param bank 银行所属银行
	 * @param channlKey 通道key
	 * @return AccountBank
	 */
	AccountBank find(long userId,String bank, String hidcard,String channlKey);
	
	/**
	 * 通过ID获得AccountCashModel
	 * 
	 * @param id
	 * @return
	 */
	AccountBank findById(long id);

}
