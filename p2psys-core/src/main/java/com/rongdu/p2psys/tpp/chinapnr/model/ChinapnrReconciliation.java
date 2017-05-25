package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.UnsupportedEncodingException;

/**
 * 放还款对账（投标对账）
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年1月17日
 */
public class ChinapnrReconciliation extends ChinapnrModel {
	private String beginDate;
	private String endDate;
	private String pageNum;
	private String pageSize;
	private String queryTransType; // 交易查询类型,REPAYMENT

	public ChinapnrReconciliation(String beginDate, String endDate,
			String pageNum, String pageSize, String queryTransType) {
		super();
		this.setCmdId("Reconciliation");
		this.setBeginDate(beginDate);
		this.setEndDate(endDate);
		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
		this.setQueryTransType(queryTransType);
	}

	private String[] paramNames = new String[] { "version", "cmdId",
			"merCustId", "beginDate", "endDate", "pageNum", "pageSize",
			"queryTransType", "chkValue" };

	public StringBuffer getMerData() throws UnsupportedEncodingException {

		StringBuffer MerData = super.getMerData();
		MerData.append(getBeginDate()).append(getEndDate())
				.append(getPageNum()).append(getPageSize())
				.append(getQueryTransType());
		return MerData;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getQueryTransType() {
		return queryTransType;
	}

	public void setQueryTransType(String queryTransType) {
		this.queryTransType = queryTransType;
	}
}
