package com.rongdu.p2psys.core.rule;

/**
 * RSA表单提交加密
 * 
 * @author cx
 * @version 2.0
 * @since 2014-02-18
 */
public class RsaFormEncryptRuleCheck extends RuleCheck {

	/** 是否启用表单加密 */
	public boolean enable_formEncrypt = false;

	@Override
	public boolean checkRule() {
		return false;
	}

	public boolean isEnable_formEncrypt() {
		return enable_formEncrypt;
	}

	public void setEnable_formEncrypt(boolean enable_formEncrypt) {
		this.enable_formEncrypt = enable_formEncrypt;
	}

}
