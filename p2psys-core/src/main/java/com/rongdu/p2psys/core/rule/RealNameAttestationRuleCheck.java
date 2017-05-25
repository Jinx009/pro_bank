package com.rongdu.p2psys.core.rule;

/**
 * 实名(身份证)校验
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月21日
 */
public class RealNameAttestationRuleCheck extends RuleCheck {

	public int status;
	public String check_url;
	public String username;
	public String password;
	public int need_upload_card_pic;

	public String style() {
		String identify = "manually";
		if (this.status == 1 && this.need_upload_card_pic == 0) {// 自动认证审核开启
			identify = "ID5";
		}
		return identify;
	}

	@Override
	public boolean checkRule() {

		return false;
	}

}
