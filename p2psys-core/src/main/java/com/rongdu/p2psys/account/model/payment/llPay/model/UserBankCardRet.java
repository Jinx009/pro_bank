package com.rongdu.p2psys.account.model.payment.llPay.model;

/***
 * 用户签约信息查询返回结果
 * @author ChenGangwei
 * 2015-5-28
 */
public class UserBankCardRet {
	
	//基本参数
	private String ret_code; 		//交易结果代码
	private String ret_msg; 		//交易结果描述
	private String user_id; 		//商户用户唯一编号
	private String count; 			//总记录条数
	private String agreement_list; 	//结果集
	private String sign_type; 		//签名方式
	private String sign; 			//签名
	//以下为结果集agreement_list，数据按签约时间倒叙，最近签约的银行卡排第一
	private String no_agree; 		//签约协议号
	private String card_no; 		//银行卡号后4位
	private String bank_code; 		//所属银行编号
	private String bank_name; 		//所属银行名称
	private String card_type; 		//银行卡类型
	private String bind_mobile; 	//绑定手机号码
	
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getAgreement_list() {
		return agreement_list;
	}
	public void setAgreement_list(String agreement_list) {
		this.agreement_list = agreement_list;
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
	public String getBank_code() {
		return bank_code;
	}
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getBind_mobile() {
		return bind_mobile;
	}
	public void setBind_mobile(String bind_mobile) {
		this.bind_mobile = bind_mobile;
	}

}
