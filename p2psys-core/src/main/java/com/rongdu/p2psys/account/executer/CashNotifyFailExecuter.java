package com.rongdu.p2psys.account.executer;

import java.util.Map;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.user.domain.User;

/**
 * 
 * 提现失败回调处理
 * 
 * @author lx
 * @version 2.0
 * @since 2014年7月31日
 */
public class CashNotifyFailExecuter extends BaseExecuter {

	private String accountLogType = Constant.CASH_FAIL;
	protected String rechargeFailRemark = "提现失败：扣除已使用充值金额${cash.recharge_cash}元。";
	protected String awardFailRemark = "提现失败：扣除已使用奖励金额${cash.award_cash}元。";
	protected String interestFailRemark = "提现失败：扣除已使用利息金额${cash.interest_cash}元。";
	protected String borrowCashFailRemark = "提现失败：扣除已使用借款金额${cash.borrow_cash}元。";
	protected String huikuanFailRemark = "提现失败：扣除已使用回款本金金额${cash.huikuan_cash}元。";
	protected String huikuanInterestFailRemark = "提现失败：扣除已使用回款利息金额${cash.huikuan_cash}元。";
	protected AccountSum accountSum;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void prepare() {
		super.prepare();
		accountSum = accountSumDao.findByUserId(super.user.getUserId());
	}

	@Override
	public void addAccountLog() {
		Account account = accountDao.findObjByProperty("user.userId", this.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		log.setMoney(this.money);
		log.setTotal(account.getTotal());
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());
		log.setCollection(account.getCollection());
		log.setRemark(this.getLogRemark());
		log.setPaymentsType((byte)0);
		accountLogDao.save(log);

	}

	@Override
	public void handleAccount() {
		accountDao.modify(0, super.money, -super.money, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		// 先取出更新前的account_sum
		Map<String, Object> map = Global.getTransfer();
		AccountCash cash = (AccountCash) map.get("cash");
		// 本次提现使用的可用充值金额处理
		rechargeCash(cash);
		// 本次提现使用的可用奖励金额处理
		awardCash(cash);
		// 本次提现使用的可用利息金额处理
		interestCash(cash);
		// 本次提现使用的可用借款金额处理
		borrowCash(cash);
		// 本次提现使用的可用回款处理
		huikuanCash(cash);
		huikuanInterest(cash);
	}

	private void rechargeCash(AccountCash cash) {
		if (cash.getRechargeCash() > 0) {
			double recharge_before_money = accountSum.getUsedRecharge();
			double recharge_money = cash.getRechargeCash();
			double recharge_after_money = BigDecimalUtil.sub(recharge_before_money, recharge_money);
			Global.setTransfer("user_cash", recharge_money);
			AccountSumLog rechargeAccountSumLog = new AccountSumLog(super.user,
					EnumAccountSumProperty.USED_RECHARGE.getValue(), new User(Constant.ADMIN_ID));
			rechargeAccountSumLog.setBeforeMoney(recharge_before_money);
			rechargeAccountSumLog.setMoney(-recharge_money);
			rechargeAccountSumLog.setAfterMoney(recharge_after_money);
			this.setAccountSumLogRemarkTemplate(rechargeFailRemark);
			rechargeAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.save(rechargeAccountSumLog);
			accountSumDao.update(EnumAccountSumProperty.USED_RECHARGE.getValue(), -recharge_money,
					super.user.getUserId());

		}
	}

	private void awardCash(AccountCash cash) {
		if (cash.getAwardCash() > 0) {
			double award_before_money = accountSum.getUsedAward();
			double award_money = cash.getAwardCash();
			double award_after_money = BigDecimalUtil.sub(award_before_money, award_money);
			Global.setTransfer("user_cash", award_money);
			AccountSumLog awardAccountSumLog = new AccountSumLog(super.user,
					EnumAccountSumProperty.USED_AWARD.getValue(), super.toUser);
			awardAccountSumLog.setBeforeMoney(award_before_money);
			awardAccountSumLog.setMoney(-award_money);
			awardAccountSumLog.setAfterMoney(award_after_money);
			this.setAccountSumLogRemarkTemplate(awardFailRemark);
			awardAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.save(awardAccountSumLog);
			accountSumDao.update(EnumAccountSumProperty.USED_AWARD.getValue(), -award_money, super.user.getUserId());
		}
	}

	private void interestCash(AccountCash cash) {
		if (cash.getInterestCash() > 0) {
			double interest_before_money = accountSum.getUsedInterest();
			double interest_money = cash.getInterestCash();
			double interest_after_money = BigDecimalUtil.sub(interest_before_money, interest_money);
			Global.setTransfer("user_cash", interest_money);
			AccountSumLog interestAccountSumLog = new AccountSumLog(super.user,
					EnumAccountSumProperty.USED_INTEREST.getValue(), super.toUser);
			interestAccountSumLog.setBeforeMoney(interest_before_money);
			interestAccountSumLog.setMoney(-interest_money);
			interestAccountSumLog.setBeforeMoney(interest_after_money);
			this.setAccountSumLogRemarkTemplate(interestFailRemark);
			interestAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.save(interestAccountSumLog);
			accountSumDao.update(EnumAccountSumProperty.USED_INTEREST.getValue(), -interest_money,
					super.user.getUserId());
		}
	}

	private void borrowCash(AccountCash cash) {
		if (cash.getBorrowCash() > 0) {
			double borrowcash_before_money = accountSum.getUsedBorrowCash();
			double borrowcash_money = cash.getBorrowCash();
			double borrowcash_after_money = BigDecimalUtil.sub(borrowcash_before_money, borrowcash_money);
			Global.setTransfer("user_cash", borrowcash_money);
			AccountSumLog borrowCashAccountSumLog = new AccountSumLog(super.user,
					EnumAccountSumProperty.USED_BORROW_CASH.getValue(), super.toUser);
			borrowCashAccountSumLog.setBeforeMoney(borrowcash_before_money);
			borrowCashAccountSumLog.setMoney(-borrowcash_money);
			borrowCashAccountSumLog.setBeforeMoney(borrowcash_after_money);
			this.setAccountSumLogRemarkTemplate(borrowCashFailRemark);
			borrowCashAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.save(borrowCashAccountSumLog);
			accountSumDao.update(EnumAccountSumProperty.USED_BORROW_CASH.getValue(), -borrowcash_money,
					super.user.getUserId());
		}

	}

	private void huikuanCash(AccountCash cash) {
		if (cash.getHuikuanCash() > 0) {
			double huikuan_before_money = accountSum.getUsedHuikuan();
			double huikuan_money = cash.getHuikuanCash();
			double huikuan_after_money = BigDecimalUtil.sub(huikuan_before_money, huikuan_money);
			Global.setTransfer("user_cash", huikuan_money);
			AccountSumLog caseFeeAccountSumLog = new AccountSumLog(super.user,
					EnumAccountSumProperty.USED_HUIKUAN.getValue(), super.toUser);
			caseFeeAccountSumLog.setBeforeMoney(huikuan_before_money);
			caseFeeAccountSumLog.setMoney(-huikuan_money);
			caseFeeAccountSumLog.setBeforeMoney(huikuan_after_money);
			this.setAccountSumLogRemarkTemplate(huikuanFailRemark);
			caseFeeAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.save(caseFeeAccountSumLog);
			accountSumDao
					.update(EnumAccountSumProperty.USED_HUIKUAN.getValue(), -huikuan_money, super.user.getUserId());
			// this.autoHuikuanForCash(super.user.getUserId() , cash.getId() ,
			// huikuan_money);
		}
	}

	private void huikuanInterest(AccountCash cash) {
		if (cash.getHuikuanInterestCash() > 0) {
			double huikuan_before_money = accountSum.getUsedHuikuanInterest();
			double huikuan_money = cash.getHuikuanInterestCash();
			double huikuan_after_money = BigDecimalUtil.sub(huikuan_before_money, huikuan_money);
			Global.setTransfer("user_cash", huikuan_money);
			AccountSumLog caseFeeAccountSumLog = new AccountSumLog(super.user,
					EnumAccountSumProperty.USED_HUIKUAN_INTEREST.getValue(), super.toUser);
			caseFeeAccountSumLog.setBeforeMoney(huikuan_before_money);
			caseFeeAccountSumLog.setMoney(-huikuan_money);
			caseFeeAccountSumLog.setBeforeMoney(huikuan_after_money);
			this.setAccountSumLogRemarkTemplate(huikuanInterestFailRemark);
			caseFeeAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.save(caseFeeAccountSumLog);
			accountSumDao.update(EnumAccountSumProperty.USED_HUIKUAN_INTEREST.getValue(), -huikuan_money,
					super.user.getUserId());
		}
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
