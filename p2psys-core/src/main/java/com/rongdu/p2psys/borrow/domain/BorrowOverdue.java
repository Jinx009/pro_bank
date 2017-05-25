package com.rongdu.p2psys.borrow.domain;

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
 * 逾期垫付
 * @author：zf
 * @version 2.0
 * @since 2014年8月6日
 */
@Entity
@Table(name = "rd_borrow_overdue")
public class BorrowOverdue implements Serializable {

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
	 * 垫付金额
	 */
	private double overdueAccount;
	
	/**
	 * 垫付时间
	 */
	private Date overdueTime;
	
	/**
	 * 借款人用户名
	 */
	private String username;
	
	/**
	 * 还款金额
	 */
	private double repaymentAccount;
	
	/**
	 * 还款时间
	 */
	private Date repaymentTime;
	
	/**
	 * 操作者
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "op_id")
	private Operator operator;
	/**
	 * 主键
	 * @return long
	 */
	public long getId() {
		return id;
	}
	/**
	 * 设置主键
	 * @param id 主键
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * 垫付金额
	 * @return double
	 */
	public double getOverdueAccount() {
		return overdueAccount;
	}
	/**
	 * 设置垫付金额
	 * @param overdueAccount 垫付金额
	 */
	public void setOverdueAccount(double overdueAccount) {
		this.overdueAccount = overdueAccount;
	}
	/**
	 * 垫付时间
	 * @return Date
	 */
	public Date getOverdueTime() {
		return overdueTime;
	}
	/**
	 * 设置垫付时间
	 * @param overdueTime 垫付时间
	 */
	public void setOverdueTime(Date overdueTime) {
		this.overdueTime = overdueTime;
	}
	/**
	 * 获得借款人用户名
	 * @return String
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置借款人用户名
	 * @param username 借款人用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 还款金额
	 * @return double
	 */
	public double getRepaymentAccount() {
		return repaymentAccount;
	}
	/**
	 * 设置还款金额
	 * @param repaymentAccount 还款金额
	 */
	public void setRepaymentAccount(double repaymentAccount) {
		this.repaymentAccount = repaymentAccount;
	}
	/**
	 * 还款时间
	 * @return Date
	 */
	public Date getRepaymentTime() {
		return repaymentTime;
	}
	/**
	 * 设置还款时间
	 * @param repaymentTime 还款时间
	 */
	public void setRepaymentTime(Date repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	/**
	 * 操作者
	 * @return Operator
	 */
	public Operator getOperator() {
		return operator;
	}
	/**
	 * 设置操作者
	 * @param operator 操作者
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	
	
	
}
