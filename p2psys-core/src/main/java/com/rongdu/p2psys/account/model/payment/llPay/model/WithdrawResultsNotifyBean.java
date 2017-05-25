package com.rongdu.p2psys.account.model.payment.llPay.model;

import java.io.Serializable;

/***
 * 银行卡代付结果通知参数列表
 * @author ChenGangwei
 * 2015-05-22
 */
public class WithdrawResultsNotifyBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private String 	 oid_partner;   //商户编号
    private String 	 sign_type;   	//签名方式
    private String 	 sign;   		//签名
    private String 	 no_order;   	//商户代付流水号
    private String 	 dt_order;   	//商户代付时间
    private String 	 money_order;   //代付金额
    private String 	 oid_paybill;   //连连支付支付单
    private String 	 info_order;   //订单描述
    private String 	 result_pay;   //代付结果
    private String 	 settle_date;   //清算日期
    
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
	public String getOid_paybill() {
		return oid_paybill;
	}
	public void setOid_paybill(String oid_paybill) {
		this.oid_paybill = oid_paybill;
	}
	public String getMoney_order() {
		return money_order;
	}
	public void setMoney_order(String money_order) {
		this.money_order = money_order;
	}
	public String getInfo_order() {
		return info_order;
	}
	public void setInfo_order(String info_order) {
		this.info_order = info_order;
	}
	public String getResult_pay() {
		return result_pay;
	}
	public void setResult_pay(String result_pay) {
		this.result_pay = result_pay;
	}
	public String getSettle_date() {
		return settle_date;
	}
	public void setSettle_date(String settle_date) {
		this.settle_date = settle_date;
	}
	
}
