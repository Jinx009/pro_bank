package com.rongdu.p2psys.account.model;

/**
 * 提现批量处理
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月18日
 */
public class AccountCashBatchModel {
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 订单号
	 */
	private String orderNo;
	/**
	 * 消息
	 */
	private String message;
	
	public AccountCashBatchModel() {
		super();
	}
	
	public AccountCashBatchModel(String userName, String orderNo, String message) {
		super();
		this.userName = userName;
		this.orderNo = orderNo;
		this.message = message;
	}


	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
