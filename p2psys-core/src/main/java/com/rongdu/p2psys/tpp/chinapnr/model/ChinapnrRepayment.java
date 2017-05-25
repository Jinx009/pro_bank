package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.tpp.TPPWay;

/**
 * 还款接口
 * 
 */
public class ChinapnrRepayment extends ChinapnrModel {

	private String outCustId;        //出账客户号
	private String fee;           //  利率
	private String subOrdId;       //   投标订单流水
	private String subOrdDate;       // 投标订单日期
	private String inCustId;        //    入账客户号
	private String isDefault;      //      是否默认
	
	private String[] paramNames=new String[] {
			"version","cmdId","merCustId","ordId",
			"ordDate","outCustId","subOrdId","subOrdDate","outAcctId",
			"transAmt","fee","inCustId","inAcctId","divDetails","feeObjFlag","bgRetUrl","merPriv","chkValue"
			};	

	public ChinapnrRepayment(){
	}
	
	public ChinapnrRepayment(String outCustId,String inCustId,String transAmt,String ordId,String ordDate,String subOrdId,String subOrdDate,String fee){
		super();
		if (inCustId.equals("1")){
			inCustId = getMerCustId();
			this.setInAcctId(getMerAcctId());
		}
		this.setVersion("20");
		this.setCmdId("Repayment");
		this.setOrdId(ordId);
		this.setOrdDate(ordDate);
		this.setOutCustId(outCustId);
		this.setInCustId(inCustId);
		this.setTransAmt(transAmt);
		this.setSubOrdId(subOrdId);
		this.setSubOrdDate(subOrdDate);
		this.setFee(fee);
		if (NumberUtil.getDouble(fee)>0) {
			String[][] divArg = new String[][]{{this.getMerCustId(), this.getMerAcctId(),fee}};
			this.setDivDetails(newDivDetails(divArg));
		}else{
			this.setDivDetails("");
		}
		this.setBgRetUrl(TPPWay.URL_WEBS2S + "/public/chinapnr/loanAndrepay.html");
	}

	public StringBuffer getMerData() throws UnsupportedEncodingException{
		StringBuffer MerData = super.getMerData();
		MerData.append(getOrdId())
		.append(getOrdDate())
		.append(getOutCustId())
		.append(getSubOrdId())
		.append(getSubOrdDate());
		if(StringUtil.isBlank(getOutAcctId())){
			MerData.append("");
		}else{
			MerData.append(getOutAcctId());
		}
		MerData.append(getTransAmt())
		.append(getFee())
		.append(getInCustId());
		if(StringUtil.isBlank(getInAcctId())){
			MerData.append("");
		}else{
			MerData.append(getInAcctId());
		}
		MerData.append(getDivDetails())
		.append(getFeeObjFlag())
		.append(getBgRetUrl());
		return MerData;
	}

	@Override
	public ChinapnrModel response(String res) throws IOException {

		super.response(res);
		try {
			JSONObject json= JSON.parseObject(res);
			this.setOrdId(json.getString("OrdId"));
			this.setOrdDate(json.getString("OrdDate"));
			this.setOutAcctId(json.getString("OutAcctId"));
			this.setInAcctId(json.getString("InAcctId"));
			this.setFee(json.getString("Fee"));
			this.setInCustId(json.getString("InCustId"));
			this.setOutCustId(json.getString("OutCustId"));
			this.setSubOrdId(json.getString("SubOrdId"));
			this.setSubOrdDate(json.getString("SubOrdDate"));
			this.setTransAmt(json.getString("TransAmt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.response(res);
	}

	public String getOutCustId() {
		return outCustId;
	}
	public void setOutCustId(String outCustId) {
		this.outCustId = outCustId;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getSubOrdId() {
		return subOrdId;
	}
	public void setSubOrdId(String subOrdId) {
		this.subOrdId = subOrdId;
	}
	public String getSubOrdDate() {
		return subOrdDate;
	}
	public void setSubOrdDate(String subOrdDate) {
		this.subOrdDate = subOrdDate;
	}
	public String getInCustId() {
		return inCustId;
	}
	public void setInCustId(String inCustId) {
		this.inCustId = inCustId;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

}
