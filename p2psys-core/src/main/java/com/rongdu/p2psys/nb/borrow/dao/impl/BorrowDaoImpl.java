package com.rongdu.p2psys.nb.borrow.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.nb.borrow.dao.BorrowDao;

@Repository("theBorrowDao")
public class BorrowDaoImpl extends BaseDaoImpl<Borrow> implements BorrowDao {

	@Override
	public Boolean isSpreadBorrow(Long borrowId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT borrow.*                            ");
		sql.append("  FROM rd_borrow borrow                    ");
		sql.append(" WHERE borrow.id = :borrowId               ");
		sql.append("   AND borrow.status = 1                   ");
		sql.append("   AND borrow.account > borrow.account_yes ");
		sql.append("   AND borrow.expiration_time < NOW()      ");

		Query query = em.createNativeQuery(sql.toString(), Borrow.class);
		query.setParameter("borrowId", borrowId);

		if (query.getResultList().size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean isSkipReview(Borrow borrow){
		String sql = " select b.id from rd_borrow b join nb_product_type n on "
				+ "b.type = n.id where n.type_category = 'VIP' and b.id= " + borrow.getId();
		Query query = em.createNativeQuery(sql);
		if (query.getResultList().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
