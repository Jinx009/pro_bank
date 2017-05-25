package com.rongdu.p2psys.user.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 推广人功能实体类
 * 
 * @author sj
 *
 */
@Entity
@Table(name = "rd_user_promot")
public class UserPromot implements Serializable {

	private static final long serialVersionUID = 8211030514725542935L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 用户ID
	 */
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 状态
	 */
	private int status;

	/**
	 * 优惠码
	 */
	private String couponCode;

	/**
	 * 可使用次数
	 */
	private int canUseTimes;

	/**
	 * 已使用次数
	 */
	private int usedTimes;

	/**
	 * 收益比例
	 */
	private double rate;

	private Date addTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public int getCanUseTimes() {
		return canUseTimes;
	}

	public void setCanUseTimes(int canUseTimes) {
		this.canUseTimes = canUseTimes;
	}

	public int getUsedTimes() {
		return usedTimes;
	}

	public void setUsedTimes(int usedTimes) {
		this.usedTimes = usedTimes;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

}
