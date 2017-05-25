package com.rongdu.p2psys.user.dao;

import java.util.List;
import java.util.Map;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;

/**
 * 用户DAO接口
 * 
 * @author ZhuJunjie
 */

public interface UserDao extends BaseDao<User>
{

	/**
	 * 根据时间段统计用户数量
	 * 
	 * @param startTime
	 * @param endTime
	 * @return 用户数量
	 */
	int count(String startTime, String endTime);

	/**
	 * 统计除此用户以外的用户是否存在该邮箱
	 * 
	 * @param email
	 * @param userId
	 * @return 用户数量
	 */
	int countByEmail(String email, long userId);

	/**
	 * 修改用户密码
	 * 
	 * @param user
	 * @return 用户实体类
	 */
	User modifyPwd(User user);

	/**
	 * 修改用户支付密码
	 * 
	 * @param user
	 * @return 用户实体类
	 */
	User modifyPaypwd(User user);

	/**
	 * 修改真实姓名
	 * 
	 * @param userId
	 * @param realName
	 */
	void modifyRealname(long userId, String realName);

	/**
	 * 修改邮箱
	 * 
	 * @param userId
	 * @param email
	 */
	void modifyEmail(long userId, String email);

	/**
	 * 修改手机号码
	 * 
	 * @param userId
	 * @param phone
	 */
	void modifyPhone(long userId, String phone);
	
	PageDataList<UserModel> userList(int pageNumber, int pageSize,UserModel model);
	
	public List<UserModel> getUserModels(Map<String,Object> param);

	public List<User> getByGroupId(Long bindId);
}
