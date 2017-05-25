package com.rongdu.p2psys.tpp.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tpp_yjf_pay")
public class YjfPay implements Serializable {
	private static final long serialVersionUID = 13245345345L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String borrowid;

	private String errormsg;

	private String money;

	private String operatetype;

	private String orderno;


	private String service;

	private String subtradeno;

	private String touserid;

	private String tradeno;

	private String userid;
	
	private int status;

	public YjfPay() {
	}

	public YjfPay(String borrowid, String errormsg, String money,
			String operatetype, String orderno,  String service,
			String subtradeno, String touserid, String tradeno, String userid) {
		super();
		this.borrowid = borrowid;
		this.errormsg = errormsg;
		this.money = money;
		this.operatetype = operatetype;
		this.orderno = orderno;
		this.service = service;
		this.subtradeno = subtradeno; 
		this.touserid = touserid;
		this.tradeno = tradeno;
		this.userid = userid;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBorrowid() {
		return this.borrowid;
	}

	public void setBorrowid(String borrowid) {
		this.borrowid = borrowid;
	}

	public String getErrormsg() {
		return this.errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getMoney() {
		return this.money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getOperatetype() {
		return this.operatetype;
	}

	public void setOperatetype(String operatetype) {
		this.operatetype = operatetype;
	}

	public String getOrderno() {
		return this.orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	
	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSubtradeno() {
		return this.subtradeno;
	}

	public void setSubtradeno(String subtradeno) {
		this.subtradeno = subtradeno;
	}

	public String getTouserid() {
		return this.touserid;
	}

	public void setTouserid(String touserid) {
		this.touserid = touserid;
	}

	public String getTradeno() {
		return this.tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


}