package com.rongdu.p2psys.borrow.domain;

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

@Entity
@Table(name = "rd_borrow_bespeak_pic")
public class BorrowBespeakPic implements Serializable {

	private static final long serialVersionUID = 5971062658230649985L;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 预约借款ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "borrow_bespeak_id")
	private BorrowBespeak borrowBespeak;
	
	/**
	 * 上传图片
	 */
	private String picPath;
	
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

	public BorrowBespeak getBorrowBespeak() {
		return borrowBespeak;
	}

	public void setBorrowBespeak(BorrowBespeak borrowBespeak) {
		this.borrowBespeak = borrowBespeak;
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

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	
}
