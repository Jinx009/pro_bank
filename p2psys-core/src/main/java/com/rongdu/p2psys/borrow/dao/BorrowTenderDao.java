package com.rongdu.p2psys.borrow.dao;

import java.util.Date;
import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.model.NewTenderModel;
import com.rongdu.p2psys.core.model.RankModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 投标DAO接口
 * 
 * @author：Administrator
 * @version 2.0
 * @since 2014年7月14日
 */
public interface BorrowTenderDao extends BaseDao<BorrowTender> {

	/**
	 * 获得我的投资列表
	 * 
	 * @param borrowModel
	 * @return
	 */
	PageDataList<BorrowTenderModel> list(BorrowTenderModel borrowTenderModel);

	/**
	 * 修改tender的待收本金和待收利息
	 * 
	 * @param capital
	 * @param interest
	 * @param id
	 */
	void updateRepayTender(double capital, double interest, long id);

	/**
	 * 查改用户是否已经投资此借款标
	 * 
	 * @param borrowId
	 * @param userId
	 * @return
	 */
	double hasTenderTotalPerBorrowByUserid(long borrowId, long userId);

	/**
	 * 根据borrowId获取tender的列表
	 * 
	 * @param borrowId
	 * @return
	 */
	PageDataList<BorrowTenderModel> list(long borrowId, int page, int size);

	/**
	 * 修改借款标
	 * 
	 * @param tender
	 * @return
	 */
	BorrowTender modifyBorrowTender(BorrowTender tender);

	/**
	 * 最新借款
	 * 
	 * @return
	 */
	List<NewTenderModel> getNewTenderList();

	/**
	 * 投资排行榜
	 * 
	 * @return
	 */
	List<RankModel> getRankList();

	/**
	 * @param tender
	 * @return
	 */
	BorrowTender addBorrowTender(BorrowTender tender);

	/**
	 * @param id
	 * @param start
	 * @param pernum
	 * @return
	 */
	PageDataList<BorrowTenderModel> getTenderListByBorrowid(long id, int start, int pernum);

	/**
	 * 当前用户秒标投标次数
	 * 
	 * @param userid
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	double getUserTenderNum(long userid, Date beginDate, Date endDate);

	/**
	 * 得到投标的次数
	 * 
	 * @param borrowId ,userId
	 * @return int
	 */
	public int getBorrowTenderTimes(long borrowId, long userId);

	/**
	 * 得到借出总额
	 */
	double sumTenderAccount(long userId);

	/**
	 * 更新状态
	 * 
	 * @param borrowId
	 * @param status
	 * @param preStatus
	 */
	void updateStatus(long borrowId, int status, int preStatus);
	
	/**
     * 更新状态
     * 
     * @param borrowId
     * @param status
     */
    void updateStatus(long id, int status);
	/**
	 * 自动投标判断一个标一个人只能投一次
	 * @param userId
	 * @param tenderType
	 * @param borrowId
	 * @param status
	 * @return
	 */
	public int getAutoTenderByUserId(long userId, byte tenderType, long borrowId, int status);
	/**
	 * 修改投标时候登记债权人时候的订单号
	 * @param id 标ID
	 * @param tenderBilNo 登记债权人订单号
	 */
	void modifyTenderBilNo(long id, String tenderBilNo);
	/**
	 * 通过投标订单号查询投标记录
	 * @param tenderBillNo 投标订单号
	 * @return 投标记录
	 */
	BorrowTender getTenderByBillNo(String tenderBillNo);
	/**
     * 通过标ID查询投标集合
     * @param id 标ID
     * @return 投标集合
     */
    List<BorrowTender> getTenderByBorrowId(long id);

    /**
     * 我投资的 标列表
     * @param BorrowTenderModel
     * @return
     */
    PageDataList<Borrow> getBorrowlist(BorrowTenderModel model);

    /**
     * 根据时间查询投资人个数
     * @param i
     * @return 投资人个数
     */
    int getInvestCountByDate(int i);
    /**
     * 根据时间查询投资人个数
     * @param date
     * @return 投资人个数
     */
    int getInvestCountByDate(String date);
    
    /**
     * 累计投资金额
     * @return
     */
    double tenderAccount(String startTime, String endTime);
    
    /**
     * 投资用户数个数
     * @param date
     */
    int getInvestUserCount();
    
    /**
     * 根据时间段统计投资用户数个数
     * @param startTime
     * @param endTime
     * @return
     */
    int getInvestUserCount(String startTime, String endTime);
    
    /**
     * 累计投资利息
     * @return
     */
    double tenderAllInterest();

    /**
     * 根据标ID得到当前投资总利息
     * @param id
     * @return
     */
	double getInterestByBorrowId(long id);
	
	/**
	 * 根据用户和投标状态获取投标个数
	 * @param user_id
	 * @param status
	 * @return
	 */
	int getTenderByUserAndStatus(long userId,int status);
	
	/**
	 * 根据borrow获取投标记录总数
	 * @param borrow
	 * @return
	 */
	int getTenderCountByBorrow(Borrow borrow);
	
	/**
	 * 获取用户投资次数
	 * @param userId
	 * @param status
	 * @return
	 */
	int getUserTenderNum(long userId);
	
	/**
	 * 获取用户初某个标种外的投资次数
	 * @param userId
	 * @param borrowType
	 * @return
	 */
	int getUserTenderNum(long userId,int borrowType);
	
	/**
	 * 投资记录列表（投标单位）
	 * @param model
	 * @return
	 */
	PageDataList<BorrowTenderModel> getTenderRecordlist(BorrowTenderModel model);

	BorrowTender getTenderBySubmitBidNo(String submitBidNo);

	/**
	 * 根据borrowId获得List
	 * @param borrowId
	 * @return
	 */
	public List<BorrowTender> getBorrowTenderByBorrowId(Long borrowId);
	
	/**
	 * 获取用户最近一年投资金额
	 * 
	 * @param starTime
	 * @param endTime
	 * @param user
	 * @return
	 */
	public double getLastYearInvest(Date starTime, Date endTime, User user);
	
	/**
	 *根据用户ID和borrowId获取在标中的投资顺序 
	 */
	public int getUserBorrowTenderOrder(long tenderId,long borrowId);
}
