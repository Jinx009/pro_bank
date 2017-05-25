package com.rongdu.p2psys.core.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.core.domain.Dict;

public class DictModel extends Dict {

	private static final long serialVersionUID = 1L;

	/** 当前页数 **/
	private int page;
	/** 每页总数 **/
	private int rows;
	
	private  String searchName; 

	public Dict prototype() {
		Dict dict = new Dict();
		BeanUtils.copyProperties(this, dict);
		return dict;
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
