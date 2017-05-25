package com.rongdu.p2psys.tpp.ips.model;

import java.io.Serializable;

import com.rongdu.p2psys.tpp.ips.tool.XmlTool;

public class IpsCollection extends IpsModel implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 标的编号 */
    private String creMerBillNo;
    
    /** 转入方 IPS 托管账户号 */
    private String inAcctNo;
    
    /** 转入方手续费 */
    private String inFee;
   
    /** 转出方手续费  */
    private String outInfoFee;
    
    /** 转入金额  */
    private String  inAmt;
    
    /** 转入状态:Y#还款成功；N#还款失败  */
    private String status;
    
    /** 转入结果说明 */
    private String message;

    /** 输入参数 */
    private String[] paramNames = new String[]{"CreMerBillNo","InAcctNo","InFee","OutInfoFee","InAmt"};
    
    /**
     * 输出参数解析：回调xml解析封装成对象
     * @param xml
     * @return ipsCollection
    */
    public IpsCollection doReturnCreate(String xml) {
        IpsCollection ipsCollection = new IpsCollection();
        XmlTool tool = new XmlTool();
        tool.SetDocument(xml);
        ipsCollection.setMessage(tool.getNodeValue("pMessage"));
        ipsCollection.setCreMerBillNo(tool.getNodeValue("pCreMerBillNo"));
        ipsCollection.setInAcctNo(tool.getNodeValue("pInAcctNo"));
        ipsCollection.setInFee(tool.getNodeValue("pInFee"));
        ipsCollection.setStatus(tool.getNodeValue("pStatus"));
        return ipsCollection;
    } 

    public String getCreMerBillNo() {
        return creMerBillNo;
    }

    public void setCreMerBillNo(String creMerBillNo) {
        this.creMerBillNo = creMerBillNo;
    }

    public String getInAcctNo() {
        return inAcctNo;
    }

    public void setInAcctNo(String inAcctNo) {
        this.inAcctNo = inAcctNo;
    }

    public String getInFee() {
        return inFee;
    }

    public void setInFee(String inFee) {
        this.inFee = inFee;
    }

    public String getOutInfoFee() {
        return outInfoFee;
    }

    public void setOutInfoFee(String outInfoFee) {
        this.outInfoFee = outInfoFee;
    }

    public String getInAmt() {
        return inAmt;
    }

    public void setInAmt(String inAmt) {
        this.inAmt = inAmt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String[] getParamNames() {
        return paramNames;
    }
    
    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }
}
