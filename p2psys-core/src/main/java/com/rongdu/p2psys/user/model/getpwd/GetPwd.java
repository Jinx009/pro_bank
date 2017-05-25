package com.rongdu.p2psys.user.model.getpwd;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.p2psys.user.domain.User;

/**
 * 忘记密码-找回密码
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月2日
 */
public interface GetPwd {

	/**
	 * 第一步-填写用户名+邮箱/用户名+手机号
	 * 
	 * @param user
	 * @param valid_code
	 * @return
	 */
	public User getPwdStep1(HttpServletRequest request, User user, String valid_code) throws Exception;

	/**
	 * 第二步-填写校验码
	 * 
	 * @param user
	 * @param code
	 * @return
	 */
	public void getPwdStep2(User user, String code) throws Exception;

	/**
	 * 第三步-重置密码
	 * 
	 * @param user
	 */
	public void getPwdReset(Map<String, Object> session, User user, String confirm_new_pwd) throws Exception;
	
	/**
	 * 第一步-填写用户名+邮箱/用户名+手机号
	 * 
	 * @param request
	 * @param user
	 * @param valid_code
	 * @return
	 * @throws Exception
	 */
	public User getPayPwdStep1(HttpServletRequest request, User user, String valid_code) throws Exception;
	
	/**
	 * 第二步-填写校验码
	 * 
	 * @param user
	 * @param code
	 * @return
	 */
	public void getPayPwdStep2(User user, String code) throws Exception;

	/**
	 * 第三步-重置密码
	 * 
	 * @param user
	 */
	public void getPayPwdReset(Map<String, Object> session, User user, String confirm_new_pwd) throws Exception;
}
