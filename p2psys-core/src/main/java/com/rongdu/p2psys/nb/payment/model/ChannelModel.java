package com.rongdu.p2psys.nb.payment.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.payment.domain.Channel;

/**
 * 多支付通道信息model
 */
public class ChannelModel extends Channel {
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;
	
	/** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows = Page.ROWS;

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

//	public ChannelModel() {
//		super();
//	}

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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	public static ChannelModel instance(Channel channel) {
		ChannelModel channelModel = new ChannelModel();
		BeanUtils.copyProperties(channel, channelModel);
		return channelModel;
	}
	
	public static Channel instance(ChannelModel channelModel) {
		Channel channel = new Channel();
		BeanUtils.copyProperties(channelModel, channel);
		return channel;
	}
}
