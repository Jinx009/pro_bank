package com.rongdu.p2psys.nb.user.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.User;

public interface UserDao extends BaseDao<User>
{
	/**
	 * 通过第三方信息获取User
	 * 
	 * @param attributeName
	 * @param attributeValue
	 * @param attributeFactoryName
	 * @param attributeFactoryId
	 * @return
	 */
	public User getByAttribute(String attributeName,String attributeValue,String attributeFactoryName,String attributeFactoryId);
	
	/**
	 * 通过GroupId获取User列表
	 * 
	 * @param groupId
	 * @return
	 */
	public List<User> getByGroupId(Long groupId);
	
	/**
	 * 通过cardId获取已认证信息
	 * 
	 * @param cardId 身份证号
	 * @param channelKey 支付通道
	 * @return 0不存在，1存在
	 */
	public int getUserByCardId(String cardId,String channelKey);
	
	/**
	 * 通过cardId获取已认证信息
	 * 
	 * @param cardId 身份证号
	 */
	public User getUserByCardId(String cardId);
	
	/**
	 * 保存用户
	 * @param user
	 * @return
	 */
	public User saveUser(User user);
	
	/**
	 * 更新User
	 * 
	 * @param user
	 */
	public void updateUser(User user);
	
	/**
	 * 用户登陆
	 * 
	 * @param userName
	 * @param pwd
	 * @return
	 */
	public User doLogin(String userName,String pwd);
	
	/**
	 * 通过用户名查找用户
	 * 
	 * @param userName
	 * @return
	 */
	public User getByUserName(String userName);

	public List<User> findWechatUser(String appId);
}
