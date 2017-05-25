package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.crowdfunding.domain.ProjectLog;
import com.rongdu.p2psys.crowdfunding.model.ProjectLogModel;

public interface ProjectLogDao extends BaseDao<ProjectLog>{

	public List<ProjectLog> getByHql(String hql);

	public List<ProjectLog> getByProjectId(Long id);

	public List<ProjectLogModel> getNotices(String sql, long userId);
	
}
