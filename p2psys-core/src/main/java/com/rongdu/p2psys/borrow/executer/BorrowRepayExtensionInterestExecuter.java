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
 * 扣除展期利息
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowRepayExtensionInterestExecuter extends BaseExecuter {

	private String accountLogType = Constant.BORROW_REPAY_EXT_INTEREST;

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
		Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId", super.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, super.toUser);
		log.setMoney(super.money);
		log.setTotal(account.getTotal());
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());
		log.setCollection(account.getCollection());
		log.setRemark(this.getLogRemark());
		log.setPaymentsType((byte)2);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount() {
		accountDao.modify(-super.money, -super.money, 0, 0,super.user.getUserId());
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

	}

	@Override
	public void handleNotice() {

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
