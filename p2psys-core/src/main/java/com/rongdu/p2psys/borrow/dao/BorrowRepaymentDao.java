package com.rongdu.p2psys.borrow.dao;

import java.util.Date;
import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;

/**
 * 还款
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月20日
 */
public interface BorrowRepaymentDao extends BaseDao<BorrowRepayment> {

	/**
	 * 根据ID取得还款计划
	 * 
	 * @param id 主键ID
	 * @return 还款计划
	 */
	BorrowRepayment find(long id);

	/**
	 * 检查之前是否有期数未还款
	 * 
	 * @param period 期数
	 * @param borrowId 标ID
	 * @return ture：存在未还款
	 */
	boolean hasRepaymentAhead(int period, long borrowId);

	/**
	 * 根据用户ID获取借款总额
	 * 
	 * @param userId 用户ID
	 * @return 借款总额
	 */
	double getUserBorrowTotal(long userId);
	/**
	 * 根据用户ID获取还款总额
	 * @param userId 用户ID
	 * @return 待还款总额
	 */
	double getUserBorrowRepayTotal(long userId);

	/**
	 * 修改还款状态
     * 
     * @param repayId 主键ID
	 * @param status 需要更改的状态
	 * @param status_ 更改前的状态
	 */
	void updateStatus(int status, int status_, long repayId);

	/**
	 * 修改还款计划的已还数据
	 * 
	 * @param repay 还款计划
	 */
	void updateBorrowRepaymentByStatus(BorrowRepayment repay);

	/**
	 * 根据标ID和期数取得还款计划
	 * 
	 * @param borrowId 标ID
	 * @param period 期数
	 * @return 还款计划
	 */
	BorrowRepayment find(long borrowId, int period);

	/**
	 * 计算剩余待还本金
	 * @param borrowId
	 * @return
	 */
	public double getRemainderCapital(long borrowId);

	/**
	 * 本次提前还款待还利息总和
	 * @param id
	 * @param period
	 * @return
	 */
	public double getwaitRpayInterest(long borrowId, int period);

	/**
	 * 原始待还利息总额
	 * @param id
	 * @return
	 */
	public double getRemainderInterest(long borrowId);
	
	/**
	 * 获取待还的统计信息
	 * @param userId 用户ID
	 * @return 待还的统计信息
	 */
	BorrowRepaymentModel getRepayStatistics(long userId);
	
	/**
	 * 统计某个月份的待还金额
	 * @param month 要统计的那个月份
	 * @param userId 用户ID
	 * @return 当月待还金额
	 */
	double getRepayByMonth(int month, long userId);
	/**
	 *最近一笔待还信息
	 * @param userId 用户ID
	 * @return 最近一笔待还信息
	 */
	BorrowRepayment getNextRepayByUserId(long userId);
	/**
	 * 统计下一个待还日期的待还次数及待还金额
	 * @param bm 待还model
	 * @param nextRepayTime 下一个待还日期
	 * @param userId 用户ID
	 * @return 待还
	 */
	BorrowRepaymentModel getBRMByCollectTime(BorrowRepaymentModel bm, Date nextRepayTime, long userId);

	/**
	 * 提前还款更新status和webstatus
	 * @param borrowRepayment
	 */
	public void updateBorrowRepaymentStatusAndWebStatus(BorrowRepayment borrowRepayment);

	/**
	 * 处理逾期垫付的状态才2更新到1
	 * @param borrowRepayment
	 */
	public void updateBorrowRepaymentStatus(BorrowRepayment borrowRepayment);

	/**
	 * 获得没有还款的利息
	 * @param period
	 * @return
	 */
	public double getWaitInterest(long borrowId, int period);

	/**
     * 待还信息查询
     */
    PageDataList<BorrowRepayment> getList(BorrowRepaymentModel model);
    
    /**
     * 获得当前标需要还的还款期数
     * @param period
     * @return
     */
    public int getCurrPeriod(long borrowId);
    
    /**
     * 获取当前担保公司逾期项目个数
     */
    int getOverdueCount(long userId);
    
    /**
     * 获取当前担保公司正在担保的标的信息列表
     * @param userId
     * @return 标的总信息
     */
    List<BorrowRepayment> getGuaranteeingList(String userId);     
    
    /**
     * 获取当前担保公司要催收的标的信息列表
     * @param userId
     * @return 标的总信息
     */
    List<BorrowRepayment> getUrgeGuaranteeList(String userId);    
    
    /**
     * 获取当前担保公司逾期的标的信息列表
     * @param userId
     * @return 标的总信息
     */
    PageDataList<BorrowRepayment> getOverdueGuaranteeList(BorrowModel model); 
    
    /**
     * 获取当前担保公司已经代偿的标的信息列表
     * @param userId
     * @return 标的总信息
     */
    PageDataList<BorrowRepayment> getCompensatedList(BorrowModel model);    
    
    /**
     * 根据标id统计当前标的所有还款金额
     * @param borrowId
     * @param status
     * @return
     */
    public double getSumRepayAccount(long borrowId,int status);
    
    /**
     * 定时算逾期
     */
    public List<BorrowRepayment> doLate();

	public List<BorrowRepayment> middleReapy();
	
	/**
	 * 根据时间和状态获取总额
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
	 * 获取自动还款列表
	 * 
	 * @return
	 */
	List<BorrowRepayment> autoRepayList();
}
