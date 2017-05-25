package com.rongdu.p2psys.user.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.user.domain.UserInvite;

/**
 * 用户邀请Model
 * 
 * @author zf
 * @version 2.0
 * @since 2014年10月24日
 */
public class UserInviteModel extends UserInvite {
	/**
	 * 搜索名称
	 */
	private String searchName;
	/**
	 * 邀请人用户名
	 */
	private String inviteUserName;
	/**
	 * 邀请人真实姓名
	 */
	private String inviteRealName;
	/**
	 * 被邀请人用户名
	 */
	private String userName;
	/**
	 * 被邀请人真实姓名
	 */
	private String realName;
	/**
	 * 被邀请人实名状态 1已认证 2未认证
	 */
	private int realNameStatus;
	/**
	 * 红包派送状态1已派送 2未派送
	 */
	private int status;
	/**
	 * 被邀请人注册时间
	 */
	private Date regTime;
	/**
	 * 被邀请人开始注册时间
	 */
	private String regStartTime;
	/**
	 * 被邀请人结束注册时间
	 */
	private String regEndTime;
	/** 当前页码 */
	private int page;
	/** 每页总数 **/
	private int rows;
	
	public static UserInviteModel instance(UserInvite user) {
		UserInviteModel userModel = new UserInviteModel();
		BeanUtils.copyProperties(user, userModel);
		return userModel;
	}

	public UserInvite prototype() {
		UserInvite user = new UserInvite();
		BeanUtils.copyProperties(this, user);
		return user;
	}

	public String getInviteUserName() {
		return inviteUserName;
	}

	public void setInviteUserName(String inviteUserName) {
		this.inviteUserName = inviteUserName;
	}

	public String getInviteRealName() {
		return inviteRealName;
	}

	public void setInviteRealName(String inviteRealName) {
		this.inviteRealName = inviteRealName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getRealNameStatus() {
		return realNameStatus;
	}

	public void setRealNameStatus(int realNameStatus) {
		this.realNameStatus = realNameStatus;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRegStartTime() {
		return regStartTime;
	}

	public void setRegStartTime(String regStartTime) {
		this.regStartTime = regStartTime;
	}

	public String getRegEndTime() {
		return regEndTime;
	}

	public void setRegEndTime(String regEndTime) {
		this.regEndTime = regEndTime;
	}
}
