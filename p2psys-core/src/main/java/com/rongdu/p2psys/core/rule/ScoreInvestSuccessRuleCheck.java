package com.rongdu.p2psys.core.rule;

import java.util.List;

public class ScoreInvestSuccessRuleCheck extends RuleCheck {

	// 规则状态是否启用，1启用，0不启用
	public byte status;
	
	// 不能获得积分的标种
	public List<Integer> borrow_type_no;
	
	// 月标奖励积分投资额度基数
	public double month_base_money;
	
	// 天标奖励积分投资额度基数
	public double day_base_money;
	
	// 月标奖励是否启用，1启用，0不启用
	public byte is_month;
	
	// 天标奖励是否启用，1启用，0不启用
	public byte is_day;
	
	// 积分进位约束
	public double decimal_manage;
	
	@Override
	public boolean checkRule() {
		// TODO Auto-generated method stub
		return false;
	}

}
