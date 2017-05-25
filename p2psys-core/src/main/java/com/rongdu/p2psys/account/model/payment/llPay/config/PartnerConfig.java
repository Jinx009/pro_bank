package com.rongdu.p2psys.account.model.payment.llPay.config;

import com.rongdu.p2psys.core.Global;

/**
* 商户配置信息
* @author cgw
* @date:2015-6-3
* @version :1.0
*
*/
public class PartnerConfig{
	
	/**
	 * 银通公钥
	 */
	 private String ytpubKey = Global.getString("LL_YT_PUB_KEY");
	
	/**
	 * 商户私钥
	 */
	 private String traderpriKey = Global.getString("LL_TRADER_PRI_KEY");

	/**
	 * MD5 KEY
	 */
	 private String md5Key = Global.getString("LL_MD5_KEY");

	/**
     * 接收支付异步通知地址
     */
	 private String notifyUrl = Global.getString("LL_NOTIFY_URL");
	 
	 /**
     * 接收提现异步通知地址
     */
	 private String notifyCUrl = Global.getString("LL_NOTIFYC_URL");

    /**
     *  连连支付PC结束后返回地址
     */
    private String urlReturn = Global.getString("LL_URL_RETURN");
    /**
     *  连连支付WAP结束后返回地址
     */
    private String wapurlReturn = Global.getString("LL_WAP_URL_RETURN");
    /**
     *  连连支付WEB收银台支付服务地址
     */
    private String payUrl = Global.getString("LL_PAY_URL"); 
    /**
     *  连连wap端认证支付请求地址
     */
    private String wappayUrl = Global.getString("LL_WAP_PAY_URL");
    /**
     *  连连商户编号
     */
    private String oidPartner = Global.getString("LL_OID_PARTNER");
    /**
     *  连连加密类型
     */
    private String signType = Global.getString("LL_SIGN_TYPE");
    /**
     *  连连系统版本号
     */
    private String version = Global.getString("LL_VERSION");
    /**
     *  业务类型，连连支付根据商户业务为商户开设的业务类型； （101001：虚拟商品销售、109001：实物商品销售、108001：外部账户充值）
     */
    private String busiPartner = Global.getString("LL_BUSI_PARTNER");
    
	public String getYtpubKey() {
		return ytpubKey;
	}
	public void setYtpubKey(String ytpubKey) {
		this.ytpubKey = ytpubKey;
	}
	public String getTraderpriKey() {
		return traderpriKey;
	}
	public void setTraderpriKey(String traderpriKey) {
		this.traderpriKey = traderpriKey;
	}
	public String getMd5Key() {
		return md5Key;
	}
	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public String getNotifyCUrl() {
		return notifyCUrl;
	}
	public void setNotifyCUrl(String notifyCUrl) {
		this.notifyCUrl = notifyCUrl;
	}
	public String getUrlReturn() {
		return urlReturn;
	}
	public void setUrlReturn(String urlReturn) {
		this.urlReturn = urlReturn;
	}
	public String getWapurlReturn() {
		return wapurlReturn;
	}
	public void setWapurlReturn(String wapurlReturn) {
		this.wapurlReturn = wapurlReturn;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getWappayUrl() {
		return wappayUrl;
	}
	public void setWappayUrl(String wappayUrl) {
		this.wappayUrl = wappayUrl;
	}
	public String getOidPartner() {
		return oidPartner;
	}
	public void setOidPartner(String oidPartner) {
		this.oidPartner = oidPartner;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getBusiPartner() {
		return busiPartner;
	}
	public void setBusiPartner(String busiPartner) {
		this.busiPartner = busiPartner;
	}
    
}
