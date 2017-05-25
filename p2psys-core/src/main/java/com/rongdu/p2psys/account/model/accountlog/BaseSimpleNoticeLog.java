package com.rongdu.p2psys.account.model.accountlog;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.user.domain.User;

public class BaseSimpleNoticeLog extends BaseAccountLog {

	private static final long serialVersionUID = 1L;

	/**
	 * 调用父类
	 */
	public BaseSimpleNoticeLog() {
		super();
	}

	public BaseSimpleNoticeLog(double money, User user, User toUser) {
		super(money, user, toUser);
	}

	public BaseSimpleNoticeLog(double money, User user) {
		super(money, user);
	}

	@Override
	public void doEvent() {
		// 调试时手动传参，服务器上通过Spring容器获取
		if (DEBUG) {
			transfer();
		}
		Global.setTransfer("weburl", Global.getString("weburl"));
		// 消息
		sendNotice();
		// 操作日志
		addOperateLog();
	}

}
