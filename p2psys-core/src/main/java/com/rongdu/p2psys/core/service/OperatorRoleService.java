package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.domain.OperatorRole;

/**
 * 用户角色关联信息service接口
 * 
 * @author zhangyz
 * @date 2014-04-02
 */
public interface OperatorRoleService {

	/**
	 * 用户角色关联信息查询
	 * 
	 * @param userId 角色ID
	 * @return 关联信息List
	 */
	List<OperatorRole> getUserRoleList(long userId);

	/**
	 * @return
	 */
	List<OperatorRole> getKefuList();

	/**
	 * @param customerUserId
	 * @return
	 */
	OperatorRole getOperatorRole(int customerUserId);

	/**
	 * @param param
	 * @return
	 */
	PageDataList<OperatorRole> getOperatorRole(QueryParam param);
}
