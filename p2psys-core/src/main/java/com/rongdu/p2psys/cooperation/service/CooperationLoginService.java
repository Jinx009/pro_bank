package com.rongdu.p2psys.cooperation.service;

import com.rongdu.p2psys.cooperation.domain.CooperationLogin;
import com.rongdu.p2psys.user.domain.User;

public interface CooperationLoginService {

	/**
	 * 新增联合登陆信息
	 * 
	 * @param cooperation
	 */
	public void addCooperationLogin(CooperationLogin cooperation);

	/**
	 * 据openId和type查询联合登陆信息
	 * 
	 * @param openId
	 * @param type
	 * @return
	 */
	public CooperationLogin getCooperationLogin(String openId, int type);
	
	/**
	 * 联合登陆注册
	 * 
	 * @param user User实体类
	 * @param openType 联合登陆类型
	 * @param openId 联合登陆开放ID
	 * @return User实体类
	 */
	public User doQQRegister(User user, String openType, String openId);
	
	/**
	 * 登录,绑定QQ
	 * 
	 * @param user User实体类
	 * @param isRsa 是否加密
	 * @param openType 联合登陆类型
	 * @param openId 联合登陆开放ID
	 * @return User实体类
	 * @throws Exception 异常
	 */
	User doQQLogin(User user, int isRsa, String openType, String openId) throws Exception;

}
