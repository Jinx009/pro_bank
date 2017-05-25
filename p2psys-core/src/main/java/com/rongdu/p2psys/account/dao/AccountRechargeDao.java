package com.rongdu.p2psys.account.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.AccountMoneyModel;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 充值Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月20日
 */
public interface AccountRechargeDao extends BaseDao<AccountRecharge> {

	/**
	 * 更新充值状态
	 * 
	 * @param status
	 * @param tradeNo
	 */
	int updateRechargeByStatus(int status, String returnText, String tradeNo);

	/**
	 * 根据交易订单号获取充值记录
	 * 
	 * @param tradeNo
	 * @return
	 */
	AccountRecharge getRechargeByTradeno(String tradeNo);

	/**
	 * 更新充值状态
	 * 
	 * @param status
	 * @param tradeNo
	 */
	int updateRecharge(int status, String returnText, String tradeNo);

	/**
	 * 修改充值费用问题
	 * 
	 * @param fee
	 * @param id
	 */
	void updateRechargeFee(double fee, long id);

	/**
	 * 成功充值信息
	 * 
	 * @param userId
	 * @return
	 */
	AccountRechargeModel getRechargeSummary(long userId);

	/**
	 * 统计不同类型某时间段的充值总额
	 * 
	 * @param userId 用户ID
	 * @param type 充值类型 ,如果type为-1，则为所有类型
	 * @param startTime 统计时间开始时间，如果值为-1，查询所有
	 * @param endTime 统计时间结束时间，如果值为-1，查询所有
	 * @return 充值总额
	 */
	double getLastRechargeSum(long userId, int type, long startTime, long endTime);

	/**
	 * 等待审核的充值
	 * 
	 * @param status
	 * @return
	 */
	int count(int status);
	/**
	 * 更新充值状态
	 * @param id 主键Id
	 * @param status 状态
	 */
	void updateStatus(long id, int status);
	/**
     * 更新充值状态
     * @param id 主键Id
     * @param status 目标状态
     * @param status 当前状态
     */
    void updateStatus(long id, int status,int preStatus);
    
    /**
     * 充值用户数
     * 
     * @param status
     * @return
     */
    int rechargedUserCount();
    
    /**
     * 根据时间段搜索充值用户数
     * @param startTime
     * @param endTime
     * @return
     */
    int rechargedUserCount(String startTime, String endTime);
    
    /**
     * 投资人累计充值金额总和
     * 
     * @param status
     * @return
     */
    double rechargedAllMomeny(String startTime, String endTime);
    
    /**
     * 用户充值总额
     * @return
     */
    double getRechargeTotal(long userId);

    /**
     * 用户充值总额
     * @param userId 用户id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    double getRechargeTotal(long userId, String startTime, String endTime);
    
    /**
     * 获取用户充值次数
     * @param user
     * @param status
     * @return
     */
    int getRechargeCountByUser(User user, int status);

	public AccountRechargeModel sumAmount(AccountRechargeModel model);
	
	/**
	 * 根据时间获取充值金额
	 * @param date
	 * @return 充值金额
	 */
	public double getAccountRechargeSumByDate(String date);
	double getNewRechargeTotal(String startTime, String endTime);
	double getAccessAcountMoneyTotal(String startTime, String endTime);
	PageDataList<AccountMoneyModel> getBorrowCollectionMoney2(String startTime, String endTime,int pageNo,int rowCount);
	PageDataList<AccountMoneyModel> getBorrowCollectionMoney(String startTime, String endTime,int pageNo,int rowCount);
	PageDataList<AccountMoneyModel> getPpfundCollectionMoney(String startTime, String endTime,int pageNo,int rowCount);
	PageDataList<AccountMoneyModel> getRedPacketMoney(String startTime, String endTime,int pageNo,int rowCount);
	PageDataList<AccountMoneyModel> getRecommendMoney(String startTime, String endTime,int pageNo,int rowCount);
	
	/**
	 * 根据连连返回的充值单号查询是否存在充值信息
	 * @param oid_paybill 连连钱包支付单号
	 * @return 0不存在，1已存在
	 */
	public int getInfoByLLOrder(String oid_paybill) ;
}
