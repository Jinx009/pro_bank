package com.rongdu.p2psys.account.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.dao.AccountSumDao;
import com.rongdu.p2psys.account.dao.AccountSumLogDao;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.util.BeanUtil;

/**
 * 充值
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-29
 */
public class BaseRechargeSuccessExecuter extends BaseExecuter {

	AccountSumDao accountSumDao = (AccountSumDao) BeanUtil.getBean("accountSumDao");
	AccountSumLogDao accountSumLogDao = (AccountSumLogDao) BeanUtil.getBean("accountSumLogDao");

	@Override
	public void handleAccountSum() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.findByUserId(super.user.getUserId());
		if(accountSum == null) return;
		double recharge = accountSum.getRecharge();
		if (super.money > 0) {
			// 增加recharge sum log 日志
			double recharge_after_money = BigDecimalUtil.add(recharge, super.money);
			Global.setTransfer("money", super.money);
			AccountSumLog rechargeAccountSumLog = new AccountSumLog(super.user,
					EnumAccountSumProperty.RECHARGE.getValue(), super.toUser);
			rechargeAccountSumLog.setBeforeMoney(recharge);
			rechargeAccountSumLog.setMoney(super.money);
			rechargeAccountSumLog.setAfterMoney(recharge_after_money);
			this.setAccountSumLogRemarkTemplate(rechargeRemark);
			rechargeAccountSumLog.setRemark(this.getAccountSumLogRemark());
			accountSumLogDao.save(rechargeAccountSumLog);
			accountSumDao.update(EnumAccountSumProperty.RECHARGE.getValue(), super.money, super.user.getUserId());
		}
	}

}
