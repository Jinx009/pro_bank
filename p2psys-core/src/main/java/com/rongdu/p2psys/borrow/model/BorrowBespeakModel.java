package com.rongdu.p2psys.borrow.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.borrow.domain.BorrowBespeak;

public class BorrowBespeakModel extends BorrowBespeak {

	private static final long serialVersionUID = -5880881163621791114L;

	private String userName;
	
	private String realName;
	
	public static BorrowBespeakModel instance(BorrowBespeak borrowBespeak) {
		BorrowBespeakModel borrowBespeakModel = new BorrowBespeakModel();
		BeanUtils.copyProperties(borrowBespeak, borrowBespeakModel);
		return borrowBespeakModel;
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
