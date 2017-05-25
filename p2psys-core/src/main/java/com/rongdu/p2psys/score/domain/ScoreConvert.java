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
@Table(name = "rd_score_convert")
public class ScoreConvert implements Serializable {

	private static final long serialVersionUID = -0L;
	
	/**
	 * 0未审核
	 */
	public static final byte WAIT_AUDIT = 0; 
	
	/**
	 * 1审核通过
	 */
	public static final byte PASS_AUDIT = 1;
	
	/**
	 * 2审核不通过
	 */
	public static final byte NOT_PASS_AUDIT = 2;
	
	/**
	 * 3无用数据
	 */
	public static final byte FAIL_AUDIT = 3;
	
	//主键ID 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	//会员ID 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	//兑换的积分数值 
	private int score;
	
	//实际的兑换金额 
	private double money;
	
	//状态：0未审核，1审核通过，2审核不通过，-1无用数据 
	private byte status;
	
	//添加时间 
	private Date addTime;
	
	//审核时间 
	private Date verifyTime;
	
	//审核人 
	private String verifyUser;
	
	//审核人ID 
	private long verifyUserId;
	
	//审核备注 
	private String verifyRemark;
	
	//备注 
	private String remark;
	
	//积分类型代码 
	private String scoreTypeNid;
	
	//积分类型名称 
	private String scoreTypeName;

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

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getVerifyUser() {
		return verifyUser;
	}

	public void setVerifyUser(String verifyUser) {
		this.verifyUser = verifyUser;
	}

	public long getVerifyUserId() {
		return verifyUserId;
	}

	public void setVerifyUserId(long verifyUserId) {
		this.verifyUserId = verifyUserId;
	}

	public String getVerifyRemark() {
		return verifyRemark;
	}

	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
}
