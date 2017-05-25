package com.rongdu.p2psys.borrow.executer;

import com.rongdu.common.util.FreemarkerUtil;
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
 * 借款成功
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowSuccessExecuter extends BaseExecuter {

	
	private String accountLogType = Constant.BORROW_SUCCESS;
	private String sumLogRemarkTemplate = "借款合计：借款入账${money}元，标ID：[${borrow.id}]";

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
		Account account = accountDao.findObjByProperty("user.userId", super.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		log.setMoney(super.money);
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
		accountDao.modify(super.money, super.money, 0, 0,super.repayMoney,super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		accountSumDao.update(EnumAccountSumProperty.BORROW_CASH.getValue(), super.money, super.user.getUserId());

		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		sumLog.setMoney(super.money);
		sumLog.setBeforeMoney(sum.getBorrowCash());
		sumLog.setRemark(getAccountSumLogRemark());
		double afterMoney = sum.getBorrowCash() + super.money;
		sumLog.setAfterMoney(afterMoney);
		accountSumLogDao.save(sumLog);
	}

	public String getAccountSumLogRemark() {
		try {
			return FreemarkerUtil.renderTemplate(sumLogRemarkTemplate, Global.getTransfer());
		} catch (Exception e) {
		}
		return "";
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
		//满标复审，调用放款
		
	}

	@Override
	public void extend() {
		// TODO Auto-generated method stub

	}
}
