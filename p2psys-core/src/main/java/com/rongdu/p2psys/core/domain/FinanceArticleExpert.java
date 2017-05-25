package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 文章作者
 * @author sj
 * @since 2015年3月25日14:17:15
 *
 */
@Entity
@Table(name = "rd_finance_article_expert")
public class FinanceArticleExpert implements Serializable {

	private static final long serialVersionUID = -5307543702502347135L;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 作者
	 */
	private String autorName;
	
	/**
	 * 职位
	 */
	private String position;
	
	/**
	 * 是否删除
	 */
	private int isDelete;
	
	/**
	 * 作者头像
	 */
	private String picPath;
	
	/**
	 * 微博路径
	 */
	private String blogUrl;
	
	/**
	 * 微信路径
	 */
	private String wechatPath;
	
	/**
	 * 作者简介
	 */
	private String content;
	
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getAutorName() {
		return autorName;
	}
	
	public void setAutorName(String autorName) {
		this.autorName = autorName;
	}
	
	public String getPosition() {
		return position;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getPicPath() {
		return picPath;
	}
	
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getBlogUrl() {
		return blogUrl;
	}

	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}

	public String getWechatPath() {
		return wechatPath;
	}

	public void setWechatPath(String wechatPath) {
		this.wechatPath = wechatPath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
