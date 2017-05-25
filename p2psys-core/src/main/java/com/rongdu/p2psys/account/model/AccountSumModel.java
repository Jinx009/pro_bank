package com.rongdu.p2psys.account.model;

import com.rongdu.p2psys.account.domain.Account;

public class AccountSumModel extends Account {

	private double waitCollect;

	private double waitRepay;

	private String userName;

	private String realName;

	private double netAssets;

	private double jinWaitRepay;

	public double getWaitCollect() {
		return waitCollect;
	}

	public void setWaitCollect(double waitCollect) {
		this.waitCollect = waitCollect;
	}

	public double getWaitRepay() {
		return waitRepay;
	}

	public void setWaitRepay(double waitRepay) {
		this.waitRepay = waitRepay;
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

	public double getNetAssets() {
		return netAssets;
	}

	public void setNetAssets(double netAssets) {
		this.netAssets = netAssets;
	}

	public double getJinWaitRepay() {
		return jinWaitRepay;
	}

	public void setJinWaitRepay(double jinWaitRepay) {
		this.jinWaitRepay = jinWaitRepay;
	}

}
