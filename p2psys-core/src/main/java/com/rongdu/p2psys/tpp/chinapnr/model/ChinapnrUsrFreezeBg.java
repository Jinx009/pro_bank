package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.tpp.TPPWay;

/**
 * 后台冻结资金接口
 *
 */
public class ChinapnrUsrFreezeBg extends ChinapnrModel {

	private String usrId;     
	private String[] paramNames = new String[] { 
			"version", "cmdId", "merCustId", "usrCustId", "ordId","ordDate", "transAmt","bgRetUrl","chkValue"};
	
	public ChinapnrUsrFreezeBg() {
		this("","");
	}
	
	public ChinapnrUsrFreezeBg(String usrId,String transAmt) {
		this(usrId,transAmt,"","");
	}
	
	public ChinapnrUsrFreezeBg(String usrId,String transAmt,String ordId,String date) {
		super();
		this.setUsrCustId(usrId);
		this.setTransAmt(transAmt);
		this.setOrdId(ordId);
		this.setBgRetUrl(TPPWay.URL_WEBS2S+"/public/chinapnr/chinapnrBgRet.html");
		this.setCmdId("UsrFreezeBg");
		this.setOrdDate(date);
	}
	
	public StringBuffer getMerData() throws UnsupportedEncodingException{

		StringBuffer MerData =super.getMerData();
		MerData.append(StringUtil.isNull(getUsrCustId()))	
				.append(StringUtil.isNull(getOrdId()))
				.append(StringUtil.isNull(getOrdDate()))
				.append(StringUtil.isNull(getTransAmt()))
				.append(StringUtil.isNull(getBgRetUrl()));
		return MerData;
	}



	@Override
	public ChinapnrModel response(String res) throws IOException {
		super.response(res);
		try {
			JSONObject json= JSON.parseObject(res);
			this.setTransAmt(json.getString("TransAmt"));
			this.setTrxId(json.getString("TrxId"));	 //此参数必须要，在解冻时根据trxId来解冻
			this.setOrdId(json.getString("OrdId"));
			this.setUsrCustId(json.getString("UsrCustId"));
			this.setOrdDate(json.getString("OrdDate"));
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

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

}
