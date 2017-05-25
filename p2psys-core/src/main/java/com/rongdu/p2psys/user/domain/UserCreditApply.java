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
 * 信用额度申请表
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_user_credit_apply")
public class UserCreditApply implements Serializable {
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 申请额度
	 */
	private double account;
	/**
	 * 审核后总的信用额度
	 */
	private double accountNew;
	/**
	 * 审核前总的信用额度
	 */
	private double accountOld;
	/**
	 * 状态 2:申请 1：审核通过 -1不通过
	 */
	private int status;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 审核备注
	 */
	private String verifyRemark;
	/**
	 * 审核时间
	 */
	private Date verifyTime;
	/**
	 * 审核人
	 */
	private long verifyUser;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;

	public UserCreditApply() {
		super();
	}

	public UserCreditApply(User user, double account, int status, UserCredit uAmount, String content, String remark) {
		super();
		this.user = user;
		this.account = account;
		this.status = status;
		if (uAmount != null) {
			this.accountOld = uAmount.getCredit();
		}
		this.content = content;
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
	public double getAccount() {
		return account;
	}

	/**
	 * 设置
	 * 
	 * @param account 要设置的
	 */
	public void setAccount(double account) {
		this.account = account;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public double getAccountNew() {
		return accountNew;
	}

	/**
	 * 设置
	 * 
	 * @param accountNew 要设置的
	 */
	public void setAccountNew(double accountNew) {
		this.accountNew = accountNew;
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public double getAccountOld() {
		return accountOld;
	}

	/**
	 * 设置
	 * 
	 * @param accountOld 要设置的
	 */
	public void setAccountOld(double accountOld) {
		this.accountOld = accountOld;
	}

	/**
	 * 获取状态
	 * 
	 * @return 状态
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置状态
	 * 
	 * @param status 要设置的状态
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content 要设置的内容
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * 获取审核备注
	 * 
	 * @return 审核备注
	 */
	public String getVerifyRemark() {
		return verifyRemark;
	}

	/**
	 * 设置审核备注
	 * 
	 * @param verifyRemark 要设置的审核备注
	 */
	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	/**
	 * 获取审核人
	 * 
	 * @return 审核人
	 */
	public long getVerifyUser() {
		return verifyUser;
	}

	/**
	 * 设置审核人
	 * 
	 * @param verifyUser 要设置的审核人
	 */
	public void setVerifyUser(long verifyUser) {
		this.verifyUser = verifyUser;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
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
