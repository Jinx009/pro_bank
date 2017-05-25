package com.rongdu.p2psys.borrow.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;

/**
 * 标种配置Model
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月22日
 */
public class BorrowConfigModel extends BorrowConfig {

	private static final long serialVersionUID = 1L;

	/** 当前页码 */
	private int page;
	
	/** 条件查询 */
	private String searchName;

	
	/** 每页数据条数 */
	private int size = Page.ROWS;

	public static BorrowConfigModel instance(BorrowConfig borrowConfig) {
		BorrowConfigModel borrowConfigModel = new BorrowConfigModel();
		BeanUtils.copyProperties(borrowConfig, borrowConfigModel);
		return borrowConfigModel;
	}

	public BorrowConfig prototype() {
		BorrowConfig borrowConfig = new BorrowConfig();
		BeanUtils.copyProperties(this, borrowConfig);
		return borrowConfig;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size > 0 ? size : Page.ROWS;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	

}
