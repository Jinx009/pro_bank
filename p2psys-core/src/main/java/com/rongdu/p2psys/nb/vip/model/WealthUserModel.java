package com.rongdu.p2psys.nb.vip.model;


import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.vip.domain.WealthUser;

public class WealthUserModel extends WealthUser
{
	/**
	 * 当前页数
	 */
	private int page;
	/**
	 * 每页页数
	 */
	private int rows = Page.ROWS;
	
	private List<String> list;
	
	public static WealthUserModel instance(WealthUser wealthUser)
	{
		WealthUserModel model = new WealthUserModel();
		BeanUtils.copyProperties(wealthUser, model);
		return model;
	}

	public WealthUser prototype()
	{
		WealthUser wealthUser = new WealthUser();
		BeanUtils.copyProperties(this, wealthUser);
		return wealthUser;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getRows()
	{
		return rows;
	}

	public void setRows(int rows)
	{
		this.rows = rows;
	}

	public List<String> getList()
	{
		return list;
	}

	public void setList(List<String> list)
	{
		this.list = list;
	}

	
	
	
	
	
}
