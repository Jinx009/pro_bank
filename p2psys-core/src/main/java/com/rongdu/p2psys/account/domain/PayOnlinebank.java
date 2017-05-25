package com.rongdu.p2psys.account.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 线上支付银行账户
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_pay_onlinebank")
public class PayOnlinebank implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	/**
	 * 银行编号
	 */
	private String bank;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 图标
	 */
	private String logo;
	/**
	 * 标识
	 */
	private String nid;
	/**
	 * 支付接口标识
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pay_id")
	private Pay pay;
	/**
	 * 是否开启
	 */
	private int enable;

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 获取银行编号
	 * 
	 * @return 银行编号
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * 设置银行编号
	 * 
	 * @param bank 要设置的银行编号
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name 要设置的名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取图标
	 * 
	 * @return 图标
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * 设置图标
	 * 
	 * @param logo 要设置的图标
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * 获取标识
	 * 
	 * @return 标识
	 */
	public String getNid() {
		return nid;
	}

	/**
	 * 设置标识
	 * 
	 * @param nid 要设置的标识
	 */
	public void setNid(String nid) {
		this.nid = nid;
	}

	public Pay getPay() {
		return pay;
	}

	public void setPay(Pay pay) {
		this.pay = pay;
	}

	/**
	 * 获取是否开启
	 * 
	 * @return 是否开启
	 */
	public int getEnable() {
		return enable;
	}

	/**
	 * 设置是否开启
	 * 
	 * @param enable 要设置的是否开启
	 */
	public void setEnable(int enable) {
		this.enable = enable;
	}
}
