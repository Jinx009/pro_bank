package com.rongdu.p2psys.borrow.executer;

import java.util.ArrayList;
import java.util.List;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.domain.AccountSumLog;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.enums.EnumAccountSumProperty;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.yjf.service.YjfService;
import com.rongdu.p2psys.tpp.yjf.service.impl.YjfServiceImpl;

/**
 * 投标奖励
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class AwardTenderAwardExecuter extends BaseExecuter {

	private String accountLogType = Constant.AWARD_ADD;
	protected String logAwardRemarkTemplate = "奖励总和:收入奖励${award}元。";

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
		log.setPaymentsType((byte)1);
		accountLogDao.save(log);
	}

	@Override
	public void handleAccount() {
		accountDao.modify(super.money, super.money, 0, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		accountSumDao.update(EnumAccountSumProperty.AWARD.getValue(), super.money, super.user.getUserId());
		AccountSum sum = accountSumDao.findByUserId(super.user.getUserId());
		AccountSumLog sumLog = new AccountSumLog(super.user, accountLogType, super.toUser);
		this.setAccountSumLogRemarkTemplate(logAwardRemarkTemplate);
		sumLog.setBeforeMoney(sum.getAward());
		sumLog.setAfterMoney(BigDecimalUtil.add(sum.getAward(), super.money));
		sumLog.setRemark(getAccountSumLogRemark());
		accountSumLogDao.save(sumLog);
	}

	@Override
	public void handlePoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNotice() {
		//BaseMsg msg = new BaseMsg(NoticeConstant.RECEIVE_TENDER_AWARD);
		//.doEvent();
	}

	@Override
	public void addOperateLog() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInterface() {
		//调用接口给予投资人奖励
		if (BaseTPPWay.isOpenApi()) {
			if(TPPWay.API_CODE == TPPWay.API_CODE_YJF){
				YjfService apiService = new YjfServiceImpl();
				List<Object> taskList = new ArrayList<Object>();
				Borrow borrow = (Borrow) Global.getTransfer().get("borrow");
				BorrowTender tender = (BorrowTender) Global.getTransfer().get("tender");
				apiService.fullSuccessAward(borrow, tender, borrow.getUser(), super.user, taskList, money);
				apiService.doApiTask(taskList);
			}
			
		}

	}

	@Override
	public void extend() {
		// TODO Auto-generated method stub

	}
}
