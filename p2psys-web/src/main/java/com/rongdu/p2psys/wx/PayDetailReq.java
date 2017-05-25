package com.rongdu.p2psys.wx;

public class PayDetailReq {

	// 产品id
	private long projectId;
	// 产品类型
	private String projectType;
	// 产品名称
	private String projectName = "";
	// 购买金额
	private String investPrice = "";
	// 项目描述
	private String projectDesc = "";
	// 回调地址
	private String successCallback = "";
	private String failedCallback = "";
	// 购买地址
	private String submitUrl = "";
	// ppfund转出时间
	private String outTime="";
	// 图片地址
	private String imageUrl = "";

	
	// ppfund borrow
	private String paramJson="";
	private String password="";
	private String pwd="";
	private String id;
	private String payPwd="";
	
	
	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * PPFUND
	 */
	private double money;

	public String getParamJson() {
		return paramJson;
	}

	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getInvestPrice() {
		return investPrice;
	}

	public void setInvestPrice(String investPrice) {
		this.investPrice = investPrice;
	}

	public String getProjectDesc() {
		return projectDesc;
	}

	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}

	public String getSuccessCallback() {
		return successCallback;
	}

	public void setSuccessCallback(String successCallback) {
		this.successCallback = successCallback;
	}

	public String getFailedCallback() {
		return failedCallback;
	}

	public void setFailedCallback(String failedCallback) {
		this.failedCallback = failedCallback;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getSubmitUrl() {
		return submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	
	

}
