package com.rongdu.p2psys.nb.ppfund.domain;

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

import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.user.domain.User;

/**
 * 体验金表
 * 
 * @author cgw
 * @version 1.0
 * @since 2015-07-14
 */
@Entity
@Table(name = ("nb_experience_gold"))
public class ExperienceGold implements Serializable {
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
	 * 用户ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	/**
	 * 投资现金类的标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ppfund_id")
	private Ppfund ppfund;
	
	/**
	 * 0：正常，1：失效
	 */
	private int status;
	
	/**
	 * 有效天数
	 */
	private int days;
	
	/**
	 * 体验金额
	 */
	private double money;
	
	/**
	 * 发放时间
	 */
	private Date addTime;
	
	/**
	 * 投标时间
	 */
	private Date investTime;

	public ExperienceGold() {
		super();
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

	public Ppfund getPpfund() {
		return ppfund;
	}

	public void setPpfund(Ppfund ppfund) {
		this.ppfund = ppfund;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getInvestTime() {
		return investTime;
	}

	public void setInvestTime(Date investTime) {
		this.investTime = investTime;
	}

}
