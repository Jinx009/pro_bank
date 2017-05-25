package com.rongdu.p2psys.nb.payment.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 通道URL表
 * 
 * @author cgw
 * @version 1.0
 * @since 2015-06-23
 */
@Entity
@Table(name = ("nb_channel_url"))
public class ChannelUrl implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * 支付通道id
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cid")
	private Channel chanel;
	/**
	 * 充值/支付入口URL
	 */
	private String rechargeUrl;
	/**
	 * 提现入口URL
	 */
	private String cashUrl;
	/**
	 * 连接类型：0Web端，1移动端
	 */
	private int urlType;

	public ChannelUrl() {
		super();
	}

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置主键
	 * 
	 * @param id 要设置的主键
	 */
	public void setId(long id) {
		this.id = id;
	}

	public Channel getChanel() {
		return chanel;
	}

	public void setChanel(Channel chanel) {
		this.chanel = chanel;
	}

	public String getRechargeUrl() {
		return rechargeUrl;
	}

	public void setRechargeUrl(String rechargeUrl) {
		this.rechargeUrl = rechargeUrl;
	}

	public String getCashUrl() {
		return cashUrl;
	}

	public void setCashUrl(String cashUrl) {
		this.cashUrl = cashUrl;
	}

	public int getUrlType() {
		return urlType;
	}

	public void setUrlType(int urlType) {
		this.urlType = urlType;
	}
	
	
}
