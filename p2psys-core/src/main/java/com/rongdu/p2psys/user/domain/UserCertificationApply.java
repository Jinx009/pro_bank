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

import com.rongdu.p2psys.core.domain.Operator;

/**
 * 认证材料申请表
 * @author zf
 * @version 2.0
 * @since 2014-11-07
 */
@Entity
@Table(name = "rd_user_certification_apply")
public class UserCertificationApply implements Serializable {
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
	 * 申请用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 审核管理员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operator_id")
	private Operator operator;
	/**
	 * 材料类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	private CertificationType type;
	
	/**
	 * 状态0:待审核 1：审核通过 -1不通过
	 */
	private int status;

	/**
	 * 资料审核评分
	 */
	private int score;
	
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 审核时间
	 */
	private Date verifyTime;
	/**
	 * 审核备注
	 */
	private String verifyRemark;
	/**
	 * 添加IP
	 */
	private String addIp;

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
	
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public CertificationType getType() {
		return type;
	}

	public void setType(CertificationType type) {
		this.type = type;
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
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getVerifyRemark() {
		return verifyRemark;
	}

	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}
}
