package com.rongdu.p2psys.borrow.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 标种配置
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_borrow_config")
public class BorrowConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	private long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 标识
	 */
	private String cname;
	/**
	 * 最大投标额
	 */
	private double mostAccount;
	/**
	 * 最小投标额
	 */
	private double lowestAccount;
	/**
	 * 最大年利率
	 */
	private double mostApr;
	/**
	 * 最小年利率
	 */
	private double lowestApr;
	/**
	 * 最大奖励比率
	 */
	private double mostAwardApr;
	/**
	 * 最小奖励比率
	 */
	private double lowestAwardApr;
	/**
	 * 
	 */
	private double mostAwardFunds;
	/**
	 * 
	 */
	private double lowestAwardFunds;
	/**
	 * 是否跳过初审
	 */
	private boolean isTrail;
	/**
	 * 是否跳过复审
	 */
	private boolean isReview;
	/**
	 * 认证条件
	 */
	private String identify;
	/**
	 * 借款管理费
	 */
	private double manageFee;
	/**
	 * 日借款管理费
	 */
	private double dayManageFee;
	/**
	 * 性质
	 */
	private String nature;
	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 是否启用 0：不启用，1启用
	 */
	private boolean enable;

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

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public double getMostAccount() {
		return mostAccount;
	}

	public void setMostAccount(double mostAccount) {
		this.mostAccount = mostAccount;
	}

	public double getLowestAccount() {
		return lowestAccount;
	}

	public void setLowestAccount(double lowestAccount) {
		this.lowestAccount = lowestAccount;
	}

	public double getMostApr() {
		return mostApr;
	}

	public void setMostApr(double mostApr) {
		this.mostApr = mostApr;
	}

	public double getLowestApr() {
		return lowestApr;
	}

	public void setLowestApr(double lowestApr) {
		this.lowestApr = lowestApr;
	}

	public double getMostAwardApr() {
		return mostAwardApr;
	}

	public void setMostAwardApr(double mostAwardApr) {
		this.mostAwardApr = mostAwardApr;
	}

	public double getLowestAwardApr() {
		return lowestAwardApr;
	}

	public void setLowestAwardApr(double lowestAwardApr) {
		this.lowestAwardApr = lowestAwardApr;
	}

	public double getMostAwardFunds() {
		return mostAwardFunds;
	}

	public void setMostAwardFunds(double mostAwardFunds) {
		this.mostAwardFunds = mostAwardFunds;
	}

	public double getLowestAwardFunds() {
		return lowestAwardFunds;
	}

	public void setLowestAwardFunds(double lowestAwardFunds) {
		this.lowestAwardFunds = lowestAwardFunds;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

	public double getManageFee() {
		return manageFee;
	}

	public void setManageFee(double manageFee) {
		this.manageFee = manageFee;
	}

	public double getDayManageFee() {
		return dayManageFee;
	}

	public void setDayManageFee(double dayManageFee) {
		this.dayManageFee = dayManageFee;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isTrail() {
		return isTrail;
	}

	public void setTrail(boolean isTrail) {
		this.isTrail = isTrail;
	}

	public boolean isReview() {
		return isReview;
	}

	public void setReview(boolean isReview) {
		this.isReview = isReview;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
