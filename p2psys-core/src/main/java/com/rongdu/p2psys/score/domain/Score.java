package com.rongdu.p2psys.score.domain;

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

@Entity
@Table(name = "rd_score")
public class Score implements Serializable {
	
	private static final long serialVersionUID = -0L;

	//会员ID 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//会员ID 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	//总积分 
	private int totalScore;
	
	//有效积分 
	private int validScore;
	
	//消费积分 
	private int expenseScore;
	
	//冻结积分 
	private int freezeScore;
	
	//添加时间 
	private Date addTime;
	
	//添加IP 
	private String addIp;

	public Score() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Score(User user) {
		super();
		this.user = user;
		this.addTime = new Date();
	}

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

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getValidScore() {
		return validScore;
	}

	public void setValidScore(int validScore) {
		this.validScore = validScore;
	}

	public int getExpenseScore() {
		return expenseScore;
	}

	public void setExpenseScore(int expenseScore) {
		this.expenseScore = expenseScore;
	}

	public int getFreezeScore() {
		return freezeScore;
	}

	public void setFreezeScore(int freezeScore) {
		this.freezeScore = freezeScore;
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
