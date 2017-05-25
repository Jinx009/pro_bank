package com.rongdu.p2psys.bond.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.bond.domain.BondCollection;

/**
 * 债权待收DAO接口
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
public interface BondCollectionDao extends BaseDao<BondCollection> {

    /**
     * 查询债权待收
     * 
     * @param id 主键ID
     * @return 债权待收实体
     */
    BondCollection getBondCollectionById(long id);
    
	/**
	 * 查询债权待收
	 * @param bondTenderId
	 * @param period
	 * @return
	 */
    BondCollection getCollectionByTenderAndPeriod(long bondTenderId, int period);
    
	/**
	 * 计算投资人剩余待还本金
	 * @param bondTenderId
	 * @return
	 */
	double getRemainderCapital(long bondTenderId); 
	
	/**
	 * 标的还款是否有逾期
	 * @param borrowId
	 * @return
	 */
	boolean isLatedByBorrowId(long borrowId);
	
	/**
	 * 债权待还列表
	 * @param repaymentId
	 * @return
	 */
	List<BondCollection> getBondCollectionList(long repaymentId);
	
	 /**
     * 查询当前用户债权待收本金和利息
     * @param userId
     * @return
     */
    public Object[] getSumBondCollection(long userId);
	

}
