package com.rongdu.p2psys.nb.user.service;

import java.util.List;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.user.domain.User;

public interface UserService 
{
	public User getByUserName(String userName);
	
	public User doLogin(String userName,String pwd);
	
	public User getByAttribute(String attributeName,String attributeValue,String attributeFactoryName,String attributeFactoryId);
	
	public List<User> getByGroupId(Long groupId);
	
	public User saveUser(User user);
	
	public void updateUser(User user);
	
	public User getByUserId(long user_id);
	
	/**
	 * 根据身份证、通道计数实名且绑了该通道的信息
	 * 
	 * @param cardId 身份证号
	 * @param channelKey 支付通道
	 * @return 0不存在，1存在
	 */
	int countByCardId(String cardId,String channelKey);
	
	/**
	 * 根据身份证号查询对应实名用户
	 * @param cardId
	 * @return
	 */
	public User getUserByCardId(String cardId);
	
	public User saveWechatUser(User user);
	
	/**
	 * 统计用户数量
	 * 
	 * @return 用户数量
	 */
	int count(QueryParam param);
	
	public User savePcUser(User user,String inviteCode);
	
	public List<User> getWechatUserList(String appId);
	
}
