package com.rongdu.p2psys.core.rule;

/**
 * 借款标利率限制
 * 
 * @author cx
 * @version 2.0
 * @since 2014-02-19
 */
public class BorrowAprLimitRuleCheck extends RuleCheck {

	public AprLimit apr_limit;

	/** 净值标发标限制用到此属性 **/
	public AccountOwnMoney account_own_money;
	public JinRepayTotal jin_repay_total;
	/** 展期配置 **/
	public Extension extension;

	public static class AprLimit {
		/** =1 利率必须大于config中的最小值 小于config中的最大值 **/
		public int status;
	}

	public static class AccountOwnMoney {
		/** =1 则判断净资产是否小于限制额度 **/
		public int status;
		/** 限制额度 =500 **/
		public int limit;
	}

	public static class JinRepayTotal {
		/** =1 判断净资产是否小于净值标待还总额（默认0） **/
		public int status;
	}

	public static class Extension {
		public boolean extension_enable;
		public int tender_apr;
	}

	@Override
	public boolean checkRule() {
		return false;
	}

}
