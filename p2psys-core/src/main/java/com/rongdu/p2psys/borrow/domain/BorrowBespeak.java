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

import com.rongdu.p2psys.user.domain.User;

/**
 * 预约借款
 * @author sj
 * @version 2.0
 * @since 2014-08-20
 */
/**
 * @author Administrator
 *
 */
@Entity
@Table(name = "rd_borrow_bespeak")
public class BorrowBespeak implements Serializable {

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
	 * 类型 1个人预约借款 2企业预约借款
	 */
	private int type;
	
	/**
	 * 月均收入
	 */
	private double averageMoney;
	
	/**
	 * 婚姻状况
	 */
	private int marryStatus;
	
	/**
	 * 借款金额
	 */
	private double money;
	
	/**
	 * 借款期限
	 */
	private int timeLimit;
	
	/**
	 * 申请贷款种类
	 */
	private int loanType;
	
	/**
	 * 借款用途
	 */
	private String borrowUse;
	
	/**
	 * 还款来源
	 */
	private int repaySource;
	
	/**
	 * 借款利率
	 */
	private double apr;
	
	/**
	 * 省
	 */
	private String province;
	
	/**
	 * 市
	 */
	private String city;
	
	/**
	 * 区
	 */
	private String area;
	
	/**
	 * 所属行业
	 */
	private String industry;
	
	/**
	 * 固定资产总额
	 */
	private String fixedMoney;
	
	/**
	 * 状态 0未处理 1 已处理 2不处理
	 */
	private int status;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 添加时间
	 */
	private Date addTime;
	
	/**
	 * 处理时间
	 */
	private Date doTime;
	
	/**
	 * 添加IP
	 */
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getAverageMoney() {
		return averageMoney;
	}

	public void setAverageMoney(double averageMoney) {
		this.averageMoney = averageMoney;
	}

	public int getMarryStatus() {
		return marryStatus;
	}

	public void setMarryStatus(int marryStatus) {
		this.marryStatus = marryStatus;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getLoanType() {
		return loanType;
	}

	public void setLoanType(int loanType) {
		this.loanType = loanType;
	}

	public String getBorrowUse() {
		return borrowUse;
	}

	public void setBorrowUse(String borrowUse) {
		this.borrowUse = borrowUse;
	}

	public int getRepaySource() {
		return repaySource;
	}

	public void setRepaySource(int repaySource) {
		this.repaySource = repaySource;
	}

	public double getApr() {
		return apr;
	}

	public void setApr(double apr) {
		this.apr = apr;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getFixedMoney() {
		return fixedMoney;
	}

	public void setFixedMoney(String fixedMoney) {
		this.fixedMoney = fixedMoney;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public Date getDoTime() {
		return doTime;
	}

	public void setDoTime(Date doTime) {
		this.doTime = doTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	
}
