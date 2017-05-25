package com.rongdu.p2psys.user.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 加息券分类实体类
 */
@Entity
@Table(name = "nb_coupon_category")
public class CouponCategory implements Serializable {

	private static final long serialVersionUID = -3925410831758488509L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 加息券名称
	 */
	private String name;

	/**
	 * 备注
	 */
	private String content;

	/**
	 * 基本加息比例
	 */
	private Double rate;

	/**
	 * 推荐人奖励加息比例
	 */
	private Double bonusRate;

	/**
	 * 有效期起始日
	 */
	private Date validFrom;

	/**
	 * 有效期截止日
	 */
	private Date validTo;

	/**
	 * 发行量
	 */
	private Long circulation;

	/**
	 * 加息期
	 */
	private Integer effectiveDays;

	/**
	 * 限制产品1
	 */
	private Long productId1;

	/**
	 * 限制产品2
	 */
	private Long productId2;

	/**
	 * 限制产品3
	 */
	private Long productId3;

	/**
	 * 投资界限1
	 */
	private Double investLimit1;

	/**
	 * 投资界限2
	 */
	private Double investLimit2;

	/**
	 * 投资界限3
	 */
	private Double investLimit3;

	/**
	 * 投资界限4
	 */
	private Double investLimit4;

	/**
	 * 投资界限5
	 */
	private Double investLimit5;

	/**
	 * 界限1加息比例
	 */
	private Double limitRate1;

	/**
	 * 界限2加息比例
	 */
	private Double limitRate2;

	/**
	 * 界限3加息比例
	 */
	private Double limitRate3;

	/**
	 * 界限4加息比例
	 */
	private Double limitRate4;

	/**
	 * 界限5加息比例
	 */
	private Double limitRate5;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getBonusRate() {
		return bonusRate;
	}

	public void setBonusRate(Double bonusRate) {
		this.bonusRate = bonusRate;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public Long getCirculation() {
		return circulation;
	}

	public void setCirculation(Long circulation) {
		this.circulation = circulation;
	}

	public Integer getEffectiveDays() {
		return effectiveDays;
	}

	public void setEffectiveDays(Integer effectiveDays) {
		this.effectiveDays = effectiveDays;
	}

	public Long getProductId1() {
		return productId1;
	}

	public void setProductId1(Long productId1) {
		this.productId1 = productId1;
	}

	public Long getProductId2() {
		return productId2;
	}

	public void setProductId2(Long productId2) {
		this.productId2 = productId2;
	}

	public Long getProductId3() {
		return productId3;
	}

	public void setProductId3(Long productId3) {
		this.productId3 = productId3;
	}

	public Double getInvestLimit1() {
		return investLimit1;
	}

	public void setInvestLimit1(Double investLimit1) {
		this.investLimit1 = investLimit1;
	}

	public Double getInvestLimit2() {
		return investLimit2;
	}

	public void setInvestLimit2(Double investLimit2) {
		this.investLimit2 = investLimit2;
	}

	public Double getInvestLimit3() {
		return investLimit3;
	}

	public void setInvestLimit3(Double investLimit3) {
		this.investLimit3 = investLimit3;
	}

	public Double getInvestLimit4() {
		return investLimit4;
	}

	public void setInvestLimit4(Double investLimit4) {
		this.investLimit4 = investLimit4;
	}

	public Double getInvestLimit5() {
		return investLimit5;
	}

	public void setInvestLimit5(Double investLimit5) {
		this.investLimit5 = investLimit5;
	}

	public Double getLimitRate1() {
		return limitRate1;
	}

	public void setLimitRate1(Double limitRate1) {
		this.limitRate1 = limitRate1;
	}

	public Double getLimitRate2() {
		return limitRate2;
	}

	public void setLimitRate2(Double limitRate2) {
		this.limitRate2 = limitRate2;
	}

	public Double getLimitRate3() {
		return limitRate3;
	}

	public void setLimitRate3(Double limitRate3) {
		this.limitRate3 = limitRate3;
	}

	public Double getLimitRate4() {
		return limitRate4;
	}

	public void setLimitRate4(Double limitRate4) {
		this.limitRate4 = limitRate4;
	}

	public Double getLimitRate5() {
		return limitRate5;
	}

	public void setLimitRate5(Double limitRate5) {
		this.limitRate5 = limitRate5;
	}

}
