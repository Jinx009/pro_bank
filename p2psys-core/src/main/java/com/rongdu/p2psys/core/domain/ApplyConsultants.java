package com.rongdu.p2psys.core.domain;

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

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.domain.User;

/**
 * 私人顾问
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
@Entity
@Table(name = (Global.DB_PREFIX + "apply_consultants"))
public class ApplyConsultants implements Serializable {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 申请人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 预约专家
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "consultant_id")
	private Consultant consultant;
	
	/**
	 * 状态，是否已受理 1已受理 0未受理
	 */
	private int status;
	/**
	 * 预约时间一
	 */
	private String timeFirst;

	/**
	 * 预约时间二
	 */
	private String timeSecond;

	/**
	 * 预约时间
	 */
	private Date addTime;

	/**
	 * IP
	 */
	private String addIp;

	public ApplyConsultants() {
		super();
	}
	
	public ApplyConsultants(User user,
			Consultant consultant, String timeFirst,
			String timeSecond) {
		super();
		this.user = user;
		this.consultant = consultant;
		this.timeFirst = timeFirst;
		this.timeSecond = timeSecond;
		this.addTime = new Date();
		this.addIp = Global.getIP();
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Consultant getConsultant() {
		return consultant;
	}

	public void setConsultant(Consultant consultant) {
		this.consultant = consultant;
	}

	public String getTimeFirst() {
		return timeFirst;
	}

	public void setTimeFirst(String timeFirst) {
		this.timeFirst = timeFirst;
	}

	public String getTimeSecond() {
		return timeSecond;
	}

	public void setTimeSecond(String timeSecond) {
		this.timeSecond = timeSecond;
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
