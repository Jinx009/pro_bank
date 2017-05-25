package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.p2psys.crowdfunding.domain.ProjectLog;
import com.rongdu.p2psys.crowdfunding.model.ProjectLogModel;
import com.rongdu.p2psys.user.domain.User;

public interface ProjectLogService {

	public List<ProjectLog> getAudit();
	
	public ProjectLog save(ProjectLog projectLog);
	
	public List<ProjectLog> getReadyAudit();
	
	public List<ProjectLogModel> getNotices(User user);
}
