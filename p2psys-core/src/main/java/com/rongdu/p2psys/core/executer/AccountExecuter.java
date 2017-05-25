package com.rongdu.p2psys.core.executer;

import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.dao.AccountSumDao;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.dao.UserDao;

public class AccountExecuter extends AbstractExecuter {

	protected AccountDao accountDao;
	protected AccountLogDao AccountLogDao;
	protected UserDao userDao;
	protected AccountSumDao AccountSumDao;

	@Override
	public void prepare() {
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		userDao = (UserDao) BeanUtil.getBean("userDao");
	}

	@Override
	public void addAccountLog() {

		AccountLogDao.save(new AccountLog());
	}

	@Override
	public void handleAccount() {

	}

	@Override
	public void handleAccountSum() {

	}

	@Override
	public void handlePoints() {

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
