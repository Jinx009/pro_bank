package com.rongdu.p2psys.nb.recommend.domain;

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

import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;

/**
 * 推荐纪录
 * 
 * @author Chris
 *
 */
@Entity
@Table(name = "nb_recommend_record")
public class RecommendRecord implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 添加时间
	 */
	private Date addTime;

	/**
	 * 金额（默认20）
	 */
	private Double account;

	/**
	 * 推荐红包
	 */
	private String name;

	/**
	 * 推荐
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invite_user")
	private User inviteUser;

	/**
	 * 被推荐人
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "red_packet")
	private UserRedPacket userRedPacket;
	
	
	
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public Date getAddTime()
	{
		return addTime;
	}

	public void setAddTime(Date addTime)
	{
		this.addTime = addTime;
	}

	public Double getAccount()
	{
		return account;
	}

	public void setAccount(Double account)
	{
		this.account = account;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public User getInviteUser()
	{
		return inviteUser;
	}

	public void setInviteUser(User inviteUser)
	{
		this.inviteUser = inviteUser;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public UserRedPacket getUserRedPacket()
	{
		return userRedPacket;
	}

	public void setUserRedPacket(UserRedPacket userRedPacket)
	{
		this.userRedPacket = userRedPacket;
	}

}
