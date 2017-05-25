package com.rongdu.p2psys.core.rule;


public class ScoreConvertVipRuleCheck extends RuleCheck {
	
	/**
	 * 积分兑换规则是否启用
	 */
	public byte status;
	
	/**
	 * 兑换VIP需要的积分
	 */
	public int convert_score;
	
	/**
	 * 兑换VIP的延长时间
	 */
	public int vip_time;
	
	/**
	 * 兑换VIP约束是否启用
	 */
	public int is_check;
	
	/**
	 * 兑换VIP间隔时间（单位：月）
	 */
	public int check_time;
	
	@Override
	public boolean checkRule() {
		// TODO Auto-generated method stub
		return false;
	}

}
