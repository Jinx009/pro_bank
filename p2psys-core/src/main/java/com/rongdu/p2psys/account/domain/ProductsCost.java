package com.rongdu.p2psys.account.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.ppfund.domain.Ppfund;

/**
 * 理财产品费用记录
 * 
 * @author yl
 *
 */
@Entity
@Table(name = "rd_products_cost")
public class ProductsCost implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 产品名称
	 */
	private String name;
	
	/**
	 * 产品编码
	 */
	private String code;
	
	/**
	 * 现金管理产品
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ppfund_id")
	private Ppfund ppfund;
	
	/**
	 * 借款标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "borrow_id")
	private Borrow borrow;
	
	/**
	 * 管理费
	 */
	private double manageFee;
	
	/**
	 * 风险备用金
	 */
	private double riskReserveFee;
	
	/**
	 * 添加时间
	 */
	private Date addTime;

	public ProductsCost() {
		super();
	}

	public ProductsCost(String name, String code, Ppfund ppfund,
			double manageFee, double riskReserveFee) {
		super();
		this.name = name;
		this.code = code;
		this.ppfund = ppfund;
		this.manageFee = manageFee;
		this.riskReserveFee = riskReserveFee;
		this.addTime = new Date();
	}

	
	public ProductsCost(String name, String code, Borrow borrow,
			double manageFee, double riskReserveFee) {
		super();
		this.name = name;
		this.code = code;
		this.borrow = borrow;
		this.manageFee = manageFee;
		this.riskReserveFee = riskReserveFee;
		this.addTime = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Ppfund getPpfund() {
		return ppfund;
	}

	public void setPpfund(Ppfund ppfund) {
		this.ppfund = ppfund;
	}

	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}

	public double getManageFee() {
		return manageFee;
	}

	public void setManageFee(double manageFee) {
		this.manageFee = manageFee;
	}

	public double getRiskReserveFee() {
		return riskReserveFee;
	}

	public void setRiskReserveFee(double riskReserveFee) {
		this.riskReserveFee = riskReserveFee;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}
