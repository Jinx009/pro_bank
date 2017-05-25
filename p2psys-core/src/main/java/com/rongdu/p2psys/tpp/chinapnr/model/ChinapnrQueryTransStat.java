package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.common.util.StringUtil;

/**
 * 交易状态查询
 * @author yinliang
 * @version 2.0
 * @Date   2015年1月17日
 */
public class ChinapnrQueryTransStat extends ChinapnrModel {
	private String ordId;
	private String queryTransType; // 交易类型：LOANS，交易放款；REPAYMENT，还款交易
	private String transStat;
	private String address = "/muser/publicRequests";
	private String testAddress = "/muser/publicRequests";

	private String[] paramNames = new String[] { "version", "cmdId",
			"merCustId", "ordId", "ordDate", "queryTransType", "chkValue" };

	public ChinapnrQueryTransStat() {
		super();

	}

	public ChinapnrQueryTransStat(String ordId, String ordDate,
			String querytransType) {
		this.setOrdId(ordId);
		this.setOrdDate(ordDate);
		this.setQueryTransType(querytransType);
		this.setCmdId("QueryTransStat");
	}

	public StringBuffer getMerData() {

		StringBuffer MerData = new StringBuffer();
		MerData.append(StringUtil.isNull(getVersion()))
				.append(StringUtil.isNull(getCmdId()))
				.append(StringUtil.isNull(getMerCustId()))
				.append(StringUtil.isNull(getOrdId())).append(getOrdDate())
				.append(getQueryTransType());
		return MerData;
	}

	@Override
	public ChinapnrQueryTransStat response(String res) throws IOException {
		super.response(res);
		try {
			JSONObject json = JSON.parseObject(res);
			queryTransType = json.getString("queryTransType");
			transStat = json.getString("TransStat");
			this.setOrdDate(json.getString("OrdDate"));
			this.setOrdId(json.getString("OrdId"));
		} catch (Exception e) {

		}
		return null;
	}

	public String getOrdId() {
		return ordId;
	}

	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}

	public String getQueryTransType() {
		return queryTransType;
	}

	public void setQueryTransType(String queryTransType) {
		this.queryTransType = queryTransType;
	}

	public String getTransStat() {
		return transStat;
	}

	public void setTransStat(String transStat) {
		this.transStat = transStat;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTestAddress() {
		return testAddress;
	}

	public void setTestAddress(String testAddress) {
		this.testAddress = testAddress;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

}
