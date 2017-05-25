package com.rongdu.p2psys.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.domain.User;

/**
 * 操作日志类
 * 
 * @author cx
 * @version 2.0
 * @since 2014-4-22
 */
@Entity
@Table(name = "rd_operation_log")
public class OperationLog implements Serializable {

	private static final long serialVersionUID = 1L;

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
	 * 审核人
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verify_user")
	private Operator verifyUser;

	/**
	 * 操作类型
	 */
	private String type;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * ip
	 */
	private String addIp;
	private String operationResult;
	/**
	 * 订单号
	 */
	private String orderNo;

	public OperationLog() {
		super();
	}

	public OperationLog(User user, Operator verifyUser, String type) {
		super();
		this.user = user;
		this.verifyUser = verifyUser;
		this.type = type;
		this.addTime = new Date();
		this.addIp = Global.getIP();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(String operationResult) {
		this.operationResult = operationResult;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

}
