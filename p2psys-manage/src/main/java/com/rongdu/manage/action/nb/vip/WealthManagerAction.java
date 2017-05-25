package com.rongdu.manage.action.nb.vip;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.vip.domain.WealthManager;
import com.rongdu.p2psys.nb.vip.domain.WealthUser;
import com.rongdu.p2psys.nb.vip.model.WealthManagerModel;
import com.rongdu.p2psys.nb.vip.service.WealthManagerService;
import com.rongdu.p2psys.nb.vip.service.WealthManagerUserService;
import com.rongdu.p2psys.nb.vip.service.WealthUserService;
import com.rongdu.p2psys.user.domain.User;

public class WealthManagerAction extends BaseAction<WealthManagerModel> implements ModelDriven<WealthManagerModel>{
	
	
	@Resource
	private WealthManagerService wealthManagerService;
	@Resource
	private WealthManagerUserService wealthManagerUserService;
	@Resource
	private WealthUserService wealthUserService;
	
	private Map<String, Object> data;
	
	@Action(value = "/modules/nb/vip/wealthManager/wealthManagerList")
	public void dataList() throws Exception
	{
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<WealthManagerModel> dataModelList = wealthManagerService.dataList(model, pageNumber, pageSize);
		data.put("total", dataModelList.getPage().getTotal());
		data.put("rows", dataModelList.getList());
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 跳转至列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManager/wealthManager")
	public String dataWealthManagerPage() throws Exception
	{
		return "wealthManager";
	}
	

	/**
	 * 跳转至新增页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManager/wealthManagerAddPage")
	public String dataAddPage() throws Exception
	{
		return "wealthManagerAddPage";
	}
	
	
	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManager/wealthManagerAdd")
	public void dataAdd() throws Exception
	{	
		
		data = new HashMap<String, Object>();			
		WealthManager wealthManager = model.prototype();
		
		String path = imgUpload();
		
		if(path!=null&&!"".equals(path))
		{
			wealthManager.setIcon(path);
		}

		wealthManagerService.saveObject(wealthManager);	
		
		data.put("result", true);
		data.put("msg", "保存成功！");		
		printJson(getStringOfJpaObj(data));
		
	}
	
	
	/**
	 * 编辑
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManager/wealthManagerEditPage")
	public String dataEditPage() throws Exception
	{
		Integer id = paramInt("id");
		WealthManager wealthManager = wealthManagerService.findById(id);
		String path = imgUpload();
		if(path!=null&&!"".equals(path)){
			
			wealthManager.setIcon(path);
		}
		request.setAttribute("wealthManager", wealthManager);
		return "wealthManagerEditPage";
	}
	
	
	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManager/wealthManagerDelete")
	public void dataDelete() throws Exception
	{
		data = new HashMap<String, Object>();
		Integer id = paramInt("id");
		wealthManagerService.delWealthManager(id);
		data.put("result", true);
		data.put("msg", "删除成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 更新
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManager/wealthManagerUpdate")
	public void dataEdit() throws Exception
	{
		
		data = new HashMap<String, Object>();
		WealthManager wealthManager = model.prototype();
		String path = imgUpload();
		if(path!=null&&!"".equals(path)){
			
			wealthManager.setIcon(path);
		}
		wealthManagerService.update(wealthManager);
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}
	
	
	@Action(value = "/modules/nb/vip/wealthManager/getWealthManagerByName")
	public void getWealthManagerByName() throws Exception
	{
		data = new HashMap<String,Object>();
		String name = new String(paramString("name").getBytes("iso-8859-1"),"UTF-8");
		WealthManager wealtManager = wealthManagerService.find(name);
		data.put("wealtManager", wealtManager);
		printJson(getStringOfJpaObj(data));
	}
	
	
	@Action(value = "/modules/nb/vip/wealthManager/getAllUserList")
	public void getAllUserList() throws Exception
	{
		data = new HashMap<String,Object>();
		String wealthId = request.getParameter("id");
		Integer id = Integer.parseInt(wealthId);
		List<User> users = wealthManagerService.getAllUserList(id);
		data.put("users", users);
		printJson(getStringOfJpaObj(data));
		
	}
	
	@Action(value = "/modules/nb/vip/wealthManager/findWealthManagerById")
	public void findWealthManagerById() throws Exception
	{
		data = new HashMap<String,Object>();
		String wealthId = request.getParameter("id");
		Integer id = Integer.parseInt(wealthId);
		WealthManager wealthManager = wealthManagerService.findById(id);
		data.put("wealthManager", wealthManager);
		printJson(getStringOfJpaObj(data));
		
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManager/getUserList")
	public String getUserList() throws Exception
	{
		Integer id = paramInt("id");
		request.setAttribute("id", id);
		
		return "wealthManagerList";
	}
	
	
	@Action(value = "/modules/nb/vip/wealthManager/findNotInWealthManagerId")
	public void findNotInWealthManagerId() throws Exception
	{
		data = new HashMap<String,Object>();
		String wealthId = request.getParameter("id");
		Integer id = Integer.parseInt(wealthId);
		List<WealthManager> wealthManager = wealthManagerService.findNotIn(id);
		data.put("wealthManager", wealthManager);
		printJson(getStringOfJpaObj(data));
		
	}
	
	@Action(value = "/modules/nb/vip/wealthManager/deleteByWealthId")
	public void deleteByWealthId() throws Exception
	{		
		data = new HashMap<String, Object>();
		String[] wealthUserId = request.getParameterValues("wealthUserId");
		String wealthId = request.getParameter("id");
		Integer id = Integer.parseInt(wealthId);
		
		for(int j = 0;j<wealthUserId.length;j++){
			WealthUser wealthUser = wealthUserService.getWealthUser(id, Integer.parseInt(wealthUserId[j]));
			wealthUserService.delWealthUser(wealthUser.getId());
			
			Integer wuId = wealthManagerUserService.getByWuId(wealthUser.getId());
			wealthManagerUserService.delWealthManagerUser(wuId);
		}
		
		data.put("result", "删除成功！");
		printJson(getStringOfJpaObj(data));
	}
	
}
