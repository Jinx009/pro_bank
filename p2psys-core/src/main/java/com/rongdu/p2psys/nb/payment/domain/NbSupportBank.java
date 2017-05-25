package com.rongdu.p2psys.nb.payment.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 支持的银行卡
 * @author cgw
 * @version 2.0
 * @date 2015年9月06日
 */
@Entity
@Table(name = "nb_support_bank")
public class NbSupportBank implements Serializable {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 银行卡名称
	 */
	private String bankName;
	
	/**
	 * 银行卡图标
	 */
	private String bankLogo;

	/**
	 * 银行卡标识
	 */
	private String bankCode;
	
	/**
	 * 单笔限额
	 */
	private double singleAmt;
	
	/**
	 * 单日限额
	 */
	private double dayAmt;
	
	/**
	 * 单月限额
	 */
	private double monthAmt;
	
	/**
	 * 是否启用
	 */
	private byte enable;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankLogo() {
		return bankLogo;
	}

	public void setBankLogo(String bankLogo) {
		this.bankLogo = bankLogo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public double getSingleAmt() {
		return singleAmt;
	}

	public void setSingleAmt(double singleAmt) {
		this.singleAmt = singleAmt;
	}

	public double getDayAmt() {
		return dayAmt;
	}

	public void setDayAmt(double dayAmt) {
		this.dayAmt = dayAmt;
	}

	public double getMonthAmt() {
		return monthAmt;
	}

	public void setMonthAmt(double monthAmt) {
		this.monthAmt = monthAmt;
	}

	public byte getEnable() {
		return enable;
	}

	public void setEnable(byte enable) {
		this.enable = enable;
	}
	
}
