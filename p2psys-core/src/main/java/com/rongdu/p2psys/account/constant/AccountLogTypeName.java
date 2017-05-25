package com.rongdu.p2psys.account.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 资金记录类型
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年1月7日
 */
public class AccountLogTypeName {

	public static AccountLogTypeName accountLogTypeName;

	public Map<String, String> typeNameMap = new HashMap<String, String>();

	public AccountLogTypeName() {
		typeNameMap.put("wait_award", "待收奖励");
		typeNameMap.put("wait_capital", "待收本金");
		typeNameMap.put("recharge", "用户充值");
		typeNameMap.put("borrow_fee", "借款手续费");
		typeNameMap.put("cash_cancel", "取消提现解冻");
		typeNameMap.put("cash_frost", "提现冻结");
		typeNameMap.put("tender", "投标");
		typeNameMap.put("borrow_success", "借款入帐");
		typeNameMap.put("invest", "扣除冻结款");
		typeNameMap.put("stop_tender_unfreeze", "截标解冻多余费用");
		typeNameMap.put("award_deduct", "扣除投标奖励");
		typeNameMap.put("award_add", "	投标奖励");
		typeNameMap.put("repaid", "	还款");
		typeNameMap.put("repaid_capital", "	还款本金");
		typeNameMap.put("repaid_interest", "还款利息");
		typeNameMap.put("repayment_success", "还款成功");
		typeNameMap.put("vip_fee", "vip会员费");
		typeNameMap.put("recharge_success", "充值成功");
		typeNameMap.put("manage_fee", "利息管理费用");
		typeNameMap.put("freeze", "	冻结资金");
		typeNameMap.put("unfreeze", "解冻资金");
		typeNameMap.put("wait_interest", "待收利息");
		typeNameMap.put("cash_success", "提现成功");
		typeNameMap.put("cash_fail", "提现失败");
		typeNameMap.put("account_back", "扣款");
		typeNameMap.put("capital_collect", "本金收回");
		typeNameMap.put("interest_collect", "利息收回");
		typeNameMap.put("offrecharge_award", "线下充值奖励费");
		typeNameMap.put("sms_fee", "短信费");
		typeNameMap.put("repayment_award", "还款奖励");
		typeNameMap.put("vip_freeze", "VIP申请冻结费用");
		typeNameMap.put("account_back_freeze", "后台扣费冻结");
		typeNameMap.put("account_back_unfreeze", "解冻后台扣费冻结");
		typeNameMap.put("account_recharge_fee", "充值收费");
		typeNameMap.put("new_borrow_fee_freeze", "发标费用冻结");
		typeNameMap.put("new_borrow_fee_unfreeze", "解冻发标冻结费用");
		typeNameMap.put("borrow_repay_ext_intere", "缴纳展期利息");
		typeNameMap.put("off_recharge", "线下充值");
		typeNameMap.put("online_recharge", "充值成功");
		typeNameMap.put("late_repayment_income", "逾期利息");
		typeNameMap.put("repay_unfreeze", "还款失败解冻资金");
		typeNameMap.put("late_repayment", "逾期利息");
		typeNameMap.put("off_recharge", "线下充值");
		typeNameMap.put("online_recharge", "线上充值");
		typeNameMap.put("account_back_freeze", "后台扣款");
		typeNameMap.put("account_back", "后台扣款审核通过");
		typeNameMap.put("account_back_unfreeze", "后台扣款审核不通过	");
		typeNameMap.put("bond_sell_capital", "收回本金");
		typeNameMap.put("bond_sell_interest", "收回利息");
		typeNameMap.put("bond_sell_award", "收回奖励");
		typeNameMap.put("bond_sell_manageFee", "债权转让管理费");
		typeNameMap.put("bond_sell_coll_capital", "扣除待收本金");
		typeNameMap.put("bond_sell_coll_interest", "扣除待收利息");
		typeNameMap.put("bond_sell_coll_award", "扣除待收奖励");
		typeNameMap.put("bond_buy_capital", "扣除本金");
		typeNameMap.put("bond_buy_interest", "扣除利息");
		typeNameMap.put("bond_buy_award", "扣除奖励");
		typeNameMap.put("bond_buy_coll_capital", "增加待收本金");
		typeNameMap.put("bond_buy_coll_interest", "增加待收利息");
		typeNameMap.put("bond_buy_coll_award", "增加待收奖励");
		typeNameMap.put("risk_reserve_fee", "风险备用金");
		typeNameMap.put("wait_interest_rate", "加息券待收利息");
		typeNameMap.put("interest_rate_collect", "加息券收回利息");
		typeNameMap.put("bond_sell_interest_manageFee", "债权转让利息管理费");
		typeNameMap.put("back_recharge", "平台充值");
		typeNameMap.put("float_income", "浮动收益");
		typeNameMap.put("ppfund_tender", "现金管理购买扣除资金");
		typeNameMap.put("ppfund_out_capital", "现金管理收回本金");
		typeNameMap.put("ppfund_out_interest", "现金管理收益利息");
		typeNameMap.put("ppfund_out_risk_reserve", "现金管理风险备用金");
		typeNameMap.put("ppfund_out_manage_fee", "现金管理居间服务费");
		typeNameMap.put("ppfund_collection_interest", "现金管理待收收益");
		typeNameMap.put("user_red_packet_exchange", "红包兑换");
		typeNameMap.put("tender_red_packet_success", "红包抵扣");
		typeNameMap.put("tender_red_packet_fail", "投资失败红包扣回");
		typeNameMap.put("ppfud_red_packet_success", "红包抵扣");
		typeNameMap.put("appointment_tender", "预约投资");
		typeNameMap.put("experience_gold_invest","体验标");
		typeNameMap.put("recommend_profit", "推荐收益");
	}

	public static AccountLogTypeName getInstance() {
		if (accountLogTypeName == null) {
			return new AccountLogTypeName();
		} else {
			return accountLogTypeName;
		}
	}

}
