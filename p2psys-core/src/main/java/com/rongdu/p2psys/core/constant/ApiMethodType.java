package com.rongdu.p2psys.core.constant;
/**
 * 此类中只定义，autoServiceImpl,borrowServiceImpl中使用的参数， 在 apiServiceImpl 中判断对应的方法是否调用。
 * @author zxc
 * 1:汇付，2：易极付，...
 * 格式如下："1"触发汇付，"2"触发易极付，"1,2"触发汇付和易极付。
 */
public class ApiMethodType {

	/**
	 * 放款，调用1类型。只有易极付接口触发
	 */
	public static String FULL_SUCCESS_A="2"; 
	/**
	 * 放款，调用2类型。只有汇付接口触发。
	 */
	public static String FULL_SUCCESS_B="1"; 
	/**
	 * 放款，调用2,3类型。易极付、双乾接口触发
	 */
	public static String FULL_SUCCESS_C="2,3";
	/**
	 * 还款给网站，易极付等接口专用。
	 */
	public static String REPAY_TO_WEB_SITE_A="2";
	/**
	 * 网站垫付，易极付等接口专用。
	 */
	public static String WEB_SITE_PAY="2";
	/**
	 * 好利贷网站垫付，易极付等接口专用。
	 */
	public static String HAOLIDAI_WEB_SITE_PAY="2";
	
	/**
	 * 投标，调用类型1，只触发汇付冻结接口
	 */
	public static String BORROW_TENDER_A = "1";
	
	/**
	 * 投标，调用类型2，只触发易极付冻结接口
	 */
	public static String BORROW_TENDER_B = "2";
	/**
	 * 投标，调用类型3，触发易极付、双乾冻结接口
	 */
	public static String BORROW_TENDER_C = "2,3";
	/**
	 * 还款，调用类型1，只触发汇付接口
	 */
	public static String BORROW_REPAY_A = "1";
	/**
	 * 还款，调用类型2，只触发易极付接口
	 */
	public static String BORROW_REPAY_B = "2";
	/**
	 * 还款，调用类型3，触发易极付、汇付接口
	 */
	public static String BORROW_REPAY_C = "1,2";
	/**
	 * 还款，调用类型3，触发易极付、汇付接口、双乾接口
	 */
	public static String BORROW_REPAY_D = "1,2,3";
	/**
	 * 流转标还款，调用类型2，只触发易极付接口
	 */
	public static String FLOW_REPAY = "2";
	
	/**
	 * 撤标，调用汇付操作
	 */
	public static String FAILL_BORROW_A = "1";
	/**
	 * 撤标调用易极付操作
	 */
	public static String FAILL_BORROW_B = "1";
	/**
	 *撤标都调用 汇付、易极付
	 */
	public static String FAILL_BORROW_C = "1,2";
	/**
	 *撤标都调用 
	 */
	public static String FAILL_BORROW_D = "1,2,3";
	
}
