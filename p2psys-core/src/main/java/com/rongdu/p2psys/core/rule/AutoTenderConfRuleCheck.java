package com.rongdu.p2psys.core.rule;

import java.util.List;

/**
 * 自动投标配置/投标前验证
 * 
 * @author cx
 * @version 2.0
 * @since 2014-02-20
 */
public class AutoTenderConfRuleCheck extends RuleCheck {

	public List<Integer> enable_auto_tender;

	/** 投标前验证 **/
	public TenderValid tender_valid;

	public static class TenderValid {
		/** 1 启用验证 **/
		public int status;
		public int real_enable;
		public int email_enable;
		public int phone_enable;
	}

	/** 投标次数限制 **/
	public TenderLimit tender_limit;

	public static class TenderLimit {
		/** 1启用限制 **/
		public int status;
	}

	@Override
	public boolean checkRule() {
		return false;
	}

}
