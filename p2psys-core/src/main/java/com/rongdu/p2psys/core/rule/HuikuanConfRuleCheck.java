package com.rongdu.p2psys.core.rule;

import java.util.Map;

/**
 * 回款配置
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-30
 */
public class HuikuanConfRuleCheck extends RuleCheck {

	public int jinRefuse;
	public int miaoRefuse;
	public int dayRefuse;
	public int lowest_huikuan;
	public HuikuanAward huikuan_award;

	public static class HuikuanAward {
		public int cal_style;
		public Map<String, Object> month_rate;
	}

	@Override
	public boolean checkRule() {
		// TODO Auto-generated method stub
		return false;
	}

}
