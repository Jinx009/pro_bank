package com.rongdu.p2psys.tpp.chinapnr.model;

import java.util.List;

/**
 * 汇付子账户信息
 * @author yinliang
 * @version 2.0
 * @Date   2015年2月3日
 */
public class ChinapnrAccts {
	/**
	 * 子账户类型
	 */
	private String acctType;
	
	/**
	 * 子账户账户号
	 */
	private String subAcctId;
	
	/**
	 * 子账户可用余额
	 */
	private double avlBal;
	
	/**
	 * 子账户账户余额
	 */
	private double acctBal;
	
	/**
	 * 子账户冻结金额
	 */
	private double frzBal;

	public ChinapnrAccts() {
		super();
	}

	public ChinapnrAccts(String acctType, String subAcctId, double avlBal,
			double acctBal, double frzBal) {
		super();
		this.acctType = acctType;
		this.subAcctId = subAcctId;
		this.avlBal = avlBal;
		this.acctBal = acctBal;
		this.frzBal = frzBal;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getSubAcctId() {
		return subAcctId;
	}

	public void setSubAcctId(String subAcctId) {
		this.subAcctId = subAcctId;
	}

	public double getAvlBal() {
		return avlBal;
	}

	public void setAvlBal(double avlBal) {
		this.avlBal = avlBal;
	}

	public double getAcctBal() {
		return acctBal;
	}

	public void setAcctBal(double acctBal) {
		this.acctBal = acctBal;
	}

	public double getFrzBal() {
		return frzBal;
	}

	public void setFrzBal(double frzBal) {
		this.frzBal = frzBal;
	}
	
}

/**
 * 对象数组
 * @author yinliang
 * @version 2.0
 * @Date   2015年2月3日
 */
class ChinapnrAcctList {
	List<ChinapnrAccts> chinapnrAcct;

	public List<ChinapnrAccts> getChinapnrAcct() {
		return chinapnrAcct;
	}

	public void setChinapnrAcct(List<ChinapnrAccts> chinapnrAcct) {
		this.chinapnrAcct = chinapnrAcct;
	}
}