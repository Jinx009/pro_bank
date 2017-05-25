package com.rongdu.p2psys.borrow.executer;

import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 
 * TODO 秒表复审通过解冻资金
 * 
 * @author lx
 * @version 2.0
 * @since 2014年7月30日
 */
public class SecondBorrowUnFeezeExcuter extends BaseExecuter {

	protected AccountLogDao accountLogDao;
	protected AccountDao accountDao;
	private String accountLogType = Constant.NEW_BORROW_FEE_UNFREEZE;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void prepare() {
		accountLogDao = (AccountLogDao) BeanUtil.getBean("accountLogDao");
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");

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
		log.setPaymentsType((byte)0);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount() {
		accountDao.modify(0, this.money, -this.money, this.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		//秒标复审调用接口解冻资金，
		

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
		
		
	}

	@Override
	public void extend() {
		// TODO Auto-generated method stub

	}

}
