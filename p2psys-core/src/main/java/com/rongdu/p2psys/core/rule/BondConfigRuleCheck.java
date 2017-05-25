package com.rongdu.p2psys.core.rule;

/**
 * 债权参数配置
 * 
 * @author wzh
 * @version 2.0
 * @since 2014年12月15日
 */
public class BondConfigRuleCheck extends RuleCheck {
	
	/** 规则是否启用 */
	public byte status = 1;
	
	/** 债权转让是否需要初审 */
	public byte bondNeedVerify = 0;
	
	/** 收款方式  1:固定，2：比例*/
	public int feeType = 2;
	
	/** 费用 */
	public double sellFee = 0.03;

	/** 最低债权转让折扣率 */
	public double bondAprL = 0.2;
	
	/** 最高债权转让折扣率 */
	public double bondAprH = 1;
	
	/** 持有期需天数 */
	public int holdDays = 15;
	
	/** 距下一期还款到期天数 */
	public int remainDays = 7;
	
	/** 单笔最低转让金额 */
	public double minBondMoney = 1000;
	
	/** 最小申购金额 */
	public double minTenderMoney = 100;
	
	@Override
	public boolean checkRule() {
		return false;
	}
}
