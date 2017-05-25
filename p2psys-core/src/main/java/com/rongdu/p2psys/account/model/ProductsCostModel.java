package com.rongdu.p2psys.account.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.account.domain.ProductsCost;

public class ProductsCostModel extends ProductsCost {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 当前页数 **/
	private int page;
	
	/** 每页总数 **/
	private int rows = Page.ROWS;
	
	/**
	 * 搜索条件(产品名称)
	 */
	private String searchName;
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 实例化
	 * @param cost
	 * @return
	 */
	public static ProductsCostModel instance(ProductsCost cost) {
		ProductsCostModel model = new ProductsCostModel();
		BeanUtils.copyProperties(cost, model);
		return model;
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

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
