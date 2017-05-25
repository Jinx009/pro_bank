package com.rongdu.p2psys.tpp.chinapnr.tool;

/**
 * 汇付后台任务计划表对应的类型。
 * 
 * @author Administrator
 * 
 */
public class ChinaPnrType {
	// 汇付交易类型：
	/**
	 * 冻结
	 */
	public static String UsrFreezeBg = "UsrFreezeBg";
	/**
	 * 解冻
	 */
	public static String UsrUnFreeze = "UsrUnFreeze";
	/**
	 * 取现
	 */
	public static String CashOut = "CashOut";
	/**
	 * 债权转让接口
	 */
	public static String CREDITASSIGN = "CREDITASSIGN";
	/**
	 * 系统账户 用于系统转账。
	 */
	public static long ADMINOUT = 1;
	/**
	 * 系统账户 用于给系统转账。
	 */
	public static String ADMININ = "1";
	/**
	 * 从系统账号给用户划账
	 */
	public static String TRANSFER = "TRANSFER";
	// 系统操作类型
	/**
	 * 发标
	 */
	public static String ADDBORROW = "ADDBORROW";
	/**
	 * 投标
	 */
	public static String ADDTENDER = "ADDTENDER";
	/**
	 * 申请vip
	 */
	public static String APPLYVIP = "APPLYVIP";
	/**
	 * 还款
	 */
	public static String DOREPAY = "DOREPAY";
	/**
	 * 提前还款
	 */
	public static String PREREPAY = "PREREPAY";
	/**
	 * 自动还款
	 */
	public static String AUTOREPAY = "AUTOREPAY";
	/**
	 * 后台满标复审通过
	 */
	public static String AUTOVERIFYFULLSUCCESS = "AUTOVERIFYFULLSUCCESS";
	/**
	 * 后台满标复审通过放款
	 */
	public static String VERIFYLOANS = "VERIFYLOANS";
	/**
	 * 投标奖励
	 */
	public static String AUTOREWARD = "AUTOREWARD";
	/**
	 * 流转标还款
	 */
	public static String AUTOFLOWREPAY = "AUTOFLOWREPAY";
	/**
	 * 流转标放款
	 */
	public static String AUTOFLOWLOANS = "AUTOFLOWLOANS";
	/**
	 * 满标初审
	 */
	public static String VERIFYBORROW = "VERIFYBORROW";
	/**
	 * 后台满标复审不通过
	 */
	public static String AUTOVERIFYFULLFAIL = "AUTOVERIFYFULLFAIL";
	/**
	 * 后台发标审不通过
	 */
	public static String ADDVERIFAIL = "ADDVERIFAIL";
	/**
	 * 取消的标
	 */
	public static String AUTOCANCEL = "AUTOCANCEL";
	/**
	 * 秒标冻结
	 */
	public static String AUTODOREPAYFORSECOND = "AUTODOREPAYFORSECOND";
	/**
	 * 秒标冻结
	 */
	public static String SECONDUNUNFREEZE = "SECONDUNUNFREEZE";
	/**
	 * 前台满标处理
	 */
	public static String VERIFYFULLBORROW = "VERIFYFULLBORROW";
	/**
	 * 扣款审核
	 */
	public static String VERIFYRECHARGE = "VERIFYRECHARGE";
	/**
	 * vip 审核通过
	 */
	public static String VERIFYVIPSUCCESS = "VERIFYVIPSUCCESS";
	/**
	 * vip审核不通过
	 */
	public static String VERIFYVIPFAIL = "VERIFYVIPFAIL";
	/**
	 * 发标
	 */
	public static String WEBSITEPAYFORLATEBORROW = "WEBSITEPAYFORLATEBORROW";
	/**
	 * 提现申请
	 */
	public static String NEWCASH = "NEWCASH";
	/**
	 * 提现审核
	 */
	public static String VERIFYCASH = "VERIFYCASH";
	/**
	 * 扣款
	 */
	public static String CASHBACK = "CASHBACK";
	/**
	 * 取消提现
	 */
	public static String CANCELCASH = "CANCELCASH";
	/**
	 * 放款接口
	 */
	public static String LOANS = "LOANS";
	/**
	 * 债权转让接口
	 */
	public static String CREDITASSIGN_VERIFY = "CREDITASSIGN_VERIFY";
	/**
	 * 还款接口
	 */
	public static String REPAYMENT = "REPAYMENT";
	/**
	 * 借款管理费
	 */
	public static String BORROWFREE = "BORROWFREE";
	/**
	 * 风险准备金
	 */
	public static String RISKMONEY = "RISKMONEY";
	/**
	 * 借款手续费
	 */
	public static String BORROWCHARGE = "BORROWCHARGE";
	/**
	 * 还款保证金
	 */
	public static String REPAYBONDMONEY = "REPAYBONDMONEY";
	/**
	 * 网站垫付，用户还款
	 */
	public static String WEBSITEREPAY = "WEBSITEREPAY";
	/**
	 * 网站垫付
	 */
	public static String WEBSITE = "WEBSITE";
	/**
	 * 网站垫付
	 */
	public static String WEBPAY = "WEBPAY";
	/**
	 * 撤标
	 */
	public static String FAILL_BORROW = "FAILL_BORROW";
	/**
	 * 流转标还款
	 */
	public static String FLOW_REPAY = "FLOW_REPAY";

	// 金账户操作类型
	/**
	 * 充值
	 */
	public static String RECHARGE = "RECHARGE";
	/**
	 * 提现
	 */
	public static String CASH = "CASH";
	/**
	 * 金账户充值
	 */
	public static String WEBRECHARGE = "WEBRECHARGE";

	/**
	 * 担保公司
	 */
	public static String GUARANTEE_CORP = "Y";
	/**
	 * 借款人
	 */
	public static String BORROWER_CORP = "N";// 撤标
	/**
	 * 企业开户
	 */
	public static String CORP = "CORP";
	/**
	 * 个人开户
	 */
	public static String PERSONAL = "PERSONAL";
}
