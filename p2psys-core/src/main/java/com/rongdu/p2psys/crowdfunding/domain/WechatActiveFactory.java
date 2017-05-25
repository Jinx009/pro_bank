package com.rongdu.p2psys.crowdfunding.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = ("cf_wechat_active_factory"))
public class WechatActiveFactory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	
	private Double money;
	
	private Integer activeTimes;
	
	private Date updateTime;
	
	private Integer wechatId;
	
	
	
	
	
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Integer getActiveTimes() {
		return activeTimes;
	}
	public void setActiveTimes(Integer activeTimes) {
		this.activeTimes = activeTimes;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getWechatId() {
		return wechatId;
	}
	public void setWechatId(Integer wechatId) {
		this.wechatId = wechatId;
	}
	
}
