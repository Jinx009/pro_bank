package com.rongdu.p2psys.crowdfunding.executer;

import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.util.BeanUtil;

/**
 * 普通标满标审核失败解冻投资人的资金
 */
public class ProjectFailExecuter extends BaseExecuter
{
	protected AccountLogDao accountLogDao;
	protected AccountDao accountDao;
	private String accountLogType = Constant.UNFREEZE_NO_PASS;

	@Override
	public String getAccountLogType()
	{
		return accountLogType;
	}

	@Override
	public void prepare()
	{
		accountLogDao = (AccountLogDao) BeanUtil.getBean("accountLogDao");
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");
	}

	@Override
	public void addAccountLog()
	{
		Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId",
				super.user.getUserId());
		AccountLog log = new AccountLog(super.user, Constant.UNFREEZE,
				super.toUser);
		log.setMoney(super.money);
		log.setTotal(account.getTotal());
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());
		log.setCollection(account.getCollection());
		log.setPaymentsType((byte) 0);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount()
	{
		accountDao.modify(0, super.money, -super.money, 0,
				super.user.getUserId());
	}

}
