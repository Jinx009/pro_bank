package com.rongdu.p2psys.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.model.UserCacheModel;

/**
 * 用户附属信息Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月20日
 */
public interface UserCacheDao extends BaseDao<UserCache> {

	/**
	 * 我的基本信息
	 * 
	 * @param userId
	 * @return userCacheModel
	 */
	UserCacheModel getUserCache(long userId);

	/**
	 * 实名认证更新证件号码
	 * 
	 * @param userId real_name
	 * @return
	 */
	void modify(long userId, String card_id);

	/**
	 * 实名认证更新证件号码 证件号码正反面
	 * 
	 * @param userId real_name
	 * @return
	 */
	void modify(long userId, String card_id, String card_positive, String card_opposite);

	/**
	 * 更新修改密码时间
	 * 
	 * @param userId
	 */
	void modifyPwdTime(long userId);
	
	/**
	 * 更新修改交易密码时间
	 * 
	 * @param userId
	 */
	void modifyPayPwdTime(long userId);

	/**
	 * 锁定用户
	 * 
	 * @param userId
	 */
	void userLockEdit(long userId, int status);

	/**
	 * get userCache
	 * 
	 * @param UserId
	 * @return
	 */
	UserCache getUserCacheByUserId(long UserId);

	/**
	 * @param userId
	 * @return
	 */
	UserCache findByUserId(long userId);

	void updateStatus();
	/**
	 * 修改用户的第三方账号和状态
	 * @param userId 用户ID
	 * @param api_id 第三方账号
	 * @param api_status 第三方账号状态
	 */
	void modifyApi(long userId, String api_id, String api_status,String apiUserCustId);

	/**
	 * 查询营业执照编号
	 * @param businessRegistrationNumber
	 * @return
	 */
	int countByBusinessRegistrationNumber(String businessRegistrationNumber,long userId);
}
