package com.rongdu.p2psys.nb.ppfund.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.ppfund.dao.PpfundDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;

@Repository("thePpfundDao")
public class PpfundDaoImpl extends BaseDaoImpl<Ppfund> implements PpfundDao {

	@Override
	public Double getTotalInvestMoney(Ppfund ppfund) {
		String strSql = "SELECT SUM(account) FROM rd_ppfund_in WHERE ppfund_id = :ppfundId";
		Query query = em.createNativeQuery(strSql);
		query.setParameter("ppfundId", ppfund.getId());

		if (query.getSingleResult() != null) {
			return new Double(query.getSingleResult().toString());
		}
		return new Double(0);
	}

	@Override
	public Double getTotalProfitMoney(Ppfund ppfund) {
		String strSql = "SELECT SUM(interest) FROM rd_ppfund_in WHERE ppfund_id = :ppfundId";
		Query query = em.createNativeQuery(strSql);
		query.setParameter("ppfundId", ppfund.getId());

		if (query.getSingleResult() != null) {
			return new Double(query.getSingleResult().toString());
		}
		return new Double(0);
	}

}
