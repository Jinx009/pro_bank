package com.rongdu.p2psys.nb.payment.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.user.domain.User;

/**
 * 通道实名关联表
 * 
 * @author cgw
 * @version 1.0
 * @since 2015-06-23
 */
@Entity
@Table(name = ("nb_channel_bank"))
public class ChannelBank implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 用户ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 支付通道id
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cid")
	private Channel chanel;
	
	/**
	 * 实名银行卡id
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bid")
	private AccountBank ab;
	
	/**
	 * 0：关闭，1：启用
	 */
	private int status;

	public ChannelBank() {
		super();
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
	}

	public Channel getChanel() {
		return chanel;
	}

	public void setChanel(Channel chanel) {
		this.chanel = chanel;
	}

	public AccountBank getAb() {
		return ab;
	}

	public void setAb(AccountBank ab) {
		this.ab = ab;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
