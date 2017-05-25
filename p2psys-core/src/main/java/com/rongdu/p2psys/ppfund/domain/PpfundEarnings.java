package com.rongdu.p2psys.ppfund.domain;

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

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund资金管理产品收益记录
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月19日
 */
@Entity
@Table(name = Global.DB_PREFIX + "ppfund_earnings")
public class PpfundEarnings implements Serializable {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 收益人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 购买记录
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ppfund_in_id")
	private PpfundIn ppfundIn;

	/**
	 * 收益金额
	 */
	private double money;

	/**
	 * 收益时间
	 */
	private Date addTime;

	public PpfundEarnings() {
		super();
	}

	public PpfundEarnings(User user, PpfundIn ppfundIn, double money) {
		super();
		this.user = user;
		this.ppfundIn = ppfundIn;
		this.money = money;
		this.addTime = new Date();
	}

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
	
	public PpfundIn getPpfundIn() {
		return ppfundIn;
	}

	public void setPpfundIn(PpfundIn ppfundIn) {
		this.ppfundIn = ppfundIn;
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
}
