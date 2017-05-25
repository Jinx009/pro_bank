package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.MessageDao;
import com.rongdu.p2psys.core.domain.Message;
import com.rongdu.p2psys.core.model.MessageModel;
import com.rongdu.p2psys.core.service.MessageService;
import com.rongdu.p2psys.user.dao.UserNoticeConfigDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserNoticeConfig;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Resource
	private MessageDao messageDao;
	@Resource
	private UserNoticeConfigDao userNoticeConfigDao;
	
	@Override
	public int unreadCount(long receive_user) {
		return messageDao.unreadCount(receive_user);
	}

	@Override
	public PageDataList<MessageModel> receiveList(long userId, int startPage) {
		return messageDao.receiveList(userId, startPage);
	}

	@Override
	public PageDataList<MessageModel> sentList(long userId, int startPage) {
		return messageDao.sentList(userId, startPage);
	}

	@Override
	public MessageModel find(long id, long userId) {
		MessageModel model = messageDao.find(id, userId);
		if (model.getStatus() == 0) {
			messageDao.setRead(id, userId);
		}
		return model;
	}

	@Override
	public Message add(Message msg) {
		return (Message) messageDao.save(msg);
	}

	@Override
	public void set(int type, Object[] ids, long userId) {
		switch (type) {
			case 1:
				messageDao.delReceiveList(ids, userId);
				break;
			case 2:
				messageDao.delSentList(ids, userId);
				break;
			case 3:
				messageDao.setUnreadList(ids, userId);
				break;
			case 4:
				messageDao.setReadList(ids, userId);
				break;
			default:

				break;
		}
	}

	@Override
	public void reply(Message m) {
		messageDao.save(m);
	}

	@Override
	public PageDataList<Message> messageList(Message model, int pageNumber, int pageSize) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize);
		if (!StringUtil.isBlank(model.getTitle())) {
			param.addParam("title", Operators.EQ, model.getTitle());
		}
		return messageDao.findPageList(param);
	}

	@Override
	public void noticeUserSet(List<UserNoticeConfig> list, User user) {
		userNoticeConfigDao.update(list);	
	}

	@Override
	public List<UserNoticeConfig> getAllUNConfigs(long userId) {
		List<UserNoticeConfig> allUNConfigs = userNoticeConfigDao.getAllUNConfigs(userId);
		if(allUNConfigs != null && allUNConfigs.size() > 0){
			return allUNConfigs;
		}
		return null;
	}
}
