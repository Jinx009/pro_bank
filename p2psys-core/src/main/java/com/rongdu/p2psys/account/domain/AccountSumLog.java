package com.rongdu.p2psys.account.domain;

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
 * 用户资金合计日志表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_account_sum_log")
public class AccountSumLog implements Serializable {
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
	 * 获得资金的用户ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 提供资金的用户ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id")
	private User toUser;
	/**
	 * 变动前的金额
	 */
	private double beforeMoney;
	/**
	 * 变动金额
	 */
	private double money;
	/**
	 * 变动后金额
	 */
	private double afterMoney;
	/**
	 * 记录信息类型
	 */
	private String type;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;

	public AccountSumLog() {
		super();
	}

	public AccountSumLog(User user, String type, User toUser) {
		super();
		this.user = user;
		this.type = type;
		this.toUser = toUser;
		this.addIp = Global.getIP();
		this.addTime = new Date();
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
	 * 获取获得资金的用户ID
	 * 
	 * @return 获得资金的用户ID
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置获得资金的用户ID
	 * 
	 * @param user 要设置的获得资金的用户
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取提供资金的用户ID
	 * 
	 * @return 提供资金的用户ID
	 */
	public User getToUser() {
		return toUser;
	}

	/**
	 * 设置提供资金的用户
	 * 
	 * @param toUser 要设置的提供资金的用户
	 */
	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	/**
	 * 获取变动前的金额
	 * 
	 * @return 变动前的金额
	 */
	public double getBeforeMoney() {
		return beforeMoney;
	}

	/**
	 * 设置变动前的金额
	 * 
	 * @param beforeMoney 要设置的变动前的金额
	 */
	public void setBeforeMoney(double beforeMoney) {
		this.beforeMoney = beforeMoney;
	}

	/**
	 * 获取变动金额
	 * 
	 * @return 变动金额
	 */
	public double getMoney() {
		return money;
	}

	/**
	 * 设置变动金额
	 * 
	 * @param money 要设置的变动金额
	 */
	public void setMoney(double money) {
		this.money = money;
	}

	/**
	 * 获取变动后金额
	 * 
	 * @return 变动后金额
	 */
	public double getAfterMoney() {
		return afterMoney;
	}

	/**
	 * 设置变动后金额
	 * 
	 * @param afterMoney 要设置的变动后金额
	 */
	public void setAfterMoney(double afterMoney) {
		this.afterMoney = afterMoney;
	}

	/**
	 * 获取记录信息类型
	 * 
	 * @return 记录信息类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置记录信息类型
	 * 
	 * @param type 要设置的记录信息类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * 
	 * @param remark 要设置的备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
}
