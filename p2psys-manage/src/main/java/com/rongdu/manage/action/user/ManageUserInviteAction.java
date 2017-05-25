package com.rongdu.manage.action.user;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.model.UserInviteModel;
import com.rongdu.p2psys.user.service.UserInviteService;

/**
 * 好友邀请管理
 * @author zf
 * @version 2.0
 * @since 2014年12月1日
 */
public class ManageUserInviteAction extends BaseAction<UserInviteModel> implements ModelDriven<UserInviteModel>{

	@Resource
	private UserInviteService userInviteService;

	private Map<String, Object> data;

	/**
	 * 获得用户红包清单页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/userInvite/userInviteManager")
	public String userInviteManager() throws Exception {
		
		return "userInviteManager";
	}

	/**
	 * 获得用户红包清单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/userInvite/userInviteList")
	public void userInviteList() throws Exception {
		data = new HashMap<String, Object>();
		PageDataList<UserInviteModel> pagaDataList = userInviteService.findByModel(model);
		int total = pagaDataList.getPage().getTotal();// 总记录数
		data.put("total", total);
		data.put("rows", pagaDataList.getList());
		printJson(getStringOfJpaObj(data));
	}
	/**
	 * 获得用户红包统计页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userRedPacketStatisticsManager")
	public String userRedPacketStatisticsManager() throws Exception {
		
		return "userRedPacketStatisticsManager";
	}
	
	/**
	 * 获得用户红包清单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userRedPacketStatisticsList")
	public void userRedPacketStatisticsList() throws Exception {
		data = new HashMap<String, Object>();
		/*List<UserRedPacketModel> list = userRedPacketService.statisticsByModel(model);
		data.put("rows", list);
		long total = userRedPacketService.getTotalByModel(model);
		data.put("total", total);*/
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 获得用户红包ID获取红包明细
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/user/redPacket/userRedPacketDetail")
	public String userRedPacketDetail() throws Exception {
		return "userRedPacketDetail";
	}
	
}
