package com.rongdu.p2psys.borrow.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.borrow.dao.BorrowAppointmentBidDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAppointmentBid;

/**
 * 预约投标
 * @author sj
 * @since 2015年3月16日17:22:28
 *
 */
@Repository("borrowAppointmentBidDao")
public class BorrowAppointmentBidDaoImpl extends BaseDaoImpl<BorrowAppointmentBid> implements BorrowAppointmentBidDao {

	@Override
	public double sumBidMoney(Borrow borrow) {
		String sql = "SELECT SUM(money) FROM rd_borrow_appointment_bid WHERE borrow_id = ?1";
		Query query = em.createNativeQuery(sql).setParameter(1, borrow.getId());
		Object ret = query.getSingleResult();
		if (ret == null){
			return 0;
		}
		return Double.parseDouble(ret.toString());
	}

	@Override
	public List<BorrowAppointmentBid> getBorrowAppointmentBidByBorrowId(
			long borrowId) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("borrow.id", borrowId);
		return super.findByCriteria(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowAppointmentBid> getBorrowAppointmentBid() {
		// 提前3分钟处理已预约投资用户
		String sql = "SELECT bab.* FROM rd_borrow_appointment_bid bab, rd_borrow b WHERE bab.borrow_id = b.id and bab.status = 2 and NOW() - b.fixed_time < 180";
		Query query = em.createNativeQuery(sql, BorrowAppointmentBid.class);
		return query.getResultList();
	}

	
	
}
