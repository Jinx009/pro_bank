package com.rongdu.p2psys.user.executer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.yjf.service.YjfService;
import com.rongdu.p2psys.tpp.yjf.service.impl.YjfServiceImpl;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserVipApply;

/**
 * 审核vip
 * 
 * @author zxc
 */
public class VerifyVipExecuter extends BaseExecuter {

	Logger logger = Logger.getLogger(VerifyVipExecuter.class);

	protected UserVipApply userVipApply;

	@Override
	public void prepare() {
		// 初始化...
		accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		accountLogDao = (AccountLogDao) BeanUtil.getBean("accountLogDao");
		userCacheDao = (UserCacheDao) BeanUtil.getBean("userCacheDao");
		userVipApply = (UserVipApply) Global.getTransfer().get("userVipApply");

	}

	@Override
	public void handleAccount() {

		User user = super.user;
		double money = super.money;
		if (userVipApply.getStatus() == 1) {// 审核通过，扣除冻结资金,总金额
			accountDao.modify(-money, 0, -money, user.getUserId());
		} else {// 审核不通过，解冻
			accountDao.modify(0, money, -money, user.getUserId());
		}
	}

	@Override
	public void addAccountLog() {
		AccountLog log = null;
		if (userVipApply.getStatus() == 1) {// 审核通过，扣除冻结资金
			log = new AccountLog(user, Constant.DEDUCT_FREEZE, new User(Constant.ADMIN_ID));
			log.setRemark("vip审核通过，扣除冻结资金" + super.money);
		} else {
			log = new AccountLog(user, Constant.UNFREEZE, new User(Constant.ADMIN_ID));
			log.setRemark("vip审核不通过，解冻冻结资金" + super.money);

		}
		Account act = accountDao.getAccountByUserId(super.user.getUserId());
		log.setAddIp(Global.IP_THREADLOCAL.get());
		log.setAddTime(new Date());
		log.setCollection(act.getCollection());
		log.setNoUseMoney(act.getNoUseMoney());
		log.setTotal(act.getTotal());
		log.setUseMoney(act.getUseMoney());
		log.setMoney(super.money);
		log.setPaymentsType((byte)2);
		accountLogDao.save(log);

	}

	@Override
	public void handleAccountSum() {
		logger.info("处理资金统计.....");
	}

	@Override
	public void handlePoints() {
		logger.info("积分计算.....");
	}

	@Override
	public void handleNotice() {
		logger.info("通知vip申请人.....");
	}

	@Override
	public void addOperateLog() {
		logger.info("添加操作记录.....");
	}

	@Override
	public void handleInterface() {
		if (userVipApply.getStatus() == 1  && BaseTPPWay.isOpenApi()) {
			logger.info("第三方接口处理.....");
			// 审核通过，扣除冻结资金
			if(TPPWay.API_CODE == TPPWay.API_CODE_YJF){
				YjfService apiService = new YjfServiceImpl();
				List<Object> taskList = new ArrayList<Object>();
				apiService.verifyVipSuccess(super.user, money, taskList);
				apiService.doApiTask(taskList);
			}
		}
	}

	@Override
	public void extend() {

	}

}
