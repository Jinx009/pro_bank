package com.rongdu.p2psys.account.model.payment.llPay.model;

import java.io.Serializable;

/***
 * 银行卡代付申请参数列表
 * @author ChenGangwei
 * 2015-05-22
 */
public class WithdrawBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private String 	 platform;   	//平台来源
    private String 	 oid_partner;   //商户编号
    private String 	 sign_type;   	//签名方式
    private String 	 sign;   		//签名
    private String 	 no_order;   	//商户流水号
    private String 	 dt_order;   	//商户时间
    private String 	 money_order;   //代付金额
    private String 	 flag_card;   	//对公对私标志，0-对私 1 –对公
    private String 	 card_no;   	//银行账号
    private String 	 acct_name;   	//用户银行账号姓名
    private String 	 bank_code;   	//银行编码
    private String 	 province_code; //开户行所在省编码
    private String 	 city_code;   	//开户行所在市编码
    private String 	 brabank_name;  //开户支行名称
    private String 	 info_order;   	//订单描述
    private String 	 notify_url;   	//代付结果服务器异步通知地址
    private String 	 api_version;   //版本号
    private String 	 prcptcd;   	//大额行号
    
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getOid_partner() {
		return oid_partner;
	}
	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getNo_order() {
		return no_order;
	}
	public void setNo_order(String no_order) {
		this.no_order = no_order;
	}
	public String getDt_order() {
		return dt_order;
	}
	public void setDt_order(String dt_order) {
		this.dt_order = dt_order;
	}
	public String getMoney_order() {
		return money_order;
	}
	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}
	public String getFlag_card() {
		return flag_card;
	}
	public void setFlag_card(String flag_card) {
		this.flag_card = flag_card;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getAcct_name() {
		return acct_name;
	}
	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}
	public String getBank_code() {
		return bank_code;
	}
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	public String getProvince_code() {
		return province_code;
	}
	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public String getBrabank_name() {
		return brabank_name;
	}
	public void setBrabank_name(String brabank_name) {
		this.brabank_name = brabank_name;
	}
	public String getInfo_order() {
		return info_order;
	}
	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getApi_version() {
		return api_version;
	}
	public void setApi_version(String api_version) {
		this.api_version = api_version;
	}
	public String getPrcptcd() {
		return prcptcd;
	}
	public void setPrcptcd(String prcptcd) {
		this.prcptcd = prcptcd;
	}
    
}
