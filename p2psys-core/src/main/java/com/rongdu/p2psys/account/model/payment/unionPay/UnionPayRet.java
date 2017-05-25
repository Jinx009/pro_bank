package com.rongdu.p2psys.account.model.payment.unionPay;

/**
 * 银联在线响应参数
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月7日
 */
public class UnionPayRet {
	
	/**
	 * 返回码
	 */
	private String retCode;

	/**
	 * 失败返回错误描述
	 */
	private String retDesc;

	/**
	 * 成功返回银行卡唯一绑定号（绑卡接口/解绑银行卡接口）
	 */
	private String bindId;

	/**
	 * 银行卡号（查询绑定银行卡信息接口）
	 */
	private String cardNo;

	/**
	 * 持卡人姓名（查询绑定银行卡信息接口）
	 */
	private String accName;

	/**
	 * CVN安全码，信用卡后三位安全码（查询绑定银行卡信息接口）
	 */
	private String cvn;

	/**
	 * 信用卡有效期（查询绑定银行卡信息接口）
	 */
	private String expire;

	/**
	 * 身份证号码（查询绑定银行卡信息接口）
	 */
	private String accId;

	/**
	 * 手机号码（查询绑定银行卡信息接口）
	 */
	private String mobile;

	/**
	 * 创建时间(查询绑定银行卡信息接口)
	 */
	private String createDateTime;

	/**
	 * 商户请求订单编号（扣款接口/付款接口）
	 */
	private String orderNo;

	/**
	 * 订单状态 0:已接受, 1:处理中,2:处理成功,3:处理失败（扣款接口/付款接口）
	 */
	private int orderStatus;

	/**
	 * 系统处理日期（扣款接口/付款接口）
	 */
	private String processDate;

	/**
	 * 查询订单详情（订单查询接口）
	 */
	private String orderDetail;

	/**
	 * 金额，整数并以分为单位（备付金查询接口）
	 */
	private long amount;

	/**
	 * 银行编号（获取银行卡信息接口）
	 */
	private String bankId;

	/**
	 * 银行名称（获取银行卡信息接口）
	 */
	private String bankName;

	/**
	 * 卡名称（获取银行卡信息接口）
	 */
	private String cardName;

	/**
	 * 卡类型，直接返回借记卡/贷记卡（获取银行卡信息接口）
	 */
	private String cardType;

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetDesc() {
		return retDesc;
	}

	public void setRetDesc(String retDesc) {
		this.retDesc = retDesc;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getCvn() {
		return cvn;
	}

	public void setCvn(String cvn) {
		this.cvn = cvn;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getProcessDate() {
		return processDate;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	public String getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
}

