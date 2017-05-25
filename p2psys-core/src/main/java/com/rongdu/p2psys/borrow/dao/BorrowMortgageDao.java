package com.rongdu.p2psys.borrow.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.borrow.domain.BorrowMortgage;

/**
 * 借款抵押物
 * @author zf
 * @version 2.0
 * @since 2014-08-22
 */
public interface BorrowMortgageDao extends BaseDao<BorrowMortgage> {
    /**
     * 获取车辆抵押总价
     * @param ids
     * @return 车辆抵押总价
     */
    double getTotalMortgagePriceByMortgageIds(long[] ids);
    /**
     * 获取车辆评估总价
     * @param ids
     * @return 车辆评估总价
     */
    double getTotalAssessPriceByBorrowId(long id);
    
    int getMaxNumByBorrowId(long borrowId);
    double getTotalAssessPriceByBorrowIdAndNum(long id, int num);
    double getTotalMortgagePriceByBorrowId(long borrowId);
    Object[] getTotalPriceByBorrowIdAndNum(long id, int num);
	
}
