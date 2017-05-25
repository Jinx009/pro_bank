package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Role;
import com.rongdu.p2psys.core.model.RoleModel;

/**
 * 角色Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月16日
 */
public interface RoleService {

	/**
	 * 添加角色
	 * 
	 * @param role 角色实体
	 * @param menuIdArr 角色拥有的菜单
	 */
	void addRole(Role role, String[] menuIdArr);

	/**
	 * 查询角色
	 * 
	 * @param id 主键ID
	 * @return 角色
	 */
	Role getRoleById(long id);

	/**
	 * 角色修改
	 * 
	 * @param role 角色实体
	 */
	void roleUpdate(Role role);

	/**
	 * 角色查询
	 * 
	 * @return 角色List
	 */
	List<Role> getRoleList();

	/**
	 * 角色删除
	 * 
	 * @param id 主键ID
	 */
	void deleteRole(long id);

	/**
	 * 角色分页
	 * 
	 * @param model 查询参数
	 * @return 分页
	 */
	PageDataList<Role> list(RoleModel model);

	/**
	 * 角色拥有的菜单修改
	 * 
	 * @param roleId 角色ID
	 * @param menuIdArr 角色拥有的菜单
	 */
	void roleMenuUpdate(long roleId, String[] menuIdArr);

	/**
	 * 查询查询用户所拥有的角色
	 * 
	 * @param userId 用户ID
	 * @return 角色List
	 */
	List<Role> getRoleList(long userId);
}
