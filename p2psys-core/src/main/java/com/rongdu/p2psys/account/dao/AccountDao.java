package com.rongdu.p2psys.account.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 用户账户Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月30日
 */
public interface AccountDao extends BaseDao<Account> {

	/**
	 * 用户账户列表
	 * 
	 * @param account
	 * @return
	 */
	PageDataList<AccountModel> list(AccountModel model);
	
	/**
	 * 用户账户列表
	 * 
	 * @param account
	 * @return
	 */
	PageDataList<AccountModel> userList(AccountModel model);
	
	/**
	 * 用户账户列表
	 * 
	 * @param account
	 * @return
	 */
	PageDataList<AccountModel> exportUserList(AccountModel model);

	/**
	 * @param userId
	 * @return
	 */
	Account getAccountByUserId(long userId);
	
	
	/**
	 * @param userId
	 * @return
	 */
	List<Account> getGroupAccountListByUserId(long userId);
		

	/**
	 * 更新用户的账户信息
	 * 
	 * @param act
	 */
	void modify(Account act);

	/**
	 * 更新用户的账户信息，防止数据库幻读
	 * 
	 * @param act
	 */
	void modify(double totalVar, double useVar, double nouseVar, long userId);

	/**
	 * @param totalVar
	 * @param useVar
	 * @param nouseVar
	 * @param collectVar
	 * @param userId
	 */
	void modify(double totalVar, double useVar, double nouseVar, double collectVar, long userId);
	
	void modify(double totalVar, double useVar, double nouseVar, double collectVar,double repay,long userId);

	/**
	 * 获取用户可以金额
	 * 
	 * @param userId
	 * @return
	 */
	double getAccountUseMoney(long userId);
	
    /**
     * 投资人账户可用余额总和
     * 
     * @param userId
     * @return
     */
    double getAllUseMoney();
    
    /**
     * 获取投资人账户总额
     * 
     * @return
     */
    double getAllTotal();
    
    void update(double totalVar, double useVar, double nouseVar, double collectVar, long userId);

    /**
     * 平台资金汇总
     * @return
     */
    Object[] financialStatistics();
    /**
     * 关联账户
     * @return
     */
    public List<Account> findListAccount(double user_id);
    /**
     * 关联账户总额
     * @return
     */
    public double getSumAccount(long user_id);
    
    /**
	 * 导出列表
	 * 
	 * @param account
	 * @return
	 */
	PageDataList<AccountModel> exportList(AccountModel model);
}
