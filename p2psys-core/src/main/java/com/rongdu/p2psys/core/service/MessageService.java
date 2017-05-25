package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Message;
import com.rongdu.p2psys.core.model.MessageModel;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserNoticeConfig;

/**
 * 站内信Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月28日
 */
public interface MessageService {

	/**
	 * 收件箱未读计数
	 * 
	 * @param userId
	 * @return
	 */
	int unreadCount(long receive_user);

	/**
	 * 收件箱列表
	 * 
	 * @param userId
	 * @param startPage
	 * @return
	 */
	PageDataList<MessageModel> receiveList(long userId, int startPage);

	/**
	 * 发件箱列表
	 * 
	 * @param userId
	 * @param startPage
	 * @return
	 */
	PageDataList<MessageModel> sentList(long userId, int startPage);

	/**
	 * 获取
	 * 
	 * @param id
	 * @return
	 */
	MessageModel find(long id, long userId);

	/**
	 * 新增
	 * 
	 * @param msg
	 * @return
	 */
	Message add(Message msg);

	/**
	 * 批量操作
	 * 
	 * @param ids
	 * @param userId
	 */
	void set(int type, Object[] ids, long userId);

	/**
	 * 信息回复
	 * 
	 * @param m
	 */
	void reply(Message m);

	/**
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageDataList<Message> messageList(Message model, int pageNumber, int pageSize);

	/**
	 * 用户通知设置
	 */
	public void noticeUserSet(List<UserNoticeConfig> list, User user);

	/**
	 * 获得用户的通知配置列表
	 * @param userId
	 * @return
	 */
	public List<UserNoticeConfig> getAllUNConfigs(long userId);

}
