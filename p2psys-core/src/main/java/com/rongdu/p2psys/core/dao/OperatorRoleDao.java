package com.rongdu.p2psys.core.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.OperatorRole;

public interface OperatorRoleDao extends BaseDao<OperatorRole> {

	/**
	 * 根据用户ID删除用户角色关联信息
	 * 
	 * @param userId 用户ID
	 */
	void deleteByUserId(long userId);
}
