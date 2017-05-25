package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import chinapnr.SecureLink;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.tpp.TPPWay;

/*
 * 充值接口
 * 
 * */
public class ChinapnrNetSave extends ChinapnrModel {
	private static final Logger logger=Logger.getLogger(ChinapnrNetSave.class);

	private String address="/muser/publicRequests";
	private String testAddress="/muser/publicRequests";
	private String dcFlag;        //借贷标记：D：借记,;C：贷记
	private String gateBusiId;         //充值方式
	private String gateBankId;          //充值选择的银行
	private String feeAmt;    //充值手续费，新增
	private String feeCustId;  //充值扣费客户号
	private String feeAcctId;   //充值扣费子账户
	
	private String[] paramNames=new String[]{
			 "version", "cmdId", "merCustId","usrCustId","ordId", "ordDate",
			 "transAmt","retUrl","bgRetUrl","merPriv","chkValue"};
	
	public ChinapnrNetSave(){
		
	}
	
	public ChinapnrNetSave(String ordid,String date,String dcFlag,String transAmt,String usrCusid){
		super();
		setUsrCustId(usrCusid);
		setCmdId("NetSave");
		setRetUrl(TPPWay.URL_WEB+"/public/chinapnr/netSaveReturn.html");
		setBgRetUrl(TPPWay.URL_WEBS2S+"/public/chinapnr/netSaveNotify.html");
		setGateBusiId("B2C");
		setDcFlag(dcFlag);
		setOrdId(ordid);
		setOrdDate(date);
		setTransAmt(transAmt);
	}

	public StringBuffer getMerData() throws UnsupportedEncodingException{
		StringBuffer MerData = super.getMerData();
		MerData.append(getUsrCustId())
		.append(getOrdId())
		.append(getOrdDate())
		.append(getTransAmt())
		.append(getRetUrl())
		.append(getBgRetUrl())
		.append(getMerPriv());
		return MerData;
	}
	
	public String getDcFlag() {
		return dcFlag;
	}
	
	public void setDcFlag(String dcFlag) {
		this.dcFlag = dcFlag;
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
	
	public String getGateBusiId() {
		return gateBusiId;
	}
	
	public void setGateBusiId(String gateBusiId) {
		this.gateBusiId = gateBusiId;
	}
	
	public String getGateBankId() {
		return gateBankId;
	}
	
	public void setGateBankId(String gateBankId) {
		this.gateBankId = gateBankId;
	}
	
	//获取返回参数列表
	public String getReturnString() {
		StringBuffer merData=new StringBuffer();
		merData.append("CmdId=" + StringUtil.isNull(getCmdId()))
		        .append("&")
			   .append("RespCode="+StringUtil.isNull(getRespCode()))
			   .append("&")
			   .append("MerCustId="+StringUtil.isNull(getMerCustId()))
			   .append("&")
			   .append("UsrCustId="+StringUtil.isNull(getUsrCustId()))
			   .append("&")
			   .append("OrdId=" + StringUtil.isNull(getOrdId()))
			   .append("&")
			   .append("OrdDate="+StringUtil.isNull(getOrdDate()))
			   .append("&")
			   .append("TransAmt="+StringUtil.isNull(getTransAmt()))
			   .append("&")
			   .append("TrxId="+StringUtil.isNull(getTrxId()))
			   .append("&")
			   .append("RetUrl="+StringUtil.isNull(getRetUrl()))
			   .append("&")
			   .append("BgRetUrl="+StringUtil.isNull(getBgRetUrl()))
			   .append("&")
			   .append("MerPriv="+StringUtil.isNull(getMerPriv()));
		logger.info(merData.toString());
		return merData.toString();
	}
	
	@Override
	public StringBuffer getCallbackMerData() {
		logger.info("进入充值回调参数拼接………………");
		StringBuffer merData=new StringBuffer();
		try {
			merData.append(StringUtil.isNull(getCmdId()))
				   .append(StringUtil.isNull(getRespCode()))
				   .append(StringUtil.isNull(getMerCustId()))
				   .append(StringUtil.isNull(getUsrCustId()))
				   .append(StringUtil.isNull(getOrdId()))
				   .append(StringUtil.isNull(getOrdDate()))
				   .append(StringUtil.isNull(getTransAmt()))
				   .append(StringUtil.isNull(getTrxId()))
				   .append(URLDecoder.decode(StringUtil.isNull(getRetUrl()),"utf-8"))
				   .append(URLDecoder.decode(StringUtil.isNull(getBgRetUrl()),"utf-8"))
				   .append(URLDecoder.decode(StringUtil.isNull(getMerPriv()),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			e.printStackTrace();
		}
		logger.info(merData.toString());
		return merData;
	}

	public int callback(){
		logger.info("汇付2.0进入充值");
		String merKeyFile=createPubKeyFile();
		SecureLink sl = new SecureLink( ) ;
		logger.info("Chinapnr callback:"+this.getCallbackMerData().toString());
		logger.info("pubKeyFile:"+merKeyFile);
		logger.info("CallbackMerData:"+this.getCallbackMerData().toString());
		logger.info("getChkValue:"+getChkValue());
		int ret = sl.VeriSignMsg(merKeyFile , getCallbackMerData().toString(), getChkValue());
		return ret;
	}
	
	public String getFeeAmt() {
		return feeAmt;
	}
	
	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}
	
	public String getFeeCustId() {
		return feeCustId;
	}
	
	public void setFeeCustId(String feeCustId) {
		this.feeCustId = feeCustId;
	}
	
	public String getFeeAcctId() {
		return feeAcctId;
	}
	
	public void setFeeAcctId(String feeAcctId) {
		this.feeAcctId = feeAcctId;
	}
		
}
