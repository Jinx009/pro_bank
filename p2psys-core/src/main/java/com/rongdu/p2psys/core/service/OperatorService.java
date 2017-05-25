package com.rongdu.p2psys.core.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.domain.Operator;

public interface OperatorService {

	/**
	 * 管理员登录
	 * 
	 * @param userName
	 * @param password
	 * @param uchoncode
	 * @return 管理员
	 * @throws Exception
	 */
	Operator login(String userName, String password, String uchoncode)
			throws Exception;

	/**
	 * 添加用户方法
	 * 
	 * @param operator
	 *            用户对象
	 * @param roleIdArr
	 *            用户选择角色数组
	 */
	void addOperator(Operator operator, String[] roleIdArr);

	/**
	 * 用户查询
	 * 
	 * @param id
	 *            主键ID
	 * @return 用户信息
	 */
	Operator getUserById(long id);

	/**
	 * 用户修改
	 * 
	 * @param user
	 *            用户
	 * @param roleIdArr
	 *            用户选择角色数组
	 */
	void userUpdate(Operator user, String[] roleIdArr);

	/**
	 * 用户删除
	 * 
	 * @param id
	 *            主键ID
	 */
	void userDelete(long id);

	/**
	 * 用户分页
	 * 
	 * @param param
	 *            查询参数
	 * @return 分页
	 */
	PageDataList<Operator> getUserPageList(QueryParam param);

	/**
	 * 修改用户
	 * 
	 * @param user
	 *            用户
	 */
	void userUpdate(Operator user);

	/**
	 * 修改用户密码
	 * 
	 * @param user
	 *            用户
	 */
	void updateUserPwd(Operator user);

	/**
	 * 根据用户名查询用户信息
	 * 
	 * @param userName
	 *            用户名
	 * @return 用户信息
	 */
	Operator getUserByUserName(String userName);

	// List<Operator> getOperators(long id);
}
