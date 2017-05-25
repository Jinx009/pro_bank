package com.rongdu.p2psys.core.constant;

/**
 * 产品状态
 */
public class ProductStatusConstant {

	/**
	 * 0:等待初审
	 */
	public static final int STATUS_WAITING_FOR_APPROVE = 0;

	/**
	 * 1:初审通过
	 */
	public static final int STATUS_APPROVED = 1;

	/**
	 * 2:初审不通过
	 */
	public static final int STATUS_FAIL = 2;

	/**
	 * 3:满标复审通过
	 */
	public static final int STATUS_RECHECK_PASS = 3;

	/**
	 * 49:满标复审不通过
	 */
	public static final int STATUS_RECHECK_FAIL = 49;

	/**
	 * 59:后台管理员撤回
	 */
	public static final int STATUS_AUTO_CANCEL = 59;

	/**
	 * 6:还款中
	 */
	public static final int STATUS_REPAYMENT_START = 6;

	/**
	 * 99:无关状态
	 */
	public static final int STATUS_UNRELATED = 99;

	// 描述

	public static final String DESC_WAITING_FOR_APPROVE = "等待初审";

	public static final String DESC_APPROVED = "初审通过";

	public static final String DESC_FAIL = "初审不通过";

	public static final String DESC_RECHECK_PASS = "满标复审通过";

	public static final String DESC_RECHECK_FAIL = "满标复审不通过";

	public static final String DESC_AUTO_CANCEL = "后台管理员撤回";

	public static final String DESC_REPAYMENT_START = "还款中";

	public static final String DESC_UNRELATED = "未知状态";

}
