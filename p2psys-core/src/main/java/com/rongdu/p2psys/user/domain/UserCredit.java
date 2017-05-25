package com.rongdu.p2psys.user.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 用户信用表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_user_credit")
public class UserCredit implements Serializable {
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
	 * 用户
	 */
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 
	 */
	private double credit;
	/**
	 * 可用信用额度
	 */
	@Column(name = "credit_use")
	private double creditUse;
	/**
	 * 冻结信用额度
	 */
	@Column(name = "credit_nouse")
	private double creditNouse;

	public UserCredit() {
		super();
	}

	public UserCredit(User user) {
		super();
		this.user = user;
	}

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
	 * 所属用户
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public double getCredit() {
		return credit;
	}

	/**
	 * 设置
	 * 
	 * @param credit 要设置的
	 */
	public void setCredit(double credit) {
		this.credit = credit;
	}

	/**
	 * 获取可用信用额度
	 * 
	 * @return 可用信用额度
	 */
	public double getCreditUse() {
		return creditUse;
	}

	/**
	 * 设置可用信用额度
	 * 
	 * @param creditUse 要设置的可用信用额度
	 */
	public void setCreditUse(double creditUse) {
		this.creditUse = creditUse;
	}

	/**
	 * 获取冻结信用额度
	 * 
	 * @return 冻结信用额度
	 */
	public double getCreditNouse() {
		return creditNouse;
	}

	/**
	 * 设置冻结信用额度
	 * 
	 * @param creditNouse 要设置的冻结信用额度
	 */
	public void setCreditNouse(double creditNouse) {
		this.creditNouse = creditNouse;
	}
}
