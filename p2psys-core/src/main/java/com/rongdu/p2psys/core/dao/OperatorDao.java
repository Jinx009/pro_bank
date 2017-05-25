package com.rongdu.p2psys.core.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.Operator;

/**
 * 管理员DAO
 */
public interface OperatorDao extends BaseDao<Operator> {

	/**
	 * 根据用户名查询管理员
	 * 
	 * @param userName
	 * @return 管理员
	 */
	Operator getOperatorByName(String userName);

	// TODO

	/**
	 * 用户查询
	 * 
	 * @param id
	 *            主键ID
	 * @return 用户信息
	 */
	Operator userFind(long id);

	/**
	 * 根据用户名查询用户信息
	 * 
	 * @param userName
	 *            用户名
	 * @return 用户信息
	 */
	Operator getManagerByUserName(String userName);

}
