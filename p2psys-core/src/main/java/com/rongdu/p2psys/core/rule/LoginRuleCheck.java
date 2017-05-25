package com.rongdu.p2psys.core.rule;

/**
 * 规则约束-登录
 * 
 * @author xx
 * @version 2.0
 * @since 2014年1月24日
 */
public class LoginRuleCheck extends RuleCheck {
	/** 登录是否填写验证码 */
	public boolean enable_codeCheck = true;
	/** 当日最大登录失败次数，达到即锁定 */
	public int max_login_fail_times = 5;
	/** 当日最大登录失败次数，达到即需要验证码 */
	public int max_fail_times_code = 5;
	/** 连续输入错误时间范围 */
	public int time_range = 120;
	/** 是否用户名登录 **/
	public boolean userName_login = true;
	/** 是否邮箱登录 **/
	public boolean email_login = true;
	/** 是否电话登录 **/
	public boolean mobile_phone_login = true;

	@Override
	public boolean checkRule() {
		return false;
	}

	public boolean isEnable_codeCheck() {
		return enable_codeCheck;
	}

	public void setEnable_codeCheck(boolean enable_codeCheck) {
		this.enable_codeCheck = enable_codeCheck;
	}

	public int getMax_login_fail_times() {
		return max_login_fail_times;
	}

	public void setMax_login_fail_times(int max_login_fail_times) {
		this.max_login_fail_times = max_login_fail_times;
	}

	public boolean isuserName_login() {
		return userName_login;
	}

	public void setuserName_login(boolean userName_login) {
		this.userName_login = userName_login;
	}

	public boolean isEmail_login() {
		return email_login;
	}

	public void setEmail_login(boolean email_login) {
		this.email_login = email_login;
	}

	public boolean isMobile_phone_login() {
		return mobile_phone_login;
	}

	public void setMobile_phone_login(boolean mobile_phone_login) {
		this.mobile_phone_login = mobile_phone_login;
	}

    public int getMax_fail_times_code() {
        return max_fail_times_code;
    }

    public void setMax_fail_times_code(int max_fail_times_code) {
        this.max_fail_times_code = max_fail_times_code;
    }

    public int getTime_range() {
        return time_range;
    }

    public void setTime_range(int time_range) {
        this.time_range = time_range;
    }

    public boolean isUserName_login() {
        return userName_login;
    }

    public void setUserName_login(boolean userName_login) {
        this.userName_login = userName_login;
    }


}
