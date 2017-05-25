package com.rongdu.p2psys.account.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.user.domain.User;

/**
 * 充值费用
 * 
 * @author cx
 * @version 1.0
 * @since 2014-6-5
 */
public class DeductRechargeFeeExecuter extends BaseExecuter {

	private String accountLogType = Constant.ACCOUNT_RECHARGE_FEE;

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
		accountDao.modify(-super.money, -super.money, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		this.sumManage();
		// 扣款合计
		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		Global.setTransfer("money", super.money);
		this.setAccountSumLogRemarkTemplate(usedRemark);
		sumLog.setRemark(this.getAccountSumLogRemark());
		sumLog.setBeforeMoney(sum.getDeduct());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getDeduct(), super.money));
		accountSumLogDao.save(sumLog);
		accountSumDao.update(EnumAccountSumProperty.DEDUCT.getValue(), money, super.user.getUserId());

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
		if(operator != null){
			OperationLog operationLog = new OperationLog(super.user, super.operator, Constant.LINE_RECHARGE_SUCCESS);
			operationLog.setOperationResult("（IP:" + super.operator.getLoginIp() + "）用户名为" + super.operator.getUserName()
					+ "的操作员审核后台充值" + super.user.getUserName() + super.money + "元成功");
			operationLogDao.save(operationLog);
		}
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
