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
 * 平台账户操作记录
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年1月30日
 */
@Entity
@Table(name = "tpp_glod_log")
public class TppGlodLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 290916177061248977L;

	/**
	 * 主键
	 * 
	 * @return
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * 操作员ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operator_id")
	private Operator operator;

	/**
	 * 付款账户
	 */
	private String account;

	/**
	 * 收款账户
	 * 
	 */
	private String payAccount;

	/**
	 * 操作金额
	 */
	private double money;

	/**
	 * 操作手续费
	 */
	private double fee;

	/**
	 * 操作状态
	 */
	private byte status;
	
	/**
	 * 操作类型 cash：提现，recharge：充值，
	 * transfer：平台账户间转账，webTransfer:平台给用户转账
	 */
	private String type;

	/**
	 * 充值订单号
	 */
	private String ordId;

	/**
	 * 充值时间
	 */
	private Date addtime;

	/**
	 * 添加ip
	 */
	private String addip;

	/**
	 * 备注
	 */
	private String memo;

	public TppGlodLog() {
		super();
	}

	public TppGlodLog(Operator operator, String account, String payAccount,
			double money, double fee, byte status, String type, String ordId, String memo) {
		super();
		this.operator = operator;
		this.account = account;
		this.payAccount = payAccount;
		this.money = money;
		this.fee = fee;
		this.status = status;
		this.type = type;
		this.ordId = ordId;
		this.memo = memo;
		this.addtime = new Date();
		this.addip = Global.getIP();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrdId() {
		return ordId;
	}

	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
