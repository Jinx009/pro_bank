package com.rongdu.p2psys.ppfund.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;

/**
 * PPfund资金管理产品转出Model
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月21日
 */
public class PpfundOutModel extends PpfundOut {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 当前页数 **/
	private int page;

	/** 每页总数 **/
	private int rows = Page.ROWS;

	/** 用户名 **/
	private String userName;
	
	/** 产品名称 **/
	private String ppfundName;

	/** 收益总额 **/
	private double interestTotal;
	
	/** 产品年化 **/
	private double ppfundApr;
	
	public static PpfundOutModel instance(PpfundOut out) {
		PpfundOutModel ppfundOutModel = new PpfundOutModel();
		BeanUtils.copyProperties(out, ppfundOutModel);
		return ppfundOutModel;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPpfundName() {
		return ppfundName;
	}

	public void setPpfundName(String ppfundName) {
		this.ppfundName = ppfundName;
	}

	public double getInterestTotal() {
		return interestTotal;
	}

	public void setInterestTotal(double interestTotal) {
		this.interestTotal = interestTotal;
	}

	public double getPpfundApr() {
		return ppfundApr;
	}

	public void setPpfundApr(double ppfundApr) {
		this.ppfundApr = ppfundApr;
	}

}
