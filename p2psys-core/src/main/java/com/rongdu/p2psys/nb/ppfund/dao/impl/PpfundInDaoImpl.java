package com.rongdu.p2psys.nb.ppfund.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.nb.ppfund.dao.PpfundInDao;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.user.domain.User;

@Repository("thePpfundInDao")
public class PpfundInDaoImpl extends BaseDaoImpl<PpfundIn> implements PpfundInDao
{

	public PpfundIn findByUser(User user, long ppfundId)
	{
		QueryParam param = QueryParam.getInstance();
		param.addParam("ppfund.id", ppfundId);
		param.addParam("user.userId", user.getUserId());
		PpfundIn pp = super.findByCriteriaForUnique(param);
		return pp;
	}

	@Override
	public Double getTotalDayInvestMoney(Long userId) {		
		String sql = "SELECT sum(account) FROM rd_ppfund_in WHERE to_days(add_time) = to_days(now()) AND user_id = :userId";
        Query query = em.createNativeQuery(sql).setParameter("userId", userId);
        Object obj = query.getSingleResult();
        if (obj != null) {
        	return Double.parseDouble(obj.toString());
        }
        return (double) 0;
	}

}
