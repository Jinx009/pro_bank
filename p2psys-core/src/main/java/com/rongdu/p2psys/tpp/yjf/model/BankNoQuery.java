package com.rongdu.p2psys.tpp.yjf.model;


/**
 * 线上充值 包含信息
 * @author Administrator
 *
 */
public class BankNoQuery extends PayModel {
	
	private String districtName;
	private String  bankId;
	
	private String[] paramNames = new String[]{"service","partnerId",
			         "signType","sign","orderNo","districtName","bankId"};
	
	private String bankLasalle;
	private String branchName;
	public BankNoQuery(){
		super();
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankLasalle() {
		return bankLasalle;
	}

	public void setBankLasalle(String bankLasalle) {
		this.bankLasalle = bankLasalle;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

}
