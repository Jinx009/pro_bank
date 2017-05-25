package com.rongdu.p2psys.wechat;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.wechat.model.WechatCacheModel;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;

public class WechatMessageAction extends BaseAction<WechatCacheModel>  implements ModelDriven<WechatCacheModel>
{
	private Map<String,Object> map;
	
	@Resource
	private WechatCacheService wechatCacheService;
	
	/**
	 * 发送通用模板消息
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Action(value = "/nb/sendCommonMessage")
	public void sendCommonMessage() throws ClientProtocolException, IOException
	{
		
		
	}
	
	
	public Map<String, Object> getMap()
	{
		return map;
	}
	public void setMap(Map<String, Object> map)
	{
		this.map = map;
	}
	
}
