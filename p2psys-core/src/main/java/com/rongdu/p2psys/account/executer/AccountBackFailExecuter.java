package com.rongdu.p2psys.account.executer;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.user.domain.User;

/**
 * 扣款失败
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-20
 */
public class AccountBackFailExecuter extends BaseExecuter {

	private String accountLogType = Constant.ACCOUNT_BACK_UNFREEZE;

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
		accountDao.modify(0, this.money, -this.money, this.user.getUserId());
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
		OperationLog operationLog = new OperationLog(user, operator, Constant.BACKSTAGE_BACK_FAIL);
		operationLog.setOperationResult("用户名为" + operator.getUserName() + "的操作员后台扣款" + user.getUserName() + "（" + money
				+ "元）失败");
		operationLogDao.save(operationLog);
	}

	@Override
	public void handleInterface() {

	}

	@Override
	public void extend() {

	}

}
