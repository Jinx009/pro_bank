package com.rongdu.p2psys.account.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.user.domain.User;

/**
 * 线下充值奖励executer
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-17
 */
public class AwardOffRechargeExecuter extends BaseExecuter {

	private String accountLogType = Constant.OFFRECHARGE_AWARD;
	protected String logAwardRemarkTemplate = "奖励总和:收入奖励${award}元。";

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
		Account account = accountDao.findObjByProperty("user.userId", this.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		log.setMoney(this.money);
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
		accountDao.modify(super.money, super.money, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		accountSumDao.update(EnumAccountSumProperty.AWARD.getValue(), super.money, super.user.getUserId());
		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, super.toUser);
		this.setAccountSumLogRemarkTemplate(logAwardRemarkTemplate);
		sumLog.setRemark(getAccountSumLogRemark());
		sumLog.setBeforeMoney(sum.getAward());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getAward(), super.money));
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
