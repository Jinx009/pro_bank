package com.rongdu.p2psys.tpp.ips.model;

import com.rongdu.p2psys.tpp.ips.tool.XmlTool;

public class IpsRecharge extends IpsModel {
	/**
	 * 账户类型 
	 * 固定值为  1，表示为类型为 IPS 个人账户
	 */
	private String acctType = "1";
	/**
	 * 证件号码
	 * 真实身份证（个人）/IPS 颁发的商户号（商户）
	 */
	private String identNo;
	/**
	 * 真实姓名（中文）
	 */
	private String realName;
	/**
	 * 账户类型为 1 时，为IPS 托管账户号（个人）
	 */
	private String ipsAcctNo = "1";
	/**
	 * 充值日期
	 * 格式：YYYYMMDD
	 */
	private String trdDate;
	/**
	 * 充值金额
	 */
	private String trdAmt;
	/**
	 * 充值渠道种类
	 * 1#网银充值；2#代扣充值
	 */
	private String channelType;
	/**
	 * 充值银行
	 */
	private String trdBnkCode;
	/**
	 * 平台手续费
	 */
	private String merFee;
	/**
	 * IPS 向平台收取的费用  
	 * 1：平台支付  2：用户支付
	 */
	private String ipsFeeType;
	
	/**
	 * 备注字段
	 */
	private String memo1;
	private String memo2;
	private String memo3;
	
	/**
     * 请求参数列表
     */
    private String[] paramNames=new String[]{"MerBillNo","AcctType","IdentNo","RealName","IpsAcctNo","TrdDate",
    		"TrdAmt","ChannelType","TrdBnkCode","MerFee","IpsFeeType","WebUrl","S2SUrl","Memo1","Memo2","Memo3"};
    
    //回调参数
    private String ipsBillNo;
	public IpsRecharge(){
		super();
	}
	
	/**
	 * @param str
	 * @return
	 */
	public void doReturnMessage(String str){
		XmlTool tool = new XmlTool();
		tool.SetDocument(str);
		this.setMerBillNo(tool.getNodeValue("pMerBillNo"));
		this.setAcctType(tool.getNodeValue("pAcctType"));
		this.setIdentNo(tool.getNodeValue("pIdentNo"));
		this.setRealName(tool.getNodeValue("pRealName"));
		this.setIpsAcctNo(tool.getNodeValue("pIpsAcctNo"));
		this.setTrdDate(tool.getNodeValue("pTrdDate"));
		this.setTrdAmt(tool.getNodeValue("pTrdAmt"));
		this.setTrdBnkCode(tool.getNodeValue("pTrdBnkCode"));
		this.setIpsBillNo(tool.getNodeValue("pIpsBillNo"));
		this.setMemo1(tool.getNodeValue("pMemo1"));
		this.setMemo2(tool.getNodeValue("pMemo2"));
		this.setMemo3(tool.getNodeValue("pMemo3"));
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
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

	public String getTrdDate() {
		return trdDate;
	}

	public void setTrdDate(String trdDate) {
		this.trdDate = trdDate;
	}

	public String getTrdAmt() {
		return trdAmt;
	}

	public void setTrdAmt(String trdAmt) {
		this.trdAmt = trdAmt;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getTrdBnkCode() {
		return trdBnkCode;
	}

	public void setTrdBnkCode(String trdBnkCode) {
		this.trdBnkCode = trdBnkCode;
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

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("pMerBillNo:"+getMerBillNo());
		buf.append(",pAcctType"+getAcctType());
		buf.append(",pIdentNo"+getIdentNo());
		buf.append(",pRealName"+getRealName());
		buf.append(",pIpsAcctNo"+getIpsAcctNo());
		buf.append(",pTrdDate"+getTrdDate());
		buf.append(",pTrdAmt"+getTrdAmt());
		buf.append(",pTrdBnkCode"+getTrdBnkCode());
		buf.append(",pIpsBillNo"+getIpsBillNo());
		buf.append(",pMemo1"+getMemo1());
		buf.append(",pMemo2"+getMemo2());
		buf.append(",pMemo3"+getMemo3());
		return buf.toString();
	}
	
}
