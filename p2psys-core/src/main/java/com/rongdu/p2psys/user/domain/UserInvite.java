package com.rongdu.p2psys.user.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * 邀请好友实体类
 * 
 */
@Entity
@Table(name="rd_user_invite")
public class UserInvite {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	/**
	 * 被邀请人
	 */
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="user_id")
	private User user;
	/**
	 * 邀请人
	 */
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="invite_user")
	private User inviteUser;
	
	/**
	 * 是否已赠送
	 */
	private boolean isGift;

	/**
	 * 邀请时间
	 */
	private Date inviteTime;

	/**
	 * 赠送时间
	 */
	private Date giftTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getInviteUser() {
		return inviteUser;
	}

	public void setInviteUser(User inviteUser) {
		this.inviteUser = inviteUser;
	}

	public boolean isGift() {
		return isGift;
	}

	public void setGift(boolean isGift) {
		this.isGift = isGift;
	}

	public Date getInviteTime() {
		return inviteTime;
	}

	public void setInviteTime(Date inviteTime) {
		this.inviteTime = inviteTime;
	}

	public Date getGiftTime() {
		return giftTime;
	}

	public void setGiftTime(Date giftTime) {
		this.giftTime = giftTime;
	}
	
	
}
