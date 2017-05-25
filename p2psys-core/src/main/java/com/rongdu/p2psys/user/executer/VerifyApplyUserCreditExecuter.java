package com.rongdu.p2psys.user.executer;

import java.util.Map;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.UserCreditApply;

/**
 * 审核信用额度
 * 
 * @author qj
 */
public class VerifyApplyUserCreditExecuter extends BaseExecuter {

	Logger logger = Logger.getLogger(VerifyApplyUserCreditExecuter.class);
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
		OperationLog operationLog = new OperationLog(user, operator, Constant.VERIFY_APPLY_USER_AMOUNT);
		operationLog.setOperationResult("用户名为" + operator.getUserName() + "的操作员申请后台审核信用额度,用户名：" + user.getUserName()
				+ "（额度：" + userCreditApply.getAccount() + "），审核状态:" + userCreditApply.getStatus());
		operationLogDao.save(operationLog);
	}

	@Override
	public void handleInterface() {
	}

	@Override
	public void extend() {
	}

}
