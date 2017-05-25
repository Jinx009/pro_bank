package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.p2psys.core.domain.RoleMenu;

/**
 * 角色菜单关联信息service接口
 * 
 * @author zhangyz
 * @date 2014-3-20
 */
public interface RoleMenuService {

	/**
	 * 角色菜单关联信息查询
	 * 
	 * @param roleId 角色ID
	 * @return 角色List
	 */
	List<RoleMenu> getRoleMenuList(long roleId);

}
