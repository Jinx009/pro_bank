package com.rongdu.p2psys.borrow.service;

import java.util.List;

import com.rongdu.p2psys.borrow.domain.BorrowMortgage;
import com.rongdu.p2psys.borrow.domain.BorrowUpload;

/**
 * 借款抵押物
 * @author sj
 * @since 2014年8月24日
 */
public interface BorrowMortgageService {

	/**
	 * 获取借款抵押物
	 * @param borrowId
	 * @param status
	 * @return
	 */
	public List<BorrowMortgage> findByBorrowId(long borrowId, int status);

	/**
	 * 更新资产包
	 * @param ids
	 * @param borrowId
	 */
	public void updateBorrowCollateral(BorrowMortgage borrowMortgage, List<BorrowUpload> pathList);
	/**
     * 获取车辆抵押总价
     * @param ids
     * @return 车辆抵押总价
     */
    public double getTotalMortgagePriceByMortgageIds(long[] ids);
    
    /**
     * 获取车辆评估总价
     * @param b
     * @return 车辆评估总价
     */
    public double getTotalAssessPriceByBorrowId(long id);

    /**
     * 根据抵押物id获取抵押物对象
     * @param id
     * @return 抵押物对象
     */
    public BorrowMortgage findById(long id);

    public void updateBorrowMortgage(List<BorrowMortgage> borrowMortgageList, List<BorrowMortgage> bms,List<BorrowUpload> list);

    public int getMaxNumByBorrowId(long borrowId);

    public List<BorrowMortgage> findByBorrowIdAndNum(long id, int num);

    public List<BorrowMortgage> findByBorrowIdAndNumAndStatus(long id, int num, int status);

    public double getTotalAssessPriceByBorrowIdAndNum(long id, int num);

    public double getTotalMortgagePriceByBorrowId(long borrowId);

    public Object[] getTotalPriceByBorrowIdAndNum(long id, int num);

    /**
     * 查询入库状态的抵押物
     * @param id
     * @param i
     * @return
     */
    public List<BorrowMortgage> findInByBorrowIdAndNum(long id, int i);

    /**
     * 添加资产包
     * @param borrowMortgageList
     * @param list
     */
    public void addMortgage(List<BorrowMortgage> borrowMortgageList, List<BorrowUpload> list);

}
