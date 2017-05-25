package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;

/**
 * 顾问专家
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
@Entity
@Table(name = (Global.DB_PREFIX + "consultant"))
public class Consultant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 顾问名字
	 */
	private String name;

	/**
	 * 顾问职位
	 */
	private String position;
	
	/**
	 * 顾问介绍
	 */
	private String introduction;
	
	/**
	 * 是否推荐
	 */
	private int isRecommend;
	
	/**
	 * 顾问头像
	 */
	private String avatar;
	
	/**
	 * 是否删除
	 */
	private int isDelete;
	
	/**
	 * 添加时间
	 */
	private Date addTime;
	
	/**
	 * IP
	 */
	private String addIp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(int isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
}
