package com.rongdu.p2psys.user.model.login;

import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 登录方式-以用户名登录
 * 
 * @author xx
 * @version 2.0
 * @since 2014年2月11日
 */
public class UserNameLoginWay implements LoginWay {
	private UserService userService;
	private boolean enable = false;

	public UserNameLoginWay(boolean enable) {
		super();
		this.enable = enable;
	}

	@Override
	public User doLogin(User user) {
		User u = null;
		if (enable && !user.getUserName().contains("@") && !ValidateUtil.isPhone(user.getUserName())) {// 用户名非邮箱非手机号，同时注册时用户名不能包含@，不能是手机号
			userService = (UserService) BeanUtil.getBean("userService");
			u = userService.getUserByUserName(user.getUserName());
		}
		return u;
	}

}
