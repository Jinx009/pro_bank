package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.ProfitRuleDao;
import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;

@Repository("profitRuleDao")
public class ProfitRuleDaoImpl extends BaseDaoImpl<ProfitRule> implements ProfitRuleDao{

	@SuppressWarnings("unchecked")
	public List<ProfitRule> getByHql(String hql) {
		Query query = em.createQuery(hql);
		List<ProfitRule> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

}
