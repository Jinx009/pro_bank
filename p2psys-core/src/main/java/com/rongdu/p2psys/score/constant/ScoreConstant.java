package com.rongdu.p2psys.score.constant;

/**
 * 积分通用常量类
 */
public class ScoreConstant {

	/**
	 * 积分兑换规则停用
	 */
	public static final byte STATUS_NO = 0; 
	
	/**
	 * 积分兑换规则启用
	 */
	public static final byte STATUS_YES = 1;
	
	/**
	 * 积分兑换约束停用
	 */
	public static final byte CHECK_NO = 0; 
	
	/**
	 * 积分兑换约束启用
	 */
	public static final byte check_yes = 1;
	
	
	/**
	 * 0未审核
	 */
	public static final byte WAIT_AUDIT = 0; 
	
	/**
	 * 1审核通过
	 */
	public static final byte PASS_AUDIT = 1;
	
	/**
	 * 2审核不通过
	 */
	public static final byte NOT_PASS_AUDIT = 2;
	
	/**
	 * -1无用数据
	 */
	public static final byte FAIL_AUDIT = -1;
	
}
