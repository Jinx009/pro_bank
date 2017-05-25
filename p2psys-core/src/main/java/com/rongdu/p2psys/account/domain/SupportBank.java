package com.rongdu.p2psys.account.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 支持的银行卡
 * @author yl
 * @version 2.0
 * @date 2015年4月28日
 */
@Entity
@Table(name = "rd_support_bank")
public class SupportBank implements Serializable {

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
	private String name;
	
	/**
	 * 银行卡图标
	 */
	private String logo;
	
	/**
	 * 银行卡标识
	 */
	private String nid;
	
	/**
	 * 是否支持借记卡（储蓄卡）
	 */
	private byte debitStatus;
	
	/**
	 * 是否支持贷记卡（信用卡）
	 */
	private byte creditStatus;
	
	/**
	 * 单笔限额
	 */
	private double singleLimit;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	public byte getDebitStatus() {
		return debitStatus;
	}

	public void setDebitStatus(byte debitStatus) {
		this.debitStatus = debitStatus;
	}

	public byte getCreditStatus() {
		return creditStatus;
	}

	public void setCreditStatus(byte creditStatus) {
		this.creditStatus = creditStatus;
	}

	public double getSingleLimit() {
		return singleLimit;
	}

	public void setSingleLimit(double singleLimit) {
		this.singleLimit = singleLimit;
	}

	public byte getEnable() {
		return enable;
	}

	public void setEnable(byte enable) {
		this.enable = enable;
	}
	
}
