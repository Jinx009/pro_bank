package com.rongdu.p2psys.pc.user;

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

public class UserRedPacketAction extends BaseAction<UserRedPacketModel> implements ModelDriven<UserRedPacketModel>
{
private Map<String,Object> map;
	
	@Resource
	private UserRedPacketService theUserRedPacketService;
	
	/**
	 * 红包页面
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/pc/member/redPacket" )
	public String wechatRedPacket() throws IOException
	{
		return "redPacket";
	}
	/**
	 * 红包数据
	 * @throws IOException
	 * @throws ParseException
	 */
	@Action(value = "/nb/pc/redPacketListJson" )
	public void getRedPacket() throws IOException, ParseException
	{
		map = new HashMap<String, Object>();
		
		if(hasSessionUser())
		{
			User user = getNBSessionUser();
			
			long userId = user.getUserId();
			
			List<UserRedPacketModel> notUsedList = theUserRedPacketService.findNotUsed(userId);
			List<UserRedPacketModel> usedList = theUserRedPacketService.findUsed(userId);
			List<UserRedPacketModel> overdue = theUserRedPacketService.findOverdue(userId);
			
			map.put("notUsedList",notUsedList);
			map.put("usedList",usedList);
			map.put("overdue",overdue);
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		}
		else
		{
			map = getErrorMap();
		}
		
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 红包兑现
	 * @throws Exception
	 */
	@Action(value = "/nb/pc/exchangeRedPacket")
	public void exchangeRedPacket() throws Exception
	{
		map = new HashMap<String, Object>();
		
		long id = paramLong("id");
		
		if(hasSessionUser())
		{
			User user = getNBSessionUser();
			
			String[] redPacketIds = {String.valueOf(id)};
			
			UserRedPacket userRedPacket = theUserRedPacketService.getById(id);
			
			if(userRedPacket.isUsed())
			{
				map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
			}
			else
			{
				theUserRedPacketService.doExchangeRedPacket(redPacketIds, user);
				
				map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			}
			
		}
		else
		{
			map = getErrorMap();
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	
	
	
}
