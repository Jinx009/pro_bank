package com.rongdu.p2psys.account.model.payment.gopay;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.RechargeUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.constant.RechargeConstant;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.service.PayService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.BeanUtil;

/**
 * 国付宝在线充值
 */
public class Gopay extends BasePayment {
    
    private final static Logger logger = Logger.getLogger(Gopay.class);
    
    /**
     * 网关版本号（必须为2.1）
     */
    private String version;
    
    /**
     * 字符集（1GBK，2UTF-8，默认为1）
     */
    private String charset;
    
    /**
     * 网关语言版本（1 中文，2 英语）
     */
    private String language;
    
    /**
     * 报文加密方式（1 MD5,2SHA，默认为1）
     */
    private String signType;
    
    /**
     * 交易代码（交易类型，支付网关接口必须为8888）
     */
    private String tranCode;
    
    /**
     * 商户号
     */
    private String merchantID;
    
    /**
     * 币种（暂只能为156，代表人民币）
     */
    private String currencyType;
    
    /**
     * 商户前台通知地址（与后台通知地址不能同时为空）
     */
    private String frontMerUrl;
    
    /**
     * 商户后台通知地址（与前台通知地址不能同时为空）
     */
    private String backgroundMerUrl;
    
    /**
     * 交易时间（格式：YYYYMMDDHHMMSS）
     */
    private String tranDateTime;
    
    /**
     * 国付宝转入账户
     */
    private String virCardNoIn;
    
    /**
     * 用户浏览器IP
     */
    private String tranIP;
    
    /**
     * 订单是否允许重复提交（0 不允许，1 允许；默认1）
     */
    private String isRepeatSubmit;
    
    /**
     * 密文串
     */
    private String signValue;
    
    /**
     * 银行代码
     */
    private String bankCode;
    
    /**
     * 用户类型
     */
    private String userType;
    
    private String submitUrl;
    
    //private String serverTimeUrl = "https://gateway.gopay.com.cn/time.do";
    
    private String orderId;
    private String gopayOutOrderId;
    private String servetime;
    private String respCode;
    private String msgExt;
    private String privateKey;
    
    private TranGood good; //封装的商品信息
    
    //国付宝的默认信息:
    public void init(String type, String bankCode, AccountRecharge r) {
        this.payname=RechargeConstant.GOPAY;
        this.version = "2.1";
        this.charset = "2";
        this.language = "1";
        this.signType = "1";
        this.currencyType = "156";
        this.isRepeatSubmit = "1";
        this.tranCode = "8888";
        this.servetime = "";
        String callback = "";
        this.orderPrefix = "G";
        PayService payService = (PayService) BeanUtil.getBean("payService");
        this.pay = payService.findByNid(RechargeConstant.GOPAY);
        submitUrl = pay.getGatewayUrl();
        r.setTradeNo(RechargeUtil.generateTradeNO(r.getUser().getUserId(), this.orderPrefix));
        TranGood good = this.createTranGood(r);
        this.setGood(good);
        this.tranIP=r.getAddIp();
        this.userType=type;
        if (!StringUtil.isBlank(type)) {
            this.userType=type;
        }else{
            this.userType="1";
        }
        if(bankCode != null && bankCode.length() > 0){
            this.bankCode = bankCode;
        }
        if (pay != null) {
            this.privateKey=pay.getGoodsKey();
            this.merchantID=pay.getMerchantId();
            this.virCardNoIn=pay.getIntoAccount();
            String weburl = Global.getValue("weburl");
            callback = weburl + pay.getReturnUrl();
        }
        logger.error("CallbacK:" + callback);
        this.frontMerUrl=callback;
        this.backgroundMerUrl=callback;
        String _v = this.getSignVal();
        logger.error("Sign明文:" + _v);
        this.signValue = getGopaySignValueByMD5(_v);
        logger.error("Sign密文:" + signValue);
    }
    
    private String getSignVal() {
        StringBuffer sb = new StringBuffer();
        sb.append("version=[").append(this.getVersion()).append("]");
        sb.append("tranCode=[").append(this.getTranCode()).append("]");
        sb.append("merchantID=[").append(this.getMerchantID()).append("]");
        sb.append("merOrderNum=[").append(this.getGood().getMerOrderNum()).append("]");
        sb.append("tranAmt=[").append(this.getGood().getTranAmt()).append("]");
        if (StringUtil.isBlank(this.getGood().getFeeAmt())) {
            sb.append("feeAmt=[]");
        } else {
            sb.append("feeAmt=[").append(this.getGood().getFeeAmt()).append("]");
        }
        sb.append("tranDateTime=[").append(this.getGood().getTranDateTime()).append("]");
        sb.append("frontMerUrl=[").append(this.getFrontMerUrl()).append("]");
        sb.append("backgroundMerUrl=[").append(this.getBackgroundMerUrl()).append("]");
        sb.append("orderId=[]");
        sb.append("gopayOutOrderId=[]");
        sb.append("tranIP=[").append(this.getTranIP()).append("]");
        sb.append("respCode=[]");
        sb.append("gopayServerTime=[]");
        sb.append("VerficationCode=[").append(getPrivateKey()).append("]");
        return sb.toString();
    }
    
//    private String getGopayServerTime() {
//        HttpClient httpClient = new HttpClient();
//        httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
//        httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 10000); 
//        GetMethod getMethod = new GetMethod(serverTimeUrl);
//        getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");  
//        // 执行getMethod
//        int statusCode = 0;
//        try {
//            statusCode = httpClient.executeMethod(getMethod);           
//            if (statusCode == HttpStatus.SC_OK){
//                String respString = org.apache.commons.lang.StringUtils.trim((new String(getMethod.getResponseBody(),"UTF-8")));
//                return respString;
//            }           
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            getMethod.releaseConnection();
//        }
//        return null;
//    }
    
    private TranGood createTranGood(AccountRecharge r) {
        TranGood good = new TranGood();
        // 设置交易时间
        Date d = new Date();
        good.setTranDateTime(DateUtil.dateStr3(d));
        // 设置订单号
        good.setMerOrderNum(r.getTradeNo());
        // 设置交易金额
        double tranAamt = r.getMoney();
        double feeAmt = tranAamt * NumberUtil.getDouble(Global.getValue("online_rechargefee"));
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
        nf.setGroupingUsed(false);  
        good.setTranAmt(nf.format(tranAamt));
        if (feeAmt > 0.01) {
            good.setFeeAmt(NumberUtil.format2(feeAmt) + "");
        }
        // 设置交易商品信息
        good.setGoodsName(Global.getValue("webname"));
        good.setGoodsDetail("用户充值：" + r.getMoney() + "RMB");
        good.setBuyerName(Global.getValue("webname"));
        good.setBuyerContact("");
        return good;
    }
    
    public static String getGopaySignValueByMD5(String signvalue){
        String val = "";
        try {
            val = DigestUtils.md5Hex(signvalue.getBytes("UTF-8"));
        } catch (Exception e) {
            logger.error("getGopaySignValueByMD5() has error");
            e.printStackTrace();
        }
        return val;
    }
    
	public void doCallBack(HttpServletRequest request){
        PayService payService = (PayService) BeanUtil.getBean("payService");
        this.pay = payService.findByNid(RechargeConstant.GOPAY);
        this.privateKey=pay.getGoodsKey();
        this.msgExt = request.getParameter("msgExt");
        TranGood tranGood = new TranGood();
        tranGood.setMerOrderNum(request.getParameter("merOrderNum"));
        tranGood.setTranAmt(request.getParameter("tranAmt"));
        tranGood.setFeeAmt(request.getParameter("feeAmt"));
        tranGood.setTranDateTime(request.getParameter("tranDateTime"));
        this.good = tranGood;
	}
	
	@Override
	public boolean reponseSuccess() {
		if ("0000".equals(getRespCode()) && getSignValue().equals(getCallbackMd5SignVal())) {
				return true;
		}
		return false;
	}

    private String getCallbackMd5SignVal(){
        String callbackSignVal = getCallbackSignVal();
        logger.error("Callback Sign明文:" + callbackSignVal);
        String callbackMd5SignVal = getGopaySignValueByMD5(callbackSignVal);
        logger.error("Callback Sign密文:" + callbackMd5SignVal);
        return callbackMd5SignVal;
    }	
    
    private String getCallbackSignVal(){
        StringBuffer sb=new StringBuffer();
        sb.append("version=[").append(this.getVersion()).append("]");
        sb.append("tranCode=[").append(this.getTranCode()).append("]");
        sb.append("merchantID=[").append(this.getMerchantID()).append("]");
        sb.append("merOrderNum=[").append(this.getGood().getMerOrderNum()).append("]");
        sb.append("tranAmt=[").append(this.getGood().getTranAmt()).append("]");
        sb.append("feeAmt=[").append(this.getGood().getFeeAmt()).append("]");
        sb.append("tranDateTime=[").append(this.getGood().getTranDateTime()).append("]");
        sb.append("frontMerUrl=[").append(this.getFrontMerUrl()).append("]");
        sb.append("backgroundMerUrl=[").append(this.getBackgroundMerUrl()).append("]");
        sb.append("orderId=[").append(this.getOrderId()).append("]");
        sb.append("gopayOutOrderId=[").append(this.getGopayOutOrderId()).append("]");
        sb.append("tranIP=[").append(this.getTranIP()).append("]");
        sb.append("respCode=[").append(respCode).append("]");
        sb.append("gopayServerTime=[]");
        sb.append("VerficationCode=[").append(getPrivateKey()).append("]");
        return sb.toString();
    }    
    
	@Override
	public String getTranNo() {
		return good.getMerOrderNum();
	}
	
	@Override
	public String getRemoteReturnSign() {
		return msgExt;
	}	
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getFrontMerUrl() {
        return frontMerUrl;
    }

    public void setFrontMerUrl(String frontMerUrl) {
        this.frontMerUrl = frontMerUrl;
    }

    public String getBackgroundMerUrl() {
        return backgroundMerUrl;
    }

    public void setBackgroundMerUrl(String backgroundMerUrl) {
        this.backgroundMerUrl = backgroundMerUrl;
    }

    public String getTranDateTime() {
        return tranDateTime;
    }

    public void setTranDateTime(String tranDateTime) {
        this.tranDateTime = tranDateTime;
    }

    public String getVirCardNoIn() {
        return virCardNoIn;
    }

    public void setVirCardNoIn(String virCardNoIn) {
        this.virCardNoIn = virCardNoIn;
    }

    public String getTranIP() {
        return tranIP;
    }

    public void setTranIP(String tranIP) {
        this.tranIP = tranIP;
    }

    public String getIsRepeatSubmit() {
        return isRepeatSubmit;
    }

    public void setIsRepeatSubmit(String isRepeatSubmit) {
        this.isRepeatSubmit = isRepeatSubmit;
    }

    public String getSignValue() {
        return signValue;
    }

    public void setSignValue(String signValue) {
        this.signValue = signValue;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGopayOutOrderId() {
        return gopayOutOrderId;
    }

    public void setGopayOutOrderId(String gopayOutOrderId) {
        this.gopayOutOrderId = gopayOutOrderId;
    }

    public String getServetime() {
        return servetime;
    }

    public void setServetime(String servetime) {
        this.servetime = servetime;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public TranGood getGood() {
        return good;
    }

    public void setGood(TranGood good) {
        this.good = good;
    }
}
