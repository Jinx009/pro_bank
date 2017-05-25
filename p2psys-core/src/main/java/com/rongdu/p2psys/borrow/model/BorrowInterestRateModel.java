package com.rongdu.p2psys.borrow.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;

public class BorrowInterestRateModel extends BorrowInterestRate {

	private static final long serialVersionUID = 2066655839828499012L;

	private String userName;
	
	private String realName;
	
	public static BorrowInterestRateModel instance(BorrowInterestRate borrowInterestRate) {
		BorrowInterestRateModel borrowInterestRateModel = new BorrowInterestRateModel();
		BeanUtils.copyProperties(borrowInterestRate, borrowInterestRateModel);
		return borrowInterestRateModel;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
}
