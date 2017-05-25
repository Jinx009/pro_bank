package com.rongdu.p2psys.tpp.ips.model;
/**
 * 
 * 环讯开户
 * 
 * @author lx
 * @version 2.0
 * @since 2014年8月13日
 */
public class IpsRechargeBank {
	/**
	 * 环讯充值的银行名称
	 */
	private String  bankName;  
	/**
	 * 环讯充值的银行编号	 
	 */
	private String bankCode;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}  
	
	
	
}
