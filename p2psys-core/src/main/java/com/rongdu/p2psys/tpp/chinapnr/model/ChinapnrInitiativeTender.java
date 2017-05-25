package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import chinapnr.SecureLink;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.tpp.TPPWay;

/**
 * 用户投标
 * 
 */
public class ChinapnrInitiativeTender extends ChinapnrModel {
	private static final Logger logger=Logger.getLogger(ChinapnrInitiativeTender.class);

	private String borrowerDetails; 
	private String maxTenderRate;   //最大投资手续费
	
	private String[] paramNames = new String[] { 
			"version", "cmdId", "merCustId",
			"ordId","ordDate","transAmt","usrCustId", 
			"maxTenderRate","borrowerDetails","retUrl","bgRetUrl","merPriv","chkValue"};		
	
	public ChinapnrInitiativeTender(){
		super();
	}
		
	public ChinapnrInitiativeTender(String usrCustId,String transAmt,String maxTenderRate){
		super();
		this.setUsrCustId(usrCustId);
		this.setCmdId("InitiativeTender");
		this.setTransAmt(transAmt);
		this.setMerCustId(super.getMerCustId());
		this.setMaxTenderRate(maxTenderRate);
		this.setRetUrl(TPPWay.URL_WEB + "/public/chinapnr/pnrTenderReturn.html");
		this.setBgRetUrl(TPPWay.URL_WEBS2S+ "/public/chinapnr/pnrTenderNotify.html");
	}
		
	public StringBuffer getMerData() throws UnsupportedEncodingException{
		StringBuffer MerData =super.getMerData();
		            MerData.append(getOrdId())
		            .append(getOrdDate())
		            .append(getTransAmt())
		            .append(getUsrCustId())
		            .append(getMaxTenderRate())
		            .append(getBorrowerDetails())
		            .append(getRetUrl())
		            .append(getBgRetUrl())
		            .append(getMerPriv());
		return MerData;	
	}
	
	 /**
	  * 在汇付2.0版本中，转换投标接口中
	  * BorrowerDetails字段转换为json格式
	  * */
	 public void fildBorrowerDetails(String[][] args){
		 StringBuffer sb=new StringBuffer();
			
	        boolean first = true;
	        sb.append("[");
	        for (int i = 0; i < args.length; i++) {
	            String[] blogItem = args[i];
	            if (!first) {
	                sb.append(",");
	            }
	            sb.append("{");
	            sb.append("\"BorrowerCustId\":\""+blogItem[0] + "\",");
	            sb.append("\"BorrowerAmt\":\""+blogItem[1]+"\",");
	            sb.append("\"BorrowerRate\":\""+blogItem[2]+"\"");
	            sb.append("}");
	            first = false;
	        }
	        sb.append("]");
	        this.setBorrowerDetails(sb.toString());
	 }
	  
	 //用户投标回调参数拼接
	@Override
	public StringBuffer getCallbackMerData() {
		StringBuffer merData = new StringBuffer();
					 try {
						merData.append(StringUtil.isNull(getCmdId()))
						 		.append(StringUtil.isNull(getRespCode()))
						 		.append(StringUtil.isNull(getMerCustId()))
						 		.append(StringUtil.isNull(getOrdId()))
						 		.append(StringUtil.isNull(getOrdDate()))
						 		.append(StringUtil.isNull(getTransAmt()))
						 		.append(StringUtil.isNull(getUsrCustId()))
						 		.append(StringUtil.isNull(getTrxId()))
						 		.append(URLDecoder.decode(StringUtil.isNull(getRetUrl()),"utf-8"))
						 		.append(URLDecoder.decode(StringUtil.isNull(getBgRetUrl()),"utf-8"))
						 		.append(URLDecoder.decode(StringUtil.isNull(getMerPriv()),"utf-8"));
					} catch (UnsupportedEncodingException e) {
						logger.error(e);
						e.printStackTrace();
					}
					 logger.info("用户投标回调参数拼接"+merData.toString());
		return merData;
	}

	public int callback(){
		logger.info("进入用户投标回调验证");
		String merKeyFile=createPubKeyFile();
		SecureLink sl = new SecureLink( ) ;
		logger.info("Chinapnr callback:"+this.getCallbackMerData().toString());
		logger.info("pubKeyFile:"+merKeyFile);
		logger.info("CallbackMerData:"+this.getCallbackMerData().toString());
		logger.info("getChkValue:"+getChkValue());
		int ret = sl.VeriSignMsg(merKeyFile , getCallbackMerData().toString(), getChkValue());
		logger.info("投标ret"+ret);
			return ret;
	}

	public String getBorrowerDetails() {
		return borrowerDetails;
	}

	public void setBorrowerDetails(String borrowerDetails) {
		this.borrowerDetails = borrowerDetails;
	}

	public String getMaxTenderRate() {
		return maxTenderRate;
	}

	public void setMaxTenderRate(String maxTenderRate) {
		this.maxTenderRate = maxTenderRate;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
}
