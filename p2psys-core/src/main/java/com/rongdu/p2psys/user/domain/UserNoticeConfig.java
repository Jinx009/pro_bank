package com.rongdu.p2psys.user.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;

/**
 * 用户接收通知设置实体类
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = Global.DB_PREFIX + "user_notice_config")
public class UserNoticeConfig implements Serializable {
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 
	 */
	private String nid;
	/**
	 * 
	 */
	private int sms;
	/**
	 * 
	 */
	private int email;
	/**
	 * 
	 */
	private int message;

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
	public User getUser() {
		return user;
	}

	/**
	 * 设置
	 * 
	 * @param userId 要设置的
	 */
	public void setUser(User user) {
		this.user = user;
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
	public int getSms() {
		return sms;
	}

	/**
	 * 设置
	 * 
	 * @param sms 要设置的
	 */
	public void setSms(int sms) {
		this.sms = sms;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public int getEmail() {
		return email;
	}

	/**
	 * 设置
	 * 
	 * @param email 要设置的
	 */
	public void setEmail(int email) {
		this.email = email;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public int getMessage() {
		return message;
	}

	/**
	 * 设置
	 * 
	 * @param message 要设置的
	 */
	public void setMessage(int message) {
		this.message = message;
	}
}
