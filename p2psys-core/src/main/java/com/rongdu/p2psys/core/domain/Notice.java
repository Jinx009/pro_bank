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

import com.rongdu.p2psys.user.domain.User;

/**
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_notice")
public class Notice implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 
	 */
	private String nid;
	/**
	 * 
	 */
	private String type;
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sent_user")
	private User sentUser;
	/**
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receive_user")
	private User receiveUser;
	/**
	 * 
	 */
	private int status;
	/**
	 * 
	 */
	private String title;
	/**
	 * 
	 */
	private String content;
	/**
	 * 
	 */
	private String result;
	/**
	 * 
	 */
	private String receiveAddr;
	/**
	 * 
	 */
	private Date addTime;

	/**
	 * 获取
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置
	 * 
	 * @param id 要设置的
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * 设置
	 * 
	 * @param nid 要设置的
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置
	 * 
	 * @param type 要设置的
	 */
	public void setType(String type) {
		this.type = type;
	}

	public User getSentUser() {
		return sentUser;
	}

	public void setSentUser(User sentUser) {
		this.sentUser = sentUser;
	}

	public User getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(User receiveUser) {
		this.receiveUser = receiveUser;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置
	 * 
	 * @param status 要设置的
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置
	 * 
	 * @param title 要设置的
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置
	 * 
	 * @param content 要设置的
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String getResult() {
		return result;
	}

	/**
	 * 设置
	 * 
	 * @param result 要设置的
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public String getReceiveAddr() {
		return receiveAddr;
	}

	/**
	 * 设置
	 * 
	 * @param receiveAddr 要设置的
	 */
	public void setReceiveAddr(String receiveAddr) {
		this.receiveAddr = receiveAddr;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

}
