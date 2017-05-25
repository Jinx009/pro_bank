package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;

/**
 * 自动扣款，放款接口
 * 
 */
public class ChinapnrLoans extends ChinapnrModel {

		private String outCustId;        //出账客户号
		private String fee;           //  
		private String subOrdId;       //   投标订单流水
		private String subOrdDate;       // 投标订单日期
		private String inCustId;        //    入账客户号
		private String isUnFreeze;    // 是否解冻
		private String unFreezeOrdId;       // 解冻订单号
		private String freezeTrxId;            // 解冻标示（冻结订单号）
		private String isDefault;      //      是否默认
		private String address = "/muser/publicRequests";
		private String testAddress = "/muser/publicRequests";
		
		public ChinapnrLoans(String outCustId,String inCustId,String transAmt,String ordId,String ordDate,String subOrdId,String subOrdDate,String fee,String riskReserveFee){
			super();
			// 2.0接口能直接解冻，并可指定管理费出资方
			this.setVersion("20");
			this.setCmdId("Loans");
			setOutCustId(outCustId);
			setInCustId(inCustId);
			this.setTransAmt(transAmt);
			this.setOrdId(ordId);
			this.setOrdDate(ordDate);
			this.setSubOrdId(subOrdId);
			this.setSubOrdDate(subOrdDate);
			this.setFee(BaseTPPWay.formatMoney((NumberUtil.getDouble(fee)+NumberUtil.getDouble(riskReserveFee))));
			if (NumberUtil.getDouble(fee)>0 && NumberUtil.getDouble(riskReserveFee) <=0) {
				String[][] divArg = new String[][]{{this.getMerCustId(), this.getMerAcctId(),fee}};
				this.setDivDetails(newDivDetails(divArg));
			}else if(NumberUtil.getDouble(riskReserveFee)>0 && NumberUtil.getDouble(fee) <=0){
				String[][] divArg = new String[][]{{this.getMerCustId(), this.getRiskReserveId(),riskReserveFee}};
				this.setDivDetails(newDivDetails(divArg));
			}else if(NumberUtil.getDouble(riskReserveFee)>0 && NumberUtil.getDouble(fee) >0){
				String[][] divArg = new String[][]{{this.getMerCustId(), this.getMerAcctId(),fee},{this.getMerCustId(),this.getRiskReserveId(),riskReserveFee}};
				this.setDivDetails(newDivDetails(divArg));
			}else{
				this.setDivDetails("");
			}
			this.setBgRetUrl(TPPWay.URL_ADMINS2S + "/public/chinapnr/loanAndrepay.html");
		}
		

		private String[] paramNames=new String[] {
				"version","cmdId","merCustId","ordId",
				"ordDate","outCustId","transAmt","fee",
				"subOrdId","subOrdDate","inCustId","divDetails","feeObjFlag","isDefault","isUnFreeze","unFreezeOrdId","freezeTrxId","bgRetUrl","chkValue"
				};
		
		public StringBuffer getMerData() throws UnsupportedEncodingException{
			StringBuffer MerData = super.getMerData();
			MerData.append(getOrdId())
			.append(getOrdDate())
			.append(getOutCustId())
			.append(getTransAmt())
			.append(getFee())
			.append(getSubOrdId())
			.append(getSubOrdDate())
			.append(getInCustId())
			.append(getDivDetails())
			.append(getFeeObjFlag())
			.append(getIsDefault())
			.append(getIsUnFreeze())
			.append(getUnFreezeOrdId())
			.append(getFreezeTrxId())
			.append(getBgRetUrl());
			return MerData;
		}
		
		@Override
		public ChinapnrModel response(String res) throws IOException {
			super.response(res);
			try {
				JSONObject json= JSON.parseObject(res);
				this.setTransAmt(json.getString("TransAmt"));
				this.setOutCustId(json.getString("OutCustId"));
				this.setInCustId(json.getString("InCustId"));
				this.setOrdId(json.getString("OrdId"));
				this.setOrdDate(json.getString("OrdDate"));
				this.setSubOrdId(json.getString("SubOrdId"));
				this.setSubOrdDate(json.getString("SubOrdDate"));
				this.setFee(json.getString("Fee"));
			} catch (Exception e) {
			}
			return super.response(res);
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
		public String getFee() {
			return fee;
		}
		public String getOutCustId() {
			return outCustId;
		}
		public void setOutCustId(String outCustId) {
			this.outCustId = outCustId;
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
		public String getIsUnFreeze() {
			return isUnFreeze;
		}

		public void setIsUnFreeze(String isUnFreeze) {
			this.isUnFreeze = isUnFreeze;
		}

		public String getUnFreezeOrdId() {
			return unFreezeOrdId;
		}

		public void setUnFreezeOrdId(String unFreezeOrdId) {
			this.unFreezeOrdId = unFreezeOrdId;
		}

		public String getFreezeTrxId() {
			return freezeTrxId;
		}

		public void setFreezeTrxId(String freezeTrxId) {
			this.freezeTrxId = freezeTrxId;
		}

		public String getIsDefault() {
			return isDefault;
		}
		public void setIsDefault(String isDefault) {
			this.isDefault = isDefault;
		}
}
