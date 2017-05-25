package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.ArrayList;
import java.util.List;












import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.AttentionListDao;
import com.rongdu.p2psys.crowdfunding.dao.OrderDao;
import com.rongdu.p2psys.crowdfunding.dao.ProfitRuleDao;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;
import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;
import com.rongdu.p2psys.crowdfunding.model.ProfitRuleModel;
import com.rongdu.p2psys.crowdfunding.service.ProfitRuleService;

@Service("profitRuleService")
public class ProfitRuleServiceImpl implements ProfitRuleService {

	@Resource
	private ProfitRuleDao profitRuleDao;
	@Resource
	private OrderDao cfOrderDao;
	@Resource
	private AttentionListDao attentionListDao;

	public ProfitRule find(Integer Id) {
		return profitRuleDao.findObjByProperty("id", Id);
	}

	public List<ProfitRule> findAll() {
		return profitRuleDao.findAll();
	}

	public List<ProfitRule> findByProjectId(long project_id) {
		String hql = "  from ProfitRule WHERE projectId = " + project_id;
		List<ProfitRule> list = profitRuleDao.getByHql(hql);
		return list;
	}

	public void update(ProfitRule profitRule) {
		profitRuleDao.update(profitRule);
	}

	public void delete(Integer id) {
		profitRuleDao.delete(id);
	}

	public ProfitRule saveObject(ProfitRule profitRule) {
		return profitRuleDao.save(profitRule);
	}

	@Override
	public List<ProfitRuleModel> findByProjectIdforWechat(long project_id) {
		String hql = "  from ProfitRule WHERE projectId = " + project_id;
		List<ProfitRule> list = profitRuleDao.getByHql(hql);
		List<ProfitRuleModel> modelList = null;
		ProfitRuleModel  model=null;
		if(null != list && !list.isEmpty()){
			modelList = new ArrayList<ProfitRuleModel>();
			for (ProfitRule profitRule : list) {
				model =ProfitRuleModel.instance(profitRule);
				model.setRuleNum(countRule(profitRule.getId()));
				modelList.add(model);
			}
		}
		
		return modelList;
	}

	//统计没项权益支持者人数
	public int countRule(long id){
		StringBuffer buffer = new StringBuffer();
		buffer.append("from InvestOrder  WHERE profitRule.id =  ");
		buffer.append(id);
		List<InvestOrder> list = cfOrderDao.getByHql(buffer.toString());
		if(null !=list && !list.isEmpty()){
			return list.size();
		}
		return 0;
	}
}
