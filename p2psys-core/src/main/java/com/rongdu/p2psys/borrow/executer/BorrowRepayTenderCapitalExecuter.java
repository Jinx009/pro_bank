package com.rongdu.p2psys.borrow.executer;

import java.util.Map;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.rule.HuikuanConfRuleCheck;

/**
 * 还款投资人收到还款本金
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowRepayTenderCapitalExecuter extends BaseExecuter {

	private String accountLogType = Constant.CAPITAL_COLLECT;
	private String sumLogRemarkTemplate = "回款合计：回款本金${money}元，标ID：[${borrow.id}]。";

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
		log.setPaymentsType((byte)0);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount() {
		accountDao.modify(0, super.money, 0, -super.money, super.user.getUserId());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void handleAccountSum() {
		accountSumDao.update(EnumAccountSumProperty.HUIKUAN.getValue(), super.money, super.user.getUserId());

		Map tranData = Global.getTransfer();
		Borrow borrow = (Borrow) (tranData.get("borrow"));
		// 回款规则配置 秒标、净值标、天标还款时自动使用掉回款
		HuikuanConfRuleCheck rule = (HuikuanConfRuleCheck) Global.getRuleCheck("huikuanConf");
		if (rule != null) {
			int miaoRefuse = rule.miaoRefuse;
			int jinRefuse = rule.jinRefuse;
			int dayRefuse = rule.dayRefuse;
			if ((miaoRefuse == 1 && borrow.getType() == Borrow.TYPE_SECOND)
					|| (jinRefuse == 1 && borrow.getType() == Borrow.TYPE_PROPERTY)
					|| (dayRefuse == 1 && borrow.getBorrowTimeType() == 1)) {
				this.huikuanUpdate(super.money);// 如果秒标和净值标，将立马减去获得的回款
			}
		}

		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, Constant.HUIKUAN_CAPITAL, super.toUser);
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		sumLog.setMoney(super.money);
		sumLog.setBeforeMoney(sum.getHuikuan());
		sumLog.setRemark(getAccountSumLogRemark());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getHuikuan(), super.money));
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
