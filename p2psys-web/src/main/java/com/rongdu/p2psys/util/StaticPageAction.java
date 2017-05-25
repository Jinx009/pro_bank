package com.rongdu.p2psys.util;

import org.apache.struts2.convention.annotation.Action;

import com.rongdu.p2psys.core.web.BaseAction;

@SuppressWarnings("rawtypes")
public class StaticPageAction extends BaseAction{
	/**
	 * 新手指南页面
	 * @return
	 */
	@Action(value = "/nb/wechat/static/NewbieGuide")
	public String NewbieGuide()
	{
		return "NewbieGuide";
	}
	
	/**
	 * 活动详情页面
	 * @return
	 */
	@Action(value = "/nb/wechat/static/ActivityProductDetail")
	public String ActivityProductDetail()
	{
		return "activity_productDetail";
	}
}
