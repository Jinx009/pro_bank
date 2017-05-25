package com.rongdu.p2psys.crowdfunding.model;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.crowdfunding.domain.AttentionList;
import com.rongdu.p2psys.crowdfunding.domain.Materials;

public class AttentionListModel extends AttentionList{

	private static final long serialVersionUID = -4408830069396866809L;

	// 当前页码
	private int page;
	// 每页页数
	private int rows = Page.ROWS;
	// 项目名称
	private String projectName;
	//项目状态
	private Integer timeStatus;
	//项目类型
	private Integer type;
	//素材
	private List<Materials> materials;

	public static AttentionListModel instance(AttentionList attentionList) {
		AttentionListModel model = new AttentionListModel();
		BeanUtils.copyProperties(attentionList, model);
		return model;
	}
	public AttentionList prototype() {
		AttentionList attentionList = new AttentionList();
		BeanUtils.copyProperties(this,attentionList);
		return attentionList;
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
	public Integer getTimeStatus() {
		return timeStatus;
	}
	public void setTimeStatus(Integer timeStatus) {
		this.timeStatus = timeStatus;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<Materials> getMaterials() {
		return materials;
	}
	public void setMaterials(List<Materials> materials) {
		this.materials = materials;
	}
	
}
