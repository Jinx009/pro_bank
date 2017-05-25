package com.rongdu.p2psys.borrow.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.user.domain.User;

/**
 * 加息劵实体类
 * @author sj
 * @since 2014年12月17日
 *
 */
@Entity
@Table(name = "rd_borrow_interest_rate")
public class BorrowInterestRate implements Serializable {

	private static final long serialVersionUID = 1496578868157705739L;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	/**
	 * 投标
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tender_id")
	private BorrowTender tender;
	
	/**
	 * 状态 1正常 2 过期 3 已使用
	 */
	private int status;
	
	/**
	 * 加息劵值
	 */
	private double value;
	
	/**
	 * 投标金额
	 */
	private double money;
	
	/**
	 * 时间
	 */
	private Date addTime;
	
	/**
	 * ip
	 */
	private String addIp;

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

	public BorrowTender getTender() {
		return tender;
	}

	public void setTender(BorrowTender tender) {
		this.tender = tender;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	
}
