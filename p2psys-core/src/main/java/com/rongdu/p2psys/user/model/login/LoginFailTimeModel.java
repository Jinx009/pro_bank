package com.rongdu.p2psys.user.model.login;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.rule.LoginRuleCheck;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 处理用户登录失败 记录登录失败次数，处理锁定
 * 
 * @author xx
 * @version 2.0
 * @since 2014年2月11日
 */
public class LoginFailTimeModel {

	private LoginRuleCheck loginRuleCheck = (LoginRuleCheck) Global.getRuleCheck("login");
	private UserService userService;

	public void dealUserStatus(User user) {
		// user不为空，登录失败或登录成功
		// 登录失败，密码为空

		if (StringUtil.isBlank(user.getPwd())) {// 密码错误
			// 增加登录失败次数，达到3次锁定用户
		} else {

		}
	}
}
