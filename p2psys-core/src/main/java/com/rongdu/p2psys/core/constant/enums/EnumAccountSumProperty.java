package com.rongdu.p2psys.core.constant.enums;

/**
 * 资金合计类型
 * 
 * @author
 * @version 1.0
 * @since 2013-11-28
 */
public enum EnumAccountSumProperty {

	INTEREST("interest"), // 利息总和

	RECHARGE("recharge"), // 充值

	AWARD("award"), // 奖励

	INTEREST_FEE("interest_fee"), // 扣除的利息手续费

	CASH("cash"), // 提现

	DEDUCT("deduct"), // 扣款总计

	REPAY_CASH("repay_cash"), // 还款钱

	HUIKUAN("huikuan"), // 回款钱

	USED_RECHARGE("used_recharge"), // 已使用充值

	USED_INTEREST("used_interest"), // 已使用利息

	USED_AWARD("used_award"), // 已使用奖励

	USED_HUIKUAN("used_huikuan"), // 已使用回款

	USED_BORROW_CASH("used_borrow_cash"), // 已使用借款

	BORROW_CASH("borrow_cash"), // 借款合计

	CASH_FEE("cash_fee"), // 还款金额

	HUIKUAN_INTEREST("huikuan_interest"), // 回款利息合计

	USED_HUIKUAN_INTEREST("used_huikuan_interest");// 已使用回款利息合计

	EnumAccountSumProperty(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return this.value;
	}

}
