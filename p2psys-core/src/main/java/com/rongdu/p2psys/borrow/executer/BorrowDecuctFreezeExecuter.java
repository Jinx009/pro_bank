package com.rongdu.p2psys.borrow.executer;

import java.util.Map;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.rule.HuikuanConfRuleCheck;

/**
 * 扣除冻结投标金额，生成待收本金
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class BorrowDecuctFreezeExecuter extends BaseExecuter {

	private String accountLogType = Constant.INVEST;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void addAccountLog() {
		Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId", super.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, super.toUser);
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
		accountDao.modify(0, 0, -super.money, super.money, user.getUserId());
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@Override
	public void handleAccountSum() {
		// 先取出更新前的account_sum
		AccountSum accountSum = accountSumDao.findByUserId(super.user.getUserId());
		// 取出可能扣款的值
		double huikuan = accountSum.getHuikuan();
		double usedHuikuan = accountSum.getUsedHuikuan();
		double useHuikuan = BigDecimalUtil.sub(huikuan, usedHuikuan);

		double huikuanInterest = accountSum.getHuikuanInterest();
		double usedHuikuanInterest = accountSum.getUsedHuikuanInterest();
		double useHuikuanInterest = BigDecimalUtil.sub(huikuanInterest, usedHuikuanInterest);
		useHuikuan = BigDecimalUtil.add(useHuikuan, useHuikuanInterest);
		// 扣除回款的account_sum_log
		double currHuikuan = 0;
		// 本次使用的回款最大不能超过可用回款，
		currHuikuan = (useHuikuan >= money) ? money : (useHuikuan);
		if (currHuikuan > 0) {
			int enableHuikuan = Global.getInt("huikuan_enable");
			Map tranData = Global.getTransfer();
			Borrow borrow = (Borrow) (tranData.get("borrow"));
			BorrowTender tender = (BorrowTender) (tranData.get("tender"));

			// 回款规则配置 秒标、净值标、天标投标不使用回款额度，不产生回款续投奖励
			HuikuanConfRuleCheck rule = (HuikuanConfRuleCheck) Global.getRuleCheck("huikuanConf");
			if (rule != null) {
				double lowestHuikuan = rule.lowest_huikuan;
				int miaoRefuse = rule.miaoRefuse;
				int jinRefuse = rule.jinRefuse;
				int dayRefuse = rule.dayRefuse;
				if ((miaoRefuse != 1 || borrow.getType() != Borrow.TYPE_SECOND)
						&& (jinRefuse != 1 || borrow.getType() != Borrow.TYPE_PROPERTY)
						&& (dayRefuse != 1 || borrow.getBorrowTimeType() != 1) && enableHuikuan == 1
						&& /** tender.getAutoRepurchase() == 1 && **/currHuikuan >= lowestHuikuan) {

					this.huikuanManage(money);
				}
			}
		}
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
		//满标复审解冻资金，并转账，环讯采用一个投资人一次转账
		/*if (BaseTPPWay.isOpenApi()) {
			if(BaseTPPWay.apiCode().equals("2")){
				IpsService ipsService = (IpsService)BeanUtil.getBean("ipsService");
				List<Object> taskList = new ArrayList<Object>();
				Borrow borrow = (Borrow) Global.getTransfer().get("borrow");
				BorrowTender tender =(BorrowTender)Global.getTransfer().get("tender");
				ipsService.transfer(borrow, tender, super.user, super.toUser, "1", "1", taskList);
				ipsService.doIpsTask(taskList);
			}
		}*/
		
	}

	@Override
	public void extend() {
		// TODO Auto-generated method stub

	}
}
