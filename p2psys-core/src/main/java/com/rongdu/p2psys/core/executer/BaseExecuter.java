package com.rongdu.p2psys.core.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.p2psys.account.dao.AccountBankDao;
import com.rongdu.p2psys.account.dao.AccountCashDao;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.dao.AccountSumDao;
import com.rongdu.p2psys.account.dao.AccountSumLogDao;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.domain.User;

public class BaseExecuter extends AbstractExecuter {

	protected AccountSumDao accountSumDao;
	protected AccountSumLogDao accountSumLogDao;
	protected AccountDao accountDao;
	protected AccountLogDao accountLogDao;
	protected OperationLogDao operationLogDao;
	protected UserCacheDao userCacheDao;
	protected AccountCashDao accountCashDao;
	protected AccountBankDao accountBankDao;

	@Override
	public void prepare() {
		accountLogDao = (AccountLogDao) BeanUtil.getBean("accountLogDao");
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		accountSumDao = (AccountSumDao) BeanUtil.getBean("accountSumDao");
		accountSumLogDao = (AccountSumLogDao) BeanUtil.getBean("accountSumLogDao");
		operationLogDao = (OperationLogDao) BeanUtil.getBean("operationLogDao");
		userCacheDao = (UserCacheDao) BeanUtil.getBean("userCacheDao");
		accountCashDao = (AccountCashDao) BeanUtil.getBean("accountCashDao");
		accountBankDao = (AccountBankDao) BeanUtil.getBean("accountBankDao");
		if (super.toUser == null) {
			super.toUser = new User(Constant.ADMIN_ID);
		}
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

	public String getAccountLogType() {
		return null;
	}

	public String getLogRemark() {
		String template = Global.getLogTempValue((byte) 2, getAccountLogType());
		try {
			return FreemarkerUtil.renderTemplate(template, Global.getTransfer());
		} catch (Exception e) {
		}
		return "";
	}

	// 暂时从1.0移过来
	protected String sumLogRemarkTemplate;
	protected String usedRechargeRemark = "已使用充值：扣除充值${user_cash}元!";
	protected String usedInterestRemark = "已使用利息：扣除获得利息${user_cash}元!";
	protected String usedAwardRemark = "已使用奖励：扣除获得奖励${user_cash}元!";
	protected String usedHuikuanRemark = "已使用回款本金：扣除回款${user_cash}元!";
	protected String usedHuikuanInterestRemark = "已使用回款利息：扣除回款${user_cash}元!";
	protected String usedBorrowCashRemark = "已使用借款额：扣除借款额${user_cash}元!";
	protected String cashfeeRemark = "提现收费合计：提现${cash.money}元,手续费${cash.fee}元!";
	protected String cashRemark = "提现金额合计：提现${cash.money}元!";
	protected String rechargeRemark = "充值合计：增加充值${recharge.amountIn}元!";
	protected String usedRemark = "扣款合计：扣除${money}元!";
	protected String huikuanRemark = "回款合计：增加${money}元!";
	protected String interestFeeRemark = "利息管理费合计：增加${money}元!";

	public String getAccountSumLogRemark() {
		try {
			return FreemarkerUtil.renderTemplate(getAccountSumLogRemarkTemplate(), Global.getTransfer());
		} catch (Exception e) {
		}
		return "";
	}

	public String getAccountSumLogRemarkTemplate() {
		return sumLogRemarkTemplate;
	}

	public void setAccountSumLogRemarkTemplate(String sumLogRemarkTemplate) {
		this.sumLogRemarkTemplate = sumLogRemarkTemplate;
	}

	/**
	 * sum和sum日志更新
	 * 
	 * @param money 操作金额
	 * @param sumMoney 当前已有的金额
	 * @param logType 日志类型
	 * @param sumType sum修改字段类型
	 * @return 扣费的顺序是充值、奖励、利息、借款，如果不同，则在子类中写
	 */
	public void sumUpdate(double money, double sumMoney, String logType, String sumType) {
		AccountSumLog sumLog = new AccountSumLog(super.user, logType, super.toUser);
		sumLog.setBeforeMoney(sumMoney);
		sumLog.setMoney(money);
		sumLog.setAfterMoney(BigDecimalUtil.add(money, sumMoney));
		sumLog.setRemark(this.getAccountSumLogRemark());
		accountSumLogDao.save(sumLog);
		accountSumDao.update(sumType, money, super.user.getUserId());
	}

	/**
	 * sum数据处理
	 * 
	 * @return
	 */
	public void sumManage() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.findByUserId(super.user.getUserId());
		// 取出可能扣款的值
		double recharge = accountSum.getRecharge();
		double usedRecharge = accountSum.getUsedRecharge();
		double useRecharge = recharge - usedRecharge;

		double award = accountSum.getAward();
		double usedAward = accountSum.getUsedAward();
		double useAward = award - usedAward;

		double interest = accountSum.getInterest();
		double usedInterest = accountSum.getUsedInterest();
		double useInterest = interest - usedInterest;

		double usedBorrowCash = accountSum.getUsedBorrowCash();

		// 增加account_sum_log日志
		// 扣费的顺序是充值、奖励、利息、借款
		double money = super.money;
		double currRecharge = 0;
		double currAward = 0;
		double currInterest = 0;
		double currBorrowCash = 0;

		if (useRecharge >= money) {
			currRecharge = money;
		} else {
			if ((useRecharge + useAward) >= money) {
				currRecharge = useRecharge;
				currAward = money - useRecharge;
			} else {
				if ((useRecharge + useAward + useInterest) >= money) {
					currRecharge = useRecharge;
					currAward = useAward;
					currInterest = money - (useRecharge + useAward);
				} else {
					currRecharge = useRecharge;
					currAward = useAward;
					currInterest = useInterest;
					currBorrowCash = money - (useRecharge + useAward + useInterest);
				}
			}
		}

		if (currRecharge > 0) {
			// 增加recharge sum log 日志
			Global.setTransfer("user_cash", currRecharge);
			this.setAccountSumLogRemarkTemplate(usedRechargeRemark);
			String logType = EnumAccountSumProperty.USED_RECHARGE.getValue();
			String sumType = EnumAccountSumProperty.USED_RECHARGE.getValue();
			this.sumUpdate(currRecharge, usedRecharge, logType, sumType);
		}
		if (currAward > 0) {
			// 增加award sum log 使用日志
			Global.setTransfer("user_cash", currAward);
			this.setAccountSumLogRemarkTemplate(usedAwardRemark);
			String logType = EnumAccountSumProperty.USED_AWARD.getValue();
			String sumType = EnumAccountSumProperty.USED_AWARD.getValue();
			this.sumUpdate(currAward, usedAward, logType, sumType);
		}
		if (currInterest > 0) {
			// 增加interest sum log使用日志
			Global.setTransfer("user_cash", currInterest);
			this.setAccountSumLogRemarkTemplate(usedInterestRemark);
			String logType = EnumAccountSumProperty.USED_INTEREST.getValue();
			String sumType = EnumAccountSumProperty.USED_INTEREST.getValue();
			this.sumUpdate(currInterest, usedInterest, logType, sumType);
		}
		if (currBorrowCash > 0) {
			// 增加interest sum log使用日志
			Global.setTransfer("user_cash", currBorrowCash);
			this.setAccountSumLogRemarkTemplate(usedBorrowCashRemark);
			String logType = EnumAccountSumProperty.USED_BORROW_CASH.getValue();
			String sumType = EnumAccountSumProperty.USED_BORROW_CASH.getValue();
			this.sumUpdate(currBorrowCash, usedBorrowCash, logType, sumType);
		}
		// 扣除回款的account_sum_log
		this.huikuanManage(null);
	}

	/**
	 * 回款处理
	 * 
	 * @param money 操作金额，如果为空，默认值就是account log中的money值
	 */
	public void huikuanManage(Double money) {
		AccountSum accountSum = accountSumDao.findByUserId(super.user.getUserId());
		double huikuan = accountSum.getHuikuan();
		double usedHuikuan = accountSum.getUsedHuikuan();
		double useHuikuan = BigDecimalUtil.sub(huikuan, usedHuikuan);

		double huikuanInterest = accountSum.getUsedHuikuanInterest();
		double usedHuikuanInterest = accountSum.getUsedHuikuanInterest();
		double useHuikuanInterest = BigDecimalUtil.sub(huikuanInterest, usedHuikuanInterest);

		if (money == null)
			money = super.money;
		double currHuikuan = 0;
		double currHuikuanInterest = 0;
		double moenyHKuan = BigDecimalUtil.sub(useHuikuanInterest, money);
		currHuikuanInterest = moenyHKuan >= 0 ? money : useHuikuanInterest;
		if (moenyHKuan < 0) {// 本次使用的回款最大不能超过可用回款，
			currHuikuan = money - useHuikuanInterest;
			currHuikuan = currHuikuan >= useHuikuan ? useHuikuan : currHuikuan;
		}
		if (currHuikuanInterest > 0) {
			this.huikuanInterestUpdate(currHuikuanInterest);
		}
		if (currHuikuan > 0) {
			this.huikuanUpdate(currHuikuan);
		}
	}

	// 回款更新
	public void huikuanUpdate(Double currHuikuan) {
		if (currHuikuan == null)
			currHuikuan = super.money;
		AccountSum accountSum = accountSumDao.findByUserId(super.user.getUserId());
		double huikuan = BigDecimalUtil.sub(accountSum.getHuikuan(), accountSum.getUsedHuikuan());
		currHuikuan = currHuikuan >= huikuan ? huikuan : currHuikuan;
		Global.setTransfer("user_cash", currHuikuan);
		this.setAccountSumLogRemarkTemplate(usedHuikuanRemark);
		String logType = EnumAccountSumProperty.USED_HUIKUAN.getValue();
		String sumType = EnumAccountSumProperty.USED_HUIKUAN.getValue();
		this.sumUpdate(currHuikuan, accountSum.getUsedHuikuan(), logType, sumType);
	}

	// 回款利息更新
	public void huikuanInterestUpdate(Double currHuikuanInterest) {
		if (currHuikuanInterest == null)
			currHuikuanInterest = super.money;
		AccountSum accountSum = accountSumDao.findByUserId(super.user.getUserId());
		double huikuanInterest = BigDecimalUtil.sub(accountSum.getUsedHuikuanInterest(),
				accountSum.getUsedHuikuanInterest());
		currHuikuanInterest = currHuikuanInterest >= huikuanInterest ? huikuanInterest : currHuikuanInterest;
		Global.setTransfer("user_cash", currHuikuanInterest);
		this.setAccountSumLogRemarkTemplate(usedHuikuanInterestRemark);
		String logType = EnumAccountSumProperty.USED_HUIKUAN_INTEREST.getValue();
		String sumType = EnumAccountSumProperty.USED_HUIKUAN_INTEREST.getValue();
		this.sumUpdate(currHuikuanInterest, accountSum.getUsedHuikuanInterest(), logType, sumType);
	}

}
