package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.ProjectLogDao;
import com.rongdu.p2psys.crowdfunding.domain.ProjectLog;
import com.rongdu.p2psys.crowdfunding.model.ProjectLogModel;
import com.rongdu.p2psys.crowdfunding.service.ProjectLogService;
import com.rongdu.p2psys.user.domain.User;

@Service("projectLogService")
public class ProjectLogServiceImpl implements ProjectLogService{

	@Resource
	private ProjectLogDao projectLogDao;
	
	public List<ProjectLog> getAudit() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM ProjectLog WHERE type = 0 or type = 1 ORDER BY addTime DESC ");
		List<ProjectLog> list = projectLogDao.getByHql(buffer.toString());
		return list;
	}
	
	

	public ProjectLog save(ProjectLog projectLog) {
		return projectLogDao.save(projectLog);
	}

	public List<ProjectLog> getReadyAudit() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" FROM ProjectLog WHERE type = 2 ORDER BY addTime DESC ");
		List<ProjectLog> list = projectLogDao.getByHql(buffer.toString());
		return list;
	}



	public List<ProjectLogModel> getNotices(User user) {
		String sql = " SELECT l.id,l.add_time,l.project_id,l.content,l.type FROM  cf_project_log l,cf_project_baseinfo p WHERE p.user_id = :userId AND l.project_id = p.id ORDER BY l.add_time DESC ";
		return projectLogDao.getNotices(sql,user.getUserId());
	}

}
