package com.rongdu.p2psys.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.borrow.dao.BorrowDao;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.user.dao.UserCreditApplyDao;
import com.rongdu.p2psys.user.dao.UserCreditDao;
import com.rongdu.p2psys.user.dao.UserCreditLogDao;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserCreditApply;
import com.rongdu.p2psys.user.domain.UserCreditLog;
import com.rongdu.p2psys.user.service.UserCreditService;

@Service("userCreditService")
public class UserCreditServiceImpl implements UserCreditService {

	@Resource
	private UserCreditDao userCreditDao;
	@Resource
	private UserCreditApplyDao userCreditApplyDao;
	@Resource
	private UserCreditLogDao userCreditLogDao;
	@Resource
	private BorrowDao borrowDao;
	@Resource
	private VerifyLogDao verifyLogDao;

	public UserCredit findByUserId(long userId) {
		return userCreditDao.findObjByProperty("user.userId", userId);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addUserCreditApply(UserCreditApply userCreditApply) {
		List list = new ArrayList();
		list.add(userCreditApply);
		userCreditDao.save(list);
	}

	@Override
	public UserCredit getUserCredit(long id) {
		return userCreditDao.find(id);
	}

	@Override
	public void update(double totalVar, double useVar, double nouseVar, long userId) {
		userCreditDao.update(totalVar, useVar, nouseVar, userId);
	}

	@Override
	public void saveUserCreditLog(UserCreditLog userCreditLog) {
		userCreditLogDao.save(userCreditLog);
	}

	

	/**
	 * 申请信用额度
	 */
	@Override
	public void applyUserCredit(UserCreditApply userCreditApply) {
		// 添加信用额度审核记录
		userCreditApplyDao.save(userCreditApply);
		// 进入executer处理
		Global.setTransfer("userCreditApply", userCreditApply);
		AbstractExecuter executer = ExecuterHelper.doExecuter("applyUserCreditExecuter");
		executer.execute(0, userCreditApply.getUser(), null);
	}

	/**
	 * 审核信用额度
	 */
	@Override
	public void verifyApplyUserCredit(UserCreditApply userCreditApply, UserCreditLog userCreditLog, Operator operator) {
		// 更新信用额度申请信息
		userCreditApplyDao.save(userCreditApply);
		// 添加信用额度日志
		userCreditLogDao.save(userCreditLog);

		// 进入execute处理
		AbstractExecuter executer = ExecuterHelper.doExecuter("verifyApplyUserCreditExecuter");
		Global.setTransfer("userCreditApply", userCreditApply);
		executer.execute(0, userCreditApply.getUser().getUserId(), operator);

		// 添加审核日志
		VerifyLog verifyLog = new VerifyLog(new Operator(userCreditApply.getVerifyUser()), "credit",
				userCreditApply.getId(), 2, userCreditApply.getStatus(), userCreditApply.getRemark());
		verifyLogDao.save(verifyLog);
	}

}
