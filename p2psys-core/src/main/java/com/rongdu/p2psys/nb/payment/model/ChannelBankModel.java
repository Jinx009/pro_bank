package com.rongdu.p2psys.nb.payment.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.payment.domain.ChannelBank;

/**
 * 多支付通道银行信息model
 */
public class ChannelBankModel extends ChannelBank {
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
	 * 银行卡id
	 */
	private long bid;
	/**
	 * 支付通道key
	 */
	private String channelKey;
	/**
	 * 支付通道名称
	 */
	private String channelName;
	/**
	 * 银行卡所属银行
	 */
	private String bank;
	/**
	 * 银行卡号
	 */
	private String bankNo;
	/**
	 * 绑定协议号
	 */
	private String bindId;
	/**
	 * 用户真实姓名
	 */
	private String realName;
	
	/**
	 * 0：关闭，1：启用
	 */
	private int status;

	/**
	 * 绑定时间
	 */
	private String addTime;

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

//	public ChannelBankModel() {
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
	
	public long getBid() {
		return bid;
	}

	public void setBid(long bid) {
		this.bid = bid;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
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
	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static ChannelBankModel instance(ChannelBank channelBank) {
		ChannelBankModel channelBankModel = new ChannelBankModel();
		BeanUtils.copyProperties(channelBank, channelBankModel);
		return channelBankModel;
	}
	
	public static ChannelBank instance(ChannelBankModel channelBankModel) {
		ChannelBank channelBank = new ChannelBank();
		BeanUtils.copyProperties(channelBankModel, channelBank);
		return channelBank;
	}
}
