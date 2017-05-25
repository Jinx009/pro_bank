package com.rongdu.p2psys.borrow.executer;

import java.util.ArrayList;
import java.util.List;

import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.yjf.service.YjfService;
import com.rongdu.p2psys.tpp.yjf.service.impl.YjfServiceImpl;
import com.rongdu.p2psys.user.dao.UserCacheDao;

/**
 * 自动投标冻结资金
 * @author sj
 * @since 2014年8月15日15:40:30
 *
 */
public class AutoBorrowTenderFreezeExecuter extends BaseExecuter {
	
	protected AccountLogDao accountLogDao;
	protected AccountDao accountDao;
	protected UserCacheDao userCacheDao;
	private String accountLogType = Constant.TENDER;

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
		log.setMoney(this.money);
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
		accountDao.modify(0, -super.money, super.money, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {

	}

	@Override
	public void handlePoints() {

	}

	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.AUTO_TENDER);
		msg.doEvent();
	}

	@Override
	public void addOperateLog() {

	}

	@Override
	public void handleInterface() {
		Borrow borrow = (Borrow) Global.getTransfer().get("borrow");
		if (BaseTPPWay.isOpenApi()) {
			if(TPPWay.API_CODE == TPPWay.API_CODE_YJF){
				YjfService apiService = new YjfServiceImpl();
				List<Object> taskList = new ArrayList<Object>();
				apiService.verifyVipSuccess(super.user, money, taskList);
				apiService.doApiTask(taskList);
				if (borrow.getType() == Borrow.TYPE_FLOW) {
					// 流转标直接打款
					apiService.flowBorrowLoan(money, user, borrow, toUser, taskList);
				} else {
					apiService.addTenderFreezeMoney(money, user, borrow, toUser, taskList);
				}
				apiService.doApiTask(taskList);
			}
		}

	}

	@Override
	public void extend() {

	}

}
