package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.tpp.TPPWay;

/**
 * 后台解冻冻结资金接口
 * */
public class ChinapnrUsrUnFreeze extends ChinapnrModel {

	private String usrId;
	private String trxId;       //日期加流水账户
	private String address = "/muser/publicRequests";
	private String testAddress = "/muser/publicRequests";

	private String[] paramNames = new String[] {"version","cmdId","merCustId",
			"ordId","ordDate","trxId","retUrl","bgRetUrl","chkValue"};
	
	public ChinapnrUsrUnFreeze() {
		this("","");
	}
	
	public ChinapnrUsrUnFreeze(String usrId,String transAmt) {
		this(usrId,transAmt,"","");
	}
	
	public ChinapnrUsrUnFreeze(String usrId,String date,String ordId,String trxId) {
		super();
		this.setOrdDate(date);
		this.setOrdId(ordId);
		this.setCmdId("UsrUnFreeze");
		this.setTrxId(trxId);
		this.setBgRetUrl(TPPWay.URL_WEBS2S + "/public/chinapnr/chinapnrBgRet.html");
	}

	public StringBuffer getMerData() throws UnsupportedEncodingException{

		StringBuffer MerData =super.getMerData();
		MerData.append(StringUtil.isNull(getOrdId()))
				.append(StringUtil.isNull(getOrdDate()))
				.append(StringUtil.isNull(getTrxId()))
				.append(StringUtil.isNull(getRetUrl()))
				.append(StringUtil.isNull(getBgRetUrl()));
		return MerData;
	}
	
	@Override
	public ChinapnrModel response(String res) throws IOException {
		super.response(res);
		try {
			JSONObject json= JSON.parseObject(res);
			this.setTrxId(json.getString("TrxId"));
			this.setOrdId(json.getString("OrdId"));
			this.setOrdDate(json.getString("OrdDate"));
			this.setRespDesc(json.getString("RespDesc"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return super.response(res);
	}

	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
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

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

}
