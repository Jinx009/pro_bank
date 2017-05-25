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
 * 投标表
 */
@Entity
@Table(name = "rd_borrow_tender")
public class BorrowTender implements Serializable {

	private static final long serialVersionUID = 8565466657509869143L;

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 投资人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 借款标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "borrow_id")
	private Borrow borrow;

	/**
	 * 状态
	 * 
	 * <p>
	 * 0:投标待处理
	 * </p>
	 * <p>
	 * 1:成功
	 * </p>
	 * <p>
	 * 2:失败
	 * </p>
	 */
	private int status;

	/**
	 * 投标金额
	 */
	private double money;

	/**
	 * 借款总额
	 */
	private double account;

	/**
	 * 预还总额(+利息)
	 */
	private double repaymentAccount;

	/**
	 * 利息
	 */
	private double interest;

	/**
	 * 已还金额
	 */
	private double repaymentYesAccount;

	/**
	 * 已还利息
	 */
	private double repaymentYesInterest;

	/**
	 * 待还总额
	 */
	private double waitAccount;

	/**
	 * 待还利息
	 */
	private double waitInterest;

	/**
	 * 投标类型
	 * 
	 * <p>
	 * 0:网站投标
	 * </p>
	 * <p>
	 * 1:自动投标
	 * </p>
	 * <p>
	 * 2:手机投标
	 * </p>
	 * <p>
	 * 3:预约投标
	 * </p>
	 */
	private byte tenderType;

	/**
	 * 添加时间
	 */
	private Date addTime;

	/**
	 * IP
	 */
	private String addIp;

	/**
	 * 登记债权时候的订单号
	 */
	private String tenderBilNo;

	/**
	 * 登记债权时候的订单日期
	 */
	private String tenderBilDate;

	/**
	 * 投标冻结流水号
	 */
	private String trxId;

	/**
	 * 加息劵
	 */
	private double interestRateValue;

	/**
	 * 浮动收益率
	 */
	private double floatRate;

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

	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

	public double getRepaymentAccount() {
		return repaymentAccount;
	}

	public void setRepaymentAccount(double repaymentAccount) {
		this.repaymentAccount = repaymentAccount;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getRepaymentYesAccount() {
		return repaymentYesAccount;
	}

	public void setRepaymentYesAccount(double repaymentYesAccount) {
		this.repaymentYesAccount = repaymentYesAccount;
	}

	public double getRepaymentYesInterest() {
		return repaymentYesInterest;
	}

	public void setRepaymentYesInterest(double repaymentYesInterest) {
		this.repaymentYesInterest = repaymentYesInterest;
	}

	public double getWaitAccount() {
		return waitAccount;
	}

	public void setWaitAccount(double waitAccount) {
		this.waitAccount = waitAccount;
	}

	public double getWaitInterest() {
		return waitInterest;
	}

	public void setWaitInterest(double waitInterest) {
		this.waitInterest = waitInterest;
	}

	public byte getTenderType() {
		return tenderType;
	}

	public void setTenderType(byte tenderType) {
		this.tenderType = tenderType;
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

	public String getTenderBilNo() {
		return tenderBilNo;
	}

	public void setTenderBilNo(String tenderBilNo) {
		this.tenderBilNo = tenderBilNo;
	}

	public String getTenderBilDate() {
		return tenderBilDate;
	}

	public void setTenderBilDate(String tenderBilDate) {
		this.tenderBilDate = tenderBilDate;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public double getInterestRateValue() {
		return interestRateValue;
	}

	public void setInterestRateValue(double interestRateValue) {
		this.interestRateValue = interestRateValue;
	}

	public double getFloatRate() {
		return floatRate;
	}

	public void setFloatRate(double floatRate) {
		this.floatRate = floatRate;
	}

}
