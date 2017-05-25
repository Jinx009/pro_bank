package com.rongdu.p2psys.core.rule;

/**
 * 规则约束-注册
 * 
 * @author xx
 * @version 2.0
 * @since 2014年1月24日
 */
public class RegisterRuleCheck extends RuleCheck {
	/** 注册是否填写验证码 */
	public boolean enable_codeCheck = true;
	/** 注册成功是否自动登录 */
	public boolean auto_login = true;

	@Override
	public boolean checkRule() {
		return false;
	}

	public boolean isAuto_login() {
		return auto_login;
	}

	public void setAuto_login(boolean auto_login) {
		this.auto_login = auto_login;
	}

	public boolean isEnable_codeCheck() {
		return enable_codeCheck;
	}

	public void setEnable_codeCheck(boolean enable_codeCheck) {
		this.enable_codeCheck = enable_codeCheck;
	}

}
