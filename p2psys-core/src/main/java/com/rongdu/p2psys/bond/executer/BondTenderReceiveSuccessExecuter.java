package com.rongdu.p2psys.bond.executer;

import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;

/**
 * 向债权投资人发送还款成功通知
 * 
 * @author wzh
 * @version 2.0
 * @since 2014-12-24
 */
public class BondTenderReceiveSuccessExecuter extends BaseExecuter {

	private String accountLogType = "";

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
		BaseMsg msg = new BaseMsg(NoticeConstant.BOND_RECEIVE_SUCC);
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
