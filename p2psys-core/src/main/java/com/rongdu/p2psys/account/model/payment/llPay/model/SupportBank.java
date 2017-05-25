package com.rongdu.p2psys.account.model.payment.llPay.model;


/**
* 支持银行参数信息
* @author cgw
* @date:2015-09-01
* @version :1.0
*
*/
public class SupportBank{
    private static final long serialVersionUID = 1L;
    
    private String oid_partner; //商户编号
    private String api_version;   //版本号
    private String sign_type; 	//签名方式
    private String sign; 		//签名
    private String bank_code; 		//所属银行编号
	private String card_type; 		//银行卡类型,2借记卡，3信用卡
	private String product_type; 	//支付方式,1认证支付
	private String pay_chnl;  	//支付渠道类型，13PCweb端，16wap端
	
	/**
	 * 查询单个银行额度信息
	 * @param bank_code
	 * @param pay_chnl
	 */
	public SupportBank(String bank_code,String pay_chnl) {
		this.bank_code = bank_code;
		this.pay_chnl = pay_chnl;
	}
	
	/**
	 * 查询所有银行额度信息
	 * @param pay_chnl
	 */
	public SupportBank(String pay_chnl) {
		this.pay_chnl = pay_chnl;
	}
	
	public String getOid_partner() {
		return oid_partner;
	}
	public void setOid_partner(String oid_partner) {
		this.oid_partner = oid_partner;
	}
	public String getApi_version() {
		return api_version;
	}
	public void setApi_version(String api_version) {
		this.api_version = api_version;
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
	public String getBank_code() {
		return bank_code;
	}
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	public String getCard_type() {
		return card_type;
	}
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public String getPay_chnl() {
		return pay_chnl;
	}
	public void setPay_chnl(String pay_chnl) {
		this.pay_chnl = pay_chnl;
	}
    
}
