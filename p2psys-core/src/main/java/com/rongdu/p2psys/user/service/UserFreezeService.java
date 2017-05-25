package com.rongdu.p2psys.user.service;

import java.util.Map;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.domain.UserFreeze;
import com.rongdu.p2psys.user.model.UserFreezeModel;

public interface UserFreezeService {

	/**
	 * 冻结列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param f
	 * @return
	 */
	public PageDataList<UserFreezeModel> freezeList(int pageNumber, int pageSize, UserFreezeModel model);

	/**
	 * 新增保存
	 * 
	 * @param model
	 */
	public Map<String, Object> freezeAdd(UserFreezeModel model, Operator operator);

	/**
	 * 查询实体
	 * 
	 * @param id
	 * @return
	 */
	public UserFreeze find(long id);

	/**
	 * 修改
	 * 
	 * @param model
	 */
	public String freezeEdit(UserFreezeModel model, Operator operator);

	/**
	 * 删除(启用，未启用)
	 * 
	 * @param id
	 * @param status
	 */
	public void freezeDelete(long id, int status);

	/**
	 * 通过userName获得Freeze
	 * 
	 * @param userName
	 * @return
	 */
	public UserFreeze getByUserName(String userName);

	/**
	 * 通过userId获得Freeze
	 * 
	 * @param userId
	 * @return
	 */
	public UserFreeze getByUserId(long userId);
}
