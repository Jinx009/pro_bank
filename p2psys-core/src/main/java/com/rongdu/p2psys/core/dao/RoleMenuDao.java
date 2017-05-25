package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.RoleMenu;

public interface RoleMenuDao extends BaseDao<RoleMenu> {

	/**
	 * 删除角色菜单关联表信息（物理删除）
	 * 
	 * @param roleId 角色ID
	 */
	void deleteByRoleId(long roleId);

	/**
	 * 角色菜单关联信息查询
	 * 
	 * @param roleId 角色ID
	 * @return 角色List
	 */
	List<RoleMenu> getRoleMenuList(long roleId);
}
