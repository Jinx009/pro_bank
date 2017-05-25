package com.rongdu.p2psys.core.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.RoleDao;
import com.rongdu.p2psys.core.dao.RoleMenuDao;
import com.rongdu.p2psys.core.domain.Menu;
import com.rongdu.p2psys.core.domain.Role;
import com.rongdu.p2psys.core.domain.RoleMenu;
import com.rongdu.p2psys.core.model.RoleModel;
import com.rongdu.p2psys.core.service.RoleService;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	/**
	 * 角色DAO
	 */
	@Resource
	private RoleDao roleDao;
	/**
	 * 角色菜单DAO
	 */
	@Resource
	private RoleMenuDao roleMenuDao;

	public PageDataList<Role> list(RoleModel model) {
		return roleDao.list(model);
	}

	public void addRole(Role role, String[] menuIdArr) {
		role.setUpdateTime(new Date());
		role.setAddTime(new Date());
		Role r = roleDao.save(role);
		for (int i = 0; i < menuIdArr.length; i++) {
			String menuIdStr = menuIdArr[i];
			long menuId = Long.parseLong(menuIdStr);
			Menu menu = new Menu(menuId);
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setMenu(menu);
			roleMenu.setRole(r);
			roleMenuDao.save(roleMenu);
		}
	}

	public Role getRoleById(long id) {
		return roleDao.roleFind(id);
	}

	public void roleUpdate(Role role) {
		role.setUpdateTime(new Date());
		roleDao.update(role);
	}

	public List<Role> getRoleList() {
		return roleDao.roleList();
	}

	public void deleteRole(long id) {
		Role role = roleDao.roleFind(id);
		if (role == null) {
			return;
		}
		role.setUpdateTime(new Date());
		role.setDelete(true);
		roleDao.update(role);
	}

	public PageDataList<Role> getRolePageList(QueryParam param) {
		return roleDao.findPageList(param);
	}

	public void roleMenuUpdate(long roleId, String[] menuIdArr) {
		Role r = roleDao.roleFind(roleId);
		if (r == null) {
			return;
		}
		roleMenuDao.deleteByRoleId(roleId);
		for (int i = 0; i < menuIdArr.length; i++) {
			String menuIdStr = menuIdArr[i];
			long menuId = Long.parseLong(menuIdStr);
			Menu menu = new Menu(menuId);
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setMenu(menu);
			roleMenu.setRole(r);
			roleMenuDao.save(roleMenu);
		}
	}

	public List<Role> getRoleList(long userId) {
		return roleDao.getRoleList(userId);
	}
}
