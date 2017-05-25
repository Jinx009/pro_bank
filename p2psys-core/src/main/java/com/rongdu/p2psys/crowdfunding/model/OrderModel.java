package com.rongdu.p2psys.crowdfunding.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.crowdfunding.domain.InvestOrder;

/**
 * 众筹产品订单
 * @author Jinx
 *
 */
public class OrderModel extends InvestOrder {

	private static final long serialVersionUID = -6687431303928787561L;
	// 当前页数
	private int page;
	// 每页数据条数
	private int rows = Page.ROWS;
	// 购买用户名称
	private String userName;
	// 项目名称
	private String projectName;
	// 购买金额
	private String buyMoney;
	//订单状态
	private Integer modelStatus;
	//订单产品类型
	private Integer projectType;
	//用户头像
	private String userPic;
	//领投状态
	private Integer leaderStatus;
	//产品图片
	private String projectPic;
	//是否关注
	private int attetionFlage;
	//项目名称
	private long projectId;

	public static OrderModel instance(InvestOrder order) {
		OrderModel model = new OrderModel();
		BeanUtils.copyProperties(order, model);
		return model;
	}
	public InvestOrder prototype() {
		InvestOrder order = new InvestOrder();
		BeanUtils.copyProperties(this, order);
		return order;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getBuyMoney() {
		return buyMoney;
	}
	public void setBuyMoney(String buyMoney) {
		this.buyMoney = buyMoney;
	}
	public Integer getModelStatus() {
		return modelStatus;
	}
	public void setModelStatus(Integer modelStatus) {
		this.modelStatus = modelStatus;
	}
	public Integer getProjectType() {
		return projectType;
	}
	public void setProjectType(Integer projectType) {
		this.projectType = projectType;
	}
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
	public Integer getLeaderStatus() {
		return leaderStatus;
	}
	public void setLeaderStatus(Integer leaderStatus) {
		this.leaderStatus = leaderStatus;
	}
	public String getProjectPic() {
		return projectPic;
	}
	public void setProjectPic(String projectPic) {
		this.projectPic = projectPic;
	}
	public int getAttetionFlage() {
		return attetionFlage;
	}
	public void setAttetionFlage(int attetionFlage) {
		this.attetionFlage = attetionFlage;
	}
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	
}
