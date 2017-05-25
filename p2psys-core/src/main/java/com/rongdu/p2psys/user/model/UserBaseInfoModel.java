package com.rongdu.p2psys.user.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.user.domain.UserBaseInfo;

/**
 * 用户基本信息
 * 
 * @author wzh
 * @version 2.0
 * @since 2014年11月18日
 */
public class UserBaseInfoModel extends UserBaseInfo {

	public static UserBaseInfoModel instance(UserBaseInfo userBaseInfo) {
		UserBaseInfoModel userBaseInfoModel = new UserBaseInfoModel();
		BeanUtils.copyProperties(userBaseInfo, userBaseInfoModel);
		return userBaseInfoModel;
	}
}
