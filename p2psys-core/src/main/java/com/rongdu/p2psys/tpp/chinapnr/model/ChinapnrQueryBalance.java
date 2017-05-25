package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.common.util.StringUtil;

/**
 * 汇付查询用户资金
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年1月17日
 */
public class ChinapnrQueryBalance extends ChinapnrModel {
	/**
	 * 查询余额接口 后台方式
	 * */
	private String usrId;
	private String acctType;

	private String address = "/muser/publicRequests";
	private String testAddress = "/muser/publicRequests";

	private String[] paramNames = new String[] { "version", "cmdId","merCustId", "usrCustId", "chkValue" };

	public ChinapnrQueryBalance() {
		super();
		this.setCmdId("QueryBalanceBg");
	}

	public ChinapnrQueryBalance(long usrId) {
		this();
		this.setUsrCustId(getMerCustId() + usrId);
	}

	public ChinapnrQueryBalance(String usrCustId) {
		this();
		this.setUsrCustId(usrCustId);
	}

	public StringBuffer getMerData() {

		StringBuffer MerData = new StringBuffer();
		MerData.append(StringUtil.isNull(getVersion()))
				.append(StringUtil.isNull(getCmdId()))
				.append(StringUtil.isNull(getMerCustId()))
				.append(StringUtil.isNull(getUsrCustId()));
		return MerData;
	}

	@Override
	public ChinapnrModel response(String res) throws IOException {

		try {
			JSONObject json = JSON.parseObject(res);
			this.setRespCode(json.getString("RespCode"));
			this.setRespDesc(json.getString("RespDesc"));
			this.setAvlBal(json.getString("AvlBal"));
			this.setFrzBal(json.getString("FrzBal"));
			this.setAcctBal(json.getString("AcctBal"));
			this.setUsrCustId(json.getString("UsrCustId"));
		} catch (Exception e) {

		}
		return null;
	}

	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
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
