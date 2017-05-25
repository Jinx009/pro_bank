package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import chinapnr.SecureLink;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.tpp.TPPWay;

/**
 * 用户取现接口,通过页面形式
 * 
 */
public class ChinapnrCashOut extends ChinapnrModel {

	private static final Logger logger=Logger.getLogger(ChinapnrCashOut.class);
	private String usrId;
	private String transAmt;
	private String openAcctId;
	private String address = "/muser/publicRequests";
	private String testAddress = "/muser/publicRequests";	
	private String feeAmt;    //汇付取现手续费 
	private String feeCustId;
	private String feeAcctId;
	private String servFee;  //商户收取服务费金额
	private String ServFeeAcctId; //商户子账户号
	
	private String[] paramNames=new String[]{
			"version","cmdId","merCustId","ordId","usrCustId",
			"transAmt","servFee","ServFeeAcctId","openAcctId","retUtl","bgRetUrl","remark","merPriv","reqExt","chkValue"
	};

	public ChinapnrCashOut() {
		super();
	}
	
	public ChinapnrCashOut(String usrCustId) {
		super();
		this.setUsrCustId(usrCustId);
		this.setCmdId("Cash");
		this.setServFeeAcctId(getMerAcctId());
		this.setRetUrl(TPPWay.URL_WEB + "/public/chinapnr/cashReturn.html");
		this.setBgRetUrl(TPPWay.URL_WEBS2S + "/public/chinapnr/cashNotify.html");
	}
	
	public ChinapnrCashOut(long usrId) {
		this.usrId=getMerId()+usrId;
	}
	
	public StringBuffer getMerData() throws UnsupportedEncodingException{
		
		StringBuffer MerData =super.getMerData();
		MerData.append(StringUtil.isNull(getOrdId()))
				.append(StringUtil.isNull(getUsrCustId()))
				.append(StringUtil.isNull(getTransAmt()))
				.append(StringUtil.isNull(getServFee()))
				.append(StringUtil.isNull(getServFeeAcctId()))
				.append(StringUtil.isNull(getOpenAcctId()))
				.append(getRetUrl())
				.append(StringUtil.isNull(getBgRetUrl()))
				.append(getMerPriv())
				.append(StringUtil.isNull(getReqExt()));
		return MerData;
	}

	@Override
	public StringBuffer getCallbackMerData() {
		StringBuffer merData = new StringBuffer();
					 try {
						merData.append(StringUtil.isNull(getCmdId()))
						 		.append(StringUtil.isNull(getRespCode()))
						 		.append(StringUtil.isNull(getMerCustId()))
						 		.append(StringUtil.isNull(getOrdId()))
						 		.append(StringUtil.isNull(getUsrCustId()))
						 		.append(StringUtil.isNull(getTransAmt()))
						 		.append(StringUtil.isNull(getOpenAcctId()))
						 		.append(StringUtil.isNull(getOpenBankId()))
						 		.append(StringUtil.isNull(getFeeAmt()))
						 		.append(StringUtil.isNull(getFeeCustId()))
						 		.append(StringUtil.isNull(getFeeAcctId()))
						 		.append(StringUtil.isNull(getServFee()))
						 		.append(StringUtil.isNull(getServFeeAcctId()))
						 		.append(URLDecoder.decode(StringUtil.isNull(getRetUrl()),"utf-8"))
						 		.append(URLDecoder.decode(StringUtil.isNull(getBgRetUrl()),"utf-8"))
						 		.append(URLDecoder.decode(StringUtil.isNull(getMerPriv()),"utf-8"))
						        .append(URLDecoder.decode(StringUtil.isNull(getReqExt()),"utf-8"));
					} catch (UnsupportedEncodingException e) {
						logger.error(e);
						e.printStackTrace();
					}
					 logger.info("用户取现回调参数拼接"+merData.toString());
		return merData;
	}
	
	public void fieldReqExt(String[][] args){
		StringBuffer sb = new StringBuffer();
        boolean first = true;
        sb.append("[");
        for (int i = 0; i < args.length; i++) {
           String[] blogItem = args[i];
           if (!first) {
               sb.append(",");
           }
           sb.append("{");
           sb.append("\"FeeObjFlag\":\"" + blogItem[0] + "\",");
           sb.append("\"FeeAcctId\":\"" + blogItem[1] +"\",");
           sb.append("\"CashChl\":\"" + blogItem[2] +"\"");
           sb.append("}");
           first = false;
       }
       sb.append("]");
       this.setReqExt(sb.toString());
	}
	
	//用户取现验签操作
	public int callback(){
		logger.info("进入用户取现验签回调验证………………");
		String merKeyFile=createPubKeyFile();
		SecureLink sl = new SecureLink( ) ;
		logger.info("Chinapnr callback:"+this.getCallbackMerData().toString());
		logger.info("pubKeyFile:"+merKeyFile);
		logger.info("CallbackMerData:"+this.getCallbackMerData().toString());
		logger.info("getChkValue:"+getChkValue());
		int ret = sl.VeriSignMsg(merKeyFile , getCallbackMerData().toString(), getChkValue());
		return ret;
	}
	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
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

	public String getOpenAcctId() {
		return openAcctId;
	}

	public void setOpenAcctId(String openAcctId) {
		this.openAcctId = openAcctId;
	}
	public String getFeeAmt() {
		return feeAmt;
	}
	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}
	public String getServFee() {
		return servFee;
	}
	public void setServFee(String servFee) {
		this.servFee = servFee;
	}
	public String getServFeeAcctId() {
		return ServFeeAcctId;
	}
	public void setServFeeAcctId(String servFeeAcctId) {
		ServFeeAcctId = servFeeAcctId;
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
