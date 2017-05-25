package com.rongdu.p2psys.nb.recommend.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.user.domain.User;

/**
 * 推荐纪录
 * @author Chris
 *
 */
@Entity
@Table(name = ("nb_recommend_profit_record"))
public class RecommendProfitRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	/**
	 * 添加时间
	 */
	private Date addTime;
	
	/**
	 * 项目Id
	 */
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="borrow_id")
	private Borrow borrow;
	
	/**
	 * 投资用户（被推荐人）
	 */
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	/**
	 * 推荐人
	 */
	@ManyToOne
	@JoinColumn(name = "invite_user")
	private User inviteUser;
	
	/**
	 * 投资纪录Id
	 */
	private Long tenderId;
	
	/**
	 * 推荐人获取的收益
	 */
	private Double  money;
	
	/**
	 * 投资人投的钱
	 */
	private Double account;
	
	/**
	 *  推荐受益Id
	 */
	private Long profit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow borrow) {
		this.borrow = borrow;
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

	public Long getTenderId() {
		return tenderId;
	}

	public void setTenderId(Long tenderId) {
		this.tenderId = tenderId;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getAccount() {
		return account;
	}

	public void setAccount(Double account) {
		this.account = account;
	}

	public Long getProfit() {
		return profit;
	}

	public void setProfit(Long profit) {
		this.profit = profit;
	}

	
}
