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
 * 通道配置表
 * 
 * @author cgw
 * @version 1.0
 * @since 2015-06-23
 */
@Entity
@Table(name = ("nb_channel_config"))
public class ChannelConfig implements Serializable {
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
	@JoinColumn(name = "bid")
	private NbSupportBank sb;

	/**
	 * web端充值通道key
	 */
	private String webRechargeKey;
	
	/**
	 * web端提现通道key
	 */
	private String webCashKey;
	
	/**
	 * 微信端充值通道key
	 */
	private String wapRechargeKey;
	
	/**
	 * 微信端提现通道key
	 */
	private String wapCashKey;

	public ChannelConfig() {
		
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

	public NbSupportBank getSb() {
		return sb;
	}

	public void setSb(NbSupportBank sb) {
		this.sb = sb;
	}

	public String getWebRechargeKey() {
		return webRechargeKey;
	}

	public void setWebRechargeKey(String webRechargeKey) {
		this.webRechargeKey = webRechargeKey;
	}

	public String getWebCashKey() {
		return webCashKey;
	}

	public void setWebCashKey(String webCashKey) {
		this.webCashKey = webCashKey;
	}

	public String getWapRechargeKey() {
		return wapRechargeKey;
	}

	public void setWapRechargeKey(String wapRechargeKey) {
		this.wapRechargeKey = wapRechargeKey;
	}

	public String getWapCashKey() {
		return wapCashKey;
	}

	public void setWapCashKey(String wapCashKey) {
		this.wapCashKey = wapCashKey;
	}

}
