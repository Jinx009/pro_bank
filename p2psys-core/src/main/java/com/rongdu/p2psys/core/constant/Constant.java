package com.rongdu.p2psys.core.constant;

/**
 * 常用变量类
 * 
 * @author fuxingxing
 * @date 2012-7-10-上午10:26:20
 * @version 2.0
 */
public final class Constant
{

	/** 数据库表前缀 */
	public static final String DB_PREFIX = "rd_";
	/** 平台交易方ID */
	public static final long ADMIN_ID = 1;

	/*********************** session ****************************************/
	/** 用户 */
	public static final String SESSION_USER = "session_user";
	/** 用户认证状态 */
	public static final String SESSION_USER_IDENTIFY = "session_user_identify";
	/** 后台操作员 */
	public static final String SESSION_OPERATOR = "session_operator";
	/** 系统 */
	public static final String SYSTEM = "system";
	/*********************** 用户资金类型 ****************************************/
	/** 满标审核失败 */
	public static final String BORROW_FULL_VERIFY_FAIL = "borrow_full_verify_fail";
	/** 投标 */
	public static final String TENDER = "tender";
	/** 投标 */
	public static final String APPOINTMENT_TENDER = "appointment_tender";
	/** 用户充值 */
	public static final String RECHARGE = "recharge";
	/** 扣除冻结款 */
	public static final String INVEST = "invest";
	/** 充值成功 */
	public static final String RECHARGE_SUCCESS = "recharge_success";
	/** 充值失败 */
	public static final String RECHARGE_FAIL = "recharge_fail";
	/** 冻结资金 */
	public static final String FREEZE = "freeze";
	/** 解冻资金 */
	public static final String UNFREEZE = "unfreeze";
	/** 撤标本金返还 */
	public static final String VIP_CAPITAL_UNFREEZE = "vip_capital_unfreeze";
	/** 撤标扣除利息 */
	public static final String VIP_INTEREST_UNFREEZE = "vip_interest_unfreeze";
	/** 审核失败解冻投资人的资金 */
	public static final String UNFREEZE_NO_PASS = "unfreeze_no_pass";
	/** 借款入帐 */
	public static final String BORROW_SUCCESS = "borrow_success";
	/** 借款手续费 */
	public static final String BORROW_FEE = "borrow_fee";
	/** 风险备用金 */
	public static final String RISK_RESERVE_FEE = "risk_reserve_fee";
	/** 利息管理费用 */
	public static final String MANAGE_FEE = "manage_fee";
	/** 还款 */
	public static final String REPAID = "repaid";
	/** 还款本金 */
	public static final String REPAID_CAPITAL = "repaid_capital";
	/** 还款利息 */
	public static final String REPAID_INTEREST = "repaid_interest";
	/** 还款成功 */
	public static final String REPAYMENT_SUCCESS = "repayment_success";
	/** 代偿成功 */
	public static final String COMPENSATE_SUCCESS = "compensate_success";
	/** vip会员费 */
	public static final String VIP_FEE = "vip_fee";
	/** 扣款 */
	public static final String ACCOUNT_BACK = "account_back";
	/** 提现解冻 */
	public static final String CASH_FROST = "cash_frost";
	/** 提现成功 */
	public static final String CASH_SUCCESS = "cash_success";
	/** 提现处理中 */
	public static final String CASH_PROCESS = "cash_process";
	/** 提现失败 */
	public static final String CASH_FAIL = "cash_fail";
	/** 取消提现解冻 */
	public static final String CASH_CANCEL = "cash_cancel";
	/** 待收本金 */
	public static final String WAIT_CAPITAL = "wait_capital";
	/** 待收利息 */
	public static final String WAIT_INTEREST = "wait_interest";
	/** 投标奖励 */
	public static final String AWARD_ADD = "award_add";
	/** 扣除投标奖励 */
	public static final String AWARD_DEDUCT = "award_deduct";
	/** 充值收费 */
	public static final String ACCOUNT_RECHARGE_FEE = "account_recharge_fee";
	/** 本金收回 */
	public static final String CAPITAL_COLLECT = "capital_collect";
	/** 利息收回 */
	public static final String INTEREST_COLLECT = "interest_collect";
	/** 逾期还款 */
	public static final String LATE_REPAYMENT = "late_repayment";
	/** 待收奖励 */
	public static final String WAIT_AWARD = "wait_award";
	/** 线下充值奖励费 */
	public static final String OFFRECHARGE_AWARD = "offrecharge_award";
	/** 交易手续费 */
	public static final String TRANSACTION_FEE = "transaction_fee";
	/** 后台扣费冻结 */
	public static final String ACCOUNT_BACK_FREEZE = "account_back_freeze";
	/** 解冻后台扣费冻结 */
	public static final String ACCOUNT_BACK_UNFREEZE = "account_back_unfreeze";
	/** 发标费用冻结 */
	public static final String NEW_BORROW_FEE_FREEZE = "new_borrow_fee_freeze";
	/** 解冻发标冻结费用 */
	public static final String NEW_BORROW_FEE_UNFREEZE = "new_borrow_fee_unfreeze";
	/** 缴纳展期利息 */
	public static final String BORROW_REPAY_EXT_INTEREST = "borrow_repay_ext_interest";
	/** 逾期利息 */
	public static final String LATE_REPAYMENT_INCOME = "late_repayment_income";
	/** 线上充值 **/
	public static final String AL_ONLINE_RECHARGE = "online_recharge";
	/** 线下充值 **/
	public static final String AL_OFF_RECHARGE = "off_recharge";
	/** 后台充值 */
	public static final String AL_BACK_RECHARGE = "back_recharge";
	/** 截标解冻多余费用 */
	public static final String STOP_TENDER_UNFREEZE = "stop_tender_unfreeze";
	/** 后台扣款申请 */
	public static final String BACKSTAGE_BACK_APPLY = "backstage_back_apply";
	/** 后台扣款审核成功 */
	public static final String BACKSTAGE_BACK_SUCCESS = "backstage_back_success";
	/** 后台扣款审核失败 */
	public static final String BACKSTAGE_BACK_FAIL = "backstage_back_fail";
	/** 后台线下充值申请 */
	public static final String BACKSTAGE_RECHARGE_APPLY = "backstage_recharge_apply";
	/** 后台线下充值审核成功 */
	public static final String BACKSTAGE_RECHARGE_SUCCESS = "backstage_recharge_success";
	/** 后台线下充值审核失败 */
	public static final String BACKSTAGE_RECHARGE_FAIL = "backstage_recharge_fail";
	/** 线上充值审核成功 */
	public static final String ONLINE_RECHARGE_SUCCESS = "online_recharge_success";
	/** 线上充值审核失败 */
	public static final String ONLINE_RECHARGE_FAIL = "online_recharge_fail";
	/** 线下充值审核成功 */
	public static final String LINE_RECHARGE_SUCCESS = "line_recharge_success";
	/** 线下充值审核失败 */
	public static final String LINE_RECHARGE_FAIL = "line_recharge_fail";
	/** 申请信用额度 */
	public static final String APPLY_USER_AMOUNT = "apply_user_amount";
	/** 后台审核信用额度 */
	public static final String VERIFY_APPLY_USER_AMOUNT = "verify_apply_user_amount";
	/** vip冻结金额 */
	public static final String VIP_FREEZE = "vip_freeze";
	/** 还款奖励 */
	public static final String REPAYMENT_AWARD = "repayment_award";
	/** 短信费 */
	public static final String SMS_FEE = "sms_fee";
	/** 债权转让出让人归还本金 */
	public static final String BOND_SELL_CAPITAL = "bond_sell_capital";
	/** 债权转让出让人归还利息 */
	public static final String BOND_SELL_INTEREST = "bond_sell_interest";
	/** 债权转让扣除归还利息管理费 */
	public static final String BOND_SELL_INTEREST_MANAGEFEE = "bond_sell_interest_manageFee";
	/** 债权转让出让人归还奖励 */
	public static final String BOND_SELL_AWARD = "bond_sell_award";
	/** 债权转让出让人扣除管理费 */
	public static final String BOND_SELL_MANAGEFEE = "bond_sell_manageFee";
	/** 债权转让出让人扣除待收本金 */
	public static final String BOND_SELL_COLL_CAPITAL = "bond_sell_coll_capital";
	/** 债权转让出让人扣除待收利息 */
	public static final String BOND_SELL_COLL_INTEREST = "bond_sell_coll_interest";
	/** 债权转让出让人扣除待收奖励 */
	public static final String BOND_SELL_COLL_AWARD = "bond_sell_coll_award";
	/** 债权转让受让人归还出让人本金 */
	public static final String BOND_BUY_CAPITAL = "bond_buy_capital";
	/** 债权转让受让人归还出让人利息 */
	public static final String BOND_BUY_INTEREST = "bond_buy_interest";
	/** 债权转让受让人归还出让人奖励 */
	public static final String BOND_BUY_AWARD = "bond_buy_award";
	/** 债权转让受让人待收本金 */
	public static final String BOND_BUY_COLL_CAPITAL = "bond_buy_coll_capital";
	/** 债权转让出让人扣除待收利息 */
	public static final String BOND_BUY_COLL_INTEREST = "bond_buy_coll_interest";
	/** 债权转让出让人扣除待收奖励 */
	public static final String BOND_BUY_COLL_AWARD = "bond_buy_coll_award";
	/** 债权使用红包投资 */
	public static final String BOND_BUY_RED_MONEY = "bond_buy_red_money";
	/** 债权最低转让金额 */
	public static final double BOND_LOWEST_TRANS_MONEY = 100;
	/** 积分兑换 */
	public static String CONVERT_SUCCESS = "convert_success";

	/** 资金合计 */
	public static final String HUIKUAN_INTEREST = "huikuan_interest";
	public static final String HUIKUAN_CAPITAL = "huikuan_capital";
	public static final String DEDUCT_FREEZE = "deduct_freeze";
	public static final String BACK_HUIKUAN_INTEREST = "back_huikuan_interest";

	public static final byte SYSTEM_NOTICE = 1;
	public static final byte USER_NOTICE = 2;

	/** 还款奖励等待 */
	public static final int REPAY_AWARD_STATUS_NORAML = 0;
	/** 还款奖励已收 */
	public static final int REPAY_AWARD_STATUS_PAYED = 1;
	/** 正常还款 */
	public static final int REPAYMENT_TYPE_NORAML = 1;
	/** 代偿还款 */
	public static final int REPAYMENT_TYPE_COMPENSATE = 2;
	/** 获得抵用券 */
	public static final String GET_VOUCHER = "get_voucher";
	/** 使用抵用券 */
	public static final String VOUCHER_EXCHANGE_MONEY = "voucher_exchange_money";
	/** 加息劵待收利息 */
	public static final String WAIT_INTEREST_RATE = "wait_interest_rate";
	/** 加息劵待收利息收回 */
	public static final String INTEREST_RATE_COLLECT = "interest_rate_collect";
	/** 添加推荐人 */
	public static final String ADD_USER_PROMOT = "add_user_promot";
	/** 删除推荐人 */
	public static final String DELETE_USER_PROMOT = "delete_user_promot";
	/** 海外投资产品浮动收益 */
	public static final String FLOAT_INCOME = "float_income";
	/** PPfund资金管理产品投资 */
	public static final String PPFUND_TENDER = "ppfund_tender";
	/** PPfund资金管理产品待收利息 */
	public static final String PPFUND_COLLECTION_INTEREST = "ppfund_collection_interest";
	/** PPfund资金管理产品转出本金 */
	public static final String PPFUND_OUT_CAPITAL = "ppfund_out_capital";
	/** PPfund资金管理产品转出利息 */
	public static final String PPFUND_OUT_INTEREST = "ppfund_out_interest";
	/** PPfund资金管理产品转出风险备用金 */
	public static final String PPFUND_OUT_RISK_RESERVE = "ppfund_out_risk_reserve";
	/** PPfund资金管理产品转出居间服务费 */
	public static final String PPFUND_OUT_MANAGE_FEE = "ppfund_out_manage_fee";
	/** 用户现金红包兑换 */
	public static final String USER_RED_PACKET_EXCHANGE = "user_red_packet_exchange";
	/** 投资成功使用红包 */
	public static final String TENDER_RED_PACKET_SUCCESS = "tender_red_packet_success";
	/** 投资失败退回红包 */
	public static final String TENDER_RED_PACKET_FAIL = "tender_red_packet_fail";
	/** 现金管理产品购买使用红包 */
	public static final String PPFUND_RED_PACKET_SUCCESS = "ppfud_red_packet_success";
	/** PC */
	public static final String COOPERATE_TYPE__PC = "pc";
	/** 微信 */
	public static final String COOPERATE_TYPE__WECHAT = "wechat";
	/** QQ */
	public static final String COOPERATE_TYPE__QQ = "qq";
	/** 微博 */
	public static final String COOPERATE_TYPE__WEIBO = "weibo";
	/** 最近订单号 */
	public static final String SESSION_ORDER = "session_order";
	/** 众筹收益 */
	public static final String REPAID_ZC_PROFIT = "repaid_zc_profit";
	public static final String ZCPROJECTS_TENDER = "zc_projects_tender";
	public static final String YXPROJECTS_TENDER = "yx_projects_tender";
	
	/** 体验金投资 */
	public static final String EXPERIENCE_GOLD_INVEST = "experience_gold_invest";
	/** 体验金收益 */
	public static final String EXPERIENCE_GOLD_INCOME = "experience_gold_income";
	/** 充值类型 */
	public static final String RECHARGE_OPER_TYPE = "recharge_oper_type";
	
	/** 线下充值最低限额KEY */
	public static final String OFFLINE_RECHARGE_MONEY = "OFFLINE_RECHARGE_MONEY";
	/** 线下提现最低限额KEY */
	public static final String OFFLINE_CASH_MONEY = "OFFLINE_CASH_MONEY";
	
	/**365天一年*/
	public static final int YEAR_DAY=365;
	
	/**体验表typeCode*/
	public static final int  EXPERIENCE=202;
	
	/****赎回最高限额******/
	public static final String  REDEEM_MONEY ="REDEEM_MONEY";
	
	/******最低赎回金额*******/
	public static final String REDEEM_MIN_MONEY = "REDEEM_MIN_MONEY";
	
	/**每日提现次数*/
	public static final String CASH_COUNT = "CASH_COUNT";
	

	/**
	 * 公共变量
	 */
	private Constant()
	{

	}

}
