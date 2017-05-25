package com.rongdu.p2psys.nb.product.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nb_product_set")
public class ProductSet implements Serializable
{
	private static final long serialVersionUID = -1625116884541755958L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * 组合ID
	 */
	private Long productId;

	/**
	 * 组合所含产品ID
	 */
	private Long subProductId;

	/**
	 * 分配比例
	 */
	private Double rate;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public Long getSubProductId()
	{
		return subProductId;
	}

	public void setSubProductId(Long subProductId)
	{
		this.subProductId = subProductId;
	}

	public Double getRate()
	{
		return rate;
	}

	public void setRate(Double rate)
	{
		this.rate = rate;
	}

}
