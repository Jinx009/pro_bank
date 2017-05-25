package com.rongdu.p2psys.borrow.executer;

import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.executer.BaseExecuter;

/**
 * 还款投资人收到还款利息
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowRepayTenderInterestExecuter extends BaseExecuter {

	private String accountLogType = Constant.INTEREST_COLLECT;

	private String interestSumLogRemarkTemplate = "利息合计：获得利息${money}元，标ID：[${borrow.id}]";
	private String sumLogRemarkTemplate = "回款合计：收到还款利息${money-borrowFee}元，标ID：[${borrow.id}]";

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

	@Override
	public void handleAccountSum() {
//		Map tranData = Global.getTransfer();
//		double borrowFee = Double.parseDouble(tranData.get("borrowFee").toString());
//		double realHuikuan = BigDecimalUtil.sub(super.money, borrowFee);
//		accountSumDao.update(EnumAccountSumProperty.HUIKUAN_INTEREST.getValue(), realHuikuan, super.user.getUserId());
//		accountSumDao.update(EnumAccountSumProperty.INTEREST.getValue(), super.money, super.user.getUserId());
//
//		Borrow borrow = (Borrow) (tranData.get("borrow"));
//		// 回款规则配置（1.投标不使用回款续投的标种;2.还款时自动消耗掉回款的标种）
//		HuikuanConfRuleCheck rule = (HuikuanConfRuleCheck) Global.getRuleCheck("huikuanConf");
//		if (rule != null) {
//			int miaoRefuse = rule.miaoRefuse;
//			int jinRefuse = rule.jinRefuse;
//			int dayRefuse = rule.dayRefuse;
//			if ((miaoRefuse == 1 && borrow.getType() == Borrow.TYPE_SECOND)
//					|| (jinRefuse == 1 && borrow.getType() == Borrow.TYPE_PROPERTY)
//					|| (dayRefuse == 1 && borrow.getBorrowTimeType() == 1)) {
//				this.huikuanInterestUpdate(realHuikuan);
//			}
//		}
//
//		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
//		AccountSumLog sumLog = new AccountSumLog(super.user, Constant.HUIKUAN_INTEREST, super.toUser);
//		sumLog.setMoney(realHuikuan);
//		sumLog.setBeforeMoney(sum.getHuikuan());
//		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getHuikuan(), realHuikuan));
//		sumLog.setRemark(getHuikuanSumLogRemarkTemplate());
//		accountSumLogDao.save(sumLog);
//
//		AccountSumLog InterestLog = new AccountSumLog(super.user, EnumAccountSumProperty.INTEREST.getValue(),
//				super.toUser);
//		InterestLog.setMoney(super.money);
//		InterestLog.setBeforeMoney(sum.getInterest());
//		InterestLog.setAfterMoney(BigDecimalUtil.add(sum.getInterest(), super.money));
//		InterestLog.setRemark(getInterestSumLogRemarkTemplate());
//		accountSumLogDao.save(InterestLog);

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
