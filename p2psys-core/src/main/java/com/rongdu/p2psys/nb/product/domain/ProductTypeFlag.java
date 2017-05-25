package com.rongdu.p2psys.nb.product.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nb_product_type_flag")
public class ProductTypeFlag implements Serializable {

	private static final long serialVersionUID = 408944651128570065L;

	public ProductTypeFlag() {
		super();
	}

	public ProductTypeFlag(Long id) {
		super();
		this.id = id;
	}

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 产品标志名称
	 */
	private String flagName;

	/**
	 * 产品标志描述
	 */
	private String flagDescription;

	/**
	 * 推荐时间
	 */
	private Date recommendTime;

	/**
	 * 是否启用
	 * 
	 * <p>
	 * 0：未启用
	 * </p>
	 * <p>
	 * 1：启用
	 * </p>
	 */
	private Integer isEnable;

	/**
	 * 产品标志背景图URL
	 */
	private String picUrl;

	/**
	 * 产品标志PC图标URL
	 */
	private String iconUrl;

	/**
	 * 产品列表图标URL
	 */
	private String listUrl;

	/**
	 * 备注
	 */
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlagName() {
		return flagName;
	}

	public void setFlagName(String flagName) {
		this.flagName = flagName;
	}

	public String getFlagDescription() {
		return flagDescription;
	}

	public void setFlagDescription(String flagDescription) {
		this.flagDescription = flagDescription;
	}

	public Date getRecommendTime() {
		return recommendTime;
	}

	public void setRecommendTime(Date recommendTime) {
		this.recommendTime = recommendTime;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getListUrl() {
		return listUrl;
	}

	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}

}
