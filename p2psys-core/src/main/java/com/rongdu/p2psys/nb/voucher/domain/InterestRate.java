package com.rongdu.p2psys.nb.voucher.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nb_interest_rate")
public class InterestRate implements Serializable {

	private static final long serialVersionUID = 7037471243695542921L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 状态
	 * 
	 * <p>
	 * 1:正常
	 * </p>
	 * <p>
	 * 2:过期
	 * </p>
	 * <p>
	 * 3:已使用
	 * </p>
	 */
	private Integer status;

	/**
	 * 加息百分比
	 */
	private Double rate;

	/**
	 * 时间
	 */
	private Date addTime;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

}
