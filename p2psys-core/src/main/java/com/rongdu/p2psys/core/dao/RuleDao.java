package com.rongdu.p2psys.core.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.Rule;

/**
 * 规则DAO
 */
public interface RuleDao extends BaseDao<Rule> {

	/**
	 * 根据nid修改规则JSON
	 * 
	 * @param nid
	 * @param rule_check
	 */
	void update(String nid, String rule_check);

}
