package com.rongdu.p2psys.core.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.interest.EachPlan;
import com.rongdu.p2psys.borrow.model.interest.InstallmentRepaymentCalculator;
import com.rongdu.p2psys.borrow.model.interest.InterestCalculator;
import com.rongdu.p2psys.borrow.model.interest.MonthlyInterestCalculator;
import com.rongdu.p2psys.borrow.model.interest.OnetimeRepaymentCalculator;

/**
 * 计算器
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-22
 */
public class CalculatorAction extends BaseAction {

	/** 还款方式 */
	private int style;
	/** 是否月标 */
	private int is_month;
	/** 借款金额 */
	private double account;
	/** 借款金额 */
	private double lilv;
	private double manage_fee_percentage;
	private double tender_award_percentage;
	private int isAPR;
	private int times;

	@Action("/calculator/detail")
	public String detail() throws Exception {
		return "detail";
	}

	/**
	 * 计算利息
	 * 
	 * @throws Exception if has error
	 */
	@Action("/calculator/interest")
	public void interest() throws Exception {
		if (is_month == 1 && times > 12) {
			throw new BorrowException("借款期限不能超过12个月", 1);
		} else if (is_month == 0 && times > 30) {
			throw new BorrowException("借款期限不能超过30天", 1);
		}
		InterestCalculator ic = null;
		switch (style) {
			// 等额本息
			case Borrow.STYLE_INSTALLMENT_REPAYMENT:
				ic = new InstallmentRepaymentCalculator(account, lilv / 100, new Date(), times,
						manage_fee_percentage / 100);
				break;
			// 一次性还款
			case Borrow.STYLE_ONETIME_REPAYMENT:
				ic = new OnetimeRepaymentCalculator(account, lilv / 100, new Date(), times,
						manage_fee_percentage / 100);
				break;
			// 每月还息到期还本
			case Borrow.STYLE_MONTHLY_INTEREST:
				ic = new MonthlyInterestCalculator(account, lilv / 100, new Date(), times, true,
						manage_fee_percentage / 100);
				break;
			default:
				break;
		}
		// 是否月标
		if (is_month == 1) {
			ic.calculator();
		} else {
			ic.calculator(times);
		}
		request.setAttribute("ic", ic);
		// 投标奖励
		double tenderAward = BigDecimalUtil.round(BigDecimalUtil.mul(tender_award_percentage / 100, account));

		double sum = 0; // 待收总利息
		for (EachPlan plan : ic.getEachPlan()) {
			sum = BigDecimalUtil.add(sum, plan.getNetInterest());
		}
		// 实际总收益
		double totalAward = BigDecimalUtil.round(BigDecimalUtil.add(tenderAward, sum));
		request.setAttribute("tender_award", tenderAward);
		request.setAttribute("type", style);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", ic.getEachPlan());
		data.put("totalAward", totalAward);
		printJson(getStringOfJpaObj(data));
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getIs_month() {
		return is_month;
	}

	public void setIs_month(int is_month) {
		this.is_month = is_month;
	}

	public double getLilv() {
		return lilv;
	}

	public void setLilv(double lilv) {
		this.lilv = lilv;
	}

	public int getIs_APR() {
		return isAPR;
	}

	public void setIs_APR(int is_APR) {
		this.isAPR = is_APR;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public double getAccount() {
		return account;
	}

	public void setAccount(double account) {
		this.account = account;
	}

	public double getManage_fee_percentage() {
		return manage_fee_percentage;
	}

	public void setManage_fee_percentage(double manage_fee_percentage) {
		this.manage_fee_percentage = manage_fee_percentage;
	}

	public double getTender_award_percentage() {
		return tender_award_percentage;
	}

	public void setTender_award_percentage(double tender_award_percentage) {
		this.tender_award_percentage = tender_award_percentage;
	}
}
