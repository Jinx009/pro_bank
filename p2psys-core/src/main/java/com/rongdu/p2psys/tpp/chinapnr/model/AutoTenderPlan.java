package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;

import chinapnr.SecureLink;

/**
 * 自动投标计划
 * @author sj
 * @since 2014年12月22日15:53:31
 *
 */
public class AutoTenderPlan extends ChinapnrModel {
	
	private static final Logger logger=Logger.getLogger(AutoTenderPlan.class);
	
	private String tenderPlanType = "W";  //取值：P:部分授权。W:完全授权
	
	public AutoTenderPlan() {
		super();
	}
	
	public AutoTenderPlan(String usercustId) {
		super();
		this.setCmdId("AutoTenderPlan");
		this.setUsrCustId(usercustId);
		this.setRetUrl(Global.getValue("weburl") + "/public/chinapnr/autoTenderPlanReturn.html");
		this.setBgRetUrl(Global.getValue("weburl") + "/public/chinapnr/autoTenderNotify.html");
	}
	private String[] paramNames = new String[]{
			"version","cmdId","merCustId","usrCustId","tenderPlanType",
			"retUrl","merPriv","chkValue"
	};
	
	@Override
	public StringBuffer getMerData() throws UnsupportedEncodingException{
		StringBuffer MerData = super.getMerData();
		MerData.append(StringUtil.isNull(getUsrCustId())).append(getTenderPlanType())
				.append(getRetUrl()).append(getMerPriv());
		return MerData;
	}
	
	@Override
	public StringBuffer getCallbackMerData() {
		StringBuffer merData = new StringBuffer();
					 try {
						merData.append(StringUtil.isNull(getCmdId()))
						 		.append(StringUtil.isNull(getRespCode()))
						 		.append(StringUtil.isNull(getMerCustId()))
						 		.append(StringUtil.isNull(getUsrCustId()))
						 		.append(StringUtil.isNull(getTenderPlanType()))
//						 		.append(StringUtil.isNull(getTransAmt()))
						 		.append(URLDecoder.decode(StringUtil.isNull(getRetUrl()),"utf-8"))
						 		.append(URLDecoder.decode(StringUtil.isNull(getMerPriv()),"utf-8"));
					} catch (UnsupportedEncodingException e) {
						logger.error(e);
						e.printStackTrace();
					}
					logger.info("自动投标计划回调参数拼接"+merData.toString());
		return merData;
	}
	
	public int callback(){
		logger.info("进入用户投标回调验证");
		String merKeyFile = createPubKeyFile();
		SecureLink sl = new SecureLink( ) ;
		logger.info("Chinapnr callback:"+this.getCallbackMerData().toString());
		logger.info("pubKeyFile:"+merKeyFile);
		logger.info("CallbackMerData:"+this.getCallbackMerData().toString());
		logger.info("getChkValue:"+getChkValue());
		int ret = sl.VeriSignMsg(merKeyFile , getCallbackMerData().toString(), getChkValue());
		logger.info("自动投标计划ret" + ret);
		return ret;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String getTenderPlanType() {
		return tenderPlanType;
	}

	public void setTenderPlanType(String tenderPlanType) {
		this.tenderPlanType = tenderPlanType;
	}
	

}
