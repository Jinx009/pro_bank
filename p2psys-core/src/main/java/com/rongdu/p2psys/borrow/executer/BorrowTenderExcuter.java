package com.rongdu.p2psys.borrow.executer;

import java.util.ArrayList;
import java.util.List;

import com.rongdu.common.util.BigDecimalUtil;
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
 * 投标
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-22
 */
public class BorrowTenderExcuter extends BaseExecuter {

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
		log.setMoney(BigDecimalUtil.add(this.money));
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
		// TODO Auto-generated method stub

	}

	@Override
	public void handlePoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.INVEST_SUCC);
		msg.doEvent();
	}

	@Override
	public void addOperateLog() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInterface() {
		if (BaseTPPWay.isOpenApi()) {
			if(TPPWay.API_CODE == TPPWay.API_CODE_YJF){
				YjfService apiService = new YjfServiceImpl();
				Borrow borrow = (Borrow) Global.getTransfer().get("borrow");
				List<Object> taskList = new ArrayList<Object>();
				if (borrow.getType() == Borrow.TYPE_FLOW) {
					// 流转标直接打款
					apiService.flowBorrowLoan(money, super.user, borrow, super.toUser, taskList);
				} else {
					apiService.addTenderFreezeMoney(money,  super.user, borrow, super.toUser, taskList);
				}
				apiService.doApiTask(taskList);
			}
			
		}
	}

	@Override
	public void extend() {
		// TODO Auto-generated method stub

	}

}
