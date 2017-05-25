package com.rongdu.p2psys.account.executer;

import java.util.Map;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountCash;
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
 * 提现审核成功
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-19
 */
public class CashSuccessExecuter extends BaseExecuter {

	private String accountLogType = Constant.CASH_SUCCESS;

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
		accountDao.modify(-super.money, 0, -super.money, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.findByUserId(super.user.getUserId());
		// 取出可能扣款的值
		double cashFee = accountSum.getCashFee();
		Map<String, Object> transferMap = Global.getTransfer();
		AccountCash cash = (AccountCash) transferMap.get("cash");
		// 添加提现费的account_sum_log
		double fee = cash.getFee();
		if (fee > 0) {
			AccountSumLog caseFeeAccountSumLog = new AccountSumLog(super.user,
					EnumAccountSumProperty.CASH_FEE.getValue(), new User(Constant.ADMIN_ID));
			caseFeeAccountSumLog.setBeforeMoney(cashFee);
			caseFeeAccountSumLog.setMoney(fee);
			caseFeeAccountSumLog.setAfterMoney(BigDecimalUtil.add(cashFee, fee));
			this.setAccountSumLogRemarkTemplate(cashfeeRemark);
			caseFeeAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.save(caseFeeAccountSumLog);
			accountSumDao.update(EnumAccountSumProperty.CASH_FEE.getValue(), fee, super.user.getUserId());
		}
		// 提现成功增加account sum 中的case
		// 生成account_sum_log
		if (super.money > 0) {
			AccountSumLog cashAccountSumLog = new AccountSumLog(super.user, EnumAccountSumProperty.CASH.getValue(),
					super.toUser);
			cashAccountSumLog.setBeforeMoney(accountSum.getCash());
			cashAccountSumLog.setMoney(super.money);
			cashAccountSumLog.setAfterMoney(BigDecimalUtil.add(accountSum.getCash(), super.money));
			this.setAccountSumLogRemarkTemplate(cashRemark);
			cashAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.save(cashAccountSumLog);
			accountSumDao.update(EnumAccountSumProperty.CASH.getValue(), money, super.user.getUserId());
		}
	}

	@Override
	public void handlePoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.CASH_VERIFY_SUCC);
		msg.doEvent();
	}

	@Override
	public void addOperateLog() {
		OperationLog operationLog = new OperationLog(super.user, operator, Constant.CASH_SUCCESS);
		operationLog.setOperationResult("IP:（" + super.operator.getLoginIp() + "）用户名为" + super.operator.getUserName()
				+ "的操作员审核后台提现" + money + "元成功");

		operationLogDao.save(operationLog);
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
