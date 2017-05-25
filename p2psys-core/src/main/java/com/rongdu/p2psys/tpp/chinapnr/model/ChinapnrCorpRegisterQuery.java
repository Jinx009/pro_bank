package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.IOException;
import java.net.URLDecoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.common.util.StringUtil;

public class ChinapnrCorpRegisterQuery extends ChinapnrModel {

	private String busiCode;

	private String[] paramNames = new String[] { "version", "cmdId", "merCustId", "busiCode", "chkValue" };

	// 返回参数
	private String auditStat;
	private String usrId;

	public ChinapnrCorpRegisterQuery() {
		super();
		this.setCmdId("CorpRegisterQuery");
	}

	public StringBuffer getMerData() {
		StringBuffer MerData = new StringBuffer();
		MerData.append(StringUtil.isNull(getVersion()))
				.append(StringUtil.isNull(getCmdId()))
				.append(StringUtil.isNull(getMerCustId()))
				.append(StringUtil.isNull(getBusiCode()));
		return MerData;
	}

	@Override
	public ChinapnrModel response(String res) throws IOException {
		try {
			JSONObject json = JSON.parseObject(res);
			this.setRespCode(json.getString("CmdId"));
			this.setRespDesc(StringUtil.isNull(URLDecoder.decode(
					StringUtil.isNull(json.getString("RespDesc")), "utf-8")));
			this.setAvlBal(json.getString("MerCustId"));
			this.setFrzBal(json.getString("UsrCustId"));
			this.setUsrId(json.getString("UsrId"));
			this.setAuditStat(json.getString("AuditStat"));
			this.setBusiCode(json.getString("BusiCode"));
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

	public String getBusiCode() {
		return busiCode;
	}

	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}

	public String getAuditStat() {
		return auditStat;
	}

	public void setAuditStat(String auditStat) {
		this.auditStat = auditStat;
	}

	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

}
