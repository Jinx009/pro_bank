package com.rongdu.p2psys.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.model.UserIdentifyModel;

/**
 * 认证信息
 * 
 * @author sj
 * @version 2.0
 * @since 2014年2月17日17:27:02
 */
public interface UserIdentifyDao extends BaseDao<UserIdentify> {

	/**
	 * 
	 * @param userId
	 * @return
	 */
	UserIdentify findByUserId(long userId);

	/**
	 * 更新实名认证状态real_name_status
	 * 
	 * @param userId
	 */
	void modifyRealnameStatus(long userId, int status, int preStatus);
	/**
     * 更新实名认证状态real_name_status
     * 
     * @param userId
     */
    void modifyRealnameStatus(long userId, int status);

	/**
	 * 更新邮箱激活状态email_status
	 * 
	 * @param userId
	 */
	void modifyEmailStatus(long userId, int status, int preStatus);

	/**
	 * 更新手机绑定状态mobile_phone_status
	 * 
	 * @param userId
	 */
	void modifyMobilePhoneStatus(long userId, int status, int preStatus);
	/**
     * 更新手机绑定状态mobile_phone_status
     * 
     * @param userId
     */
    void modifyMobilePhoneStatus(long userId, int status);

	/**
	 * 得到单个用户的认证状态
	 * 
	 * @param userId
	 * @return
	 */
	UserIdentifyModel getUserIdentifyByUserId(long userId);

	/**
	 * 审核用户认证信息
	 * 
	 * @param id
	 * @param realNameVerifyRemark
	 * @param realNameStatus
	 * @param user
	 */
	void userAttestationEdit(long id, String realNameVerifyRemark, int realNameStatus, Operator operator);

	/**
	 * 根据实名认证状态统计总数
	 * 
	 * @param status
	 * @return
	 */
	int countByRealName(int status);
	
	/**
	 * 根据状态及时间统计实名认证总数
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	int countByRealName(int status, String startTime, String endTime);

	/**
	 * 根据手机号计数 注意:已经手机认证的用户
	 * 
	 * @param mobilePhone
	 * @return
	 */
	int countByMobilePhone(String mobilePhone);

	/**
	 * 统计邮箱：已经认证的用户
	 * 
	 * @param email
	 * @return
	 */
	int countByEmail(String email);

	/**
	 * 更具主键查询
	 * 
	 * @param userIdentifyId
	 * @return
	 */
	UserIdentify findById(long userIdentifyId);

}
