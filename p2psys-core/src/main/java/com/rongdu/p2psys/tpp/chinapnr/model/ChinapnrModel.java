package com.rongdu.p2psys.tpp.chinapnr.model;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import chinapnr.SecureLink;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongdu.common.util.StringUtil;
//import com.rongdu.context.Global;
//import com.rongdu.util.NumberUtils;
//import com.rongdu.util.ReflectUtils;
//import com.rongdu.util.StringUtils;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.ReflectUtils;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinapnrHttpHelper;
import com.rongdu.p2psys.tpp.chinapnr.tool.HttpClientUtils;

public class ChinapnrModel {
	
	private static final Logger logger=Logger.getLogger(ChinapnrModel.class);
	
	private String version="10";
	//消息类型
	private String cmdId; 
	//商户号
	private String merId=Global.getString("tpp_base_account");
	//用户号
	private String ordId;
	//交易额
	private String transAmt;
	//用户客户号
	private String usrCustId;
	//可用金额
	private String avlBal;
	//冻结金额
	private String frzBal;
	//账户总余额
	private String acctBal;
	//支付状态
	private String procStat;
	//签名
	private String chkValue;
	//应答返回码
	private String respCode;
	//日期加流水号
	private String trxId;
	//币种
	private String curCode="RMB";
	//同步返回地址
	private String retUrl;
	//商户私有域
	private String merPriv;
	//2.0无此参数
	private String gateId="61";    
	private String usrMp;             
	private String divDetails;
	private String payUsrId;      //2.0无此参数
	private String bgRetUrl;
	//第三方平台客户号
	private String merCustId=Global.getValue("tpp_cust_id");	
	//平台专属账户，正式环境中应该通过配置
	private String merAcctId=Global.getValue("tpp_account_id");	 
	//平台风险备用金账户
	private String riskReserveId=Global.getValue("tpp_risk_reserve_id");
	private String respDesc;
	//子账户账户  
	private String subAcctId;    
	//子账户类型
	private String subAcctType;  
	 //汇付2.0中订单时间
	private String ordDate;     
	 //出账账户子账户
	private String outAcctId; 
	//入账账户子账户
	private String inAcctId;  
	//开户银行代号
	private String openBankId;    
	//开户银行账户号
	private String openAcctId;         
	private String merKeyFileName=Global.getString("tpp_merKeyFileName");
	private String merKeyFile;
	private String pubKeyFileName=Global.getString("tpp_pubKeyFileName");
	private String pubKeyFile;
	private String url="";
	private String remark="";
	//2.0扩展接口
	private String reqExt="";
	private String[] paramNames=new String[]{};
	//用户签名
	private SecureLink sl=new SecureLink();
	// 手续费收取对象标志
	private String feeObjFlag = "I";     

	/**
	 * 是否开通线上环境配置。
	 * @return
	 */
	public boolean isOnlineConfig(){
		return "1".equals(Global.getValue("config_online"));
	}
	
	public ChinapnrModel() {
		super();
		init();
	}

	public void init(){
		if("1".equals(Global.getValue("config_online"))){//开通线上配置环境环境
			setUrl(Global.getValue("tpp_service_url"));
		}else{//开通测试环境地址
			setUrl(Global.getValue("tpp_service_test_url"));
		}
	}
	
	public String sign() throws Exception{
		logger.info("createMerKeyFile()");
		merKeyFile	= createMerKeyFile();			
		//商户私钥文件路径  请将MerPrK510010.key改为你的私钥文件名称
		logger.info("merKeyFile:"+merKeyFile);	
		String MerData = getMerData().toString();
		logger.info("MerData:" + MerData);
		SecureLink sl=new SecureLink();
		int ret=sl.SignMsg(merCustId,merKeyFile,MerData.getBytes("UTF-8"));

		if (ret != 0) 
		{
			logger.error("签名错误 ret=" + ret );
			throw new Exception("签名错误 ret=" + ret);
		}
		chkValue = sl.getChkValue( );
		logger.info("chkValue------------>"+chkValue);
		return chkValue;
	}
	
	public String createMerKeyFile(){
		String merKeyFile= this.getClass().getResource("/") + "com/rongdu/chinapnr"+merKeyFileName;
		logger.info(merKeyFile);
		if(merKeyFile.startsWith("file:/")) merKeyFile=merKeyFile.replaceFirst("file:/", "");
		if(File.separator.equals("/")) merKeyFile="/"+merKeyFile;
		return merKeyFile;
	}
	
	public String createPubKeyFile(){
		String pubKeyFile= this.getClass().getResource("/") + "com/rongdu/chinapnr"+pubKeyFileName;
		if(pubKeyFile.startsWith("file:/")) pubKeyFile=pubKeyFile.replaceFirst("file:/", "");
		if(File.separator.equals("/")) pubKeyFile="/"+pubKeyFile;
		return pubKeyFile;
	}
	
	public String submit() throws Exception{
		sign();
		String resp="";
		if(isOnlineConfig()){//线上配置
			showParams(getParam());
			logger.debug("提交路径："+getUrl());
			resp=HttpClientUtils.doHttpsPost(getUrl(), getmapparm(getParam()), "UTF-8", 50);
		}else{
			ChinapnrHttpHelper http=new ChinapnrHttpHelper(getUrl(),getParam(),"UTF-8");
			showParams(getParam());
			logger.debug("提交路径："+getUrl());
			resp=http.execute();
		}
		if(resp!=null){
			this.response(resp);
			logger.info("汇付http请求回调："+resp);
		}
		return resp;
	}
	
	
	public void showParams(String [][] args) {
		for(int i = 0 ; i < args.length; i++) {
			String key  = args[i][0];
			String value  = args[i][1];
			System.out.println(key +"="+value);
		}
	}
	
	public static Map<String, String> getmapparm(String[][] args){
		Map<String, String> map=new HashMap<String, String>();
		for(int i = 0 ; i < args.length; i++) {
			String key  = args[i][0];
			String value  = args[i][1];
			map.put(key, value);
		}
		return map;
	}

	public Properties getProp(String res) throws IOException{
		Properties prop=new Properties();
		prop.load(new StringReader(res));
		return prop;
	}
	
	public ChinapnrModel response(String res) throws IOException{
		try {
			JSONObject json= JSON.parseObject(res);
			cmdId=json.getString("CmdId");
			respCode=json.getString("RespCode");
			respDesc=json.getString("RespDesc");  
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String[][] getParam(){
		String[] paramNames=getParamNames();
		String[][] namePair=new String[paramNames.length][2];
		for(int i=0;i<paramNames.length;i++){
			String name=paramNames[i];
			Object result=ReflectUtils.invokeGetMethod(getClass(), this, name);
			String value=(result==null?"":result.toString());
			namePair[i][0]=StringUtil.firstCharUpperCase(name);
			namePair[i][1]=value;
		}
		return namePair;
	}
	
	public StringBuffer getMerData() throws UnsupportedEncodingException {
		StringBuffer MerData=new StringBuffer();
		logger.info("父类"+getMerCustId());
		MerData.append(StringUtil.isNull(getVersion()))
			.append(StringUtil.isNull(getCmdId()))
			.append(StringUtil.isNull(	getMerCustId()));
		
		return MerData;
	}

	/**
	 * 汇付2.0中用户给商户
	 * 自动扣款接口中使用，转换划账DivDetails字段,扣款和放款接口中使用
	 * 转换后格式为json格式的
	 * */
	public String newDivDetails(String[][] args){
		 if(args == null){
			 return "";
		 }
		StringBuffer sb=new StringBuffer();
        boolean first = true;
        sb.append("[");
        for (int i = 0; i < args.length; i++) {
            String[] blogItem = args[i];
            if (!first) {
                sb.append(",");
            }
            sb.append("{");
            sb.append("\"DivCustId\":\"" + blogItem[0] + "\",");
            sb.append("\"DivAcctId\":\"" + blogItem[1] + "\",");
            sb.append("\"DivAmt\":\"" + blogItem[2] +"\"");
            sb.append("}");
            first = false;
        }
        sb.append("]");
	        return sb.toString();
	}	
	

	public void fillDivDetailsForFee(String[][] args){
		StringBuffer sb=new StringBuffer();
		for(String[] str:args){
			sb.append("Agent:").append(getMerId()).append(str[0]).append(":").append(str[1]).append(";");
		}
		this.setDivDetails(sb.toString());
	}
	
	public StringBuffer getCallbackMerData(){
		StringBuffer MerData =new StringBuffer();
		return MerData;
	}
	
	public int callback(){
		return -1;
	}
	
	public boolean success(){
		return StringUtil.isNull(getRespCode()).equals("000");
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCmdId() {
		return cmdId;
	}

	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getOrdId() {
		return ordId;
	}

	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}

	public String getChkValue() {
		return chkValue;
	}

	public void setChkValue(String chkValue) {
		this.chkValue = chkValue;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getCurCode() {
		return curCode;
	}

	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}

	public String getRetUrl() {
		return retUrl;
	}

	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}

	public String getMerPriv() {
		return merPriv;
	}

	public void setMerPriv(String merPriv) {
		this.merPriv = merPriv;
	}

	public String getGateId() {
		return gateId;
	}

	public void setGateId(String gateId) {
		this.gateId = gateId;
	}

	public String getUsrMp() {
		return usrMp;
	}

	public void setUsrMp(String usrMp) {
		this.usrMp = usrMp;
	}

	public String getDivDetails() {
		return divDetails;
	}

	public void setDivDetails(String divDetails) {
		this.divDetails = divDetails;
	}

	public String getPayUsrId() {
		return payUsrId;
	}

	public void setPayUsrId(String payUsrId) {
		this.payUsrId = payUsrId;
	}

	public String getBgRetUrl() {
		return bgRetUrl;
	}

	public void setBgRetUrl(String bgRetUrl) {
		this.bgRetUrl = bgRetUrl;
	}

	public String getMerKeyFileName() {
		return merKeyFileName;
	}

	public void setMerKeyFileName(String merKeyFileName) {
		this.merKeyFileName = merKeyFileName;
	}

	public String getMerKeyFile() {
		return merKeyFile;
	}

	public void setMerKeyFile(String merKeyFile) {
		this.merKeyFile = merKeyFile;
	}
	public String getPubKeyFileName() {
		return pubKeyFileName;
	}
	public void setPubKeyFileName(String pubKeyFileName) {
		this.pubKeyFileName = pubKeyFileName;
	}

	public String getPubKeyFile() {
		return pubKeyFile;
	}

	public void setPubKeyFile(String pubKeyFile) {
		this.pubKeyFile = pubKeyFile;
	}

	public SecureLink getSl() {
		return sl;
	}

	public void setSl(SecureLink sl) {
		this.sl = sl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String[] getParamNames() {
		return paramNames;
	}

	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}

	public String getProcStat() {
		return procStat;
	}

	public void setProcStat(String procStat) {
		this.procStat = procStat;
	}

	public String getAvlBal() {
		return avlBal;
	}

	public void setAvlBal(String avlBal) {
		this.avlBal = avlBal;
	}

	public String getFrzBal() {
		return frzBal;
	}

	public void setFrzBal(String frzBal) {
		this.frzBal = frzBal;
	}
	public String getRespDesc() {
		try{
			return URLDecoder.decode(StringUtil.isNull(respDesc),"utf-8");
		}catch(UnsupportedEncodingException e){
			return "";
		}
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public String getMerCustId() {
		return merCustId;
	}

	public void setMerCustId(String merCustId) {
		this.merCustId = merCustId;
	}

	public String getUsrCustId() {
		return usrCustId;
	}

	public void setUsrCustId(String usrCustId) {
		this.usrCustId = usrCustId;
	}

	public String getSubAcctId() {
		return subAcctId;
	}

	public void setSubAcctId(String subAcctId) {
		this.subAcctId = subAcctId;
	}

	public String getOrdDate() {
		return ordDate;
	}

	public void setOrdDate(String ordDate) {
		this.ordDate = ordDate;
	}

	public String getSubAcctType() {
		return subAcctType;
	}

	public void setSubAcctType(String subAcctType) {
		this.subAcctType = subAcctType;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getAcctBal() {
		return acctBal;
	}

	public void setAcctBal(String acctBal) {
		this.acctBal = acctBal;
	}

	public String getOutAcctId() {
		return outAcctId;
	}

	public void setOutAcctId(String outAcctId) {
		this.outAcctId = outAcctId;
	}

	public String getInAcctId() {
		return inAcctId;
	}

	public void setInAcctId(String inAcctId) {
		this.inAcctId = inAcctId;
	}

	public String getOpenBankId() {
		return openBankId;
	}

	public void setOpenBankId(String openBankId) {
		this.openBankId = openBankId;
	}

	public String getOpenAcctId() {
		return openAcctId;
	}

	public void setOpenAcctId(String openAcctId) {
		this.openAcctId = openAcctId;
	}

	public String getMerAcctId() {
		return merAcctId;
	}

	public void setMerAcctId(String merAcctId) {
		this.merAcctId = merAcctId;
	}
	
	public String getRiskReserveId() {
		return riskReserveId;
	}

	public void setRiskReserveId(String riskReserveId) {
		this.riskReserveId = riskReserveId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReqExt() {
		return reqExt;
	}

	public void setReqExt(String reqExt) {
		this.reqExt = reqExt;
	}
	public String getFeeObjFlag() {
		return feeObjFlag;
	}

	public void setFeeObjFlag(String feeObjFlag) {
		this.feeObjFlag = feeObjFlag;
	}
    

}
