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
 * 还款奖励
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class AwardRepayExecuter extends BaseExecuter {

	private String accountLogType = Constant.REPAYMENT_AWARD;
	protected String logAwardRemarkTemplate = "奖励总和:收入奖励${award}元。";

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
		log.setRemark(this.getLogRemark());
		log.setPaymentsType((byte)1);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount() {
		accountDao.modify(super.money, super.money, 0, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		accountSumDao.update(EnumAccountSumProperty.AWARD.getValue(), super.money, super.user.getUserId());
		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, super.toUser);
		this.setAccountSumLogRemarkTemplate(logAwardRemarkTemplate);
		sumLog.setRemark(getAccountSumLogRemark());
		sumLog.setBeforeMoney(sum.getAward());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getAward(), super.money));
		accountSumLogDao.save(sumLog);
	}

	@Override
	public void handlePoints() {

	}

	@Override
	public void handleNotice() {
	    BaseMsg msg = new BaseMsg(NoticeConstant.RECEIVE_TENDER_AWARD);
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
