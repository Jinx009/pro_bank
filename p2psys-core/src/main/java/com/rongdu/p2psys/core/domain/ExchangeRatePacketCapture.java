package com.rongdu.p2psys.core.domain;

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

@Entity
@Table(name = "rd_exchange_rate_packet_capture")
public class ExchangeRatePacketCapture implements Serializable {

	private static final long serialVersionUID = -6087096515404779253L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 借款标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "borrow_id")
	private Borrow borrow;
	
	/**
	 * 现汇买入价
	 */
	private double cashPurchasePrice;
	
	private Date addTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
	}

	public double getCashPurchasePrice() {
		return cashPurchasePrice;
	}

	public void setCashPurchasePrice(double cashPurchasePrice) {
		this.cashPurchasePrice = cashPurchasePrice;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
}
