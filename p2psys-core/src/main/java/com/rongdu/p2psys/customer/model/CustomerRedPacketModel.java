package com.rongdu.p2psys.customer.model;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.exception.PpfundException;

/**
 * PPfund（资金管理产品）model
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月16日
 */
public class CustomerRedPacketModel extends RedPacket {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 当前页数 **/
	private int page;
	
	/** 每页总数 **/
	private int rows = 12;
	
	private String customerName;
	
	private String customerPhone;
	
	private Double  packectMoney;
	
	private Double  recieveTime;
	
	private Date  dueTime;
	
	private Integer  isExchanged;
	
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


	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public Double getPackectMoney() {
		return packectMoney;
	}

	public void setPackectMoney(Double packectMoney) {
		this.packectMoney = packectMoney;
	}

	public Double getRecieveTime() {
		return recieveTime;
	}

	public void setRecieveTime(Double recieveTime) {
		this.recieveTime = recieveTime;
	}

	public Date getDueTime() {
		return dueTime;
	}

	public void setDueTime(Date dueTime) {
		this.dueTime = dueTime;
	}

	public Integer getIsExchanged() {
		return isExchanged;
	}

	public void setIsExchanged(Integer isExchanged) {
		this.isExchanged = isExchanged;
	}


	
}
