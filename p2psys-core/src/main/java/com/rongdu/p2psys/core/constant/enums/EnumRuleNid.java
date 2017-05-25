package com.rongdu.p2psys.core.constant.enums;

/**
 * 规则类型
 */
public enum EnumRuleNid {
	/**
	 * 自动投标配置,投标前验证
	 */
	AUTO_TENDER_CONF("autoTenderConf"),
	/**
	 * 首页规则
	 */
	INDEX("index"),
	/**
	 * 投标利率限制
	 */
	BORROW_APR_LIMIT("borrowAprLimit"),

	/**
	 * 回款配置
	 */
	HUIKUAN_CONF("huikuan_conf"),

	/**
	 * 投标前验证
	 */
	TENDER_BEFORE_VALID("tender_before_valid"),

	/**
	 * 短信：快线400
	 */
	KUAIXIAN_SMS("kuaixian_sms"),
	/**
	 * 短信：互亿无线
	 */
	YWD_SMS("ywd_sms"),
	/**
	 * 短信：中国短信网
	 */
	CHINA_SMS("china_sms"), 
	
	/**
	 * 短信：融都短信
	 */
	ERONGDU_SMS("erongdu_sms"), 
	
	/**
	 * 短信：
	 */
	YND_SMS("ynd_sms"), 
	
	YUN_SMS("yun_sms"),
	
	/**
	 * 短信：大汉三通
	 */
	SANTONG_SMS("santong_sms");
	
	/**
	 * 规则
	 */
	private String value;

	/**
	 * 设置规则
	 * 
	 * @param value 规则
	 */
	EnumRuleNid(String value) {
		this.value = value;
	}

	/**
	 * 获取规则
	 * 
	 * @return 规则
	 */
	public String getValue() {
		return this.value;
	}
}
