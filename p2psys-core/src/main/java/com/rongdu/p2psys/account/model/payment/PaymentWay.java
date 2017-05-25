package com.rongdu.p2psys.account.model.payment;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.AccountRechargeModel;

/**
 * 充值--支付接口
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月7日
 */
public interface PaymentWay {
	
	/**
	 * 支付请求
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */	
	BasePayment paymentRequest(HttpServletRequest request, AccountRechargeModel model) throws Exception;
	
	/**
	 * 支付请求具体实现
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	BasePayment payment(HttpServletRequest request) throws Exception;	
	
	/**
	 * 支付回调
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	void callbackRequest(HttpServletRequest request) throws Exception;
	
	/**
	 * 支付回调具体实现
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	BasePayment callback(HttpServletRequest request) throws Exception;	
	
	/**
	 * 获取充值对象
	 * 
	 * @return
	 */
	AccountRecharge getRecharge();

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	String responseSuccess() throws Exception;
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	String responseFail() throws Exception;
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	boolean isSuccess() throws Exception;

}
