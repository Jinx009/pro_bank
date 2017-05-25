package com.rongdu.p2psys.core.rule;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.exception.UserException;

/**
 * 规则约束-平台安全性
 * 
 * @author xx
 * @version 2.0
 * @since 2014年1月23日
 */
public class SafetyRuleCheck extends RuleCheck {
	/** 后台安全性 */
	public SafetyAdmin admin;
	/** 前台安全性 */
	public SafetyFront front;
	/** 前台密保 */
	public SafetyFrontPwdSecret front_pwd_secret;

	public SafetyRuleCheck() {
		super();
	}

	@Override
	public boolean checkRule() {
		return false;
	}

	public static class SafetyAdmin {
		/** 状态，1：启用，0：关闭 */
		public int status;
		/** 用户名-禁用关键字 */
		public String unallowed_username;
		/** 定期（天）修改密码 */
		public int regular_modpwd_day;
		/** 登录失败是否锁定，1：启用，0：关闭 */
		public int login_fail_isLock;
		/** 一天内最多登录失败次数，达到及锁定 */
		public int max_login_fail_times;
		/** 登录失败锁定时间（分） */
		public int lock_time_min;

		public void setStatus(int status) {
			this.status = status;
		}

		public void setUnallowed_username(String unallowed_username) {
			this.unallowed_username = unallowed_username;
		}

		public void setRegular_modpwd_day(int regular_modpwd_day) {
			this.regular_modpwd_day = regular_modpwd_day;
		}

		public void setLogin_fail_isLock(int login_fail_isLock) {
			this.login_fail_isLock = login_fail_isLock;
		}

		public void setMax_login_fail_times(int max_login_fail_times) {
			this.max_login_fail_times = max_login_fail_times;
		}

		public void setLock_time_min(int lock_time_min) {
			this.lock_time_min = lock_time_min;
		}

	}

	public static class SafetyFront {
		/** 状态，1：启用，0：关闭 */
		public int status;
		/** 用户名-禁用关键字 */
		public String unallowed_username;
		/** 长期未登录定期（天）锁定 */
		public int unLogin_regular_lock_day;

		public void setStatus(int status) {
			this.status = status;
		}

		public void setUnallowed_username(String unallowed_username) {
			this.unallowed_username = unallowed_username;
		}

		public void setUnLogin_regular_lock_day(int unLogin_regular_lock_day) {
			this.unLogin_regular_lock_day = unLogin_regular_lock_day;
		}

	}

	public static class SafetyFrontPwdSecret {
		public String password_token;
		public int phone_sms;

		public void setPassword_token(String password_token) {
			this.password_token = password_token;
		}

		public void setPhone_sms(int phone_sms) {
			this.phone_sms = phone_sms;
		}

	}

	public void setAdmin(SafetyAdmin admin) {
		this.admin = admin;
	}

	public void setFront(SafetyFront front) {
		this.front = front;
	}

	public void setFront_pwd_secret(SafetyFrontPwdSecret front_pwd_secret) {
		this.front_pwd_secret = front_pwd_secret;
	}

	/**
	 * 数据校验方法
	 */
	public int hasFontUnallowedUsername(User user) {
		if (user == null || StringUtil.isBlank(user.getUserName())) {
			throw new UserException("请填写用户名!", 1);
		}
		if (front != null && front.status == 1) {
			String fuu = front.unallowed_username;
			if (StringUtil.isNotBlank(fuu) && fuu.contains(user.getUserName())) {
				throw new UserException("请勿使用含有敏感词汇的用户名：" + fuu, 1);
			}
		}
		return 1;
	}

}
