package com.rongdu.p2psys.core.rule;

import java.util.Map;

/**
 * 规则约束-借款管理费
 * 
 * @author cx
 * @version 2.0
 * @since 2014年1月23日
 */
public class BorrowManageFeeRuleCheck extends RuleCheck {
	/** 计算方式 */
	public int cal_style;
	/** 月收费比率 */
	public MonthRate monthRate;

	public BorrowManageFeeRuleCheck() {
		super();
	}

	@Override
	public boolean checkRule() {
		return false;
	}

	public static class MonthRate {
		public Map<String, String> month_rate;
	}

}
