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
import com.rongdu.p2psys.user.domain.User;

/**
 * 撤回标解冻发标人资金
 * 
 * @author cx
 * @version 1.0
 * @since 2014-5-16
 */
public class CancelBorrowUnFeezeExcuter extends BaseExecuter {

	protected AccountLogDao accountLogDao;
	protected AccountDao accountDao;
	private String accountLogType = Constant.NEW_BORROW_FEE_UNFREEZE;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void prepare() {
		accountLogDao = (AccountLogDao) BeanUtil.getBean("accountLogDao");
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");

	}

	@Override
	public void addAccountLog() {
		Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId", this.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
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
		accountDao.modify(0, this.money, -this.money, this.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		//撤标调用接口解冻资金，
		if (BaseTPPWay.isOpenApi()) {
			if(TPPWay.API_CODE == TPPWay.API_CODE_YJF){
				YjfService apiService = new YjfServiceImpl();
				Borrow borrow = (Borrow) Global.getTransfer().get("borrow");
				List<Object> taskList = new ArrayList<Object>();
				apiService.failBorrow(taskList, borrow);
				apiService.doApiTask(taskList);
			}
			
		}

	}

	@Override
	public void handlePoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNotice() {
	    BaseMsg msg = new BaseMsg(NoticeConstant.BORROW_CANCEL);
        msg.doEvent();
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
