package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 文章表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_article")
public class Article implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 栏目
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private Site site;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 状态，0：隐藏，1：显示
	 */
	private byte status;
	/**
	 * 排序
	 */
	private int sort;
	/**
	 * 是否推荐，0：否，1：是
	 */
	private byte isRecommend;
	/**
	 * 是否置顶，0：否，1：是
	 */
	private byte isTop;
	
	/**
	 * 是否删除，0：否，1：是
	 */
	private byte isDelete;
	
	/**
	 * 简介
	 */
	private String introduction;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 点击量
	 */
	private int clicks;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;
	/**
	 * 图片保存路径
	 */
	private String picPath;

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * Site对象
	 * @return Site Site对象
	 */
	public Site getSite() {
		return site;
	}
	/**
	 * 设置Site对象
	 * @param site 
	 */
	public void setSite(Site site) {
		this.site = site;
	}

	/**
	 * 获取标题
	 * 
	 * @return 标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置标题
	 * 
	 * @param title 要设置的标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取状态，0：隐藏，1：显示
	 * 
	 * @return 状态，0：隐藏，1：显示
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * 设置状态，0：隐藏，1：显示
	 * 
	 * @param status 要设置的状态，0：隐藏，1：显示
	 */
	public void setStatus(byte status) {
		this.status = status;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public int getSort() {
		return sort;
	}

	/**
	 * 设置排序
	 * 
	 * @param sort 要设置的排序
	 */
	public void setSort(int sort) {
		this.sort = sort;
	}

	/**
	 * 获取是否推荐，0：否，1：是
	 * 
	 * @return 是否推荐，0：否，1：是
	 */
	public byte getIsRecommend() {
		return isRecommend;
	}

	/**
	 * 设置是否推荐，0：否，1：是
	 * 
	 * @param isRecommend 要设置的是否推荐，0：否，1：是
	 */
	public void setIsRecommend(byte isRecommend) {
		this.isRecommend = isRecommend;
	}

	/**
	 * 获取是否置顶，0：否，1：是
	 * 
	 * @return 是否置顶，0：否，1：是
	 */
	public byte getIsTop() {
		return isTop;
	}

	/**
	 * 设置是否置顶，0：否，1：是
	 * 
	 * @param isTop 要设置的是否置顶，0：否，1：是
	 */
	public void setIsTop(byte isTop) {
		this.isTop = isTop;
	}

	/**
	 * 获取是否删除，0：否，1：是
	 * @return
	 */
	public byte getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置是否删除，0：否，1：是
	 * @param isDelete
	 */
	public void setIsDelete(byte isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * 获取简介
	 * 
	 * @return 简介
	 */
	public String getIntroduction() {
		return introduction;
	}

	/**
	 * 设置简介
	 * 
	 * @param introduction 要设置的简介
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content 要设置的内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取点击量
	 * 
	 * @return 点击量
	 */
	public int getClicks() {
		return clicks;
	}

	/**
	 * 设置点击量
	 * 
	 * @param clicks 要设置的点击量
	 */
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	/**
	 * 获取添加时间
	 * 
	 * @return 添加时间
	 */
	public Date getAddTime() {
		return addTime;
	}

	/**
	 * 设置添加时间
	 * 
	 * @param addTime 要设置的添加时间
	 */

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取添加IP
	 * 
	 * @return 添加IP
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置添加IP
	 * 
	 * @param addIp 要设置的添加IP
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	/**
	 * 图片路径
	 * @return 图片路径
	 */
	public String getPicPath() {
		return picPath;
	}
	/**
	 * 图片路径
	 * 
	 * @param pic 图片路径
	 */
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	
}
