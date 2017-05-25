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

/**
 * 对公付款银行卡
 * 
 * @author yl
 * @version 2.0
 * @date 2015年4月30日
 */
@Entity
@Table(name = "rd_comp_bank")
public class CompBank implements Serializable {

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
	 * 是否已删除
	 */
	private byte isDelete;

	/**
	 * 操作员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operator_id")
	private Operator operator;

	/**
	 * 添加时间
	 */
	private Date addTime;

	/**
	 * 添加IP
	 */
	private String addIp;

	public CompBank() {
		super();
	}
	
	public CompBank(String cardNo, String accName, String bankName,
			String bankCode, Operator operator) {
		super();
		this.cardNo = cardNo;
		this.accName = accName;
		this.bankName = bankName;
		this.bankCode = bankCode;
		this.operator = operator;
		this.addTime = new Date();
		this.addIp = Global.getIP();
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(byte isDelete) {
		this.isDelete = isDelete;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getAddIp() {
		return addIp;
	}

	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}
}
