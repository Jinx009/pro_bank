package com.rongdu.p2psys.user.model.getpwd;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserService;

public class BaseGetPwd implements GetPwd {
	protected UserService userService;
	protected UserCacheService userCacheService;

	@Override
	public User getPwdStep1(HttpServletRequest request, User user,
			String valid_code) throws Exception {
		return null;
	}

	@Override
	public void getPwdStep2(User user, String code) throws Exception {

	}

	@Override
	public void getPwdReset(Map<String, Object> session, User user,
			String confirm_new_pwd) throws Exception {
		String getPwdSign = (String) session.get("getPwdSign");
		if (StringUtil.isBlank(getPwdSign)) {
			throw new UserException("非法提交！", 1);
		}
		UserModel userModel = UserModel.instance(user);
		userModel.setConfirmNewPwd(confirm_new_pwd);
		userModel.validGetPwdReset();
		userService = (UserService) BeanUtil.getBean("userService");
		User u;

		if (!"".equals(user.getEmail()) && user.getEmail() != null) {
			if (!getPwdSign.equals(user.getEmail())) {
				throw new UserException("非法提交！", 1);
			}
			u = userService.getUserByEmail(user.getEmail());
		} else {
			if (!getPwdSign.equals(user.getMobilePhone())) {
				throw new UserException("非法提交！", 1);
			}
			u = userService.getUserByMobilePhone(user.getMobilePhone());
		}
		u.setPwd(user.getPwd());
		userService.modifyPwd(u);
		session.put("logintime", System.currentTimeMillis());
	}

	@Override
	public User getPayPwdStep1(HttpServletRequest request, User user,
			String valid_code) throws Exception {

		return null;
	}

	@Override
	public void getPayPwdStep2(User user, String code) throws Exception {

	}

	@Override
	public void getPayPwdReset(Map<String, Object> session, User user,
			String confirm_new_pwd) throws Exception {
		UserModel userModel = UserModel.instance(user);
		userModel.setConfirmNewPwd(confirm_new_pwd);
		userModel.validGetPayPwdReset();
		userService = (UserService) BeanUtil.getBean("userService");
		userCacheService = (UserCacheService) BeanUtil
				.getBean("userCacheService");
		User u;
		if (!"".equals(user.getEmail()) && user.getEmail() != null) {
			u = userService.getUserByEmail(user.getEmail());
		} else {
			u = userService.getUserByMobilePhone(user.getMobilePhone());
		}
		u.setPayPwd(user.getPayPwd());
		userService.modifyPaypwd(u);
		userCacheService.modifyPayPwdTime(u.getUserId());
		session.put("logintime", System.currentTimeMillis());
		session.put(Constant.SESSION_USER,u);
	}

}
