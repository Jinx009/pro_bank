package com.rongdu.p2psys.user.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 加息券实体类
 */
@Entity
@Table(name = "nb_coupon")
public class Coupon implements Serializable {

	private static final long serialVersionUID = -3042716389904959571L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 加息券名称
	 */
	private String code;

	/**
	 * 加息券描述
	 */
	private String pwd;

	/**
	 * 加息券类别
	 */
	@ManyToOne
	@JoinColumn(name = "category_id")
	private CouponCategory category;

	/**
	 * 加息券状态
	 */
	private Integer status;

	/**
	 * 加息券发起人
	 */
	@ManyToOne
	@JoinColumn(name = "from_id")
	private User userFrom;

	/**
	 * 加息券发起人手机
	 */
	private String fromMobile;

	/**
	 * 加息券发起人加息比例
	 */
	private Double fromRate;

	/**
	 * 加息券发起人加息比例调整值
	 */
	private Double fromRateAdjust;

	/**
	 * 加息券拥有人
	 */
	@ManyToOne
	@JoinColumn(name = "to_id")
	private User userTo;

	/**
	 * 加息券拥有人手机
	 */
	private String toMobile;

	/**
	 * 加息券拥有人加息比例
	 */
	private Double toRate;

	/**
	 * 加息券拥有人加息比例调整值
	 */
	private Double toRateAdjust;

	/**
	 * 非现金投资记录ID
	 */
	private Long borrowTenderId;

	/**
	 * 现金投资记录ID
	 */
	private Long ppfundInId;

	/**
	 * 获赠时间
	 */
	private Date addTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public CouponCategory getCategory() {
		return category;
	}

	public void setCategory(CouponCategory category) {
		this.category = category;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public User getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(User userFrom) {
		this.userFrom = userFrom;
	}

	public String getFromMobile() {
		return fromMobile;
	}

	public void setFromMobile(String fromMobile) {
		this.fromMobile = fromMobile;
	}

	public Double getFromRate() {
		return fromRate;
	}

	public void setFromRate(Double fromRate) {
		this.fromRate = fromRate;
	}

	public Double getFromRateAdjust() {
		return fromRateAdjust;
	}

	public void setFromRateAdjust(Double fromRateAdjust) {
		this.fromRateAdjust = fromRateAdjust;
	}

	public User getUserTo() {
		return userTo;
	}

	public void setUserTo(User userTo) {
		this.userTo = userTo;
	}

	public String getToMobile() {
		return toMobile;
	}

	public void setToMobile(String toMobile) {
		this.toMobile = toMobile;
	}

	public Double getToRate() {
		return toRate;
	}

	public void setToRate(Double toRate) {
		this.toRate = toRate;
	}

	public Double getToRateAdjust() {
		return toRateAdjust;
	}

	public void setToRateAdjust(Double toRateAdjust) {
		this.toRateAdjust = toRateAdjust;
	}

	public Long getBorrowTenderId() {
		return borrowTenderId;
	}

	public void setBorrowTenderId(Long borrowTenderId) {
		this.borrowTenderId = borrowTenderId;
	}

	public Long getPpfundInId() {
		return ppfundInId;
	}

	public void setPpfundInId(Long ppfundInId) {
		this.ppfundInId = ppfundInId;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
