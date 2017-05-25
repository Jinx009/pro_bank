package com.rongdu.p2psys.account.model.payment;

import com.rongdu.p2psys.account.domain.AccountRecharge;

public interface Payment {

	/**
	 * 支付名称
	 * @return 支付名称
	 */
	String payname();

	/**
	 * @return
	 */
	String orderPrefix();

	/**
	 * @param sign
	 * @return
	 */
	String encrypt(String sign);

	/**
	 * 
	 */
	void sign();

	/**
	 * @return
	 */
	String encryptSign();

	/**
	 * @return
	 */
	String localReturnSign();

	/**
	 * @return
	 */
	String encryptLocalReturnSign();

	/**
	 * @return
	 */
	String remoteReturnSin();

	/**
	 * @return
	 */
	Object protype();

	/**
	 * @return
	 */
	String tranNo();

	/**
	 * @param recharge
	 */
	void init(AccountRecharge recharge);

	/**
	 * @return
	 */
	boolean reponseSuccess();

}
