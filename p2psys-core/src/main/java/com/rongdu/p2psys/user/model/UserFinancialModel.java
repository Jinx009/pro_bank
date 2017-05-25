package com.rongdu.p2psys.user.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;

/**
 * 业务员-用户资金相关
 * @author yinliang
 * @version 2.0
 * @Date   2014年12月29日
 */
public class UserFinancialModel extends User{
	/** 用户投标总额 **/
	private double tenderTotal;
	
	/** 用户充值总额  **/
	private double rechargeTotal;
	
	/** 用户提现总额  **/
	private double cashTotal;
	
	private UserCache userCache;

	/** 理财顾问用户名 **/
	private String opName;
	
	private int status;
	
	/** 条件查询 */
	private String searchName;
	
	private UserIdentify userIdentify;
	
	/** 是否分配理财顾问 */
	private int fStatus;
	
	/** 当前登录管理员 **/
	private Operator operator;
	
	/**
	 * 充值开始时间
	 */
	private String rStartTime;
	
	/**
	 * 充值结束时间
	 */
	private String rEndTime;
	
	public static UserFinancialModel instance(User user) {
		UserFinancialModel userFinancialModel = new UserFinancialModel();
		BeanUtils.copyProperties(user, userFinancialModel);
		return userFinancialModel;
	}

	public double getTenderTotal() {
		return tenderTotal;
	}

	public void setTenderTotal(double tenderTotal) {
		this.tenderTotal = tenderTotal;
	}

	public double getRechargeTotal() {
		return rechargeTotal;
	}

	public void setRechargeTotal(double rechargeTotal) {
		this.rechargeTotal = rechargeTotal;
	}

	public double getCashTotal() {
		return cashTotal;
	}

	public void setCashTotal(double cashTotal) {
		this.cashTotal = cashTotal;
	}
	
	public UserCache getUserCache() {
		return userCache;
	}

	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public UserIdentify getUserIdentify() {
		return userIdentify;
	}

	public void setUserIdentify(UserIdentify userIdentify) {
		this.userIdentify = userIdentify;
	}

	public int getfStatus() {
		return fStatus;
	}

	public void setfStatus(int fStatus) {
		this.fStatus = fStatus;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getrStartTime() {
		return rStartTime;
	}

	public void setrStartTime(String rStartTime) {
		this.rStartTime = rStartTime;
	}

	public String getrEndTime() {
		return rEndTime;
	}

	public void setrEndTime(String rEndTime) {
		this.rEndTime = rEndTime;
	}
}
