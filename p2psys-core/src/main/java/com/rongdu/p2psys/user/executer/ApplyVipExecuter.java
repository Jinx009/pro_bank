package com.rongdu.p2psys.user.executer;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 申请vip
 * 
 * @author zxc
 */
public class ApplyVipExecuter extends BaseExecuter {

	Logger logger = Logger.getLogger(ApplyVipExecuter.class);
	protected AccountDao accountDao;
	protected AccountLogDao accountLogDao;
	protected OperationLogDao operationLogDao;
	String accountLogType = Constant.VIP_FEE;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void prepare() {
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		accountLogDao = (AccountLogDao) BeanUtil.getBean("accountLogDao");
		operationLogDao = (OperationLogDao) BeanUtil.getBean("operationLogDao");
	}

	@Override
	public void handleAccount() {
		// 申请vip，冻结资金
		User user = super.user;
		double money = super.money;
		accountDao.modify(0, -money, money, user.getUserId());
	}

	@Override
	public void addAccountLog() {
		Account account = accountDao.getAccountByUserId(super.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		log.setMoney(this.money);
		log.setTotal(account.getTotal());
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());
		log.setCollection(account.getCollection());
		log.setRemark("申请VIP冻结金额" + super.money + "元。");
		log.setPaymentsType((byte)0);
		accountLogDao.save(log);
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
		OperationLog operationLog = new OperationLog(user, operator, Constant.VIP_FREEZE);
		operationLog.setOperationResult("用户名为" + user.getUserName() + "用户，申请vip成功，冻结资金" + money + " 元，等待审核 ");
		operationLogDao.save(operationLog);
	}

	@Override
	public void handleInterface() {
	}

	@Override
	public void extend() {

	}

}
