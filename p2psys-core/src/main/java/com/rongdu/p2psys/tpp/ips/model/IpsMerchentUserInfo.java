package com.rongdu.p2psys.tpp.ips.model;

import org.apache.log4j.Logger;

import com.ips.security.utility.IpsCrypto;

public class IpsMerchentUserInfo extends IpsModel{

    
    private static Logger logger = Logger.getLogger(IpsMerchentUserInfo.class);
    /**
     * IPS 资金托管账号
     */
    private String argIpsAccount="";

    //以下为返回的参数
    
    /**
     * 由IPS 颁发的商户号
     */
    private String pMerCode="";
    
    /**
     * IPS 账户号
     */
    private String pIpsAcctNo="";
    
    /**
     * 开户时注册邮箱
     */
    private String pEmail="";
    
    /**
     * 账户开通状态
     * 01 未开户；02 开户成功未激活账户；03 开户失败；04
                    已激活账户；05 已删除
     */
    private String pStatus="";
    
    /**
     * 身份证验证状态
     * 01 未验证；02 验证通过，03 验证不通过
     */
    private String pUCardStatus="";
    
    /**
     * 提现银行名称
     */
    private String pBankName="";
    /**
     * 银行卡号后四位
     */
    private String pBankCard="";
    
    /**
     * 银行卡状态
     * 01 未登记；02 已登记03 登记失败
     */
    private String pBCardStatus="";
    
    /**
     * 代扣签约状态
     * 01 签约申请中 02 签约处理中 03 签约失败（签约拒绝）
     */
    private String pSignStatus="";
 
    
    
    
    public IpsMerchentUserInfo()
    {
        super();
    }
  
    /**
     * 覆盖基类，基类没有下面类型的方法
     * 创建数字签名
     * 
     */
    public void createSign() {

        String sign = IpsCrypto.md5Sign(super.getMerCode() +argIpsAccount+super.getCert_md5());
        super.setSign(sign);
        String[][] commitParams = new String[][]{{"argMerCode","argIpsAccount", "argSign","argMemo"}, {super.getMerCode(), argIpsAccount,sign,"bank"}};
        super.setCommitParams(commitParams);
        
    }


    
    
    
    public String getArgIpsAccount() {
        return argIpsAccount;
    }


    public void setArgIpsAccount(String argIpsAccount) {
        this.argIpsAccount = argIpsAccount;
    }


    public String getpMerCode() {
        return pMerCode;
    }


    public void setpMerCode(String pMerCode) {
        this.pMerCode = pMerCode;
    }


    public String getpIpsAcctNo() {
        return pIpsAcctNo;
    }


    public void setpIpsAcctNo(String pIpsAcctNo) {
        this.pIpsAcctNo = pIpsAcctNo;
    }


    public String getpEmail() {
        return pEmail;
    }


    public void setpEmail(String pEmail) {
        this.pEmail = pEmail;
    }


    public String getpStatus() {
        return pStatus;
    }


    public void setpStatus(String pStatus) {
        this.pStatus = pStatus;
    }


    public String getpUCardStatus() {
        return pUCardStatus;
    }


    public void setpUCardStatus(String pUCardStatus) {
        this.pUCardStatus = pUCardStatus;
    }


    public String getpBankName() {
        return pBankName;
    }


    public void setpBankName(String pBankName) {
        this.pBankName = pBankName;
    }


    public String getpBankCard() {
        return pBankCard;
    }


    public void setpBankCard(String pBankCard) {
        this.pBankCard = pBankCard;
    }


    public String getpBCardStatus() {
        return pBCardStatus;
    }


    public void setpBCardStatus(String pBCardStatus) {
        this.pBCardStatus = pBCardStatus;
    }


    public String getpSignStatus() {
        return pSignStatus;
    }


    public void setpSignStatus(String pSignStatus) {
        this.pSignStatus = pSignStatus;
    }
    
    
    
}
