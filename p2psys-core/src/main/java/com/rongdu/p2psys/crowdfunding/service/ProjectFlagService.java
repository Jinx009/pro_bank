package com.rongdu.p2psys.crowdfunding.service;

import java.util.List;

import com.rongdu.p2psys.crowdfunding.domain.ProjectFlag;

public interface ProjectFlagService {

	public List<ProjectFlag> findAll();
	
	public List<ProjectFlag> findByProject(Integer projectId);
	
	public void save(ProjectFlag projectFlag);
	
	public void delete(Integer id);
	
}
