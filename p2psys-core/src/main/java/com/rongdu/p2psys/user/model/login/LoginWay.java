package com.rongdu.p2psys.user.model.login;

import com.rongdu.p2psys.user.domain.User;

/**
 * 登录方式
 * 
 * @author xx
 * @version 2.0
 * @since 2014年2月11日
 */
public interface LoginWay {

	/**
	 * 登录
	 * 
	 * @param user
	 * @return
	 */
	public User doLogin(User user);
}
