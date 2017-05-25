package com.rongdu.p2psys.account.model.payment.unspay;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.RechargeUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.constant.RechargeConstant;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.service.PayService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.BeanUtil;

public class Unspay extends BasePayment{
    
    private static Logger logger = Logger.getLogger(Unspay.class);

    private String version="3.0.0"; //接口版本号
    
    private String time="";//订单时间
    
    private String remark="";//备注，附加信息
    
    private String mac="";//MAC值
    
    private String bankCode="";//支付方式（通过代码区分）
    
    private String b2b="false";//是否B2B网上银行支付
    
    private String merchantId="";//商户编号
    
    private String merchantUrl="";//商户接收银生反馈数据的响应url
    
    private String responseMode="3";//商户要求银生反馈信息的响应方式
    
    private String orderId="";//订单号
    
    private String currencyType="CNY";//货币类型
    
    private String amount;//支付金额
    
    private String assuredPay="";//是否通过银生担保支付
    
    private String commodity="";//商品名称
    
    private String orderUrl="";//订单url
    
    private String merchantKey=""; 
    
    private String submitUrl;
    
    /** 请求的参数 */
    private SortedMap parameters=new TreeMap() ;
    
    @SuppressWarnings("static-access")
    public void init(String bankCode, AccountRecharge r){
        String weburl = Global.getValue("weburl");
        PayService payService = (PayService) BeanUtil.getBean("payService");
        this.pay = payService.findByNid(RechargeConstant.UNSPAY);
        this.orderPrefix="U";
        this.tranNo = RechargeUtil.generateTradeNO(r.getUser().getUserId(), this.orderPrefix);
        String nowTime=DateUtil.dateStr3(DateUtil.getNowTimeStr());
        StringBuffer s = new StringBuffer();
        s.append("merchantId=").append(this.pay.getMerchantId());
        s.append("&merchantUrl=").append(weburl+this.pay.getReturnUrl());
        s.append("&responseMode=").append(responseMode);
              s.append("&orderId=").append(tranNo);
        s.append("&currencyType=").append(currencyType);
        s.append("&amount=").append(r.getMoney());
        s.append("&assuredPay=").append(assuredPay);
        s.append("&time=").append(nowTime);
        s.append("&remark=").append(remark);
        s.append("&merchantKey=").append(this.pay.getGoodsKey());
        logger.info("macString==="+s);
        //md5加密
        String mac = new MD5().getMD5ofStr(s.toString());       
        logger.info("mac 加密======="+mac);
        this.mac=mac;
        this.orderId=tranNo;
        this.merchantId=this.pay.getMerchantId();               //商户号
        this.merchantUrl=weburl+pay.getReturnUrl();         //交易完成后跳转的URL
        this.orderUrl=weburl+pay.getRequestUrl();            //接收后台通知的URL
        this.amount=r.getMoney()+"";
        this.time=nowTime;                    //直连银行参数，例子是直接转跳到招商银行时的参数
        this.merchantKey=this.pay.getGoodsKey();
        this.bankCode=bankCode;
        this.commodity=Global.getValue("webname");
        StringBuffer ss = new StringBuffer();
        ss.append("&merchantId=").append(this.pay.getMerchantId());
        ss.append("&merchantUrl=").append(weburl+this.pay.getReturnUrl());
        ss.append("&responseMode=").append(responseMode);
        ss.append("&orderId=").append(this.tranNo);
        ss.append("&currencyType=").append(currencyType);
        ss.append("&amount=").append(amount);
        ss.append("&assuredPay=").append(assuredPay);
        ss.append("&time=").append(nowTime);
        ss.append("&remark=").append(remark);
        ss.append("&mac=").append(mac);
        this.submitUrl=this.pay.getGatewayUrl()+ss.toString();
        logger.info("submitUrl======="+submitUrl);
    }

    
    /**
     * 设置参数值
     * 
     * @param parameter
     *            参数名称
     * @param parameterValue
     *            参数值
     */
    public void setParameter(String parameter, String parameterValue) {
        String v = "";
        if (null != parameterValue) {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }
    /**
     * 获取参数值
     * @param parameter 参数名称
     * @return String 
     */
    public String getParameter(String parameter) {
        String s = (String)this.parameters.get(parameter); 
        return (null == s) ? "" : s;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    
    public String getB2b() {
        return b2b;
    }

    public void setB2b(String b2b) {
        this.b2b = b2b;
    }

    public String getResponseMode() {
        return responseMode;
    }

    public String getMerchantId() {
        return merchantId;
    }
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
    public String getMerchantUrl() {
        return merchantUrl;
    }
    public void setMerchantUrl(String merchantUrl) {
        this.merchantUrl = merchantUrl;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getCurrencyType() {
        return currencyType;
    }
    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
    public String getAssuredPay() {
        return assuredPay;
    }
    public void setAssuredPay(String assuredPay) {
        this.assuredPay = assuredPay;
    }
    public String getCommodity() {
        return commodity;
    }
    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }
    public String getOrderUrl() {
        return orderUrl;
    }
    public void setOrderUrl(String orderUrl) {
        this.orderUrl = orderUrl;
    }
    

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        Unspay.logger = logger;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }

    public SortedMap getParameters() {
        return parameters;
    }

    public void setParameters(SortedMap parameters) {
        this.parameters = parameters;
    }

    public void setResponseMode(String responseMode) {
        this.responseMode = responseMode;
    }
}
