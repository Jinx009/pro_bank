package com.rongdu.p2psys.crowdfunding.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.rongdu.p2psys.user.domain.User;

/**
 * 项目领投人
 * @author Jinx
 *
 */
@Entity
@Table(name = ("cf_leader"))
public class Leader implements Serializable{

	private static final long serialVersionUID = 4203541049345357939L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	//领投人姓名
	private String name;
	//领投人用户Id
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	//领投人简介
	private String info;
	//领投人领投理由
	private String reason;
	//领投状态
	private Integer status;
	//对应产品
	@ManyToOne
	@JoinColumn(name = "project_id")
	private ProjectBaseinfo project;
	//是否对应领投人仓库
	@OneToOne
	@JoinColumn(name = "leader_factory_id")
	private LeaderFactory leaderFactory;
	//领头人头像
	private String picPath;
	//添加时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "add_time")
	private Date addTime;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public ProjectBaseinfo getProject() {
		return project;
	}
	public void setProject(ProjectBaseinfo project) {
		this.project = project;
	}
	public LeaderFactory getLeaderFactory() {
		return leaderFactory;
	}
	public void setLeaderFactory(LeaderFactory leaderFactory) {
		this.leaderFactory = leaderFactory;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
}
