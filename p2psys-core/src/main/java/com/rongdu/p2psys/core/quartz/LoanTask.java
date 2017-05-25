package com.rongdu.p2psys.core.quartz;

public interface LoanTask {

	/**
	 * 父类使用
	 */
	static String LOAN_STATUS = "LOAN_STATUS";
	/**
	 * 发送消息使用
	 */
	static String MESSAGE_STATUS = "MESSAGE_STATUS";
	/**
	 * 投资使用
	 */
	static String TENDER_STATUS = "TENDER_STATUS";
	/**
	 * 标使用
	 */
	static String BORROW_STATUS = "BORROW_STATUS";
	/**
	 * 流转标还款使用
	 */
	static String FLOW_REPAY_STATUS = "FLOW_REPAY_STATUS";
	/**
	 * 充值、取现使用
	 */
	static String CASH_STATUS = "CASH_STATUS";
	/**
	 * 审核标的业务使用
	 */
	static String VERIFY_BORROW_STATUS = "VERIFY_BORROW_STATUS";
	/**
	 * 用户相关业务使用：开户，绑定银行卡等
	 */
	static String USER_STATUS = "USER_STATUS";
	/**
	 * 债权队列
	 */
	static String BOND_STATUS = "BOND_STATUS";
	/**
	 * PPfund资金管理产品队列
	 */
	static String PPFUND_STATUS = "PPFUND_STATUS";
	/**
	 * 其他业务使用
	 */
	static String OTHER_STATUS = "OTHER_STATUS";

	void execute();

	void doLoan();

	void stop();

	Object getLock();

}
