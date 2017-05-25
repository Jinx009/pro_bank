package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Role;
import com.rongdu.p2psys.core.model.RoleModel;

/**
 * TODO 类说明</p>
 * 
 * @author：Administrator
 * @version 1.0
 * @since 2014年7月14日
 */
public interface RoleDao extends BaseDao<Role> {

	/**
	 * @param model
	 * @return
	 */
	PageDataList<Role> list(RoleModel model);

	/**
	 * 角色查询
	 * 
	 * @return 角色List
	 */
	List<Role> roleList();

	/**
	 * 查询角色
	 * 
	 * @param id 主键ID
	 * @return 角色实体
	 */
	Role roleFind(long id);

	/**
	 * 查询查询用户所拥有的角色
	 * 
	 * @param userId 用户ID
	 * @return 角色List
	 */
	List<Role> getRoleList(long userId);
}
