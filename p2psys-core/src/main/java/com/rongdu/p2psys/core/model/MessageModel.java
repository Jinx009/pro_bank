package com.rongdu.p2psys.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.domain.Message;
import com.rongdu.p2psys.core.exception.MessageException;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserNoticeConfig;

/**
 * 站内信Model
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月5日
 */
public class MessageModel extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6938216832515701555L;

	/** 当前页码 */
	private int page;

	/** 收件人用户名 */
	private String receiveUserName;
	/** 发件人用户名 */
	private String sentUserName;
	/** 验证码 */
	private String validCode;
	
	private List<UserNoticeConfig> list = new ArrayList<UserNoticeConfig>();

	public Message prototype(User sentUser, User receiveUser) {
		Message message = new Message(sentUser, receiveUser);
		BeanUtils.copyProperties(this, message, new String[] { "sentUser", "receiveUser", "status", "type", "addTime",
				"addIp" });
		return message;
	}

	public Message prototype(MessageModel msg, String content) {
		Message message = new Message(msg, content);
		BeanUtils.copyProperties(this, message, new String[] { "sentUser", "receiveUser", "status", "type", "addTime",
				"addIp", "title", "content" });
		return message;
	}

	public static MessageModel instance(Message message) {
		MessageModel messageModel = new MessageModel();
		BeanUtils.copyProperties(message, messageModel);
		return messageModel;
	}

	public void validSendMessage(User receiveUser) {
		if (StringUtil.isBlank(this.receiveUserName)) {
			throw new MessageException("收件人不能为空！", 1);
		} else if (StringUtil.isBlank(getTitle())) {
			throw new MessageException("标题不能为空！", 1);
		} else if (StringUtil.isBlank(getContent())) {
			throw new MessageException("内容不能为空！", 1);
		} else if (StringUtil.isBlank(this.validCode) || !ValidateUtil.checkValidCode(this.validCode)) {
			throw new MessageException("验证码错误!", 1);
		} else if (receiveUser == null) {
			throw new MessageException("收件人不存在！", 1);
		}
	}

	/**
	 * 批量操作 校验ids
	 * 
	 * @param ids
	 * @return
	 */
	public String validSet(String[] ids) {
		String ids_ = Arrays.toString(ids);
		if (StringUtil.isBlank(ids_)) {
			throw new MessageException("请选择需要处理的数据！", 1);
		}
		return ids_.replaceAll("\\[", "").replaceAll("\\]", "");
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	public String getSentUserName() {
		return sentUserName;
	}

	public void setSentUserName(String sentUserName) {
		this.sentUserName = sentUserName;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public List<UserNoticeConfig> getList() {
		return list;
	}

	public void setList(List<UserNoticeConfig> list) {
		this.list = list;
	}
	
}
