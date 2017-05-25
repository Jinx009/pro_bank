package com.rongdu.p2psys.borrow.model.worker;

import java.util.Date;
import java.util.List;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.interest.InterestCalculator;
import com.rongdu.p2psys.user.domain.User;

/**
 * 封装Borrow业务处理接口
 */
public interface BorrowWorker {

	Borrow prototype();

	/**
	 * 后台校验
	 * 
	 * @return boolean
	 */
	boolean checkModelData();

	/**
	 * 默认值
	 */
	void setBorrowField();

	/**
	 * 截标
	 */
	void stopBorrow();

	/**
	 * 撤标
	 */
	void cancelBorrow();

	/**
	 * 撤回
	 */
	void revokeBorrow();

	/**
	 * 秒标初审不通过解冻资金
	 */
	void secondUnVerifyFreeze();

	/**
	 * 还款处理
	 * 
	 * @param model
	 * @return BorrowRepayment
	 */
	BorrowRepayment repay(BorrowModel model);

	/**
	 * 是否跳过满表复审
	 */
	void skipReview();

	/**
	 * 计算借款标的利息
	 * 
	 * @return double
	 */
	double calculateInterest();

	/**
	 * 计算借款标的利息
	 * 
	 * @param validAccount
	 * @return double
	 */
	double calculateInterest(double validAccount);

	InterestCalculator interestCalculator();

	InterestCalculator interestCalculator(double validAccount);

	/**
	 * 计算借款标的手续费
	 * 
	 * @return double
	 */
	double calculateBorrowFee();

	double calculateBorrowAward();

	Date getRepayTime(int period);

	Date getFlowRepayTime(int period);

	boolean isNeedRealName();

	boolean isNeedEmail();

	boolean isNeedPhone();

	/**
	 * 是否最后一期
	 * 
	 * @param period
	 * @return boolean
	 */
	boolean isLastPeriod(int period);

	List<BorrowCollection> createCollectionList(BorrowTender tender,
			InterestCalculator ic);

	Date calCollectionRepayTime(BorrowTender tender, int period);

	List<BorrowRepayment> createFlowRepaymentList(List<BorrowCollection> clist);

	double calculateAward(double account);

	double validAccount(BorrowTender tender);

	boolean allowPublish(User user);

	/**
	 * 发标前处理
	 * 
	 * @param borrow
	 * @return Borrow
	 */
	Borrow handleBorrowBeforePublish(Borrow borrow);

	/**
	 * 发标后处理
	 * 
	 * @param borrow
	 * @return Borrow
	 */
	Borrow handleBorrowAfterPublish(Borrow borrow);

	/**
	 * 环迅投标校验
	 * 
	 * @param model
	 * @param tenderNum
	 * @param totalPacketMoney
	 * @param user
	 * @param pwd
	 */
	void checkTender(BorrowModel model, double tenderNum,
			double totalPacketMoney, User user, String pwd);

	BorrowTender tenderSuccess(BorrowTender tender, InterestCalculator ic);

	/**
	 * 满标复审处理第三方接口
	 */
	void handleVerifyFull();

	/**
	 * 满标复审通过后，处理tender、collection
	 */
	void handleTenderAfterFullSuccess();

	/**
	 * 满标复审通过后，处理borrow、repayment
	 */
	void handleBorrowAfterFullSuccess();

	/**
	 * 满标复审未通过后，处理tender
	 */
	void handleTenderAfterFullFail();

	/**
	 * 投标后立即生息，流转标
	 * 
	 * @param tender
	 */
	void immediateInterestAfterTender(BorrowTender tender);

	/**
	 * 投完标后立即还款，秒标、提前还息的几个
	 * 
	 * @param tender
	 */
	void immediateRepayAfterTender(BorrowTender tender);

	/**
	 * 计算借款手续费
	 * 
	 * @return double
	 */
	double getManageFee();

	/**
	 * 还款前校验
	 * 
	 * @param borrowRepayment
	 * @param account
	 */
	void validBeforeRepayment(BorrowRepayment borrowRepayment, Account account);

	/**
	 * 还款，处理借款人资金
	 * 
	 * @param repay
	 */
	void borrowRepayHandleBorrow(BorrowRepayment repay);

	/**
	 * 还款，处理投资人资金
	 * 
	 * @param repay
	 */
	void borrowRepayHandleTender(BorrowRepayment repay);

	/**
	 * 还款，处理借款人资金(提前还款)
	 * 
	 * @param repay
	 */
	void borrowPriorRepayHandleBorrow(BorrowRepayment repay);

	/**
	 * 还款，处理投资人资金(提前还款)
	 * 
	 * @param repay
	 */
	void borrowPriorRepayHandleTender(BorrowRepayment repay);

	/**
	 * 代偿前校验
	 * 
	 * @param borrowRepayment
	 */
	void validBeforeCompensate(BorrowRepayment borrowRepayment);

}
