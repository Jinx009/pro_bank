package com.rongdu.p2psys.ppfund.domain;

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

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund资金管理产品转入
 */
@Entity
@Table(name = Global.DB_PREFIX + "ppfund_in")
public class PpfundIn implements Serializable {

	private static final long serialVersionUID = -7828377427058833869L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 购买人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 产品
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ppfund_id")
	private Ppfund ppfund;

	/**
	 * 是否转出 0：否，1：是
	 */
	private Integer isOut;

	/**
	 * 购买金额
	 */
	private Double money;

	/**
	 * 购买有效金额
	 */
	private Double account;

	/**
	 * 计息金额
	 */
	private Double interestAmount;
	
	/*
	 * 原投标金额
	 * */
	private Double originalAccount;
	
	/*
	 * 原投标利息
	 * */
	private Double originalInterest;
	
	

	/**
	 * 收益金额
	 */
	private Double interest;

	/**
	 * 转出时间
	 */
	private Date outTime;

	/**
	 * 购买时间
	 */
	private Date addTime;

	/**
	 * 购买IP
	 */
	private String addIp;

	/**
	 * 标类型
	 *
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type")
	private ProductType productType;

	public PpfundIn() {
		super();
	}

	public PpfundIn(User user, Ppfund ppfund, Integer isOut, Double money,
			Double account, Double interestAmount, Double interest) {
		super();
		this.user = user;
		this.ppfund = ppfund;
		this.isOut = isOut;
		this.money = money;
		this.account = account;
		this.interestAmount = interestAmount;
		this.interest = interest;
		this.addTime = new Date();
		this.addIp = Global.getIP();
	}

	public PpfundIn(User user, Ppfund ppfund, Integer isOut, Double money,
			Double account, Double interestAmount, Double interest,
			Date outTime, ProductType productType) {
		super();
		this.user = user;
		this.ppfund = ppfund;
		this.isOut = isOut;
		this.money = money;
		this.account = account;
		this.interestAmount = interestAmount;
		this.interest = interest;
		this.outTime = outTime;
		this.addTime = new Date();
		this.addIp = Global.getIP();
		this.productType = productType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Ppfund getPpfund() {
		return ppfund;
	}

	public void setPpfund(Ppfund ppfund) {
		this.ppfund = ppfund;
	}

	public Integer getIsOut() {
		return isOut;
	}

	public void setIsOut(Integer isOut) {
		this.isOut = isOut;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getAccount() {
		return account;
	}

	public void setAccount(Double account) {
		this.account = account;
	}

	public Double getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(Double interestAmount) {
		this.interestAmount = interestAmount;
	}
	
	public Double getOriginalInterest() {
		return originalInterest;
	}

	public void setOriginalInterest(Double originalInterest) {
		this.originalInterest = originalInterest;
	}
	
	public Double getOriginalAccount() {
		return originalAccount;
	}

	public void setOriginalAccount(Double originalAccount) {
		this.originalAccount = originalAccount;
	}
	
	public Double getInterest() {
		return interest;
	}

	public void setInterest(Double interest) {
		this.interest = interest;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

}
