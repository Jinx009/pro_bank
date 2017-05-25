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
@Table(name = "rd_score_log")
public class ScoreLog implements Serializable {
	
	private static final long serialVersionUID = -0L;
	
	// 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//会员ID 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	//变动积分数值 
	private int score;
	
	//日志类型
	private String type;
	
	//剩余可用积分 
	private int validScore;
	
	//剩余冻结积分 
	private int freezeScore;
	
	//积分类型代码 
	private String scoreTypeNid;
	
	//积分类型名称 
	private String scoreTypeName;
	
	//备注 
	private String remark;
	
	//添加时间 
	private Date addTime;
	
	//添加IP 
	private String addIp;

	public ScoreLog() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ScoreLog(Score item) {
		super();
		// TODO Auto-generated constructor stub
		this.setValidScore(item.getValidScore());
		this.setFreezeScore(item.getFreezeScore());
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getValidScore() {
		return validScore;
	}

	public void setValidScore(int validScore) {
		this.validScore = validScore;
	}

	public int getFreezeScore() {
		return freezeScore;
	}

	public void setFreezeScore(int freezeScore) {
		this.freezeScore = freezeScore;
	}

	public String getScoreTypeNid() {
		return scoreTypeNid;
	}

	public void setScoreTypeNid(String scoreTypeNid) {
		this.scoreTypeNid = scoreTypeNid;
	}

	public String getScoreTypeName() {
		return scoreTypeName;
	}

	public void setScoreTypeName(String scoreTypeName) {
		this.scoreTypeName = scoreTypeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
