package com.rongdu.p2psys.core.dao.jdbc;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.RuleDao;
import com.rongdu.p2psys.core.domain.Rule;
import com.rongdu.p2psys.user.exception.UserException;

@Repository("ruleDao")
public class RuleDaoImpl extends BaseDaoImpl<Rule> implements RuleDao {

	/**
	 * 根据nid修改规则JSON
	 * 
	 * @param nid
	 * @param rule_check
	 */
	public void update(String nid, String ruleCheck) {
		String jpql = " Update Rule SET ruleCheck = :ruleCheck WHERE nid = :nid ";
		Query query = em.createQuery(jpql);
		query.setParameter("ruleCheck", ruleCheck);
		query.setParameter("nid", nid);
		int result = query.executeUpdate();
		if (result != 1) {
			throw new UserException("修改规则失败！");
		}
	}

}
