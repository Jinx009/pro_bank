package com.rongdu.p2psys.crowdfunding.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.crowdfunding.domain.ProjectLog;


public class ProjectLogModel extends ProjectLog{

	private static final long serialVersionUID = -719389657059726479L;

	private String projectName;
	private String typeName;
	
	public static ProjectLogModel instance(ProjectLog projectLog) {
		ProjectLogModel projectLogModel = new ProjectLogModel();
		BeanUtils.copyProperties(projectLog, projectLogModel);
		return projectLogModel;
	}

	public ProjectLog prototype() {
		ProjectLog projectLog = new ProjectLog();
		BeanUtils.copyProperties(this, projectLog);
		return projectLog;
	}

	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
