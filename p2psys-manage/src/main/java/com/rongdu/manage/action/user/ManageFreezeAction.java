package com.rongdu.manage.action.user;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.UserFreeze;
import com.rongdu.p2psys.user.model.UserFreezeModel;
import com.rongdu.p2psys.user.service.UserFreezeService;

@SuppressWarnings("rawtypes")
public class ManageFreezeAction extends BaseAction implements ModelDriven<UserFreezeModel> {

	@Resource
	private UserFreezeService freezeService;

	private UserFreezeModel model = new UserFreezeModel();

	private Map<String, Object> data;

	public UserFreezeModel getModel() {
		return model;
	}

	/**
	 * 冻结列表页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/user/freeze/freezeManager")
	public String freezeManager() throws Exception {
		return "freezeManager";
	}

	/**
	 * 冻结列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/user/freeze/freezeList")
	public void freezeList() throws Exception {
		data = new HashMap<String, Object>();
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		PageDataList<UserFreezeModel> freezeList = freezeService.freezeList(pageNumber, pageSize, model);
		data.put("total", freezeList.getPage().getTotal()); // 总行数
		data.put("rows", freezeList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 新增页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/user/freeze/freezeAddPage")
	public String freezeAddPage() throws Exception {
		return "freezeAddPage";
	}

	/**
	 * 新增保存
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/user/freeze/freezeAdd")
	public void freezeAdd() throws Exception {
		data = new HashMap<String, Object>();
		Operator operator = getOperator();
		data = freezeService.freezeAdd(model, operator);
		
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/user/freeze/freezeEditPage")
	public String freezeEditPage() throws Exception {
		long id = paramLong("id");
		UserFreeze freeze = freezeService.find(id);
		UserFreezeModel model = UserFreezeModel.instance(freeze);
		String userName = model.getUser().getUserName();
		request.setAttribute("freeze", freeze);
		request.setAttribute("name", userName);
		return "freezeEditPage";
	}

	/**
	 * 修改保存
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/user/freeze/freezeEdit")
	public void freezeEdit() throws Exception {
		data = new HashMap<String, Object>();
		String msg = freezeService.freezeEdit(model, getOperator());
		if(!"SUCCESS".equals(msg)){
			data.put("result", false);
			data.put("msg", msg);
			//token重新保存
		}else{
			data.put("result", true);
			data.put("msg", "修改成功！");
		}
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 删除(启用，未启用)
	 * 
	 * @throws Exception
	 */
	@Action("/modules/user/freeze/freezeDelete")
	public void freezeDelete() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		int status = paramInt("status");
		freezeService.freezeDelete(id, status);
		data.put("result", true);
		data.put("msg", "更新成功！");
		printJson(getStringOfJpaObj(data));
	}

}
