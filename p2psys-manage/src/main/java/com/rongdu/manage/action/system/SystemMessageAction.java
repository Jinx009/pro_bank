package com.rongdu.manage.action.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Message;
import com.rongdu.p2psys.core.service.MessageService;
import com.rongdu.p2psys.core.web.BaseAction;

/**
 * 站内信管理Action
 * 
 * @author zf
 * @version 2.0
 * @since 2014-05-20
 */
public class SystemMessageAction extends BaseAction implements ModelDriven<Message> {

	private Message model = new Message();

	@Override
	public Message getModel() {
		return model;
	}

	/**
	 * 转换JSON字符串用map
	 */
	private Map<String, Object> data = new HashMap<String, Object>();

	@Resource
	private MessageService messageService;

	/**
	 * 站内信管理
	 * 
	 * @return 返回页面
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/message/messageManager")
	public String messageManager() throws Exception {
		return "messageManager";
	}

	/**
	 * 站内信列表
	 * 
	 * @throws Exception 异常
	 */
	@Action(value = "/modules/system/message/messageList")
	public void messageList() throws Exception {
		int pageNumber = paramInt("page"); // 当前页数
		int pageSize = paramInt("rows"); // 每页每页总数
		PageDataList<Message> pagaDataList = messageService.messageList(model, pageNumber, pageSize);
		data.put("total", pagaDataList.getPage().getTotal()); // 总行数
		data.put("rows", pagaDataList.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}

}
