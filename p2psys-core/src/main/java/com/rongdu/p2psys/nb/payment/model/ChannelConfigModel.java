package com.rongdu.p2psys.nb.payment.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.nb.payment.domain.ChannelConfig;
import com.rongdu.p2psys.nb.util.ConstantUtil;

/**
 * 多支付通道配置信息model
 */
public class ChannelConfigModel extends ChannelConfig {
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;
	
	/** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows = Page.ROWS;

	/**
	 * 银行id
	 */
	private long bid;
	
	/**
	 * 支持的银行名称
	 */
	private String bankName;
	/**
	 * 银行图标
	 */
	private String bankLogo;
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
	 * 微信端充值通道URL
	 */
	private String wapRechargeURL;
	
	/**
	 * PC端充值通道key
	 */
	private String webRechargeURL;
	
	/**
	 * 微信端提现通道key
	 */
	private String wapCashKey;

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
	
	public long getBid() {
		return bid;
	}

	public void setBid(long bid) {
		this.bid = bid;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankLogo() {
		return bankLogo;
	}

	public void setBankLogo(String bankLogo) {
		this.bankLogo = bankLogo;
	}

	public String getWebRechargeKey() {
		if (StringUtil.isNotBlank(webRechargeKey)) {
			if(webRechargeKey.equals(ConstantUtil.channelKey.unionpay.getValue())){
				return "银联支付";
			}else{
				return "连连支付";
			}
		}
		return "连连支付";
	}

	public void setWebRechargeKey(String webRechargeKey) {
		this.webRechargeKey = webRechargeKey;
	}

	public String getWebCashKey() {
		if (StringUtil.isNotBlank(webCashKey)) {
			if(webCashKey.equals(ConstantUtil.channelKey.llpay.getValue())){
				return "连连支付";
			}else{
				return "银联支付";
			}
		}
		return "银联支付";
	}

	public void setWebCashKey(String webCashKey) {
		this.webCashKey = webCashKey;
	}

	public String getWapRechargeKey() {
		if (StringUtil.isNotBlank(wapRechargeKey)) {
			if(wapRechargeKey.equals(ConstantUtil.channelKey.unionpay.getValue())){
				return "银联支付";
			}else{
				return "连连支付";
			}
		}
		return "连连支付";
	}

	public void setWapRechargeKey(String wapRechargeKey) {
		this.wapRechargeKey = wapRechargeKey;
	}

	public String getWapCashKey() {
		if (StringUtil.isNotBlank(wapCashKey)) {
			if(wapCashKey.equals(ConstantUtil.channelKey.llpay.getValue())){
				return "连连支付";
			}else{
				return "银联支付";
			}
		}
		return "银联支付";
	}

	public void setWapCashKey(String wapCashKey) {
		this.wapCashKey = wapCashKey;
	}

	public String getWapRechargeURL() {
		return wapRechargeURL;
	}

	public void setWapRechargeURL(String wapRechargeURL) {
		this.wapRechargeURL = wapRechargeURL;
	}

	public String getWebRechargeURL() {
		return webRechargeURL;
	}

	public void setWebRechargeURL(String webRechargeURL) {
		this.webRechargeURL = webRechargeURL;
	}

	public static ChannelConfigModel instance(ChannelConfig channelConfig) {
		ChannelConfigModel channelConfigModel = new ChannelConfigModel();
		BeanUtils.copyProperties(channelConfig, channelConfigModel);
		return channelConfigModel;
	}
	
	public static ChannelConfig instance(ChannelConfigModel channelConfigModel) {
		ChannelConfig channelConfig = new ChannelConfig();
		BeanUtils.copyProperties(channelConfigModel, channelConfig);
		return channelConfig;
	}
}
