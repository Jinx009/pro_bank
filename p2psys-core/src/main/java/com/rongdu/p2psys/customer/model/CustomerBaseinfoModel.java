package com.rongdu.p2psys.customer.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.exception.PpfundException;

/**
 * PPfund（资金管理产品）model
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月16日
 */
public class CustomerBaseinfoModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 当前页数 **/
	private int page;
	
	/** 每页总数 **/
	private int rows = 12;
	
	private int id;

	private String saleCode;
	
	private String saleName;
	
	private String realName;
	
	private Integer sex;
	
	private String phone;
	
	private Integer age;
	
	private String certificate;
	
	private String bank;
	
	private String bankNo;
	
	private Date registerTime;
	
	private Date realNameTime;
	
	private Date bindCardTime;
	
	private Integer accountState;
	
	private String recommendCode;
	
	private Double useMoney;
	
	private Double investMoney;
	
	private String searchName;
	
	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSaleCode() {
		return saleCode;
	}

	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
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

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getRealNameTime() {
		return realNameTime;
	}

	public void setRealNameTime(Date realNameTime) {
		this.realNameTime = realNameTime;
	}

	public Date getBindCardTime() {
		return bindCardTime;
	}

	public void setBindCardTime(Date bindCardTime) {
		this.bindCardTime = bindCardTime;
	}

	public Integer getAccountState() {
		return accountState;
	}

	public void setAccountState(Integer accountState) {
		this.accountState = accountState;
	}

	public String getRecommendCode() {
		return recommendCode;
	}

	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}
	
	public Double getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(Double useMoney) {
		this.useMoney = useMoney;
	}


	public Double getInvestMoney() {
		return investMoney;
	}

	public void setInvestMoney(Double investMoney) {
		this.investMoney = investMoney;
	}


	
}
