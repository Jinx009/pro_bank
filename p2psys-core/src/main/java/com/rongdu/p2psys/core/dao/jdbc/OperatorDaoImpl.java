package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.OperatorDao;
import com.rongdu.p2psys.core.domain.Operator;

@Repository("operatorDao")
public class OperatorDaoImpl extends BaseDaoImpl<Operator> implements
		OperatorDao {

	@Override
	public Operator getOperatorByName(String userName) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("userName", userName);
		param.addParam("isDelete", false);

		return findByCriteriaForUnique(param);
	}

	public Operator userFind(long id) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("isDelete", false);
		param.addParam("id", id);
		List<Operator> list = super.findByCriteria(param);

		// String jpql = "from User where isDelete = ?1 and id=?2";
		// Query query = em.createQuery(jpql);
		// query.setParameter(1, 0);
		// query.setParameter(2, id);
		// List<Operator> list = query.getResultList();
		if (list != null && list.size() > 0) {
			return (Operator) list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public Operator getManagerByUserName(String userName) {
		String jpql = "from Operator where userName = ?1";
		Query query = em.createQuery(jpql);
		query.setParameter(1, userName);
		List list = query.getResultList();
		if (list != null && list.size() >= 1) {
			return (Operator) list.get(0);
		} else {
			return null;
		}
	}

}
