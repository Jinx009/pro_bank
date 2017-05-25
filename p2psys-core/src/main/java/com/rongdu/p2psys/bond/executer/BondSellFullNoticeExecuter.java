package com.rongdu.p2psys.bond.executer;

import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;

/**
 * 债权转让给出让人发通知
 * 
 * @author wzh
 * @version 2.0
 * @since 2014-12-12
 */
public class BondSellFullNoticeExecuter extends BaseExecuter {

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

	/**
	 * 重新写发送通知的方法
	 */
	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.BOND_SELL_SUCC);
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
