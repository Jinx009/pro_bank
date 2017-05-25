package com.rongdu.p2psys.user.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

/**
 * rd_user_identify 实体类 用户认证状态
 * 
 * @author sj
 * @version 2.0
 * @since 2014-02-11
 */
@Entity
@Table(name = "rd_user_identify")
public class UserIdentify {

	/** 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/** 用户 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/** 实名认证 0:未认证 1：实名认证通过 2：实名认证待审核 -1：实名认证未通过 */
	private int realNameStatus;

	/** 邮箱认证 0:未认证 1：邮箱认证通过 */
	@Type(type = "int")
	private int emailStatus;

	/** 手机认证 -1:未通过,0:未认证,1:通过,2:待审核 */
	private int mobilePhoneStatus;

	/** 实名认证审核时间 */
	private Date realNameVerifyTime;

	/** 手机认证审核时间 */
	private Date mobilePhoneVerifyTime;

	/** 状态 0:未申请,2:待审核，1:审核成功 ，已经是vip -1：审核不成功*/
	private int vipStatus;

	/** VIP审核时间 */
	private Date vipVerifyTime;
	/** VIP截止时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "vip_end_time")
	private Date vipEndTime;
	
	/** 视频认证状态 **/
	private int videoStatus;
	/** 视频认证时间 **/
	private Date videoVerifyTime;

	public UserIdentify() {
		super();
	}

	public UserIdentify(User user) {
		super();
		this.user = user;
	}
	public UserIdentify(User user, int emailStatus) {
		super();
		this.user = user;
		this.emailStatus = emailStatus;
	}

	public long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRealNameStatus() {
		return realNameStatus;
	}

	public void setRealNameStatus(int realNameStatus) {
		this.realNameStatus = realNameStatus;
	}

	public int getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(int emailStatus) {
		this.emailStatus = emailStatus;
	}

	public int getMobilePhoneStatus() {
		return mobilePhoneStatus;
	}

	public void setMobilePhoneStatus(int mobilePhoneStatus) {
		this.mobilePhoneStatus = mobilePhoneStatus;
	}

	public Date getRealNameVerifyTime() {
		return realNameVerifyTime;
	}

	public void setRealNameVerifyTime(Date realNameVerifyTime) {
		this.realNameVerifyTime = realNameVerifyTime;
	}

	public Date getMobilePhoneVerifyTime() {
		return mobilePhoneVerifyTime;
	}

	public void setMobilePhoneVerifyTime(Date mobilePhoneVerifyTime) {
		this.mobilePhoneVerifyTime = mobilePhoneVerifyTime;
	}

	public int getVipStatus() {
		return vipStatus;
	}

	public void setVipStatus(int vipStatus) {
		this.vipStatus = vipStatus;
	}

	public Date getVipVerifyTime() {
		return vipVerifyTime;
	}

	public void setVipVerifyTime(Date vipVerifyTime) {
		this.vipVerifyTime = vipVerifyTime;
	}

	public Date getVipEndTime() {
		return vipEndTime;
	}

	public void setVipEndTime(Date vipEndTime) {
		this.vipEndTime = vipEndTime;
	}

	public int getVideoStatus() {
		return videoStatus;
	}

	public void setVideoStatus(int videoStatus) {
		this.videoStatus = videoStatus;
	}

	public Date getVideoVerifyTime() {
		return videoVerifyTime;
	}

	public void setVideoVerifyTime(Date videoVerifyTime) {
		this.videoVerifyTime = videoVerifyTime;
	}

}
