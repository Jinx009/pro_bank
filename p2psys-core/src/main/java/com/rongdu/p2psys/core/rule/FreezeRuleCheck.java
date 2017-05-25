package com.rongdu.p2psys.core.rule;

/**
 * 冻结规则
 * 
 * @author sj
 * @version 2.0
 * @since 2014年4月22日
 */
public class FreezeRuleCheck extends RuleCheck {

	/** 登录 */
	public String login;
	/** 充值 */
	public String recharge;
	/** 提现 */
	public String cash;
	/** 投标 */
	public String tender;
	/** 发标 */
	public String borrow;

	@Override
	public boolean checkRule() {
		return false;
	}

}
