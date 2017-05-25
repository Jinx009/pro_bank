package com.rongdu.p2psys.user.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Operator;

/**
 * 用户冻结实体类
 * 
 * @author sj
 * @version 2.0
 * @since 2014-04-21
 */
@Entity
@Table(name = Global.DB_PREFIX + "user_freeze")
public class UserFreeze implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 提现
	 */
	public static final String CASH = "cash";
	
	/**
	 * 充值
	 */
	public static final String RECHARGE = "recharge";
	
	/**
	 * 投标
	 */
	public static final String TENDER = "tender";
	
	/**
	 * 登录
	 */
	public static final String LOGIN = "login";
	
	/**
	 * 支付
	 */
	public static final String PAY = "pay";

	/** 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/** 用户 ID */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/** 用户 ID */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verify_user")
	private Operator verifyUser;

	/** 状态 */
	private int status;

	/** 冻结标示 */
	private String mark;

	/** 备注 */
	private String remark;

	/** 添加时间 */
	private Date addTime;

	/** 添加IP */
	private String addIp;

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

	public Operator getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(Operator verifyUser) {
		this.verifyUser = verifyUser;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

}
