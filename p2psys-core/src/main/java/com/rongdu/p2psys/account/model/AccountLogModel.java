package com.rongdu.p2psys.account.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.AccountLog;

/**
 * 资金记录Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月27日
 */
public class AccountLogModel extends AccountLog {

	private static final long serialVersionUID = 1L;

	/** 当前页面 */
	private int page;
	/** 类型名称 */
	private String typeName;
	/** 交易方 */
	private String toUserName;
	/** 开始日期 **/
	private String startTime;
	/** 结束日期 **/
	private String endTime;
	/**日期范围：0：全部，1：最近七天 2：最近一个月  3：最近两个月，4 最近三个月**/
	private int time;
	/** 用户名 */
	private String userName;
	/** 真实姓名 */
	private String realName;
	/** 标名称 */
	private String borrowName;
	/**
	 * 资金类型--取该名字为了前端统一
	 */
	private String status;
	
	/** 条件查询 */
	private String searchName;
	
	/** 资金类型 */
	private String accountType;

	public static AccountLogModel instance(AccountLog accountLog) {
		AccountLogModel accountLogModel = new AccountLogModel();
		BeanUtils.copyProperties(accountLog, accountLogModel);
		return accountLogModel;
	}
	
	public static AccountLog instance(AccountLogModel model) {
		AccountLog accountLog = new AccountLog();
		BeanUtils.copyProperties(model, accountLog);
		return accountLog;
	}

	public AccountLog prototype() {
		AccountLog accountLog = new AccountLog();
		BeanUtils.copyProperties(this, accountLog);
		return accountLog;
	}

	/**
	 * 获取（隐藏一定位数的）交易对方
	 * 
	 * @return
	 */
	public String getHideToUserName() {
	    
		return StringUtil.hideStr(toUserName, 1, toUserName.length()-1);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
}
