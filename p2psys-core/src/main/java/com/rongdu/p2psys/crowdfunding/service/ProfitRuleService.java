package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;
import com.rongdu.p2psys.crowdfunding.model.ProfitRuleModel;

public interface ProfitRuleService
{
	public ProfitRule find(Integer Id);

	public List<ProfitRule> findAll();

	public List<ProfitRule> findByProjectId(long project_id);

	public void update(ProfitRule profitRule);

	public void delete(Integer id);

	public ProfitRule saveObject(ProfitRule profitRule);
	
	public List<ProfitRuleModel> findByProjectIdforWechat(long project_id);
}
