package com.rongdu.p2psys.core.constant;

/**
 * 产品类型
 */
public class ProductTypeConstant {

	/**
	 * 0:未启用类型
	 */
	public static final int ENABLE_FALSE = 0;

	/**
	 * 1:已启用类型
	 */
	public static final int ENABLE_TRUE = 1;

	/**
	 * 体验标
	 */
	public static final String PRODUCT_CATEGORY__EXPERIENCE = "新手专享";

	/**
	 * 现金类产品
	 */
	public static final String PRODUCT_CATEGORY__PPFUND = "现金类产品";

	/**
	 * 非现金类产品
	 */
	public static final String PRODUCT_CATEGORY__BORROW = "非现金类产品";

	/**
	 * 组合产品
	 */
	public static final String PRODUCT_CATEGORY__SET = "组合产品";

	/**
	 * 众筹产品
	 */
	public static final String PRODUCT_CATEGORY__CROWDFUNDING = "众筹产品";

	/**
	 * 五星专享
	 */
	public static final String PRODUCT_CATEGORY__FIVESTAR = "五星专享";

	/**
	 * VIP
	 */
	public static final String PRODUCT_CATEGORY__VIP = "VIP";

	/********** 类型 **********/

	/**
	 * 随存随取
	 */
	public static final long PRODUCT_TYPE__CASH = 101;

	/**
	 * 优选基金
	 */
	public static final long PRODUCT_TYPE__FUNDS = 102;

	/**
	 * 固定收益
	 */
	public static final long PRODUCT_TYPE__FIX = 103;

	/**
	 * 海外投资
	 */
	public static final long PRODUCT_TYPE__OVERSEA = 119;

	/**
	 * 浮动收益
	 */
	public static final long PRODUCT_TYPE__FLOAT = 122;

	/**
	 * 体验标
	 */
	public static final long PRODUCT_TYPE__EXPERIENCE = 202;

	/********** 众筹类型 **********/

	/**
	 * 实物众筹
	 */
	public static final long PRODUCT_TYPE__CROWDFUNDING_GOODS = 301;

	/**
	 * 权益众筹
	 */
	public static final long PRODUCT_TYPE__CROWDFUNDING_RIGHTS = 302;

	/********** 组合类型 **********/

	/**
	 * 保守组合
	 */
	public static final long SET_TYPE__CONSERVATIVE = 801;

	/**
	 * 稳健组合
	 */
	public static final long SET_TYPE__STEADY = 802;
	
	/**
	 * 现金类产品单日投资限额
	 */
	public static final double PRODUCT_CATEGORY__PPFUND_LIMIT = 200000;
}
