package com.rongdu.p2psys.account.model.payment.baofoo;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.RechargeUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.constant.RechargeConstant;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.payment.BasePayment;
import com.rongdu.p2psys.account.service.PayService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.util.BeanUtil;

/**
 * 宝付充值直联
 */
public class BaoFooPay extends BasePayment{
    
    /**
     * 商户号
     */
    private String memberID;
    
    /**
     * 终端号
     */
    private String terminalID;
    
    /**
     * 接口版本号
     */
    private String interfaceVersion;
    
    /**
     * 加密类型  Md5:1
     */
    private int keyType;
    
    /**
     * 选择支付方式  这里默认使用银联  编号 是：1080
     */
    private String payID;
    
    /**
     * 交易日期（格式：YYYYMMDDHHmmss）
     */
    private String tradeDate;
    
    /**
     * 商户流水号
     */
    private String transID;
    
    /**
     * 订单金额
     */
    private String orderMoney;
    
    /**
     * 商品名称: 默认为空  必要字段
     */
    private String productName = "";
    
    /**
     * 商品数量  默认为 1  便于计算。
     */
    private String amount = "1";
    
    /**
     * 支付用户名: 默认为空  必要字段
     */
    private String username = "";
    
    /**
     * 订单附加消息: 默认为空  必要字段
     */
    private String additionalInfo = "";
    
    /**
     * 通知商户地址
     */
    private String pageUrl;
    
    /**
     * 底层通知地址
     */
    private String returnUrl;
    
    /**
     * Md5签名
     */
    private String signature;
    
    /**
     * 支付结果 1 成功; 0 失败
     */
    private String result;
    
    /**
     * 支付结果描述
     */
    private String resultDesc;
    
    /**
     * 实际成交金额
     */
    private String factMoney;
    
    /**
     * 交易成功时间
     */
    private String succTime;
    
    /**
     * Md5签名字段
     */
    private String md5Sign;    
    
    /**
     * 通知方式（默认为0）
     * 1：服务器通知和页面通知。支付成功后，自动重定向到“通知商户地址”
     * 0：只发服务器端通知，不跳转
     */
    private int noticeType;
    
    // 提交地址
    private String submitUrl;
    
    public void init(AccountRecharge r, String bankCode){

        PayService payService = (PayService) BeanUtil.getBean("payService");
        this.pay = payService.findByNid(RechargeConstant.BAOFOO_PAY);
        String weburl = Global.getValue("weburl");
        this.orderPrefix = "B";
        this.payname=RechargeConstant.BAOFOO_PAY;
        if (pay != null) {
            // 页面跳转地址
            this.pageUrl= weburl +pay.getReturnUrl();
            // 分配的商户号
            this.memberID=pay.getMerchantId();
            // 底层通知 url
            this.returnUrl=weburl +pay.getRequestUrl();
            // 终端号
            this.terminalID=pay.getTerminalId();
        }

        // 通知 方式 1 是 服务器通知 和 页面跳转
        this.noticeType=1;
        // 订单金额 必须换算成 分
        String orderMoney;
        if (r.getMoney() > 0) {
            double a = r.getMoney() * 100; // 使用分进行提交
            Double d = new Double(a);
            int minMoney = d.intValue();
            orderMoney = String.valueOf(minMoney);
        } else {
            orderMoney = "0";
        }
        this.orderMoney=orderMoney;
        // 支付方式 使用默认的银联模式
        if(bankCode != null && bankCode.length() > 0){
            this.payID=bankCode;
        }else{
            this.payID="";
        }
        // 订单交易的 交易时间
        this.tradeDate=DateUtil.dateStr3(new Date());
        // 生成商户流水号
        this.transID=RechargeUtil.generateTradeNO(r.getUser().getUserId(), this.orderPrefix);
        // 生成 签名字段
        String Md5key = "";
        if(pay != null){
             Md5key = pay.getGoodsKey(); // md5密钥（KEY）
        }
        String MARK = "|";
      //MD5签名格式
        String md5SignValue = new String(this.memberID + MARK + this.payID + MARK 
                + this.tradeDate + MARK + this.transID + MARK + this.orderMoney + MARK 
                + this.pageUrl + MARK + this.returnUrl + MARK + this.noticeType + MARK 
                + Md5key);
        this.signature = md5Sign(md5SignValue);// 计算MD5值
        this.interfaceVersion="4.0"; // 接口版本号
        this.keyType=1; // 加密类型
        this.submitUrl = pay.getGatewayUrl();
    }
    
	public void doCallBack(HttpServletRequest request){
        PayService payService = (PayService) BeanUtil.getBean("payService");
        this.pay = payService.findByNid(RechargeConstant.BAOFOO_PAY);
	}
	
	@Override
	public boolean reponseSuccess() {
		String signMD5 = md5Sign(callbackSign());
		if (signMD5.equals(md5Sign) && "1".equals(result)) {
				return true;
		}
		return false;
	}
	
	private String md5Sign(String value){
		String signedValue = MD5.getMD5ofStr(value);
		return signedValue;
	}
	
	private String callbackSign(){
        // 生成 签名字段
        String Md5key = "";
        if(pay != null){
             Md5key = pay.getGoodsKey(); // md5密钥（KEY）
        }
		String MARK = "~|~";
		String content = "MemberID=" + memberID + MARK + "TerminalID=" + terminalID + MARK + "TransID=" 
				+ transID + MARK + "Result=" + result + MARK + "ResultDesc=" + resultDesc + MARK
				+ "FactMoney=" + factMoney + MARK + "AdditionalInfo=" + additionalInfo + MARK 
				+ "SuccTime=" + succTime + MARK + "Md5Sign=" + Md5key;
		return content;
	}
	
	@Override
	public String getTranNo() {
		return transID;
	}
	
	@Override
	public String getRemoteReturnSign() {
		return resultDesc;
	}	

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public String getPayID() {
        return payID;
    }

    public void setPayID(String payID) {
        this.payID = payID;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getFactMoney() {
		return factMoney;
	}

	public void setFactMoney(String factMoney) {
		this.factMoney = factMoney;
	}

	public String getSuccTime() {
		return succTime;
	}

	public void setSuccTime(String succTime) {
		this.succTime = succTime;
	}

	public String getMd5Sign() {
		return md5Sign;
	}

	public void setMd5Sign(String md5Sign) {
		this.md5Sign = md5Sign;
	}
}
