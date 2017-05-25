package com.rongdu.p2psys.account.model.payment.llPay.model;

/***
 * 用户签约信息查询请求参数
 * @author ChenGangwei
 * 2015-5-28
 */
public class UserBankCard {

	//基本参数
	private String oid_partner; //商户编号
	private String user_id; 	//商户用户唯一编号
	private String platform; 	//平台来源标示
	private String pay_type; 	//支付方式
	private String sign_type; 	//签名方式
	private String sign; 		//签名
	//业务参数
	private String no_agree; 	//签约协议号
	private String card_no; 	//签约银行卡号
	private String offset; 		//查询偏移量
	

	public String getOid_partner() {
		return oid_partner;
	}
	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
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
	public String getNo_agree() {
		return no_agree;
	}
	public void setNo_agree(String no_agree) {
		this.no_agree = no_agree;
	}
	public String getCard_no() {
		return card_no;
	}
	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}

}
