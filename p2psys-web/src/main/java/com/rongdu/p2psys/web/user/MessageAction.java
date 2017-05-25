package com.rongdu.p2psys.web.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Message;
import com.rongdu.p2psys.core.model.MessageModel;
import com.rongdu.p2psys.core.service.MessageService;
import com.rongdu.p2psys.core.util.ValidateUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserNoticeConfig;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 站内信
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月28日
 */
public class MessageAction extends BaseAction implements ModelDriven<MessageModel> {

	private MessageModel model = new MessageModel();

	@Override
	public MessageModel getModel() {
		return model;
	}

	@Resource
	private MessageService messageService;
	@Resource
	private UserService userService;

	private User user;
	private Map<String, Object> data;

	/**
	 * 站内信箱页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value="/member/message/detail",results = { @Result(name = "log", type = "ftl", location = "/member/message/detail.html"),
			@Result(name = "detail_firm", type = "ftl", location = "/member_borrow/message/detail.html")})
	public String detail() throws Exception {
		user = getSessionUser();
		if (user.getUserCache().getUserType() == 3 && paramInt("borrow") == 1 || user.getUserCache().getUserType() == 2) {
			return "detail_firm";
		}
		return "detail";
	}

	/**
	 * 读取收件箱
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/message/box")
	public void box() throws Exception {
		user = getSessionUser();
		PageDataList<MessageModel> pageDataList = messageService.receiveList(user.getUserId(), model.getPage());
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 已发送邮件页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/message/sent")
	public String sent() throws Exception {
		return "sent";
	}

	/**
	 * 已发送邮件
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/message/sentList")
	public void sentList() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		PageDataList<MessageModel> pageDataList = messageService.sentList(userId, model.getPage());
		data = new HashMap<String, Object>();
		data.put("data", pageDataList);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 发消息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/message/send")
	public String send() throws Exception {
		user = getSessionUser();
		List<UserNoticeConfig> list = messageService.getAllUNConfigs(user.getUserId());
		request.setAttribute("user", user);
		request.setAttribute("unConfigList", list);
		return "send";
	}

	/**
	 * 发送消息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/message/doSend")
	public void doSend() throws Exception {
		user = getSessionUser();
		User receiveUser = userService.getUserByUserName(model.getReceiveUserName());
		model.validSendMessage(receiveUser);
		Message m = model.prototype(user, receiveUser);
		messageService.add(m);
		printWebSuccess();
	}

	/**
	 * 判断发送消息验证码是否正确
	 * 
	 * @throws Exception
	 */
	@Action("/member/message/checkValidCode")
	public void checkValidCode() throws Exception {
		data = new HashMap<String, Object>();
		data.put("result", ValidateUtil.checkValidCode(model.getValidCode()));
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 判断用户名是否已存在
	 * 
	 * @throws Exception
	 */
	@Action("/member/message/checkUsername")
	public void checkUsername() throws Exception {
		data = new HashMap<String, Object>();
		int count = userService.countByUserName(model.getReceiveUserName());
		data.put("result", count > 0 ? true : false);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 信息回复
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/message/reply")
	public void reply() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		long id = paramLong("id");
		MessageModel msg = messageService.find(id, userId);
		Message m = model.prototype(msg, paramString("content"));
		m.setSentUser(msg.getReceiveUser());
		m.setReceiveUser(msg.getSentUser());
		m.setId(0);
		messageService.reply(m);
		frontRedirect("/member/message/sent.html");
	}

	/**
	 * 删除信息，已标记已读，已标记未读
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/message/set")
	public void set() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		String[] ids = request.getParameterValues("id");
		messageService.set(paramInt("type"), ids, userId);
		printWebSuccess();
	}

	/**
	 * 消息发送或消息收件的详情
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/message/view")
	public String view() throws Exception {
		user = getSessionUser();
		long userId = user.getUserId();
		long id = paramLong("id");
		MessageModel msg = messageService.find(id, userId);
		request.setAttribute("type", paramString("type"));
		request.setAttribute("msg", msg);
		return "view";
	}

	
	/**
	 * 用户通知设置
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/message/noticeUserSet")
	public void noticeUserSet() throws Exception {
		user = getSessionUser();
		messageService.noticeUserSet(model.getList(), user);
		printWebSuccess();
	}
	
}
