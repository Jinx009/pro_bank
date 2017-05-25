package com.rongdu.p2psys.crowdfunding.dao;


import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.model.LeaderModel;

public interface LeaderDao extends BaseDao<Leader>{

	public List<Leader> getByHql(String hql);

	public PageDataList<LeaderModel> getList(LeaderModel model, int pageNumber,
			int pageSize);
}
