package com.rongdu.p2psys.borrow.executer;

import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.core.util.BeanUtil;

/**
 * 普通标满标审核失败解冻投资人的资金
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowFailExecuter extends BaseExecuter {

	protected AccountLogDao accountLogDao;
	protected AccountDao accountDao;
	private String accountLogType = Constant.UNFREEZE_NO_PASS;

	// private final static String noticeTypeNid =
	// NoticeConstant.NOTICE_INVEST_FAIL;

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
		Account account = accountDao.findObjByProperty("user.userId", super.user.getUserId());
		AccountLog log = new AccountLog(super.user, Constant.UNFREEZE, super.toUser);
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
		accountDao.modify(0, super.money, -super.money, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {

	}

	@Override
	public void handlePoints() {

	}

	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.INVEST_FAIL);
		msg.doEvent();
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
