package com.rongdu.p2psys.borrow.model.worker;

import java.util.List;

import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.borrow.dao.BorrowDao;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowConfig;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.interest.InterestCalculator;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderInvestSuccessLog;
import com.rongdu.p2psys.user.domain.User;

/**
 * 流转标
 */
public class FlowBorrowWorker extends BaseBorrowWorker {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 7375703874958748525L;

	public FlowBorrowWorker(Borrow data, BorrowConfig config) {
		super(data, config);

	}

	public FlowBorrowWorker(Borrow data, BorrowConfig config, boolean flag) {
		super(data, config, flag);
	}

	@Override
	public void revokeBorrow() {
		throw new BorrowException("如需撤回流转标，请联系平台客服！", 1);
	}

	@Override
	public double calculateInterest() {
		InterestCalculator ic = interestCalculator();
		double interest = ic.repayTotal() - prototype().getFlowMoney()
				* prototype().getFlowCount();
		return interest;
	}

	@Override
	public Borrow handleBorrowBeforePublish(Borrow borrow) {
		if (borrow.getAccount() <= 0) {
			throw new BorrowException("借款金额必须是大于0的整数！", 1);
		}
		if (borrow.getFlowMoney() <= 0) {
			throw new BorrowException("每份金额必须是大于0的整数！", 1);
		}
		if (borrow.getAccount() % borrow.getFlowMoney() != 0) {
			throw new BorrowException("借款金额必须是每份金额的整数倍！", 1);
		}
		borrow.setFlowCount((int) (borrow.getAccount() / borrow.getFlowMoney()));
		borrow.setFlowYesCount(0);
		return borrow;
	}

	@Override
	public InterestCalculator interestCalculator() {
		data.setRepaymentAccount(0);
		double account = data.getFlowMoney() * data.getFlowCount();
		InterestCalculator ic = interestCalculator(account);
		return ic;
	}

	public BorrowModel checkTenderBefore(BorrowModel model, double tenderMoney,
			User user, int flow_count) {
		if (flow_count < 1) {
			throw new BorrowException("投标的份数必须大于1的整数！", 1);
		}
		tenderMoney = model.getFlowMoney() * flow_count;
		super.checkTenderBefore(model, tenderMoney, user, flow_count);
		return model;
	}

	@Override
	public List<BorrowCollection> createCollectionList(BorrowTender tender,
			InterestCalculator ic) {
		List<BorrowCollection> collectionlist = super.createCollectionList(
				tender, ic);
		BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
		borrowDao.modifyBorrowAndRepay(data);
		return collectionlist;
	}

	@Override
	public BorrowTender tenderSuccess(BorrowTender tender, InterestCalculator ic) {
		// 总共需要还款金额
		double repaymentAccount = ic.repayTotal();
		double repaymentInterest = repaymentAccount - tender.getAccount();
		tender.setRepaymentAccount(repaymentAccount);
		tender.setInterest(repaymentInterest);
		tender.setWaitAccount(tender.getRepaymentAccount());
		tender.setWaitInterest(tender.getInterest());
		tender.setStatus(1);
		BorrowTenderDao tenderDao = (BorrowTenderDao) BeanUtil
				.getBean("borrowTenderDao");
		tenderDao.modifyBorrowTender(tender);
		return tender;
	}

	@Override
	public double validAccount(BorrowTender tender) {
		double validAccount = super.validAccount(tender);
		double mostSingleLimit = data.getMostSingleLimit();
		if (mostSingleLimit > 0 && validAccount > mostSingleLimit) {
			double one_flow_money = data.getFlowMoney();
			int flow_count = (int) (mostSingleLimit / one_flow_money);
			double real_flow_money = one_flow_money * flow_count;
			validAccount = real_flow_money;
		}
		return validAccount;
	}

	/**
	 * 投标后立即生息
	 */
	@Override
	public void immediateInterestAfterTender(BorrowTender tender) {
		Global.setTransfer("tender", tender);
		// 扣除冻结/生产待收本金
		double validAccount = tender.getAccount();
		AbstractExecuter flowBorrowExecuter = ExecuterHelper
				.doExecuter("flowBorrowDecuctFreezeExecuter");
		flowBorrowExecuter.execute(validAccount, tender.getUser(),
				data.getUser());
		// 生产待收利息
		double interest = calculateInterest(validAccount);
		Global.setTransfer("money", interest);
		AbstractExecuter flowBorrowWaitInterestExecuter = ExecuterHelper
				.doExecuter("flowBorrowWaitInterestExecuter");
		flowBorrowWaitInterestExecuter.execute(interest, tender.getUser(),
				data.getUser());
		// 借款人资金入账
		Global.setTransfer("money", validAccount);
		AbstractExecuter flowBorrowSuccessExecuter = ExecuterHelper
				.doExecuter("flowBorrowSuccessExecuter");
		flowBorrowSuccessExecuter.execute(validAccount, data.getUser(),
				tender.getUser());
		// 投标奖励
		double awardValue = calculateAward(validAccount);
		if (awardValue > 0) {
			// 扣除借款人奖励
			Global.setTransfer("award", awardValue);
			AbstractExecuter deductAwardExecuter = ExecuterHelper
					.doExecuter("deductAwardExecuter");
			deductAwardExecuter.execute(awardValue, data.getUser(),
					tender.getUser());
			// 给予投资人奖励
			AbstractExecuter awardTenderAwardExecuter = ExecuterHelper
					.doExecuter("awardTenderAwardExecuter");
			awardTenderAwardExecuter.execute(awardValue, tender.getUser(),
					data.getUser());

		}
		// 投资积分处理
		BaseScoreLog bLog = new TenderInvestSuccessLog(tender.getUser()
				.getUserId(), data, tender);
		bLog.doEvent();
		// 流转标投标后向repayment表插入数据，产生待还记录
		List<BorrowRepayment> list = createFlowRepaymentList(collectionList);
		BorrowRepaymentDao repaymentDao = (BorrowRepaymentDao) BeanUtil
				.getBean("borrowRepaymentDao");
		repaymentDao.save(list);
		AbstractExecuter baseExecuter = new BaseExecuter();
		baseExecuter.execute(0, tender.getUser(), data.getUser());
	}

	@Override
	public void validBeforeRepayment(BorrowRepayment borrowRepayment,
			Account account) {
		throw new BorrowException("流转标不能手动还款！", 1);
	}

}
