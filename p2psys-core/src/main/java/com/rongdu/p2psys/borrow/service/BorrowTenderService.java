package com.rongdu.p2psys.borrow.service;

import java.util.Date;
import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.model.NewTenderModel;
import com.rongdu.p2psys.core.model.RankModel;

public interface BorrowTenderService {

	/**
	 * 我的投资列表
	 * 
	 * @param borrowModel
	 * @return
	 */
	PageDataList<BorrowTenderModel> list(BorrowTenderModel borrowTenderModel);
	
	/**
	 * 当前多身份用户—投资列表信息查询(by:Chris)
	 * 
	 * @param borrowModel
	 * @return Multiple identities
	 */
	PageDataList<BorrowTenderModel> multipleIdentitiesList(BorrowTenderModel borrowTenderModel);

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
	 * 投标
	 * 
	 * @param tender
	 * @param user
	 * @return
	 */
	BorrowTender addTender(Borrow borrow, BorrowModel model);
	
	/**
	 * VIP投标
	 * 
	 * @param tender
	 * @param user
	 * @return
	 */
	BorrowTender addVipTender(Borrow borrow, BorrowModel model);

	/**
	 * 首页显示最新借款
	 * 
	 * @return
	 */
	List<NewTenderModel> getNewTenderList();

	/**
	 * 首页投资排行榜
	 * 
	 * @return
	 */
	List<RankModel> getRankList();

	/**
	 * @param borrowId
	 * @param page
	 * @return
	 */
	PageDataList<BorrowTenderModel> getTenderList(long borrowId, int page);

	/**
	 * @param userid
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	double getUserTenderNum(long userid, Date beginDate, Date endDate);

	/**
	 * @param borrowId
	 * @return
	 */
	List<BorrowTenderModel> getTenderList(long borrowId);

	/**
	 * 根据borrowId查询tenderList
	 * 
	 * @param borrowId
	 * @return
	 */
	List<BorrowTender> getTenderListByBorrowId(long borrowId);

	/**
	 * 根据id查询
	 * 
	 * @param tenderId
	 * @return
	 */
	BorrowTender getTenderById(long tenderId);

	/**
	 * 投资用户汇总
	 * @return
	 */
    List<Integer> getInvestCount();
    
    /**
     * 我投资的 标列表
     * @param borrowModel
     * @return
     */
    PageDataList<BorrowModel> getBorrowlist(BorrowTenderModel model);

    /**
     * 根据时间获取投资用户个数
     * @param date
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
     * 根据用户和投标状态获取投标个数
     * @param user_id
     * @param status
     * @return
     */
    int countTenderByUserAndStatus(long userId,int status);
    
    void checkNovice(long userId);
    
    /**
     * 根据model返回BorrowTenderModel列表(投资记录)
     * @param model
     * @return
     */
	PageDataList<BorrowTenderModel> getTenderRecordlist(BorrowTenderModel model);

	public PageDataList<BorrowTenderModel> bidList(BorrowModel model);

	BorrowTender addNbTender(Borrow borrow, BorrowModel m);
	
	public int getUserBorrowTenderOrder(long tenderId,long borrowId);
}
