package com.rongdu.p2psys.user.service;

import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.user.domain.UserCreditApply;
import com.rongdu.p2psys.user.domain.UserCreditLog;
import com.rongdu.p2psys.user.domain.UserCredit;

/**
 * 信用额度Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月25日
 */
public interface UserCreditService {

	public UserCredit findByUserId(long userId);

	/**
	 * @param userId
	 * @return
	 */
	public UserCredit getUserCredit(long userId);

	public void update(double totalVar, double useVar, double nouseVar, long userId);

	/**
	 * 操作日志
	 * 
	 * @param userCreditLog
	 */
	public void saveUserCreditLog(UserCreditLog userCreditLog);

	

	/**
	 * 申请信用额度
	 * 
	 * @param userCreditApply
	 */
	public void applyUserCredit(UserCreditApply userCreditApply);

	/**
	 * 审核信用额度
	 * 
	 * @param userCreditApply
	 * @param userCreditLog
	 */
	public void verifyApplyUserCredit(UserCreditApply userCreditApply, UserCreditLog userCreditLog, Operator operator);

}
