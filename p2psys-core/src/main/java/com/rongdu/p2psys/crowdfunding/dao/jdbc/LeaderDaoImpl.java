package com.rongdu.p2psys.crowdfunding.dao.jdbc;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.crowdfunding.dao.LeaderDao;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.model.LeaderModel;

@Repository("leaderDao")
public class LeaderDaoImpl extends BaseDaoImpl<Leader> implements LeaderDao  {

	@SuppressWarnings("unchecked")
	public List<Leader> getByHql(String hql) {
		Query query = em.createQuery(hql);
		List<Leader> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

	@Override
	public PageDataList<LeaderModel> getList(LeaderModel model, int pageNumber,
			int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber,
				pageSize);
		param.addParam("project.id",
				model.getProject().getId());

		PageDataList<Leader> domainList = super.findPageList(param);
		PageDataList<LeaderModel> modelList = new PageDataList<LeaderModel>();
		List<LeaderModel> list = new ArrayList<LeaderModel>();
		modelList.setPage(domainList.getPage());
		if (domainList.getList().size() > 0)
		{
			for (Leader tempDomain : domainList.getList())
			{
				LeaderModel tempModel = LeaderModel
						.instance(tempDomain);
				tempModel.setProjectName(tempDomain.getProject().getProjectName());
				list.add(tempModel);
			}
		}
		modelList.setList(list);
		return modelList;
	}

}
