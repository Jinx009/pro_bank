package com.rongdu.p2psys.borrow.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowCollectionModel;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 待收Service
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月26日15:31:10
 */
public interface BorrowCollectionService {

	void save(List<BorrowCollection> list);

	/**
	 * 已赚利息
	 * 
	 * @param userId
	 * @return
	 */
	double getReceivedInterestSum(long userId);

	/**
	 * 根据标获取用户的待收
	 * 
	 * @param userId
	 * @param borrowId
	 * @return
	 */
	List<BorrowCollection> getListByUserAndBorrow(BorrowTender tender);

	/**
	 * 分页
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<BorrowCollectionModel> list(BorrowCollectionModel model);
	
	/**
	 * 获取下一个待收
	 * @param userId 用户ID
	 * @return 待收信息
	 */
	BorrowCollection getNextCollectionByUserId(long userId);
	/**
	 * 根据用户id和状态获取待收状况
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
     * 
     * @param user
     * @return
     */
    public List<BorrowCollectionModel> investCollectionList(User user);

    /**
     * 累计净收益
     * @param user
     * @return
     */
    public double accumulatedNetIncome(User user);
    
    /**
     * 累计净收益（多身份 累计净收益）
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
     * @return 待收利息总额
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
     * 用户月份代收时间统计
     * @param user 信息
     * @return
     */
    List<String> getCollectionDate(User user);
    
    /**
     * 为用户赚取收益
     * @return
     */
    double sumInterest();
    
    /**
     * 统计单条投标记录利息管理费
     * @param t
     * @return
     */
    double sumInterestFeeByTender(BorrowTender t);
    
	/**
     * 借款标回款计划
     * @param model
     * @return
     */
	PageDataList<BorrowCollectionModel> getRepayPlanByModel(
			BorrowTenderModel model);

	/**
	 * 获取当前借款标回款计划
	 * @param model
	 * @return
	 */
	PageDataList<BorrowCollectionModel> getCurrentRepayPlanByModel(BorrowTenderModel model);
	
	/**
	 * 根据tender_id获取所有待收记录利息，本金和
	 * @param tenderId
	 * @return
	 */
	public Object[] getBorrowCollectionList(long tenderId);
	
	/**
	 * 根据还款时间段统计实际还款利息
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public double getRepaymentYesInterest(String startTime, String endTime);
}
