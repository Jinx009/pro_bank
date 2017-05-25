package com.rongdu.p2psys.user.executer;

import java.util.Map;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.UserCreditApply;

/**
 * 信用额度申请
 * 
 * @author qj
 */
public class ApplyUserCreditExecuter extends BaseExecuter {

	Logger logger = Logger.getLogger(ApplyUserCreditExecuter.class);
	protected OperationLogDao operationLogDao;

	@Override
	public void prepare() {
		operationLogDao = (OperationLogDao) BeanUtil.getBean("operationLogDao");
	}

	@Override
	public void addAccountLog() {
	}

	@Override
	public void handleAccount() {

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
		Map<String, Object> map = Global.getTransfer();
		UserCreditApply userCreditApply = (UserCreditApply) map.get("userCreditApply");
		OperationLog operationLog = new OperationLog(user, new Operator(1L), Constant.APPLY_USER_AMOUNT);
		operationLog.setOperationResult("用户名为" + user.getUserName() + "的用户申请信用额度,申请（额度为" + userCreditApply.getAccount()
				+ "）成功，等待审核。");
		operationLogDao.save(operationLog);
	}

	@Override
	public void handleInterface() {
	}

	@Override
	public void extend() {
	}

}
