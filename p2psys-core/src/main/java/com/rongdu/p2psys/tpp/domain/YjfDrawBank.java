
package com.rongdu.p2psys.tpp.domain;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * 
 * 银行卡表
 * 
 * @author lx
 * @version 2.0
 * @since 2014年7月31日
 */
@Entity
@Table(name="tpp_yjf_draw_bank")
public class YjfDrawBank implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 278857869254393329L;
	
	public static String DRAW_CARDTYPE = "DEBIT_CARD";
	// 提现银行卡 WITHDRAW_B2C[对私] WITHDRAW_B2B[对公] ALL[所有]
	public static String DRAW_CHANNEL_NO = "WITHDRAW_B2C";
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String bankCode;

	private String bankName;

	private String batch;

	private String cardType;

	private String channelNo;

	private String env;

	@Lob
	private String logoUrl;

	private String memo;

	private String owner;

	private String payChannelApi;

	private String publicTag;

	private String state;

	public YjfDrawBank() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBatch() {
		return this.batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getCardType() {
		return this.cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getChannelNo() {
		return this.channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getEnv() {
		return this.env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getLogoUrl() {
		return this.logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPayChannelApi() {
		return this.payChannelApi;
	}

	public void setPayChannelApi(String payChannelApi) {
		this.payChannelApi = payChannelApi;
	}

	public String getPublicTag() {
		return this.publicTag;
	}

	public void setPublicTag(String publicTag) {
		this.publicTag = publicTag;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

}