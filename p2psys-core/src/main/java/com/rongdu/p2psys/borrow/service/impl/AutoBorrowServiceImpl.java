package com.rongdu.p2psys.borrow.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.bond.dao.BondDao;
import com.rongdu.p2psys.bond.domain.Bond;
import com.rongdu.p2psys.borrow.dao.BorrowAutoDao;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.dao.BorrowDao;
import com.rongdu.p2psys.borrow.dao.BorrowInterestRateDao;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowAuto;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowHelper;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.interest.InterestCalculator;
import com.rongdu.p2psys.borrow.model.worker.BorrowWorker;
import com.rongdu.p2psys.borrow.service.AutoBorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.ProductStatusConstant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitDao;
import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitRecordDao;
import com.rongdu.p2psys.nb.recommend.dao.RecommendRecordDao;
import com.rongdu.p2psys.nb.user.dao.UserInviteDao;
import com.rongdu.p2psys.nb.user.service.CouponService;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrModel;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinaPnrType;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinapnrHelper;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.tpp.domain.TppPnrPay;
import com.rongdu.p2psys.tpp.ips.dao.TppIpsPayDao;
import com.rongdu.p2psys.tpp.yjf.PayModelHelper;
import com.rongdu.p2psys.tpp.yjf.model.TradeCreatePoolReverse;
import com.rongdu.p2psys.tpp.yjf.model.TradePayPoolReverse;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.Coupon;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.service.UserRedPacketService;

@Service("autoBorrowService")
public class AutoBorrowServiceImpl implements AutoBorrowService {

	private static final Logger logger = Logger
			.getLogger(AutoBorrowServiceImpl.class);
	@Resource
	private BorrowRepaymentDao borrowRepaymentDao;
	@Resource
	private BorrowDao borrowDao;
	@Resource
	private BorrowAutoDao borrowAutoDao;
	@Resource
	private BorrowTenderDao tenderDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private UserDao userDao;
	@Resource
	private BorrowCollectionDao borrowCollectionDao;
	@Resource
	private UserCacheDao userCacheDao;
	@Resource
	private ChinapnrService chinapnrService;
	@Resource
	private BorrowCollectionDao collectionDao;
	@Resource
	private BondDao bondDao;
	@Resource
	private BorrowInterestRateDao borrowInterestRateDao;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private BorrowTenderDao borrowTenderDao;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private RecommendProfitRecordDao recommendProfitRecordDao;
	@Resource
	private RecommendProfitDao recommendProfitDao;
	@Resource
	private UserInviteDao theUserInviteDao;
	@Resource
	private RecommendRecordDao recommendRecordDao;
	@Resource
	private CouponService couponService;
	@Resource
	private ProductTypeService productTypeService;

	@Override
	public void autoBorrowRepay(BorrowRepayment borrowRepayment) {
		logger.info("repayment_id=" + borrowRepayment.getId() + "进行还款....");
		Borrow borrow = borrowDao.find(borrowRepayment.getBorrow().getId());
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		try {
			borrowRepayment = checkRepay(borrowRepayment);

			worker.borrowRepayHandleTender(borrowRepayment);
			borrowRepayment.setStatus(this.getRepayStatus(borrowRepayment));
			borrowRepayment.setRepaymentYesTime(new Date());
			// 还款类型1：代偿
			borrowRepayment.setType(Constant.REPAYMENT_TYPE_NORAML);
			// 实际还款者
			borrowRepayment.setRealRepayer(borrow.getUser());
			borrowRepayment = this.getResealRepay(borrowRepayment);// 还款信息根据情况重新封装
			borrowRepaymentDao.updateBorrowRepaymentByStatus(borrowRepayment);
			int status = worker.isLastPeriod(borrowRepayment.getPeriod()) ? 8
					: 7;
			borrow.setStatus(status);
			// borrow.setRepaymentAccount(0);去掉将应还本金置为0的效果
			if ((borrow.getType() == 119 || borrow.getType() == 112)
					&& borrow.getBorrowTimeType() == 1) {
				double total = BigDecimalUtil.add(borrowRepayment.getCapital(),
						borrowRepayment.getInterest());
				borrow.setRepaymentYesAccount(total);
				borrow.setRepaymentYesInterest(borrowRepayment.getInterest());
			} else {
				double total = BigDecimalUtil.add(borrowRepayment.getCapital(),
						borrowRepayment.getInterest());
				borrow.setRepaymentYesAccount(BigDecimalUtil.add(
						borrow.getRepaymentYesAccount(), total));
				borrow.setRepaymentYesInterest(borrow.getRepaymentYesInterest()
						+ borrowRepayment.getInterest());
			}

			borrowDao.update(borrow);
			/*
			 * if(borrow.getType() != Borrow.TYPE_ENTRUST){ // 向借款人发送还款成功通知
			 * Global.setTransfer("repay", borrowRepayment);
			 * Global.setTransfer("user", borrow.getUser()); AbstractExecuter
			 * successExecuter = ExecuterHelper
			 * .doExecuter("borrowRepaySuccessExecuter");
			 * successExecuter.execute(0, borrow.getUser()); }
			 */
			// 还款后停止相关债权转让
			bondDao.stopBond(borrow.getId(), Bond.STATUS_STOP_AUTO);
		} catch (Exception e) {
			// 解冻用户资金
			repayFail(borrowRepayment, borrow);
			logger.error("还款失败");
			throw new BorrowException("还款失败" + e.getMessage());
		}
	}

	private void repayFail(BorrowRepayment borrowRepayment, Borrow borrow) {
		borrowRepayment.setWebStatus(0);
		borrowRepayment = borrowRepaymentDao.update(borrowRepayment);
	}

	@Override
	public void overdue(BorrowRepayment borrowRepayment) {
		Borrow borrow = borrowRepayment.getBorrow();
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		try {
			// 处理投资人资金
			worker.borrowRepayHandleTender(borrowRepayment);
			borrowRepayment.setStatus(2);
			borrowRepayment.setRepaymentYesTime(new Date());
			borrowRepayment
					.setRepaymentYesAccount(BigDecimalUtil.add(
							borrowRepayment.getCapital(),
							borrowRepayment.getInterest()));
			borrowRepayment.setRealRepayer(new User(Constant.ADMIN_ID));
			borrowRepaymentDao.updateBorrowRepaymentByStatus(borrowRepayment);
			double total = BigDecimalUtil.add(borrowRepayment.getCapital(),
					borrowRepayment.getInterest()); // 还款表本金+利息
			borrow.setRepaymentYesAccount(BigDecimalUtil.add(
					borrow.getRepaymentYesAccount(), total));
			borrow.setRepaymentYesInterest(borrow.getRepaymentYesInterest()
					+ borrowRepayment.getInterest());
			borrowDao.update(borrow);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void overduePayment(BorrowRepayment borrowRepayment) {
		Borrow borrow = borrowDao.find(borrowRepayment.getBorrow().getId());
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		try {
			// 处理借款人资金
			worker.borrowRepayHandleBorrow(borrowRepayment);
			// 处理逾期垫付的状态才2更新到1
			borrowRepayment.setStatus(1);
			int status = worker.isLastPeriod(borrowRepayment.getPeriod()) ? 8
					: 7;
			borrow.setStatus(status);
			double total = BigDecimalUtil.add(borrowRepayment.getCapital(),
					borrowRepayment.getInterest()); // 还款表本金+利息
			borrow.setRepaymentYesAccount(BigDecimalUtil.add(
					borrow.getRepaymentYesAccount(), total));
			borrow.setRepaymentYesInterest(borrow.getRepaymentYesInterest()
					+ borrowRepayment.getInterest());
			borrowDao.update(borrow);
			borrowRepaymentDao.updateBorrowRepaymentStatus(borrowRepayment);
			// 通过付汇接口处理借款人还款给平台操作--模拟还款操作
			List<BorrowCollection> list = collectionDao.list(borrowRepayment
					.getBorrow().getId(), borrowRepayment.getPeriod(), 1);
			// 任务列表
			List<Object> taskList = new ArrayList<Object>();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					BorrowCollection collection = list.get(i);
					double money = BigDecimalUtil.add(collection.getCapital(),
							collection.getInterest(),
							collection.getLateInterest());
					double manageFee = 0;
					TppPnrPay cppRepay = fillTppPnrPay(ChinaPnrType.REPAYMENT,
							manageFee, ChinaPnrType.AUTOVERIFYFULLSUCCESS,
							money, null, collection.getTender(), collection
									.getBorrow().getUser());
					taskList.add(cppRepay);
				}
			}
			chinapnrService.doApiTask(taskList);
		} catch (Exception e) {
			logger.error("还款失败");
			throw new BorrowException("还款失败");
		}

	}

	public TppPnrPay fillTppPnrPay(String cmdid, double fee,
			String operateType, double ordamt, User userIn,
			BorrowTender tender, User userOut) {
		TppPnrPay tppPnrPay = new TppPnrPay();
		String date = DateUtil.dateStr7(new Date());
		tppPnrPay.setAddtime(new Date());
		tppPnrPay.setBorrowId(String.valueOf(tender.getBorrow().getId()));
		tppPnrPay.setCmdid(cmdid);
		tppPnrPay.setFee(BaseTPPWay.formatMoney(fee));
		tppPnrPay.setOperateType(operateType);
		tppPnrPay.setOrdId(OrderNoUtils.getSerialNumber());
		tppPnrPay.setOrdamt(ordamt);
		tppPnrPay.setOrddate(date);
		tppPnrPay.setPayUserName("");
		tppPnrPay.setPayUserCustId("1");
		tppPnrPay.setStatus("0");
		tppPnrPay.setSubordId(tender.getTenderBilNo());
		tppPnrPay.setSuborddate(tender.getTenderBilDate());
		tppPnrPay.setTenderId(String.valueOf(tender.getId()));
		tppPnrPay.setTrxId(tender.getTrxId());
		tppPnrPay.setUserName(userOut.getUserName());

		return tppPnrPay;
	}

	@Override
	public void doPriorRepay(BorrowRepayment borrowRepayment) {
		Borrow borrow = borrowDao.find(borrowRepayment.getBorrow().getId());
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		TradePayPoolReverse tpr = null;
		String resultCode = null;
		boolean isOpenApi = BaseTPPWay.isOpenApi();
		double waitRemainderRepayCapital = borrowRepaymentDao
				.getRemainderCapital(borrow.getId()); // 计算剩余待还本金
		double waitRemainderRepayInterest = borrowRepaymentDao
				.getwaitRpayInterest(borrow.getId(),
						borrowRepayment.getPeriod()); // 本次提前还款待还利息总和
		double money = BigDecimalUtil.add(waitRemainderRepayCapital,
				waitRemainderRepayInterest);
		if (isOpenApi) {
			TPPWay way = TPPFactory.getTPPWay(money);

			String payerUserId = String.valueOf(borrow.getUser().getUserId());
			TradeCreatePoolReverse tcp = (TradeCreatePoolReverse) way
					.tradeCreatePoolReverse(payerUserId, money); // 创建还款交易号
			List<BorrowCollection> list = borrowCollectionDao.list(
					borrowRepayment.getBorrow().getId(),
					borrowRepayment.getPeriod());
			StringBuffer sbf = new StringBuffer("[");
			for (BorrowCollection borrowCollection : list) {
				BorrowTender tender = tenderDao.find(borrowCollection
						.getTender().getId());
				sbf.append("{\"orderNo\" : \"" + OrderNoUtils.getSerialNumber()
						+ "\", \"payerUserId\" : \"" + payerUserId + "\", "
						+ "\"payeeUserId\" : \"" + tcp.getPartnerId()
						+ "\", \"transferAmount\" : \""
						+ borrowCollection.getManageFee() + "\"},");
				double repayAcount = BigDecimalUtil.add(
						borrowCollection.getRepaymentAccount(),
						borrowRepayment.getLateInterest(),
						borrowRepayment.getExtensionInterest());
				sbf.append("{\"orderNo\" : \"" + OrderNoUtils.getSerialNumber()
						+ "\", \"payerUserId\" : \"" + payerUserId + "\", "
						+ "\"payeeUserId\" : \"" + tender.getUser().getUserId()
						+ "\", \"transferAmount\" : \"" + repayAcount + "\"},");
			}
			String subOrders = sbf.replace(sbf.length() - 1, sbf.length(), "]")
					.toString();
			tpr = tradePayPoolReverse(tcp.getTradeNo(), payerUserId, subOrders,
					money + "");
			resultCode = tpr.getResultCode();
		}
		if ("EXECUTE_SUCCESS".equals(resultCode) || !isOpenApi) {
			try {
				// 处理借款人资金（提前还款）
				worker.borrowPriorRepayHandleBorrow(borrowRepayment);
				// 处理投资人资金（提前还款）
				worker.borrowPriorRepayHandleTender(borrowRepayment);
				borrowRepayment.setStatus(1);
				borrowRepayment.setRepaymentYesTime(new Date());
				borrowRepayment.setRepaymentYesAccount(BigDecimalUtil.add(
						borrowRepayment.getCapital(),
						borrowRepayment.getInterest()));
				borrowRepaymentDao
						.updateBorrowRepaymentStatusAndWebStatus(borrowRepayment);
				// 提前还款完成状态变成8
				borrow.setStatus(8);
				double total = BigDecimalUtil.add(borrowRepayment.getCapital(),
						borrowRepayment.getInterest()); // 还款本金+利息

				borrow.setRepaymentYesAccount(BigDecimalUtil.add(
						borrow.getRepaymentYesAccount(), total));
				borrow.setRepaymentYesInterest(borrow.getRepaymentYesInterest()
						+ borrowRepayment.getInterest());

				borrowDao.update(borrow);
			} catch (Exception e) {
				throw new BorrowException("提前还款失败" + e.getMessage());
			}
		} else {
			Global.setTransfer("tenderAccount", money);
			Global.setTransfer("borrow", borrow);
			Global.setTransfer("repay", borrowRepayment);
			AbstractExecuter executer = ExecuterHelper
					.doExecuter("borrowRepayFreezeExecuter");
			executer.execute(money, borrow.getUser());
		}

	}

	/**
	 * 还款
	 * 
	 * @param tradeNo
	 * @param payerUserId
	 * @param tendersArray
	 * @return
	 */
	public TradePayPoolReverse tradePayPoolReverse(String tradeNo,
			String payerUserId, String subOrders, String money) {
		TradePayPoolReverse tpr = PayModelHelper.tradePayPoolReverse(tradeNo,
				payerUserId, subOrders, money);
		return tpr;
	}

	/**
	 * 自动投标功能
	 */
	@Override
	public void autoDealTender(BorrowModel model) throws Exception {
		Borrow borrow = model.prototype();
		logger.info("标ID：" + borrow.getId() + "进入自动投标流程， status:"
				+ borrow.getStatus());
		try {
			List<BorrowAuto> autoList = borrowAutoDao.list(model);
			autoTenderHelper(autoList, borrow);
			// 自动投标结束，还原标状态
			borrowDao.updateStatus(borrow.getId(), 1, 19);
		} catch (Exception e) {
			e.printStackTrace();
			// 出异常则将标重新状态置为0
			borrowDao.updateStatus(borrow.getId(), 0, 19);
			logger.error(e.getMessage());
		}
	}

	/**
	 * 自动投标业务处理
	 * 
	 * @param autoList
	 * @param borrow
	 */
	public void autoTenderHelper(List<BorrowAuto> autoList, Borrow borrow) {
		// 最大投标比率
		double autoTenderMaxApr = Global.getDouble("auto_tender_max_apr");
		// 最大投标金额
		double autoTenderMaxAcount = BigDecimalUtil.mul(borrow.getAccount(),
				autoTenderMaxApr);
		// 已经投标额度
		double totalAccountYes = 0;
		// 剩余可投
		double lastAccount = autoTenderMaxAcount;
		for (BorrowAuto borrowAuto : autoList) {
			lastAccount = BigDecimalUtil.sub(autoTenderMaxAcount,
					totalAccountYes);
			// 剩余可投金额等于0，则结束循环
			if (lastAccount == 0) {
				break;
			}
			// 投资者
			User user = userDao.find(borrowAuto.getUser().getUserId());
			// 用户是否被锁定是否是投资者身份校验
			UserCache userCache = user.getUserCache();
			if (userCache.getStatus() == 1) {
				continue;
			}
			Account account = accountDao.getAccountByUserId(user.getUserId());
			double useMoney = account.getUseMoney();
			// 自动投标还款方式校验
			if (StringUtil.isNotBlank(borrowAuto.getStyle())) {
				if (!borrowAuto.getStyle().contains(borrow.getStyle() + "")) {
					continue;
				}
			}
			// 自动投标年率校验
			if (borrowAuto.getAprDown() != 0
					&& borrowAuto.getAprDown() > borrow.getApr()) {
				continue;
			}
			// 自动投标借款期限校验
			if (borrowAuto.getTimelimitUp() != 0) {
				// 判断当前标为天标的情况
				if (borrow.getBorrowTimeType() == 1) {
					if (borrowAuto.getTimelimitDown() >= 1) {
						continue;
					}
				} else { // 当前标是月标
					if (borrowAuto.getTimelimitUp() < borrow.getTimeLimit()
							|| borrowAuto.getTimelimitDown() > borrow
									.getTimeLimit()) {
						continue;
					}
				}
			}
			// 最终投资金额
			double money = 0;
			// 设置的自动投标金额
			double autoMoney = borrowAuto.getMoney();
			// 标最小投标金额
			double lowestAccount = borrow.getLowestAccount();
			// 标最大投标金额
			double mostAccount = borrow.getMostAccount();
			if (borrowAuto.getTenderStyle() == BorrowAuto.BALANCE_FULL) { // 投资方式：余额全投
				if (lastAccount < lowestAccount || lastAccount >= lowestAccount
						&& lastAccount < useMoney) {
					money = lastAccount;
				} else if (lastAccount > useMoney) {
					money = useMoney;
				} else if (mostAccount != 0 && useMoney > mostAccount) {
					money = mostAccount;
				}
			} else if (borrowAuto.getTenderStyle() == BorrowAuto.FIXED_ACCOUNT) { // 投资方式：固定金额
				if (autoMoney > lastAccount || mostAccount != 0
						&& autoMoney > mostAccount || autoMoney > useMoney
						|| autoMoney < lowestAccount) {
					continue;
				} else {
					money = autoMoney;
				}
			} else if (borrowAuto.getTenderStyle() == BorrowAuto.ACCOUNT_INTERVAL) { // 投资方式：金额区间
				if (borrowAuto.getMin() > lastAccount || mostAccount != 0
						&& borrowAuto.getMin() > mostAccount
						|| lastAccount < lowestAccount
						&& borrowAuto.getMax() < lastAccount) {
					continue;
				} else if (lastAccount < mostAccount || mostAccount == 0) {
					if (lastAccount >= borrowAuto.getMax()) {
						money = borrowAuto.getMax();
					} else {
						money = lastAccount;
					}
				} else {
					if (mostAccount >= borrowAuto.getMax()) {
						money = borrowAuto.getMax();
					} else {
						money = mostAccount;
					}
				}
			} else { // 投资方式设置异常
				continue;
			}
			// 可用余额必须大于等于最终投资金额
			if (useMoney < money) {
				continue;
			}
			totalAccountYes = BigDecimalUtil.add(totalAccountYes, money);
			// 冻结投资人本金
			Global.setTransfer("borrow", borrow);
			Global.setTransfer("money", money);
			Global.setTransfer("user", user);
			Global.setTransfer("noticeTime", new Date());
			AbstractExecuter borrowTenderExecuter = ExecuterHelper
					.doExecuter("autoBorrowTenderFreezeExecuter");
			borrowTenderExecuter.execute(money, user, borrow.getUser());
			borrowDao.update(money,
					totalAccountYes / borrow.getAccount() * 100,
					borrow.getStatus(), borrow.getId());
			BorrowTender tender = new BorrowTender();
			tender.setBorrow(borrow);
			tender.setMoney(money);
			tender.setAccount(money);
			tender.setStatus(0);
			tender.setAddTime(new Date());
			tender.setAddIp(Global.getIP());
			tender.setUser(user);
			tender.setTenderType(Byte.parseByte("1"));
			tender = tenderDao.addBorrowTender(tender);
			borrow.setScales(-1);
			BorrowWorker worker = BorrowHelper.getWorker(borrow);
			double validAccount = tender.getAccount();
			InterestCalculator ic = worker.interestCalculator(validAccount);
			tender = worker.tenderSuccess(tender, ic);
			List<BorrowCollection> collectList = worker.createCollectionList(
					tender, ic);
			borrowCollectionDao.save(collectList);

			worker.tenderSuccess(tender, ic);
			// 更新auto时间
			borrowAuto.setUpdateTime(new Date());
			borrowAutoDao.update(borrowAuto);

			// TODO zjj
			// String[][] args = new String[][]{{borrow.getUser().getApiId(),
			// NumberUtil.format2Str(validAccount),
			// NumberUtil.format2Str(1.00)}}; //拼装投标信息的json格式的二维数组
			String[][] args = new String[][] { {
					String.valueOf(borrow.getUser().getUserId()),
					NumberUtil.format2Str(validAccount),
					NumberUtil.format2Str(1.00) } }; // 拼装投标信息的json格式的二维数组
			TPPWay way = TPPFactory.getTPPWay();
			ChinapnrModel model = way.autoTender(user, args, borrow.getId(),
					validAccount);
			if (model == null || !model.success()) {
				logger.info("汇付处理失败，失败原因：" + model.getRespDesc());
				throw new BussinessException("汇付扣除冻结款失败！失败原因:"
						+ model.getRespDesc());
			}

			// 处理汇付冻结接口，如果冻结失败，直接提示投标失败,不进行异步调用处理
			TppPnrPay cpp = new TppPnrPay(); // 封装用户冻结信息
			// TODO zjj
			// cpp.setUsrCustId(user.getApiId());
			// 实际冻结金额=投资金额-红包金额-抵用券金额
			cpp.setOrdamt(tender.getMoney());
			String trxId = freezeChinapnr(cpp);
			// 冻结流水号
			tender.setTrxId(trxId);

		}
		logger.info("自动投标业务逻辑完成.....");

	}

	/**
	 * 用户资金冻结接口 freeze :冻结金额 user_id :用户id usrcustid:用户号 返回一个冻结的订单号,在解冻时必须要的
	 * */
	public synchronized String freezeChinapnr(TppPnrPay cpp) {
		String returnTrxid = "";
		// 汇付2.0特殊规则，订单号为纯数字
		String ordId = OrderNoUtils.getSerialNumber();
		ChinapnrModel retMod = ChinapnrHelper.usrFreezeBg(cpp.getUsrCustId(),
				BaseTPPWay.formatMoney(cpp.getOrdamt()), ordId,
				DateUtil.dateStr7(new Date()));
		if (retMod == null || !retMod.success()) {
			logger.info("汇付处理失败，失败原因：" + retMod.getRespDesc());
			throw new BussinessException("汇付扣除冻结款失败！失败原因:"
					+ retMod.getRespDesc());
		} else {
			returnTrxid = retMod.getTrxId();
			logger.info("返回的returnordid：" + returnTrxid);

		}
		return returnTrxid;
	}

	@Override
	public void autoVerifyFull(Borrow borrow) throws Exception {
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		worker.handleVerifyFull();
		// 标的状态改为3，满标复审中
		worker.skipReview();
	}

	@Override
	public void autoVerifyFullSuccess(BorrowModel model) throws Exception {
		if (model != null && model.getId() > 0) {
			BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
			model = BorrowModel.instance(borrowDao.find(model.getId()));
		}

		BorrowWorker worker = BorrowHelper.getWorker(model);

		Global.setTransfer("borrow", model);
		if (model.getStatus() == Borrow.STATUS_REPAYMENT_START) {
			logger.error("该借款标的状态已经处在放款状态！");
			return;
		}
		worker.secondUnVerifyFreeze();
		worker.handleTenderAfterFullSuccess();
		worker.handleBorrowAfterFullSuccess();
		// 由于秒还标发标时已经冻结利息并产生资金记录，所以下面的情况要排除秒还标
		if (model.getType() != Borrow.TYPE_SECOND) {
			// 由于borrow表算出还款金额和tender表根据投资人算出的还款金额累加有精度有误差，所以更新borrow表中的还款金额与repayment表还款金额保持一致
			double repayAccount = this.borrowRepaymentDao.getSumRepayAccount(
					model.getId(), 0);
			if (model.getRepaymentAccount() != repayAccount) {
				borrowDao.updateRepaymentAccount(model.getId(), repayAccount);
			}
		}

		// 借款手续费
		deductFee(model, worker);
		// 提前付息
		freezeFirstRepay(model);
		// 修改状态
		borrowDao.updateStatus(model.getId(), Borrow.STATUS_REPAYMENT_START,
				Borrow.STATUS_RECHECK_PASS);
		AbstractExecuter succExecuter = ExecuterHelper
				.doExecuter("borrowFullVerifySuccExecuter");
		// 新结构 - ProductBasic
		ProductBasic productBasic = productBasicService.getProductBasicInfo(
				new Long(model.getType()), model.getId());
		productBasic.setStatus(ProductStatusConstant.STATUS_REPAYMENT_START);
		productBasicService.updateProductBasic(productBasic);
		if (model.getType() != Borrow.TYPE_ENTRUST) {
			// 给借款人发送通知
			User user = model.getUser();
			user.getUserId();
			Global.setTransfer("borrow", model);
			Global.setTransfer("user", user);
			succExecuter.execute(0, user);
		}
		// 给投资人发送通知
		List<BorrowTender> list = tenderDao.getTenderByBorrowId(model.getId());
		for (BorrowTender bt : list) {
			User btUser = bt.getUser();

			Global.setTransfer("user", btUser);
			succExecuter.execute(0, btUser);

			// 执行红包发放过程
			userRedPacketService.doTenderRedPacket(btUser, bt);
		}
		// 秒标还款
		worker.repay(model);
	}

	private void deductFee(BorrowModel model, BorrowWorker worker) {
		if (model != null && model.getId() > 0) {
			BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
			model = BorrowModel.instance(borrowDao.find(model.getId()));
		}
		// 扣除交易手续费
		double f = worker.getManageFee();
		DecimalFormat df = new DecimalFormat("#.00");
		double fee = Double.valueOf(df.format(f));
		User user = model.getUser();
		if (fee > 0) {
			Global.setTransfer("deduct", model);
			Global.setTransfer("money", fee);
			AbstractExecuter deductExecuter = ExecuterHelper
					.doExecuter("deductBorrowExecuter");
			deductExecuter.execute(fee, user);
		}

		fee = BigDecimalUtil.round(BigDecimalUtil.mul(model.getAccount(),
				Global.getDouble("transaction_fee")));
		if (fee > 0) {
			Global.setTransfer("deduct", model);
			Global.setTransfer("money", fee);
			AbstractExecuter tranExecuter = ExecuterHelper
					.doExecuter("deductTransactionFeeExecuter");
			tranExecuter.execute(fee, user);
		}
	}

	/**
	 * 满标复审-提前付息
	 */
	private void freezeFirstRepay(BorrowModel model) {
	}

	@Override
	public void autoVerifyFullFail(BorrowModel model) throws Exception {
		BorrowWorker worker = BorrowHelper.getWorker(model);
		Global.setTransfer("borrow", model);
		if (model.getStatus() == Borrow.STATUS_RECHECK_PASSLESS2) {
			logger.error("该借款标满标审核未通过!");
			return;
		}
		if (model != null && model.getId() > 0) {
			BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
			model = BorrowModel.instance(borrowDao.find(model.getId()));
		}
		worker.secondUnVerifyFreeze();
		worker.handleTenderAfterFullFail();
		// 更改标的状态 49
		model.setStatus(Borrow.STATUS_RECHECK_PASSLESS2);
		model.setRepaymentAccount(BigDecimalUtil.sub(
				model.getRepaymentAccount(), model.getRepaymentAccount()));
		model.setRepaymentYesAccount(BigDecimalUtil.sub(
				model.getRepaymentYesAccount(), model.getRepaymentYesAccount()));
		model.setRepaymentYesInterest(BigDecimalUtil.sub(
				model.getRepaymentYesInterest(),
				model.getRepaymentYesInterest()));
		borrowDao.update(model.prototype());
		// 新结构 - ProductBasic
		ProductBasic productBasic = productBasicService.getProductBasicInfo(
				new Long(model.getType()), model.getId());
		productBasic.setStatus(ProductStatusConstant.STATUS_RECHECK_FAIL);
		productBasicService.updateProductBasic(productBasic);
	}

	@Override
	public void autoCancel(Borrow borrow) {
		borrow = borrowDao.find(borrow.getId());
		if (borrow.getStatus() == 59) {
			logger.error("借款标的已经撤回并退款了!");
			return;
		}
		ProductType type = productTypeService.findById(Long.valueOf(borrow.getType()));
//		if (borrow.getStatus() != Borrow.STATUS_TRIAL_PASSED
//			&&!type.getTypeCategory().equals(ProductTypeConstant.PRODUCT_CATEGORY__VIP)
//			||borrow.getStatus() != Borrow.STATUS_REPAYMENT_START
//			&&type.getTypeCategory().equals(ProductTypeConstant.PRODUCT_CATEGORY__VIP)) {
//		
//		}
		List<BorrowTender> tenderList = tenderDao.findByProperty("borrow.id",
				borrow.getId());
		UserRedPacketDao userRedPacketDao = (UserRedPacketDao) BeanUtil
				.getBean("userRedPacketDao");
		Global.setTransfer("borrow", borrow);
		ProductBasic productBasic = productBasicService.getProductBasicInfo(
				new Long(borrow.getType()), borrow.getId());
		Global.setTransfer("flag", productBasic.getProductTypeFlag());
		for (int i = 0; i < tenderList.size(); i++) {
			BorrowTender t = tenderList.get(i);
			// 通过tender得到投资使用的红包金额
			double redMoney = userRedPacketDao.getTotalPacketMoneyByTender(t
					.getId());
			List<UserRedPacket> list = userRedPacketDao.getListByTenderId(t
					.getId());
			if (list != null && list.size() > 0) {
				for (UserRedPacket userRedPacket : list) {
					userRedPacket.setUsed(false);
					userRedPacket.setTender(null);
					userRedPacket.setUsedTime(null);
					userRedPacketDao.update(userRedPacket);
				}
			}
			Object[] collectionArray = borrowCollectionDao.getBorrowCollectionList(t.getId());
			double vipMoney = 0;
			double vipInterest = 0;
			if (collectionArray != null && collectionArray.length> 0) {
				vipMoney =  (Double) collectionArray[0];
				vipInterest = (Double) collectionArray[1];
			}
			QueryParam param = QueryParam.getInstance();
			param.addParam("tender.id", t.getId());
			List<BorrowCollection> collectionList = borrowCollectionDao.findByCriteria(param);
			for(BorrowCollection b:collectionList)
			{
				b.setStatus(2);
			}
			borrowCollectionDao.update(collectionList);
			if(type.getTypeCategory().equals(ProductTypeConstant.PRODUCT_CATEGORY__VIP))
			{
				Global.setTransfer("totalAccount", vipMoney);
				AbstractExecuter cancelExecuter = ExecuterHelper
						.doExecuter("cancelVipTenderExcuter");//返还本金
				cancelExecuter.execute(vipMoney, t.getUser(), null, 0);
				
				Global.setTransfer("totalInterest", vipInterest);
				AbstractExecuter cancelInterestExecuter = ExecuterHelper
						.doExecuter("cancelVipInterestExcuter");//扣除利息
				cancelInterestExecuter.execute(vipInterest, t.getUser(), null, 0);
			}else{
				Global.setTransfer("tenderAccount", t.getAccount());
				AbstractExecuter cancelExecuter = ExecuterHelper
						.doExecuter("cancelTenderUnFeezeExcuter");
				cancelExecuter.execute(vipMoney, t.getUser(), null, 0);
			}
			tenderDao.updateStatus(t.getId(), 2);
			if (redMoney > 0) {
				// 红包使用资金日志
				if (redMoney > 0) {
					Global.setTransfer("user", t.getUser());
					Global.setTransfer("money", redMoney);
					Global.setTransfer("borrow", borrow);
					AbstractExecuter redPacketExecuter = ExecuterHelper
							.doExecuter("tenderRedPacketFailExecuter");
					redPacketExecuter.execute(redMoney, t.getUser(),
							new User(1));
				}
			}
			// 返还用户使用的加息券
			BorrowInterestRate bir = borrowInterestRateDao
					.findByStatusAndTender(2, t);
			if (bir != null) {
				bir.setTender(null);
				bir.setStatus(1);
				borrowInterestRateDao.update(bir);
			}
			double borrowInterestRate = 0;
			if(borrow.getInterestRate()!=null)
			{
				borrowInterestRate = borrow.getInterestRate().getRate();
			}
			if(t.getInterestRateValue()>borrowInterestRate)//说明使用了个人加息券
			{
				Coupon coupon = couponService.findCouponInfoByBorrowTenderId(t.getId());
				coupon.setBorrowTenderId(null);
				coupon.setStatus(0);
				couponService.updateCouponInfo(coupon);
			}
		}
		// 更改标的状态 59
		borrow.setStatus(Borrow.STATUS_MANAGER_CANCEL2);
		borrowDao.update(borrow);
		// 新结构 - ProductBasic
		// ProductBasic productBasic = productBasicService
		// .getInfoForBorrow(borrow);
		productBasic.setStatus(ProductStatusConstant.STATUS_AUTO_CANCEL);
		productBasicService.updateProductBasic(productBasic);
		// 用户撤回借款标消息通知
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		worker.secondUnVerifyFreeze();
		borrow.setRepaymentAccount(0.00);
		borrow.setRepaymentYesAccount(0.00);
		borrow.setRepaymentYesInterest(0.00);
	}

	/*
	 * 代偿成功后处理
	 * 
	 * @param borrowRepayment
	 */
	@Override
	public void autoCompensateSuccess(BorrowRepayment borrowRepayment) {
		Borrow borrow = borrowDao.find(borrowRepayment.getBorrow().getId());
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		try {
			// 担保公司
			User user = borrow.getVouchFirm();
			// 处理投资人资金
			worker.borrowRepayHandleTender(borrowRepayment);
			borrowRepayment.setStatus(1);
			borrowRepayment.setRepaymentYesTime(new Date());
			borrowRepayment.setRepaymentYesAccount(BigDecimalUtil.add(
					borrowRepayment.getCapital(),
					borrowRepayment.getInterest(),
					borrowRepayment.getLateInterest()));
			// 还款类型1：代偿
			borrowRepayment.setType(Constant.REPAYMENT_TYPE_COMPENSATE);
			// 实际还款者
			borrowRepayment.setRealRepayer(user);
			borrowRepaymentDao.updateBorrowRepaymentByStatus(borrowRepayment);
			int status = worker.isLastPeriod(borrowRepayment.getPeriod()) ? 8
					: 7;
			borrow.setStatus(status);
			double total = BigDecimalUtil.add(borrowRepayment.getCapital(),
					borrowRepayment.getInterest()); // 还款表本金+利息
			borrow.setRepaymentYesAccount(BigDecimalUtil.add(
					borrow.getRepaymentYesAccount(), total));
			borrow.setRepaymentYesInterest(BigDecimalUtil.add(
					borrow.getRepaymentYesInterest(),
					borrowRepayment.getInterest()));
			borrowDao.update(borrow);
			// 向担保公司发送担保还款成功通知
			Global.setTransfer("borrow", borrow);
			Global.setTransfer("repay", borrowRepayment);
			Global.setTransfer("user", user);
			AbstractExecuter successExecuter = ExecuterHelper
					.doExecuter("borrowCompensateSuccessExecuter");
			successExecuter.execute(0, user);
		} catch (Exception e) {
			logger.error("代偿失败" + e.getMessage());
			throw new BorrowException("代偿失败" + e.getMessage());
		}

	}

	@Override
	public void updateStatus(long id, int status, int preStatus) {
		borrowDao.updateStatus(id, status, preStatus);
	}

	/**
	 * 还款时，根据本次还款的条件，确定还款的状态
	 * 
	 * @param repay
	 * @return
	 */
	public int getRepayStatus(BorrowRepayment repay) {
		boolean isOpenApi = BaseTPPWay.isOpenApi();
		// 托管环讯
		if (isOpenApi && TPPWay.API_CODE == TPPWay.API_CODE_IPS) {
			if (repay.getRepaymentAccount() > repay.getRepaymentYesAccount()) {
				return BorrowRepayment.STATUS_PART_REPAY;
			} else if (repay.getRepaymentAccount() == repay
					.getRepaymentYesAccount()) {
				return BorrowRepayment.STATUS_YES_REPAY;
			}
		} else {
			return BorrowRepayment.STATUS_YES_REPAY;
		}
		return 0;
	}

	/**
	 * 由于托管分批次还款数据
	 * 
	 * @param repay
	 */
	public BorrowRepayment getResealRepay(BorrowRepayment repay) {
		boolean isOpenApi = BaseTPPWay.isOpenApi();
		// 托管环讯
		if (isOpenApi && TPPWay.API_CODE == TPPWay.API_CODE_IPS) {
			BorrowRepayment pay = borrowRepaymentDao.find(repay.getId());
			repay.setCapital(pay.getCapital());
			repay.setInterest(pay.getInterest());
			repay.setExtensionInterest(pay.getExtensionInterest());
			return repay;
		} else {
			repay.setRepaymentYesAccount(BigDecimalUtil.add(repay.getCapital(),
					repay.getInterest()));
		}
		return repay;
	}

	/**
	 * 环迅还款后回调：如果此次还款的代收里面，有任何一个待收不是待还款状态，则返回false 如果成功，则计算本次应还的金额
	 * 
	 * @param repay
	 *            还款
	 * @return 成功/失败
	 */
	private BorrowRepayment checkRepay(BorrowRepayment repay) {
		boolean isOpenApi = BaseTPPWay.isOpenApi();
		// 托管环讯
		if (isOpenApi && TPPWay.API_CODE == TPPWay.API_CODE_IPS) {
			TppIpsPayDao tppIpsPayDao = (TppIpsPayDao) BeanUtil
					.getBean("tppIpsPayDao");
			List<BorrowCollection> list = tppIpsPayDao.getCollByIpsNo(
					repay.getMerBillNo(), TppIpsPay.STATUS_SUCCESS);
			if (list != null && list.size() > 0) {
				double capitalMoney = 0;
				double interestMoney = 0;
				double lateInterestMoney = 0;
				double extensionInterestMoney = 0;
				for (BorrowCollection coll : list) {
					if (coll == null || coll.getStatus() != 0) {
						throw new BussinessException();
					} else if (coll.getStatus() == 0) {
						capitalMoney = capitalMoney + coll.getCapital();
						interestMoney = interestMoney + coll.getInterest();
						lateInterestMoney = lateInterestMoney
								+ coll.getLateInterest();
						extensionInterestMoney = extensionInterestMoney
								+ coll.getExtensionInterest();
					}
				}
				repay.setRepaymentYesAccount(repay.getRepaymentYesAccount()
						+ capitalMoney + interestMoney);
				repay.setLateInterest(repay.getLateInterest()
						+ lateInterestMoney);
				repay.setExtensionInterest(extensionInterestMoney);
				repay.setCapital(capitalMoney);
				repay.setInterest(interestMoney);
				return repay;
			}
		} else if (!isOpenApi) {
			return repay;
		}
		return repay;
	}
}
