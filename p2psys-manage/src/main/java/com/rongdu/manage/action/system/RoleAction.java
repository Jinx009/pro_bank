package com.rongdu.manage.action.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.domain.Menu;
import com.rongdu.p2psys.core.domain.Role;
import com.rongdu.p2psys.core.domain.RoleMenu;
import com.rongdu.p2psys.core.model.RoleModel;
import com.rongdu.p2psys.core.service.MenuService;
import com.rongdu.p2psys.core.service.RoleService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 角色管理
 * 
 * @version 2.0
 */
public class RoleAction extends BaseAction implements ModelDriven<RoleModel> {

	private RoleModel model = new RoleModel();

	public RoleModel getModel() {
		return model;
	}

	@Resource
	private RoleService roleService;

	@Resource
	private MenuService menuService;

	private Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * 角色管理页面
	 * 
	 * @return
	 */
	@Action("/modules/system/role/roleManager")
	public String roleManager() throws Exception {
		return "roleManager";
	}

	@Action(value = "/modules/system/role/roleList")
	public void roleList() throws Exception {
		PageDataList<Role> pageList = roleService.list(model);
		int totalPage = pageList.getPage().getTotal(); // 总页数
		map.put("total", totalPage);
		map.put("rows", pageList.getList());
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 获取用户所拥有的角色
	 * 
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/role/myRoleList")
	public void myRoleList() throws Exception {
		List<Role> roleList = roleService.getRoleList(getOperatorId());
		map.put("rows", roleList);
		printJson(getStringOfJpaObj(map));
	}

	/**
	 * 角色添加页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/modules/system/role/roleAddPage")
	public String roleAddPage() throws Exception {
		List<Menu> menuList = menuService.menuUseList();
		request.setAttribute("menuList", menuList);
		return "roleAddPage";
	}

	/**
	 * 角色添加
	 * 
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/role/roleAdd")
	public void roleAdd() throws Exception {
		Role role = (Role) paramModel(Role.class);
		String menuIdStr = request.getParameter("menuId");
		if (StringUtil.isBlank(menuIdStr)) {
			printResult(MessageUtil.getMessage("I10004"), false);
			return;
		}
		String[] menuIdArr = menuIdStr.split(",");
		if (menuIdArr == null || menuIdArr.length <= 0) {
			printResult(MessageUtil.getMessage("I10004"), false);
			return;
		}
		role.setAddUser(getOperator().getName());
		roleService.addRole(role, menuIdArr);
		printResult(MessageUtil.getMessage("I10001"), true);
	}

	/**
	 * 角色修改页面
	 * 
	 * @return 页面
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/role/roleEditPage")
	public String roleEditPage() throws Exception {
		Role role = roleService.getRoleById(this.model.getId());
		request.setAttribute("role", role);
		return "roleEditPage";
	}

	/**
	 * 角色修改
	 * 
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/role/roleEdit")
	public void roleEdit() throws Exception {
		Role role = (Role) this.paramModel(Role.class);
		role.setUpdateUser(getOperator().getName());
		roleService.roleUpdate(role);
		printResult(MessageUtil.getMessage("I10002"), true);
	}

	/**
	 * 角色删除（逻辑删除）
	 * 
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/role/roleDelete")
	public void roleDelete() throws Exception {
		roleService.deleteRole(this.model.getId());
		printResult(MessageUtil.getMessage("I10003"), true);
	}

	/**
	 * 角色拥有的菜单修改
	 * 
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/role/roleEditMenu")
	public void roleEditMenu() throws Exception {
		String menuIdStr = request.getParameter("menuId");
		String[] menuIdArr = {};
		if (menuIdStr != null && menuIdStr.length() > 0) {
			menuIdArr = menuIdStr.split(",");
		}
		roleService.roleMenuUpdate(this.model.getId(), menuIdArr);
		printResult(MessageUtil.getMessage("I10002"), true);
	}

	/**
	 * 角色拥有的菜单修改查询
	 * 
	 * @throws Exception 异常
	 * @return 页面
	 */
	@Action(value = "/modules/system/role/roleEditMenuPage")
	public String roleEditMenuPage() throws Exception {
		long id = this.paramLong("id");
		if (id <= 0) {
			printResult(MessageUtil.getMessage("I10007"), false);
			return "roleEditMenuPage";
		}
		Role role = roleService.getRoleById(id);
		List<Menu> menuList = menuService.menuUseList();
		List<RoleMenu> roleMenuList = role.getRoleMenus();
		request.setAttribute("roleMenuList", roleMenuList);
		request.setAttribute("role", role);
		request.setAttribute("menuList", menuList);
		request.setAttribute("id", id);
		return "roleEditMenuPage";
	}

}
