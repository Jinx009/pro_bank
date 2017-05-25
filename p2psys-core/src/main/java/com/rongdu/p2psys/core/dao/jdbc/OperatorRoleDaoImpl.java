package com.rongdu.p2psys.core.dao.jdbc;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.OperatorRoleDao;
import com.rongdu.p2psys.core.domain.OperatorRole;

@Repository
public class OperatorRoleDaoImpl extends BaseDaoImpl<OperatorRole> implements OperatorRoleDao {

	public void deleteByUserId(long userId) {
		Query query = em.createNativeQuery("delete from s_operator_role where operator_id=?1").setParameter(1, userId);
		query.executeUpdate();
	}

}
