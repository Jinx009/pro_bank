package com.rongdu.p2psys.tpp.ips.model;

import com.rongdu.p2psys.tpp.ips.tool.XmlTool;

/**
 * 登记担保方开户
 * @author lx
 *
 */
public class IpsRegisterGuarantor extends IpsModel {
    /**
     * 商户日期  否  格式：yyyyMMdd
     */
	private String merDate;
	/**
	 * 标的号  否  字母和数字，如a~z,A~Z,0~9
	 */
	private String bidNo;
	/**
	 * 担保金额  否  金额单位元，不能为负，不允许为0
    担保人针对该合同标的承诺的最高赔付金额
	 */
	private String amount;
	/**
     * 担保保证金  否  金额单位元，不能为负，允许为0
    担保人针对该合同标的被冻结的金额
     */
	private String marginAmt;
	/**
     * 金额单位元，不能为负，允许为0
     */
	private String proFitAmt;
	/**
     *  担保收益  否  金额单位元，不能为负，允许为0
     */
	private String acctType;
	/**
     * 担保方证件号码  否  针对担保方类型为1时：真实身份证（个人）
    针对担保方类型为0时：由IPS颁发的商户号
     */
	private String fromIdentNo;
	/**
     * 担保方账户姓名  否  针对担保方类型为1时：担保方账户真实姓名
    针对担保方类型为0时：在IPS开户时登记的商户名称
     */
	private String accountName;
	/**
     * 担保方账户  否  担保方类型为1时，IPS托管账户号（个人）
    担保方类型为0时，由IPS颁发的商户号
     */
    private String account;
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
    private String[] paramNames = new String[]{"MerBillNo","MerDate","BidNo","Amount","MarginAmt","ProFitAmt","AcctType",
            "FromIdentNo","AccountName","Account","WebUrl","S2SUrl","Memo1","Memo2","Memo3"};
    
    /**
     * 回调xml解析封装成对象
     * @param xml
     * @return ipsRegisterGuarantor
    */
    public IpsRegisterGuarantor doReturnCreate(String xml) {
        IpsRegisterGuarantor ipsRegisterGuarantor = new IpsRegisterGuarantor();
        XmlTool tool = new XmlTool();
        tool.SetDocument(xml);
        ipsRegisterGuarantor.setMerDate(tool.getNodeValue("pMerDate"));
        ipsRegisterGuarantor.setBidNo(tool.getNodeValue("pBidNo"));
        ipsRegisterGuarantor.setAmount(tool.getNodeValue("pAmount"));
        ipsRegisterGuarantor.setMarginAmt(tool.getNodeValue("pMarginAmt"));
        ipsRegisterGuarantor.setProFitAmt(tool.getNodeValue("pProFitAmt"));
        ipsRegisterGuarantor.setAcctType(tool.getNodeValue("pAcctType"));
        ipsRegisterGuarantor.setFromIdentNo(tool.getNodeValue("pFromIdentNo"));
        ipsRegisterGuarantor.setAccountName(tool.getNodeValue("pAccountName"));
        ipsRegisterGuarantor.setAccount(tool.getNodeValue("pAccount"));
        ipsRegisterGuarantor.setWebUrl(tool.getNodeValue("pWebUrl"));
        ipsRegisterGuarantor.setS2SUrl(tool.getNodeValue("pS2SUrl"));
        ipsRegisterGuarantor.setMemo1(tool.getNodeValue("pMemo1"));
        ipsRegisterGuarantor.setMemo2(tool.getNodeValue("pMemo2"));
        ipsRegisterGuarantor.setMemo3(tool.getNodeValue("pMemo3"));
        ipsRegisterGuarantor.setRealFreezeAmt(tool.getNodeValue("pRealFreezeAmt"));
        ipsRegisterGuarantor.setCompenAmt(tool.getNodeValue("pCompenAmt"));
        ipsRegisterGuarantor.setIpsTime(tool.getNodeValue("pIpsTime"));
        ipsRegisterGuarantor.setStatus(tool.getNodeValue("pStatus"));
        ipsRegisterGuarantor.setMerBillNo(tool.getNodeValue("pMerBillNo"));
        return ipsRegisterGuarantor;
    } 
    
    
    /***回调时所需参数***/
    /**
     * 实际冻结金额   IPS返回的担保保证金
     */
    private String realFreezeAmt ;
    /**
     *   已代偿金额 IPS返回的担保金额
     */
    private String compenAmt ;
    /**
     * IPS处理时间 
     */
    private String ipsTime;
    /**
     * 担保状态 0：新增  1：进行中(登记成功) 2：处理中 10：结束  9：失败
     */
    private String status;
    
    public String getMerDate() {
        return merDate;
    }
    public void setMerDate(String merDate) {
        this.merDate = merDate;
    }
    public String getBidNo() {
        return bidNo;
    }
    public void setBidNo(String bidNo) {
        this.bidNo = bidNo;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getMarginAmt() {
        return marginAmt;
    }
    public void setMarginAmt(String marginAmt) {
        this.marginAmt = marginAmt;
    }
    public String getProFitAmt() {
        return proFitAmt;
    }
    public void setProFitAmt(String proFitAmt) {
        this.proFitAmt = proFitAmt;
    }
    public String getAcctType() {
        return acctType;
    }
    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }
    public String getFromIdentNo() {
        return fromIdentNo;
    }
    public void setFromIdentNo(String fromIdentNo) {
        this.fromIdentNo = fromIdentNo;
    }
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
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
    public String getRealFreezeAmt() {
        return realFreezeAmt;
    }
    public void setRealFreezeAmt(String realFreezeAmt) {
        this.realFreezeAmt = realFreezeAmt;
    }
    public String getCompenAmt() {
        return compenAmt;
    }
    public void setCompenAmt(String compenAmt) {
        this.compenAmt = compenAmt;
    }
    public String getIpsTime() {
        return ipsTime;
    }
    public void setIpsTime(String ipsTime) {
        this.ipsTime = ipsTime;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String[] getParamNames() {
        return paramNames;
    }
    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }
    
    
    
    
    
}
