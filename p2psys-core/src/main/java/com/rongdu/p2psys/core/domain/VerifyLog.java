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
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;

/**
 * 审核记录表
 */
@Entity
@Table(name = "rd_verify_log")
public class VerifyLog implements Serializable {

	private static final long serialVersionUID = -6403958545604504331L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 审核人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verify_user")
	private Operator verifyUser;

	/**
	 * 数据类型
	 * 
	 * <p>
	 * ppfund(现金类)
	 * </p>
	 * <p>
	 * borrow(非现金类)
	 * </p>
	 * <p>
	 * crowdfunding(众筹)
	 * </p>
	 * <p>
	 * productset(组合)
	 * </p>
	 */
	private String type;

	/**
	 * 数据ID
	 */
	private long fid;

	/**
	 * 审核类型
	 * 
	 * <p>
	 * 1:初审
	 * </p>
	 * <p>
	 * 2:复审
	 * </p>
	 */
	private int verifyType;

	/**
	 * 结果
	 * 
	 * <p>
	 * 1:通过
	 * </p>
	 * <p>
	 * 2:不通过
	 * </p>
	 */
	private int result;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 时间
	 */
	private Date time;

	/**
	 * IP
	 */
	private String ip;

	public VerifyLog() {
		super();
	}

	public VerifyLog(String type, long fid) {
		super();
		this.type = type;
		this.fid = fid;
		this.verifyType = 1;
		this.time = new Date();
		this.ip = Global.getIP();
	}

	public VerifyLog(Operator verifyUser, String type, long fid,
			int verifyType, int result, String remark) {
		super();
		this.verifyUser = verifyUser;
		this.type = type;
		this.fid = fid;
		this.verifyType = verifyType;
		this.result = result;
		this.remark = remark;
		this.time = new Date();
		this.ip = Global.getIP();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Operator getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(Operator verifyUser) {
		this.verifyUser = verifyUser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getFid() {
		return fid;
	}

	public void setFid(long fid) {
		this.fid = fid;
	}

	public int getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(int verifyType) {
		this.verifyType = verifyType;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
