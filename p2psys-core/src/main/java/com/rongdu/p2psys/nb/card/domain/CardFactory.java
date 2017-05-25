package com.rongdu.p2psys.nb.card.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 卡券库
 * 
 * @author Jinx
 *
 */
@Entity
@Table(name = "nb_card_factory")
public class CardFactory implements Serializable
{
	private static final long serialVersionUID = 2432966222051758084L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Integer status;
	
	private String cardNo;
	
	private String cardPassword;
	
	private String type;
	
	private String cardDesc;

	
	
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getCardNo()
	{
		return cardNo;
	}

	public void setCardNo(String cardNo)
	{
		this.cardNo = cardNo;
	}

	public String getCardPassword()
	{
		return cardPassword;
	}

	public void setCardPassword(String cardPassword)
	{
		this.cardPassword = cardPassword;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getCardDesc()
	{
		return cardDesc;
	}

	public void setCardDesc(String cardDesc)
	{
		this.cardDesc = cardDesc;
	}
}
