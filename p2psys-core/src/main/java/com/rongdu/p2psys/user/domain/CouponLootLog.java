package com.rongdu.p2psys.user.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 加息券抢注记录实体类
 */
@Entity
@Table(name = "nb_coupon_loot_log")
public class CouponLootLog implements Serializable {

	private static final long serialVersionUID = 1606106907942932975L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 抢注时间
	 */
	private Date lootTime;

	/**
	 * 抢注手机号码
	 */
	private String lootMobile;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLootTime() {
		return lootTime;
	}

	public void setLootTime(Date lootTime) {
		this.lootTime = lootTime;
	}

	public String getLootMobile() {
		return lootMobile;
	}

	public void setLootMobile(String lootMobile) {
		this.lootMobile = lootMobile;
	}

}
