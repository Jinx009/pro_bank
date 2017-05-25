package com.rongdu.p2psys.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.user.domain.UserFreeze;
import com.rongdu.p2psys.user.model.UserFreezeModel;

public interface UserFreezeDao extends BaseDao<UserFreeze> {

	/**
	 * 冻结列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return PageDataList
	 */
	PageDataList<UserFreezeModel> freezeList(int pageNumber, int pageSize, UserFreezeModel model);

	/**
	 * 通过用户名判断是否有该用户已经被冻结
	 * 
	 * @param userName
	 * @return boolean 
	 */
	boolean isExistsByUserName(String userName);

	/**
	 * 删除(启用，未启用)
	 * 
	 * @param id
	 * @param status
	 */
	void freezeDelete(long id, int status);

	/**
	 * 通过userName获得Freeze
	 * 
	 * @param userName
	 * @return UserFreeze
	 */
	UserFreeze getByUserName(String userName);

	/**
	 * 通过userId获得Freeze
	 * 
	 * @param userId
	 * @return UserFreeze
	 */
	UserFreeze getByUserId(long userId);

}
