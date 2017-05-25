package com.rongdu.p2psys.borrow.executer;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;

/**
 * 利息管理费
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class DeductManageFeeExecuter extends BaseExecuter {

	private String accountLogType = Constant.MANAGE_FEE;
	private String logInterestFeeRemark = "利息手续费合计：还款扣除利息管理费 ${money} 元。";

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
		log.setPaymentsType((byte)2);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount() {
		accountDao.modify(-super.money, -super.money, 0, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.findByUserId(super.user.getUserId());

		// 利息管理费只能从利息中扣除
		double usedInterest = accountSum.getUsedInterest();
		double interestFee = accountSum.getInterestFee();

		// 增加account_sum_log日志
		// 扣费的顺序是充值、奖励、利息、借款
		double money = super.money;

		// 利息管理费只从利息中扣除,而且不存在不够的情况，所以已用金额就只是直接赋值为日志的金额即可
		double currInterest = money;

		if (currInterest > 0) {
			// 增加interest sum log使用日志
			// 扣除利息管理费会增加两条sum_log，一条是增加已用利息，一条是增加利息管理费，

			// 增加interest_fee项
			double interestfee_before_money = interestFee;
			double interestfee_money = currInterest;
			double interestfee_after_money = interestFee + currInterest;

			AccountSumLog interestFeeAccountSumLog = new AccountSumLog(super.user, Constant.MANAGE_FEE, super.toUser);
			this.setAccountSumLogRemarkTemplate(logInterestFeeRemark);
			interestFeeAccountSumLog.setBeforeMoney(interestfee_before_money);
			interestFeeAccountSumLog.setMoney(interestfee_money);
			interestFeeAccountSumLog.setAfterMoney(interestfee_after_money);
			interestFeeAccountSumLog.setRemark(getAccountSumLogRemark());
			accountSumLogDao.save(interestFeeAccountSumLog);
			accountSumDao.update(EnumAccountSumProperty.INTEREST_FEE.getValue(), interestfee_money,
					super.user.getUserId());

			// 增加used interest项
			double interest_before_money = usedInterest;
			double interest_money = currInterest;
			double interest_after_money = usedInterest + currInterest;
			Global.setTransfer("user_cash", interest_money);
			AccountSumLog interestAccountSumLog = new AccountSumLog(super.user,
					EnumAccountSumProperty.USED_INTEREST.getValue(), super.toUser);
			this.setAccountSumLogRemarkTemplate(usedInterestRemark);
			interestAccountSumLog.setBeforeMoney(interest_before_money);
			interestAccountSumLog.setMoney(interest_money);
			interestAccountSumLog.setAfterMoney(interest_after_money);
			interestAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.save(interestAccountSumLog);
			accountSumDao.update(EnumAccountSumProperty.USED_INTEREST.getValue(), interest_money,
					super.user.getUserId());
		}
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
