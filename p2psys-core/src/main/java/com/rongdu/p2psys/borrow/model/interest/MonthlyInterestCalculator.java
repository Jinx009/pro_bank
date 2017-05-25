package com.rongdu.p2psys.borrow.model.interest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.borrow.exception.BorrowException;

/**
 * 每月还息到期还本利息计算函数
 * 
 * @author：fuxingxing
 * @version 1.0
 * @since 2014年7月15日
 */
public class MonthlyInterestCalculator implements InterestCalculator {
	/** 借款金额 */
	private double account;
	/** 年利率 */
	private double yearApr;
	/** 利息管理费 */
	private double manageFee;
	/** 期数 */
	private int periods;
	/** 标记是否为满标复审 */
	private boolean flag;
	/** 还款金额 */
	private double repayAccount;
	/** 开始计息日 */
	private Date interestTime;
	/** 还款日 */
	private Date repayTime;
	/** 还款计划 */
	private List<EachPlan> eachPlan;

	/**
	 * 初始化每月还息到期还本构造函数
	 * 
	 * @param account 借款金额
	 * @param yearApr 年利率
	 * @param interestTime 开始计息日
	 * @param periods 期数
	 * @param flag ？？？
	 * @param manageFee 利率管理费
	 */
	public MonthlyInterestCalculator(double account, double yearApr, Date interestTime, int periods, boolean flag,
			double manageFee) {
		this.account = account;
		this.yearApr = yearApr;
		this.periods = periods;
		this.manageFee = manageFee;
		this.interestTime = interestTime;
		this.flag = flag;
		eachPlan = new ArrayList<EachPlan>();
	}

	@Override
	public List<EachPlan> calculator() {
		/*
		 * 如果是发标或复审或投标或网贷计算器则total = account; 否则total = BigDecimalUtil.mul(account, periods);
		 */
		double total;
		if (flag) {
			total = account;
		} else {
			total = BigDecimalUtil.mul(account, periods);
		}
		double needRepay = total;
		double eInterest = 0;
		double netInterest = 0;
		double eCapital = 0;
		double eTotal = 0;
		double sum = 0;
		Date eInterestTime = null;
		Date eRepayTime = null;
		EachPlan e = null;
		for (int i = 0; i < periods; i++) {
			e = new EachPlan();
			// 计算每月需要支付的利息
			eInterest = BigDecimalUtil.div(BigDecimalUtil.mul(needRepay, yearApr), 12);
			// 计算本金
			eCapital = 0;
			// 每期还款即是本期的还款总额
			if (i == periods - 1) { // 判断是否是最后一期
				eCapital = account;
				// 最后一期利息计算(总利息-最后一期以为的利息之和)
				eInterest = BigDecimalUtil.sub(BigDecimalUtil.div(BigDecimalUtil.mul(needRepay, yearApr, periods), 12), sum);
			}
			netInterest = BigDecimalUtil.round(BigDecimalUtil.mul(eInterest, BigDecimalUtil.sub(1, manageFee)));
			eInterest = BigDecimalUtil.round(eInterest);
			eTotal = BigDecimalUtil.add(eCapital, eInterest);
			// 本期开始计息日
			eInterestTime = DateUtil.rollDay(interestTime, 1);
			// 本期还款日
			eRepayTime = this.calculatorRepaytime(eInterestTime);
			e.setCapital(eCapital);
			e.setInterest(eInterest);
			e.setNetInterest(netInterest);
			e.setTotal(eTotal);
			double netTotal = BigDecimalUtil.add(eCapital, netInterest);
			e.setNetTotal(netTotal);
			e.setInterestTime(eInterestTime);
			e.setRepayTime(eRepayTime);
			repayTime = eRepayTime;
			eachPlan.add(e);
			sum = BigDecimalUtil.add(eInterest, sum);
		}
		this.repayAccount = BigDecimalUtil.add(account, sum);
		e = eachPlan.get(periods - 1);
		e.setNeedRepay(0);
		e.setCapital(account);
		return eachPlan;
	}

	@Override
	public List<EachPlan> calculator(int days) {
		throw new BorrowException("每月还息到期还本不支持按天还款!", 1);
	}

	@Override
	public List<EachPlan> getEachPlan() {
		return eachPlan;
	}

	@Override
	public double repayTotal() {
		return this.repayAccount;
	}

	@Override
	public Date repayTime() {
		return repayTime;
	}

	@Override
	public int repayPeriods() {
		return periods;
	}

	@Override
	public Date calculatorRepaytime(Date date) {
		return DateUtil.rollMon(date, 1);
	}

}
