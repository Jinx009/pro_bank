package com.rongdu.p2psys.tpp.ips.model;

import com.rongdu.p2psys.tpp.ips.tool.XmlTool;

/**
 * 
 * 环讯开户
 * 
 * @author lx
 * @version 2.0
 * @since 2014年8月13日
 */
public class IpsCash extends IpsModel {
	
	/**商户开户流水号*/
	private String  merBillNo; 
	/**
	 * 账户类型  否  0#机构（暂未开放） ；1#个人
	 */
	private String  acctType; 
	/**
	 * 提现模式  否  1#普通提现；2#定向提现<暂丌开放>
	 */
	private String  outType; 
	/**
	 * 标号  是/否  提现模式为 2 时，此字段生效
	 */
	private String  bidNo; 
	/**
	 * 合同号  是/否  提现模式为 2 时，此字段生效内容是投标时的合同号
	 */
	private String  contractNo; 
	
	/**
	 * 提现去向  是/否  提现模式为 2 时，此字段生效
	 */
	private String  dwTo; 
	/**
	 * 证件号码
	 */
	private String identNo;  
	/**
	 * 姓名
	 */
	private String realName;  
	/**
	 *  IPS 账户号  否  账户类型为 1 时，IPS 个人托管账户号
	账户类型为 0 时，由 IPS 颁发的商户号
	 */
	private String ipsAcctNo; 
	/**
	 * 提现日期  否  格式：YYYYMMDD
	 */
	private String dwDate; 
	/**
	 * 提现金额  否  金额单位，丌能为负，丌允许为 0
	 */
	private String trdAmt;  
	/**
	 * 平台手续费  否  金额单位，丌能为负，允许为 0
	 */
	private String merFee; 
	/**
	 * IPS 手续费收取方  否  这里是 IPS 收取的费用
	1：平台支付
	2：提现方支付
	 */
	private String ipsFeeType;  
	/**
	 * 状态返回地址 :同步
	 */
	private String webUrl;  
	/**
	 * 状态返回地址 ：异步
	 */
	private String s2SUrl;  
	/**
	 * 备注1
	 */
	private String memo1; 
	/**
	 * 备注2
	 */
	private String memo2;  
	/**
	 * 备注3
	 */
	private String memo3;  
	
	private String[] paramNames = new String[]{"MerBillNo","AcctType","OutType","BidNo","ContractNo","DwTo","IdentNo","RealName","IpsAcctNo","DwDate","TrdAmt",
			"MerFee","IpsFeeType","WebUrl","S2SUrl","Memo1","Memo2","Memo3"};

	/**回调参数***/
	/**
	 * Ips返回回来的流水号
	 */
	private String ipsBillNo;  
	
	
	public IpsCash(){
		super();
	}
	public IpsCash doReturnCreate(String str){
		IpsCash ipsCash = new IpsCash();
		XmlTool tool = new XmlTool();
		tool.SetDocument(str);
		ipsCash.setMerBillNo(tool.getNodeValue("pMerBillNo"));
		ipsCash.setAcctType(tool.getNodeValue("pAcctType"));
		ipsCash.setOutType(tool.getNodeValue("pOutType"));
		ipsCash.setBidNo(tool.getNodeValue("pBidNo"));
		ipsCash.setContractNo(tool.getNodeValue("pContractNo"));
		ipsCash.setDwTo(tool.getNodeValue("pDwTo"));
		ipsCash.setIdentNo(tool.getNodeValue("pIdentNo"));
		ipsCash.setRealName(tool.getNodeValue("pRealName"));
		ipsCash.setIpsAcctNo(tool.getNodeValue("pIpsAcctNo"));
		ipsCash.setDwDate(tool.getNodeValue("pDwDate"));
		ipsCash.setTrdAmt(tool.getNodeValue("pTrdAmt"));
		ipsCash.setMerFee(tool.getNodeValue("pMerFee"));
		ipsCash.setIpsFeeType(tool.getNodeValue("pIpsFeeType"));
		ipsCash.setWebUrl(tool.getNodeValue("pWebUrl"));
		ipsCash.setS2SUrl(tool.getNodeValue("pS2SUrl"));
		ipsCash.setMemo1(tool.getNodeValue("pMemo1"));
		ipsCash.setMemo2(tool.getNodeValue("pMemo2"));
		ipsCash.setMemo3(tool.getNodeValue("pMemo3"));
		ipsCash.setIpsBillNo(tool.getNodeValue("pIpsBillNo"));
		return ipsCash;
	}
	public String getMerBillNo() {
		return merBillNo;
	}
	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
	}
	public String getAcctType() {
		return acctType;
	}
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}
	public String getOutType() {
		return outType;
	}
	public void setOutType(String outType) {
		this.outType = outType;
	}
	public String getBidNo() {
		return bidNo;
	}
	public void setBidNo(String bidNo) {
		this.bidNo = bidNo;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getDwTo() {
		return dwTo;
	}
	public void setDwTo(String dwTo) {
		this.dwTo = dwTo;
	}
	public String getIdentNo() {
		return identNo;
	}
	public void setIdentNo(String identNo) {
		this.identNo = identNo;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIpsAcctNo() {
		return ipsAcctNo;
	}
	public void setIpsAcctNo(String ipsAcctNo) {
		this.ipsAcctNo = ipsAcctNo;
	}
	public String getDwDate() {
		return dwDate;
	}
	public void setDwDate(String dwDate) {
		this.dwDate = dwDate;
	}
	public String getTrdAmt() {
		return trdAmt;
	}
	public void setTrdAmt(String trdAmt) {
		this.trdAmt = trdAmt;
	}
	public String getMerFee() {
		return merFee;
	}
	public void setMerFee(String merFee) {
		this.merFee = merFee;
	}
	public String getIpsFeeType() {
		return ipsFeeType;
	}
	public void setIpsFeeType(String ipsFeeType) {
		this.ipsFeeType = ipsFeeType;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getS2SUrl() {
		return s2SUrl;
	}
	public void setS2SUrl(String s2sUrl) {
		s2SUrl = s2sUrl;
	}
	public String getMemo1() {
		return memo1;
	}
	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}
	public String getMemo2() {
		return memo2;
	}
	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}
	public String getMemo3() {
		return memo3;
	}
	public void setMemo3(String memo3) {
		this.memo3 = memo3;
	}
	public String[] getParamNames() {
		return paramNames;
	}
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	public String getIpsBillNo() {
		return ipsBillNo;
	}
	public void setIpsBillNo(String ipsBillNo) {
		this.ipsBillNo = ipsBillNo;
	}
	
	
}
