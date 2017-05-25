package com.rongdu.p2psys.account.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.Pay;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.tpp.yjf.model.RechargeModel;

/**
 * 用户账户Servcie
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月30日
 */
public interface AccountService {

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
	 * 根据用户查询
	 * 
	 * @param userId
	 * @return
	 */
	Account findByUser(long userId);

	/**
	 * 用户待收金额信息
	 * 
	 * @param userId
	 * @return
	 */
	AccountModel getUserCollectionAccount(long userId) throws Exception;

	/**
	 * 更新用户的账户信息
	 * 
	 * @param act
	 */
	void updateAccount(Account act);

	/**
	 * 获取用户可以金额
	 * 
	 * @param userId
	 * @return
	 */
	double getAccountUseMoney(long userId);

	/**
	 * 更新充值记录,并将充值金额到账
	 * 
	 * @param r
	 * @param log
	 */
	void newRecharge(String orderId, String returnText, Pay pay);

	/**
	 * 充值失败
	 * 
	 * @param orderId
	 * @param returnText
	 */
	void failRecharge(String orderId, String returnText);

	/**
	 * 通过userId获得AccountModel
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	AccountModel getAccount(long userId);
	/**
	 * 处理充值回调
	 * @param rechargeModel 充值
	 * @param accountLog 资金日志
	 */
	void doRechargeTask(RechargeModel rechargeModel);
	/**
	 * 充值失败
	 * @param rechargeModel 充值
	 * @param accountLog 资金日志
	 * @param params 其它参数
	 */
	void failRecharge(RechargeModel rechargeModel);

	void newRecharge(RechargeModel rem);
	
	 /**
     * 投资人账户可用余额总和
     * 
     * @param userId
     * @return
     */
    double getAllUseMoney();
    
    /**
     * 获取用户账户总额
     * */
    public double getSumAccount(long user_id);
    
    /**
     * 投资人账户总额
     * 
     * @return
     */
    double getAllTotal();
    
    /**
	 * 导出用户账户列表
	 * 
	 * @param account
	 * @return
	 */
	PageDataList<AccountModel> exportList(AccountModel model);

}
