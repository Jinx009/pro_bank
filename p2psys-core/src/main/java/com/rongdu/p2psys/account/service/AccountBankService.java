package com.rongdu.p2psys.account.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.model.AccountBankModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 银行卡Service
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月17日
 */
public interface AccountBankService {

	/**
	 * 提现银行卡列表
	 * 
	 * @return
	 */
	List<AccountBank> list(long userId);
	
	List<AccountBank> list(long userId,String channelKey);

	/**
	 * 我的银行卡列表
	 * 
	 * @param userId
	 * @return
	 */
	List<AccountBank> listAll(long userId);

	/**
	 * 添加
	 * 
	 * @param bank
	 */
	AccountBank save(AccountBank bank);

	/**
	 * 删除/禁用
	 * 
	 * @param userId
	 * @param id
	 */
	String disable(long userId, long id);

	/**
	 * 删除/禁用
	 * 
	 * @param id
	 * @return
	 */
	String disable(long id);

	/**
	 * 统计用户银行卡数量
	 * 
	 * @param userId
	 * @return
	 */
	int count(long userId);

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
	 * 用户银行卡列表
	 * 
	 * @param accountBank
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageDataList<AccountBankModel> accountBankList(AccountBankModel model, int pageNumber, int pageSize);

	/**
	 * 查询易极付可提现银行
	 * 
	 * @param param 过滤条件
	 * @return 可提现银行
	 */
	@SuppressWarnings("rawtypes")
	List getYjfDrawBankBySearchParam(QueryParam param);

	/**
	 * 更新银行卡信息
	 * 
	 * @param bank
	 * @return
	 */
	AccountBank update(AccountBank bank);

	/**
	 * 判断是否已存在银行卡
	 * 
	 * @param bankNo
	 * @return
	 */
	AccountBank findByBankNo(String bankNo);
	
	public AccountBank findByBankNoAndUserid(String bankNo, User user);
	
	/**
	 * 通过ID获得AccountCashModel
	 * 
	 * @param id
	 * @return
	 */
	AccountBank findById(long id);
}
