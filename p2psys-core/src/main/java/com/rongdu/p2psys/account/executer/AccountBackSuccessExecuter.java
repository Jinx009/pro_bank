package com.rongdu.p2psys.account.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.user.domain.User;

/**
 * 扣款成功
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-20
 */
public class AccountBackSuccessExecuter extends BaseExecuter {

	private String accountLogType = Constant.ACCOUNT_BACK;

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
		log.setPaymentsType((byte)2);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount() {
		accountDao.modify(-this.money, 0, -this.money, this.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		this.sumManage();
		// 扣款合计
		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		Global.setTransfer("money", money);
		this.setAccountSumLogRemarkTemplate(usedRemark);
		sumLog.setRemark(this.getAccountSumLogRemark());
		sumLog.setBeforeMoney(sum.getDeduct());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getDeduct(), super.money));
		accountSumLogDao.save(sumLog);
		accountSumDao.update(EnumAccountSumProperty.DEDUCT.getValue(), super.money, super.user.getUserId());
	}

	@Override
	public void handlePoints() {
		
	}

	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.HOUTAI_DEDUCT_SUCC);
		msg.doEvent();
	}

	@Override
	public void addOperateLog() {
		OperationLog operationLog = new OperationLog(user, operator, Constant.ACCOUNT_BACK);
		operationLog.setOperationResult("用户名为" + operator.getUserName() + "的操作员后台扣款" + user.getUserName() + "（" + money
				+ "元）成功，扣款成功");
		operationLogDao.save(operationLog);
	}

	@Override
	public void handleInterface() {

	}

	@Override
	public void extend() {

	}

}
