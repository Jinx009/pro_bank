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
 * 借款待收表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_borrow_collection")
public class BorrowCollection implements Serializable {
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
	 * 还款状态 0未收款 1已收款
	 */
	private int status;
	/**
	 * 期数
	 */
	private int period;
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
	 * 投标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tender_id")
	private BorrowTender tender;

	/**
	 * 预计还款时间
	 */
	private Date repaymentTime;
	/**
	 * 已经还款时间
	 */
	private Date repaymentYesTime;
	/**
	 * 预还金额
	 */
	private double repaymentAccount;
	/**
	 * 已还金额
	 */
	private double repaymentYesAccount;
	/**
	 * 本金
	 */
	private double capital;
	/**
	 * 利息
	 */
	private double interest;
	/**
	 * 逾期天数
	 */
	private int lateDays;
	/**
	 * 逾期利息
	 */
	private double lateInterest;
	/**
	 * 实际展期天数
	 */
	private int realExtensionDay;
	/**
	 * 展期费率
	 */
	private double extensionInterest;
	/**
	 * 利息管理费
	 */
	private double manageFee;
	/**
	 * 待收类型 0普通待收;1债权转让
	 */
	private int type;
	/**
	 * 时间
	 */
	private Date addTime;
	/**
	 * ip
	 */
	private String addIp;
	/**
	 * 待收金额
	 */
	private double repayAward;
	/**
	 * 0:还款奖励等待 1还款奖励已收
	 */
	private int repayAwardStatus;
	
	/**
	 * 加息劵利息
	 */
	private double interestRate;
	
	/**
	 * 已收加息劵利息
	 */
	private double interestRateYes;
	
	/**
	 * 已成功转出本金
	 */
	private double bondCapital;
	/**
	 * 已成功转出利息
	 */
	private double bondInterest;
	/**
	 * 已成功转出奖励
	 */
	private double bondAward;
	
	/**
	 * 浮动收益
	 */
	private double floatIncome;

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
	 * 获取还款状态 0未还款 1已还款
	 * 
	 * @return 还款状态 0未还款 1已还款
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置还款状态 0未还款 1已还款
	 * 
	 * @param status 要设置的还款状态 0未还款 1已还款
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取期数
	 * 
	 * @return 期数
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * 设置期数
	 * 
	 * @param period 要设置的期数
	 */
	public void setPeriod(int period) {
		this.period = period;
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

	public BorrowTender getTender() {
		return tender;
	}

	public void setTender(BorrowTender tender) {
		this.tender = tender;
	}

	/**
	 * 获取预计还款时间
	 * 
	 * @return 预计还款时间
	 */
	public Date getRepaymentTime() {
		return repaymentTime;
	}

	/**
	 * 设置预计还款时间
	 * 
	 * @param repayTime 要设置的预计还款时间
	 */
	public void setRepaymentTime(Date repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	/**
	 * 获取已经还款时间
	 * 
	 * @return 已经还款时间
	 */
	public Date getRepaymentYesTime() {
		return repaymentYesTime;
	}

	/**
	 * 设置已经还款时间
	 * 
	 * @param repayYesTime 要设置的已经还款时间
	 */
	public void setRepaymentYesTime(Date repaymentYesTime) {
		this.repaymentYesTime = repaymentYesTime;
	}

	/**
	 * 获取预还金额
	 * 
	 * @return 预还金额
	 */
	public double getRepaymentAccount() {
		return repaymentAccount;
	}

	/**
	 * 设置预还金额
	 * 
	 * @param repayAccount 要设置的预还金额
	 */
	public void setRepaymentAccount(double repaymentAccount) {
		this.repaymentAccount = repaymentAccount;
	}

	/**
	 * 获取已还金额
	 * 
	 * @return 已还金额
	 */
	public double getRepaymentYesAccount() {
		return repaymentYesAccount;
	}

	/**
	 * 设置已还金额
	 * 
	 * @param repayYesAccount 要设置的已还金额
	 */
	public void setRepaymentYesAccount(double repaymentYesAccount) {
		this.repaymentYesAccount = repaymentYesAccount;
	}

	/**
	 * 获取本金
	 * 
	 * @return 本金
	 */
	public double getCapital() {
		return capital;
	}

	/**
	 * 设置本金
	 * 
	 * @param capital 要设置的本金
	 */
	public void setCapital(double capital) {
		this.capital = capital;
	}

	/**
	 * 获取利息
	 * 
	 * @return 利息
	 */
	public double getInterest() {
		return interest;
	}

	/**
	 * 设置利息
	 * 
	 * @param interest 要设置的利息
	 */
	public void setInterest(double interest) {
		this.interest = interest;
	}

	/**
	 * 获取逾期天数
	 * 
	 * @return 逾期天数
	 */
	public int getLateDays() {
		return lateDays;
	}

	/**
	 * 设置逾期天数
	 * 
	 * @param lateDays 要设置的逾期天数
	 */
	public void setLateDays(int lateDays) {
		this.lateDays = lateDays;
	}

	/**
	 * 获取逾期利息
	 * 
	 * @return 逾期利息
	 */
	public double getLateInterest() {
		return lateInterest;
	}

	/**
	 * 设置逾期利息
	 * 
	 * @param lateInterest 要设置的逾期利息
	 */
	public void setLateInterest(double lateInterest) {
		this.lateInterest = lateInterest;
	}

	/**
	 * 获取实际展期天数
	 * 
	 * @return 实际展期天数
	 */
	public int getRealExtensionDay() {
		return realExtensionDay;
	}

	/**
	 * 设置实际展期天数
	 * 
	 * @param realExtensionDay 要设置的实际展期天数
	 */
	public void setRealExtensionDay(int realExtensionDay) {
		this.realExtensionDay = realExtensionDay;
	}

	/**
	 * 获取展期费率
	 * 
	 * @return 展期费率
	 */
	public double getExtensionInterest() {
		return extensionInterest;
	}

	/**
	 * 设置展期费率
	 * 
	 * @param extensionInterest 要设置的展期费率
	 */
	public void setExtensionInterest(double extensionInterest) {
		this.extensionInterest = extensionInterest;
	}

	/**
	 * 获取利息管理费
	 * 
	 * @return 利息管理费
	 */
	public double getManageFee() {
		return manageFee;
	}

	/**
	 * 设置利息管理费
	 * 
	 * @param manageFee 要设置的利息管理费
	 */
	public void setManageFee(double manageFee) {
		this.manageFee = manageFee;
	}

	/**
	 * 获取待收类型 0普通待收;1债权转让
	 * 
	 * @return 待收类型 0普通待收;1债权转让
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置待收类型 0普通待收;1债权转让
	 * 
	 * @param type 要设置的待收类型 0普通待收;1债权转让
	 */
	public void setType(int type) {
		this.type = type;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取ip
	 * 
	 * @return ip
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置ip
	 * 
	 * @param addIp 要设置的ip
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获取待收金额
	 * 
	 * @return 待收金额
	 */
	public double getRepayAward() {
		return repayAward;
	}

	/**
	 * 设置待收金额
	 * 
	 * @param repayAward 要设置的待收金额
	 */
	public void setRepayAward(double repayAward) {
		this.repayAward = repayAward;
	}

	/**
	 * 获取0:还款奖励等待 1还款奖励已收
	 * 
	 * @return 0:还款奖励等待 1还款奖励已收
	 */
	public int getRepayAwardStatus() {
		return repayAwardStatus;
	}

	/**
	 * 设置0:还款奖励等待 1还款奖励已收
	 * 
	 * @param repayAwardStatus 要设置的0:还款奖励等待 1还款奖励已收
	 */
	public void setRepayAwardStatus(int repayAwardStatus) {
		this.repayAwardStatus = repayAwardStatus;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public double getInterestRateYes() {
		return interestRateYes;
	}

	public void setInterestRateYes(double interestRateYes) {
		this.interestRateYes = interestRateYes;
	}

	public double getBondCapital() {
		return bondCapital;
	}

	public void setBondCapital(double bondCapital) {
		this.bondCapital = bondCapital;
	}

	public double getBondInterest() {
		return bondInterest;
	}

	public void setBondInterest(double bondInterest) {
		this.bondInterest = bondInterest;
	}

	public double getBondAward() {
		return bondAward;
	}

	public void setBondAward(double bondAward) {
		this.bondAward = bondAward;
	}

	public double getFloatIncome() {
		return floatIncome;
	}

	public void setFloatIncome(double floatIncome) {
		this.floatIncome = floatIncome;
	}
}
