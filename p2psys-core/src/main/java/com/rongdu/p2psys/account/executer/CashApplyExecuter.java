package com.rongdu.p2psys.account.executer;

import java.util.Map;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountCash;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.domain.User;

/**
 * 提现申请
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-30
 */
public class CashApplyExecuter extends BaseExecuter {

	private String accountLogType = Constant.CASH_FROST;

	@Override
	public String getAccountLogType() {
		return accountLogType;
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
		accountDao.modify(0, -money, money, user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.findByUserId(super.user.getUserId());
		Map<String, Object> map = Global.getTransfer();
		AccountCash cash = (AccountCash) map.get("cash");
		// 取出可能扣款的值
		double usedRecharge = accountSum.getUsedRecharge();
		double usedAward = accountSum.getUsedAward();
		double usedInterest = accountSum.getUsedInterest();
		double usedBorrowCash = accountSum.getUsedBorrowCash();

		double recharge_money = cash.getRechargeCash();
		if (recharge_money > 0) {
			// 增加recharge sum log 日志
			double currRecharge = BigDecimalUtil.sub(accountSum.getRecharge(), usedRecharge);
			recharge_money = recharge_money > currRecharge ? currRecharge : recharge_money;
			Global.setTransfer("user_cash", recharge_money);
			this.setAccountSumLogRemarkTemplate(usedRechargeRemark);
			String logType = EnumAccountSumProperty.USED_RECHARGE.getValue();
			String sumType = EnumAccountSumProperty.USED_RECHARGE.getValue();
			this.sumUpdate(recharge_money, usedRecharge, logType, sumType);
		}

		double award_money = cash.getAwardCash();
		if (award_money > 0) {
			// 增加award sum log 使用日志
			double currAward = BigDecimalUtil.sub(accountSum.getAward(), accountSum.getUsedAward());
			award_money = award_money > currAward ? currAward : award_money;
			Global.setTransfer("user_cash", award_money);
			this.setAccountSumLogRemarkTemplate(usedAwardRemark);
			String logType = EnumAccountSumProperty.USED_AWARD.getValue();
			String sumType = EnumAccountSumProperty.USED_AWARD.getValue();
			this.sumUpdate(award_money, usedAward, logType, sumType);
		}

		double interest_money = cash.getInterestCash();
		if (interest_money > 0) {
			// 增加interest sum log使用日志
			double currInterest = BigDecimalUtil.sub(accountSum.getInterest(), accountSum.getUsedInterest());
			interest_money = interest_money > currInterest ? currInterest : interest_money;
			Global.setTransfer("user_cash", interest_money);
			this.setAccountSumLogRemarkTemplate(usedInterestRemark);
			String logType = EnumAccountSumProperty.USED_INTEREST.getValue();
			String sumType = EnumAccountSumProperty.USED_INTEREST.getValue();
			this.sumUpdate(interest_money, usedInterest, logType, sumType);
		}

		double borrowcash_money = cash.getBorrowCash();
		if (borrowcash_money > 0) {
			// 增加BorrowCash sum log使用日志
			double currBorrowCash = BigDecimalUtil.sub(accountSum.getBorrowCash(), accountSum.getUsedBorrowCash());
			borrowcash_money = borrowcash_money > currBorrowCash ? currBorrowCash : borrowcash_money;
			Global.setTransfer("user_cash", borrowcash_money);
			this.setAccountSumLogRemarkTemplate(usedBorrowCashRemark);
			String logType = EnumAccountSumProperty.USED_BORROW_CASH.getValue();
			String sumType = EnumAccountSumProperty.USED_BORROW_CASH.getValue();
			this.sumUpdate(borrowcash_money, usedBorrowCash, logType, sumType);
		}

		// 扣除回款的account_sum_log
		double huikuan_money = cash.getHuikuanCash();
		if (huikuan_money > 0) {
			this.huikuanUpdate(huikuan_money);
		}
		double huikuan_interest_money = cash.getHuikuanInterestCash();
		if (huikuan_interest_money > 0) {
			this.huikuanInterestUpdate(huikuan_interest_money);
		}
	}

	@Override
	public void handlePoints() {
		
	}

	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.CASH_APPLY);
		msg.doEvent();
	}

	@Override
	public void addOperateLog() {

	}

	@Override
	public void handleInterface() {
		if (BaseTPPWay.isOpenApi() && TPPWay.API_CODE == TPPWay.API_CODE_YJF) {
			TPPWay dw = TPPFactory.getTPPWay();
			AccountCash cash = (AccountCash) Global.getTransfer().get("cash");
			QueryParam param = new QueryParam();
			param.addParam("bankNo",  cash.getBankNo());
			AccountBank ab = accountBankDao.findByCriteriaForUnique(param);
			int cashnum = accountCashDao.getSuccessAccountCash(this.user.getUserId());
			dw.doNewCash(cash, user, cashnum, ab.getProvince(), ab.getCity(), ab.getYjfDrawBank().getBankCode());
		}
	}

	@Override
	public void extend() {
	}

}
