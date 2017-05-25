package com.rongdu.p2psys.nb.vip.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "ehb_zc_wealth_manager_user")
public class WealthManagerUser implements Serializable
{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	/**
	 * wealthUser表的主键  
	 */
	private Integer wealthUserId;;
	/**
	 * 财富id
	 */
	@OneToOne
	@JoinColumn(name = "wealth_manager_id", insertable = true, unique = true)
	private WealthManager wealthManager;
	

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	

	public Integer getWealthUserId()
	{
		return wealthUserId;
	}

	public void setWealthUserId(Integer wealthUserId)
	{
		this.wealthUserId = wealthUserId;
	}

	public WealthManager getWealthManager()
	{
		return wealthManager;
	}

	public void setWealthManager(WealthManager wealthManager)
	{
		this.wealthManager = wealthManager;
	}



}
