package com.rongdu.p2psys.account.executer;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;

/**
 * 投资成功使用红包
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月8日
 */
public class PpfundRedPacketSuccessExecuter extends BaseExecuter {
	private String accountLogType = Constant.PPFUND_RED_PACKET_SUCCESS;
	private String sumLogRemarkTemplate = "奖励合计：红包兑换金额${money}元。";
	@Override
	public String getAccountLogType() {
		return accountLogType;
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
		log.setPaymentsType((byte) 1);
		accountLogDao.save(log);

	}

	@Override
	public void handleAccount() {
		
	}

	@Override
	public void handleAccountSum() {
		this.sumManage();
		accountSumDao.update(EnumAccountSumProperty.AWARD.getValue(), super.money, super.user.getUserId());
		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, super.toUser);
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		sumLog.setMoney(super.money);
		sumLog.setBeforeMoney(sum.getAward());
		sumLog.setRemark(getAccountSumLogRemark());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getAward(), super.money));
		accountSumLogDao.save(sumLog);

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
