package com.rongdu.p2psys.tpp.ips.model;

import java.io.Serializable;

import com.rongdu.p2psys.tpp.ips.tool.XmlTool;

/**
 * 环迅发标接口
 * @author wujing
 *
 */
public class IpsAddBorrow extends IpsModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1095261831869968997L;
	
	public IpsAddBorrow(){
		super();
	}

	/**
	 * 标号
	 */
	private String bidNo;  
	
	/**
	 * 商户日期
	 */
	private String regDate;
	
	/**
	 * 借款金额
	 */
	private String lendAmt;
	
	/**
	 * 借款保证金
	 * 允许冻结的金额
	 */
	private String guaranteesAmt;
	
	/**
	 * 借款利率
	 * 借款利率  < 48%，例如：45.12%传入45.12
	 */
	private String trdLendRate; 
	
	/**
	 * 借款周期类型
	 * 借款周期类型，1：天；3：月； 借款周期 <= 5年 
	 */
	private String trdCycleType;
	
	/**
	 * 借款周期值：借款周期 <= 5年。 如果借款周期类型为天，
	 * 则借款周期值<=  1800(360  * 5)；如果借款周期类
	 * 型为月，则借款周期值<= 60(12 * 5) 
	 * 
	 */
	private String trdCycleValue;
	
	/**
	 * 借款用途
	 */
	private String lendPurpose;
	
	/**
	 * 还款方式：还款方式，1：等额本息，
	 * 2：按月还息到期还本；
	 * 3：等额本金；
	 * 99：其他； 
	 */
	private String repayMode;
	
	/**
	 * 标的操作类型：标的操作类型，1：新增，2：结束 
	 * 标的“结束”后，投资人投标冻结金额、担
	 * 保方保证金、借款人保证金均自劢解冻。
	 */
	private String operationType;
	
	/**
	 * 借款人手续费 
	 */
	private String lendFee;
	
	/**
	 * 账户类型 :0#机构（暂未开放）；1#个人
	 */
	private String acctType;
	
	/**
	 * 证件号码：真实身份证（个人）/由IPS颁发的商户号
	 */
	private String identNo; 
	
	/**
	 * 真实姓名（中文）
	 */
	private String realName;
	
	/**
	 * IPS账户号 :账户类型为1时，IPS托管账户号（个人） 
	 * 账户类型为0时，由IPS颁发的商户号 
	 */
	private String ipsAcctNo; 
	
	/**
	 * 浏览器返回地址
	 */
	private String webUrl;
	
	/**
	 * 异步返回地址
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
	
	
	private String[] paramNames = new String[]{"MerBillNo","BidNo","RegDate","LendAmt","GuaranteesAmt",
												"TrdLendRate","TrdCycleType","TrdCycleValue","LendPurpose",
												"RepayMode","OperationType","LendFee","AcctType","IdentNo",
												"RealName","IpsAcctNo","WebUrl","S2SUrl","Memo1","Memo2","Memo3"};
	
	
	
	/***回调时所需参数***/
	
	/**
	 *标的状态：1：新增；2：募集中；
	 *3：迚行中；8：结束处理中；9：失败；10：结束； 
	 */
	private String bidStatus;
	
	/**
	 * 由IPS系统生成的唯一流水号 
	 */
	private String ipsBillNo;
	
	/**
	 * IPS处理时间 :yyyyMMddHHmmss
	 */
	private String ipsTime;
	
	/**
	 * 实际冻结金额，金额单位，丌能为负，丌允许为0； 
	 * 实际冻结金额 = 保证金+手续费 
	 */
	private String realFreezenAmt;
	
	
	/**
	 * 回调xml解析封装成对象
	 * @param xml
	 * @return
	 */

	public IpsAddBorrow doReturnCreate(String xml){
		IpsAddBorrow ipsBorrow = new IpsAddBorrow();
		XmlTool tool = new XmlTool();
		tool.SetDocument(xml);
		ipsBorrow.setBidNo(tool.getNodeValue("pBidNo"));
		ipsBorrow.setRegDate(tool.getNodeValue("pRegDate"));
		ipsBorrow.setLendAmt(tool.getNodeValue("pLendAmt"));
		ipsBorrow.setGuaranteesAmt(tool.getNodeValue("pGuaranteesAmt"));
		ipsBorrow.setTrdLendRate(tool.getNodeValue("pTrdLendRate"));
		ipsBorrow.setTrdCycleType(tool.getNodeValue("pTrdCycleType"));
		ipsBorrow.setTrdCycleValue(tool.getNodeValue("pTrdCycleValue"));
		ipsBorrow.setLendPurpose(tool.getNodeValue("pLendPurpose"));
		ipsBorrow.setRepayMode(tool.getNodeValue("pRepayMode"));
		ipsBorrow.setOperationType(tool.getNodeValue("pOperationType"));
		ipsBorrow.setLendFee(tool.getNodeValue("pLendFee"));
		ipsBorrow.setAcctType(tool.getNodeValue("pAcctType"));
		ipsBorrow.setIdentNo(tool.getNodeValue("pIdentNo"));
		ipsBorrow.setRealName(tool.getNodeValue("pRealName"));
		ipsBorrow.setIpsAcctNo(tool.getNodeValue("pIpsAcctNo"));
		ipsBorrow.setIpsBillNo(tool.getNodeValue("pIpsBillNo"));
		ipsBorrow.setIpsTime(tool.getNodeValue("pIpsTime"));
		ipsBorrow.setBidStatus(tool.getNodeValue("pBidStatus"));
		ipsBorrow.setRealFreezenAmt(tool.getNodeValue("pRealFreezenAmt"));
		ipsBorrow.setMerBillNo(tool.getNodeValue("pMerBillNo"));
		ipsBorrow.setMemo1(tool.getNodeValue("pMemo1"));
		ipsBorrow.setMemo2(tool.getNodeValue("pMemo2"));
		ipsBorrow.setMemo3(tool.getNodeValue("pMemo3"));
		return ipsBorrow;
		
	}
	/*
	 * 
	<pIpsBillNo>string</pIpsBillNo> 
	<pMemo1><![CDATA[string]]></pMemo1> */

	public String getBidNo() {
		return bidNo;
	}

	public void setBidNo(String bidNo) {
		this.bidNo = bidNo;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getLendAmt() {
		return lendAmt;
	}

	public void setLendAmt(String lendAmt) {
		this.lendAmt = lendAmt;
	}

	public String getGuaranteesAmt() {
		return guaranteesAmt;
	}

	public void setGuaranteesAmt(String guaranteesAmt) {
		this.guaranteesAmt = guaranteesAmt;
	}

	public String getTrdLendRate() {
		return trdLendRate;
	}

	public void setTrdLendRate(String trdLendRate) {
		this.trdLendRate = trdLendRate;
	}

	public String getTrdCycleType() {
		return trdCycleType;
	}

	public void setTrdCycleType(String trdCycleType) {
		this.trdCycleType = trdCycleType;
	}

	public String getTrdCycleValue() {
		return trdCycleValue;
	}

	public void setTrdCycleValue(String trdCycleValue) {
		this.trdCycleValue = trdCycleValue;
	}

	public String getLendPurpose() {
		return lendPurpose;
	}

	public void setLendPurpose(String lendPurpose) {
		this.lendPurpose = lendPurpose;
	}

	public String getRepayMode() {
		return repayMode;
	}

	public void setRepayMode(String repayMode) {
		this.repayMode = repayMode;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getLendFee() {
		return lendFee;
	}

	public void setLendFee(String lendFee) {
		this.lendFee = lendFee;
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

	public String getBidStatus() {
		return bidStatus;
	}

	public void setBidStatus(String bidStatus) {
		this.bidStatus = bidStatus;
	}



	public String getIpsTime() {
		return ipsTime;
	}

	public void setIpsTime(String ipsTime) {
		this.ipsTime = ipsTime;
	}

	public String getRealFreezenAmt() {
		return realFreezenAmt;
	}

	public void setRealFreezenAmt(String realFreezenAmt) {
		this.realFreezenAmt = realFreezenAmt;
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
