package com.rongdu.p2psys.user.dao.jdbc;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.user.dao.UserCreditDao;
import com.rongdu.p2psys.user.domain.UserCredit;

@Repository("userCreditDao")
public class UserCreditDaoImpl extends BaseDaoImpl<UserCredit> implements UserCreditDao {

	@Override
	public void update(double totalVar, double useVar, double nouseVar, long userId) {
		StringBuffer buffer = new StringBuffer(
				"UPDATE rd_user_credit SET credit=credit+:credit,credit_use=credit_use+:creditUse,credit_nouse=credit_nouse+:creditNouse WHERE user_id=:userId");
		Query query = em.createNativeQuery(buffer.toString());
		query.setParameter("credit", totalVar);
		query.setParameter("creditUse", useVar);
		query.setParameter("creditNouse", nouseVar);
		query.setParameter("userId", userId);
		query.executeUpdate();
	}

	public UserCredit findByUserId(long userId) {
		return super.findObjByProperty("user.userId", userId);
	}
	
	

}
