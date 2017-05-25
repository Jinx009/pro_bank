package com.rongdu.p2psys.user.model.getpwd;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 忘记密码-通过邮箱找回密码
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月2日
 */
public class GetPwdEmail extends BaseGetPwd {

	@Override
	public User getPwdStep1(HttpServletRequest request, User user, String valid_code) {
		UserModel userModel = UserModel.instance(user);
		userModel.setValidCode(valid_code);
		userModel.validGetPwdByEmailStep1();
		userService = (UserService) BeanUtil.getBean("userService");
		return userService.getPwdByEmail( user.getUserName(), user.getEmail());
	}

	@Override
	public void getPwdStep2(User userAll, String code) throws Exception {
		UserModel userModel = UserModel.instance(userAll);
		userModel.setCode(code);
		userModel.validGetPwdByEmailStep2();
	}
	
	@Override
	public User getPayPwdStep1(HttpServletRequest request, User user, String valid_code) {
//		UserModel userModel = UserModel.instance(user);
//		userModel.setValidCode(valid_code);
//		userModel.validGetPwdByEmailStep1();
		userService = (UserService) BeanUtil.getBean("userService");
		return userService.getPayPwdByEmail( user.getUserName(), user.getEmail());
	}

	@Override
	public void getPayPwdStep2(User userAll, String code) throws Exception {
		UserModel userModel = UserModel.instance(userAll);
		userModel.setCode(code);
		userModel.validGetPayPwdByEmailStep2();
	}

}
