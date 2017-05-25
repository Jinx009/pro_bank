package com.rongdu.p2psys.wechat;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.user.service.UserRedPacketService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.UserRedPacketModel;

public class WechatRedPacketAction extends BaseAction<UserRedPacketModel> implements ModelDriven<UserRedPacketModel>
{
	private Map<String,Object> map;
	
	@Resource
	private UserRedPacketService theUserRedPacketService;
	
	/**
	 * 红包页面
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/redPacket" )
	public String wechatRedPacket() throws IOException
	{
		User user = getNBSessionUser();
		
		request.setAttribute("userId",user.getUserId());
		
		return "redPacket";
	}
	
	/**
	 * 红包数据
	 * @throws IOException
	 * @throws ParseException
	 */
	@Action(value = "/nb/wechat/account/redPacketListJson" )
	public void getRedPacket() throws IOException, ParseException
	{
		map = new HashMap<String, Object>();
		
		long userId = paramLong("userId");
		
		List<UserRedPacketModel> notUsedList = theUserRedPacketService.findNotUsed(userId);
		List<UserRedPacketModel> usedList = theUserRedPacketService.findUsed(userId);
		List<UserRedPacketModel> overdue = theUserRedPacketService.findOverdue(userId);
		
		map.put("notUsedList",notUsedList);
		map.put("usedList",usedList);
		map.put("overdue",overdue);
		
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 微信兑红包
	 * @throws Exception
	 */
	@Action(value = "/nb/wechat/account/exchangeRedPacket")
	public void exchangeRedPacket() throws Exception
	{
		User user = getNBSessionUser();
		
		map = new HashMap<String, Object>();
		
		String[] redPacketIds = {paramString("id")};
		
		UserRedPacket userRedPacket = theUserRedPacketService.getById(paramLong("id"));
		
		if(userRedPacket.isUsed())
		{
			map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		}
		else
		{
			theUserRedPacketService.doExchangeRedPacket(redPacketIds, user);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		}
		
		printWebJson(getStringOfJpaObj(map));
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
