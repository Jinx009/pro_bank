package com.rongdu.p2psys.borrow.executer;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;

/**
 * 还款奖励
 * 
 * @author cx
 * @version 1.0
 * @since 2014-6-6
 */
public class RepayDeductAwardExecuter extends DeductAwardExecuter {

	private String accountLogType = Constant.AWARD_DEDUCT;

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
		log.setRemark("扣除还款奖励" + super.money + "元。");
		log.setPaymentsType((byte)0);
		accountLogDao.save(log);
	}
}
