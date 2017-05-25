package com.rongdu.manage.action.nb.vip;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.vip.domain.WealthManager;
import com.rongdu.p2psys.nb.vip.domain.WealthManagerUser;
import com.rongdu.p2psys.nb.vip.domain.WealthUser;
import com.rongdu.p2psys.nb.vip.model.WealthUserModel;
import com.rongdu.p2psys.nb.vip.service.WealthManagerService;
import com.rongdu.p2psys.nb.vip.service.WealthManagerUserService;
import com.rongdu.p2psys.nb.vip.service.WealthUserService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserService;

public class WealthUserAction extends BaseAction<WealthUserModel> implements
		ModelDriven<WealthUserModel>
{
	@Resource
	private WealthUserService wealthUserService;

	@Resource
	private UserService userService;

	@Resource
	private WealthManagerService wealthManagerService;

	@Resource
	private WealthManagerUserService wealthManagerUserService;

	private Map<String, Object> data;

	private User user;

	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthUserJson")
	public void dataJsonList() throws Exception
	{

		data = new HashMap<String, Object>();
		String realName = paramString("realName");
		String userName = paramString("userName");
		
		User user = new User();
		user.setUserName(userName);
		user.setRealName(realName);
		
		model.setUser(user);
		
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<WealthUserModel> dataModelList = wealthUserService.getPage(model, pageNumber, pageSize);
		data.put("total", dataModelList.getPage().getTotal());
		data.put("rows", dataModelList.getList());
		printJson(getStringOfJpaObj(data));
	}


	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUserAddAllList")
	public void dataAddAllList() throws Exception
	{

		data = new HashMap<String, Object>();
		String[] wealthUserId = request.getParameterValues("wealthUserId");
		String[] wealthManagerId = request.getParameterValues("wealthManagerId");

		for (int i = 0; i < wealthUserId.length; i++)
		{
			Integer wuid = Integer.parseInt(wealthUserId[i]);
			User user = userService.getUserById(wuid);
			WealthUser wealthUser = model.prototype();
			wealthUser.setAddTime(new Date());
			wealthUser.setUser(user);
			wealthUser = wealthUserService.saveObject(wealthUser);
			Integer id = wealthUser.getId();
			for (int j = 0; j < wealthManagerId.length; j++)
			{
				WealthManagerUser wealthManagerUser = new WealthManagerUser();
				wealthManagerUser.setWealthUserId(id);
				Integer wmid = Integer.parseInt(wealthManagerId[j]);
				WealthManager wealthManager = wealthManagerService.findById(wmid);
				wealthManagerUser.setWealthManager(wealthManager);
				wealthManagerUserService.saveObject(wealthManagerUser);
			}
		}
		data.put("result", true);
		data.put("msg", "保存成功！");
		printJson(getStringOfJpaObj(data));

	}


	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthUserDelete")
	public void dataDelete() throws Exception
	{
		data = new HashMap<String, Object>();
		Integer id = paramInt("id");
		wealthUserService.delWealthUser(id);
		data.put("result", true);
		data.put("msg", "删除成功！");
		printJson(getStringOfJpaObj(data));
	}


	public Map<String, Object> getData()
	{
		return data;
	}

	public void setData(Map<String, Object> data)
	{
		this.data = data;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

}
