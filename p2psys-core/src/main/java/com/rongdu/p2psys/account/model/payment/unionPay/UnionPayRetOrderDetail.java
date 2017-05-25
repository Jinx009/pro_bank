package com.rongdu.p2psys.account.model.payment.unionPay;

/**
 * 订单查询订单详情
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月15日
 */
public class UnionPayRetOrderDetail {
	/**
	 * 订单查询状态
	 */
	private String retCode;
	
	/**
	 * 订单状态描述
	 */
	private String retDesc;
	
	/**
	 * 订单系统处理日期
	 */
	private String processDate;
	
	/**
	 * 订单处理状态 0:已接受,1:处理中,2:处理成功,3:处理失败
	 */
	private int orderStatus;
	
	/**
	 * 订单备注
	 */
	private String remark;

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetDesc() {
		return retDesc;
	}

	public void setRetDesc(String retDesc) {
		this.retDesc = retDesc;
	}

	public String getProcessDate() {
		return processDate;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
