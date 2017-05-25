package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 汇付取现对账
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年1月17日
 */
public class ChinapnrCashReconciliation extends ChinapnrModel {
	private String beginDate;
	private String endDate;
	private String pageNum;
	private String pageSize;
	private String cashReconciliationDtoList; // 取现对账信息字符串

	public ChinapnrCashReconciliation(String beginDate, String endDate,
			String pageNum, String pageSize) {
		super();
		this.setBeginDate(beginDate);
		this.setEndDate(endDate);
		this.setPageNum(pageNum);
		this.setPageSize(pageSize);
		this.setCmdId("CashReconciliation");
	}

	private String[] paramNames = new String[] { "version", "cmdId",
			"merCustId", "beginDate", "endDate", "pageNum", "pageSize",
			"chkValue" };

	public StringBuffer getMerData() throws UnsupportedEncodingException {

		StringBuffer MerData = super.getMerData();
		MerData.append(getBeginDate()).append(getEndDate())
				.append(getPageNum()).append(getPageSize());
		return MerData;
	}

	@SuppressWarnings("unused")
	@Override
	public ChinapnrModel response(String res) throws IOException {
		super.response(res);
		try {
			JSONObject json = JSON.parseObject(res);
		} catch (Exception e) {

		}
		return null;
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

	public String getCashReconciliationDtoList() {
		return cashReconciliationDtoList;
	}

	public void setCashReconciliationDtoList(String cashReconciliationDtoList) {
		this.cashReconciliationDtoList = cashReconciliationDtoList;
	}
}
