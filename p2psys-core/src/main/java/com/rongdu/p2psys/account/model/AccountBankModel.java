package com.rongdu.p2psys.account.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.account.domain.AccountBank;

/**
 * 银行卡model
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月5日
 */
public class AccountBankModel extends AccountBank {

	private static final long serialVersionUID = 1L;

	private String bankName;// 开户银行名称

	private String userName;

	private String provinceStr;

	private String cityStr;

	private String areaStr;
	
	private String realName;
	/** 条件查询 */
	private String searchName;
	
	/** 状态描述 **/
	private String statusStr;

	public static AccountBankModel instance(AccountBank accountBank) {
		AccountBankModel accountBankModel = new AccountBankModel();
		BeanUtils.copyProperties(accountBank, accountBankModel);
		return accountBankModel;
	}

	public AccountBank prototype() {
		AccountBank accountBank = new AccountBank();
		BeanUtils.copyProperties(this, accountBank);
		return accountBank;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProvinceStr() {
		return provinceStr;
	}

	public void setProvinceStr(String provinceStr) {
		this.provinceStr = provinceStr;
	}

	public String getCityStr() {
		return cityStr;
	}

	public void setCityStr(String cityStr) {
		this.cityStr = cityStr;
	}

	public String getAreaStr() {
		return areaStr;
	}

	public void setAreaStr(String areaStr) {
		this.areaStr = areaStr;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getStatusStr() {
		switch (getStatus()) {
		case 1:
			statusStr = "启用";
			break;
		case 2:
			statusStr = "已解绑";
			break;
		default:
			break;
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

}
