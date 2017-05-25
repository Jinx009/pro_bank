package com.rongdu.p2psys.core.model;

import java.io.Serializable;

public class RankModel implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6632945396689637373L;
	private long userId;
	private String username;
	private String realname;
	private double tenderMoney;
	private String tenderCount;// 投标次数
	private String lastTenderTime;// 最后投标时间

	/**
	 * @return the userId
	 */
	public long getuserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setuserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username.charAt(0) + "***"
				+ username.charAt(username.length() - 1);
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the realname
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * @param realname the realname to set
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}

	/**
	 * @return the tenderMoney
	 */
	public double getTenderMoney() {
		return tenderMoney;
	}

	/**
	 * @param tenderMoney the tenderMoney to set
	 */
	public void setTenderMoney(double tenderMoney) {
		this.tenderMoney = tenderMoney;
	}

	public String getTenderCount() {
		return tenderCount;
	}

	public void setTenderCount(String tenderCount) {
		this.tenderCount = tenderCount;
	}

	public String getLastTenderTime() {
		return lastTenderTime;
	}

	public void setLastTenderTime(String lastTenderTime) {
		this.lastTenderTime = lastTenderTime;
	}
}
