package com.rongdu.p2psys.crowdfunding.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.crowdfunding.dao.ProjectFlagDao;
import com.rongdu.p2psys.crowdfunding.domain.ProjectFlag;
import com.rongdu.p2psys.crowdfunding.service.ProjectFlagService;

@Service("projectFlagService")
public class ProjectFlagServiceImpl implements ProjectFlagService{

	@Resource
	private ProjectFlagDao projectFlagDao;
	
	public List<ProjectFlag> findAll() {
		return projectFlagDao.findAll();
	}

	public List<ProjectFlag> findByProject(Integer projectId) {
		String hql = " FROM ProjectFlag where projectId ="+projectId;
		return projectFlagDao.findByHql(hql);
	}

	public void save(ProjectFlag projectFlag) {
		projectFlagDao.save(projectFlag);
	}

	public void delete(Integer id) {
		projectFlagDao.delete(id);
	}

}
