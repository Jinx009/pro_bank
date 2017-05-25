package com.rongdu.p2psys.borrow.executer;

import java.util.ArrayList;
import java.util.List;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.yjf.service.YjfService;
import com.rongdu.p2psys.tpp.yjf.service.impl.YjfServiceImpl;
import com.rongdu.p2psys.user.domain.User;

/**
 * 交易手续费
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-21
 */
public class DeductTransactionFeeExecuter extends BaseExecuter {

	private String accountLogType = Constant.TRANSACTION_FEE;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void prepare() {
		super.prepare();
	}

	@Override
	public void addAccountLog() {
		Global.setTransfer("weburl", Global.getString("weburl"));
		Account account = accountDao.findObjByProperty("user.userId", super.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		log.setMoney(super.money);
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
		accountDao.modify(-super.money, 0, -super.money, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {

	}

	@Override
	public void handlePoints() {

	}

	@Override
	public void handleNotice() {

	}

	@Override
	public void addOperateLog() {

	}

	@Override
	public void handleInterface() {
		// 扣除交易手续费
		if (BaseTPPWay.isOpenApi()) {
			if(TPPWay.API_CODE == TPPWay.API_CODE_YJF){
				YjfService apiService = new YjfServiceImpl();
				BorrowModel borrow = (BorrowModel) Global.getTransfer().get("deduct");
				List<Object> taskList = new ArrayList<Object>();
				apiService.fullSuccessDeductFee(taskList, money, borrow, borrow.getUser());
				apiService.doApiTask(taskList);
			}
			
		}
	}

	@Override
	public void extend() {

	}
}
