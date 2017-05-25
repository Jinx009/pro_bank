package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.crowdfunding.domain.ProjectFlag;

public interface ProjectFlagDao extends BaseDao<ProjectFlag>{

	public List<ProjectFlag> findByHql(String hql);
	
}
