package com.rongdu.p2psys.account.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 线下支付（收账）银行账户
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_pay_offlinebank")
public class PayOfflinebank implements Serializable {
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
	 * 账户所有人
	 */
	private String owner;
	/**
	 * 状态 1禁用 2不禁用
	 */
	private int status;

	/**
	 * 账号
	 */
	private String bankNo;
	/**
	 * 银行
	 */
	private String bank;
	/**
	 * 支行
	 */
	private String branch;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String area;

	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;

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
	 * 获取账户所有人
	 * 
	 * @return 账户所有人
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * 设置账户所有人
	 * 
	 * @param owner 要设置的账户所有人
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取账号
	 * 
	 * @return 账号
	 */
	public String getBankNo() {
		return bankNo;
	}

	/**
	 * 设置账号
	 * 
	 * @param bankNo 要设置的账号
	 */
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
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
	 * 获取支行
	 * 
	 * @return 支行
	 */
	public String getBranch() {
		return branch;
	}

	/**
	 * 设置支行
	 * 
	 * @param branch 要设置的支行
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	/**
	 * 获取添加IP
	 * 
	 * @return 添加IP
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置添加IP
	 * 
	 * @param addIp 要设置的添加IP
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
}
