package com.rongdu.p2psys.borrow.model.interest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;

/**
 * 一次性还款利息计算函数
 * 
 * @author：xx
 * @version 1.0
 * @since 2014年7月15日
 */
public class OnetimeRepaymentCalculator implements InterestCalculator {

	/** 借款金额 */
	private double account;
	/** 年利率 */
	private double yearApr;
	/** 利息管理费 */
	private double manageFee;
	/** 天利率 */
	private double dayApr;
	/** 期数 */
	private int periods;
	/** 还款金额 */
	private double repayAccount;
	/** 开始计息日 */
	private Date interestTime;
	/** 还款日 */
	private Date repayTime;
	/** 还款计划 */
	private List<EachPlan> eachPlan;

	/**
	 * 初始化一次性还款构造函数
	 * @param account 借款总额
	 * @param yearApr 年利率
	 * @param interestTime 开始计息日
	 * @param periods 期数
	 * @param manageFee 利息管理费
	 */
	public OnetimeRepaymentCalculator(double account, double yearApr, Date interestTime,
			int periods, double manageFee) {
		super();
		this.account = account;
		this.yearApr = yearApr;
		this.manageFee = manageFee;
		this.periods = periods;
		this.interestTime = interestTime;
		eachPlan = new ArrayList<EachPlan>();
	}

	@Override
	public List<EachPlan> calculator() {
		EachPlan e = new EachPlan();
		double i = account * yearApr / 12 * periods;
		e.setCapital(account);
		e.setInterest(i);
		double netInterest = BigDecimalUtil.round(BigDecimalUtil.mul(i, BigDecimalUtil.sub(1, manageFee)), 2);
		e.setNetInterest(netInterest);
		double netTotal = BigDecimalUtil.add(account, netInterest);
		e.setNetTotal(netTotal);
		e.setTotal(i + account);
		e.setInterestTime(interestTime);
		e.setRepayTime(this.calculatorRepaytime(interestTime));
		eachPlan.add(e);
		// 汇总信息
		this.repayAccount = e.getTotal();
		repayTime = e.getRepayTime();
		return eachPlan;
	}

	@Override
	public List<EachPlan> calculator(int days) {
//		this.dayApr = BigDecimalUtil.div(this.yearApr, 365);
		eachPlan = new ArrayList<EachPlan>();
		EachPlan e = new EachPlan();
//		double i = BigDecimalUtil.round(BigDecimalUtil.mul(dayApr, account * days), 2);
		double i = BigDecimalUtil.round(BigDecimalUtil.mul(this.yearApr, account * days/365), 2);
		e.setCapital(account);
		e.setInterest(i);
		double netInterest = BigDecimalUtil.round(BigDecimalUtil.mul(i, BigDecimalUtil.sub(1, manageFee)), 2);
		e.setNetInterest(netInterest);
		double total = BigDecimalUtil.round(BigDecimalUtil.add(account, i), 2);
		e.setTotal(total);
		double netTotal = BigDecimalUtil.round(BigDecimalUtil.add(account, netInterest), 2);
		e.setNetTotal(netTotal);
		e.setInterestTime(interestTime);
		e.setRepayTime(this.calculatorRepaytime(interestTime));
		eachPlan.add(e);
		// 汇总信息
		this.repayAccount = e.getTotal();
		repayTime = e.getRepayTime();
		return eachPlan;
	}

	@Override
	public List<EachPlan> getEachPlan() {
		return eachPlan;
	}

	@Override
	public double repayTotal() {
		return repayAccount;
	}

	@Override
	public int repayPeriods() {
		return periods;
	}

	@Override
	public Date calculatorRepaytime(Date date) {
		return DateUtil.rollMon(date, 1);
	}

	@Override
	public Date repayTime() {
		return repayTime;
	}
}
