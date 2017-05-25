package com.rongdu.p2psys.nb.payment.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.payment.domain.ChannelUrl;

/**
 * 多支付通道URL信息model
 */
public class ChannelUrlModel extends ChannelUrl {
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;
	
	/** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows = Page.ROWS;

	/**
	 * 支付通道id
	 */
	private long cid;
	/**
	 * 支付通道key
	 */
	private String channelKey;
	/**
	 * 支付通道名称
	 */
	private String channelName;
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

//	public ChannelUrlModel() {
//		super();
//	}

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
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
	public static ChannelUrlModel instance(ChannelUrl channelUrl) {
		ChannelUrlModel channelUrlModel = new ChannelUrlModel();
		BeanUtils.copyProperties(channelUrl, channelUrlModel);
		return channelUrlModel;
	}
	
	public static ChannelUrl instance(ChannelUrlModel channelUrlModel) {
		ChannelUrl channelUrl = new ChannelUrl();
		BeanUtils.copyProperties(channelUrlModel, channelUrl);
		return channelUrl;
	}
}
