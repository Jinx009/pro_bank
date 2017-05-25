package com.rongdu.p2psys.core.rule;


/**
 * 积分兑换现金规则Model
 */
public class ScoreConvertMoneyRuleCheck extends RuleCheck {

	/**
	 * 兑换基数
	 */
	public int convert_basic;
	
	/**
	 * 最小起兑参数
	 */
	public int convert_min;
	
	/**
	 * 积分兑换现金状态
	 */
	public byte status;
	
	@Override
	public boolean checkRule() {
		// TODO Auto-generated method stub
		return false;
	}

}
