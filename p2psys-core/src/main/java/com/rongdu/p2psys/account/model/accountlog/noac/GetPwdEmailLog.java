package com.rongdu.p2psys.account.model.accountlog.noac;

import com.rongdu.p2psys.account.model.accountlog.BaseSimpleNoticeLog;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.user.domain.User;

/**
 * 消息通知-通过邮箱找回密码
 * 
 * @author xx
 * @version 2.0
 * @since 2014年2月28日
 */
@SuppressWarnings("serial")
public class GetPwdEmailLog extends BaseSimpleNoticeLog {
	private final static String noticeTypeNid = NoticeConstant.NOTICE_GET_PWD_EMAIL;

	public GetPwdEmailLog() {
		super();
	}

	public GetPwdEmailLog(double money, User user, User toUser) {
		super(money, user, toUser);
	}

	public GetPwdEmailLog(double money, User user) {
		super(money, user);
	}

	public GetPwdEmailLog(User user) {
		super();
		this.setUser(user);
	}

	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	}
}
