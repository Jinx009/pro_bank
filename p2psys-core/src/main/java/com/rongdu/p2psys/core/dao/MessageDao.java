package com.rongdu.p2psys.core.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Message;
import com.rongdu.p2psys.core.model.MessageModel;

/**
 * 站内信Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月28日
 */
public interface MessageDao extends BaseDao<Message> {

	/**
	 * 收件箱未读计数
	 * 
	 * @param userId
	 * @return
	 */
	int unreadCount(long receiveUser);

	/**
	 * 收件箱列表
	 * 
	 * @param receive_user
	 * @param start
	 * @param pernum
	 * @return
	 */
	PageDataList<MessageModel> receiveList(long receiveUser, int startPage);

	/**
	 * 发件箱列表
	 * 
	 * @param sent_user
	 * @param start
	 * @param pernum
	 * @return
	 */
	PageDataList<MessageModel> sentList(long sentUser, int startPage);

	/**
	 * 获取
	 * 
	 * @param id
	 * @return
	 */
	MessageModel find(long id, long userId);

	/**
	 * 标记为已读
	 * 
	 * @param id
	 * @param userId
	 */
	void setRead(long id, long receiveUser);

	/**
	 * 批量删除收件箱信息
	 * 
	 * @param ids
	 */
	void delReceiveList(Object[] ids, long receiveUser);

	/**
	 * 批量删除发件箱信息
	 * 
	 * @param ids
	 */
	void delSentList(Object[] ids, long sentUser);

	/**
	 * 批量设置已读
	 * 
	 * @param ids
	 */
	void setReadList(Object[] ids, long receiveUser);

	/**
	 * 批量设置未读
	 * 
	 * @param ids
	 */
	void setUnreadList(Object[] ids, long receiveUser);

}
