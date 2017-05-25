package com.rongdu.p2psys.borrow.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountLogDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
import com.rongdu.p2psys.borrow.dao.BorrowDao;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowCollection;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.borrow.exception.BorrowException;
import com.rongdu.p2psys.borrow.model.BorrowHelper;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowTenderModel;
import com.rongdu.p2psys.borrow.model.NewTenderModel;
import com.rongdu.p2psys.borrow.model.interest.InterestCalculator;
import com.rongdu.p2psys.borrow.model.worker.BorrowWorker;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.CouponStatusConstant;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.model.RankModel;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.nb.invest.dao.FrozenProductDao;
import com.rongdu.p2psys.nb.invest.dao.FrozenUserDao;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitRecordDao;
import com.rongdu.p2psys.nb.recommend.dao.RecommendRecordDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfitRecord;
import com.rongdu.p2psys.nb.recommend.domain.RecommendRecord;
import com.rongdu.p2psys.nb.user.dao.CouponDao;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.voucher.domain.InterestRate;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.nb.wechat.util.WechatMessage;
import com.rongdu.p2psys.nb.wechat.util.WechatMessageData;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderInvestSuccessLog;
import com.rongdu.p2psys.tpp.ips.service.IpsService;
import com.rongdu.p2psys.user.dao.UserInviteDao;
import com.rongdu.p2psys.user.dao.UserPromotDao;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.Coupon;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserInvite;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserVipService;

@Service("borrowTenderService")
public class BorrowTenderServiceImpl implements BorrowTenderService {

	@Resource
	private BorrowTenderDao tenderDao;
	@Resource
	private BorrowDao borrowDao;
	@Resource
	private BorrowCollectionDao borrowCollectionDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private UserRedPacketDao userRedPacketDao;
	@Resource
	private UserInviteDao userInviteDao;
	@Resource
	private UserPromotDao userPromotDao;
	@Resource
	private FrozenProductDao frozenProductDao;
	@Resource
	private FrozenUserDao frozenUserDao;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private WechatCacheService wechatCacheService;
	@Resource
	private UserService theUserService;
	@Resource
	private CouponDao couponDao;

	@Override
	public PageDataList<BorrowTenderModel> multipleIdentitiesList(
			BorrowTenderModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
			if (null != model.getUser() && model.getUser().getBindId() > 0) {
				param.addParam("user.bindId", model.getUser().getBindId());
			} else if (null != model.getUser()
					&& model.getUser().getUserId() > 0) {
				param.addParam("user.userId", model.getUser().getUserId());
			}
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ,
						model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getBorrowName())) {
				param.addParam("borrow.name", Operators.LIKE,
						model.getBorrowName());
			}
			Date d = DateUtil.getDayEndTime(System.currentTimeMillis() / 1000);
			Date start = null;
			if (model.getTime() == 7) {
				start = DateUtil.getDayStartTime(DateUtil.rollDay(d, -7)
						.getTime() / 1000);
				param.addParam("addTime", Operators.GTE, start);
				param.addParam("addTime", Operators.LTE, d);
			} else if (model.getTime() > 0 && model.getTime() < 4) {
				start = DateUtil.getDayStartTime(DateUtil.rollMon(d,
						-model.getTime()).getTime() / 1000);
				param.addParam("addTime", Operators.GTE, start);
				param.addParam("addTime", Operators.LTE, d);
			}
			if (model.getStatus() > -1) {
				param.addParam("status", model.getStatus());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
				param.addParam("addTime", Operators.LTE, end);
			}
			param.addPage(model.getPage(), model.getSize());
		}
		param.addOrder(OrderType.DESC, "id");

		PageDataList<BorrowTender> pageDataList = tenderDao.findPageList(param);
		PageDataList<BorrowTenderModel> pageDateList = new PageDataList<BorrowTenderModel>();
		List<BorrowTenderModel> list = new ArrayList<BorrowTenderModel>();
		pageDateList.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowTender t = (BorrowTender) pageDataList.getList().get(i);
				BorrowTenderModel tm = BorrowTenderModel.instance(t);
				Borrow borrow = t.getBorrow();
				tm.setBorrowName(borrow.getName());
				tm.setAccountYes(borrow.getAccountYes());
				tm.setUserName(t.getUser().getUserName());
				if (borrow.getUser() != null) {
					tm.setBorrowUserName(borrow.getUser().getUserName());
				} else {
					tm.setBorrowUserName("平台");
				}
				tm.setScales(borrow.getScales());
				tm.setBorrowId(borrow.getId());
				tm.setApr(borrow.getApr());
				// 计算借款到期日
				if (t.getStatus() == 1) {
					if (borrow.getBorrowTimeType() == 1) {// 天标
						tm.setExpirationDate(DateUtil.dateStr2(DateUtil
								.rollDay(borrow.getReviewTime(),
										borrow.getTimeLimit())));
					} else {
						tm.setExpirationDate(DateUtil.dateStr2(DateUtil
								.rollMon(borrow.getReviewTime(),
										borrow.getTimeLimit())));
					}
				}
				tm.setUser(null);
				tm.setBorrow(null);
				list.add(tm);
			}
		}
		pageDateList.setList(list);
		return pageDateList;
	}

	@Override
	public PageDataList<BorrowTenderModel> list(BorrowTenderModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
			if (model.getUser() != null && model.getUser().getUserId() > 0) {
				param.addParam("user.userId", model.getUser().getUserId());
			}
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", Operators.EQ,
						model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getBorrowName())) {
				param.addParam("borrow.name", Operators.LIKE,
						model.getBorrowName());
			}
			Date d = DateUtil.getDayEndTime(System.currentTimeMillis() / 1000);
			Date start = null;
			if (model.getTime() == 7) {
				start = DateUtil.getDayStartTime(DateUtil.rollDay(d, -7)
						.getTime() / 1000);
				param.addParam("addTime", Operators.GTE, start);
				param.addParam("addTime", Operators.LTE, d);
			} else if (model.getTime() > 0 && model.getTime() < 4) {
				start = DateUtil.getDayStartTime(DateUtil.rollMon(d,
						-model.getTime()).getTime() / 1000);
				param.addParam("addTime", Operators.GTE, start);
				param.addParam("addTime", Operators.LTE, d);
			}
			if (model.getStatus() > -1) {
				param.addParam("status", model.getStatus());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
				param.addParam("addTime", Operators.LTE, end);
			}
			param.addPage(model.getPage(), model.getSize());
		}
		param.addOrder(OrderType.DESC, "id");

		PageDataList<BorrowTender> pageDataList = tenderDao.findPageList(param);
		PageDataList<BorrowTenderModel> pageDateList = new PageDataList<BorrowTenderModel>();
		List<BorrowTenderModel> list = new ArrayList<BorrowTenderModel>();
		pageDateList.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowTender t = (BorrowTender) pageDataList.getList().get(i);
				BorrowTenderModel tm = BorrowTenderModel.instance(t);
				Borrow borrow = t.getBorrow();
				tm.setBorrowName(borrow.getName());
				tm.setAccountYes(borrow.getAccountYes());
				tm.setUserName(t.getUser().getUserName());
				tm.setBorrowType(borrow.getType());
				if (borrow.getUser() != null) {
					tm.setBorrowUserName(borrow.getUser().getUserName());
				} else {
					tm.setBorrowUserName("平台");
				}
				tm.setScales(borrow.getScales());
				tm.setBorrowId(borrow.getId());
				tm.setApr(borrow.getApr());
				// 计算借款到期日
				if (t.getStatus() == 1) {
					if (borrow.getBorrowTimeType() == 1) {
						tm.setExpirationDate(DateUtil.dateStr2(DateUtil
								.addDate(DateUtil.rollDay(
										borrow.getReviewTime(),
										borrow.getTimeLimit()), -1)));
					} else {
						tm.setExpirationDate(DateUtil.dateStr2(DateUtil
								.addDate(DateUtil.rollMon(
										borrow.getReviewTime(),
										borrow.getTimeLimit()), -1)));
					}
				}
				tm.setUser(null);
				tm.setBorrow(null);
				tm.setFlagId(productBasicService
						.getProductBasicInfo(new Long(borrow.getType()),
								borrow.getId()).getProductTypeFlag().getId());
				list.add(tm);
			}
		}
		pageDateList.setList(list);
		return pageDateList;
	}

	@Override
	public double hasTenderTotalPerBorrowByUserid(long borrowId, long userId) {
		return tenderDao.hasTenderTotalPerBorrowByUserid(borrowId, userId);
	}

	@Override
	public PageDataList<BorrowTenderModel> list(long borrowId, int page,
			int size) {
		return tenderDao.list(borrowId, page, size);
	}

	@Override
	public BorrowTender addNbTender(Borrow borrow, BorrowModel m) {
		if (BigDecimalUtil.add(borrow.getAccountYes(), m.getMoney()) > borrow
				.getAccount()) {
			throw new BorrowException("投标金额不能大于可投金额");
		}
		User user = m.getUser();
		QueryParam param = QueryParam.getInstance().addParam("user", user)
				.addParam("tenderType", 0);
		/**
		 * 判断是否首次投资
		 */
		double rate = 0;
		if (tenderDao.countByCriteria(param) == 0) {
			UserInvite userInvite = userInviteDao
					.findByCriteriaForUnique(QueryParam.getInstance().addParam(
							"user", user));
			if (userInvite != null) {
				UserPromot userPromot = userPromotDao
						.getUserPromotByUserId(userInvite.getInviteUser());
				if (userPromot != null) {
					rate = userPromot.getRate();
				}
			}
		}
		double money = m.getMoney();

		Long[] ids = m.getIds();
		double totalPacketMoney = userRedPacketDao
				.getTotalPacketMoneyByIds(ids);
		/**
		 * 用于标记是投标的情况
		 */
		borrow.setScales(-1);
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		BorrowModel model = BorrowModel.instance(worker.prototype());
		/**
		 * 投标校验
		 */
		Account act = accountDao.findObjByProperty("user.userId",
				user.getUserId());
		/**
		 * 红包总额与用户可用余额总和
		 */
		double totalUseMoney = BigDecimalUtil.add(act.getUseMoney(),
				totalPacketMoney);
		if (money > totalUseMoney) {
			model.setMoney(totalUseMoney);
		} else {
			model.setMoney(money);
		}
		/**
		 * 填充投标
		 */
		BorrowTender tender = fillBorrowTender(model, user, m.getFlowCount(),
				rate);
		BorrowTender validTender = getValidTender(tender);
		if (ids != null) {
			/**
			 * 红包存放tender
			 */
			for (long id : ids) {
				UserRedPacket userRedPacket = userRedPacketDao.find(id);
				userRedPacket.setUsed(true);
				userRedPacket.setTender(validTender);
				userRedPacket.setUsedTime(new Date());
				userRedPacketDao.merge(userRedPacket);
			}
		}
		double validAccount = validTender.getAccount();
		InterestCalculator ic = worker.interestCalculator(validAccount);
		validTender = worker.tenderSuccess(validTender, ic);
		List<BorrowCollection> collectList = worker.createCollectionList(
				validTender, ic);
		borrowCollectionDao.save(collectList);
		/**
		 * 冻结投资人本金
		 */
		if (!"appointmentBid".equals(m.getBidLogo())) {
			Global.setTransfer("borrow", borrow);
			Global.setTransfer("money", validTender.getMoney());
			Global.setTransfer("tender", tender);
			Global.setTransfer("user", tender.getUser());
			AbstractExecuter borrowTenderExecuter = ExecuterHelper
					.doExecuter("borrowTenderExcuter");

			borrowTenderExecuter.execute(validAccount, user, borrow.getUser());
		}
		if (totalPacketMoney > 0) {
			/**
			 * 红包使用资金日志
			 */
			if (totalPacketMoney > 0) {
				accountDao.modify(totalPacketMoney, totalPacketMoney, 0, 0,
						user.getUserId());
				Global.setTransfer("user", user);
				Global.setTransfer("money", totalPacketMoney);
				Global.setTransfer("borrow", borrow);
				ProductBasic productBasic = productBasicService
						.getProductBasicInfo(new Long(borrow.getType()),
								borrow.getId());
				Global.setTransfer("flag", productBasic.getProductTypeFlag());
				AbstractExecuter redPacketExecuter = ExecuterHelper
						.doExecuter("tenderRedPacketSuccessExecuter");
				redPacketExecuter.execute(money, user, new User(1));
			}
		}
		/**
		 * disruptor可能是事物原因导致无法直接获取更新后的borrow标yes_account字段，重新查询一次赋值
		 */
		Borrow b = borrowDao.find(borrow.getId());
		worker = BorrowHelper.getWorker(b);
		worker.immediateInterestAfterTender(validTender);
		worker.immediateRepayAfterTender(validTender);
		return tender;

	}

	@Override
	public BorrowTender addTender(Borrow borrow, BorrowModel m) {
		User user = m.getUser();
		if (BigDecimalUtil.add(borrow.getAccountYes(), m.getMoney()) > borrow
				.getAccount()) {
			frozenProductDao.unLockProjectMoney(borrow.getId(),
					m.getProductBasicId(), user.getUserId());
			frozenUserDao.unLockUserMoney(borrow.getId(),
					m.getProductBasicId(), user.getUserId());
			throw new BorrowException("投标金额不能大于可投金额");
		}

		QueryParam param = QueryParam.getInstance().addParam("user", user)
				.addParam("tenderType", 0);
		// 判断是否首次投资
		double rate = 0;
		if (tenderDao.countByCriteria(param) == 0) {
			UserInvite userInvite = userInviteDao
					.findByCriteriaForUnique(QueryParam.getInstance().addParam(
							"user", user));
			if (userInvite != null) {
				UserPromot userPromot = userPromotDao
						.getUserPromotByUserId(userInvite.getInviteUser());
				if (userPromot != null) {
					rate = userPromot.getRate();
				}
			}
		}
		
		InterestRate interestVoucher = borrow.getInterestRate();
		if(interestVoucher!=null)//标自带的加息券
		{
			rate += interestVoucher.getRate();
		}

		Long[] cids = m.getCids();
		Coupon coupon = null;
		if(cids!=null)//一次投资只准使用一次加息券 
		{
			if(cids[0]!=null){
				coupon = couponDao.find(cids[0]);
				if(coupon!=null)
				{
					rate += coupon.getToRateAdjust();
				}
			}
		}
		double money = m.getMoney();

		Long[] ids = m.getIds();
		double totalPacketMoney = userRedPacketDao
				.getTotalPacketMoneyByIds(ids);
		borrow.setScales(-1); // 用于标记是投标的情况
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		BorrowModel model = BorrowModel.instance(worker.prototype());
		// 投标校验
		Account act = accountDao.findObjByProperty("user.userId",
				user.getUserId());
		// 用户总余额
		double useMoney = accountDao.getSumAccount(user.getBindId());

		double totalUseMoney = BigDecimalUtil.add(useMoney, totalPacketMoney);
		if (money > totalUseMoney) {
			model.setMoney(totalUseMoney);
		} else {
			model.setMoney(money);
		}
		// 填充投标
		BorrowTender tender = fillBorrowTender(model, user, m.getFlowCount(),
				rate);
		BorrowTender validTender = getValidTender(tender);
		
		if(coupon!=null)//一次投资只准使用一次加息券 
		{
			coupon.setBorrowTenderId(tender.getId());
			coupon.setStatus(CouponStatusConstant.STATUS_USED);
			couponDao.update(coupon);
		}
		if (ids != null) { // 红包存放tender
			for (long id : ids) {
				UserRedPacket userRedPacket = userRedPacketDao.find(id);
				userRedPacket.setUsed(true);
				userRedPacket.setTender(validTender);
				userRedPacket.setUsedTime(new Date());
				userRedPacketDao.merge(userRedPacket);
			}
		}
		double validAccount = validTender.getAccount();
		InterestCalculator ic = worker.interestCalculator(validAccount);
		validTender = worker.tenderSuccess(validTender, ic);
		List<BorrowCollection> collectList = worker.createCollectionList(
				validTender, ic);
		borrowCollectionDao.save(collectList);
		ProductBasic productBasic = productBasicService.getProductBasicInfo(
				new Long(borrow.getType()), borrow.getId());
		// 冻结投资人本金
		if (!"appointmentBid".equals(m.getBidLogo())) {
			Global.setTransfer("flag", productBasic.getProductTypeFlag());
			Global.setTransfer("borrow", borrow);
			Global.setTransfer("money", validTender.getMoney());
			Global.setTransfer("tender", tender);
			Global.setTransfer("user", tender.getUser());
			AbstractExecuter borrowTenderExecuter = ExecuterHelper
					.doExecuter("borrowTenderExcuter");
			if (totalPacketMoney > 0) {
				validAccount = validAccount - totalPacketMoney;
			}
			if (validAccount > 0) {
				borrowTenderExecuter.execute(validAccount, user,
						borrow.getUser());
			}

		}
		if (totalPacketMoney > 0) {
			// 红包使用资金日志
			accountDao.modify(totalPacketMoney, 0, totalPacketMoney, 0,
					user.getUserId());
			Global.setTransfer("user", user);
			Global.setTransfer("money", totalPacketMoney);
			Global.setTransfer("borrow", borrow);
			Global.setTransfer("flag", productBasic.getProductTypeFlag());
			AbstractExecuter redPacketExecuter = ExecuterHelper
					.doExecuter("tenderRedPacketSuccessExecuter");
			redPacketExecuter.execute(totalPacketMoney, user, new User(1));
		}
		// disruptor可能是事物原因导致无法直接获取更新后的borrow标yes_account字段，重新查询一次赋值
		Borrow b = borrowDao.find(borrow.getId());
		worker = BorrowHelper.getWorker(b);
		worker.immediateInterestAfterTender(validTender);
		worker.immediateRepayAfterTender(validTender);
		// 解锁金额
		frozenProductDao.unLockProjectMoney(borrow.getId(),
				m.getProductBasicId(), user.getUserId());
		frozenUserDao.unLockUserMoney(borrow.getId(), m.getProductBasicId(),
				user.getUserId());
		//发送模板消息
		List<User> list = new ArrayList<User>();
		list = theUserService.getByGroupId(user.getBindId());
		String userName = user.getUserName().substring(0, 3) + "****"
				+ user.getUserName().substring(7, user.getUserName().length());
		if (list.size() > 1){
			for (int i = 0; i < list.size(); i++){
				if (null != list.get(i).getWechatOpenId()
						&&!"".equals(list.get(i).getWechatOpenId())
						&&WechatMessageData.A_APP_ID.equals(list.get(i).getWechatId())){
					WechatMessage wechatMessage = new WechatMessage();
					SimpleDateFormat time=new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒"); 
					wechatMessage.setAppId(WechatMessageData.A_APP_ID);
					wechatMessage.setAppSecret(WechatMessageData.A_APP_SECRET);
					wechatMessage.setType(WechatMessageData.Acoount_Change);
					wechatMessage.setFirstData("尊敬的用户，您"+userName+"的账号已成功投标["+b.getName()+"]，投标金额已冻结。等待起息中");
					wechatMessage.setProductDate(time.format(new Date()));
					wechatMessage.setBuyMoney(model.getMoney());
					wechatMessage.setBusinessType("投标锁定");
					wechatMessage.setLeftMoney(accountDao.getSumAccount(user.getBindId()));
					wechatMessage.setRemark("");
					wechatMessage.setUrl(WechatMessageData.A_HOST+"/nb/wechat/account/lock.html");
					wechatMessage.setOpenId(list.get(i).getWechatOpenId());
					System.out.println("开始发送微信投资成功模板:"+wechatMessage.getOpenId()+"--"+wechatMessage.getAppId());
					try{
						wechatCacheService.sendWechatMessage(wechatMessage);
					} catch (ClientProtocolException e){
						e.printStackTrace();
					} catch (IOException e){
						e.printStackTrace();
					}
				}
			}
		}
		
		return tender;
	}
	
	
	public BorrowTender addVipTender(Borrow borrow, BorrowModel m) {
		User user = m.getUser();
		if (BigDecimalUtil.add(borrow.getAccountYes(), m.getMoney()) > borrow
				.getAccount()) {
			frozenProductDao.unLockProjectMoney(borrow.getId(),
					m.getProductBasicId(), user.getUserId());
			frozenUserDao.unLockUserMoney(borrow.getId(),
					m.getProductBasicId(), user.getUserId());
			throw new BorrowException("投标金额不能大于可投金额");
		}

		QueryParam param = QueryParam.getInstance().addParam("user", user)
				.addParam("tenderType", 0);
		// 判断是否首次投资
		double rate = 0;
		if (tenderDao.countByCriteria(param) == 0) {
			UserInvite userInvite = userInviteDao
					.findByCriteriaForUnique(QueryParam.getInstance().addParam(
							"user", user));
			if (userInvite != null) {
				UserPromot userPromot = userPromotDao
						.getUserPromotByUserId(userInvite.getInviteUser());
				if (userPromot != null) {
					rate = userPromot.getRate();
				}
			}
		}
		InterestRate interestVoucher = borrow.getInterestRate();
		if(interestVoucher!=null)//标自带的加息券
		{
			rate += interestVoucher.getRate();
		}
		
		Long[] cids = m.getCids();
		Coupon coupon = null;
		if(cids!=null)//一次投资只准使用一次加息券 
		{
			if(cids[0]!=null){
				coupon = couponDao.find(cids[0]);
				if(coupon!=null)
				{
					rate += coupon.getToRateAdjust();
				}
			}
		}
		double money = m.getMoney();

		Long[] ids = m.getIds();
		double totalPacketMoney = userRedPacketDao
				.getTotalPacketMoneyByIds(ids);
		borrow.setScales(-1); // 用于标记是投标的情况
		BorrowWorker worker = BorrowHelper.getWorker(borrow);
		BorrowModel model = BorrowModel.instance(worker.prototype());
	
		// 用户总余额
		double useMoney = accountDao.getSumAccount(user.getBindId());

		double totalUseMoney = BigDecimalUtil.add(useMoney, totalPacketMoney);
		if (money > totalUseMoney) {
			model.setMoney(totalUseMoney);
		} else {
			model.setMoney(money);
		}
		// 填充投标
		BorrowTender tender = fillBorrowTender(model, user, m.getFlowCount(),
				rate);
		BorrowTender validTender = getValidTender(tender);
		
		if (ids != null) { // 红包存放tender
			for (long id : ids) {
				UserRedPacket userRedPacket = userRedPacketDao.find(id);
				userRedPacket.setUsed(true);
				userRedPacket.setTender(validTender);
				userRedPacket.setUsedTime(new Date());
				userRedPacketDao.merge(userRedPacket);
			}
		}
		if(coupon!=null)//一次投资只准使用一次加息券 
		{
			coupon.setBorrowTenderId(tender.getId()); 
			coupon.setStatus(CouponStatusConstant.STATUS_USED);
			couponDao.update(coupon);
		}
		double validAccount = validTender.getAccount();
		InterestCalculator ic = worker.interestCalculator(validAccount);
		validTender = worker.tenderSuccess(validTender, ic);
		List<BorrowCollection> collectList = worker.createCollectionList(
				validTender, ic);
		borrowCollectionDao.save(collectList);
		ProductBasic productBasic = productBasicService.getProductBasicInfo(
				new Long(borrow.getType()), borrow.getId());
		//扣减投资人本金
		if (!"appointmentBid".equals(m.getBidLogo())) {
			Global.setTransfer("flag", productBasic.getProductTypeFlag());
			Global.setTransfer("borrow", borrow);
			Global.setTransfer("money", validTender.getMoney());
			Global.setTransfer("tender", tender);
			Global.setTransfer("user", tender.getUser());
			AbstractExecuter tenderVipSuccessExecuter = ExecuterHelper
					.doExecuter("tenderVipSuccessExecuter");
			if (totalPacketMoney > 0) {
				validAccount = validAccount - totalPacketMoney;
			}
			if (validAccount > 0) {
				tenderVipSuccessExecuter.execute(validAccount, user,
						borrow.getUser());
			}

		}
		if (totalPacketMoney > 0) {
			// 红包使用资金日志
			accountDao.modify(totalPacketMoney, 0, 0, totalPacketMoney,
					user.getUserId());
			Global.setTransfer("user", user);
			Global.setTransfer("money", totalPacketMoney);
			Global.setTransfer("borrow", borrow);
			Global.setTransfer("flag", productBasic.getProductTypeFlag());
			AbstractExecuter redPacketExecuter = ExecuterHelper
					.doExecuter("tenderRedPacketSuccessExecuter");
			redPacketExecuter.execute(totalPacketMoney, user, new User(1));
		}
		// disruptor可能是事物原因导致无法直接获取更新后的borrow标yes_account字段，重新查询一次赋值
		Borrow b = borrowDao.find(borrow.getId());
		worker = BorrowHelper.getWorker(b);
		
		User tenderUser = tender.getUser();
			
		Global.setTransfer("tender", tender);
		double account = tender.getAccount();

		double interest = tender.getInterest();
		// 扣除冻结/生产待收本金
		Global.setTransfer("money", account);
//		Global.setTransfer("borrow", data);
		
		Global.setTransfer("flag", productBasic.getProductTypeFlag());
//		AbstractExecuter freeExecuter = ExecuterHelper
//				.doExecuter("borrowDecuctFreezeExecuter");
//		freeExecuter.execute(account, tenderUser, user);
		// 生产待收利息
		if (interest > 0) {
			Global.setTransfer("money", interest);
			Global.setTransfer("borrow", borrow);
			AbstractExecuter waitExecuter = ExecuterHelper
					.doExecuter("borrowWaitInterestExecuter");
			waitExecuter.execute(interest, tenderUser, user);
		}


		// 生产待收利息--使用加息劵2015-01-03
		double interestRate = borrowCollectionDao.sumInterestRate(tender
				.getId());
		if (interestRate > 0) {
			Global.setTransfer("money", interestRate);
			Global.setTransfer("borrow", borrow);
			AbstractExecuter executer = ExecuterHelper
					.doExecuter("borrowWaitInterestRateExecuter");
			executer.execute(interestRate, tenderUser, user);
		}

		// 修改Tender表中的待收利息
		tender.setWaitAccount(tender.getRepaymentAccount());
		tender.setWaitInterest(tender.getInterest());
		tender.setStatus(1);
		tenderDao.update(tender);
		
		userRedPacketService.doTenderCoupon(tenderUser, tender);
		// 执行红包发放过程
		userRedPacketService.doTenderRedPacket(tenderUser, tender);
		userRedPacketService.doBorrowRedPacket(tenderUser, tender);
//;
//		AccountDao accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		UserInviteDao userInviteDao = (UserInviteDao) BeanUtil
				.getBean("userInviteDao");
		RecommendRecordDao recommendRecordDao = (RecommendRecordDao) BeanUtil
				.getBean("recommendRecordDao");
		RecommendProfitRecordDao recommendProfitRecordDao = (RecommendProfitRecordDao) BeanUtil
				.getBean("recommendProfitRecordDao");
		AccountLogDao accountLogDao =  (AccountLogDao) BeanUtil
				.getBean("accountLogDao");

		UserInvite userInvite = userInviteDao
				.findByCriteriaForUnique(QueryParam.getInstance().addParam(
						"user", user));

		if (null != userInvite) {
			RecommendRecord recommendRecord = recommendRecordDao
					.findRecord(userInvite.getInviteUser(),
							userInvite.getUser());
			/**
			 * 如果有推荐人
			 */
			if (null != recommendRecord) {
				RecommendProfitRecord recommendProfitRecord = new RecommendProfitRecord();
				double recommendRate = Global.getDouble("recommend_rate");
				
//				RecommendProfit recommendProfit = recommendProfitDao
//						.findByMoney(bt.getBorrow().getAccount());

				recommendProfitRecord.setAccount(tender.getAccount());
				recommendProfitRecord.setAddTime(new Date());
				recommendProfitRecord.setBorrow(tender.getBorrow());
				recommendProfitRecord.setInviteUser(userInvite
						.getInviteUser());

				int days = 0;
				/**
				 * 计算给推荐人收益天数
				 */
				if (0 == borrow.getBorrowTimeType()) {
					days = borrow.getTimeLimit();
					recommendProfitRecord.setMoney(BigDecimalUtil.round(tender.getAccount()
							* recommendRate * days / 12 / 1000));
				} else {
					days = borrow.getTimeLimit();
					recommendProfitRecord
							.setMoney(BigDecimalUtil.round(tender.getAccount()
									* recommendRate * days
									/ 365 / 1000));
				}

				recommendProfitRecord.setProfit((long)3);
				recommendProfitRecord.setTenderId(validTender.getId());
				recommendProfitRecord.setUser(validTender.getUser());

				if (0.01 <= recommendProfitRecord.getMoney()) {
					recommendProfitRecord = recommendProfitRecordDao
							.save(recommendProfitRecord);

					Account inviteUserAccount = accountDao
							.getAccountByUserId(userInvite.getInviteUser()
									.getUserId());
					inviteUserAccount.setTotal(BigDecimalUtil.add(inviteUserAccount.getTotal()
							, recommendProfitRecord.getMoney()));
					inviteUserAccount.setUseMoney(BigDecimalUtil.add(inviteUserAccount.getUseMoney()
							, recommendProfitRecord.getMoney()));
					accountDao.update(inviteUserAccount);

					AccountLog accountLog = new AccountLog();
					String userName = validTender.getUser().getUserName();
					String realName = userName.substring(
							0,
							userName.length()
									- (userName.substring(3)).length())
							+ "****" + userName.substring(7);
					accountLog.setAddTime(new Date());
					accountLog.setMoney(recommendProfitRecord.getMoney());
					accountLog.setCollection(inviteUserAccount.getCollection());
					accountLog.setNoUseMoney(inviteUserAccount.getNoUseMoney());
					accountLog.setPaymentsType((byte) 1);
					accountLog.setRemark("推荐收益来源用户" + realName + "投资项目:["
							+ borrow.getName() + "]，金额:"
							+ validTender.getAccount());
					accountLog.setTotal(inviteUserAccount.getTotal());
					accountLog.setToUser(user);
					accountLog.setType("recommend_profit");
					accountLog.setUseMoney(inviteUserAccount.getUseMoney());
					accountLog.setUser(userInvite.getInviteUser());
					accountLogDao.save(accountLog);
				}
			}
		}
		
		//发送模板消息
		List<User> list = new ArrayList<User>();
		list = theUserService.getByGroupId(user.getBindId());
		String userName = user.getUserName().substring(0, 3) + "****"
				+ user.getUserName().substring(7, user.getUserName().length());
		if (list.size() > 1){
			for (int i = 0; i < list.size(); i++){
				if (null != list.get(i).getWechatOpenId()
						&&!"".equals(list.get(i).getWechatOpenId())
						&&WechatMessageData.A_APP_ID.equals(list.get(i).getWechatId())){
					WechatMessage wechatMessage = new WechatMessage();
					SimpleDateFormat time=new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒"); 
					wechatMessage.setAppId(WechatMessageData.A_APP_ID);
					wechatMessage.setAppSecret(WechatMessageData.A_APP_SECRET);
					wechatMessage.setType(WechatMessageData.Acoount_Change);
					wechatMessage.setFirstData("尊敬的用户，您"+userName+"的账号已成功投标["+b.getName()+"]，投标金额已冻结。等待起息中");
					wechatMessage.setProductDate(time.format(new Date()));
					wechatMessage.setBuyMoney(model.getMoney());
					wechatMessage.setBusinessType("投标锁定");
					wechatMessage.setLeftMoney(accountDao.getSumAccount(user.getBindId()));
					wechatMessage.setRemark("");
					wechatMessage.setUrl(WechatMessageData.A_HOST+"/nb/wechat/account/lock.html");
					wechatMessage.setOpenId(list.get(i).getWechatOpenId());
					System.out.println("开始发送微信投资成功模板:"+wechatMessage.getOpenId()+"--"+wechatMessage.getAppId());
					try{
						wechatCacheService.sendWechatMessage(wechatMessage);
					} catch (ClientProtocolException e){
						e.printStackTrace();
					} catch (IOException e){
						e.printStackTrace();
					}
				}
			}
		}
		
		// 解锁金额
		frozenProductDao.unLockProjectMoney(borrow.getId(),
				m.getProductBasicId(), user.getUserId());
		frozenUserDao.unLockUserMoney(borrow.getId(), m.getProductBasicId(),
				user.getUserId());

//		worker.handleBorrowAfterFullSuccess();
		return validTender;
	}


	private BorrowTender fillBorrowTender(BorrowModel model, User user,
			int flowCount, double borrowInterestRateValue) {
		BorrowTender tender = new BorrowTender();
		tender.setBorrow(model.prototype());
		tender.setMoney(model.getMoney());
		// 投标订单号
		tender.setTenderBilNo(model.getTenderBilNo());
		tender.setTenderBilDate(model.getTenderBilDate());
		tender.setStatus(0);
		tender.setAddTime(new Date());
		tender.setAddIp(model.getAddIp());
		tender.setUser(user);
		tender.setInterestRateValue(borrowInterestRateValue);
		tender.setTenderType(model.getTenderType());
		return tender;
	}

	/** 最新借款 **/
	public List<NewTenderModel> getNewTenderList() {
		return tenderDao.getNewTenderList();
	}

	/** 首页投资排行榜 **/
	public List<RankModel> getRankList() {
		return tenderDao.getRankList();
	}

	public PageDataList<BorrowTenderModel> getTenderList(long borrowId, int page) {
		int pageNum = Global.getInt("con_borrow_ajaxTenderListPagenum");
		return tenderDao.getTenderListByBorrowid(borrowId, page, pageNum);
	}

	public double getUserTenderNum(long userid, Date beginDate, Date endDate) {
		return tenderDao.getUserTenderNum(userid, beginDate, endDate);
	}

	private BorrowTender getValidTender(BorrowTender tender) {
		// 获取最新的数据库的记录
		Borrow data = borrowDao.find(tender.getBorrow().getId());
		BorrowWorker worker = BorrowHelper.getWorker(data);
		double validAccount = worker.validAccount(tender);
		tender.setAccount(validAccount);
		// worker.checkTender(tender);
		double scales = BigDecimalUtil.div(
				BigDecimalUtil.add(data.getAccountYes(), validAccount),
				data.getAccount()) * 100;
		scales = BigDecimalUtil.decimal(scales, 2);
		borrowDao.update(validAccount, scales, data.getStatus(), data.getId());
		tender = tenderDao.save(tender);
		return tender;
	}

	@Override
	public List<BorrowTenderModel> getTenderList(long borrowId) {
		List<BorrowTender> itemList = tenderDao.findByProperty("borrow.id",
				borrowId);
		List<BorrowTenderModel> modelList = new ArrayList<BorrowTenderModel>();
		for (BorrowTender item : itemList) {
			BorrowTenderModel model = BorrowTenderModel.instance(item);
			model.setUserName(item.getUser().getUserName());
			model.setUserId(item.getUser().getUserId());
			modelList.add(model);
		}
		return modelList;
	}

	/**
	 * 根据borrowId查询tenderList
	 * 
	 * @param borrowId
	 * @return List<BorrowTender>
	 */
	@Override
	public List<BorrowTender> getTenderListByBorrowId(long borrowId) {
		return tenderDao.findByProperty("borrow.id", borrowId);
	}

	/**
	 * 根据id查询
	 * 
	 * @param tenderId
	 * @return
	 */
	@Override
	public BorrowTender getTenderById(long tenderId) {
		return tenderDao.find(tenderId);
	}

	@Override
	public PageDataList<BorrowModel> getBorrowlist(BorrowTenderModel model) {
		PageDataList<Borrow> pageDataList = tenderDao.getBorrowlist(model);
		PageDataList<BorrowModel> pageDataList_ = new PageDataList<BorrowModel>();
		List<BorrowModel> list = new ArrayList<BorrowModel>();
		pageDataList_.setPage(pageDataList.getPage());
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil
				.getBean("verifyLogDao");
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				Borrow borrow = (Borrow) pageDataList.getList().get(i);
				BorrowModel bm = BorrowModel.instance(borrow);
				try {
					bm.setUserName(borrow.getUser().getUserName());
					bm.setUserId(borrow.getUser().getUserId());
				} catch (Exception e) {
					bm.setUserName("<font color='red'>该用户被删除</font>");
					bm.setUserId(0);
				}

				VerifyLog log = verifyLogDao.findByType(borrow.getId(),
						"borrow", 1);
				if (log != null) {
					Date d = log.getTime(); // 初审时间
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					c.add(Calendar.DATE, borrow.getValidTime());
					Calendar now = Calendar.getInstance();
					now.setTime(new Date());
					bm.setFlow(c.before(now));
					bm.setStartTime(DateUtil.dateStr2(log.getTime()));
				}
				bm.setUser(null);

				// 获取分类ID（flag_id）
				Long flagId = productBasicService
						.getProductBasicInfo(new Long(borrow.getType()),
								borrow.getId()).getProductTypeFlag().getId();
				bm.setFlagId(flagId);

				list.add(bm);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public int getInvestCountByDate(String date) {
		return tenderDao.getInvestCountByDate(date);
	}

	@Override
	public List<Integer> getInvestCount() {
		List<Integer> counts = new ArrayList<Integer>();
		for (int i = 0; i < 7; i++) {
			int count = tenderDao.getInvestCountByDate(i);
			counts.add(count);
		}
		return counts;
	}

	@Override
	public double tenderAccount(String startTime, String endTime) {
		return tenderDao.tenderAccount(startTime, endTime);
	}

	@Override
	public int getInvestUserCount() {
		return tenderDao.getInvestUserCount();
	}

	@Override
	public int getInvestUserCount(String startTime, String endTime) {
		return tenderDao.getInvestUserCount(startTime, endTime);
	}

	@Override
	public double tenderAllInterest() {
		return tenderDao.tenderAllInterest();
	}

	@Override
	public int countTenderByUserAndStatus(long userId, int status) {
		return tenderDao.getTenderByUserAndStatus(userId, status);
	}

	@Override
	public void checkNovice(long userId) {
		int tenderNum = tenderDao.getUserTenderNum(userId);
		if (tenderNum > 0) {
			throw new BussinessException(MessageUtil.getMessage("MG20001F"),
					"/help/guide.html");
		}
	}

	@Override
	public PageDataList<BorrowTenderModel> getTenderRecordlist(
			BorrowTenderModel model) {
		return tenderDao.getTenderRecordlist(model);
	}

	@Override
	public PageDataList<BorrowTenderModel> bidList(BorrowModel model) {
		QueryParam param = QueryParam.getInstance();
		if (model != null) {
			if (!StringUtil.isBlank(model.getSearchName())) {// 模糊查询条件
				SearchFilter orFilter1 = new SearchFilter("user.userName",
						Operators.LIKE, model.getSearchName());
				SearchFilter orFilter2 = new SearchFilter("borrow.name",
						Operators.LIKE, model.getSearchName());
				SearchFilter orFilter3 = new SearchFilter("user.realName",
						Operators.LIKE, model.getSearchName());
				param.addOrFilter(orFilter1, orFilter2, orFilter3);
			} else { // 精确查询条件
				if (StringUtil.isNotBlank(model.getUserName())) {
					param.addParam("user.userName", Operators.EQ,
							model.getUserName());
				}
				if (StringUtil.isNotBlank(model.getRealName())) {
					param.addParam("user.realName", Operators.EQ,
							model.getRealName());
				}
				if (StringUtil.isNotBlank(model.getBorrowName())) {
					param.addParam("borrow.name", Operators.EQ,
							model.getBorrowName());
				}
				if (StringUtil.isNotBlank(model.getBidNo())) {
					param.addParam("borrow.bidNo", Operators.EQ,
							model.getBidNo());
				}
				if (StringUtil.isNotBlank(model.getStartTime())) {
					Date start = DateUtil.valueOf(model.getStartTime());
					param.addParam("addTime", Operators.GT, start);
				}
				if (StringUtil.isNotBlank(model.getEndTime())) {
					Date end = DateUtil.valueOf(model.getEndTime());
					param.addParam("addTime", Operators.LTE, end);
				}
			}
			param.addPage(model.getPage(), model.getSize());
		}
		param.addOrder(OrderType.DESC, "id");

		PageDataList<BorrowTender> pageDataList = tenderDao.findPageList(param);
		PageDataList<BorrowTenderModel> pageDateList = new PageDataList<BorrowTenderModel>();
		List<BorrowTenderModel> list = new ArrayList<BorrowTenderModel>();
		pageDateList.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				BorrowTender t = (BorrowTender) pageDataList.getList().get(i);
				BorrowTenderModel tm = BorrowTenderModel.instance(t);
				Borrow borrow = t.getBorrow();
				tm.setBorrowName(borrow.getName());
				tm.setUserName(t.getUser().getUserName());
				tm.setRealName(t.getUser().getRealName());
				tm.setScales(borrow.getScales());
				tm.setBidNo(borrow.getBidNo());
				tm.setUser(null);
				tm.setBorrow(null);
				list.add(tm);
			}
		}
		pageDateList.setList(list);
		return pageDateList;
	}

	public int getUserBorrowTenderOrder(long tenderId,long borrowId) {
		return this.tenderDao.getUserBorrowTenderOrder(tenderId,borrowId);
	}

}
