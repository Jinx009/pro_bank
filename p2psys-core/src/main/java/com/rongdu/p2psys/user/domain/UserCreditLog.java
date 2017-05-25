package com.rongdu.p2psys.user.domain;

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

/**
 * 信用日志信息表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_user_credit_log")
public class UserCreditLog implements Serializable {
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
	 * 类型
	 */
	private String type;

	/**
	 * 申请额度
	 */
	private double account;
	/**
	 * 审核后总的信用额度
	 */
	private double accountAll;
	/**
	 * 可用资金
	 */
	private double accountUse;
	/**
	 * 冻结资金
	 */
	private double accountNoUse;
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

	public UserCreditLog() {
		super();
		this.addTime = new Date();
		this.addIp = Global.getIP();
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
	 * 获取用户ID
	 * 
	 * @return 用户ID
	 */

	public User getUser() {
		return user;
	}

	/**
	 * 设置用户ID
	 * 
	 * @param userId 要设置的用户ID
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type 要设置的类型
	 */
	public void setType(String type) {
		this.type = type;
	}
    /**
	 * 获取资金
	 * 
	 * @return 资金
	 */
	public double getAccount() {
		return account;
	}

	/**
	 * 设置资金
	 * 
	 * @param account 要设置的资金
	 */
	public void setAccount(double account) {
		this.account = account;
	}

	/**
	 * 获取总资金
	 * 
	 * @return 总资金
	 */
	public double getAccountAll() {
		return accountAll;
	}

	/**
	 * 设置总资金
	 * 
	 * @param accountAll 要设置的总资金
	 */
	public void setAccountAll(double accountAll) {
		this.accountAll = accountAll;
	}

	/**
	 * 获取可用资金
	 * 
	 * @return 可用资金
	 */
	public double getAccountUse() {
		return accountUse;
	}

	/**
	 * 设置可用资金
	 * 
	 * @param accountUse 要设置的可用资金
	 */
	public void setAccountUse(double accountUse) {
		this.accountUse = accountUse;
	}

	public double getAccountNoUse() {
        return accountNoUse;
    }

    public void setAccountNoUse(double accountNoUse) {
        this.accountNoUse = accountNoUse;
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

	public Date getAddTime() {
		return addTime;
	}

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
