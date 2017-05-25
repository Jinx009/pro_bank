package com.rongdu.p2psys.borrow.model.worker;

import java.util.Date;

import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.interest.InterestCalculator;
import com.rongdu.p2psys.borrow.service.AutoBorrowService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.user.domain.User;

/**
 * 秒还标
 * @author cx
 *
 */
public class SecondBorrowWorker extends BaseBorrowWorker {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 7375703874958748525L;

	public SecondBorrowWorker(Borrow data, BorrowConfig config) {
		super(data, config);
	}

	/**
	 * 撤标
	 */
	@Override
	public void revokeBorrow() {
		super.revokeBorrow();
	}
	
	/**
	 * 秒标满标复审不通过或通过解冻资金
	 */
	@Override
	public void secondUnVerifyFreeze() {
		double interest = this.calculateInterest();
		double fee = this.calculateBorrowFee();
		double award = this.calculateBorrowAward();
		double freezeVal = BigDecimalUtil.round(BigDecimalUtil.add(interest, fee, award));
		Global.setTransfer("money", freezeVal);
		Global.setTransfer("borrow", this.data);
		AbstractExecuter executer = ExecuterHelper.doExecuter("secondBorrowUnFeezeExcuter");
		executer.execute(freezeVal, data.getUser());
	}

	public BorrowModel checkTenderBefore(BorrowModel model, double tenderMoney, User user, int flow_count) {
		super.checkTenderBefore(model, tenderMoney, user, flow_count);
		BorrowTenderDao tenderDao = (BorrowTenderDao) BeanUtil.getBean("borrowTenderDao");
		// 当是秒标时候，判断有没有超过当天投标次数限制
		double total_tendernum = tenderDao.getUserTenderNum(user.getUserId(), new Date(), new Date());
		// 如果设置了秒标投标限制，且今天已经投标的次数大于等于这个限制，就不让继续投标，这里成功投标是指还没复审或者已经复审的投标，撤销的不算
		if (Global.getInt("miao_tender_day_limit") > 0 && total_tendernum >= Global.getInt("miao_tender_day_limit")) {
			throw new BorrowException("你已经到达今天秒标的最大投标次数（" + Global.getInt("miao_tender_day_limit") + "次）!", 1);
		}
		return model;
	}

	@Override
	public Borrow handleBorrowBeforePublish(Borrow borrow) {
		// if (borrow.getVerifyTime() == null) {
		// borrow.setVerifyTime(new Date());
		// }
		//secondFreezeAccount(borrow);
		return borrow;
	}

	/** 秒标需要发标时就冻结资金 **/
	private void secondFreezeAccount(Borrow borrow) {
		AccountDao accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		// 计算需要冻结的资金
		double interest = calculateInterest();
		// TODO 未取值
		double fee = calculateBorrowFee();
		double award = calculateBorrowAward();

		double freezeVal = BigDecimalUtil.round(BigDecimalUtil.add(interest, fee, award));
		// 冻结账户资金
		Account act = accountDao.findObjByProperty("user.userId", borrow.getUser().getUserId());
		if (act.getUseMoney() < freezeVal) {
			throw new BorrowException("可用余额不足，需要支付利息/奖励 " + freezeVal + " 元，发标失败！", 1);
		}
		Global.setTransfer("money", freezeVal);
		Global.setTransfer("borrow", borrow); // 产生日志根据borrow.id
		AbstractExecuter secondBorrowExecuter = ExecuterHelper.doExecuter("secondBorrowExecuter");
		secondBorrowExecuter.execute(freezeVal, borrow.getUser());
	}

	@Override
	public void stopBorrow() {
	    if (this.data.getStatus() != 1) {
	        throw new BorrowException("借款标的状态不正确！", 1);
	    }
		data.setOldAccount(data.getAccount());
		// 发标account
		double borrow_interest = super.calculateInterest();
		double borrow_award = super.calculateBorrowAward();
		double borrow_freeze = borrow_interest + borrow_award;
		data.setAccount(data.getAccountYes());
        data.setScales(100);
		InterestCalculator ic = super.interestCalculator();
		double repayAccount = ic.repayTotal();
		data.setRepaymentAccount(repayAccount);
		// 撤标之后account
		double tender_interest = super.calculateInterest();
		double tender_award = super.calculateBorrowAward();
		double tender_freeze = tender_interest + tender_award;
		double freeze = borrow_freeze - tender_freeze;
		Global.setTransfer("money", freeze);
		Global.setTransfer("borrow", data);
		AbstractExecuter executer = ExecuterHelper.doExecuter("cutBorrowExcuter");
		executer.execute(freeze, data.getUser());
	}

	@Override
	public void skipReview() {
		super.skipReview();
	}

	
	/**
	 * 自动还款
	 */
	@Override
	public BorrowRepayment repay(BorrowModel model) {
		super.repay(model);
		BorrowRepaymentDao repaymentDao = (BorrowRepaymentDao) BeanUtil.getBean("borrowRepaymentDao");
		BorrowRepayment repayment = repaymentDao.find(model.getId(), 0);
		BorrowService borrowService = (BorrowService)BeanUtil.getBean("borrowService");
		borrowService.doRepay(repayment);
		return repayment;
	}
	
	/**
	 * 投标后是否满标则自动还款
	 */
	@Override
	public void immediateRepayAfterTender(BorrowTender tender) {
		super.immediateInterestAfterTender(tender);
		if(this.data.getAccountYes() >= this.data.getAccount()){
			super.skipReview(); //满标如果跳过复审将状态改为3
			if(this.data.getStatus()==3){
				BorrowModel model = BorrowModel.instance(this.data);
				AutoBorrowService autoBorrowService = (AutoBorrowService)BeanUtil.getBean("autoBorrowService");
				try {
					autoBorrowService.autoVerifyFullSuccess(model);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
