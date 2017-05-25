package com.rongdu.p2psys.borrow.model;

import java.util.Date;

public class BorrowProfitModel {
	
	private double interest;

	private Date repaymentTime;
	
	//(1:月标 0:天标)
	private int borrowTimeType;
	
	//复审时间
	private Date reviewTime;
	
	private int timeLimit;
	
	//中期天数
	private int middleDay;
	
	//加息后利息
	private double interestRate;

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public Date getRepaymentTime() {
		return repaymentTime;
	}

	public void setRepaymentTime(Date repaymentTime) {
		this.repaymentTime = repaymentTime;
	}

	public int getBorrowTimeType() {
		return borrowTimeType;
	}

	public void setBorrowTimeType(int borrowTimeType) {
		this.borrowTimeType = borrowTimeType;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public int getMiddleDay() {
		return middleDay;
	}

	public void setMiddleDay(int middleDay) {
		this.middleDay = middleDay;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	
}
