package com.rongdu.p2psys.crowdfunding.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.crowdfunding.domain.Leader;

/**
 * 项目申请领投人
 * 
 * @author Jinx
 *
 */
public class LeaderModel extends Leader {

	private static final long serialVersionUID = -5311210729438149250L;
	// 当前页码
	private int page;
	// 每页页数
	private int rows = Page.ROWS;
	// 项目名称
	private String projectName;

	public static LeaderModel instance(Leader leader) {
		LeaderModel model = new LeaderModel();
		BeanUtils.copyProperties(leader, model);
		return model;
	}
	public Leader prototype() {
		Leader leader = new Leader();
		BeanUtils.copyProperties(this, leader);
		return leader;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
