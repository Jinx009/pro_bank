package com.rongdu.p2psys.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.UserCreditApply;

/**
 * 信用额度申请Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月25日
 */
public interface UserCreditApplyDao extends BaseDao<UserCreditApply> {

	/**
	 * 根据用户ID计数
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	int count(long userId);

	/**
	 * 根据用户ID及状态计数
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	int count(long userId, int status);

}
