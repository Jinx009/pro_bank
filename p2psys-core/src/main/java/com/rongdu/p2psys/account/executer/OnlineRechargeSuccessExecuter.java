package com.rongdu.p2psys.account.executer;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.service.UserRedPacketService;

/**
 * 线上充值executer
 * 
 * @author cx
 * @version 2.0
 * @since 2014-5-17
 */
public class OnlineRechargeSuccessExecuter extends BaseRechargeSuccessExecuter {

	private String accountLogType = Constant.AL_ONLINE_RECHARGE;

	@Override
	public String getAccountLogType() {
		return accountLogType;
	}

	@Override
	public void addAccountLog() {
		Account account = accountDao.findObjByProperty("user.userId", this.user.getUserId());
		AccountLog log = new AccountLog(super.user, accountLogType, new User(Constant.ADMIN_ID));
		AccountRecharge recherge = (AccountRecharge)Global.getTransfer().get("recharge");
		log.setMoney(this.money);
		log.setTotal(account.getTotal());
		log.setUseMoney(account.getUseMoney());
		log.setNoUseMoney(account.getNoUseMoney());
		log.setCollection(account.getCollection());
		
		log.setRemark(this.getLogRemark());
		log.setPaymentsType((byte)1);
		accountLogDao.save(log);

		doRedPacket();
	}

	@Override
	public void handleAccount() {
		accountDao.modify(super.money, super.money, 0, super.user.getUserId());
	}

	@Override
	public void handleAccountSum() {
		super.handleAccountSum();

	}

	@Override
	public void handlePoints() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.RECHARGE_SUCC);
		msg.doEvent();
	}

	@Override
	public void addOperateLog() {
		/*OperationLog operationLog = new OperationLog(super.user, super.operator, Constant.ONLINE_RECHARGE_SUCCESS);
		operationLog.setOperationResult("（IP:" + super.operator.getLoginIp() + "）用户名为" + super.operator.getUserName()
				+ "的操作员审核网上充值" + super.user.getUserName() + super.money + "元成功");
		operationLogDao.save(operationLog);*/
	}

	@Override
	public void handleInterface() {
		// TODO Auto-generated method stub

	}

	@Override
	public void extend() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 充值发放红包
	 */
	public void doRedPacket() {
		UserRedPacketService userRedPacketService = (UserRedPacketService) BeanUtil.getBean("userRedPacketService");
		AccountRecharge recharge = (AccountRecharge)Global.getTransfer().get("recharge");
		userRedPacketService.doRechargeRedPacket(user, recharge);
	}

}
