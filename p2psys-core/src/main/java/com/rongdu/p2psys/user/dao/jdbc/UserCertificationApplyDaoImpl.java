package com.rongdu.p2psys.user.dao.jdbc;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.user.dao.UserCertificationApplyDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCertificationApply;

@Repository("userCertificationApplyDao")
public class UserCertificationApplyDaoImpl extends BaseDaoImpl<UserCertificationApply> implements UserCertificationApplyDao {

	@Override
	public int getStatusByUserAndTypeId(User user, long typeId) {
		int status;
		String sql = "select status from UserCertificationApply where user=? and type.id=? order by id desc";
		Query query = em.createQuery(sql);
		query.setParameter(1, user);
		query.setParameter(2, typeId);
		query.setMaxResults(1);
		try {
			status = (Integer)query.getSingleResult();
		} catch (Exception e) {
			status = -1;
		}
		return status;
	}

}
