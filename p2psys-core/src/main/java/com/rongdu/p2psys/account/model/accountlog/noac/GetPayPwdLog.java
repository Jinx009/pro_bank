package com.rongdu.p2psys.account.model.accountlog.noac;

import com.rongdu.p2psys.account.model.accountlog.BaseSimpleNoticeLog;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.user.domain.User;

public class GetPayPwdLog extends BaseSimpleNoticeLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8115081360693021279L;

	public GetPayPwdLog() {
		super();
	}

	public GetPayPwdLog(double money, User user, User toUser) {
		super(money, user, toUser);
	}

	public GetPayPwdLog(double money, User user) {
		super(money, user);
	}

	public GetPayPwdLog(User user) {
		super();
		this.setUser(user);
	}

	private final static String noticeTypeNid = NoticeConstant.NOTICE_GET_PAYPWD;

	@Override
	public String getNoticeTypeNid() {
		return noticeTypeNid;
	}
}
