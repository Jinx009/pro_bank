package com.rongdu.p2psys.borrow.executer;

import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;

/**
 * 还款成功
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowRepaySuccessExecuter extends BaseExecuter {

	private String accountLogType = Constant.REPAYMENT_SUCCESS;

	// private final static String noticeTypeNid =
	// NoticeConstant.NOTICE_REPAY_SUCC;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void prepare() {
		super.prepare();
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

	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.REPAY_SUCC);
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
