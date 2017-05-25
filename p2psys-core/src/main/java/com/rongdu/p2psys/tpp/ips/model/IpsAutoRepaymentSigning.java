package com.rongdu.p2psys.tpp.ips.model;

import java.io.Serializable;

import com.rongdu.p2psys.tpp.ips.tool.XmlTool;


/**
 * 环迅：自动还款签约处理Model
 * @author zhangyz
 */
public class IpsAutoRepaymentSigning extends IpsModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 商户签约订单号， 商户系统唯一不重复
     */
    private String merBillNo;
    
    /**
     * 签约日期  格式：YYYYMMDD
     */
    private String signingDate;
    
    /**
     * 证件类型  否  1#身份证，默讣：1
     */
    private String identType;
    
    /**
     * 借款人证件号码 真实身份证
     */
    private String identNo;
    
    /**
     * 借款人姓名  真实姓名
     */
    private String realName;
    
    /**
     * 借款人IPS账号  由IPS生成颁发的资金账号。
     */
    private String ipsAcctNo;
    
    /**
     * 有效期类型  D代表天，M代表月，N长期有效。   本期只支持 N 长期有效
     */
    private String validType;
    
    /**
     * 有效期,根据有效期类型,传入值限制如下：
     * D：  以天计算有效期，数值范围  1–360任选 
     * M：  以月计算有效期，数值范围 1–12任选
     * N: 0
     */
    private String validDate;

    /**
     * 状态返回地址1 
     */
    private String webUrl;

    /**
     * 状态返回地址2 
     */
    private String s2SUrl;
    
    /**
     * Ips 订单号
     */
    private String P2PBillNo;
    
    /**
     * 授权号
     */
    private String ipsAuthNo;
    
    /**
     * 处理时间  格式为：yyyyMMddHHmmss
     */
    private String ipsTime;
    
    private String memo1;
    
    private String memo2;
    
    private String memo3;

    /**
     * 输入参数
     */
    private String[] paramNames = new String[]{"MerBillNo","SigningDate","IdentType","IdentNo","RealName",
                                               "IpsAcctNo","ValidType","ValidDate","WebUrl","S2SUrl","Memo1","Memo2","Memo3"};

    /**
     * 回调xml解析封装成对象
     * @param xml
     * @return ipsAutoRepayment
    */
    public IpsAutoRepaymentSigning doReturnCreate(String xml) {
        IpsAutoRepaymentSigning ipsAutoRepayment = new IpsAutoRepaymentSigning();
        XmlTool tool = new XmlTool();
        tool.SetDocument(xml);
        ipsAutoRepayment.setMerBillNo(tool.getNodeValue("pMerBillNo"));
        ipsAutoRepayment.setSigningDate(tool.getNodeValue("pSigningDate"));
        ipsAutoRepayment.setP2PBillNo(tool.getNodeValue("pP2PBillNo"));
        ipsAutoRepayment.setIdentType(tool.getNodeValue("pIdentType"));
        ipsAutoRepayment.setIdentNo(tool.getNodeValue("pIdentNo"));
        ipsAutoRepayment.setRealName(tool.getNodeValue("pRealName"));
        ipsAutoRepayment.setIpsAcctNo(tool.getNodeValue("pIpsAcctNo"));
        ipsAutoRepayment.setIpsAuthNo(tool.getNodeValue("pIpsAuthNo"));
        ipsAutoRepayment.setValidDate(tool.getNodeValue("pValidDate"));
        ipsAutoRepayment.setIpsTime(tool.getNodeValue("pIpsTime"));
        ipsAutoRepayment.setMemo1(tool.getNodeValue("pMemo1"));
        ipsAutoRepayment.setMemo2(tool.getNodeValue("pMemo2"));
        ipsAutoRepayment.setMemo3(tool.getNodeValue("pMemo3"));
        return ipsAutoRepayment;
    }
    
    public String getMerBillNo() {
        return merBillNo;
    }

    public void setMerBillNo(String merBillNo) {
        this.merBillNo = merBillNo;
    }

    public String getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(String signingDate) {
        this.signingDate = signingDate;
    }

    public String getIdentType() {
        return identType;
    }

    public void setIdentType(String identType) {
        this.identType = identType;
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

    public String getValidType() {
        return validType;
    }

    public void setValidType(String validType) {
        this.validType = validType;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
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

    public String getP2PBillNo() {
        return P2PBillNo;
    }

    public void setP2PBillNo(String p2pBillNo) {
        P2PBillNo = p2pBillNo;
    }

    public String getIpsAuthNo() {
        return ipsAuthNo;
    }

    public void setIpsAuthNo(String ipsAuthNo) {
        this.ipsAuthNo = ipsAuthNo;
    }

    public String getIpsTime() {
        return ipsTime;
    }

    public void setIpsTime(String ipsTime) {
        this.ipsTime = ipsTime;
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
}
