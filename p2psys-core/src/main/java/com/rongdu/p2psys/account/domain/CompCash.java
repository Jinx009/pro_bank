package com.rongdu.p2psys.account.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.util.OrderNoUtils;

/**
 * 对公付款银行卡
 * 
 * @author yl
 * @version 2.0
 * @date 2015年4月30日
 */
@Entity
@Table(name = "rd_comp_cash")
public class CompCash implements Serializable {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 网站审核状态：0待审，1审核成功，2审核失败
	 */
	private int webStatus;
	/**
	 * 第三方处理状态：0处理中，1成功，2处理失败
	 */
	private int tppStatus;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 操作金额
	 */
	private double amount;
	
	/**
	 * 银行账户卡号
	 */
	private String cardNo;

	/**
	 * 银行账户名称
	 */
	private String accName;

	/**
	 * 开户行名称
	 */
	private String bankName;

	/**
	 * 开户行代码
	 */
	private String bankCode;
	
	/**
	 * 开户行省
	 */
	private String province;
	
	/**
	 * 开户行市
	 */
	private String city;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 操作员(添加)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "add_operator_id")
	private Operator addOperator;
	
	/**
	 * 操作员（审核)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verify_operator_id")
	private Operator verifyOperator;
	
	/**
	 * 添加时间
	 */
	private Date addTime;

	/**
	 * 审核时间
	 */
	private Date verifyTime;
	
	/**
	 * 添加IP
	 */
	private String addIp;
	
	public CompCash() {
		super();
	}

	public CompCash(int webStatus, int tppStatus, double amount, String cardNo,
			String accName, String bankName, String bankCode, String province,
			String city, String remark) {
		super();
		this.webStatus = webStatus;
		this.tppStatus = tppStatus;
		this.amount = amount;
		this.cardNo = cardNo;
		this.accName = accName;
		this.bankName = bankName;
		this.bankCode = bankCode;
		this.province = province;
		this.city = city;
		this.remark = remark;
		this.orderNo = OrderNoUtils.getSerialNumber();
		this.addTime = new Date();
		this.addIp = Global.getIP();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getWebStatus() {
		return webStatus;
	}

	public void setWebStatus(int webStatus) {
		this.webStatus = webStatus;
	}

	public int getTppStatus() {
		return tppStatus;
	}

	public void setTppStatus(int tppStatus) {
		this.tppStatus = tppStatus;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Operator getAddOperator() {
		return addOperator;
	}

	public void setAddOperator(Operator addOperator) {
		this.addOperator = addOperator;
	}

	public Operator getVerifyOperator() {
		return verifyOperator;
	}

	public void setVerifyOperator(Operator verifyOperator) {
		this.verifyOperator = verifyOperator;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
	
}
