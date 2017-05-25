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
public class AccountLogModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 当前页数 **/
	private int page;
	
	/** 每页总数 **/
	private int rows = 12;
	
	private int id;

	private String customerName;
	
	private Integer sex;
	
	private Double account;
	
	private Double useMoney;
	
	private Double freezeMoney;
	
	private Double collection;
	
	private String cashUseType;
	
	private String searchName;
	
	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getCashUseType() {
		return cashUseType;
	}

	public void setCashUseType(String cashUseType) {
		this.cashUseType = cashUseType;
	}

	private Double operMoney;
	
	private Date operTime;
	
	private String remark;
	
	public Double getAccount() {
		return account;
	}

	public void setAccount(Double account) {
		this.account = account;
	}

	public Double getUseMoney() {
		return useMoney;
	}

	public void setUseMoney(Double useMoney) {
		this.useMoney = useMoney;
	}

	public Double getFreezeMoney() {
		return freezeMoney;
	}

	public void setFreezeMoney(Double freezeMoney) {
		this.freezeMoney = freezeMoney;
	}

	public Double getCollection() {
		return collection;
	}

	public void setCollection(Double collection) {
		this.collection = collection;
	}

	public Double getOperMoney() {
		return operMoney;
	}

	public void setOperMoney(Double operMoney) {
		this.operMoney = operMoney;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	

	
}
