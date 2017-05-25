package com.rongdu.p2psys.nb.wechat.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.Page;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;

public class WechatCacheModel extends WechatCache
{
	private static final long serialVersionUID = 4355045628331047832L;

	/**
	 * 当前页数
	 */
	private int page;
	/**
	 * 每页页数
	 */
	private int rows = Page.ROWS;
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 获取Model实例
	 * @param wechatCache
	 * @return
	 */
	public static WechatCacheModel instance(WechatCache wechatCache)
	{
		WechatCacheModel model = new WechatCacheModel();
		BeanUtils.copyProperties(wechatCache, model);
		return model;
	}
	/**
	 * 获取实例
	 * @return
	 */
	public WechatCache prototype()
	{
		WechatCache wechatCache = new WechatCache();
		BeanUtils.copyProperties(this,wechatCache);
		return wechatCache;
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
	public String getRemark()
	{
		return remark;
	}
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
	
	
}
