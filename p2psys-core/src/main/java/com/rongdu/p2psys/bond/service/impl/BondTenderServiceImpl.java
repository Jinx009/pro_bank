package com.rongdu.p2psys.bond.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.bond.dao.BondCollectionDao;
import com.rongdu.p2psys.bond.dao.BondDao;
import com.rongdu.p2psys.bond.dao.BondTenderDao;
import com.rongdu.p2psys.bond.domain.Bond;
import com.rongdu.p2psys.bond.domain.BondCollection;
import com.rongdu.p2psys.bond.domain.BondTender;
import com.rongdu.p2psys.bond.exception.BondException;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.bond.model.BondTenderModel;
import com.rongdu.p2psys.bond.service.BondTenderService;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.rule.BondConfigRuleCheck;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;

/**
 * 债权投资Service
 * 
 * @author zhangyz
 * @version 1.0
 * @since 2014-12-11
 */
@Service(value = "bondTenderService")
@Transactional
public class BondTenderServiceImpl implements BondTenderService {

	@Resource
	private BondDao bondDao;
	@Resource
	private BondTenderDao bondTenderDao;
	@Resource
	private BondCollectionDao bondCollectionDao;
	@Resource
	private BorrowCollectionDao borrowCollectionDao;
	@Resource
	private BorrowRepaymentDao borrowRepaymentDao;
	@Resource
	private UserRedPacketDao userRedPacketDao;
	@Resource
	private BorrowTenderDao borrowTenderDao;

	@Override
	public BondTender addBondTender(BondModel bondModel) {
		// 相关验证
		checkBeforTender(bondModel);
		// 债权
		Bond bond = bondDao.getBondById(bondModel.getId());
		// 出让人
		User userSell = bond.getUser();
		// 受让人
		User userBuy = bondModel.getUser();
		// 生成一条债权投资记录
		BondTender bondTender = fillBondTender(bond, bondModel.getMoney(),
				userBuy);
		bondTender.setOrderId(bondModel.getOrderId());
		bondTender.setOrderDate(bondModel.getOrderDate());
		bondTenderDao.save(bondTender);
		Borrow borrow = bond.getBorrow();
		long borrowId = borrow.getId();
		// 总期数
		int periodAll = 0;
		// 天标或者一次性还款只有1期
		if (borrow.getBorrowTimeType() == Borrow.TIME_TYPE_DAY
				|| borrow.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT) {
			periodAll = 1;
		} else {
			periodAll = borrow.getTimeLimit();
		}
		// 下一期还款信息
		BorrowTender borrowTender = borrowTenderDao.find(bond
				.getBorrowTenderId()); // 获得最初发布人的userId
		BorrowCollection nextBorrowCollection = borrowCollectionDao
				.getNextCollectionByBorrowId(borrowId, borrowTender.getUser()
						.getUserId());
		// 下一期还款期数
		int nextPeriod = nextBorrowCollection.getPeriod();
		// 下一期还款日
		Date nextRepaymentTime = nextBorrowCollection.getRepaymentTime();
		// 计算剩余本金
		double remainCapital = getRemainCapital(bond);
		// 计算已经成功售出的债权
		double soldCapital = bondDao.getSoldCapitalByKfId(bond.getKfId(),
				bond.getType());
		// 是否已经全部转让完
		boolean fullSold = false;
		if (BigDecimalUtil.add(bondTender.getTenderMoney(), soldCapital) >= remainCapital) {
			fullSold = true;
		}
		// 本次转让本金占剩余本金的比率
		double tenderRate = BigDecimalUtil.div(bondTender.getTenderMoney(),
				remainCapital);
		// 本次转让的本金
		double bondCapital = 0;
		// 本次转让的利息
		double bondInterest = 0;
		// 转出那部分下一期利息
		double nextInterest = 0;
		// 处理还没还款的每一期待还
		for (int period = nextPeriod; period < periodAll; period++) {
			// 预计还款时间
			Date repaymentTime = null;
			// 预计还款本金
			double capital = 0;
			// 预计还款利息
			double interest = 0;
			// 预计还款奖励
			double award = 0;
			// 债权出让人待收再计算
			// 更新债权出让人原来的待收记录，如果是投资转让，更新待收表（borrowcollecton）,
			// 如果是债权再转让，则更新债权待收表（bondcollection）
			if (bond.getType() == 0) { // 投资转让
				BorrowCollection borrowCollection = borrowCollectionDao
						.getCollectionByTenderAndPeriod(bond.getKfId(), period);
				// 最后全部转出那笔需要做减法
				if (fullSold) {
					// 转出本金
					capital = BigDecimalUtil.sub(borrowCollection.getCapital(),
							borrowCollection.getBondCapital());
					// 转出利息
					interest = BigDecimalUtil.sub(
							borrowCollection.getInterest(),
							borrowCollection.getBondInterest());
					// 转出奖励
					award = BigDecimalUtil.sub(
							borrowCollection.getRepayAward(),
							borrowCollection.getBondAward());
				} else {
					// 最后一期
					// 转出本金
					if (period == periodAll - 1) {
						capital = BigDecimalUtil.round(BigDecimalUtil.sub(
								bondTender.getTenderMoney(), bondCapital));
					} else {
						capital = BigDecimalUtil.round(BigDecimalUtil.mul(
								borrowCollection.getCapital(), tenderRate));
					}
					// 转出利息
					interest = BigDecimalUtil.round(BigDecimalUtil.mul(
							borrowCollection.getInterest(), tenderRate));
					// 转出奖励
					award = BigDecimalUtil.round(BigDecimalUtil.mul(
							borrowCollection.getRepayAward(), tenderRate));
				}
				// 累加已成功转出本金
				borrowCollection.setBondCapital(BigDecimalUtil.add(
						borrowCollection.getBondCapital(), capital));
				// 累加已成功转出利息
				borrowCollection.setBondInterest(BigDecimalUtil.add(
						borrowCollection.getBondInterest(), interest));
				// 累加已成功转出奖励
				borrowCollection.setBondAward(BigDecimalUtil.add(
						borrowCollection.getBondAward(), award));

				// //判断是否已经全部转出，如果全部转出更新待收表记录为已收 status=1 （暂无用）
				// double totalBondMoney =
				// BigDecimalUtil.add(borrowCollection.getBondCapital(),borrowCollection.getInterest());
				// if(borrowCollection.getRepaymentAccount() == totalBondMoney){
				// borrowCollection.setStatus(3);
				// borrowCollection.setRepaymentYesTime(new Date());
				// }

				borrowCollectionDao.update(borrowCollection);
				// 预计还款时间
				repaymentTime = borrowCollection.getRepaymentTime();
			} else { // 债权再转让
				BondCollection bondCollection = bondCollectionDao
						.getCollectionByTenderAndPeriod(bond.getKfId(), period);

				if (fullSold) {
					// 转出本金
					capital = BigDecimalUtil.sub(bondCollection.getCapital(),
							bondCollection.getBondCapital());
					// 转出利息
					interest = BigDecimalUtil.sub(bondCollection.getInterest(),
							bondCollection.getBondInterest());
					// 转出奖励
					award = BigDecimalUtil.sub(bondCollection.getAward(),
							bondCollection.getBondAward());
				} else {
					// 最后一期
					// 转出本金
					if (period == periodAll - 1) {
						capital = BigDecimalUtil.round(BigDecimalUtil.sub(
								bondTender.getTenderMoney(), bondCapital));
					} else {
						capital = BigDecimalUtil.round(BigDecimalUtil.mul(
								bondCollection.getCapital(), tenderRate));
					}
					// 转出利息
					interest = BigDecimalUtil.round(BigDecimalUtil.mul(
							bondCollection.getInterest(), tenderRate));
					// 转出奖励
					award = BigDecimalUtil.round(BigDecimalUtil.mul(
							bondCollection.getAward(), tenderRate));
				}

				// 累加已成功转出本金
				bondCollection.setBondCapital(BigDecimalUtil.add(
						bondCollection.getBondCapital(), capital));
				// 累加已成功转出利息
				bondCollection.setBondInterest(BigDecimalUtil.add(
						bondCollection.getBondInterest(), interest));
				// 累加已成功转出奖励
				bondCollection.setBondAward(BigDecimalUtil.add(
						bondCollection.getBondAward(), award));
				// 更新待收表
				bondCollectionDao.update(bondCollection);
				// 预计还款时间
				repaymentTime = bondCollection.getCollectionTime();
			}

			// 债权受让人待收生成
			BondCollection addBondCollection = fillBondCollection(bond,
					bondTender.getId(), borrowId, period, userBuy,
					repaymentTime, capital, interest);
			bondCollectionDao.save(addBondCollection);

			bondCapital = BigDecimalUtil.add(bondCapital, capital);
			bondInterest = BigDecimalUtil.add(bondInterest, interest);
			// 记录下一次已经转出那部分利息，用于计算归还出让人本期所产生的利息
			if (period == nextPeriod) {
				nextInterest = interest;
			}
		}
		// 更新债权记录(bond)
		bond.setSoldCapital(BigDecimalUtil.add(bond.getSoldCapital(),
				bondCapital));
		bond.setSoldInterest(BigDecimalUtil.add(bond.getSoldInterest(),
				bondInterest));
		// 全部转让
		if (bond.getBondMoney() <= bond.getSoldCapital()) {
			bond.setStatus(Bond.STATUS_SUCC_FULL);
		}

		bondDao.update(bond);
		// 更新债权出让人资金及发送通知
		bondModel.setName("债权" + bondModel.getId());
		Global.setTransfer("bond", bondModel);
		double capital = BigDecimalUtil.round(BigDecimalUtil.mul(
				bondTender.getTenderMoney(),
				BigDecimalUtil.sub(1,
						BigDecimalUtil.div(bond.getBondApr(), 100))));
		double interest = BigDecimalUtil.round(getHappendInterest(
				bond.getBorrow(), nextInterest, nextRepaymentTime));
		// 债权转让管理费
		double manageFee = BigDecimalUtil.round(getManageFee(bondTender
				.getTenderMoney()));
		// 待收本金
		bondCapital = BigDecimalUtil.round(bondCapital);
		// 待收利息
		bondInterest = BigDecimalUtil.round(bondInterest);
		// 债权出转让人资金回收
		doBondOutUserAccount(capital, interest, 0, manageFee, bondCapital,
				bondInterest, 0, userSell, userBuy);
		// 债权投资使用的红包
		Long[] ids = bondModel.getIds();
		double usedRedMoney = userRedPacketDao.getTotalPacketMoneyByIds(ids);
		if (ids != null) { // 红包存放tender
			for (long id : ids) {
				UserRedPacket userRedPacket = userRedPacketDao.find(id);
				userRedPacket.setUsed(true);
				userRedPacket.setBondTender(bondTender);
				userRedPacketDao.merge(userRedPacket);
			}
		}
		// 更新债权受让人资金及发送通知
		doBondInUserAccount(capital, interest, 0, bondCapital, bondInterest, 0,
				usedRedMoney, userBuy, userSell);
		// 提前付息
		bondTender.setPayInterest(interest);
		// 更新债权投资表
		bondTenderDao.update(bondTender);

		// 债权全部转让成功时给债权出让人发送通知
		if (bond.getBondMoney() <= bond.getSoldCapital()) {
			Global.setTransfer("user", userSell);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondSellFullNoticeExecuter");
			repayExecuter.execute(0, userSell);
		}

		return bondTender;
	}

	/**
	 * 债权转让前验证
	 * 
	 * @param bondModel
	 */
	private void checkBeforTender(BondModel bondModel) {
		// 债权
		Bond bond = bondDao.getBondById(bondModel.getId());

		if (bond == null) {
			throw new BondException("此债权不存在!");
		}

		// 债权已经全部转让
		if (bond.getBondMoney() <= bond.getSoldCapital()) {
			throw new BondException("此债权已经全部转让!");
		}
	}

	/**
	 * 计算剩余本金
	 * 
	 * @param bond
	 * @return
	 */
	private double getRemainCapital(Bond bond) {

		double remainCapital = 0;
		// 投资转让
		if (bond.getType() == 0) {
			remainCapital = borrowCollectionDao.getRemainderCapital(bond
					.getKfId());
			// 债权再转让
		} else {
			remainCapital = bondCollectionDao.getRemainderCapital(bond
					.getKfId());
		}
		if (remainCapital == 0) {
			throw new BondException("此债权剩余本金已经全部转让!");
		}
		return remainCapital;
	}

	/**
	 * 生成一条债权投资记录
	 * 
	 * @param bond
	 * @param bondMoney
	 * @return
	 */
	private BondTender fillBondTender(Bond bond, double bondMoney, User user) {
		// 生成一条债权投资记录
		BondTender bondTender = new BondTender();
		bondTender.setBond(bond);
		bondTender.setUser(user);
		bondTender.setBorrow(bond.getBorrow());
		bondTender.setBorrowTenderId(bond.getBorrowTenderId());
		bondTender.setStatus((byte) 0);
		double validAccount = bondMoney;
		double account_val = bond.getBondMoney();
		double account_yes_val = bond.getSoldCapital();
		// 受让人输入的转让金额大于债权剩余转让金额的时候，受让人能转让的金额应为债权剩余金额
		if (bondMoney + account_yes_val >= account_val) {
			validAccount = account_val - account_yes_val;
		}
		bondTender.setTenderMoney(validAccount);
		bondTender.setAddTime(new Date());
		return bondTender;
	}

	/**
	 * 生成一条债权受让人待收
	 * 
	 * @param bond
	 * @param bondTenderId
	 * @param borrowId
	 * @param period
	 * @param user
	 * @param repaymentTime
	 * @param capital
	 * @param interest
	 * @return
	 */
	private BondCollection fillBondCollection(Bond bond, long bondTenderId,
			long borrowId, int period, User user, Date repaymentTime,
			double capital, double interest) {
		// 生成一条债权受让人待收
		BondCollection addBondCollection = new BondCollection();
		addBondCollection.setBond(bond);
		addBondCollection.setBondTenderId(bondTenderId);
		addBondCollection.setBorrow(bond.getBorrow());
		addBondCollection.setBorrowTenderId(bond.getBorrowTenderId());
		BorrowRepayment borrowRepayment = borrowRepaymentDao.find(borrowId,
				period);
		if (borrowRepayment != null) {
			addBondCollection.setBorrowRepaymentId(borrowRepayment.getId());
		}
		addBondCollection.setStatus((byte) 0);
		addBondCollection.setUser(user);
		addBondCollection.setPeriod((byte) period);
		addBondCollection.setCollectionTime(repaymentTime);
		addBondCollection.setCollectionAccount(BigDecimalUtil.add(capital,
				interest));
		addBondCollection.setCapital(capital);
		addBondCollection.setInterest(interest);
		addBondCollection.setAddTime(new Date());
		return addBondCollection;
	}

	/**
	 * 计算到现在为止已经产生利息
	 * 
	 * @param interest
	 * @param nextRepaymentTime
	 * @return
	 */
	private double getHappendInterest(Borrow borrow, double interest,
			Date nextRepaymentTime) {
		Date lastRepaymentTime = null;
		if (borrow.getBorrowTimeType() == Borrow.TIME_TYPE_DAY
				|| borrow.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT) {
			// 上一期还款日
			lastRepaymentTime = borrow.getReviewTime();
		} else {
			// 上一期还款日
			lastRepaymentTime = DateUtil.rollMon(nextRepaymentTime, -1);
		}
		// 本期所有天数
		long allDays = DateUtil.daysBetween(lastRepaymentTime,
				nextRepaymentTime);
		// 到现在为止已经产生利息的天数
		long days = DateUtil.daysBetween(lastRepaymentTime, new Date());
		// 到现在为止已经产生利息
		double result = BigDecimalUtil.mul(interest,
				BigDecimalUtil.div(days, allDays));
		if (result < 0) {
			result = 0;
		}
		return result;
	}

	/**
	 * 计算债权转让管理费
	 * 
	 * @param money
	 * @return
	 */
	private double getManageFee(double capital) {

		double manageCapital = 0;
		BondConfigRuleCheck bondConfigRuleCheck = (BondConfigRuleCheck) Global
				.getRuleCheck("bondConfig");
		// 债权管理费未启用
		if (bondConfigRuleCheck.status == 0) {
			manageCapital = 0;
		} else {
			// 按笔数收
			if (bondConfigRuleCheck.feeType == 1) {
				manageCapital = bondConfigRuleCheck.sellFee;
				// 按百分比收
			} else if (bondConfigRuleCheck.feeType == 2) {
				manageCapital = BigDecimalUtil.mul(capital,
						bondConfigRuleCheck.sellFee);
			}
		}
		return manageCapital;
	}

	/**
	 * 处理债权出让人资金及发送通知
	 * 
	 * @param capital
	 * @param interest
	 * @param award
	 * @param manageFee
	 * @param collCapital
	 * @param collInterest
	 * @param collAward
	 * @param user
	 * @param toUser
	 */
	private void doBondOutUserAccount(double capital, double interest,
			double award, double manageFee, double collCapital,
			double collInterest, double collAward, User user, User toUser) {

		// 债权成交实际金额
		if (capital > 0) {
			Global.setTransfer("capital", capital);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondSellCapitalExecuter");
			repayExecuter.execute(capital, user, toUser);
		}

		// 产生的利息
		if (interest > 0) {
			Global.setTransfer("interest", interest);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondSellInterestExecuter");
			repayExecuter.execute(interest, user, toUser);
		}

		// 扣除利息管理费
		double manageInterest = BigDecimalUtil.mul(interest,
				Global.getDouble("borrow_fee"));
		if (manageInterest > 0) {
			Global.setTransfer("money", manageInterest);
			AbstractExecuter manageFeeExecuter = ExecuterHelper
					.doExecuter("bondSellInterestManageFeeExecuter");
			manageFeeExecuter.execute(manageInterest, user, new User(
					Constant.ADMIN_ID));
		}

		// 产生的奖励
		if (award > 0) {
			Global.setTransfer("award", award);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondSellAwardExecuter");
			repayExecuter.execute(award, user, toUser);
		}

		// 扣除债权转让管理费
		if (manageFee > 0) {
			Global.setTransfer("money", capital);
			Global.setTransfer("manageFee", manageFee);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondSellManageFeeExecuter");
			repayExecuter.execute(manageFee, user, toUser);
		}

		// 减少代收本金
		if (collCapital > 0) {
			Global.setTransfer("collCapital", collCapital);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondSellCollCapitalExecuter");
			repayExecuter.execute(collCapital, user, toUser);
		}

		// 减少代收利息
		if (collInterest > 0) {
			Global.setTransfer("collInterest", collInterest);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondSellCollInterestExecuter");
			repayExecuter.execute(collInterest, user, toUser);
		}

		// 减少代收奖励
		if (collAward > 0) {
			Global.setTransfer("collAward", collAward);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondSellCollAwardExecuter");
			repayExecuter.execute(collAward, user, toUser);
		}

		// // 给债权出让人发送通知
		// Global.setTransfer("user", userOut);
		// AbstractExecuter repayExecuter =
		// ExecuterHelper.doExecuter("bondSellNoticeExecuter");
		// repayExecuter.execute(capital, userOut, userIn);

	}

	/**
	 * 处理债权受让人资金及发送通知
	 * 
	 * @param capital
	 * @param interest
	 * @param award
	 * @param collCapital
	 * @param collInterest
	 * @param collAward
	 * @param redMoney
	 * @param userOut
	 * @param userIn
	 */
	private void doBondInUserAccount(double capital, double interest,
			double award, double collCapital, double collInterest,
			double collAward, double redMoney, User user, User toUser) {

		// 债权成交实际金额
		if (capital > 0) {
			Global.setTransfer("capital", capital);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondBuyCapitalExecuter");
			repayExecuter.execute(capital, user, toUser, redMoney);
		}

		// 使用红包
		if (redMoney > 0) {
			Global.setTransfer("redMoney", redMoney);
			AbstractExecuter redExecuter = ExecuterHelper
					.doExecuter("bondBuyRedMoneyExecuter");
			redExecuter.execute(redMoney, user, toUser);
		}

		// 产生的利息
		if (interest > 0) {
			Global.setTransfer("interest", interest);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondBuyInterestExecuter");
			repayExecuter.execute(interest, user, toUser);
		}

		// 产生的奖励
		if (award > 0) {
			Global.setTransfer("award", award);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondBuyAwardExecuter");
			repayExecuter.execute(award, user, toUser);
		}

		// 增加代收本金
		if (collCapital > 0) {
			Global.setTransfer("collCapital", collCapital);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondBuyCollCapitalExecuter");
			repayExecuter.execute(collCapital, user, toUser);
		}

		// 增加代收利息
		if (collInterest > 0) {
			Global.setTransfer("collInterest", collInterest);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondBuyCollInterestExecuter");
			repayExecuter.execute(collInterest, user, toUser);
		}

		// 增加代收奖励
		if (collAward > 0) {
			Global.setTransfer("collAward", collAward);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("bondBuyCollAwardExecuter");
			repayExecuter.execute(collAward, user, toUser);
		}

		// // 给债权受让人发送通知
		// Global.setTransfer("user", userOut);
		// AbstractExecuter repayExecuter =
		// ExecuterHelper.doExecuter("bondBuyNoticeExecuter");
		// repayExecuter.execute(capital, userOut, userIn);

	}

	@Override
	public BondTender getBondTenderById(long id) {
		return bondTenderDao.getBondTenderById(id);
	}

	@Override
	public BondTender bondTenderUpdate(BondTender bondTender) {
		return bondTenderDao.update(bondTender);

	}

	@Override
	public void deleteBondTender(long id) {
		bondTenderDao.delete(id);
	}

	@Override
	public PageDataList<BondTender> getTenderPage(QueryParam param) {
		return bondTenderDao.findPageList(param);
	}

	@Override
	public PageDataList<BondTenderModel> getTenderModelPage(
			BondTenderModel model) {
		PageDataList<BondTender> itemPage = bondTenderDao
				.getTenderModelPage(model);
		PageDataList<BondTenderModel> modelPage = new PageDataList<BondTenderModel>();
		List<BondTenderModel> list = new ArrayList<BondTenderModel>();
		modelPage.setPage(itemPage.getPage());
		if (itemPage.getList().size() > 0) {
			for (BondTender item : itemPage.getList()) {
				BondTenderModel model_ = BondTenderModel.instance(item);
				model_.setBondApr(item.getBond().getBondApr());
				String username = item.getUser().getUserName();
				String hideName = username.charAt(0) + "******"
						+ username.charAt(username.length() - 1);
				model_.setUserName(hideName);
				model_.setUser(null);
				list.add(model_);
			}
			modelPage.setList(list);
		}
		return modelPage;
	}

	@Override
	public PageDataList<BondTenderModel> getBoughtBondList(BondModel model) {

		PageDataList<BondTender> itemPage = bondTenderDao
				.getBoughtBondList(model);
		PageDataList<BondTenderModel> modelPage = new PageDataList<BondTenderModel>();
		List<BondTenderModel> list = new ArrayList<BondTenderModel>();
		modelPage.setPage(itemPage.getPage());
		if (itemPage.getList().size() > 0) {
			for (BondTender item : itemPage.getList()) {
				BondTenderModel model_ = BondTenderModel.instance(item);
				model_.setBondApr(item.getBond().getBondApr());
				model_.setBorrowId(item.getBorrow().getId());
				list.add(model_);
			}
			modelPage.setList(list);
		}
		return modelPage;

	}

	@Override
	public List<BondTenderModel> getLatestTenerList() {
		List<BondTender> list = bondTenderDao.getLatestTenerList();
		List<BondTenderModel> resultList = new ArrayList<BondTenderModel>();
		if (list != null && list.size() > 0) {
			for (BondTender item : list) {
				BondTenderModel bondTenderModel = BondTenderModel
						.instance(item);
				String username = item.getUser().getUserName();
				String hideName = username.charAt(0) + "******"
						+ username.charAt(username.length() - 1);
				bondTenderModel.setUserName(hideName);
				bondTenderModel.setUser(null);
				resultList.add(bondTenderModel);
			}
		}
		return resultList;
	}

}
