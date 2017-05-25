package com.rongdu.p2psys.tpp.yjf.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.SearchParam;
import com.rongdu.p2psys.tpp.domain.YjfPay;



public interface YjfDao extends BaseDao<YjfPay> {
	/**
	 * 查询标的交易号
	 * @param userId
	 * @param borrowId
	 * @param status
	 * @return
	 */
	public YjfPay getBorrowTradeNo(String userId, String borrowId, int status);
	/**
	 * 查询所有投标成功的用户
	 * @param userId
	 * @param borrowId
	 * @param status
	 * @return
	 */
	public List<YjfPay> getTendersPayed( String borrowId);	
	/**
	 * 查询所记录
	 */
	PageDataList<YjfPay> getList(SearchParam sp);
	/**
	 * 
	 * @param borrowId
	 * @param service
	 * @return
	 */
	List<YjfPay> getWrongStatusYjfPayByBorrowId(long borrowId, String service);
	/**
	 * 通过订单号查询yjfpay
	 * @param orderNo 订单号
	 * @return yjfpay
	 */
	YjfPay findByOrderNo(String orderNo);

}
