package com.rongdu.p2psys.borrow.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;

/**
 * 借款奖励扣除
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class DeductAwardExecuter extends BaseExecuter {

	private String accountLogType = Constant.AWARD_DEDUCT;

	// private final static String noticeTypeNid =
	// NoticeConstant.NOTICE_DEDUCT_BORROWER_AWARD;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void addAccountLog() {
		Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId", super.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, super.toUser);
		log.setMoney(super.money);
		log.setTotal(account.getTotal());
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());
		log.setCollection(account.getCollection());
		log.setRemark("支付投标奖励扣除" + super.money + "元。");
		log.setPaymentsType((byte)2);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount() {
		accountDao.modify(-super.money, -super.money, 0, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		this.sumManage();
		// 扣款合计
		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, super.toUser);
		Global.setTransfer("money", money);
		this.setAccountSumLogRemarkTemplate(usedRemark);
		sumLog.setRemark(this.getAccountSumLogRemark());
		sumLog.setBeforeMoney(sum.getDeduct());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getDeduct(), super.money));
		accountSumLogDao.save(sumLog);
		accountSumDao.update(EnumAccountSumProperty.DEDUCT.getValue(), money, super.user.getUserId());

	}

	@Override
	public void handlePoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.DEDUCT_BORROWER_AWARD);
		msg.doEvent();
	}

	@Override
	public void addOperateLog() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInterface() {
		// TODO Auto-generated method stub

	}

	@Override
	public void extend() {
		// TODO Auto-generated method stub

	}
}
