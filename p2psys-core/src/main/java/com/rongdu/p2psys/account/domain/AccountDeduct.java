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

import com.alibaba.fastjson.annotation.JSONField;
import com.rongdu.common.util.RechargeUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.domain.User;

/**
 * 扣款记录
 * 
 * @author cx
 * @version 2.0
 * @since 2014-04-23
 */
@Entity
@Table(name = "rd_account_deduct")
public class AccountDeduct implements Serializable {
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
	 * 银行账号
	 */
	private String bankNo;
	

	/**
	 * 订单号
	 */
	private String tradeNo;
	/**
	 * 用户ID
	 */
	@JSONField
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 状态 0待审核 1扣款成功 2扣款失败
	 */

	private byte status;
	/**
	 * 金额
	 */
	private double money;
	/**
	 * 类型 2账户扣款
	 */
	private byte type;
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

	/**
	 * 构造函数
	 */
	public AccountDeduct() {
		super();
	}

	/**
	 * 扣款记录
	 * @param user 用户
	 * @param money 扣款金额
	 * @param remark 备注
	 */
	public AccountDeduct(User user, double money, String remark) {
		super();
		this.user = user;
		this.tradeNo = RechargeUtil.generateTradeNO(user.getUserId(), "E");
		this.money = money;
		this.type = 2;
		this.remark = remark;
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
	 * 银行账号
	 * @return String
	 */
	public String getBankNo() {
		return bankNo;
	}
	/**
	 * 设置银行账号
	 * @param bankNo 银行账号
	 */
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	/**
	 * 获取订单号
	 * 
	 * @return 订单号
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * 设置订单号
	 * 
	 * @param tradeNo 要设置的订单号
	 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
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
	 * @param user 要设置的用户
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取状态 1充值成功 2充值失败
	 * 
	 * @return 状态 1充值成功 2充值失败
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * 设置状态 1充值成功 2充值失败
	 * 
	 * @param status 要设置的状态 1充值成功 2充值失败
	 */
	public void setStatus(byte status) {
		this.status = status;
	}

	/**
	 * 获取金额
	 * 
	 * @return 金额
	 */
	public double getMoney() {
		return money;
	}

	/**
	 * 设置金额
	 * 
	 * @param money 要设置的金额
	 */
	public void setMoney(double money) {
		this.money = money;
	}

	/**
	 * 类型 2账户扣款
	 * @return byte
	 */
	public byte getType() {
		return type;
	}

	/**
	 * 类型 2账户扣款
	 * @param type 账户扣款
	 */
	public void setType(byte type) {
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
