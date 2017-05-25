package com.rongdu.p2psys.core.model;

import org.springframework.beans.BeanUtils;

import com.rongdu.p2psys.core.domain.VerifyLog;

/**
 * 审核记录Model
 */
public class VerifyLogModel extends VerifyLog {

	private static final long serialVersionUID = 5809044850702733920L;

	/**
	 * 1:初审
	 */
	public static int VERIFY_TYPE__TRIAL = 1;

	/**
	 * 2:复审
	 */
	public static int VERIFY_TYPE__RECHECK = 2;

	/**
	 * 3:截标
	 */
	public static int VERIFY_TYPE__STOP = 3;

	/**
	 * 1:成功
	 */
	public static int VERIFY_STATUS__PASS = 1;

	/**
	 * 2:失败
	 */
	public static int VERIFY_STATUS__FAILURE = 2;

	/**
	 * 操作员用户名
	 */
	private String opName;

	public static VerifyLogModel instance(VerifyLog verifyLog) {
		VerifyLogModel model = new VerifyLogModel();
		BeanUtils.copyProperties(verifyLog, model);
		return model;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

}
