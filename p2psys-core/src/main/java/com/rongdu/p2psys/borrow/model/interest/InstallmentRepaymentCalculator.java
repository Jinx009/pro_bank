package com.rongdu.p2psys.borrow.model.interest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.borrow.exception.BorrowException;

/**
 * 等额本息利息计算函数
 * 
 * @author：xx
 * @version 1.0
 * @since 2014年7月15日
 */
public class InstallmentRepaymentCalculator implements InterestCalculator {

	/** 借款总额 */
	private double account;
	/** 年利率 */
	private double yearApr;
	/** 期数 */
	private int periods;
	/** 还款金额 */
	private double repayAccount;
	/** 利息管理费 */
	private double manageFee;
	/** 开始计息日 */
	private Date interestTime;
	/** 还款日 */
	private Date repayTime;
	/** 还款计划 */
	private List<EachPlan> eachPlan;

	/**
	 * 初始化等额本息构造函数
	 * 
	 * @param account 借款总额
	 * @param yearApr 年利率
	 * @param interestTime 开始计息日
	 * @param periods 期数
	 * @param manageFee 利息管理费
	 */
	public InstallmentRepaymentCalculator(double account, double yearApr, Date interestTime, int periods,
			double manageFee) {
		this.account = account;
		this.yearApr = yearApr;
		this.periods = periods;
		this.interestTime = interestTime;
		this.manageFee = manageFee;
		eachPlan = new ArrayList<EachPlan>();
	}

	@Override
	public List<EachPlan> calculator() {
		double eachRepay = BigDecimalUtil.round(mrpi(account, yearApr, periods), 2);
		double total = BigDecimalUtil.mul(eachRepay, periods);
		double remain = account;
		double needRepay = total;
		double eInterest = 0;
		double netInterest = 0;
		double eCapital = 0;
		double eTotal = 0;
		Date eInterestTime = null;
		Date eRepayTime = null;
		for (int i = 0; i < periods; i++) {
			EachPlan e = new EachPlan();
			// 判断是否最后一期，最后一期的话，本金做减法
			if (i == periods - 1) {
				// 本金
				eCapital = BigDecimalUtil.round(remain);
				// 利息 = 每月还款金额 - 本金
				eInterest = BigDecimalUtil.round(BigDecimalUtil.sub(eachRepay, eCapital));
			} else {
				// 计算每月需要支付的利息
				eInterest = BigDecimalUtil.round(remain * yearApr / 12);
				// 计算本金
				eCapital = BigDecimalUtil.round(BigDecimalUtil.sub(eachRepay, eInterest));
			}
			netInterest = BigDecimalUtil.round(BigDecimalUtil.mul(eInterest, BigDecimalUtil.sub(1, manageFee)));
			// 每期还款即是本期的还款总额
			eTotal = eachRepay;
			needRepay -= eachRepay;
			// 用于计算利息的剩余金额
			remain -= eCapital;
			// 本期开始计息日
			eInterestTime = DateUtil.rollDay(interestTime, 1);
			// 本期还款日
			eRepayTime = this.calculatorRepaytime(eInterestTime);
			e.setCapital(eCapital);
			e.setInterest(eInterest);
			e.setNetInterest(netInterest);
			e.setTotal(eTotal);
			double netTotal = BigDecimalUtil.round(BigDecimalUtil.add(eCapital, netInterest));
			e.setNetTotal(netTotal);
			e.setNeedRepay(needRepay);
			e.setInterestTime(eInterestTime);
			e.setRepayTime(eRepayTime);
			repayTime = eRepayTime;
			eachPlan.add(e);
		}
		// 汇总信息
		this.repayAccount = total;
		return eachPlan;
	}

	@Override
	public List<EachPlan> calculator(int days) {
		throw new BorrowException("等额本息不支持按天还款!", 1);
	}

	@Override
	public List<EachPlan> getEachPlan() {
		return this.eachPlan;
	}

	@Override
	public double repayTotal() {
		return this.repayAccount;
	}

	@Override
	public Date repayTime() {
		return this.repayTime;
	}

	@Override
	public int repayPeriods() {
		return this.periods;
	}

	@Override
	public Date calculatorRepaytime(Date date) {
		return DateUtil.rollMon(date, 1);
	}

	/**
	 * 计算每一期等额的还款金额
	 * 
	 * @param p 借款总额
	 * @param r 年利率
	 * @param mn 期数
	 * @return 每期还款金额
	 */
	public double mrpi(double p, double r, int mn) {
		double mr = r / 12;
		double aprPow = Math.pow(1 + mr, mn);
		double period = 1;
		if (aprPow > 1) {
			period = aprPow - 1;
		}
		double monPay = p * mr * aprPow / period;
		return monPay;
	}

}
