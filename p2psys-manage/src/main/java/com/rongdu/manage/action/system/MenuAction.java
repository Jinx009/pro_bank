package com.rongdu.manage.action.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.domain.Menu;
import com.rongdu.p2psys.core.service.MenuService;
import com.rongdu.p2psys.core.web.BaseAction;

@SuppressWarnings("rawtypes")
public class MenuAction extends BaseAction implements ModelDriven<Menu> {

	@Resource
	private MenuService menuService;

	private Menu menu = new Menu();

	public Menu getModel() {
		return menu;
	}

	private Map<String, Object> data;

	/**
	 * 获取用户拥有的所有菜单集合
	 * 
	 * @throws Exception
	 */
	@Action(value = "/modules/system/menu/getMenuList")
	public void getMenuList() throws Exception {
		List<Menu> menuList = menuService.getMenuUseList(getOperatorId(),
				this.paramLong("parentId"), Menu.IS_MENU);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rows", menuList);
		printJson(getStringOfJpaObj(params));
	}

	/**
	 * 获取菜单集合
	 * 
	 * @return 菜单管理页面
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/menu/menuManager")
	public String menuManager() throws Exception {
		return "menuManager";
	}

	/**
	 * 获取菜单集合
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/menu/menuList")
	public void menuList() throws Exception {
		List<Menu> menuList = menuService.menuUseList();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rows", menuList);
		printJson(getStringOfJpaObj(params));
	}

	/**
	 * 获取用户拥有的所有菜单集合
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/menu/leftMenuList")
	public void leftMenuList() throws Exception {
		List<Menu> menuList = menuService.menuUseList(getOperatorId(),
				Menu.IS_MENU);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rows", menuList);
		printJson(getStringOfJpaObj(params));
	}

	/**
	 * 获取用户拥有的所有权限
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/menu/myMenuList")
	public void myMenuList() throws Exception {
		List<Menu> menuList = menuService.menuUseList(getOperatorId(),
				Menu.MENU_ALL);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rows", menuList);
		printJson(getStringOfJpaObj(params));
	}

	/**
	 * 添加菜单页面
	 * 
	 * @return 返回页面
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/menu/menuAddPage")
	public String menuAddPage() throws Exception {
		long pid = paramLong("id"); // 获取id，设置所添加菜单的下级pid
		request.setAttribute("pid", pid);
		saveToken("menuAddToken");
		return "menuAddPage";
	}

	/**
	 * 添加菜单
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/menu/menuAdd")
	public void menuAdd() throws Exception {
		data = new HashMap<String, Object>();
		checkToken("menuAddToken");
		menuService.menuAdd(menu);
		data.put("result", true);
		data.put("msg", "添加菜单成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 编辑菜单项页面
	 * 
	 * @return 返回页面
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/menu/menuEditPage")
	public String menuEditPage() throws Exception {
		saveToken("menuEditToken");
		long id = paramLong("id");
		Menu menu = menuService.menuFind(id);
		request.setAttribute("menuItem", menu);
		request.setAttribute("isMenu", menu.isMenu());
		return "menuEditPage";
	}

	/**
	 * 编辑菜单项
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/menu/menuEdit")
	public void menuEdit() throws Exception {
		data = new HashMap<String, Object>();
		checkToken("menuEditToken");
		menu.setUpdateUser(this.getOperatorUserName());
		String isMenu = this.paramString("isMenu");
		boolean is_menu = isMenu.equals("true") ? true : false;
		menu.setMenu(is_menu);
		menuService.menuUpdate(menu);
		data.put("result", true);
		data.put("msg", "修改菜单成功！");
		printJson(getStringOfJpaObj(data));
	}

	/**
	 * 理论删除，将数据库中的状态修改掉
	 * 
	 * @throws Exception
	 *             异常
	 */
	@Action(value = "/modules/system/menu/menuDelete")
	public void menuDelete() throws Exception {
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		menuService.menuDelete(id);
		data.put("result", true);
		data.put("msg", "删除菜单成功！");
		printJson(getStringOfJpaObj(data));
	}

}
