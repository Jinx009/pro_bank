package com.rongdu.p2psys.nb.invest.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.user.domain.User;


@Entity
@Table(name = "nb_frozen_user")
public class FrozenUser implements Serializable
{
	private static final long serialVersionUID = 2532644168034872971L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	
	private Integer productId;
	/**
	 * 关联用户
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	/**
	 * 组合标
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_basic_id")
	private ProductBasic productBasic;
	
	/**
	 * 标类型
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_type_id")
	private ProductType productType;
	
	/**
	 * 投资金额
	 */
	private Double money;
	
	/**
	 * 锁定状态
	 */
	private Integer status;
	
	/**
	 * 添加时间
	 */
	private Date addTime;

	
	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public ProductBasic getProductBasic()
	{
		return productBasic;
	}

	public void setProductBasic(ProductBasic productBasic)
	{
		this.productBasic = productBasic;
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}

	public Double getMoney()
	{
		return money;
	}

	public void setMoney(Double money)
	{
		this.money = money;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Date getAddTime()
	{
		return addTime;
	}

	public void setAddTime(Date addTime)
	{
		this.addTime = addTime;
	}

	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(Integer productId)
	{
		this.productId = productId;
	}
	
	
	
	
}
