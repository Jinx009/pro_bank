package com.rongdu.p2psys.borrow.executer;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.user.domain.User;

/**
 * 冻结或解冻还款人资金
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowRepayFreezeExecuter extends BaseExecuter {

	@Override
	public String getAccountLogType() {
		return (super.money) > 0 ? Constant.FREEZE : Constant.UNFREEZE;
	}

	@Override
	public void prepare() {
		super.prepare();
	}

	@Override
	public void addAccountLog() {
		Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId", super.user.getUserId());
		AccountLog log = new AccountLog(super.user, getAccountLogType(), new User(Constant.ADMIN_ID));
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
		accountDao.modify(0, -super.money, super.money, 0,super.user.getUserId());
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
}
