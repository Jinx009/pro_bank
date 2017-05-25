package com.rongdu.p2psys.tpp.chinapnr.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountBankDao;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.ProductsCost;
import com.rongdu.p2psys.account.service.ProductsCostService;
import com.rongdu.p2psys.bond.dao.BondCollectionDao;
import com.rongdu.p2psys.bond.dao.BondTenderDao;
import com.rongdu.p2psys.bond.domain.BondCollection;
import com.rongdu.p2psys.bond.domain.BondTender;
import com.rongdu.p2psys.bond.model.BondModel;
import com.rongdu.p2psys.bond.service.BondTenderService;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.dao.BorrowDao;
import com.rongdu.p2psys.borrow.dao.BorrowRepaymentDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.domain.BorrowRepayment;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.model.BorrowHelper;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.worker.BorrowWorker;
import com.rongdu.p2psys.borrow.service.AutoBorrowService;
import com.rongdu.p2psys.borrow.service.BorrowInterestRateService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.rule.BondConfigRuleCheck;
import com.rongdu.p2psys.core.service.DictService;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.core.util.OrderNoUtils;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitDao;
import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitRecordDao;
import com.rongdu.p2psys.nb.recommend.dao.RecommendRecordDao;
import com.rongdu.p2psys.nb.user.dao.UserInviteDao;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.nb.wechat.util.WechatMessage;
import com.rongdu.p2psys.nb.wechat.util.WechatMessageData;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderScorePhoneLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderScoreRealnameLog;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.ChinapnrTPPWay;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.chinapnr.dao.TppPnrPayDao;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCardCashOut;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCardCashRemove;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrCorpRegister;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrModel;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrUsrUnFreeze;
import com.rongdu.p2psys.tpp.chinapnr.model.ChinapnrWebPayModel;
import com.rongdu.p2psys.tpp.chinapnr.service.ChinapnrService;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinaPnrType;
import com.rongdu.p2psys.tpp.chinapnr.tool.ChinapnrHelper;
import com.rongdu.p2psys.tpp.domain.TppPnrPay;
import com.rongdu.p2psys.user.dao.UserBaseInfoDao;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserBaseInfo;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.service.UserService;

@Service(value = "chinapnrService")
@Transactional
public class ChinapnrServiceImpl implements ChinapnrService {
	@Resource
	private UserDao userDao;
	@Resource
	private UserIdentifyDao userIdentifyDao;
	@Resource
	private UserCacheDao userCacheDao;
	@Resource
	private BorrowDao borrowDao;
	@Resource
	private BorrowTenderDao tenderDao;
	@Resource
	private BorrowCollectionDao collectionDao;
	@Resource
	private BorrowRepaymentDao borrowRepaymentDao;
	@Resource
	private TppPnrPayDao tppPnrPayDao;
	@Resource
	private AccountBankDao accountBankDao;
	@Resource
	private UserRedPacketDao userRedPacketDao;
	@Resource
	private DictService dictService;
	@Resource
	private BorrowTenderService borrowTenderService;
	@Resource
	private BorrowService borrowService;
	@Resource
	private AutoBorrowService autoBorrowService;
	@Resource
	private BorrowInterestRateService borrowInterestRateService;
	@Resource
	private BondCollectionDao bondCollectionDao;
	@Resource
	private BondTenderDao bondTenderDao;
	@Resource
	private UserBaseInfoDao baseInfoDao;
	@Resource
	private BondTenderService bondTenderService;
	@Resource
	private ProductsCostService productsCostService;
	@Resource
	private UserService userService;
	@Resource
	private RecommendProfitRecordDao recommendProfitRecordDao;
	@Resource
	private RecommendProfitDao recommendProfitDao;
	@Resource
	private UserInviteDao theUserInviteDao;
	@Resource
	private RecommendRecordDao recommendRecordDao;
	@Resource
	private AccountDao accountDao;

	private static Logger logger = Logger.getLogger(ChinapnrServiceImpl.class);

	@Override
	public void apiUserRegister(User user) {
		// TODO zjj
		// if (user.getApiStatus() != 1) {
		// user.setApiStatus(1);
		userDao.update(user);
		// 更新用户认证状态及时间
		UserIdentify userIdentify = userIdentifyDao.findByUserId(user
				.getUserId());
		Date now = new Date();
		userIdentify.setMobilePhoneStatus(1);
		userIdentify.setMobilePhoneVerifyTime(now);
		userIdentify.setRealNameStatus(1);
		userIdentify.setRealNameVerifyTime(now);
		userIdentify.setEmailStatus(1);
		userIdentifyDao.update(userIdentify);
		String cardId = user.getCardId();
		int length = cardId.length();
		String sexNum;
		if (length == 15) {
			sexNum = cardId.substring(length - 1);
		} else {
			sexNum = cardId.substring(length - 2, length - 1);
		}
		// 获取性别 1:男,0:女
		int sex = Integer.parseInt(sexNum) % 2;
		UserCache userCache = user.getUserCache();
		userCache.setSex(sex);
		userCacheDao.update(userCache);
		// 获取生日
		Date birthday = StringUtil.getBirthdayByCardid(cardId);
		UserBaseInfo baseInfo = baseInfoDao.findObjByProperty("user.userId",
				user.getUserId());
		if (baseInfo != null) {
			baseInfo.setBirthday(birthday);
			baseInfoDao.merge(baseInfo);
		}
		BaseScoreLog bLog = new TenderScorePhoneLog(user.getUserId());
		bLog.doEvent();
		BaseScoreLog bLog_ = new TenderScoreRealnameLog(user.getUserId());
		bLog_.doEvent();
		// 用户第三方资金托管配置信息初始化
		TPPWay way = TPPFactory.getTPPWay();
		if (way != null) {
			way.addUserTppConfig(user.getUserId());
		}
		// }
	}

	@Override
	public void apiCorpRegister(User user, ChinapnrCorpRegister corpRegister) {
		// TODO zjj
		// if(user.getApiStatus() != 1 &&
		// "Y".equals(corpRegister.getAuditStat())){//开户成功
		if ("Y".equals(corpRegister.getAuditStat())) {// 开户成功
			logger.info("企业开户汇付审核成功start:" + user.getUserId());
			// 更新用户汇付信息
			// user.setApiStatus(1);
			// user.setApiId(corpRegister.getUsrId());
			// user.setApiUsercustId(corpRegister.getUsrCustId());
			userDao.update(user);

			// 更新用户附属信息
			UserCache cache = userCacheDao.findByUserId(user.getUserId());
			cache.setCompanyName(corpRegister.getUsrName());
			userCacheDao.update(cache);

			// 更新用户认证状态及时间
			UserIdentify userIdentify = userIdentifyDao.findByUserId(user
					.getUserId());
			Date now = new Date();
			userIdentify.setMobilePhoneStatus(1);
			userIdentify.setMobilePhoneVerifyTime(now);
			userIdentify.setRealNameStatus(1);
			userIdentify.setRealNameVerifyTime(now);
			userIdentify.setEmailStatus(1);
			userIdentifyDao.update(userIdentify);

			// 更新企业用户银行信息
			QueryParam param = QueryParam.getInstance().addParam("bankNo",
					corpRegister.getCardId());
			AccountBank bank = accountBankDao.findByCriteriaForUnique(param);
			boolean flag = true;
			if (bank != null) {
				bank = new AccountBank();
			} else {
				flag = false;
			}
			bank.setUser(user);
			bank.setBankNo(corpRegister.getCardId());
			Dict dict = dictService.find("account_bank",
					corpRegister.getOpenBankId());
			bank.setBank(dict.getName());
			bank.setAddTime(new Date());
			bank.setAddIp(Global.getIP());
			bank.setStatus(1);
			bank.setPicPath("/data/bank/mini/" + dict.getValue() + ".png");
			if (flag) {
				accountBankDao.save(bank);
			} else {
				accountBankDao.update(bank);
			}

			BaseScoreLog bLog = new TenderScorePhoneLog(user.getUserId());
			bLog.doEvent();
			BaseScoreLog bLog_ = new TenderScoreRealnameLog(user.getUserId());
			bLog_.doEvent();
			// 用户第三方资金托管配置信息初始化
			TPPWay way = TPPFactory.getTPPWay();
			if (way != null) {
				way.addUserTppConfig(user.getUserId());
			}
			logger.info("企业开户汇付审核成功end:" + user.getUserId());
		} else if ("R".equals(corpRegister.getAuditStat())) {// 审核拒绝
																// user.setApiStatus(2);
			userDao.update(user);
			logger.info("企业开户汇付审核被拒绝:" + user.getUserId());
		} else if ("F".equals(corpRegister.getAuditStat())) {// 开户失败
																// user.setApiStatus(-1);
			userDao.update(user);
			logger.info("企业开户汇付审核失败:" + user.getUserId());
		} else if ("Y".equals(corpRegister.getAuditStat())) {
			// 防止汇付多次回调，平台不再做处理
			logger.info("平台已经处理，不再处理:" + user.getUserId());
		} else {
			// user.setApiStatus(4);
			userDao.update(user);
			logger.info("企业开户汇付审核处理中:" + user.getUserId());
		}
	}

	@Override
	public void addTender(BorrowModel bm) {
		BorrowTender borrowTender = tenderDao.getTenderByBillNo(bm
				.getTenderBilNo());
		if (borrowTender == null) {
			Borrow borrow = borrowDao.find(bm.getId());
			User user = userService.getUserById(bm.getUserId());
			bm.setUser(user);
			BorrowTender tender = borrowTenderService.addTender(borrow, bm);
			// 投标订单号
			tender.setTenderBilNo(bm.getTenderBilNo());
			tender.setTenderBilDate(bm.getTenderBilDate());
			tender.setInterestRateValue(bm.getBorrowInterestRateValue()
					+ bm.getVipInterestRateValue());

			// 非秒标或者非满标则冻结
			// if (borrow.getType() != Borrow.TYPE_SECOND ||
			// borrow.getAccountYes() < borrow.getAccount()) {
			// 任务列表
			List<Object> taskList = new ArrayList<Object>();

			// 通过tender得到投资使用的红包金额
			double redMoney = userRedPacketDao
					.getTotalPacketMoneyByTender(tender.getId());
			if (redMoney > 0) {
				User adminUser = userService.getUserById(1);
				TppPnrPay cppRed = fillTppPnrPay(ChinaPnrType.TRANSFER, 0,
						ChinaPnrType.ADDBORROW, redMoney, tender.getUser(),
						tender, adminUser);
				// 从商户转账红包金额给投资人
				taskList.add(cppRed);
			}

			doApiTask(taskList);
			// 处理汇付冻结接口，如果冻结失败，直接提示投标失败,不进行异步调用处理
			TppPnrPay cpp = new TppPnrPay(); // 封装用户冻结信息
			// TODO zjj
			// cpp.setUsrCustId(user.getApiId());
			cpp.setUsrCustId(String.valueOf(user.getUserId()));
			// 实际冻结金额
			cpp.setOrdamt(tender.getMoney());
			String trxId = freezeChinapnr(cpp);
			// 冻结流水号
			tender.setTrxId(trxId);
			// }

			tenderDao.update(tender);

			// 更新加息劵表中的数据
			List<BorrowInterestRate> list = borrowInterestRateService
					.findByStatusAndUser(1, tender.getUser());
			if (list != null) {
				for (BorrowInterestRate borrowInterestRate : list) {
					if (bm.getBorrowInterestRateValue() == borrowInterestRate
							.getValue()) {
						borrowInterestRate.setMoney(bm.getMoney());
						borrowInterestRate.setStatus(2);
						borrowInterestRate.setTender(tender);
						borrowInterestRateService.update(borrowInterestRate);
					}
				}
			}
		}
	}

	@Override
	public boolean doApiTask(List<Object> taskList) {
		boolean isSuccess = true;
		for (Object obj : taskList) {
			TppPnrPay cp = (TppPnrPay) obj;
			if (isSuccess) {
				try {
					autoChinapnrPay(cp);
					cp.setStatus("1");
					cp.setErrorMsg("success");
				} catch (Exception e) {
					cp.setErrorMsg(e.getMessage());
					cp.setStatus("2");
					isSuccess = false;
					logger.info(e.getMessage());
				}
			}
			try {
				tppPnrPayDao.save(cp);
			} catch (Exception e) {
				logger.error(e + "保存交易信息出错！！！");
			}
		}
		return isSuccess;
	}

	@Override
	public void fullSuccess(BorrowModel model) {
		Borrow borrow = borrowDao.find(model.getId());
		model = BorrowModel.instance(borrow);
		if (model.getStatus() == 6) {
			logger.error("该借款标的状态已经处在放款状态！");
			return;
		}
		model.setStatus(3);
		BorrowWorker worker = BorrowHelper.getWorker(model);
		Global.setTransfer("borrow", model);
		worker.secondUnVerifyFreeze();
		worker.handleTenderAfterFullSuccess();
		worker.handleBorrowAfterFullSuccess();
		// 由于borrow表算出还款金额和tender表根据投资人算出的还款金额累加有精度有误差，所以更新borrow表中的还款金额与repayment表还款金额保持一致
		double repayAccount = borrowRepaymentDao.getSumRepayAccount(
				model.getId(), 0);
		if (model.getRepaymentAccount() != repayAccount) {
			borrowDao.updateRepaymentAccount(model.getId(), repayAccount);
		}

		// 投资列表
		List<BorrowTender> list = tenderDao.getTenderByBorrowId(model.getId());
		BorrowRepayment repayment = borrowRepaymentDao.find(model.getId(),
				model.getPeriod());
		// 借款相关费用扣除（借款手续费、风险备用金）
		deductFee(model, worker, list);

		// 修改状态
		borrowDao.updateStatus(model.getId(), 6, 3);
		AbstractExecuter succExecuter = ExecuterHelper
				.doExecuter("borrowFullVerifySuccExecuter");
		if (model.getType() != Borrow.TYPE_ENTRUST) {
			// 给借款人发送通知
			User user = model.getUser();
			user.getUserId();
			Global.setTransfer("borrow", model);
			Global.setTransfer("user", user);
			succExecuter.execute(0, user);
		}
		// 给投资人发送通知
		for (int i = 0; i < list.size(); i++) {
			BorrowTender tender = list.get(i);
			User btUser = tender.getUser();
			btUser.getUserId();
			Global.setTransfer("user", btUser);
			succExecuter.execute(0, btUser);
		}
		String remark = "";
		if (1 == borrow.getStyle()) {
			remark = "等额本息（按月分期还款）.";
		} else if (2 == borrow.getStyle()) {
			remark = "到期一次性还本付息.";
		} else if (3 == borrow.getStyle()) {
			remark = "按月付息到期还本息.";
		} else {
			remark = "按照设定日期还息，到期还本.";
		}
		remark += "起息日期:" + DateUtil.dateStr(new Date()) + ",到期日期:"
				+ DateUtil.dateStr(repayment.getRepaymentTime());
		for (int i = 0; i < list.size(); i++) {
			BorrowTender tender = list.get(i);
			List<User> userList = userDao.getByGroupId(tender.getUser()
					.getBindId());
			if (userList.size() > 1) {
				for (int j = 0; j < userList.size(); j++) {
					if (null != userList.get(j).getWechatOpenId()
							&& !"".equals(userList.get(j).getWechatOpenId())
							&& WechatMessageData.A_APP_ID.equals(userList
									.get(j).getWechatId())) {
						String userName = tender.getUser().getUserName()
								.substring(0, 3)
								+ "****"
								+ tender.getUser()
										.getUserName()
										.substring(
												7,
												tender.getUser().getUserName()
														.length());
						NumberFormat nf = new DecimalFormat(",###.##");
						double collection = collectionDao
								.getRemainderInterest(tender.getId());
						WechatMessage wechatMessage = new WechatMessage();
						wechatMessage.setAppId(WechatMessageData.A_APP_ID);
						wechatMessage
								.setAppSecret(WechatMessageData.A_APP_SECRET);
						wechatMessage.setType(WechatMessageData.Has_Rate);
						wechatMessage.setFirstData("尊敬的用户，您" + userName
								+ "的账号已投资成功，即日起息");
						wechatMessage.setProductName(model.getName());
						wechatMessage.setUrl(Global.getValue("weburl")
								+ "/nb/wechat/account/assets.html");
						wechatMessage.setProductDate(model.getTimeLimitStr());
						wechatMessage.setOpenId(userList.get(j)
								.getWechatOpenId());
						wechatMessage
								.setProductProfit(nf.format(model.getApr())
										+ "%");
						// 加息券
						if (null != borrow.getInterestRate()
								&& borrow.getInterestRate().getStatus() == ConstantUtil.FLAG_TRUE) {
							wechatMessage.setProductProfit(wechatMessage
									.getProductProfit()
									+ " + "
									+ nf.format(borrow.getInterestRate()
											.getRate()) + "%");
						}
						wechatMessage.setBuyMoney(tender.getMoney());
						wechatMessage.setMoney(collection);
						wechatMessage
								.setRemark("投资当日计息。\\n如有疑问请拨打客服热线400-6366-800！");
						JobQueue.getWechatMessageInstance()
								.offer(wechatMessage);
					}
				}
			}
		}
		// 秒标还款
		worker.repay(model);

		if (model.getType() == Borrow.TYPE_SECOND) {
			borrow.setStatus(8);
		} else {
			borrow.setStatus(6);
		}

		borrowDao.update(borrow);
		logger.info("跟新标的状态 id:" + model.getId() + "   " + model.getStatus());
	}

	@Override
	public void fullFail(BorrowModel model) {
		// 平台测满标复审不通过处理
		try {
			autoBorrowService.autoVerifyFullFail(model);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("满标审核未通过处理失败！");
			return;
		}
	}

	@Override
	public void cancelBorrow(Borrow borrow) {
		// 平台测撤标处理
		try {
			autoBorrowService.autoCancel(borrow);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("撤标处理失败！");
			return;
		}

		// 任务列表
		List<Object> taskList = new ArrayList<Object>();

		// 投资信息列表
		List<BorrowTender> list = tenderDao.getTenderByBorrowId(borrow.getId());

		// 如果没有投资记录，就不做资金解冻处理
		if (list == null || list.size() == 0) {
			return;
		}

		for (BorrowTender tender : list) {
			TppPnrPay cppCancel = fillTppPnrPay(ChinaPnrType.UsrUnFreeze, 0,
					ChinaPnrType.AUTOVERIFYFULLFAIL, tender.getMoney(),
					tender.getUser(), tender, tender.getUser());
			taskList.add(cppCancel);
		}
		// 第三方解冻投资人资金
		doApiTask(taskList);
	}

	@Override
	public void repay(BorrowRepayment borrowRepayment) {

		List<BorrowCollection> list = collectionDao.list(borrowRepayment
				.getBorrow().getId(), borrowRepayment.getPeriod());

		List<BondCollection> listBondColl = bondCollectionDao
				.getBondCollectionList(borrowRepayment.getId());

		// 平台侧还款处理
		autoBorrowService.autoBorrowRepay(borrowRepayment);

		// 任务列表
		List<Object> taskList = new ArrayList<Object>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				BorrowCollection collection = list.get(i);
				// 利息管理费
				double manageFee = BigDecimalUtil.mul(
						BigDecimalUtil.sub(collection.getInterest(),
								collection.getBondInterest()),
						Global.getDouble("borrow_fee"));
				manageFee = BigDecimalUtil.round(manageFee);
				TppPnrPay cppRepay = null;
				if (borrowRepayment.getBorrow().getType() == Borrow.TYPE_SECOND) {
					// if (i != list.size()-1) { // 最后一笔因为没冻结所以不用解冻
					// 解冻投资金额
					TppPnrPay cppCancel = fillTppPnrPay(
							ChinaPnrType.UsrUnFreeze, 0,
							ChinaPnrType.AUTOVERIFYFULLFAIL,
							BigDecimalUtil.sub(collection.getCapital(),
									collection.getBondCapital()),
							collection.getUser(), collection.getTender(),
							collection.getUser());
					taskList.add(cppCancel);
					// }
					// cppRepay = fillTppPnrPay(ChinaPnrType.REPAYMENT,
					// manageFee, ChinaPnrType.AUTOREPAY,
					// BigDecimalUtil.sub(collection.getInterest(),
					// collection.getBondInterest()), collection.getUser(),
					// collection.getTender(),
					// collection.getBorrow().getUser());
				} else {
					double money = BigDecimalUtil.add(collection.getCapital(),
							-collection.getBondCapital(),
							collection.getInterest(),
							-collection.getBondInterest(),
							collection.getLateInterest());
					if (money > 0 || manageFee > 0) {
						cppRepay = fillTppPnrPay(ChinaPnrType.REPAYMENT,
								manageFee, ChinaPnrType.AUTOREPAY, money,
								collection.getUser(), collection.getTender(),
								collection.getBorrow().getUser());
						taskList.add(cppRepay);
					}
				}
				double interestRate = collection.getInterestRate();
				if (interestRate > 0) {
					collection.setInterestRateYes(interestRate);
					ChinapnrWebPayModel payModel = new ChinapnrWebPayModel();
					payModel.setMoney(interestRate);
					payModel.setPayUser(collection.getTender().getUser());
					doWebPayMoney(payModel, taskList);
				} else {
					collection.setInterestRateYes(0);
				}
			}

			// 处理债权待收
			if (listBondColl != null && listBondColl.size() > 0) {
				for (int i = 0; i < listBondColl.size(); i++) {
					BondCollection bondCollection = listBondColl.get(i);
					// 利息管理费
					double manageFee = BigDecimalUtil.mul(BigDecimalUtil.sub(
							bondCollection.getInterest(),
							bondCollection.getBondInterest()), Global
							.getDouble("borrow_fee"));
					TppPnrPay cppRepay = null;
					double money = BigDecimalUtil.add(
							bondCollection.getCapital(),
							-bondCollection.getBondCapital(),
							bondCollection.getInterest(),
							-bondCollection.getBondInterest(),
							bondCollection.getLateInterest());
					cppRepay = fillTppPnrPayBond(ChinaPnrType.REPAYMENT,
							manageFee, ChinaPnrType.AUTOREPAY, money,
							bondCollection.getUser(),
							bondTenderDao.getBondTenderById(bondCollection
									.getBondTenderId()), bondCollection
									.getBorrow().getUser());
					taskList.add(cppRepay);
				}
			}

			doApiTask(taskList);
			borrowRepaymentDao.update(borrowRepayment);
		}

	}

	@Override
	public void vipRepay() {

		List<BorrowCollection> list = collectionDao.vipRepayList();

		logger.info("开始对VIP投标进行还款....");

		// int status = worker.isLastPeriod(borrowRepayment.getPeriod()) ? 8:7;
		// borrow.setStatus(status);

		ProductBasicService productBasicService = (ProductBasicService) BeanUtil
				.getBean("productBasicService");

		WechatCacheService wechatCacheService = (WechatCacheService) BeanUtil
				.getBean("wechatCacheService");

		// 平台侧还款处理
		// autoBorrowService.autoBorrowRepay(borrowRepayment);

		if (list != null && list.size() > 0) {
			for (BorrowCollection borrowCollection : list) {
				{
					Borrow borrow = borrowCollection.getBorrow();
					Global.setTransfer("borrow", borrow);
					ProductBasic productBasic = productBasicService
							.getProductBasicInfo(new Long(borrow.getType()),
									borrow.getId());
					Global.setTransfer("flag",
							productBasic.getProductTypeFlag());
					QueryParam param = new QueryParam();
					param.addParam("tender.id", borrowCollection.getTender()
							.getId());
					int repaymentCount = collectionDao.countByCriteria(param);

					BorrowTender tender = tenderDao.find(borrowCollection
							.getTender().getId());
					double capital = borrowCollection.getCapital();
					// 归还投资人本金
					if (capital > 0) {
						Global.setTransfer("money", capital);
						AbstractExecuter repayTenderExecuter = ExecuterHelper
								.doExecuter("borrowRepayTenderCapitalExecuter");
						repayTenderExecuter.execute(capital, tender.getUser(),
								borrow.getUser());

					}
					// 归还投资人利息
					double cInterest = borrowCollection.getInterest();
					if (cInterest > 0) {
						double borrow_fee = Global.getDouble("borrow_fee");
						double borrowFee = BigDecimalUtil.mul(cInterest,
								borrow_fee);
						borrowFee = BigDecimalUtil.round(borrowFee);
						// 收回利息
						Global.setTransfer("money", cInterest);
						Global.setTransfer("borrowFee", borrowFee);
						AbstractExecuter repayTenderInterestExecuter = ExecuterHelper
								.doExecuter("borrowRepayTenderInterestExecuter");
						repayTenderInterestExecuter.execute(cInterest,
								tender.getUser(), borrow.getUser());
						// 扣除投资人利息管理费
						if (borrowFee > 0) {
							Global.setTransfer("money", borrowFee);
							AbstractExecuter manageFeeExecuter = ExecuterHelper
									.doExecuter("deductManageFeeExecuter");
							manageFeeExecuter.execute(borrowFee, tender
									.getUser(), new User(Constant.ADMIN_ID));
							borrowCollection.setManageFee(borrowCollection
									.getManageFee());
						}
					}

					// 归还投资人使用加息劵产生的利息2015-01-03
					double interestRate = borrowCollection.getInterestRate();
					if (interestRate > 0) {
						Global.setTransfer("money", interestRate);
						AbstractExecuter repayTenderExecuter = ExecuterHelper
								.doExecuter("borrowRepayTenderInterestRateExecuter");
						repayTenderExecuter.execute(interestRate,
								tender.getUser(), borrow.getUser());
					}

					// 归还投资人的浮动收益率
					double floatIncome = borrowCollection.getFloatIncome();
					if (floatIncome > 0) {
						Global.setTransfer("money", floatIncome);
						AbstractExecuter borrowRepayTenderFloatIncomeExecuter = ExecuterHelper
								.doExecuter("borrowRepayTenderFloatIncomeExecuter");
						borrowRepayTenderFloatIncomeExecuter
								.execute(floatIncome, tender.getUser(),
										borrow.getUser());
					}

					if ((borrow.getType() == 119 || borrow.getType() == 112)
							&& borrow.getBorrowTimeType() == 1) {
						tenderDao.updateRepayTender(
								borrowCollection.getRepaymentAccount()
										- borrowCollection
												.getRepaymentYesAccount(),
								cInterest, tender.getId());
					} else {
						tenderDao.updateRepayTender(
								borrowCollection.getRepaymentAccount(),
								cInterest, tender.getId());
					}
					// 更新collection
					borrowCollection.setStatus(1);
					borrowCollection.setRepaymentYesTime(new Date());
					borrowCollection.setRepaymentYesAccount(borrowCollection
							.getRepaymentAccount());
					borrowCollection.setInterestRateYes(borrowCollection
							.getInterestRate());
					if (interestRate > 0) {
						borrowCollection.setInterestRateYes(interestRate);

					} else {
						borrowCollection.setInterestRateYes(0);
					}
					collectionDao.update(borrowCollection);

					double total = BigDecimalUtil.add(
							borrowCollection.getCapital(),
							borrowCollection.getInterest());
					borrow.setAccountYes(BigDecimalUtil.add(
							borrow.getAccountYes(),
							borrowCollection.getCapital()));
					borrow.setRepaymentYesAccount(BigDecimalUtil.add(
							borrow.getRepaymentYesAccount(), total));
					borrow.setRepaymentYesInterest(borrow
							.getRepaymentYesInterest()
							+ borrowCollection.getInterest());
					if (borrow.getAccount() == borrow.getAccountYes())// 全部还清
					{
						borrow.setStatus(8);
					} else if (borrow.getStatus() != 7) {
						borrow.setStatus(7);
					}
					borrowDao.update(borrow);

					// // 利息管理费
					// double manageFee =
					// BigDecimalUtil.mul(borrowCollection.getInterest(),
					// Global.getDouble("borrow_fee"));
					// manageFee = BigDecimalUtil.round(manageFee);
					//
					// double money =
					// BigDecimalUtil.add(borrowCollection.getCapital(),
					// borrowCollection.getInterest(),
					// borrowCollection.getFloatIncome(),
					// borrowCollection.getInterestRate());
					//
					// 向投资人发送还款成功通知，不做任何资金处理
					Global.setTransfer("collection", borrowCollection);
					Global.setTransfer("user", tender.getUser());
					if (borrow.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT)// 利随本清
					{
						BaseMsg msg = new BaseMsg(NoticeConstant.REPAY_SUCC);
						msg.doEvent();
					} else if (borrow.getStyle() == Borrow.STYLE_MONTHLY_INTEREST)// 先息后本
					{
						if (repaymentCount - 1 == borrowCollection.getPeriod()) {// 利息本金都有
							BaseMsg msg = new BaseMsg(
									NoticeConstant.REPAY_SUCC_FILC_LAST);
							msg.doEvent();
						} else {// 每月利息到期还本
							BaseMsg msg = new BaseMsg(
									NoticeConstant.REPAY_SUCC_FILC);
							msg.doEvent();
						}
					} else if (borrow.getStyle() == Borrow.STYLE_INSTALLMENT_REPAYMENT)// 等额本息
					{// 利息本金都有
						BaseMsg msg = new BaseMsg(
								NoticeConstant.REPAY_SUCC_ACPI);
						msg.doEvent();
					}
					// AbstractExecuter repaySuccessExecuter = ExecuterHelper
					// .doExecuter("tenderRepaySuccessExecuter");
					// repaySuccessExecuter.execute(0, tender.getUser(),
					// borrowCollection.getUser());
					// List<User> userList =
					// theUserDao.getByGroupId(tender.getUser().getBindId());
					// if (userList.size() > 1)
					// {
					// for (int i = 0; i < userList.size(); i++)
					// {
					// if (null != userList.get(i).getWechatOpenId()
					// &&!"".equals(userList.get(i).getWechatOpenId())
					// &&WechatMessageData.A_APP_ID.equals(userList.get(i).getWechatId()))
					// {
					//
					// WechatMessage wechatMessage = new WechatMessage();
					//
					// wechatMessage.setAppId(WechatMessageData.A_APP_ID);
					// wechatMessage.setAppSecret(WechatMessageData.A_APP_SECRET);
					// wechatMessage.setType(WechatMessageData.Due_Income);
					// wechatMessage.setFirstData("产品"+this.data.getName()+"到期啦!");
					// wechatMessage.setProductName(this.data.getName());
					// wechatMessage.setUrl("http://www.800bank.com.cn/nb/wechat/account/main.html");
					// wechatMessage.setDateInfo(borrowCollection.getRepaymentTime());
					// wechatMessage.setOpenId(userList.get(i).getWechatOpenId());
					// wechatMessage.setProductProfit(borrow.getApr()+"%");
					// wechatMessage.setMoney(tender.getMoney());
					// wechatMessage.setProfit(borrowCollection.getInterest());
					//
					// //
					// wechatMessage.setProductRemark("投资金额:"+tender.getMoney()+"元");
					//
					// try {
					// wechatCacheService.sendWechatMessage(wechatMessage);
					// } catch (Exception e) {
					// // TODO Auto-generated catch block
					// logger.info("推送信息异常：" + e.getMessage());
					// }
					// }
					// }
					// }
				}

			}

		}

	}

	/**
	 * 汇付绑卡操作
	 * 
	 * @param cardCashOut
	 * @param ab
	 */
	public void addAccountBank(ChinapnrCardCashOut cardcash) {

		int addFlag = 0;
		String bankNo = cardcash.getOpenAcctId();

		QueryParam param = QueryParam.getInstance().addParam("bankNo", bankNo);
		AccountBank bank = accountBankDao.findByCriteriaForUnique(param);
		if (bank == null) {
			bank = new AccountBank();
			addFlag = 1;
		} else if (bank.getStatus() == 1) {
			// throw new AccountException("该银行卡已绑定，请勿重复绑定", 1);
			addFlag = 2;
		}
		Dict dict = dictService.find("account_bank", cardcash.getOpenBankId());
		User user = userDao.find(Long.valueOf(cardcash.getMerPriv()));
		bank.setUser(user);
		bank.setBankNo(bankNo);
		bank.setBank(dict.getName());
		bank.setAddIp(Global.getIP());
		bank.setAddTime(new Date());
		bank.setStatus(1);
		bank.setPicPath("/data/bank/mini/" + dict.getValue() + ".png");
		if (addFlag == 1) {
			accountBankDao.save(bank);
		} else if (addFlag == 2) {
			accountBankDao.update(bank);
		}

	}

	@Override
	public Map<String, Object> removeAccountBank(String usrCustId, String bankNo) {
		ChinapnrCardCashRemove ccr = new ChinapnrCardCashRemove(usrCustId,
				bankNo);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "删除银行卡成功！");
		map.put("result", true);
		try {
			ccr.submit();
		} catch (Exception e) {
			e.getStackTrace();
			logger.error("删除银行卡失败！");
			map.put("message", "删除银行卡失败！");
			map.put("result", false);
			return map;
		}
		if (!ChinapnrTPPWay.RESP_CODE_SUCC.equals(ccr.getRespCode())) {
			logger.info("删除银行卡失败！");
			map.put("message", ccr.getRespDesc());
			map.put("result", false);
			return map;
		}
		return map;
	}

	@Override
	public double getRealRepayMoney(BorrowRepayment repayment) {

		// 计算逾期利息
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date nowTime = null;
		Date repaymentTime = null;
		try {
			nowTime = format.parse(DateUtil.dateStr2(new Date()));
			repaymentTime = format.parse(DateUtil.dateStr2(repayment
					.getRepaymentTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long day = nowTime.getTime() - repaymentTime.getTime();
		int days = (int) (day / (24 * 60 * 60 * 1000));
		String overdueFee = Global.getValue("overdue_fee");
		double overdue = Double.parseDouble(overdueFee);
		int FreeOverdueDay = Integer.parseInt(Global
				.getValue("free_overdue_day"));// ？天内免逾期罚息

		double capitalLate = 0; // 本金逾期金额
		double interestLate = 0; // 利息逾期金额
		double lateInterest = 0; // 逾期利息
		if (days <= FreeOverdueDay && days > 0) {
			capitalLate = repayment.getCapital();
			interestLate = repayment.getInterest();
			lateInterest = BigDecimalUtil.mul(
					BigDecimalUtil.mul(capitalLate + interestLate, days),
					overdue); // 计算出逾期利息
		} else if (days > FreeOverdueDay) {
			long borrowId = repayment.getBorrow().getId();
			capitalLate = borrowRepaymentDao.getRemainderCapital(borrowId);
			interestLate = borrowRepaymentDao.getRemainderInterest(borrowId);
			lateInterest = BigDecimalUtil.mul(
					BigDecimalUtil.mul(capitalLate + interestLate, days),
					overdue); // 计算出逾期利息
		}

		return BigDecimalUtil.add(repayment.getCapital(),
				repayment.getInterest(), lateInterest);

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

	/**
	 * 汇付2.0资金解冻接口 trxId:资金冻结时候的交易流水号 在汇付2.0解冻资金的依据是冻结时产生的流水号， 不根据用户所传入的资金来控制
	 * */
	public synchronized void unFreezeChinapnr(String usrId, String trxId) {
		String ordId = OrderNoUtils.getSerialNumber();
		String date = DateUtil.dateStr7(new Date());

		ChinapnrUsrUnFreeze usrUnFreeze = new ChinapnrUsrUnFreeze(usrId, date,
				ordId, trxId);
		try {
			usrUnFreeze.submit();
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}

		if (usrUnFreeze == null || !usrUnFreeze.success()) {
			throw new BussinessException("解除冻结款失败！失败原因:"
					+ usrUnFreeze.getRespDesc());
		}
	}

	/**
	 * 自动扣款(满标放款接口)
	 * outCustId:出账账户,inUustId:入账账户,transAmt:交易金额,fee:费率,userid:用户id
	 * subordId:投标时所产生的订单编号，subOrddate:投标时所产生的订单时间
	 * args：在满标复审时要上交给平台的管理费，格式是二维数组方式 isdefault:是否默认0
	 * */
	public synchronized void loansChianpnr(TppPnrPay cpp) {
		String isdefault = "N";
		String ordId = OrderNoUtils.getSerialNumber();
		ChinapnrModel china = ChinapnrHelper.loans(cpp.getUsrCustId(),
				cpp.getPayUserCustId(),
				BaseTPPWay.formatMoney(cpp.getOrdamt()), cpp.getFee(),
				cpp.getRiskReserveFee(), cpp.getOrdId(), cpp.getOrddate(),
				cpp.getSubordId(), cpp.getSuborddate(), isdefault,
				cpp.getDivDetails(), "Y", ordId, cpp.getTrxId());
		if (china == null || !china.success()) {
			throw new BussinessException("投标放款失败，失败原因:" + china.getRespDesc());
		}
	}

	/**
	 * 汇付2.0自动扣款(还款) outCustId:出账账户,inCustId:入账账户,transAmt:交易金额,fee:费率
	 * userid:用户id,args:分账给金账户的分账明细
	 * */
	public synchronized void repaymenChinapnr(TppPnrPay cpp) {
		ChinapnrModel china = ChinapnrHelper.repayment(cpp.getUsrCustId(),
				cpp.getPayUserCustId(),
				BaseTPPWay.formatMoney(cpp.getOrdamt()), cpp.getFee(),
				cpp.getOrdId(), cpp.getOrddate(), cpp.getSubordId(),
				cpp.getSuborddate(), cpp.getDivDetails());
		if (china == null || !china.success()) {
			throw new BussinessException("还款失败，失败原因:" + china.getRespDesc());
		}
	}

	/**
	 * 商户划账接口：参数说明 inCustId:收款方的用户号,
	 * inAcctId:子账户（目前处于待定状态，由于这个子账户在我们平台上还无法从汇付方获取到,目前取消此参数） transAmt:交易金额,
	 * outAcctId:子账户（在系统划账时，此账户就为系统的子账户，应该可以默认了）
	 **/
	public synchronized void transferChinapnr(TppPnrPay cpm) {
		String ordId = OrderNoUtils.getSerialNumber();
		cpm.setOrdId(ordId);
		ChinapnrModel ch = ChinapnrHelper.transfer(
				BaseTPPWay.formatMoney(cpm.getOrdamt()),
				cpm.getPayUserCustId(), ordId, cpm.getPayUserName(),
				cpm.getUsrCustId());
		if (ch == null || !ch.success()) {
			throw new BussinessException("给用户划账失败，失败原因！" + ch.getRespDesc());
		}
	}

	/**
	 * 汇付接口调用统一处理。
	 */
	public String autoChinapnrPay(TppPnrPay cpm) {
		// 1=处理成功 2 = 处理失败。
		if ("UsrFreezeBg".equals(cpm.getCmdid())) { // 冻结
			return freezeChinapnr(cpm);
		}
		if ("UsrUnFreeze".equals(cpm.getCmdid())) { // 解冻
			unFreezeChinapnr(cpm.getUsrCustId(), cpm.getTrxId());
		}
		if ("LOANS".equals(cpm.getCmdid())) { // 放款
			loansChianpnr(cpm);
		}
		if ("REPAYMENT".equals(cpm.getCmdid())) { // 还款
			repaymenChinapnr(cpm);
		}
		if ("TRANSFER".equals(cpm.getCmdid())) {
			transferChinapnr(cpm);
		}
		// if("CREDITASSIGN".equals(cpm.getCmdid())){
		// creditAssign(cpm);
		// }
		return "";
	}

	/**
	 * 扣除借款人借款相关费用（借款管理费、风险备用金）
	 * 
	 * @param model
	 * @param worker
	 * @param taskList
	 * @param tenderList
	 */
	public void deductFee(BorrowModel model, BorrowWorker worker,
			List<BorrowTender> tenderList) {
		// 借款管理费
		double manageFeeTotal = deductManageFee(model, worker);
		// 风险备用金
		double riskReserveFeeTotal = deductRiskReserveFee(model);

		// 保存收费记录
		if (model != null && model.getId() > 0) {
			BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
			Borrow borrow = BorrowModel.instance(borrowDao.find(model.getId()));
			ProductsCost cost = new ProductsCost(borrow.getName(),
					borrow.getBidNo(), borrow, manageFeeTotal,
					riskReserveFeeTotal);
			productsCostService.save(cost);
		}
	}

	/**
	 * 借款管理费扣除
	 * 
	 * @param model
	 * @param worker
	 */
	public double deductManageFee(BorrowModel model, BorrowWorker worker) {
		if (model != null && model.getId() > 0) {
			BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
			model = BorrowModel.instance(borrowDao.find(model.getId()));
		}
		// 扣除借款管理费
		double f = 0;
		if (model.getBorrowManageRate() >= 0) {
			if (model.getBorrowTimeType() == 0) {// 月标
				f = BigDecimalUtil.mul(model.getAccount(),
						model.getBorrowManageRate() * model.getTimeLimit()
								/ (100 * 12));
			} else {// 天标
				f = BigDecimalUtil.mul(model.getAccount(),
						model.getBorrowManageRate() * model.getTimeLimit()
								/ (100 * 365));
			}
		} else {
			f = worker.getManageFee();
		}
		DecimalFormat df = new DecimalFormat("#.00");
		double fee = Double.valueOf(df.format(f));

		/*
		 * User user = model.getUser(); if (fee > 0) {
		 * Global.setTransfer("deduct", model); Global.setTransfer("money",
		 * fee); AbstractExecuter deductExecuter =
		 * ExecuterHelper.doExecuter("deductBorrowExecuter");
		 * deductExecuter.execute(fee, user); }
		 */

		// fee = BigDecimalUtil.round(BigDecimalUtil.mul(model.getAccount(),
		// Global.getDouble("transaction_fee")));
		// if (fee > 0) {
		// Global.setTransfer("deduct", model);
		// Global.setTransfer("money", fee);
		// AbstractExecuter tranExecuter =
		// ExecuterHelper.doExecuter("deductTransactionFeeExecuter");
		// tranExecuter.execute(fee, user);
		// }

		return fee;
	}

	/**
	 * 风险备用金扣除
	 * 
	 * @param model
	 */
	public double deductRiskReserveFee(BorrowModel model) {
		if (model != null && model.getId() > 0) {
			BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
			model = BorrowModel.instance(borrowDao.find(model.getId()));
		}
		double f = 0;
		if (model.getBorrowTimeType() == 0) {// 月标
			f = BigDecimalUtil.div(
					BigDecimalUtil.mul(model.getAccount(),
							model.getRiskReserveRate() / 100,
							model.getTimeLimit()), 12);
		} else {// 天标
			f = BigDecimalUtil.div(
					BigDecimalUtil.mul(model.getAccount(),
							model.getRiskReserveRate() / 100,
							model.getTimeLimit()), 365);
		}
		DecimalFormat df = new DecimalFormat("#.00");
		double fee = Double.valueOf(df.format(f));
		/*
		 * User user = model.getUser(); if (fee > 0) {
		 * Global.setTransfer("deduct", model); Global.setTransfer("money",
		 * fee); AbstractExecuter deductExecuter =
		 * ExecuterHelper.doExecuter("deductRiskReserveFeeExecuter");
		 * deductExecuter.execute(fee, user); }
		 */
		return fee;
	}

	/**
	 * 填充汇付对象，类似放款方法，资金由投资人到借款人
	 * 
	 * @param cmdid
	 *            汇付交易对象
	 * @param fee
	 *            管理费
	 * @param operateType
	 *            系统交易对象
	 * @param ordamt
	 *            交易金额
	 * @param userIn
	 *            收入方
	 * @param tender
	 *            投标信息
	 * @param userOut
	 *            出资方
	 * @return
	 */
	public TppPnrPay fillTppPnrPay(String cmdid, double fee,
			String operateType, double ordamt, User userIn,
			BorrowTender tender, User userOut) {

		TppPnrPay tppPnrPay = new TppPnrPay();
		String date = DateUtil.dateStr7(new Date());
		tppPnrPay.setAddtime(new Date());
		tppPnrPay.setBorrowId(String.valueOf(tender.getBorrow().getId()));
		tppPnrPay.setCmdid(cmdid);
		// tppPnrPay.setErrorMsg(errorMsg);
		tppPnrPay.setFee(BaseTPPWay.formatMoney(fee));
		// tppPnrPay.setLateTime(lateTime);
		// tppPnrPay.setManageFee(manageFee);
		// tppPnrPay.setOperateTime(operateTime);
		tppPnrPay.setOperateType(operateType);
		tppPnrPay.setOrdId(OrderNoUtils.getSerialNumber());
		tppPnrPay.setOrdamt(ordamt);
		tppPnrPay.setOrddate(date);
		tppPnrPay.setPayUserName(userIn.getUserName());
		// TODO zjj
		// tppPnrPay.setPayUserCustId(userIn.getApiId());
		tppPnrPay.setPayUserCustId(String.valueOf(userIn.getUserId()));
		// tppPnrPay.setRepayId(repayId);
		tppPnrPay.setStatus("0");
		tppPnrPay.setSubordId(tender.getTenderBilNo());
		tppPnrPay.setSuborddate(tender.getTenderBilDate());
		tppPnrPay.setTenderId(String.valueOf(tender.getId()));
		tppPnrPay.setTrxId(tender.getTrxId());
		tppPnrPay.setUserName(userOut.getUserName());
		// TODO zjj
		// tppPnrPay.setUsrCustId(userOut.getApiId());
		tppPnrPay.setUsrCustId(String.valueOf(userOut.getUserId()));

		return tppPnrPay;
	}

	/**
	 * 填充汇付对象，类似放款方法，资金由投资人到借款人
	 * 
	 * @param cmdid
	 *            汇付交易对象
	 * @param fee
	 *            管理费
	 * @param riskReserveFee
	 *            风险备用金
	 * @param operateType
	 *            系统交易对象
	 * @param ordamt
	 *            交易金额
	 * @param userIn
	 *            收入方
	 * @param tender
	 *            投标信息
	 * @param userOut
	 *            出资方
	 * @return
	 */
	public TppPnrPay fillTppPnrPay(String cmdid, double fee,
			double riskReserveFee, String operateType, double ordamt,
			User userIn, BorrowTender tender, User userOut) {
		TppPnrPay tppPnrPay = new TppPnrPay();
		String date = DateUtil.dateStr7(new Date());
		tppPnrPay.setAddtime(new Date());
		tppPnrPay.setBorrowId(String.valueOf(tender.getBorrow().getId()));
		tppPnrPay.setCmdid(cmdid);
		tppPnrPay.setFee(BaseTPPWay.formatMoney(fee));
		tppPnrPay.setRiskReserveFee(BaseTPPWay.formatMoney(riskReserveFee));
		tppPnrPay.setOperateType(operateType);
		tppPnrPay.setOrdId(OrderNoUtils.getSerialNumber());
		tppPnrPay.setOrdamt(ordamt);
		tppPnrPay.setOrddate(date);
		tppPnrPay.setPayUserName(userIn.getUserName());
		// TODO zjj
		// tppPnrPay.setPayUserCustId(userIn.getApiId());
		tppPnrPay.setPayUserCustId(String.valueOf(userIn.getUserId()));
		tppPnrPay.setStatus("0");
		tppPnrPay.setSubordId(tender.getTenderBilNo());
		tppPnrPay.setSuborddate(tender.getTenderBilDate());
		tppPnrPay.setTenderId(String.valueOf(tender.getId()));
		tppPnrPay.setTrxId(tender.getTrxId());
		tppPnrPay.setUserName(userOut.getUserName());
		// TODO zjj
		// tppPnrPay.setUsrCustId(userOut.getApiId());
		tppPnrPay.setUsrCustId(String.valueOf(userOut.getUserId()));

		return tppPnrPay;
	}

	public void doWebPayMoney(ChinapnrWebPayModel payModel,
			List<Object> taskList) {
		TppPnrPay cppModel = new TppPnrPay(ChinaPnrType.TRANSFER,
				payModel.getMoney(), "1", String.valueOf(payModel.getPayUser()
						.getUserId()), "0", null, ChinaPnrType.WEBPAY);
		// TODO zjj
		// cppModel.setPayUserCustId(payModel.getPayUser().getApiId());
		cppModel.setPayUserCustId(String.valueOf(payModel.getPayUser()
				.getUserId()));
		cppModel.setUsrCustId("");
		taskList.add(cppModel);
	}

	@Override
	public void overdue(BorrowRepayment borrowRepayment) {
		List<Object> taskList = new ArrayList<Object>();
		List<BorrowCollection> list = collectionDao.list(borrowRepayment
				.getBorrow().getId(), borrowRepayment.getPeriod());
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				BorrowCollection collection = list.get(i);

				// 利息管理费
				double manageFee = collection.getManageFee();

				// 应还本金
				// 增加债权处理，待还本金需要扣除已经转出的债权价值
				double capital = BigDecimalUtil.sub(collection.getCapital(),
						collection.getBondCapital());
				if (capital > 0) {
					ChinapnrWebPayModel payModel = new ChinapnrWebPayModel();
					payModel.setMoney(capital);
					payModel.setPayUser(collection.getTender().getUser());
					doWebPayMoney(payModel, taskList);
				}

				// 应还利息
				// 增加债权处理，待还利息需要扣除已经转出的利息
				double interest = BigDecimalUtil.sub(collection.getInterest(),
						collection.getBondInterest());
				if (interest > 0) {
					ChinapnrWebPayModel payModel = new ChinapnrWebPayModel();
					payModel.setMoney(interest - manageFee);// 直接减去利息管理费，平台只需垫付减去利息管理费后的利息即可
					payModel.setPayUser(collection.getTender().getUser());
					doWebPayMoney(payModel, taskList);
				}

				// 逾期利息
				double lateInterest = BigDecimalUtil.mul(
						borrowRepayment.getLateInterest(),
						(BigDecimalUtil.div(interest,
								borrowRepayment.getInterest())));
				lateInterest = BigDecimalUtil.round(lateInterest);
				if (lateInterest > 0) {
					ChinapnrWebPayModel payModel = new ChinapnrWebPayModel();
					payModel.setMoney(lateInterest);
					payModel.setPayUser(collection.getTender().getUser());
					doWebPayMoney(payModel, taskList);
				}

				// 加息券利息
				double interestRate = collection.getInterestRate();
				if (interestRate > 0) {
					ChinapnrWebPayModel payModel = new ChinapnrWebPayModel();
					payModel.setMoney(interestRate);
					payModel.setPayUser(collection.getTender().getUser());
					doWebPayMoney(payModel, taskList);
				}

				// 应收奖励
				// 增加债权处理，待还奖励需要扣除已经转出的奖励（只需计算还款时发放的投资奖励）
				double awardValue = BigDecimalUtil.sub(
						collection.getRepayAward(), collection.getBondAward());
				if (awardValue > 0
						&& collection.getRepayAwardStatus() == Constant.REPAY_AWARD_STATUS_NORAML) {
					ChinapnrWebPayModel payModel = new ChinapnrWebPayModel();
					payModel.setMoney(awardValue);
					payModel.setPayUser(collection.getTender().getUser());
					doWebPayMoney(payModel, taskList);
				}
			}
			System.out
					.println("============================" + taskList.size());
			doApiTask(taskList);
		}

	}

	/**
	 * 填充汇付对象，类似放款方法，资金由投资人到借款人
	 * 
	 * @param cmdid
	 *            汇付交易对象
	 * @param fee
	 *            管理费
	 * @param operateType
	 *            系统交易对象
	 * @param ordamt
	 *            交易金额
	 * @param userIn
	 *            收入方
	 * @param tender
	 *            投标信息
	 * @param userOut
	 *            出资方
	 * @return
	 */
	public TppPnrPay fillTppPnrPayBond(String cmdid, double fee,
			String operateType, double ordamt, User userIn, BondTender tender,
			User userOut) {

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
		tppPnrPay.setPayUserName(userIn.getUserName());
		// TODO zjj
		// tppPnrPay.setPayUserCustId(userIn.getApiId());
		tppPnrPay.setPayUserCustId(String.valueOf(userIn.getUserId()));
		tppPnrPay.setStatus("0");
		tppPnrPay.setSubordId(tender.getOrderId());
		tppPnrPay.setSuborddate(tender.getOrderDate());
		tppPnrPay.setTenderId(String.valueOf(tender.getId()));
		tppPnrPay.setUserName(userOut.getUserName());
		// TODO zjj
		// tppPnrPay.setUsrCustId(userOut.getApiId());
		tppPnrPay.setUsrCustId(String.valueOf(userOut.getUserId()));

		return tppPnrPay;
	}

	@Override
	public double getHappendInterest(BondModel bm) {
		// 下一期还款信息
		BorrowCollection nextBorrowCollection = collectionDao
				.getNextCollectionByBorrowId(bm.getBorrow().getId(), bm
						.getUser().getUserId());
		double remainCaptil = 0;
		// 投资转让
		if (bm.getType() == 0) {
			remainCaptil = collectionDao.getRemainderCapital(bm.getKfId());
			// 债权再转让
		} else {
			remainCaptil = bondCollectionDao.getRemainderCapital(bm.getKfId());
		}
		// if(remainCaptil == 0){
		// throw new BondException("此债权剩余本金已经全部转让!");
		// }
		double rate = bm.getMoney() / remainCaptil;
		double interest = BigDecimalUtil.round(getHappendInterest(
				bm.getBorrow(),
				BigDecimalUtil.mul(nextBorrowCollection.getInterest(), rate),
				nextBorrowCollection.getRepaymentTime()));
		return interest;
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
	 * 计算债权转让管理费(债权转让管理费 + 已经产生的利息的利息管理费)
	 * 
	 * @param money
	 * @return
	 */
	@Override
	public double getManageFee(double capital, double interest) {

		double manageCapital = 0;
		double manageInterest = BigDecimalUtil.mul(interest,
				Global.getDouble("borrow_fee"));

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
		return BigDecimalUtil.add(manageCapital, manageInterest);
	}

	@Override
	public void addBondTender(BondModel bm) {
		// 执行平台债权转让处理
		BondTender bondTender = bondTenderService.addBondTender(bm);
		// 任务列表
		List<Object> taskList = new ArrayList<Object>();
		// 通过bondTender得到投资使用的红包金额
		double redMoney = userRedPacketDao
				.getTotalPacketMoneyByBondTender(bondTender.getId());
		if (redMoney > 0) {
			User adminUser = userService.getUserById(1);
			TppPnrPay cppRed = fillTppPnrPayBond(ChinaPnrType.TRANSFER, 0,
					ChinaPnrType.CREDITASSIGN, redMoney, bondTender.getUser(),
					bondTender, adminUser);
			// 从商户转账红包金额给投资人
			taskList.add(cppRed);
		}
		doApiTask(taskList);
	}

}
