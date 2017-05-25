package com.rongdu.p2psys.nb.vip.model;



import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.vip.domain.WealthManager;

public class WealthManagerModel extends WealthManager{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1302905750127600687L;
	/**
	 * 当前页数
	 */
	private int page;
	private String searchName;
	/**
	 * 每页页数
	 */
	private int rows = Page.ROWS;
	
	public static WealthManagerModel instance(WealthManager wealthManagerList)
	{
		WealthManagerModel model = new WealthManagerModel();
		BeanUtils.copyProperties(wealthManagerList, model);
		return model;
	}

	public WealthManager prototype()
	{
		WealthManager wealthManager = new WealthManager();
		BeanUtils.copyProperties(this, wealthManager);
		return wealthManager;
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
	
	

}
