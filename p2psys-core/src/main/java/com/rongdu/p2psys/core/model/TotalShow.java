package com.rongdu.p2psys.core.model;

/**
 * 用来统计资金情况的model
 * @author sj
 *
 */
public class TotalShow {

	private String userName;
	
	private String typeName;
	
	private String amountIn;
	
	private String fee;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getAmountIn() {
		return amountIn;
	}
	public void setAmountIn(String amountIn) {
		this.amountIn = amountIn;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	
}
