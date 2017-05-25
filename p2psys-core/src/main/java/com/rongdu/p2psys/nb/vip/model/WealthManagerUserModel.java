package com.rongdu.p2psys.nb.vip.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.vip.domain.WealthManagerUser;

@SuppressWarnings("serial")
public class WealthManagerUserModel extends WealthManagerUser{
	/**
	 * 当前页数
	 */
	private int page;
	/**
	 * 每页页数
	 */
	private int rows = Page.ROWS;
	
	public static WealthManagerUserModel instance(WealthManagerUser wealthManagerUser)
	{
		WealthManagerUserModel model = new WealthManagerUserModel();
		BeanUtils.copyProperties(wealthManagerUser, model);
		return model;
	}

	public WealthManagerUser prototype()
	{
		WealthManagerUser wealthManagerUser = new WealthManagerUser();
		BeanUtils.copyProperties(this, wealthManagerUser);
		return wealthManagerUser;
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
	
	
	
	

}
