package com.rongdu.p2psys.ppfund.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.dao.UserCacheDao;

/**
 * PPfund资金管理产品转出收取居间服务费
 * 
 * @author yinliang
 * @version 2.0
 * @Date   2015年3月25日
 */
public class PpfundOutManageFeeExecuter extends BaseExecuter {

	protected AccountLogDao accountLogDao;
	protected AccountDao accountDao;
	protected UserCacheDao userCacheDao;
	private String accountLogType = Constant.PPFUND_OUT_MANAGE_FEE;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void prepare() {
		accountLogDao = (AccountLogDao) BeanUtil.getBean("accountLogDao");
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		userCacheDao = (UserCacheDao) BeanUtil.getBean("userCacheDao");
	}

	@Override
	public void addAccountLog() {
		Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId", this.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, super.toUser);
		log.setMoney(BigDecimalUtil.add(this.money));
		log.setTotal(account.getTotal());
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());
		log.setCollection(account.getCollection());
		log.setRemark(this.getLogRemark());
		log.setPaymentsType((byte) 0);
		accountLogDao.save(log);

	}

	@Override
	public void handleAccount() {
		
	}

	@Override
	public void handleAccountSum() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlePoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNotice() {
		
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
