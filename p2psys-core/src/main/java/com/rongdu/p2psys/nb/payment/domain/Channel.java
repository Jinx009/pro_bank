package com.rongdu.p2psys.nb.payment.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 多支付通道信息表
 * 
 * @author cgw
 * @version 1.0
 * @since 2015-06-23
 */
@Entity
@Table(name = "nb_channel")
public class Channel implements Serializable {
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
	 * 支付通道KEY
	 */
	private String channelKey;
	/**
	 * 支付通道名称
	 */
	private String channelName;
	/**
	 * 通道LOGO
	 */
	private String channelLogo;
	/**
	 * 通道原费率
	 */
	private double channelRate;

	public Channel() {
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

	public String getChannelKey() {
		return channelKey;
	}
	
	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}
	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelLogo() {
		return channelLogo;
	}

	public void setChannelLogo(String channelLogo) {
		this.channelLogo = channelLogo;
	}

	public double getChannelRate() {
		return channelRate;
	}
	
	public void setChannelRate(double channelRate) {
		this.channelRate = channelRate;
	}

}
