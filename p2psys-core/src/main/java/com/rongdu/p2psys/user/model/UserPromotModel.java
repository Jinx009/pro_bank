package com.rongdu.p2psys.user.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.user.domain.UserPromot;

public class UserPromotModel extends UserPromot {

	private static final long serialVersionUID = -8213699821697340967L;
	
	/** 用户名 */
	private String userName;
	
	/** 真实姓名 */
	private String realName;
	
	public static UserPromotModel instance(UserPromot userPromot) {
		UserPromotModel userPromotModel = new UserPromotModel();
		BeanUtils.copyProperties(userPromot, userPromotModel);
		return userPromotModel;
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
	
}
