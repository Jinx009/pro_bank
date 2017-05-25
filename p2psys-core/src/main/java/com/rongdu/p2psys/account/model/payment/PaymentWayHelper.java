package com.rongdu.p2psys.account.model.payment;

/**
 * 充值
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月7日
 */
public class PaymentWayHelper {

	/**
	 * 根据类名获取处理类
	 * @param className 第三方支付类名
	 * @return PaymentWay
	 * @throws Exception 异常
	 */
	public static PaymentWay getPaymentWay(String className) throws Exception {
		Class<?> clazz = null;
		clazz = Class.forName(className);
		return (PaymentWay) clazz.newInstance();
	}

}
