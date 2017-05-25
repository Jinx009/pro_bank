package com.rongdu.p2psys.borrow.dao;

import java.util.Date;
import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowCollectionModel;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 待收Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月10日
 */
public interface BorrowCollectionDao extends BaseDao<BorrowCollection> {

	/**
	 * 已赚利息
	 * 
	 * @param userId
	 * @return
	 */
	double getReceivedInterestSum(long userId);

	/**
	 * 列表
	 * 
	 * @param borrow_id
	 * @param period
	 * @return
	 */
	List<BorrowCollection> list(long borrowId, int period);
	
	/**
	 * vip还款列表
	 * @param borrow_id
	 * @param period
	 * @return
	 */
	List<BorrowCollection> vipRepayList();
	
	/**
	 * 列表
	 * 
	 * @param borrow_id
	 * @param period
	 * @return
	 */
	List<BorrowCollection> list(long borrowId, int period, int status);	

	/**
	 * 计算投资人剩余待还本金
	 * @param tenderId
	 * @return
	 */
	public double getRemainderCapital(long tenderId);

	/**
	 * 所有待收利息
	 * @param tenderId
	 * @return
	 */
	public double getRemainderInterest(long tenderId);
	
    /**
     * 当期所有待收利息
     * @param borrowId
     * @param period
     * @return
     */
    public double getInterestByBorrowAndPeriod(long borrowId, int period);	

	/**
	 * 下期的待收利息
	 * @param id
	 * @param i
	 * @return
	 */
	public BorrowCollection getCollectionByTenderAndPeriod(long tenderId, int period);
	
	/**
	 * 获取下一个待收
	 * @param userId 用户ID
	 * @return 待收信息
	 */
	BorrowCollection getNextCollectionByUserId(long userId);
	/**
	 * 待收笔数
	 * @param userId 用户ID
	 * @return 待收笔数
	 */
	int countCollect(long userId,int status);
	
	/**
	 * 获取待收的统计信息
	 * @param userId 用户ID
	 * @return 待收的统计信息
	 */
	BorrowCollectionModel getCollectStatistics(long userId);
	
	/**
	 * 统计某个月份的待收金额
	 * @param month 要统计的那个月份
	 * @param userId 用户ID
	 * @return 当月待收金额
	 */
	double getCollectByMonth(int month, long userId);
	/**
	 * 统计下一个待收日期的待收次数及待收金额
	 * @param bm 待收model
	 * @param nextCollectTime 下一个待收日期
	 * @param userId 用户ID
	 * @return  待收
	 */
	BorrowCollectionModel getBCMByCollectTime(BorrowCollectionModel bm, Date nextCollectTime, long userId);

	/**
	 * 获得提前还款剩余还款的本金
	 * @param borrowId
	 * @return
	 */
	public double getRemainderMoney(long borrowId);

	/**
	 * 更新投资人的待收状态
	 * @param borrowId
	 */
	public void updatePriorRepayStatus(long borrowId);

    /**
     * 累计净收益
     * @param user
     * @return
     */
    public double accumulatedNetIncome(User user);
    
    
    /**
     * 累计净收益（多身份）
     * @param user
     * @return
     */
    public double netProfit(User user);

    /**
     * 在投金额
     * @param user
     * @return
     */
    public double inInvestAmount(User user, int status);

    /**
     * 今日收益
     * @param user
     * @return
     */
    public double sumTodayInterest(User user);
    /**
     * 根据用户获取待收利息总额
     * @param user
     * @return 待收利息总额（待收利息-转出利息+加息券待收利息）
     */
    double getInterestByUser(User user);
    /**
     * 根据用户和时间获取待收利息总额
     * @param user 用户 date 时间
     * @return 待收利息日期和金额
     */
    List<Object[]> getInterestByUserAndDate(User user, String date);
    /**
     * 根据用户获取待收本金总额
     * @param user
     * @return 待收本金总额
     */
    double getCapitalByUser(User user);
    /**
     * 根据用户和时间获取待收本金总额
     * @param user 用户 date 时间
     * @return 待收本金日期和金额
     */
    List<Object[]> getCapitalByUserAndDate(User user, String date);
    /**
     * 待收日期集合
     * @param user 用户 
     * @return 待收日期集合
     */
    List<String> getCollectionDate(User user);
    
    /**
     * 查询用户中心收款列表
     * @param user
     * @return
     */
    List<BorrowCollection> getMemberCollectionList(User user);
    
    /**
     * 待收信息查询
     */
    PageDataList<BorrowCollection> getList(BorrowCollectionModel model);
    
    /**
     * 为用户赚取收益
     * @return
     */
    double sumInterest();
    
   /**
    * 计算本次投标含本期还款的剩余待还本金
    * @param tenderId 投资ID
    * @param userId 用户ID
    * @param period 期数
    * @return
    */
    double remainderCapital(long tenderId, long userId, int period);
    
    /**
     * 计算本次投标含本期还款的剩余待还利息
     * @param tenderId 投资ID
     * @param userId 用户ID
     * @param period 期数
     * @return
     */
    double remainderInterest(long tenderId, long userId, int period);

    /**
     * 根据标ID和期数查找总本金和总利息
     * @param id
     * @param i
     * @return
     */
	Object[] getCapitalAndInterestByBorrowAndPeriod(long id, int i);
	/**
	 * 更新最后一次投资待收利息
	 * @param id
	 * @param difference
	 */
	void updateInterest(long id, double difference);
	
	/**
	 * 获得加息劵获得的利息
	 * @param tenderId
	 * @return
	 */
	public double sumInterestRate(long tenderId);
	
	/**
	 * 获取下一个待收
	 * @param borrowId 标ID
	 * @param userId 用户ID
	 * @return 待收信息
	 */
	BorrowCollection getNextCollectionByBorrowId(long borrowId, long userId);
	
	/**
	 * 获取最后还款日
	 * @param borrowId 标ID
	 * @return 待收信息
	 */
	Date getLastRepayDateByBorrowId(long borrowId);
	
	/**
	 * 标的还款是否有逾期
	 * @param borrowId
	 * @return
	 */
	boolean isLatedByBorrowId(long borrowId);
	
	double sumInterestFeeByTender(BorrowTender t);
	
	/**
	 * 根据tender_id获取所有待收记录
	 * 根据时间升序
	 * @param tenderId
	 * @return
	 */
	public Object[] getBorrowCollectionList(long tenderId);
	
	/**
	 * 根据tenderId和userId查找本次待收总投资
	 * @param tenderId
	 * @param userId
	 * @return
	 */
	double getTotalCapitalByTenderId(long tenderId, long userId);

	/**
	 * 获取当前借款标回款计划
	 * @param model
	 * @return
	 */
	List<BorrowCollection> getCurrentRepayPlanByModel(BorrowTenderModel model);
	
	/**
	 * 根据还款时间段统计实际还款利息
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public double getRepaymentYesInterest(String startTime, String endTime);

	public BorrowCollection getBorrowCollectionByTenderId(long id);
}
