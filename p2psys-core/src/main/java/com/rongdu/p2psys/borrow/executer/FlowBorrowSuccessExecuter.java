package com.rongdu.p2psys.borrow.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;

/**
 * 借款人资金入账
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-29
 */
public class FlowBorrowSuccessExecuter extends BaseExecuter {

	private String sumLogRemarkTemplate = "借款合计：借款入账${money}元，标ID：[${borrow.id}]";
	private String accountLogType = Constant.BORROW_SUCCESS;

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
		accountSumDao.update(EnumAccountSumProperty.BORROW_CASH.getValue(), super.money, super.user.getUserId());
		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, super.toUser);
		sumLog.setBeforeMoney(sum.getBorrowCash());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getBorrowCash(), super.money));
		sumLog.setRemark(getAccountSumLogRemark());
		accountSumLogDao.save(sumLog);
	}

	@Override
	public void handlePoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNotice() {
		// TODO Auto-generated method stub

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
