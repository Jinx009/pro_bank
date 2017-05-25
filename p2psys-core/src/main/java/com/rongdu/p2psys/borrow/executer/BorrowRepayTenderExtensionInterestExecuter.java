package com.rongdu.p2psys.borrow.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;

/**
 * 展期利息
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowRepayTenderExtensionInterestExecuter extends BaseExecuter {

	private String accountLogType = Constant.LATE_REPAYMENT_INCOME;

	private String sumLogRemarkTemplate = "回款合计：收到展期利息${money}元，标ID：[${borrow.id}]";
	private String interestSumLogRemarkTemplate = "利息合计：获得展期利息${money}元，标ID：[${borrow.id}]";

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
		accountSumDao.update(EnumAccountSumProperty.HUIKUAN_INTEREST.getValue(), super.money, super.user.getUserId());
		accountSumDao.update(EnumAccountSumProperty.INTEREST.getValue(), super.money, super.user.getUserId());

		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, Constant.HUIKUAN_INTEREST, super.toUser);
		sumLog.setMoney(super.money);
		sumLog.setBeforeMoney(sum.getHuikuan());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getHuikuan(), super.money));
		sumLog.setRemark(getHuikuanSumLogRemarkTemplate());
		accountSumLogDao.save(sumLog);

		AccountSumLog InterestLog = new AccountSumLog(super.user, EnumAccountSumProperty.INTEREST.getValue(),
				super.toUser);
		InterestLog.setMoney(super.money);
		InterestLog.setBeforeMoney(sum.getInterest());
		InterestLog.setAfterMoney(BigDecimalUtil.add(sum.getInterest(), super.money));
		InterestLog.setRemark(getInterestSumLogRemarkTemplate());
		accountSumLogDao.save(InterestLog);

	}

	public String getInterestSumLogRemarkTemplate() {
		try {
			return FreemarkerUtil.renderTemplate(interestSumLogRemarkTemplate, Global.getTransfer());
		} catch (Exception e) {
		}
		return "";
	}

	public String getHuikuanSumLogRemarkTemplate() {
		try {
			return FreemarkerUtil.renderTemplate(sumLogRemarkTemplate, Global.getTransfer());
		} catch (Exception e) {
		}
		return "";
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
