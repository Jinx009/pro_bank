package com.rongdu.p2psys.account.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.model.AccountCashModel;

/**
 * 提现Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月17日
 */
public interface AccountCashDao extends BaseDao<AccountCash> {

	/**
	 * 提现金额信息
	 * 
	 * @param userId
	 */
	AccountCashModel getCashMessage(long userId);

	/**
	 * 提现记录的个数
	 * 
	 * @param userId
	 * @return
	 */
	int count(long userId);

	/**
	 * 获得提现记录列表
	 * 
	 * @param userId
	 * @param start
	 * @param pernum
	 * @return
	 */
	PageDataList<AccountCash> list(long userId, int page);

	/**
	 * 更新状态
	 * 
	 * @param id
	 * @param status
	 * @param preStatus
	 */
	void updateStatus(long id, int status, int preStatus);
	/**
     * 更新状态
     * 
     * @param id
     * @param status
     */
    void updateStatus(long id, int status);
	/**
	 * 提现审核
	 * 
	 * @param cash
	 * @param preStatus
	 * @return
	 */
	int verifyCash(AccountCash cash, int preStatus);

	/**
	 * 统计等待审核的体现总数
	 * 
	 * @param status
	 * @return
	 */
	int count(int status);
	/**
	 * 统计成功提现笔数
	 * @param userId 用户ID
	 * @return 成功提现笔数
	 */
	int getSuccessAccountCash(long userId);
	
	/**
	 * 更新取现交易号
	 * @param id 
	 * @param orderNo 交易号
	 */
	void updateOrderNo(long id, String orderNo);
	
    /**
     * 投资人累计提现金额总和
     * 
     * @return
     */
    double allCashMomeny(String startTime, String endTime);

	/**
	 * 获得本月成功提现的次数
	 * @param userId
	 * @return
	 */
	public int countMonth(long userId);
	
	/**
	 * 根据时间获取提现金额
	 * @param date
	 * @return 提现金额
	 */
	public double getAccountCashSumByDate(String date);
	
	/**
	 * 获取该用户当天提现次数
	 * @param userId
	 * @return
	 */
	public int getTodayCashCountByUserId(long userId);
	
	/**
	 * 提现信息
	 * 
	 * @param orderNo
	 */
	AccountCash getCashInfo(String orderNo);

}
