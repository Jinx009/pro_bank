package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;

public class ChinapnrTransfer extends ChinapnrModel {
	/**
	 * 商户专用划账接口，只能从商户账号划账到用户账号
	 * */
	private String outCustId; // 出账账户
	private String inCustId; // 入账账号

	private String address = "/muser/publicRequests";
	private String testAddress = "/muser/publicRequests";

	private boolean isWeb;

	public ChinapnrTransfer(String transAmt, String inCustId, String ordId,
			String outAcctId, String inAcctId) {
		super();
		this.setCmdId("Transfer");
		if (StringUtil.isBlank(inAcctId) || StringUtil.isBlank(outAcctId)) {//线下充值
			this.isWeb = true;
			if(StringUtil.isBlank(outAcctId)){
				this.setOutAcctId(this.getMerAcctId());
			}else{
				this.setOutAcctId(outAcctId);
			}
			this.paramNames = new String[] { "version", "cmdId", "ordId",
					"outCustId", "outAcctId", "transAmt", "inCustId",
					"bgRetUrl", "chkValue" };
		} else {//账户间转账
			this.setInAcctId(inAcctId);
			this.setOutAcctId(outAcctId);
			this.paramNames = new String[] { "version", "cmdId", "ordId",
					"outCustId", "outAcctId", "transAmt", "inCustId",
					"inAcctId", "bgRetUrl", "chkValue" };
		}
		this.setOutCustId(getMerCustId());
		this.setTransAmt(transAmt);
		this.setInCustId(inCustId);
		this.setOrdId(ordId);
		this.setBgRetUrl(Global.getValue("weburl")
				+ "/public/chinapnr/chinapnrBgRet.html");

	}

	private String[] paramNames;

	public StringBuffer getMerData() throws UnsupportedEncodingException {
		StringBuffer MerData;
		if (this.isWeb == false) {//线下充值
			MerData = new StringBuffer();
			MerData.append(getVersion()).append(getCmdId()).append(getOrdId())
					.append(getOutCustId()).append(getOutAcctId())
					.append(getTransAmt()).append(getInCustId())
					.append(getInAcctId()).append(getBgRetUrl());
		} else {//账户间转账
			MerData = new StringBuffer();
			MerData.append(getVersion()).append(getCmdId()).append(getOrdId())
					.append(getOutCustId()).append(getOutAcctId())
					.append(getTransAmt()).append(getInCustId())
					.append(getBgRetUrl());
		}

		return MerData;
	}

	@Override
	public ChinapnrModel response(String res) throws IOException {
		super.response(res);
		try {
			JSONObject json = JSON.parseObject(res);
			this.setTransAmt(json.getString("TransAmt"));
			this.setOrdId(json.getString("OrdId"));
			this.setOutCustId(json.getString("OutCustId"));
			this.setInAcctId(json.getString("InAcctId"));
			this.setOutAcctId(json.getString("OutAcctId"));
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	public boolean isWeb() {
		return isWeb;
	}

	public void setWeb(boolean isWeb) {
		this.isWeb = isWeb;
	}

	public String getOutCustId() {
		return outCustId;
	}

	public void setOutCustId(String outCustId) {
		this.outCustId = outCustId;
	}

	public String getInCustId() {
		return inCustId;
	}

	public void setInCustId(String inCustId) {
		this.inCustId = inCustId;
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
