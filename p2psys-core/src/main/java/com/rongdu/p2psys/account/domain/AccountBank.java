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

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.tpp.domain.YjfDrawBank;
import com.rongdu.p2psys.user.domain.User;

/**
 * 用户银行账户信息表
 * 
 * @author lhm
 * @version 2.0
 * @since 2014-04-03
 */
@Entity
@Table(name = "rd_account_bank")
public class AccountBank implements Serializable {
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
	 * 用户ID
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * 银行账号
	 */
	private String bankNo;
	/**
	 * 所属银行
	 */
	private String bank;
	/**
	 * 支行
	 */
	private String branch;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String area;
	/**
	 * 添加时间
	 */
	private Date addTime;
	/**
	 * 添加IP
	 */
	private String addIp;
	/**
	 * 0：关闭，1：启用
	 */
	private int status;
	
	/**
	 * 预留手机号
	 */
	private String mobile;

	/**
	 * 绑定号，银联在线绑卡成功返回的唯一绑定号
	 */
	private String bindId;
	
	/**
	 * 通道key
	 */
	private String channelKey;
	
	/**
	 * 所属银行编号
	 */
	private String bankCode;
	
	/**
	 * 银行图片路径
	 */
	private String picPath;
	
	public AccountBank() {
		super();
	}

	public AccountBank(User user, String bankNo, String bank, String picPath) {
		super();
		this.user = user;
		this.bankNo = bankNo;
		this.bank = bank;
		this.picPath = picPath;
		this.addTime = new Date();
		this.addIp = Global.getIP();
	}

	/**
	 * 易极付银行卡表
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "draw_bank")
	private YjfDrawBank yjfDrawBank;
	
	public YjfDrawBank getYjfDrawBank() {
		return yjfDrawBank;
	}

	public void setYjfDrawBank(YjfDrawBank yjfDrawBank) {
		this.yjfDrawBank = yjfDrawBank;
	}

	/**
	 * 获取(隐藏一定位数的)银行账号
	 * 
	 * @return 银行账号
	 */
	public String getHideBankNo() {
		if (StringUtil.isNotBlank(bankNo)) {
			return "******" + bankNo.substring(bankNo.length() - 4, bankNo.length());
		}
		return "";
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

	/**
	 * 获取用户ID
	 * 
	 * @return 用户ID
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置用户ID
	 * 
	 * @param user 要设置的用户
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 获取银行账号
	 * 
	 * @return 银行账号
	 */
	public String getBankNo() {
		return bankNo;
	}

	/**
	 * 设置银行账号
	 * 
	 * @param bankNo 要设置的银行账号
	 */
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	/**
	 * 获取所属银行
	 * 
	 * @return 所属银行
	 */
	public String getBank() {
		return bank;
	}

	/**
	 * 设置所属银行
	 * 
	 * @param bank 要设置的所属银行
	 */
	public void setBank(String bank) {
		this.bank = bank;
	}

	/**
	 * 获取支行
	 * 
	 * @return 支行
	 */
	public String getBranch() {
		return branch;
	}

	/**
	 * 设置支行
	 * 
	 * @param branch 要设置的支行
	 */
	public void setBranch(String branch) {
		this.branch = branch;
	}


	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * 获取添加IP
	 * 
	 * @return 添加IP
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置添加IP
	 * 
	 * @param addIp 要设置的添加IP
	 */
	public void setAddIp(String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获取0：关闭，1：启用
	 * 
	 * @return 0：关闭，1：启用
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置0：关闭，1：启用
	 * 
	 * @param status 要设置的0：关闭，1：启用
	 */
	public void setStatus(int status) {
		this.status = status;
	}

    public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

	public String getChannelKey() {
		return channelKey;
	}

	public void setChannelKey(String channelKey) {
		this.channelKey = channelKey;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
    
}
