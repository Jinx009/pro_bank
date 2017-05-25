package com.rongdu.p2psys.crowdfunding.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 领投人仓库
 * @author Jinx
 *
 */
@Entity
@Table(name = ("cf_leader_factory"))
public class LeaderFactory implements  Serializable{

	private static final long serialVersionUID = 5852873424059468446L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	//个人简介
	private String info;
	//领投理由
	private String reason;
	//领投人头像
	private String picPath;
	//领投人姓名
	private String name;
	//历史数据
	private String history;
	//显示状态
	private Integer showStatus;
	//用户
	private Long userId;
	//审核状态
	private Integer status;
	//头像路径
	private Integer picUrl;
	//手机尾号
	private String tel;
	//测试结果
	private Integer result;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public Integer getShowStatus() {
		return showStatus;
	}
	public void setShowStatus(Integer showStatus) {
		this.showStatus = showStatus;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(Integer picUrl) {
		this.picUrl = picUrl;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	} 
	
}
