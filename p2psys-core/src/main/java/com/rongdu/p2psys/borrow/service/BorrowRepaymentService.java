package com.rongdu.p2psys.borrow.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.domain.User;

/**
 * @author sj
 * @version 2.0
 * @since 2014年2月20日
 */
public interface BorrowRepaymentService {
	/**
	 * 还款明细
	 * 
	 * @param model
	 * @return List<BorrowRepaymentModel>
	 */
	PageDataList<BorrowRepaymentModel> list(BorrowRepaymentModel model, String searchName);
	
	/**
     * 前台还款明细
     * 
     * @param model
     * @return List<BorrowRepaymentModel>
     */
    PageDataList<BorrowRepaymentModel> getList(BorrowRepaymentModel model);

	/**
	 * 获取BorrowRepayment实体
	 * 
	 * @param repay_id
	 * @return BorrowRepayment
	 */
	BorrowRepayment findById(long repay_id);
	/**
	 * 根据用户ID获取还款总额
	 * @param userId 用户ID
	 * @return 待还款总额
	 */
	double getUserBorrowRepayTotal(long userId);
    
	/**
	 * 逾期垫付
	 * @param borrowRepayment
	 */
	public void overdue(BorrowRepayment borrowRepayment, Operator operator);
	
	/**
	 * 还款统计
	 * @param userId 用户ID
	 * @return 还款统计
	 */
	BorrowRepaymentModel getReapyStatistics(long userId);
	
	/**
     * 计算剩余待还本金
     * @param borrowId
     * @return
     */
    public double getRemainderCapital(long borrowId);
    
    /**
     * 原始待还利息总额
     * @param id
     * @return
     */
    public double getRemainderInterest(long borrowId);

    /**
     * 逾期中的借款
     * @param user
     * @return 逾期标集合
     */
    List<BorrowRepayment> fingOverDueBorrowRepayment(User user);
    
    /**
     * 根据标ID查询还款计划
     * @param borrowId
     * @return
     */
    List<BorrowRepayment> getRepaymentByBorrowId(long borrowId);

    /**
     * 统计今日待还个数
     * @return
     */
    int count();
    
    /**
     * 获得当前标需要还的还款期数
     * @param period
     * @return
     */
    public int getCurrPeriod(long borrowId);
    
    /**
     * 还款提醒
     */
    void doRepaymentNotice();

    /**
     * 获取当前担保公司催收项目个数
     */
    int getUrgeCount(long userId);

    /**
     * 催收项目列表
     * @param model
     * @return
     */
    PageDataList<BorrowRepaymentModel> getCollectionList(BorrowModel model);
    
    /**
     * 定时算逾期
     */
    void doLate();

	/**
	 * 待收信息列表(浮动收益类产品)
	 * @param model
	 * @param searchName
	 * @return
	 */
	public PageDataList<BorrowRepaymentModel> repaymentEntrustList(
			BorrowRepaymentModel model, String searchName);
	
	/**
	 * 根据时间获取待还款总额
	 * @param date
	 * @return
	 */
	double getRepaymentNoByDate(String date);
	
	/**
	 * 根据时间获取已还款总额
	 * @param date
	 * @return
	 */
	double getRepaymentYesByDate(String date);
	
	/**
	 * 自动还款
	 */
	void doAutoRepay();
}
