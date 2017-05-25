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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.user.domain.User;

/**
 * 项目冻结金额
 * 
 * @author Jinx
 *
 */
@Entity
@Table(name = "nb_frozen_product")
public class FrozenProduct implements Serializable
{
	private static final long serialVersionUID = -347786243936960801L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	/**
	 * product_basic关联
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_basic_id")
	private ProductBasic productBasic;
	
	/**
	 * 真实项目id
	 */
	private Integer productId;
	
	/**
	 * 产品类别关联
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_type_id")
	private ProductType productType;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	/**
	 * 投资金额
	 */
	private Double money;
	
	/**
	 * 添加时间 
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date addTime;
	
	/**
	 * 记录状态 0代表锁定 1代表解锁
	 */
	private Integer status;
	
	
	
	
	
	
	

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public ProductBasic getProductBasic()
	{
		return productBasic;
	}

	public void setProductBasic(ProductBasic productBasic)
	{
		this.productBasic = productBasic;
	}

	public Integer getProductId()
	{
		return productId;
	}

	public void setProductId(Integer productId)
	{
		this.productId = productId;
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Double getMoney()
	{
		return money;
	}

	public void setMoney(Double money)
	{
		this.money = money;
	}

	public Date getAddTime()
	{
		return addTime;
	}

	public void setAddTime(Date addTime)
	{
		this.addTime = addTime;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	
	
	
	
	
	
}
