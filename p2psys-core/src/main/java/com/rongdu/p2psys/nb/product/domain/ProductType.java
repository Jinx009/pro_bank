package com.rongdu.p2psys.nb.product.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nb_product_type")
public class ProductType implements Serializable {

	private static final long serialVersionUID = 4991490848063189639L;

	public ProductType() {
		super();
	}

	public ProductType(Long id) {
		super();
		this.id = id;
	}

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 产品编码
	 */
	private Long typeCode;

	/**
	 * 产品类型名称
	 */
	private String typeName;

	/**
	 * 产品类型描述
	 */
	private String typeDescription;

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
	 * 产品特征
	 * 
	 * <p>
	 * ppfund
	 * </p>
	 * <p>
	 * borrow
	 * </p>
	 * <p>
	 * productset
	 * </p>
	 * <p>
	 * crowdfunding
	 * </p>
	 */
	private String typeCategory;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 推荐时间
	 */
	private Date recommendTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Long typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeDescription() {
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getTypeCategory() {
		return typeCategory;
	}

	public void setTypeCategory(String typeCategory) {
		this.typeCategory = typeCategory;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getRecommendTime() {
		return recommendTime;
	}

	public void setRecommendTime(Date recommendTime) {
		this.recommendTime = recommendTime;
	}

}
