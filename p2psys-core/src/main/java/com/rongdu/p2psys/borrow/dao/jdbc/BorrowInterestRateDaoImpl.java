package com.rongdu.p2psys.borrow.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.borrow.dao.BorrowInterestRateDao;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.user.domain.User;

@Repository("borrowInterestRateDao")
public class BorrowInterestRateDaoImpl extends BaseDaoImpl<BorrowInterestRate> implements BorrowInterestRateDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowInterestRate> findByStatus(int status) {
		String sql = "select * from rd_borrow_interest_rate where status = :status";
		Query query = em.createNativeQuery(sql,BorrowInterestRate.class).setParameter("status", status);
		List<BorrowInterestRate> list = query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowInterestRate> findByStatusAndAddTime(int status) {
		String sql = "select * from rd_borrow_interest_rate where status = :status and DATE_ADD(add_time,INTERVAL 3 MONTH) < now()";
		Query query = em.createNativeQuery(sql,BorrowInterestRate.class).setParameter("status", status);
		List<BorrowInterestRate> list = query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BorrowInterestRate> findByStatusAndUser(int status, User user) {
		String sql = "select * from rd_borrow_interest_rate where status = :status and user_id = :user_id";
		Query query = em.createNativeQuery(sql,BorrowInterestRate.class).setParameter("status", status).setParameter("user_id", user.getUserId());
		List<BorrowInterestRate> list = query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BorrowInterestRate findByStatusAndInterestRateValue(int status,
			double interestRateValue, User user) {
		String sql = "select * from rd_borrow_interest_rate where status = :status and value = :value and user_id = :user_id";
		Query query = em.createNativeQuery(sql,BorrowInterestRate.class).setParameter("status", status).setParameter("value", interestRateValue)
				.setParameter("user_id", user.getUserId());
		List<BorrowInterestRate> list = query.getResultList();
		BorrowInterestRate bir = new BorrowInterestRate();
		if(list != null && list.size()>0){
			bir = list.get(0);
		}
		return bir;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BorrowInterestRate findByUserId(long userId) {
		String sql = "select * from rd_borrow_interest_rate where user_id = :user_id";
		Query query = em.createNativeQuery(sql,BorrowInterestRate.class).setParameter("user_id", userId);
		List<BorrowInterestRate> list = query.getResultList();
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BorrowInterestRate findByStatusAndTender(int status,
			BorrowTender tender) {
		String sql = "select * from rd_borrow_interest_rate where status = :status and tender_id = :tender_id and user_id = :user_id";
		Query query = em.createNativeQuery(sql,BorrowInterestRate.class).setParameter("status", status).setParameter("tender_id", tender.getId())
				.setParameter("user_id", tender.getUser().getUserId());
		List<BorrowInterestRate> list = query.getResultList();
		BorrowInterestRate bir = new BorrowInterestRate();
		if(list != null && list.size()>0){
			bir = list.get(0);
		}
		return bir;
	}

}
