package com.rongdu.p2psys.crowdfunding.model;


import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;

/**
 * 自定义领投人仓库
 * @author Jinx
 *
 */
public class LeaderFactoryModel extends LeaderFactory{
	
	private static final long serialVersionUID = -2931589645895218409L;
	// 当前页码
	private int page;
	// 每页页数
	private int rows = Page.ROWS;
	//产品标签
	private String flagNames;
	//已领投项目
	private String projectNames;

	public static LeaderFactoryModel instance(LeaderFactory leader) {
		LeaderFactoryModel model = new LeaderFactoryModel();
		BeanUtils.copyProperties(leader, model);
		return model;
	}
	public LeaderFactory prototype() {
		LeaderFactory leader = new LeaderFactory();
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
	public String getProjectNames() {
		return projectNames;
	}
	public void setProjectNames(String projectNames) {
		this.projectNames = projectNames;
	}
	public String getFlagNames() {
		return flagNames;
	}
	public void setFlagNames(String flagNames) {
		this.flagNames = flagNames;
	}
	
}
