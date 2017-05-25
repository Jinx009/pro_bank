package com.rongdu.p2psys.crowdfunding.model;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.domain.ProfitRule;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.domain.ProjectLog;

public class ProjectBaseinfoModel extends ProjectBaseinfo {
	
	private static final long serialVersionUID = 4755081641536046772L;
	//产品显示状态
	private Integer timeStatus;
	//素材列表
	private List<Materials> materialsList;
	//当前页码
	private int page;
	//每页条数
	private int rows = Page.ROWS;
	//备注
	private String remark;
	//对应产品
	private long productId;
	//产品权益
	private List<ProfitRule> profitRuleList;
	//产品日志
	private List<ProjectLog> log;
	//新增未提交审核
	public static final int STATUS_SAVED = -1;
	//等待审核
	public static final int STATUS_WAITING_FOR_APPROVE = 0;
	//审核通过
	public static final int STATUS_APPROVED = 2;
	//众筹成功
	public static final int STATUS_CONFIRM = 3;
	//产品撤回 
	public static final int STATUS_CANCEL = 4;
	//无关状态
	public static final int STATUS_UNRELATED = 99;

	public static ProjectBaseinfoModel instance(ProjectBaseinfo projectBaseinfo) {
		ProjectBaseinfoModel model = new ProjectBaseinfoModel();
		BeanUtils.copyProperties(projectBaseinfo, model);
		return model;
	}
	public ProjectBaseinfo prototype() {
		ProjectBaseinfo projectBaseinfo = new ProjectBaseinfo();
		BeanUtils.copyProperties(this, projectBaseinfo);
		return projectBaseinfo;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public Integer getTimeStatus() {
		return timeStatus;
	}
	public void setTimeStatus(Integer timeStatus) {
		this.timeStatus = timeStatus;
	}
	public List<Materials> getMaterialsList() {
		return materialsList;
	}
	public void setMaterialsList(List<Materials> materialsList) {
		this.materialsList = materialsList;
	}
	public List<ProfitRule> getProfitRuleList() {
		return profitRuleList;
	}
	public void setProfitRuleList(List<ProfitRule> profitRuleList) {
		this.profitRuleList = profitRuleList;
	}
	public List<ProjectLog> getLog() {
		return log;
	}
	public void setLog(List<ProjectLog> log) {
		this.log = log;
	}
	
}
