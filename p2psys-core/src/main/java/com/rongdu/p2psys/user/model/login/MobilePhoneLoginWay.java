package com.rongdu.p2psys.user.model.login;

import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 登录方式-以手机号登录
 * 
 * @author xx
 * @version 2.0
 * @since 2014年2月11日
 */
public class MobilePhoneLoginWay implements LoginWay {
	private UserService userService;
	private boolean enable = false;

	public MobilePhoneLoginWay(boolean enable) {
		super();
		this.enable = enable;
	}

	@Override
	public User doLogin(User user) {
		User u = null;
		if (enable && ValidateUtil.isPhone(user.getUserName())) {// 手机号
			userService = (UserService) BeanUtil.getBean("userService");
			u = userService.getUserByMobilePhone(user.getUserName());
		}
		return u;
	}

}
