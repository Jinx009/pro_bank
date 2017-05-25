package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;

public interface ProfitRuleDao extends BaseDao<ProfitRule>
{
	public List<ProfitRule> getByHql(String hql);
}
