package com.rongdu.p2psys.user.model.login;

import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 登录方式-以邮箱登录
 * 
 * @author xx
 * @version 2.0
 * @since 2014年2月11日
 */
public class EmailLoginWay implements LoginWay {
	private UserService userService;
	private boolean enable = false;

	public EmailLoginWay(boolean enable) {
		super();
		this.enable = enable;
	}

	@Override
	public User doLogin(User user) {
		User u = null;
		if (enable && user.getUserName().contains("@")) {// 邮箱地址必须含有@
			userService = (UserService) BeanUtil.getBean("userService");
			u = userService.getUserByEmail(user.getUserName());
		}
		return u;
	}

}
