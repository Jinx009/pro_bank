package com.rongdu.p2psys.borrow.executer;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.executer.UserRegisterExecuter;

/**
 * 后台初审成功消息通知
 * @author sj
 * @since 2014年8月15日14:41:07
 *
 */
public class BorrowVerifySuccNoticeExecuter extends BaseExecuter {

	Logger logger = Logger.getLogger(UserRegisterExecuter.class);
	NoticeService noticeService;

	@Override
	public void prepare() {
		noticeService = (NoticeService) BeanUtil.getBean("noticeService");
	}

	@Override
	public void addAccountLog() {
	}

	@Override
	public void handleAccount() {

	}

	@Override
	public void handleAccountSum() {
	}

	@Override
	public void handlePoints() {
	}

	/**
	 * 重新写发送通知的方法
	 */
	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.BORROW_VERIFY_SUCC);
		msg.doEvent();
	}

	@Override
	public void addOperateLog() {
	}

	@Override
	public void handleInterface() {
	}

	@Override
	public void extend() {
	}
	
}
