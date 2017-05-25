package com.rongdu.p2psys.borrow.model.worker;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.borrow.dao.BorrowInterestRateDao;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.user.dao.UserCreditDao;
import com.rongdu.p2psys.user.dao.UserCreditLogDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserCreditLog;

/**
 * 信用标
 * 
 * @author fuxingxing
 * @date 2012-9-5 下午5:18:52
 * @version <b>Copyright (c)</b> 2012-融都rongdu-版权所有<br/>
 */
public class CreditBorrowWorker extends BaseBorrowWorker {

	private static Logger logger = Logger.getLogger(CreditBorrowWorker.class);

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 6478298326297026207L;

	public CreditBorrowWorker(Borrow data, BorrowConfig config, boolean flag) {
		super(data, config, flag);
	}

	/**
	 * 撤标
	 */
	@Override
	public void revokeBorrow() {
		super.revokeBorrow();
		double amount = this.data.getAccount();
		UserCreditDao userCreditDao = (UserCreditDao) BeanUtil
				.getBean("userCreditDao");
		UserCreditLogDao userCreditLogDao = (UserCreditLogDao) BeanUtil
				.getBean("userCreditLogDao");
		UserCredit ua = userCreditDao.findObjByProperty("user.userId",
				this.data.getUser().getUserId());
		if (ua == null) {
			throw new BorrowException("用户" + this.data.getUser().getUserId()
					+ "的信用账户不存在.", 1);
		}
		userCreditDao.update(0, amount, -amount, ua.getUser().getUserId());
		UserCreditLog amountLog = new UserCreditLog();
		amountLog.setUser(ua.getUser());
		amountLog.setType("borrow_cancel");
		amountLog.setAccount(amount);
		amountLog.setAccountAll(ua.getCreditUse());
		amountLog.setAccountUse(ua.getCreditUse() + amount);
		amountLog.setAccountNoUse(ua.getCreditNouse() - amount);
		amountLog.setRemark("用户撤回借款标");
		userCreditLogDao.save(amountLog);
	}

	@Override
	public boolean allowPublish(User user) {
		super.allowPublish(user);
		UserCreditDao uad = (UserCreditDao) BeanUtil.getBean("userCreditDao");
		UserCredit amount = uad.findByUserId(user.getUserId());
		if (amount == null || data.getAccount() > amount.getCreditUse()) {
			throw new BorrowException("可用信用额度不足!", 1);
		}
		return true;
	}

	@Override
	public Borrow handleBorrowAfterPublish(Borrow borrow) {
		update(borrow);
		return borrow;
	}

	private void update(Borrow borrow) {
		UserCreditDao userCreditDao = (UserCreditDao) BeanUtil
				.getBean("userCreditDao");
		UserCreditLogDao userCreditLogDao = (UserCreditLogDao) BeanUtil
				.getBean("userCreditLogDao");
		UserCredit ua = userCreditDao.findObjByProperty("user.userId", borrow
				.getUser().getUserId());
		double amount = borrow.getAccount();
		userCreditDao.update(0, -amount, amount, borrow.getUser().getUserId());
		UserCreditLog amountLog = new UserCreditLog();
		amountLog.setUser(ua.getUser());
		amountLog.setAccount(amount);
		amountLog.setAccountAll(ua.getCredit());
		amountLog.setAccountUse(ua.getCreditUse() - amount);
		amountLog.setAccountNoUse(ua.getCreditNouse() + amount);
		amountLog.setType("");
		amountLog.setRemark("扣除");
		userCreditLogDao.save(amountLog);
	}

	@Override
	public void borrowRepayHandleBorrow(BorrowRepayment repay) {
		super.borrowRepayHandleBorrow(repay);
		// 如果是信用标，将本次的借款金额累加回借款人的信用额度，生成信用额度流水
		UserCreditDao userCreditDao = (UserCreditDao) BeanUtil
				.getBean("userCreditDao");
		UserCredit amount = userCreditDao.findObjByProperty("user.userId", data
				.getUser().getUserId());
		userCreditDao.update(0, repay.getCapital(), -repay.getCapital(), amount
				.getUser().getUserId());
	}

	@Override
	public void borrowPriorRepayHandleBorrow(BorrowRepayment repay) {
		super.borrowPriorRepayHandleBorrow(repay);
		// 如果是信用标，借款金额累加回借款人的信用额度
		UserCreditDao userCreditDao = (UserCreditDao) BeanUtil
				.getBean("userCreditDao");
		BorrowRepaymentDao borrowRepaymentDao = (BorrowRepaymentDao) BeanUtil
				.getBean("borrowRepaymentDao");
		UserCredit amount = userCreditDao.findObjByProperty("user.userId", data
				.getUser().getUserId());
		double waitRemainderRepayCapital = borrowRepaymentDao
				.getRemainderCapital(repay.getBorrow().getId()); // 计算剩余待还本金
		userCreditDao.update(0, waitRemainderRepayCapital,
				-waitRemainderRepayCapital, amount.getUser().getUserId());
	}

	/**
	 * 撤标
	 */
	@Override
	public void stopBorrow() {
		super.stopBorrow();
		UserCreditDao userCreditDao = (UserCreditDao) BeanUtil
				.getBean("userCreditDao");
		UserCreditLogDao userCreditLogDao = (UserCreditLogDao) BeanUtil
				.getBean("userCreditLogDao");
		UserCredit ua = userCreditDao.findObjByProperty("user.userId",
				this.data.getUser().getUserId());
		if (ua == null) {
			throw new BorrowException("用户" + this.data.getUser().getUserId()
					+ "的信用账户不存在.", 1);
		}
		double account = this.data.getOldAccount();
		double account_yes = this.data.getAccountYes();
		double amountBack = account - account_yes;
		userCreditDao.update(0, amountBack, -amountBack, ua.getUser()
				.getUserId());
		UserCreditLog amountLog = new UserCreditLog();
		amountLog.setType("borrow_cancel");
		amountLog.setAccount(amountBack);
		amountLog.setAccountAll(ua.getCreditUse());
		amountLog.setAccountUse(ua.getCreditUse() + amountBack);
		amountLog.setAccountNoUse(ua.getCreditNouse() - amountBack);
		amountLog.setRemark("截标退回多占用的信用额度");
		amountLog.setAddTime(new Date());
		amountLog.setAddIp(Global.getIP());
		userCreditLogDao.save(amountLog);
	}

	@Override
	public void handleTenderAfterFullFail() {
		BorrowTenderDao tenderDao = (BorrowTenderDao) BeanUtil
				.getBean("borrowTenderDao");
		UserCreditDao userCreditDao = (UserCreditDao) BeanUtil
				.getBean("userCreditDao");
		BorrowInterestRateDao borrowInterestRateDao = (BorrowInterestRateDao) BeanUtil
				.getBean("borrowInterestRateDao");
		tenderDao.updateStatus(data.getId(), 2, 0);
		List<BorrowTender> tenderList = tenderDao.findByProperty("borrow.id",
				data.getId());
		for (int i = 0; i < tenderList.size(); i++) {
			BorrowTender tender = (BorrowTender) tenderList.get(i);
			// 还原加息劵
			BorrowInterestRate bir = borrowInterestRateDao
					.findByStatusAndTender(2, tender);
			if (bir != null) {
				bir.setTender(null);
				bir.setStatus(1);
				borrowInterestRateDao.update(bir);
			}
			double account = tender.getAccount();
			Global.setTransfer("money", account);
			Global.setTransfer("tenderAccount", account);
			Global.setTransfer("borrow", data);
			Global.setTransfer("tender", tender);
			Global.setTransfer("user", tender.getUser());
			ProductBasicService productBasicService = (ProductBasicService) BeanUtil
					.getBean("productBasicService");
			ProductBasic productBasic = productBasicService
					.getProductBasicInfo(new Long(data.getType()), data.getId());
			Global.setTransfer("flag", productBasic.getProductTypeFlag());
			AbstractExecuter deductExecuter = ExecuterHelper
					.doExecuter("borrowFailExecuter");
			deductExecuter.execute(account, tender.getUser(), data.getUser());
		}
		userCreditDao.update(0, data.getAccount(), -data.getAccount(), data
				.getUser().getUserId());
	}
}
