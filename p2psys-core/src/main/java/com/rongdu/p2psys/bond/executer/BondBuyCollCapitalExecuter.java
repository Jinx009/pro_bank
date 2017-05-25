package com.rongdu.p2psys.bond.executer;

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
 * 债权转让受让人待收本金
 * 
 * @author wzh
 * @version 2.0
 * @since 2014-12-12
 */
public class BondBuyCollCapitalExecuter extends BaseExecuter {

	private String accountLogType = Constant.BOND_BUY_COLL_CAPITAL;
	private String sumLogRemarkTemplate = "债权转让受让人待收本金${collCapital}元，债权ID：[${bond.id}]。";

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
		log.setPaymentsType((byte)1);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount() {
	    accountDao.modify(super.money, 0, 0, super.money, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		this.sumManage();
		accountSumDao.update(EnumAccountSumProperty.REPAY_CASH.getValue(), super.money, super.user.getUserId());

		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, super.toUser);
		sumLog.setMoney(super.money);
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		sumLog.setBeforeMoney(sum.getRepayCash());
		sumLog.setRemark(getAccountSumLogRemark());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getRepayCash(), super.money));
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
