package com.rongdu.manage.action.nb.vip;

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
import com.rongdu.p2psys.nb.vip.model.WealthManagerUserModel;
import com.rongdu.p2psys.nb.vip.service.WealthManagerService;
import com.rongdu.p2psys.nb.vip.service.WealthManagerUserService;
import com.rongdu.p2psys.nb.vip.service.WealthUserService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserService;

public class WealthManagerUserAction extends BaseAction<WealthManagerUserModel>
		implements ModelDriven<WealthManagerUserModel>
{

	@Resource
	private WealthManagerUserService wealthManagerUserService;
	@Resource
	private UserService userService;
	@Resource
	private WealthUserService wealthUserService;
	@Resource
	private WealthManagerService wealthManagerService;

	private Map<String, Object> data;

	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUserList")
	public void dataList() throws Exception
	{

		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		PageDataList<WealthManagerUserModel> dataModelList = wealthManagerUserService.dataList(model, pageNumber, pageSize);
		data.put("total", dataModelList.getPage().getTotal());
		data.put("rows", dataModelList.getList());
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 未有管家用户json
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/userList")
	public void dataJsonList() throws Exception
	{

		data = new HashMap<String, Object>();
		int start_num = paramInt("start_num");
		int end_num = paramInt("end_num");

		List<User> list = wealthManagerUserService.getUser(start_num, end_num);
		data.put("list", list);

		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 根据用户姓名和用户手机号查询用户
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/userSearch")
	public void dataJsonUser() throws Exception
	{

		data = new HashMap<String, Object>();
		String userName =paramString("userName");
		
		String mobilePhone = paramString("mobilePhone");
		
		List<User> user = wealthManagerUserService.getUser(userName, mobilePhone);
		
		data.put("user", user);
		printJson(getStringOfJpaObj(data));
		
	}
	

	/**
	 * 未有管家用户json
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/managerList")
	public void allWealthManagerList() throws Exception
	{

		data = new HashMap<String, Object>();

		List<WealthManager> list = wealthManagerService.getList();
		data.put("list", list);

		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 跳转至列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUser")
	public String dataWealthManagerPage() throws Exception
	{
		return "wealthManagerUser";
	}

	/**
	 * 跳转至新增页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUserAddPage")
	public String dataAddPage() throws Exception
	{
		return "wealthManagerUserAddPage";
	}

	/**
	 * 新增
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUserAdd")
	public void dataAdd() throws Exception
	{

		data = new HashMap<String, Object>();
		WealthManagerUser wealthManagerUser = model.prototype();
		wealthManagerUserService.saveObject(wealthManagerUser);
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
	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUserEditPage")
	public String dataEditPage() throws Exception
	{
		data = new HashMap<String, Object>();
		Integer userid = paramInt("userid");
		request.setAttribute("userid", userid);

		return "wealthManagerUserEditPage";
	}

	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUserEditData")
	public void getEditData() throws Exception
	{

		data = new HashMap<String, Object>();
		String userid = request.getParameter("userid");
		List<WealthManagerUser> list = wealthManagerUserService.findByUserId(Integer.parseInt(userid));
		for (int i = 0; i < list.size(); i++)
		{
			Integer wealthId = list.get(i).getWealthManager().getId();
			WealthUser wealthUser = wealthUserService.findById(list.get(i).getWealthUserId());
			data.put("wealthUser", wealthUser);
			data.put("list", list);
			printJson(getStringOfJpaObj(data));

		}

	}

	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUserDelete")
	public void dataDelete() throws Exception
	{
		data = new HashMap<String, Object>();
		Integer userId = paramInt("id");
		wealthUserService.delWealthUser(userId);
		List<WealthManagerUser> userIds = wealthManagerUserService.findByUserId(userId);
		for (int i = 0; i < userIds.size(); i++)
		{
			wealthManagerUserService.delWealthManagerUser(userIds.get(i).getId());
		}

		data.put("result", true);
		data.put("msg", "删除成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 修改删除
	 * 
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUserDelete1")
	public void dataDelete1() throws Exception
	{
		data = new HashMap<String, Object>();
		String userid = request.getParameter("userid");
		wealthUserService.delWealthUser(Integer.parseInt(userid));
		List<WealthManagerUser> userIds = wealthManagerUserService.findByUserId(Integer.parseInt(userid));
		for (int i = 0; i < userIds.size(); i++)
		{
			wealthManagerUserService.delWealthManagerUser(userIds.get(i).getId());
		}

		data.put("result", true);
		data.put("msg", "删除成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 修改新增
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUserAddAllList1")
	public void dataAddAllList() throws Exception
	{

		data = new HashMap<String, Object>();
		String[] wealthUserId = request.getParameterValues("wealthUserId");
		String[] wealthManagerId = request.getParameterValues("wealthManagerId");

		for (int i = 0; i < wealthUserId.length; i++)
		{
			
			Integer wuid = Integer.parseInt(wealthUserId[i]);			
			User user = userService.getUserById(wuid);
			WealthUser wealthUser = new WealthUser();
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
	 * 更新
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/vip/wealthManagerUser/wealthManagerUserUpdate")
	public void dataEdit() throws Exception
	{
		data = new HashMap<String, Object>();
		WealthManagerUser wealthManagerUser = model.prototype();
		wealthManagerUserService.update(wealthManagerUser);
		data.put("result", true);
		data.put("msg", "修改成功！");
		printJson(getStringOfJpaObj(data));
	}

	@Action(value = "/modules/nb/vip/wealthManagerUser/getAllUserId")
	public void getAllUserId() throws Exception
	{

		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page");
		int pageSize = paramInt("rows");
		UserModel userModel = new UserModel();
		PageDataList<UserModel> dataModelList = userService.userList(pageNumber, pageSize, userModel);
		data.put("total", dataModelList.getPage().getTotal());
		data.put("rows", dataModelList.getList());
		printJson(getStringOfJpaObj(data));

	}

	@Action(value = "/modules/nb/vip/wealthManagerUser/getUserByUserName")
	public void getUserByUserName() throws Exception
	{
		data = new HashMap<String, Object>();
		String realName = new String(paramString("realName").getBytes("iso-8859-1"), "UTF-8");
		User user = userService.getUserByRealName(realName);
		data.put("user", user);
		printJson(getStringOfJpaObj(data));
	}
	
	
	@Action(value = "/modules/nb/vip/wealthManagerUser/dataAddAllUserList")
	public void dataAddAllUserList() throws Exception
	{

		data = new HashMap<String, Object>();
		String[] wealthUserId = request.getParameterValues("wealthUserId");
		String[] wealthManagerId = request.getParameterValues("wealthManagerId");
		for (int i = 0; i < wealthUserId.length; i++)
		{
			Integer wuid = Integer.parseInt(wealthUserId[i]);
			
			
			User user = userService.getUserById(wuid);
			WealthUser wealthUser = new WealthUser();
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
	

}
