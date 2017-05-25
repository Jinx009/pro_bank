package com.rongdu.p2psys.tpp.ips.model;

import java.io.Serializable;
import java.util.Date;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.tpp.ips.tool.XmlTool;
import com.rongdu.p2psys.user.domain.User;

public class IpsAutoRecharge extends IpsModel implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 商户充值订单号 
     */
    private String merBillNo;
    /**
     * 充值日期
     */
    private String trdDate;
    /**
     * 充值金额
     */
    private String trdAmt;
    /**
     * 平台手续费 
     */
    private String merFee;
    /**
     * 谁付 IPS 手续费
     * 1：平台支付
     * 2：用户支付
     */
    private String ipsFeeType; 
    /**
     * 账户类型
     * 固定值为  1，表示为类型为 IPS 个人账户
     */
    private String acctType; 
    /**
     * 证件号码
     */
    private String identNo;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 托管账户号
     */
    private String ipsAcctNo;
    /**
     * 状态返回地址 2 
     */
    private String s2SUrl;
    /**
     * IPS 充值订单号 
     */
    private String ipsBillNo; 
    /**
     * IPS 处理时间 
     */
    private String ipsTime;
    /**
     * 实际交易金额 
     */
    private String trdRealAmt;
    /**
     * IPS 收取的手续费 
     */
    private String ipsFee;
    /**
     * 充值银行  
     */
    private String trdBnkCode;
    
    private String memo1;
    
    private String memo2;
    
    private String memo3;

    /**
     * 回调xml解析封装成对象
     * @param xml
     * @return ipsAutoRecharge
    */
    public IpsAutoRecharge doNotifyCreate(String xml) {
        IpsAutoRecharge ipsAutoRecharge = new IpsAutoRecharge();
        XmlTool tool = new XmlTool();
        tool.SetDocument(xml);
        ipsAutoRecharge.setMerBillNo(tool.getNodeValue("pMerBillNo"));
        ipsAutoRecharge.setTrdDate(tool.getNodeValue("pTrdDate"));
        ipsAutoRecharge.setTrdAmt(tool.getNodeValue("pTrdAmt"));
        ipsAutoRecharge.setMerFee(tool.getNodeValue("pMerFee"));
        ipsAutoRecharge.setIpsFeeType(tool.getNodeValue("pIpsFeeType"));
        ipsAutoRecharge.setAcctType(tool.getNodeValue("pAcctType"));
        ipsAutoRecharge.setIdentNo(tool.getNodeValue("pIdentNo"));
        ipsAutoRecharge.setRealName(tool.getNodeValue("pRealName"));
        ipsAutoRecharge.setIpsAcctNo(tool.getNodeValue("pIpsAcctNo"));
        ipsAutoRecharge.setIpsBillNo(tool.getNodeValue("pIpsBillNo"));
        ipsAutoRecharge.setIpsTime(tool.getNodeValue("pIpsTime"));
        ipsAutoRecharge.setTrdRealAmt(tool.getNodeValue("pTrdRealAmt"));
        ipsAutoRecharge.setIpsFee(tool.getNodeValue("pIpsFee"));
        ipsAutoRecharge.setTrdBnkCode(tool.getNodeValue("pTrdBnkCode"));
        ipsAutoRecharge.setMemo1(tool.getNodeValue("pMemo1"));
        ipsAutoRecharge.setMemo2(tool.getNodeValue("pMemo2"));
        ipsAutoRecharge.setMemo3(tool.getNodeValue("pMemo3"));
        return ipsAutoRecharge;
    } 
    
    private String[] paramNames = new String[]{"MerBillNo","TrdDate","TrdAmt","MerFee","IpsFeeType","AcctType",
            "IdentNo","RealName","IpsAcctNo","S2SUrl","Memo1","Memo2","Memo3"};


    public IpsAutoRecharge() {
        super();
    }

	public IpsAutoRecharge(User user, String ipsFeeType, String acctType,
			double trdAmt, double merFee) {
		super();
		this.setTrdDate(DateUtil.dateStr7(new Date()));
		this.setTrdAmt(XmlTool.format2Str(BigDecimalUtil.round(trdAmt, 2)) + "");
		this.setMerFee(XmlTool.format2Str(BigDecimalUtil.round(merFee, 2)) + "");
		this.setIpsFeeType(ipsFeeType);
		this.setAcctType(acctType);
		if (acctType.equals("1")) {
			this.setIdentNo(acctType);
		}
		this.setRealName(user.getRealName());
		// TODO zjj
		// this.setIpsAcctNo(user.getApiId());
		this.setIpsAcctNo(String.valueOf(user.getUserId()));
		this.setS2SUrl("/public/ips/ipsDoAutoRechargeNotify.html");
	}

    public String getIpsBillNo() {
        return ipsBillNo;
    }

    public void setIpsBillNo(String ipsBillNo) {
        this.ipsBillNo = ipsBillNo;
    }

    public String getIpsTime() {
        return ipsTime;
    }

    public void setIpsTime(String ipsTime) {
        this.ipsTime = ipsTime;
    }

    public String getTrdRealAmt() {
        return trdRealAmt;
    }

    public void setTrdRealAmt(String trdRealAmt) {
        this.trdRealAmt = trdRealAmt;
    }

    public String getIpsFee() {
        return ipsFee;
    }

    public void setIpsFee(String ipsFee) {
        this.ipsFee = ipsFee;
    }

    public String getTrdBnkCode() {
        return trdBnkCode;
    }

    public void setTrdBnkCode(String trdBnkCode) {
        this.trdBnkCode = trdBnkCode;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    public String getMerBillNo() {
        return merBillNo;
    }

    public void setMerBillNo(String merBillNo) {
        this.merBillNo = merBillNo;
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
}
