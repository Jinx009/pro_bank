package com.rongdu.p2psys.crowdfunding.executer;

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
 * 众筹收益计算
 */
public class CrowdFundingProfitExecuter extends BaseExecuter
{

	private String accountLogType = Constant.REPAID_ZC_PROFIT;
	private String sumLogRemarkTemplate = "已还款合计：已还款利息${money}元，标ID：[${borrow.id}]。";

	@Override
	public String getAccountLogType()
	{
		return accountLogType;
	}

	@Override
	public void prepare()
	{
		super.prepare();
	}

	@Override
	public void addAccountLog()
	{
		Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId",
				super.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType,
				super.toUser);
		log.setMoney(super.money);
		log.setTotal(account.getTotal());
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());
		log.setCollection(account.getCollection());
		log.setRemark(this.getLogRemark());
		log.setPaymentsType((byte) 2);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount()
	{
		accountDao.modify(-super.money, -super.money, 0, 0, 0,
				super.user.getUserId());
	}

	@Override
	public void handleAccountSum()
	{
		this.sumManage();
		accountSumDao.update(EnumAccountSumProperty.REPAY_CASH.getValue(),
				super.money, super.user.getUserId());
		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType,
				super.toUser);
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		sumLog.setMoney(super.money);
		sumLog.setBeforeMoney(sum.getRepayCash());
		sumLog.setRemark(getAccountSumLogRemark());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getRepayCash(), super.money));
		accountSumLogDao.save(sumLog);

	}
}
