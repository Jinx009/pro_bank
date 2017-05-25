package com.rongdu.p2psys.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.dao.AccountRechargeDao;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.borrow.dao.BorrowTenderDao;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.domain.BorrowTender;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.dao.RedPacketDao;
import com.rongdu.p2psys.core.dao.RedPacketDetailDao;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.domain.RedPacketDetail;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.nb.user.dao.CouponDao;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.dao.UserRedPacketTypeDao;
import com.rongdu.p2psys.user.domain.Coupon;
import com.rongdu.p2psys.user.domain.CouponCategory;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserRedPacketModel;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;

@Service("userRedPacketService")
public class UserRedPacketServiceImpl implements UserRedPacketService {

	@Resource
	private UserRedPacketDao userRedPacketDao;
	@Resource
	private UserRedPacketTypeDao userRedPacketTypeDao;
	@Resource
	private UserCacheDao userCacheDao;
	@Resource
	private RedPacketDao redPacketDao;
	@Resource
	private BorrowTenderDao borrowTenderDao;
	@Resource
	private RedPacketDetailDao redPacketDetailDao;
	@Resource
	private AccountRechargeDao accountRechargeDao;
	@Resource
	private AccountDao accountDao;
	@Resource
	private UserService userService;
	@Resource
	private CouponDao couponDao;

	@Override
	public PageDataList<UserRedPacketModel> findByUser(User user, int page) {
		QueryParam param = QueryParam.getInstance().addParam("user", user)
				.addPage(page).addOrder(OrderType.DESC, "id");
		PageDataList<UserRedPacket> pageDataList = userRedPacketDao
				.findPageList(param);
		PageDataList<UserRedPacketModel> pageDataList_ = new PageDataList<UserRedPacketModel>();
		List<UserRedPacketModel> list = new ArrayList<UserRedPacketModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (UserRedPacket userRedPacket : pageDataList.getList()) {
				UserRedPacketModel userRedPacketModel = UserRedPacketModel
						.instance(userRedPacket);
				Date expiredTime = userRedPacket.getExpiredTime();
				if (expiredTime.before(new Date())) {
					userRedPacketModel.setStatus(-1); // 过期
				} else if (userRedPacket.isUsed()) {
					userRedPacketModel.setStatus(1); // 已使用
				} else {
					userRedPacketModel.setStatus(2); // 未使用
				}
				userRedPacketModel.setName(userRedPacket.getType().getName());
				userRedPacketModel.setType(null);
				userRedPacketModel.setUser(null);
				list.add(userRedPacketModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public List<UserRedPacketModel> findByUser(User user) {
		QueryParam param = QueryParam.getInstance().addParam("user", user)
				.addParam("isUsed", false).addOrder("expiredTime");
		List<UserRedPacket> packets = userRedPacketDao.findByCriteria(param);
		List<UserRedPacketModel> list = new ArrayList<UserRedPacketModel>();
		for (UserRedPacket userRedPacket : packets) {
			UserRedPacketModel userRedPacketModel = UserRedPacketModel
					.instance(userRedPacket);
			Date expiredTime = userRedPacket.getExpiredTime();
			Date now = new Date();
			int lastDays = (int) ((expiredTime.getTime() - now.getTime()) / (1000 * 3600 * 24));
			if (expiredTime.after(now)) {
				userRedPacketModel.setLastDays(lastDays);
				userRedPacketModel.setName(userRedPacket.getType().getName());
				userRedPacketModel.setType(null);
				userRedPacketModel.setUser(null);
				list.add(userRedPacketModel);
			}
		}
		return list;
	}
	
	@Override
	public List<UserRedPacketModel>  findByUserAndSelType(User user,int selType)
	{
		List<UserRedPacket> packets = new ArrayList<UserRedPacket>();
		if(selType==0)
		{
			packets = userRedPacketDao.getMultiAvalibleRedPacketList(user);
		}else
		{
			packets = userRedPacketDao.getSingleAvalibleRedPacketList(user);
		}
		List<UserRedPacketModel> list = new ArrayList<UserRedPacketModel>();
		for (UserRedPacket userRedPacket : packets)
		{
			UserRedPacketModel userRedPacketModel = UserRedPacketModel
					.instance(userRedPacket);
			Date expiredTime = userRedPacket.getExpiredTime();
			Date now = new Date();
			int lastDays = (int) ((expiredTime.getTime() - now.getTime()) / (1000 * 3600 * 24));
			userRedPacketModel.setLastDays(lastDays);
			userRedPacketModel.setName(userRedPacket.getType().getName());
			userRedPacketModel.setType(null);
			userRedPacketModel.setUser(null);
			list.add(userRedPacketModel);
			
		}
		return list;
	}

	@Override
	public double getTotalPacketMoneyByIds(Long[] ids) {
		return userRedPacketDao.getTotalPacketMoneyByIds(ids);
	}

	@Override
	public PageDataList<UserRedPacketModel> findByModel(UserRedPacketModel model) {
		QueryParam param = QueryParam.getInstance();
		if (!StringUtil.isBlank(model.getSearchName())) {// 模糊查询条件
			SearchFilter orFilter1 = new SearchFilter("user.userName",
					Operators.LIKE, model.getSearchName());
			SearchFilter orFilter2 = new SearchFilter("user.realName",
					Operators.LIKE, model.getSearchName());
			param.addOrFilter(orFilter1, orFilter2);
		} else { // 精确查询条件
			if (model.getId() != 0) {
				param.addParam("user.userId", model.getId());
			}
			if (!StringUtil.isBlank(model.getUserName())) {
				param.addParam("user.userName", model.getUserName());
			}
			if (!StringUtil.isBlank(model.getRealName())) {
				param.addParam("user.realName", model.getRealName());
			}
			if (model.getTypeId() != 0) {
				param.addParam("type.id", model.getTypeId());
			}
			if (model.getStatus() == 1) {
				param.addParam("isUsed", true);
			} else if (model.getStatus() == 2) {
				param.addParam("isUsed", false);
			} else if (model.getStatus() == -1) {
				param.addParam("isUsed", false);
				param.addParam("expiredTime", Operators.LT, new Date());
			}
			if (!StringUtil.isBlank(model.getReceiveStartTime())) {
				param.addParam("addTime", Operators.GT,
						DateUtil.valueOf(model.getReceiveStartTime()));
			}
			if (!StringUtil.isBlank(model.getReceiveEndTime())) {
				Date date = DateUtil.valueOf(model.getReceiveEndTime());
				param.addParam("addTime", Operators.LT,
						DateUtil.rollDay(date, 1));
			}
			if (!StringUtil.isBlank(model.getExpiredStartTime())) {
				param.addParam("expiredTime", Operators.GT,
						DateUtil.valueOf(model.getExpiredStartTime()));
			}
			if (!StringUtil.isBlank(model.getExpiredEndTime())) {
				Date date = DateUtil.valueOf(model.getExpiredEndTime());
				param.addParam("expiredTime", Operators.LT,
						DateUtil.rollDay(date, 1));
			}
		}
		param.addPage(model.getPage(), model.getRows());
		if (model.getId() != 0) {
			param.addOrder("expiredTime");
		} else {
			param.addOrder(OrderType.DESC, "id");
		}
		PageDataList<UserRedPacket> pageDataList = userRedPacketDao
				.findPageList(param);
		PageDataList<UserRedPacketModel> pageDataList_ = new PageDataList<UserRedPacketModel>();
		List<UserRedPacketModel> list = new ArrayList<UserRedPacketModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (UserRedPacket userRedPacket : pageDataList.getList()) {
				UserRedPacketModel userRedPacketModel = UserRedPacketModel
						.instance(userRedPacket);
				Date expiredTime = userRedPacket.getExpiredTime();
				if (expiredTime.before(new Date())) {
					userRedPacketModel.setStatus(-1); // 过期
				} else if (userRedPacket.isUsed()) {
					userRedPacketModel.setStatus(1); // 已使用
				} else {
					userRedPacketModel.setStatus(2); // 未使用
				}
				userRedPacketModel.setName(userRedPacket.getType().getName());
				userRedPacketModel.setUserName(userRedPacket.getUser()
						.getUserName());
				userRedPacketModel.setRealName(userRedPacket.getUser()
						.getRealName());
				userRedPacketModel.setType(null);
				userRedPacketModel.setUser(null);
				list.add(userRedPacketModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public List<UserRedPacketModel> statisticsByModel(UserRedPacketModel model) {
		List<Object[]> list = userRedPacketDao.statisticsByModel(model);
		List<UserRedPacketModel> newList = new ArrayList<UserRedPacketModel>();
		for (Object[] obj : list) {
			UserRedPacketModel userRedPacketModel = new UserRedPacketModel();
			long id = Long.parseLong(obj[0] + "");
			userRedPacketModel.setId(id);
			userRedPacketModel.setUserName(obj[1] + "");
			String realName = null;
			if (obj[2] != null) {
				realName = obj[2] + "";
			}
			userRedPacketModel.setRealName(realName);
			userRedPacketModel.setAmount(Double.parseDouble(obj[3] + ""));
			newList.add(userRedPacketModel);
		}
		return newList;
	}

	@Override
	public int getTotalByModel(UserRedPacketModel model) {
		return userRedPacketDao.getTotalByModel(model);
	}
	
	/*
	 * 给单个用户发放红包
	 * */
	public  void giveUserRedPacket(RedPacket redPacket,User user)
	{
		
		Date date = new Date();
		if (redPacket == null // 规则是否存在
				|| redPacket.getStatus() != 1 // 红包是否开启
				|| (redPacket.getStartTime() != null && date.before(redPacket
						.getStartTime())) // 是否还未到活动开始时间
				|| (redPacket.getEndTime() != null && date.after(redPacket
						.getEndTime())) // 是否已经超过活动结束时间
				|| (redPacket.getTotalNum() != 0 && redPacket.getUseNum() >= redPacket
						.getTotalNum())// 红包是否已经发放完毕
				|| (redPacket.getIsDelete() == 1) // 红包是否已被删除
		)
		{
			throw new UserException("红包规则设置有误，请检查！");
		}
		double redPacketMoney = redPacket.getMoney();
		Date expiredTime = null;
		// 如果红包有效天数大于0则设置到期时间，否则则代表长久有效
		if (redPacket.getDay() > 0)
		{
			expiredTime = DateUtil.rollDay(new Date(), redPacket.getDay());
		}
		UserRedPacket userRedPacket = new UserRedPacket(user, null, redPacket,
				redPacketMoney, expiredTime);

		// 如果红包金额大于0，则保存红包
		if (userRedPacket.getAmount() > 0)
		{
			userRedPacketDao.save(userRedPacket);

			// 更新红包发放个数
			redPacket.setUseNum(redPacket.getUseNum() + 1);
			redPacketDao.update(redPacket);
		}
		Global.setTransfer("money", redPacketMoney);
		Global.setTransfer("user", user);
		AbstractExecuter executer = ExecuterHelper
				.doExecuter("sendUserRedPacketExecuter");
		User toUser = new User(1);
		executer.execute(redPacketMoney, user, toUser);
		
	}
	
	/*
	 * 提醒红包到期提醒  
	 * */
	@Override
	public void remindUserDueRedPacket()
	{
		List<UserRedPacket> redPacketList = userRedPacketDao.getRemindRedPacketList();
		for(UserRedPacket redPacket : redPacketList)
		{
			User user = redPacket.getUser();
			Global.setTransfer("money", redPacket.getAmount());
			Global.setTransfer("user", user);
			Global.setTransfer("expiredTime", DateUtil.dateStr2(redPacket.getExpiredTime()));
			AbstractExecuter executer = ExecuterHelper
					.doExecuter("dueRedPacketRemindExecuter");
			User toUser = new User(1);
			executer.execute(redPacket.getAmount(), user, toUser);
			redPacket.setIsRemind(true);
			userRedPacketDao.save(redPacket);
		}
	}
	

	@Override
	public void doRedPacket(String type, User user, BorrowTender tender,
			AccountRecharge recharge) {
		// 检测规则是否存在，检测该规则是否配有红包
		RedPacket redPacket = redPacketDao.findObjByProperty("serviceType",
				type);
		Date date = new Date();
		if (redPacket == null // 规则是否存在
				|| redPacket.getStatus() != 1 // 红包是否开启
				|| (redPacket.getStartTime() != null && date.before(redPacket
						.getStartTime())) // 是否还未到活动开始时间
				|| (redPacket.getEndTime() != null && date.after(redPacket
						.getEndTime())) // 是否已经超过活动结束时间
				|| (redPacket.getTotalNum() != 0 && redPacket.getUseNum() >= redPacket
						.getTotalNum())// 红包是否已经发放完毕
				|| (redPacket.getIsDelete() == 1) // 红包是否已被删除
		// || user.getUserCache().getUserType() == 2
		) {// 判断用书是否为借款人，只有投资人才会有红包
			return;
		}

		// 操作金额
		double money = 0;
		if (tender != null) {
			money = tender.getAccount();
		}
		if (recharge != null) {
			money = recharge.getMoney();
		}

		// 红包金额
		double redPacketMoney = getRedPacketMoney(money, redPacket);
		// 红包到期时间
		Date expiredTime = null;
		// 如果红包有效天数大于0则设置到期时间，否则则代表长久有效
		if (redPacket.getDay() > 0) {
			expiredTime = DateUtil.rollDay(new Date(), redPacket.getDay());
		}
		UserRedPacket userRedPacket = new UserRedPacket(user, tender,
				redPacket, redPacketMoney, expiredTime);

		// 如果红包金额大于0，则保存红包
		if (userRedPacket.getAmount() > 0) {
			userRedPacketDao.save(userRedPacket);

			// 更新红包发放个数
			redPacket.setUseNum(redPacket.getUseNum() + 1);
			redPacketDao.update(redPacket);
		}
	}

	/**
	 * 计算红包金额
	 * 
	 * @param money
	 * @param redPacket
	 * @param rule
	 * @return
	 */
	public double getRedPacketMoney(double money, RedPacket redPacket) {
		double redPacketMoney = 0;
		// 红包最小金额
		double minMoney = redPacket.getMinMoney();
		// 红包最大金额
		double maxMoney = redPacket.getMaxMoney();

		// 发放方式
		switch (redPacket.getPaymentType()) {
		case 1:// 固定金额
			redPacketMoney = redPacket.getMoney();
			break;
		case 2:// 固定兑换比率
			redPacketMoney = BigDecimalUtil.round(money * redPacket.getRate()
					/ 100, 0);
			break;
		case 3:// 浮动方式
			List<RedPacketDetail> details = redPacketDetailDao
					.getList(redPacket);
			switch (redPacket.getFloatType()) {
			case 1:// 区间固定金额
				for (int i = 0; i < details.size(); i++) {
					RedPacketDetail detail = details.get(i);
					if (i == details.size() - 1) {// 最后一档不限
						if (detail.getMin() <= money) {
							redPacketMoney = detail.getMoney();
							break;
						}
					} else {
						if (detail.getMin() <= money && money < detail.getMax()) {
							redPacketMoney = detail.getMoney();
							break;
						}
					}
				}
				break;
			case 2:// 区间固定兑换比率
				for (int i = 0; i < details.size(); i++) {
					RedPacketDetail detail = details.get(i);
					if (i == details.size() - 1) {// 最后一档不限
						if (detail.getMin() <= money) {
							redPacketMoney = BigDecimalUtil.round(money
									* detail.getRate() / 100, 0);
							break;
						}
					} else {
						if (detail.getMin() <= money && money < detail.getMax()) {
							redPacketMoney = BigDecimalUtil.round(money
									* detail.getRate() / 100, 0);
							break;
						}
					}
				}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		if (minMoney > 0 && redPacketMoney < minMoney) {
			redPacketMoney = minMoney;
		}
		if (maxMoney > 0 && redPacketMoney > maxMoney) {
			redPacketMoney = maxMoney;
		}
		return redPacketMoney;
	}

	@Override
	public void doTenderRedPacket(User user, BorrowTender tender) {
		// 检查第一次投标和投标红包是否同时开启
		RedPacket firstTenderRedPacket = redPacketDao.findObjByProperty(
				"serviceType", RedPacket.FIRST_TENDER);
		RedPacket tenderRedPacket = redPacketDao.findObjByProperty(
				"serviceType", RedPacket.TENDER);
		// 获取初秒还标之外的投标次数
		int count = borrowTenderDao.getUserTenderNum(user.getUserId(),
				Borrow.TYPE_SECOND);
		if (firstTenderRedPacket.getStatus() == 1 && count <= 1) {
			// 发放第一次投标红包
			doRedPacket(RedPacket.FIRST_TENDER, user, tender, null);
		} else if (tenderRedPacket.getStatus() == 1
				|| (firstTenderRedPacket.getStatus() == 1 && count > 1)) {
			// 发放投标红包
			doRedPacket(RedPacket.TENDER, user, tender, null);
		}
	}
	
	/**
	 * 投标生成加息券
	 * 
	 * @param user
	 * @param tender
	 */
	public void doTenderCoupon(User user, BorrowTender tender)
	{
		CouponCategory category = tender.getBorrow().getCouponCategory();
		if(category!=null)
		{
			double money = tender.getAccount();
			double couponRate = category.getRate();
			if(category.getInvestLimit5()!=null&&money>=category.getInvestLimit5())
			{
				couponRate += category.getLimitRate5();
			}else if(category.getInvestLimit4()!=null&&money>=category.getInvestLimit4())
			{
				couponRate += category.getLimitRate4();
			}else if(category.getInvestLimit3()!=null&&money>=category.getInvestLimit3())
			{
				couponRate += category.getLimitRate3();
			}else if(category.getInvestLimit2()!=null&&money>=category.getInvestLimit2())
			{
				couponRate += category.getLimitRate2();
			}else if(category.getInvestLimit1()!=null&&money>=category.getInvestLimit1())
			{
				couponRate += category.getLimitRate1();
			}
			if(couponRate>0)
			{
				Coupon coupon = new Coupon();
				coupon.setUserTo(user);
				coupon.setToMobile(user.getMobilePhone());
				coupon.setCategory(category);
				coupon.setStatus(0);
				coupon.setAddTime(new Date());
				coupon.setToRate(couponRate);
				coupon.setToRateAdjust(couponRate);
				couponDao.save(coupon);
			}
		}
	}
	@Override
	public void doRechargeRedPacket(User user, AccountRecharge recharge) {
	/*	// 检查第一次充值和充值红包是否同时开启
		RedPacket firstRechargeRedPacket = redPacketDao.findObjByProperty(
				"serviceType", RedPacket.FIRST_RECHARGE);
		RedPacket rechargeRedPacket = redPacketDao.findObjByProperty(
				"serviceType", RedPacket.RECHARGE);
		// 获取用户成功充值次数
		int count = accountRechargeDao.getRechargeCountByUser(user, 1);
		if (firstRechargeRedPacket.getStatus() == 1 && count <= 1) {
			doRedPacket(RedPacket.FIRST_RECHARGE, user, null, recharge);
		} else if (rechargeRedPacket.getStatus() == 1
				|| (firstRechargeRedPacket.getStatus() == 1 && count > 1)) {
			doRedPacket(RedPacket.RECHARGE, user, null, recharge);
		}*/
	}

	@Override
	public boolean checkIsCashRedPacket(String[] id) {
		for (String redId : id) {
			long redPacketId = NumberUtil.getLong(redId);
			UserRedPacket redPacket = userRedPacketDao.find(redPacketId);
			if (redPacket.getRedPacketType() != 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void doExchangeRedPacket(String[] id, User user) {
		double money = 0;
		for (String redId : id) {
			long redPacketId = NumberUtil.getLong(redId);
			UserRedPacket redPacket = userRedPacketDao.find(redPacketId);
			if (redPacket.getRedPacketType() == 1) {
				money += redPacket.getAmount();
				redPacket.setUsed(true);
				userRedPacketDao.update(redPacket);
			}
		}
		if (money > 0) {
			// 红包直接兑换为现金
			accountDao.update(money, money, 0, 0, user.getUserId());
			Global.setTransfer("money", money);
			AbstractExecuter executer = ExecuterHelper
					.doExecuter("userRedPacketExchangeExecuter");
			User toUser = userService.getUserById(1);
			executer.execute(money, user, toUser);
		}
	}

	@Override
	public void doBorrowRedPacket(User user, BorrowTender tender) {
		RedPacket redPacket = tender.getBorrow().getRedPacket();
		Date date = new Date();
		if (redPacket == null // 规则是否存在
				|| redPacket.getStatus() != 1 // 红包是否开启
				|| (redPacket.getStartTime() != null && date.before(redPacket
						.getStartTime())) // 是否还未到活动开始时间
				|| (redPacket.getEndTime() != null && date.after(redPacket
						.getEndTime())) // 是否已经超过活动结束时间
				|| (redPacket.getTotalNum() != 0 && redPacket.getUseNum() >= redPacket
						.getTotalNum())// 红包是否已经发放完毕
				|| (redPacket.getIsDelete() == 1) // 红包是否已被删除
		// || user.getUserCache().getUserType() == 2
		) {// 判断用书是否为借款人，只有投资人才会有红包
			return;
		}
		double redPacketMoney = getRedPacketMoney(tender.getAccount(),
				redPacket);
		Date expiredTime = null;
		// 如果红包有效天数大于0则设置到期时间，否则则代表长久有效
		if (redPacket.getDay() > 0) {
			expiredTime = DateUtil.rollDay(new Date(), redPacket.getDay());
		}
		UserRedPacket userRedPacket = new UserRedPacket(user, null, redPacket,
				redPacketMoney, expiredTime);

		// 如果红包金额大于0，则保存红包
		if (userRedPacket.getAmount() > 0) {
			userRedPacketDao.save(userRedPacket);

			// 更新红包发放个数
			redPacket.setUseNum(redPacket.getUseNum() + 1);
			redPacketDao.update(redPacket);
		}
	}

	@Override
	public void doPpfundRedPacket(User user, PpfundIn ppfundIn) {
		RedPacket redPacket = ppfundIn.getPpfund().getRedPacket();
		Date date = new Date();
		if (redPacket == null // 规则是否存在
				|| redPacket.getStatus() != 1 // 红包是否开启
				|| (redPacket.getStartTime() != null && date.before(redPacket
						.getStartTime())) // 是否还未到活动开始时间
				|| (redPacket.getEndTime() != null && date.after(redPacket
						.getEndTime())) // 是否已经超过活动结束时间
				|| (redPacket.getTotalNum() != 0 && redPacket.getUseNum() >= redPacket
						.getTotalNum())// 红包是否已经发放完毕
				|| (redPacket.getIsDelete() == 1) // 红包是否已被删除
		// || user.getUserCache().getUserType() == 2
		) {// 判断用书是否为借款人，只有投资人才会有红包
			return;
		}
		double redPacketMoney = getRedPacketMoney(ppfundIn.getAccount(),
				redPacket);
		Date expiredTime = null;
		// 如果红包有效天数大于0则设置到期时间，否则则代表长久有效
		if (redPacket.getDay() > 0) {
			expiredTime = DateUtil.rollDay(new Date(), redPacket.getDay());
		}
		UserRedPacket userRedPacket = new UserRedPacket(user, null, redPacket,
				redPacketMoney, expiredTime);

		// 如果红包金额大于0，则保存红包
		if (userRedPacket.getAmount() > 0) {
			userRedPacketDao.save(userRedPacket);

			// 更新红包发放个数
			redPacket.setUseNum(redPacket.getUseNum() + 1);
			redPacketDao.update(redPacket);
		}

	}

	@Override
	public UserRedPacket findUserRedPacketById(long redPacketId) {
		// TODO Auto-generated method stub
		return userRedPacketDao.find(redPacketId);
	}
}
