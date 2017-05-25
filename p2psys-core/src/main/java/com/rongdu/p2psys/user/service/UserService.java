package com.rongdu.p2psys.user.service;

import java.util.Collection;
import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.UserModel;

/**
 * 用户Service
 * 
 * @author ZhuJunjie
 */
public interface UserService
{

	/**
	 * 根据userId获取user
	 * 
	 * @param userId
	 *            用户ID
	 * @return User实体类
	 */
	User find(long userId);
	/*
	 * 统计
	 */

	/**
	 * 统计用户数量
	 * 
	 * @return 用户数量
	 */
	int count(QueryParam param);

	/**
	 * 根据时间段统计用户数量
	 * 
	 * @param startTime
	 * @param endTime
	 * @return 用户数量
	 */
	int count(String startTime, String endTime);

	/**
	 * 根据用户名计数
	 * 
	 * @param userName
	 * @return 用户数量
	 */
	int countByUserName(String userName);

	/**
	 * 根据身份证计数
	 * 
	 * @param cardId
	 * @return 用户数量
	 */
	int countByCardId(String cardId);

	/**
	 * 根据手机计数
	 * 
	 * @param mobilePhone
	 * @return 用户数量
	 */
	int countByMobilePhone(String mobilePhone);

	/**
	 * 根据邮箱计数
	 * 
	 * @param email
	 * @return 用户数量
	 */
	int countByEmail(String email);

	/**
	 * 统计除此用户以外的用户是否存在该邮箱
	 * 
	 * @param email
	 * @param userId
	 * @return 用户数量
	 */
	int countByEmail(String email, long userId);

	/*
	 * 获取用户信息
	 */

	/**
	 * 根据绑定ID获取用户的集合
	 * 
	 * @param bindId
	 * @return 用户实体类集合
	 */
	Collection<User> getUsersByBindId(long bindId);

	/**
	 * 根据用户ID获取用户
	 * 
	 * @param userId
	 * @return 用户实体类
	 */
	User getUserById(long userId);
	

	/**
	 * 根据用户名获取用户
	 * 
	 * @param userName
	 * @return 用户实体类
	 */
	User getUserByUserName(String userName);

	/**
	 * 根据手机号获取用户
	 * 
	 * @param mobilePhone
	 * @return 用户实体类
	 */
	User getUserByMobilePhone(String mobilePhone);

	/**
	 * 根据邮箱获取用户
	 * 
	 * @param email
	 * @return 用户实体类
	 */
	User getUserByEmail(String email);

	/**
	 * 根据用户真实姓名获取用户
	 * 
	 * @param userName
	 * @return 用户实体类
	 */
	User getUserByRealName(String realName);

	/**
	 * 获得用户信息列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return 用户信息列表
	 */
	PageDataList<UserModel> userList(int pageNumber, int pageSize,
			UserModel model);

	/*
	 * 修改用户信息
	 */

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
	 * 修改邮箱
	 * 
	 * @param userId
	 * @param email
	 */
	void modifyEmail(long userId, String email);

	/**
	 * 修改手机号及状态
	 * 
	 * @param userId
	 * @param phone
	 * @param status
	 */
	void modifyPhone(long userId, String phone, int status);

	/**
	 * 修改用户信息
	 * 
	 * @param user
	 */
	void updateUserInfo(User user);

	/*
	 * 业务操作
	 */

	/**
	 * 注册
	 * 
	 * @param model
	 * @return 用户实体类
	 * @throws Exception
	 */
	User doRegister(UserModel model) throws Exception;

	/**
	 * 登录
	 * 
	 * @param user
	 * @param isRsa
	 * @return 用户实体类
	 * @throws Exception
	 */
	User doLogin(User user, int isRsa) throws Exception;

	/**
	 * 激活邮箱
	 * 
	 * @param idStr
	 * @return 用户实体类
	 * @throws Exception
	 */
	User activationEmail(String idStr) throws Exception;

	/*
	 * 取回密码
	 */

	/**
	 * 找回密码 - 通过邮箱找回
	 * 
	 * @param userName
	 * @param email
	 * @return 用户实体类
	 */
	User getPwdByEmail(String userName, String email);

	/**
	 * 找回密码 - 通过手机找回
	 * 
	 * @param userName
	 * @param mobilePhone
	 * @return 用户实体类
	 */
	User getPwdByPhone(String userName, String mobilePhone);

	/**
	 * 找回交易密码 - 通过邮箱找回
	 * 
	 * @param userName
	 * @param email
	 * @return 用户实体类
	 */
	User getPayPwdByEmail(String userName, String email);

	/**
	 * 找回交易密码 - 通过手机找回
	 * 
	 * @param userName
	 * @param mobilePhone
	 * @return 用户实体类
	 */
	User getPayPwdByPhone(String userName, String mobilePhone);

	/*
	 * 其他
	 */

	/**
	 * 节假日发送短信祝福短信
	 */
	void doNoticeMsg();
	
	/**
	 * 更新真实姓名和身份证号码
	 * 
	 * @param user
	 */
	public void updateUser(User user);
	
	/**
	 * 根据bindId(多账户Id)查找用户列表信息
	 * @param bindId
	 * @return
	 */
	List<User> findByBindId(Long bindId);

	public List<User> getByGroupId(Long bindId);
}
