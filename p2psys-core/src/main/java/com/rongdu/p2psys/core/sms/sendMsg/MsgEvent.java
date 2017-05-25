package com.rongdu.p2psys.core.sms.sendMsg;

/**
 * 通知监听建接口
 * 
 * @author zhangyz
 * @version 1.0
 * @since 2014-04-03
 */
public interface MsgEvent {

	/**
	 * 事件执行
	 */
	void doEvent();

	/**
	 * 通知发送（邮件、站内行、短信）
	 */
	void sendMsg();
}
