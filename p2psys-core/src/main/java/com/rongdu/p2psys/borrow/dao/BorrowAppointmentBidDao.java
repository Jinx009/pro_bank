package com.rongdu.p2psys.borrow.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAppointmentBid;

/**
 * 预约投标
 * @author sj
 *
 */
public interface BorrowAppointmentBidDao extends BaseDao<BorrowAppointmentBid> {

	/**
	 * 预约投标的金额
	 * @param borrow
	 * @return
	 */
	public double sumBidMoney(Borrow borrow);

	/**
	 * 根据borrowId获得预约投标的列表
	 * @param borrowId
	 * @return
	 */
	public List<BorrowAppointmentBid> getBorrowAppointmentBidByBorrowId(long borrowId);

	/**
	 * 获得未投标的List
	 * @return
	 */
	public List<BorrowAppointmentBid> getBorrowAppointmentBid();

	
	
}
