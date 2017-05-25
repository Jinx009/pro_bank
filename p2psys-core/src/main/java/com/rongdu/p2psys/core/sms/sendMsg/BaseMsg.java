package com.rongdu.p2psys.core.sms.sendMsg;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.util.BeanUtil;

public class BaseMsg implements MsgEvent, Serializable {

	/** 序列化 */
	private static final long serialVersionUID = 1L;
	/** 发送通知service */
	protected NoticeService noticeService;

	/** 模块编码 */
	protected String noticeTypeNid;

	@Override
	public void doEvent() {
		// 邮件、短信、站内信
		sendMsg();
	}

	/**
	 * 构造方法
	 */
	public BaseMsg() {
		super();
		noticeService = (NoticeService) BeanUtil.getBean("noticeService");
	}

	/**
	 * 构造方法
	 * 
	 * @param nid 模块ID
	 */
	public BaseMsg(String nid) {
		this();
		this.setNoticeTypeNid(nid);
	}

	@Override
	public void sendMsg() {
		if (!"".equals(noticeTypeNid)) {
			Map<String, Object> transferMap = Global.getTransfer();
			transferMap.put("host", Global.getValue("weburl"));
			transferMap.put("webname", Global.getValue("webname"));
			transferMap.put("sendTime", (new Date()).toLocaleString());
			noticeService.sendNotice(noticeTypeNid, transferMap);
		}
	}

	public String getNoticeTypeNid() {
		return noticeTypeNid;
	}

	public void setNoticeTypeNid(String noticeTypeNid) {
		this.noticeTypeNid = noticeTypeNid;
	}

}
