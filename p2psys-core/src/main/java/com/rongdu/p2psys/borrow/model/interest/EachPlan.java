package com.rongdu.p2psys.borrow.model.interest;

import java.util.Date;

/**
 * 每期还款计划
 * 
 * @author：xx
 * @version 1.0
 * @since 2014年7月15日
 */
public class EachPlan {

	/** 本金 */
	private double capital;
	/** 利息 */
	private double interest;
	/** 净收利息（扣除管理费后） */
	private double netInterest;
	/** 本息 */
	private double total;
	/** 净收本息（扣除管理费后） */
	private double netTotal;
	/** 未还本息 */
	private double needRepay;
	/** 开始计息日 */
	private Date interestTime;
	/** 还款日 */
	private Date repayTime;

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
	 * 获取净收利息（扣除管理费后）
	 * 
	 * @return 净收利息（扣除管理费后）
	 */
	public double getNetInterest() {
		return netInterest;
	}

	/**
	 * 设置净收利息（扣除管理费后）
	 * 
	 * @param netInterest 要设置的净收利息（扣除管理费后）
	 */
	public void setNetInterest(double netInterest) {
		this.netInterest = netInterest;
	}

	/**
	 * 获取本息
	 * 
	 * @return 本息
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * 设置本息
	 * 
	 * @param total 要设置的本息
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * 获取净收本息（扣除管理费后）
	 * 
	 * @return 净收本息（扣除管理费后）
	 */
	public double getNetTotal() {
		return netTotal;
	}

	/**
	 * 设置净收本息（扣除管理费后）
	 * 
	 * @param netTotal 要设置的净收本息（扣除管理费后）
	 */
	public void setNetTotal(double netTotal) {
		this.netTotal = netTotal;
	}

	/**
	 * 获取未还本息
	 * 
	 * @return 未还本息
	 */
	public double getNeedRepay() {
		return needRepay;
	}

	/**
	 * 设置未还本息
	 * 
	 * @param needRepay 要设置的未还本息
	 */
	public void setNeedRepay(double needRepay) {
		this.needRepay = needRepay;
	}

	/**
	 * 获取开始计息日
	 * 
	 * @return 开始计息日
	 */
	public Date getInterestTime() {
		return interestTime;
	}

	/**
	 * 设置开始计息日
	 * 
	 * @param interestTime 要设置的开始计息日
	 */
	public void setInterestTime(Date interestTime) {
		this.interestTime = interestTime;
	}

	/**
	 * 获取还款日
	 * 
	 * @return 还款日
	 */
	public Date getRepayTime() {
		return repayTime;
	}

	/**
	 * 设置还款日
	 * 
	 * @param repayTime 要设置的还款日
	 */
	public void setRepayTime(Date repayTime) {
		this.repayTime = repayTime;
	}

}
