package com.rongdu.p2psys.account.executer;

import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 线下扣款
 * 
 * @author sj
 */
public class DeductAccountBackFreezeExecuter extends BaseExecuter {

	protected AccountLogDao accountLogDao;
	protected AccountDao accountDao;
	protected VerifyLogDao verifyLogDao;
	protected OperationLogDao operationLogDao;
	private String accountLogType = Constant.ACCOUNT_BACK_FREEZE;
	@Override
	public String getAccountLogType() {
		return accountLogType;
	}
	@Override
	public void prepare() {
		accountLogDao = (AccountLogDao) BeanUtil.getBean("accountLogDao");
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		verifyLogDao = (VerifyLogDao) BeanUtil.getBean("verifyLogDao");
		operationLogDao = (OperationLogDao) BeanUtil.getBean("operationLogDao");
	}

	@Override
	public void addAccountLog() {
		Account account = accountDao.findObjByProperty("user.userId", user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		log.setMoney(money);
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
		accountDao.modify(0, -this.money, this.money, this.user.getUserId());
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
		OperationLog operationLog = new OperationLog(user, operator, Constant.BACKSTAGE_BACK_APPLY);
		operationLog.setOperationResult("用户名为" + operator.getUserName() + "的操作员申请后台扣款" + user.getUserName() + "（"
				+ money + "元）成功，等待审核");
		operationLogDao.save(operationLog);
	}

	@Override
	public void handleInterface() {

	}

	@Override
	public void extend() {

	}

}
