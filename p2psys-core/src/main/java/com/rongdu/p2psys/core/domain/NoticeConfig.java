package com.rongdu.p2psys.core.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_notice_config")
public class NoticeConfig implements Serializable {
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
	private String type;
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
	private int letters;

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
	public int getLetters() {
		return letters;
	}

	/**
	 * 设置
	 * 
	 * @param letters 要设置的
	 */
	public void setLetters(int letters) {
		this.letters = letters;
	}
}
