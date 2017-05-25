package com.rongdu.p2psys.user.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.user.domain.UserFreeze;

public class UserFreezeModel extends UserFreeze {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;
	
	/** 条件查询 */
	private String searchName;
	public static UserFreezeModel instance(UserFreeze freeze) {
		UserFreezeModel freezeModel = new UserFreezeModel();
		BeanUtils.copyProperties(freeze, freezeModel);
		return freezeModel;
	}

	public UserFreeze prototype() {
		UserFreeze freeze = new UserFreeze();
		BeanUtils.copyProperties(this, freeze);
		return freeze;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	
}
