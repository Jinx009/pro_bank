package com.rongdu.p2psys.borrow.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.borrow.domain.BorrowTender;

/**
 * 最新投标model
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2014年12月17日
 */
public class NewTenderModel {

	private static final long serialVersionUID = 1L;
	/** 投标人用户名 */
	private String userName;

	/** 投标时间 */
	private String tenderTime;

	/** 投标金额 */
	private double tenderMoney;

	/** 投标项目名称 */
	private String borrowName;
	
	/** 投标项目名称隐藏*/
	private String borrowNameHide;
	
	/** 投标项目ID */
	private long borrowId;
	
	/** 系统当前时间 */
	private String nowTime;
	
	public String getUserName() {
		return userName.charAt(0) + "***"
				+ userName.charAt(userName.length() - 1);
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTenderTime() {
		return tenderTime;
	}

	public void setTenderTime(String tenderTime) {
		this.tenderTime = tenderTime;
	}

	public double getTenderMoney() {
		return tenderMoney;
	}

	public void setTenderMoney(double tenderMoney) {
		this.tenderMoney = tenderMoney;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getBorrowNameHide() {
		if (StringUtil.isNotBlank(borrowName)&& borrowName.length()>7) {
			return borrowName.substring(0, 6) + "***";
		}
		return borrowName;
	}

	public void setBorrowNameHide(String borrowNameHide) {
		this.borrowNameHide = borrowNameHide;
	}

	public long getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(long borrowId) {
		this.borrowId = borrowId;
	}

	public String getNowTime() {
		return DateUtil.getNowTimeStr();
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
	
}
