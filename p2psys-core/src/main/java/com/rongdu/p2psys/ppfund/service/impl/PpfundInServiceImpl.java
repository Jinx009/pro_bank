package com.rongdu.p2psys.ppfund.service.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.ProductsCost;
import com.rongdu.p2psys.account.service.AccountLogService;
import com.rongdu.p2psys.account.service.ProductsCostService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.constant.ProductTypeConstant;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.nb.invest.dao.FrozenProductDao;
import com.rongdu.p2psys.nb.invest.dao.FrozenUserDao;
import com.rongdu.p2psys.nb.ppfund.model.ExperienceGoldModel;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.domain.ProductType;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.product.service.ProductTypeService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.nb.wechat.util.WechatMessage;
import com.rongdu.p2psys.nb.wechat.util.WechatMessageData;
import com.rongdu.p2psys.ppfund.dao.PpfundDao;
import com.rongdu.p2psys.ppfund.dao.PpfundEarningsDao;
import com.rongdu.p2psys.ppfund.dao.PpfundInDao;
import com.rongdu.p2psys.ppfund.dao.PpfundOutDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.ppfund.domain.PpfundOut;
import com.rongdu.p2psys.ppfund.exception.PpfundException;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;
import com.rongdu.p2psys.user.service.UserVipService;

@Service("ppfundInService")
public class PpfundInServiceImpl implements PpfundInService {
	@Resource
	private AccountDao accountDao;
	@Resource
	private PpfundInDao ppfundInDao;
	@Resource
	private PpfundDao ppfundDao;
	@Resource
	private PpfundOutDao ppfundOutDao;
	@Resource
	private PpfundEarningsDao ppfundEarningsDao;
	@Resource
	private UserRedPacketDao userRedPacketDao;
	@Resource
	private UserService userService;
	@Resource
	private UserVipService userVipService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private ProductsCostService productsCostService;
	@Resource
	private NoticeService noticeService;
	@Resource
	private FrozenProductDao frozenProductDao;
	@Resource
	private FrozenUserDao frozenUserDao;
	@Resource
	private ExperienceGoldService theExperienceGoldService;
	@Resource
	private ProductTypeService productTypeService;
	@Resource
	private AccountLogService accountLogService;
	@Resource
	private ProductBasicService productBasicService;
	@Resource
	private WechatCacheService wechatCacheService;

	@Override
	public PpfundIn ppfundIn(PpfundInModel model) {
		Ppfund ppfund = model.getPpfund();
		ProductType pt = ppfund.getProductType();
		// 校验数据
		model.checkInModel(ppfund, pt.getTypeName());
		User user = model.getUser();
		ProductBasic productBasic = productBasicService.getProductBasicInfo(
				ppfund.getProductType().getId(), ppfund.getId());

		if (2046l == productBasic.getProductType().getId()) {
			ExperienceGoldModel experienceGoldModel = theExperienceGoldService
					.getEGByUserId(user.getUserId());

			if (null != experienceGoldModel) {
				throw new PpfundException("对不起，您不能投标!", 2);
			}

			if (!checkData(user)) {
				throw new PpfundException("对不起，您不能投标!");
			}
		}

		double money = model.getMoney();
		if (ppfund.getAccount() != 0) {
			// 计算剩余可投金额
			double surplusMoney = ppfund.getAccount() - ppfund.getAccountYes();
			if (surplusMoney < money) {
				money = surplusMoney;
			}
		}

		// 用户投资的金额
		Double ppMoney = money;

		// 验证规则要改
		double totalAccount = accountDao.getAccountUseMoney(user.getUserId());
		if (totalAccount < money) {
			frozenProductDao.unLockProjectMoney(ppfund.getId(),
					model.getProductBasicId(), user.getUserId());
			frozenUserDao.unLockUserMoney(ppfund.getId(),
					model.getProductBasicId(), user.getUserId());
			throw new PpfundException("对不起，您的账户余额不足", 2);
		}

		Long[] ids = model.getIds();
		double totalPacketMoney = userRedPacketDao
				.getTotalPacketMoneyByIds(ids);

		// 计算转出时间
		Date outTime = null;

		if (ppfund.getIsFixedTerm() == 1) {
			int addDay = 0;
			// 计息方式 t+0 0 t+1 1
			if (ppfund.getInterestWay() == 0) {
				addDay = ppfund.getTimeLimit() - 1;
			} else {
				addDay = ppfund.getTimeLimit();
			}
			outTime = DateUtil.valueOf(DateUtil.dateStr2(DateUtil.addDate(
					new Date(), addDay)) + " 23:59:59");
		}

		// 保存购买记录
		PpfundIn in = new PpfundIn(user, ppfund, new Integer(0),
				model.getMoney(), model.getMoney(), ppMoney, new Double(0.0),
				outTime, model.getProductType());
		in.setOriginalAccount(0.00);
		in.setOriginalInterest(0.00);
		ppfundInDao.save(in);

		// 修改ppfund募资金额
		ppfund.setAccountYes(ppfund.getAccountYes() + ppMoney);

		// 修改ppfund进度
		if (ppfund.getAccount() != 0) {
			ppfund.setScales(BigDecimalUtil.decimal(
					BigDecimalUtil.div(ppfund.getAccountYes(),
							ppfund.getAccount()) * 100, 2));
		}
		ppfundDao.update(ppfund);
		if (ids != null) {
			for (long id : ids) {
				UserRedPacket userRedPacket = userRedPacketDao.find(id);
				userRedPacket.setUsed(true);
				userRedPacket.setPpfundIn(in);
				userRedPacket.setUsedTime(new Date());
				userRedPacketDao.merge(userRedPacket);
			}
		}

		// 扣除用户资金,使用红包金额直接累加到投资人账户总额和可用余额
		Account account = accountDao.findObjByProperty("user", user);
		if (account.getUseMoney() >= money)// 优先使用当前用户账户中的资金
		{
			accountDao.modify(0, -money, 0, money, user.getUserId());
		} else {
			money = money - account.getUseMoney();
			accountDao.modify(0, -account.getUseMoney(), 0,
					account.getUseMoney(), user.getUserId());
			List<Account> accountList = accountDao
					.getGroupAccountListByUserId(user.getUserId());
			for (Account acc : accountList) {
				if (acc.getUser().getUserId() == user.getUserId()) // 当前账户已经扣除
				{
					continue;
				}
				if (acc.getUseMoney() >= money) {
					accountDao.modify(0, -money, 0, money, acc.getUser()
							.getUserId());
					break;
				} else {
					money = money - acc.getUseMoney();
					accountDao.modify(0, -acc.getUseMoney(), 0,
							acc.getUseMoney(), acc.getUser().getUserId());
				}

			}
		}
		Global.setTransfer("user", user);
		Global.setTransfer("ppfund", ppfund);
		Global.setTransfer("flag", productBasic.getProductTypeFlag());
		Global.setTransfer("money", money);
		AbstractExecuter executer = ExecuterHelper
				.doExecuter("ppfundTenderExecuter");
		executer.execute(money, user, new User(1));

		// 红包使用资金日志
		if (totalPacketMoney > 0) {
			accountDao.modify(totalPacketMoney, totalPacketMoney, 0, 0,
					user.getUserId());
			Global.setTransfer("user", user);
			Global.setTransfer("money", totalPacketMoney);
			Global.setTransfer("ppfund", ppfund);
			AbstractExecuter redPacketExecuter = ExecuterHelper
					.doExecuter("ppfundRedPacketSuccessExecuter");
			redPacketExecuter.execute(money, user, new User(1));
		}

		// 处理红包
		userRedPacketService.doPpfundRedPacket(user, in);
		// 处理用户VIP
		userVipService.doUserVip(money, user);

		if (2046l == productBasic.getProductType().getId()) {
			ExperienceGoldModel experienceGoldModel = theExperienceGoldService
					.getEGByUserId(user.getUserId());

			if (null == experienceGoldModel) {
				theExperienceGoldService.addPpfundExperienceGold(user, ppfund);
			}
		}

		// 解锁金额
		frozenProductDao.unLockProjectMoney(ppfund.getId(),
				model.getProductBasicId(), user.getUserId());
		frozenUserDao.unLockUserMoney(ppfund.getId(),
				model.getProductBasicId(), user.getUserId());

		List<User> list = new ArrayList<User>();

		list = userService.getByGroupId(user.getBindId());

		String userName = user.getUserName().substring(0, 3) + "****"
				+ user.getUserName().substring(7, user.getUserName().length());
		int timeLimit = ppfund.getTimeLimit();
		if (list.size() > 1) {
			for (int i = 0; i < list.size(); i++) {
				if (null != list.get(i).getWechatOpenId()
						&& !"".equals(list.get(i).getWechatOpenId())
						&& WechatMessageData.A_APP_ID.equals(list.get(i)
								.getWechatId())) {
					WechatMessage wechatMessage = new WechatMessage();
					wechatMessage.setAppId(WechatMessageData.A_APP_ID);
					wechatMessage.setAppSecret(WechatMessageData.A_APP_SECRET);
					wechatMessage.setType(WechatMessageData.Start_Rate);
					wechatMessage.setFirstData("尊敬的用户，您" + userName
							+ "的账号已投资成功。即日起息");
					if (0 == timeLimit) {
						wechatMessage.setProductDate("无固定期限 /开放式");
					} else {
						wechatMessage.setProductDate(timeLimit + "天");
					}
					wechatMessage.setProductName(ppfund.getName());
					wechatMessage.setProductProfit(getMoney(ppfund.getApr())
							+ "%");
					wechatMessage.setUrl(WechatMessageData.A_HOST
							+ "/nb/wechat/account/assets.html");
					wechatMessage.setOpenId(list.get(i).getWechatOpenId());
					wechatMessage
							.setRemark("投资当日计息。\\n如有疑问，请致电客服：400-6366-800！");
					System.out.println("开始发送微信投资成功模板:"
							+ wechatMessage.getOpenId() + "--"
							+ wechatMessage.getAppId());
					try {
						wechatCacheService.sendWechatMessage(wechatMessage);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return in;
	}

	@Override
	public PageDataList<PpfundInModel> list(long ppfundId, int page, int size) {
		QueryParam param = QueryParam.getInstance().addParam("ppfund.id",
				ppfundId);
		if (size != 0) {
			param.addPage(page, size);
		} else {
			param.addPage(page);
		}
		param.addOrder(OrderType.DESC, "addTime");
		PageDataList<PpfundIn> pageDataList = ppfundInDao.findPageList(param);
		List<PpfundInModel> list = new ArrayList<PpfundInModel>();
		PageDataList<PpfundInModel> pageDataList_ = new PageDataList<PpfundInModel>();
		for (PpfundIn ppfundIn : pageDataList.getList()) {
			PpfundInModel inModel = PpfundInModel.instance(ppfundIn);
			inModel.setPpfund(null);
			String userName = ppfundIn.getUser().getUserName();
			inModel.setUserName(userName.charAt(0) + "******"
					+ userName.charAt(userName.length() - 1));
			if (ppfundIn.getProductType().getTypeCategory()
					.equals(ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE)) {
				inModel.setTypeCode(ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE);
				inModel.setInterestMoney(ppfundIn.getInterestAmount());
			}
			list.add(inModel);
		}
		pageDataList_.setList(list);
		pageDataList_.setPage(pageDataList.getPage());
		return pageDataList_;
	}

	@Override
	public void doPpfundIn() {
		// 执行到期转出记录
		List<PpfundIn> doOutList = ppfundInDao.getExpireList();
		if (doOutList != null && doOutList.size() > 0) {
			for (PpfundIn ppfundIn : doOutList) {
				User outUser = ppfundIn.getUser();
				Ppfund ppfund = ppfundIn.getPpfund();
				ppfundIn.setIsOut(1);
				ppfundInDao.update(ppfundIn);

				PpfundOut out = new PpfundOut(outUser, ppfundIn.getPpfund(),
						ppfundIn, ppfundIn.getInterestAmount());
				ppfundOutDao.save(out);

				Global.setTransfer("ppfund", ppfund);
				Global.setTransfer("user", outUser);
				ProductBasic productBasic = productBasicService
						.getProductBasicInfo(ppfund.getProductType().getId(),
								ppfund.getId());
				Global.setTransfer("flag", productBasic.getProductTypeFlag());
				// 处理用户资金
				// 收回本金
				accountDao.modify(0, ppfundIn.getAccount(), 0,
						-ppfundIn.getAccount(), outUser.getUserId());
				Global.setTransfer("money", ppfundIn.getAccount());
				AbstractExecuter capitalExcuter = ExecuterHelper
						.doExecuter("ppfundOutCapitalExecuter");
				User toUser = userService.getUserById(1);
				capitalExcuter.execute(ppfundIn.getAccount(), outUser, toUser);

				// 收回利息
				double interest = ppfundIn.getInterest();
				if (interest > 0) {
					accountDao.modify(0, interest, 0, -interest,
							outUser.getUserId());
					Global.setTransfer("money", interest);
					AbstractExecuter interestExecuter = ExecuterHelper
							.doExecuter("ppfundOutInterestExecuter");
					interestExecuter.execute(interest, outUser, toUser);
				}

				if (ppfundIn.getProductType() != null
						&& ppfundIn
								.getProductType()
								.getTypeName()
								.equals(ProductTypeConstant.PRODUCT_TYPE__EXPERIENCE))// 体验标
				{
					ExperienceGoldModel em = theExperienceGoldService
							.getEGByUserId(outUser.getUserId());
					em.setStatus(1);// 失效
					theExperienceGoldService.updateExperienceGold(em);

				}
				// 扣除利息管理费
				double borrow_fee = Global.getDouble("borrow_fee");
				double manageFee = BigDecimalUtil.round(BigDecimalUtil.mul(
						interest, borrow_fee));
				if (manageFee > 0) {
					accountDao.modify(-manageFee, -manageFee, 0, 0,
							outUser.getUserId());
					Global.setTransfer("money", manageFee);
					AbstractExecuter manageFeeExecuter = ExecuterHelper
							.doExecuter("ppfundOutManageFeeExecuter");
					manageFeeExecuter.execute(manageFee, outUser, toUser);
				}

				// 计算投资天数
				// int timeDay =
				// DateUtil.daysBetween(ppfundIn.getAddTime(),ppfundIn.getOutTime());
				int timeDay = ppfund.getTimeLimit();
				if (ppfund.getInterestWay() == 0) {
					timeDay = DateUtil.daysBetween(ppfundIn.getAddTime(),
							ppfundIn.getOutTime()) + 1;
				} else {
					timeDay = DateUtil.daysBetween(ppfundIn.getAddTime(),
							ppfundIn.getOutTime());
				}
				// 计算借款管理费
				double managePpfundFee = BigDecimalUtil.mul(
						ppfundIn.getAccount(), ppfund.getManageRate() * timeDay
								/ (100 * 365));
				// 计算风险备用金
				double riskReserveFee = BigDecimalUtil.mul(
						ppfundIn.getAccount(), ppfund.getRiskReserveRate()
								* timeDay / (100 * 365));

				ProductsCost cost = new ProductsCost(ppfund.getName(),
						ppfund.getPidNo(), ppfund, managePpfundFee,
						riskReserveFee);
				productsCostService.save(cost);
			}
		}

		/************* 分界线：执行复利记录 **************/
		/************* 暂时不需要，注释 **************/
	}

	@Override
	public PageDataList<PpfundInModel> pageList(PpfundInModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(),
				model.getRows());
		if (StringUtil.isNotBlank(model.getSearchName())) {
			SearchFilter orFilter1 = new SearchFilter("user.userName",
					Operators.LIKE, model.getSearchName());
			SearchFilter orFilter2 = new SearchFilter("user.realName",
					Operators.LIKE, model.getSearchName());
			SearchFilter orFilter3 = new SearchFilter("ppfund.name",
					Operators.LIKE, model.getSearchName());
			param.addOrFilter(orFilter1, orFilter2, orFilter3);
		} else {
			if (model.getPpfundName() != null) {
				param.addParam("ppfund.name", model.getPpfundName());
			}
			if (StringUtil.isNotBlank(model.getUserName())) {
				param.addParam("user.userName", model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getRealName())) {
				param.addParam("user.realName", model.getRealName());
			}
			if (StringUtil.isNotBlank(model.getPidNo())) {
				param.addParam("ppfund.pidNo", model.getPidNo());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime());
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime());
				param.addParam("addTime", Operators.LTE, end);
			}

			if (StringUtil.isNotBlank(model.getStartOutTime())) {
				Date start = DateUtil.valueOf(model.getStartOutTime());
				param.addParam("outTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndOutTime())) {
				Date end = DateUtil.valueOf(model.getEndOutTime());
				param.addParam("outTime", Operators.LTE, end);
			}
		}
		PageDataList<PpfundIn> pageDataList = ppfundInDao.findPageList(param);
		PageDataList<PpfundInModel> pageDataList_ = new PageDataList<PpfundInModel>();
		List<PpfundInModel> list = new ArrayList<PpfundInModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList() != null && pageDataList.getList().size() > 0) {
			for (PpfundIn ppfundIn : pageDataList.getList()) {
				PpfundInModel inModel = PpfundInModel.instance(ppfundIn);
				// 获取昨日收益
				double lastInterest = ppfundEarningsDao
						.getLastEarningsInterest(ppfundIn.getId());
				inModel.setLastInterest(lastInterest);
				inModel.setPpfundId(ppfundIn.getPpfund().getId());
				inModel.setPpfundName(ppfundIn.getPpfund().getName());
				inModel.setPidNo(ppfundIn.getPpfund().getPidNo());
				inModel.setRealName(ppfundIn.getUser().getRealName());
				inModel.setUserName(ppfundIn.getUser().getUserName());
				inModel.setPpfundApr(ppfundIn.getPpfund().getApr());
				inModel.setIsFixedTerm(ppfundIn.getPpfund().getIsFixedTerm());
				if (ppfundIn
						.getProductType()
						.getTypeCategory()
						.equals(ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE)) {
					inModel.setTypeCode(ProductTypeConstant.PRODUCT_CATEGORY__EXPERIENCE);
					inModel.setInterestMoney(ppfundIn.getInterestAmount());
				}
				list.add(inModel);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public PageDataList<PpfundInModel> pageList(long ppfundId, int page,
			int size) {
		QueryParam param = QueryParam.getInstance();
		param.addParam("ppfund.id", ppfundId);
		param.addOrder(OrderType.DESC, "addTime");
		if (size != 0) {
			param.addPage(page, size);
		} else {
			param.addPage(page);
		}
		PageDataList<PpfundIn> pageDataList = ppfundInDao.findPageList(param);
		PageDataList<PpfundInModel> pageDataList_ = new PageDataList<PpfundInModel>();
		List<PpfundInModel> list = new ArrayList<PpfundInModel>();
		pageDataList_.setPage(pageDataList.getPage());
		if (pageDataList.getList().size() > 0) {
			for (int i = 0; i < pageDataList.getList().size(); i++) {
				PpfundIn in = pageDataList.getList().get(i);
				PpfundInModel model = PpfundInModel.instance(in);
				String username = in.getUser().getUserName();
				model.setUser(null);
				model.setPpfund(null);
				if (size == 0) {
					model.setUserName(username.charAt(0) + "******"
							+ username.charAt(username.length() - 1));
				} else {
					model.setUserName(username);
				}
				list.add(model);
			}
		}
		pageDataList_.setList(list);
		return pageDataList_;
	}

	@Override
	public PpfundIn getById(long id) {
		return ppfundInDao.find(id);
	}

	@Override
	public void ppfundIdEdit(PpfundIn in) {
		ppfundInDao.update(in);
	}

	@Override
	public double getCollectionCapitalByUser(User user) {
		return ppfundInDao.getCollectionCapitalByUser(user);
	}

	@Override
	public double getCollectionInterestByUser(User user) {
		return ppfundInDao.getCollectionInterestByUser(user);
	}

	@Override
	public double getMostAccountTotalByUserAndPpfund(User user, Ppfund ppfund) {

		return ppfundInDao.getMostAccountTotalByUserAndPpfund(user, ppfund);
	}

	@Override
	public void doPpfundInOutTime(Ppfund ppfund) {
		// 查出未设置转出时间的用户
		List<PpfundIn> list = ppfundInDao.getNoOutTimePpfundIn(ppfund);
		if (list != null && list.size() > 0) {
			for (PpfundIn ppfundIn : list) {
				Date outTime = DateUtil.valueOf(DateUtil.dateStr2(DateUtil
						.addDate(new Date(), ppfund.getCycle())) + " 23:59:59");
				ppfundIn.setOutTime(outTime);
				ppfundInDao.update(ppfundIn);
			}
		}
	}

	@Override
	public double outInterest() {

		return ppfundInDao.outInterest();
	}

	@Override
	public PpfundIn getLastInByPpfundId(long id) {

		return ppfundInDao.getLastInByPpfundId(id);
	}

	@Override
	public PpfundOut doOut(PpfundOut out) {
		PpfundIn in = out.getPpfundIn();
		// 检测是否已经转出
		if (in.getIsOut() == 1) {
			throw new PpfundException("已经转出，请勿重复操作", 1);
		}
		double sumMoney = ppfundOutDao.dayOutMoney(in.getUser());
		double ppfundOutMoney = ppfundOutDao.ppfundOutMoney(in);
		if((sumMoney + out.getMoney())>200000)
		{
			throw new PpfundException("日累计转出金额不能大于20万", 1);
		}
		double redeemMoney = out.getMoney();
		double redeemInterest = 0;
		Ppfund ppfund = in.getPpfund();
		double originalInterest = 0;
		if(in.getOriginalInterest()!=null)
		{
			originalInterest = in.getOriginalInterest();
		}
		if(in.getOriginalAccount()!=null&&(redeemMoney+ppfundOutMoney)==in.getOriginalAccount())//全部转出更改状态
		{
			redeemInterest =  in.getInterest();
			double originalAccount = in.getOriginalAccount();
			originalInterest += redeemInterest;
			in.setAccount(originalAccount);
			in.setInterestAmount(originalAccount);
			in.setMoney(originalAccount);
			in.setInterest(originalInterest);
			in.setOriginalInterest(originalInterest);
			in.setIsOut(1);
			in.setOutTime(new Date());
		}else{
			double account = in.getAccount();
			double interest = in.getInterest();
			double leftAccount = account - redeemMoney;
			redeemInterest = BigDecimalUtil.mul(interest,redeemMoney/account);
			originalInterest += redeemInterest;
			in.setAccount(leftAccount);
			in.setInterestAmount(leftAccount);
			in.setMoney(leftAccount);
			in.setInterest(interest-redeemInterest);
			in.setOriginalInterest(originalInterest);
			if(in.getOriginalAccount()==null||in.getOriginalAccount()==0)
			{
				in.setOriginalAccount(account);
			}
		}
		ppfundInDao.update(in);

//		PpfundOut out = new PpfundOut(outUser, in.getPpfund(), in,
//				out.);
		User outUser = in.getUser();
		out.setUser(outUser);
		out.setAddTime(new Date());
		out.setAddIp(Global.getIP());

		ppfundOutDao.save(out);

		Global.setTransfer("ppfund", ppfund);
		Global.setTransfer("user", outUser);
		ProductBasic productBasic = productBasicService.getProductBasicInfo(
				ppfund.getProductType().getId(), ppfund.getId());
		Global.setTransfer("flag", productBasic.getProductTypeFlag());

		// 处理用户资金
		// 收回本金
		accountDao.modify(0, redeemMoney, 0, -redeemMoney,
				outUser.getUserId());
		Global.setTransfer("money", redeemMoney);
		AbstractExecuter capitalExcuter = ExecuterHelper
				.doExecuter("ppfundOutCapitalExecuter");
		User toUser = userService.getUserById(1);
		capitalExcuter.execute(redeemMoney, outUser, toUser);

		// 收回利息
		double interest = redeemInterest ;
		if (interest > 0) {
			accountDao.modify(0, interest, 0, -interest, outUser.getUserId());
			Global.setTransfer("money", interest);
			AbstractExecuter interestExecuter = ExecuterHelper
					.doExecuter("ppfundOutInterestExecuter");
			interestExecuter.execute(interest, outUser, toUser);
		}

		// 扣除利息管理费
		double borrow_fee = Global.getDouble("borrow_fee");
		double manageFee = BigDecimalUtil.round(BigDecimalUtil.mul(interest,
				borrow_fee));
		if (manageFee > 0) {
			accountDao
					.modify(-manageFee, -manageFee, 0, 0, outUser.getUserId());
			Global.setTransfer("money", manageFee);
			AbstractExecuter manageFeeExecuter = ExecuterHelper
					.doExecuter("ppfundOutManageFeeExecuter");
			manageFeeExecuter.execute(manageFee, outUser, toUser);
		}

		// 计算投资天数
		// int timeDay = DateUtil.daysBetween(in.getAddTime(), in.getOutTime());
		int timeDay = ppfund.getTimeLimit();
		if (ppfund.getInterestWay() == 0) {
			timeDay = DateUtil.daysBetween(in.getAddTime(), new Date()) + 1;
		} else {
			timeDay = DateUtil.daysBetween(in.getAddTime(), new Date());
		}
		// 计算借款管理费
		double managePpfundFee = BigDecimalUtil.mul(redeemMoney,
				ppfund.getManageRate() * timeDay / (100 * 365));
		// 计算风险备用金
		double riskReserveFee = BigDecimalUtil.mul(redeemMoney,
				ppfund.getRiskReserveRate() * timeDay / (100 * 365));

		ProductsCost cost = new ProductsCost(ppfund.getName(),
				ppfund.getPidNo(), ppfund, managePpfundFee, riskReserveFee);
		productsCostService.save(cost);

		// 转出大额资金监管，如果转出金额大于资金监管金额，则发送短信提醒平台指定用户
		double supervisionMoney = BigDecimalUtil.add(redeemMoney,redeemInterest);
		if (supervisionMoney >= Global.getDouble("supervision_money")) {
			Global.setTransfer("user", outUser);
			Global.setTransfer("money", supervisionMoney);
			NoticeType smsType = Global.getNoticeType(
					NoticeConstant.PPFUND_SUPERVISION_NOTICE,
					NoticeConstant.NOTICE_SMS);
			String receiveAddr = Global.getValue("admin_receive_phone");
			if (smsType.getSend() == 1 && StringUtil.isNotBlank(receiveAddr)) {
				Map<String, Object> sendData = new HashMap<String, Object>();
				sendData.put("user", outUser);
				sendData.put("addTime", new Date());
				sendData.put("money", supervisionMoney);
				Notice sms = new Notice();
				sms.setType(NoticeConstant.NOTICE_SMS + "");
				sms.setReceiveAddr(receiveAddr);
				sms.setNid(NoticeConstant.PPFUND_SUPERVISION_NOTICE);
				sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(),
						sendData));
				noticeService.sendNotice(sms);
			}
		}
		return out;
	}

	public boolean checkData(User user) {
		Date userDate = user.getAddTime();
		String myString = ConstantUtil.USER_ADD_TIME;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		Date d;
		boolean flag = false;
		try {
			d = sdf.parse(myString);
			flag = d.before(userDate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	@Override
	public double getOutAmountByDate(String date) {

		return ppfundInDao.getOutAmountByDate(date);
	}

	@Override
	public double getInAmountByDate(String date) {

		return ppfundInDao.getInAmountByDate(date);
	}

	@Override
	public int getUserPpfundInOrder(long ppfundInId, long ppfundId) {
		// TODO Auto-generated method stub
		return ppfundDao.getUserPpfundInOrder(ppfundInId, ppfundId);
	}

	public static String getMoney(double money) {
		NumberFormat nf = new DecimalFormat(",###.##");

		return nf.format(money);
	}

	@Override
	public double getAllMoney() {
		return ppfundDao.getAllMoney();
	}

	@Override
	public List<PpfundIn> getPpfundInListByPpfundId(Long ppfundId) {
		return ppfundInDao.findByProperty("ppfund.id", ppfundId);
	}

}
