package com.rongdu.p2psys.borrow.model.worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.bond.dao.BondCollectionDao;
import com.rongdu.p2psys.bond.dao.BondTenderDao;
import com.rongdu.p2psys.bond.domain.Bond;
import com.rongdu.p2psys.bond.domain.BondCollection;
import com.rongdu.p2psys.bond.domain.BondTender;
import com.rongdu.p2psys.borrow.dao.BorrowCollectionDao;
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
import com.rongdu.p2psys.borrow.model.interest.EachPlan;
import com.rongdu.p2psys.borrow.model.interest.InstallmentRepaymentCalculator;
import com.rongdu.p2psys.borrow.model.interest.InterestCalculator;
import com.rongdu.p2psys.borrow.model.interest.MiddleDayInterestCalculator;
import com.rongdu.p2psys.borrow.model.interest.MonthlyInterestCalculator;
import com.rongdu.p2psys.borrow.model.interest.OnetimeRepaymentCalculator;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.constant.enums.EnumRuleNid;
import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.rule.AutoTenderConfRuleCheck;
import com.rongdu.p2psys.core.rule.BorrowAprLimitRuleCheck;
import com.rongdu.p2psys.core.rule.BorrowManageFeeRuleCheck;
import com.rongdu.p2psys.core.service.VerifyLogService;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.nb.account.dao.AccountLogDao;
import com.rongdu.p2psys.nb.product.domain.ProductBasic;
import com.rongdu.p2psys.nb.product.service.ProductBasicService;
import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitDao;
import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitRecordDao;
import com.rongdu.p2psys.nb.recommend.dao.RecommendRecordDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfit;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfitRecord;
import com.rongdu.p2psys.nb.recommend.domain.RecommendRecord;
import com.rongdu.p2psys.nb.user.dao.UserDao;
import com.rongdu.p2psys.nb.user.dao.UserInviteDao;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.nb.wechat.util.WechatMessage;
import com.rongdu.p2psys.nb.wechat.util.WechatMessageData;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderInvestSuccessLog;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.tpp.domain.TppIpsPay;
import com.rongdu.p2psys.tpp.ips.dao.TppIpsPayDao;
import com.rongdu.p2psys.tpp.ips.service.IpsService;
import com.rongdu.p2psys.user.dao.UserCacheDao;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.dao.UserRedPacketDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserInvite;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.model.UserCacheModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserVipService;

public class BaseBorrowWorker implements BorrowWorker {

	private final static Logger logger = Logger
			.getLogger(BaseBorrowWorker.class);

	protected Borrow data;

	protected BorrowConfig config;

	protected boolean flag;

	protected List<BorrowRepayment> repaymentList;

	protected List<BorrowCollection> collectionList;

	/**
	 * 添加构造函数用来判断是否是满标复审
	 * 
	 * @param data
	 * @param config
	 * @param flag
	 */
	public BaseBorrowWorker(Borrow data, BorrowConfig config, boolean flag) {
		if (data != null && data.getId() > 0) {
			BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
			Borrow borrow = borrowDao.find(data.getId());
			this.data = data;
			this.data.setUser(borrow.getUser());
			this.data.setVouchFirm(borrow.getVouchFirm());
		} else {
			this.data = data;
		}
		this.flag = flag;
		this.config = config;
	}

	public BaseBorrowWorker(Borrow data, BorrowConfig config) {
		if (data != null && data.getId() > 0) {
			BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
			Borrow borrow = borrowDao.find(data.getId());
			this.data = data;
			this.data.setUser(borrow.getUser());
			this.data.setVouchFirm(borrow.getVouchFirm());
		} else {
			this.data = data;
		}
		this.config = config;
	}

	@Override
	public Borrow prototype() {
		return data;
	}

	@Override
	public boolean checkModelData() {
		// 借款期限校验
		if (data.getTimeLimit() <= 0) {
			throw new BorrowException("需填写正确的借款期限！",
					BussinessException.TYPE_JSON);
		}

		// 中期还款天数校验
		if (data.getStyle() == 4 || data.getStyle() == 5) {
			if (data.getMiddleDay() <= 0 || data.getPeriod() <= 0) {
				throw new BorrowException("需填写正确的中期还款信息！",
						BussinessException.TYPE_JSON);
			}
			if (data.getTimeLimit() <= data.getMiddleDay()) {
				throw new BorrowException("中期还款天数应小于借款期限！",
						BussinessException.TYPE_JSON);
			}
		}

		// 收益率校验
		if (data.getType() == Borrow.TYPE_ENTRUST) {
			if (data.getExpectedLow() < 1 || data.getExpectedUp() < 1) {
				throw new BorrowException("需填写正确的最低收益率和最高收益率！",
						BussinessException.TYPE_JSON);
			}
			if (data.getExpectedLow() >= data.getExpectedUp()) {
				throw new BorrowException("最低收益率应小于最高收益率！",
						BussinessException.TYPE_JSON);
			}
		} else {
			if (data.getApr() < 1) {
				throw new BorrowException("最低收益率不能低于1%",
						BussinessException.TYPE_JSON);
			}
		}

		// 收益率上限规则
		int timeLimit = data.getTimeLimit();
		double apr = data.getApr();
		if (timeLimit <= 6 && apr > 22.4) {
			throw new BorrowException("六个月份以内(含六个月)的收益率不能高于22.4%",
					BussinessException.TYPE_JSON);
		}
		if (timeLimit <= 12 && timeLimit > 6 && apr > 24) {
			throw new BorrowException("六个月至一年(含一年)的收益率不能高于24%",
					BussinessException.TYPE_JSON);
		}
		if (timeLimit <= 36 && timeLimit > 12 && apr > 24.6) {
			throw new BorrowException("一年至三年(含三年)的收益率不能高于24.6%",
					BussinessException.TYPE_JSON);
		}
		if (timeLimit <= 60 && timeLimit > 36 && apr > 25.6) {
			throw new BorrowException("三年至五年(含五年)的收益率不能高于25.6%",
					BussinessException.TYPE_JSON);
		}
		if (timeLimit > 60 && apr > 26.2) {
			throw new BorrowException("五年以上的收益率不能高于26.2%",
					BussinessException.TYPE_JSON);
		}

		return true;
	}

	@Override
	public void setBorrowField() {
		data.setRepaymentAccount(interestCalculator().repayTotal());
		data.setUser(new User(1L));
		data.setAddIp(Global.getIP());
		data.setAddTime(new Date());
	}

	@Override
	public void stopBorrow() {
		data.setOldAccount(data.getAccount());
		data.setAccount(data.getAccountYes());
		data.setScales(100);
		data.setRepaymentAccount(interestCalculator().repayTotal());
	}

	@Override
	public void cancelBorrow() {
		data.setStatus(Borrow.STATUS_MANAGER_CANCEL);
		data.setRepaymentYesAccount(0d);
		data.setRepaymentYesInterest(0d);
	}

	// TODO PC2.0

	@Override
	public void skipReview() {
		if (review()) {
			data.setStatus(Borrow.STATUS_RECHECK_PASS);
			VerifyLog verifyLog = new VerifyLog(new Operator(1L), "borrow",
					data.getId(), 2, 1, "跳过复审");
			VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil
					.getBean("verifyLogDao");
			verifyLogDao.save(verifyLog);
		}
	}

	public boolean review() {
		if (config == null || config.isReview() == false) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void revokeBorrow() {
		if (this.data.getRepaymentAccount() == -1) {
			// 前台撤标
			if (this.data.getStatus() != 0) {
				throw new BorrowException("借款标的状态不允许撤回", 1);
			}
		} else {
			// 后台撤标
			if (this.data.getStatus() != 1 && this.data.getStatus() != 0
					&& this.data.getStatus() != -2) {
				throw new BorrowException("借款标的状态不允许撤回", 1);
			}
		}
	}

	/**
	 * 秒标初审不通过解冻资金
	 */
	@Override
	public void secondUnVerifyFreeze() {

	}

	/**
	 * 计算借款标的利息
	 */
	@Override
	public double calculateInterest() {
		InterestCalculator ic = interestCalculator();
		double interest = ic.repayTotal() - data.getAccount();
		return interest;
	}

	/**
	 * 待收利息
	 */
	@Override
	public double calculateInterest(double validAccount) {
		InterestCalculator ic = interestCalculator(validAccount);
		double interest = 0;
		interest = ic.repayTotal() - validAccount;
		return interest;
	}

	@Override
	public InterestCalculator interestCalculator() {
		return interestCalculator(data.getAccount());
	}

	@Override
	public InterestCalculator interestCalculator(double validAccount) {
		InterestCalculator ic = null;
		double apr = data.getApr() / 100;
		Date date = null;
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil
				.getBean("verifyLogDao");
		VerifyLog verifyLog = null;
		if (flag) {
			verifyLog = verifyLogDao.findByType(data.getId(), "borrow", 2); // 2:复审
		} else {
			verifyLog = verifyLogDao.findByType(data.getId(), "borrow", 1); // 1:初审
		}
		if (verifyLog != null && verifyLog.getTime() != null) {
			date = verifyLog.getTime();
		} else {
			date = new Date();
		}
		if (data.getBorrowTimeType() == 1
				&& data.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT) { // 天标，且一次性还款
			ic = new OnetimeRepaymentCalculator(validAccount, apr, date, 1, 0);
		} else if (data.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT) { // 一次性还款
			ic = new OnetimeRepaymentCalculator(validAccount, apr, date,
					data.getTimeLimit(), 0);
		} else if (data.getStyle() == Borrow.STYLE_MONTHLY_INTEREST) { // 每月还息到期还本
			ic = new MonthlyInterestCalculator(validAccount, apr, date,
					data.getTimeLimit(), flag, 0);
		} else if (data.getStyle() == Borrow.STYLE_INSTALLMENT_REPAYMENT) { // 等额本息
			ic = new InstallmentRepaymentCalculator(validAccount, apr, date,
					data.getTimeLimit(), 0);
		} else if (data.getStyle() == Borrow.STYLE_MIDDLEDAY_INTEREST) { // 中期
			ic = new MiddleDayInterestCalculator(validAccount, apr, date, 0,
					data.getPeriod(), data.getMiddleDay());
		}
		if (data.getBorrowTimeType() == 1) {
			ic.calculator(data.getTimeLimit());
		} else {
			ic.calculator();
		}
		return ic;
	}

	/**
	 * 计算借款标的手续费
	 */
	@Override
	public double calculateBorrowFee() {
		return 0;
	}

	/**
	 * 计算奖励资金
	 */
	@Override
	public double calculateBorrowAward() {
		BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
		Borrow borrow = borrowDao.find(data.getId());
		if (borrow != null) {
			if (borrow.getAward() == 1) {// 按投标金额比例
				return borrow.getPartAccount() / 100 * data.getAccount();
			} else if (borrow.getAward() == 2) {// 按固定金额分摊奖励
				return borrow.getFunds();
			}
		}
		return 0.0;
	}

	@Override
	public Date getRepayTime(int period) {
		VerifyLogService verifyLogService = (VerifyLogService) BeanUtil
				.getBean("verifyLogService");
		VerifyLog verifyLog = verifyLogService.findByType(data.getId(),
				"borrow", 2);
		Date fullVerifyTime = new Date();
		if (verifyLog != null)
			fullVerifyTime = verifyLog.getTime();
		Date repayDate = DateUtil.getLastSecIntegralTime(fullVerifyTime);
		if (data.getType() == Borrow.TYPE_SECOND) {
			return repayDate;
		} else if (data.getBorrowTimeType() == 1) {
			if(Borrow.STYLE_MIDDLEDAY_INTEREST==data.getStyle())
			{
				if(period==data.getPeriod()-1)//最后一期
				{
					repayDate = DateUtil.addDate(DateUtil.rollDay(fullVerifyTime,
							data.getTimeLimit()),-1);
				}else//不是最后一期
				{
					repayDate = DateUtil.addDate(DateUtil.rollDay(fullVerifyTime,
							(period+1)*data.getMiddleDay()),-1);
				}
			}else
			{
				repayDate = DateUtil.addDate(
						DateUtil.rollDay(repayDate, data.getTimeLimit()), -1);
			}
			return repayDate;
		} else {
			if (data.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT) {// 一次性还款
				repayDate = DateUtil.addDate(
						DateUtil.rollMon(repayDate, data.getTimeLimit()), -1);
			} else if ("5".equals(data.getStyle())) {// 满标还息每月还息到期还本
				if (period == 0) {
					repayDate = DateUtil.rollMinute(new Date(), 5);// 5分钟后还款
				} else {
					repayDate = DateUtil.addDate(
							DateUtil.rollMon(repayDate, period), -1);
				}
			} else {
				repayDate = DateUtil.addDate(
						DateUtil.rollMon(repayDate, period + 1), -1);
			}
			return repayDate;
		}
	}

	@Override
	public Date getFlowRepayTime(int period) {
		VerifyLogService verifyLogService = (VerifyLogService) BeanUtil
				.getBean("verifyLogService");
		VerifyLog verifyLog = verifyLogService.findByType(data.getId(),
				"borrow", 2);
		Date fullVerifyTime = new Date();
		if (verifyLog != null)
			fullVerifyTime = verifyLog.getTime();
		Date repayDate = DateUtil.getLastSecIntegralTime(fullVerifyTime);
		if (data.getType() == Borrow.TYPE_SECOND) {
			return repayDate;
		} else if (data.getBorrowTimeType() == 1) {
			repayDate = DateUtil.rollDay(repayDate, data.getTimeLimit());
			return repayDate;
		} else {
			// 一次性还款
			if (data.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT) {
				repayDate = DateUtil.rollMon(repayDate, data.getTimeLimit());
			} else {
				repayDate = DateUtil.rollMon(repayDate, period + 1);
			}
			return repayDate;
		}
	}

	private boolean toBool(String identify) {
		if (config == null || config.getIdentify() == null) {
			throw new BorrowException("该类借款标的配置参数不对！");
		}
		int i1 = Integer.parseInt(identify, 2);
		int i2 = Integer.parseInt(config.getIdentify(), 2);
		int ret = i1 & i2;
		if (ret > 0)
			return true;
		return false;
	}

	@Override
	public boolean isNeedRealName() {
		return this.toBool("100000");
	}

	@Override
	public boolean isNeedEmail() {
		return this.toBool("001000");
	}

	@Override
	public boolean isNeedPhone() {
		return this.toBool("000100");
	}

	/** 限制借款金额 **/
	@SuppressWarnings("unused")
	private void limitAccount() {
		if (config != null) {
			double lowest_account = 0;
			if (config.getLowestAccount() != 0) {
				lowest_account = config.getLowestAccount();
			} else {
				lowest_account = 500;
			}
			double most_account = 0;
			if (config.getMostAccount() != 0) {
				most_account = config.getMostAccount();
			} else {
				most_account = 5000000;
			}
			double account = data.getAccount();
			if (account < lowest_account) {
				throw new BorrowException("借款金额不能低于" + lowest_account + "元", 1);
			}
			if (account > most_account) {
				throw new BorrowException("借款金额不能高于" + most_account + "元", 1);
			}
		}
		double most_account = BigDecimalUtil.round(data.getMostAccount());
		double most_single_limit = data.getMostSingleLimit();
		if (most_account < most_single_limit && most_account > 0) {
			throw new BorrowException("最高单笔限额不能高于最高累计限额！！！", 1);
		}
	}

	@Override
	public boolean isLastPeriod(int period) {
		if (this.data.getType() == Borrow.TYPE_SECOND
				|| this.data.getBorrowTimeType() == 1
				|| this.data.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT) { // 修改判断为整数是否相等
			return true;
			// } else if (this.data.getStyle() == 5) { // 满标还息每月还息到期还本
			// return (this.data.getTimeLimit() == period);
		} else {
			return (this.data.getTimeLimit() == (period + 1));
		}
	}

	/**
	 * 投标奖励
	 */
	@Override
	public double calculateAward(double account) {
		double awardValue = 0;
		BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
		Borrow borrow = borrowDao.find(data.getId());
		if (borrow != null) {
			if (borrow.getAward() == 1) {
				awardValue = account * borrow.getPartAccount() / 100;
			} else if (borrow.getAward() == 2) {
				awardValue = borrow.getFunds() * account / borrow.getAccount();
			}
		}
		// BorrowAttached lateAward =
		// borrowAttachedDao.findByAttribute(data.getId(), "lateAward");
		// if (lateAward != null && StringUtil.isNotBlank(lateAward.getValue()))
		// {
		// awardValue += Double.parseDouble(lateAward.getValue());
		// }
		return awardValue;
	}

	@Override
	public double validAccount(BorrowTender tender) {
		double validAccount = 0.0;
		double tenderAccount = tender.getMoney();
		double account_val = data.getAccount();
		double account_yes_val = data.getAccountYes();
		if (tenderAccount + account_yes_val >= account_val) {
			validAccount = account_val - account_yes_val;
			skipReview();
		} else {
			validAccount = tenderAccount;
		}

		double lowestSingleLimit = data.getLowestSingleLimit();
		double mostSingleLimit = data.getMostSingleLimit();
		if (validAccount < lowestSingleLimit && lowestSingleLimit > 0) {
			if (account_val - account_yes_val < lowestSingleLimit) {
				if (account_val - account_yes_val < validAccount) {
					validAccount = account_val - account_yes_val;
				}
			} else {
				throw new BorrowException("投标金额不能少于单笔最小限额", 1);
			}
		}
		if (mostSingleLimit > 0 && validAccount > mostSingleLimit) {
			validAccount = mostSingleLimit;
		}
		return validAccount;
	}

	public BorrowModel checkTenderBefore(BorrowModel model, double tenderMoney,
			User user, int flow_count) {
		AccountDao accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		UserCacheDao userCacheDao = (UserCacheDao) BeanUtil
				.getBean("userCacheDao");
		UserIdentifyDao userIdentifyDao = (UserIdentifyDao) BeanUtil
				.getBean("userIdentifyDao");
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil
				.getBean("verifyLogDao");
		// 获取投标人的账户信息
		Account act = accountDao.findObjByProperty("user.userId",
				user.getUserId());
		if (tenderMoney > act.getUseMoney()) {
			model.setMoney(act.getUseMoney());
		} else {
			model.setMoney(tenderMoney);
		}

		UserCache userCache = userCacheDao.findObjByProperty("user.userId",
				user.getUserId());
		UserIdentify userIdentify = userIdentifyDao.findByUserId(user
				.getUserId());
		if (userCache.getStatus() == 1) {
			throw new BorrowException("您账号已经被锁定，不能进行投标!", 1);
		}
		// 用户认证校验
		validBorrowTender(model.prototype(), userIdentify);
		if (tenderMoney > act.getUseMoney()) {
			throw new BorrowException("投标金额不能大于您的可用余额！", 1);
		}
		if (tenderMoney < model.getLowestAccount()
				&& model.getLowestAccount() > 0) {
			throw new BorrowException("投标金额不能小于最小投标额！", 1);
		}
		if (tenderMoney <= 0) {
			throw new BorrowException("投标金额必须大于0！", 1);
		}
		// 验证是否是流标的标
		VerifyLog verifyLog = verifyLogDao.findObjByProperty("fid",
				model.getId());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(verifyLog.getTime());
		calendar.add(Calendar.DATE, model.getValidTime());
		if (model.getAccount() > model.getAccountYes()
				&& calendar.getTime().before(new Date())) {
			throw new BorrowException("该标已经流标，不能进行投标！", 1);
		}
		return model;
	}

	private void validBorrowTender(Borrow borrow, UserIdentify userIdentify) {
		AutoTenderConfRuleCheck check = (AutoTenderConfRuleCheck) Global
				.getRuleCheck(EnumRuleNid.AUTO_TENDER_CONF.getValue());
		if (check != null) {
			if (check.tender_valid.status == 1) {
				if (check.tender_valid.real_enable == 1
						&& userIdentify.getRealNameStatus() != 1) {
					throw new BorrowException("您还未通过实名认证，投标失败！",
							BorrowException.TYPE_JSON);
				}
				if (check.tender_valid.phone_enable == 1
						&& userIdentify.getMobilePhoneStatus() != 1) {
					throw new BorrowException("您还未通过手机认证，投标失败！",
							BorrowException.TYPE_JSON);
				}
			}
		}
	}

	@Override
	public void checkTender(BorrowModel model, double tenderNum,
			double totalPacketMoney, User user, String pwd) {
		UserIdentifyDao userIdentifyDao = (UserIdentifyDao) BeanUtil
				.getBean("userIdentifyDao");
		AccountDao accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		BorrowTenderDao tenderDao = (BorrowTenderDao) BeanUtil
				.getBean("borrowTenderDao");
		BorrowDao borrowDao = (BorrowDao) BeanUtil.getBean("borrowDao");
		// Account act = accountDao.getAccountByUserId(user.getUserId());
		UserIdentify userIdentify = userIdentifyDao.findObjByProperty(
				"user.userId", user.getUserId());
		UserCacheDao userCacheDao = (UserCacheDao) BeanUtil
				.getBean("userCacheDao");
		VerifyLogDao verifyLogDao = (VerifyLogDao) BeanUtil
				.getBean("verifyLogDao");
		UserCacheService userCacheService = (UserCacheService) BeanUtil
				.getBean("userCacheService");
		UserCache userCache = userCacheDao.findObjByProperty("user.userId",
				user.getUserId());
		Borrow borrow = borrowDao.find(model.getId());
		// 定义返回地址
		String returnUrl = model.findReturnUrl(borrow.getType()) + "?id="
				+ model.getId();
		if (userCache.getStatus() == 1) {
			throw new BorrowException("您账号已经被锁定，不能进行投标!", returnUrl);
		}
		// 实际投资金额
		double account = BigDecimalUtil.sub(tenderNum, totalPacketMoney);
		if (model.getStatus() != 1) { // 初审通过
			throw new BorrowException("不能进行投标!", returnUrl);
		}
		if (StringUtil.isBlank(model.getPayPwd())) {
			throw new BorrowException("支付密码不能为空!", returnUrl);
		}
		if (!MD5.encode(model.getPayPwd()).equals(user.getPayPwd())) {
			HttpServletRequest request = ServletActionContext.getRequest();
			userCacheService.doLock(request, user.getUserId(),
					UserCacheModel.PAY_PWD_LOCK);
			throw new BorrowException("支付密码不正确!", returnUrl);
		}
		if (model.getType() != Borrow.TYPE_ENTRUST) {
			if (model.getUser().getUserId() == user.getUserId()) {
				throw new BorrowException("自己不能投自己发布的标！", returnUrl);
			}
		}
		if (model.getAccountYes() >= model.getAccount()) {
			throw new BorrowException("此标已满!", returnUrl);
		}
		if (BigDecimalUtil.sub(model.getAccount(), model.getAccountYes()) >= model
				.getLowestAccount()) {
			if (tenderNum < model.getLowestAccount()
					&& model.getLowestAccount() > 0) {
				throw new BorrowException("投标金额不能小于最小投标额！", returnUrl);
			}
		}
		if (StringUtil.isNotBlank(model.getPwd())
				&& (!pwd.equals(model.getPwd()))) {
			throw new BorrowException("定向标密码不正确!", returnUrl);
		}
		if (tenderNum < totalPacketMoney) {
			throw new BorrowException("您的投标金额需大于红包金额！ ", returnUrl);
		}
		double userMoney = accountDao.getSumAccount(user.getUserId());
		if (account > userMoney) {
			throw new BorrowException("您的可用余额不足，投标失败！ ", returnUrl);
		}
		double most_single_account_num = data.getMostSingleLimit();
		if (most_single_account_num > 0 && tenderNum > most_single_account_num
				|| tenderNum <= 0) {
			throw new BorrowException("投标金额不能大于单笔最多投标总额，投标失败！ ", returnUrl);
		}
		double most_account_num = data.getMostAccount();
		double hasTender = tenderDao.hasTenderTotalPerBorrowByUserid(
				data.getId(), user.getUserId());
		if (most_account_num > 0 && tenderNum + hasTender > most_account_num) {
			double difference = most_account_num - hasTender;
			if (difference == 0) {
				throw new BorrowException("您对该标的投资已达到最多投标总额(￥"
						+ most_account_num + ")，不能继续投标，投标失败！", returnUrl);
			}
			throw new BorrowException("投标金额不能大于最大投标额,您当前还可以投" + difference
					+ "元", returnUrl);
		}
		// 用户认证校验
		validBorrowTender(model.prototype(), userIdentify);
		// 验证是否是流标的标
		VerifyLog verifyLog = verifyLogDao.findByType(model.getId(), "borrow",
				1);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(verifyLog.getTime());
		calendar.add(Calendar.DATE, model.getValidTime());
		if (model.getAccount() > model.getAccountYes()
				&& calendar.getTime().before(new Date())) {
			throw new BorrowException("该标已经流标，不能进行投标！", returnUrl);
		}
	}

	/**
	 * 发标前校验
	 */
	@Override
	public boolean allowPublish(User user) {
		UserIdentifyDao userIdentifyDao = (UserIdentifyDao) BeanUtil
				.getBean("userIdentifyDao");
		UserIdentify ua = userIdentifyDao.findByUserId(user.getUserId());
		if (isNeedRealName() && ua.getRealNameStatus() != 1) {
			throw new BorrowException("需要通过实名认证！", 1);
		}
		if (isNeedEmail() && ua.getEmailStatus() != 1) {
			throw new BorrowException("需要通过邮箱认证！", 1);
		}
		if (isNeedPhone() && ua.getMobilePhoneStatus() != 1) {
			throw new BorrowException("需要通过手机认证！", 1);
		}
		return true;
	}

	@Override
	public BorrowTender tenderSuccess(BorrowTender tender, InterestCalculator ic) {
		BorrowTenderDao tenderDao = (BorrowTenderDao) BeanUtil
				.getBean("borrowTenderDao");
		// tender标利息总和
		double totalInterest = 0;
		// 总共需要还款金额
		double repayment_account = 0;
		double totalAccount = BigDecimalUtil.add(data.getAccountYes(),
				tender.getAccount());
		if (data.getAccount() == totalAccount) {
			totalInterest = tenderDao.getInterestByBorrowId(data.getId());
			// 待还利息总额
			double interest = BigDecimalUtil.sub(data.getRepaymentAccount(),
					data.getAccount());
			// 若由于四舍五入导致待还利息总额比待收利息总额偏差，则给最后一次投资做减法，保证资金一致
			totalInterest = BigDecimalUtil.add(interest, -totalInterest);
			repayment_account = BigDecimalUtil.add(tender.getAccount(),
					totalInterest);
		} else {
			// 总共需要还款金额
			repayment_account = ic.repayTotal();
			totalInterest = BigDecimalUtil.sub(repayment_account,
					tender.getAccount());
		}
		tender.setRepaymentAccount(repayment_account);
		tender.setInterest(totalInterest);
		tender.setStatus(0);
		tenderDao.modifyBorrowTender(tender);
		return tender;
	}

	@Override
	public void handleVerifyFull() {
		BorrowTenderDao tenderDao = (BorrowTenderDao) BeanUtil
				.getBean("borrowTenderDao");
		List<BorrowTender> tenderList = tenderDao.findByProperty("borrow.id",
				data.getId());

		IpsService ipsService = (IpsService) BeanUtil.getBean("ipsService");
		// 投资人给借款人转账
		logger.info("投资人给借款人转账处理开始");
		ipsService.transfer(data, tenderList, "1", "2");
		logger.info("投资人给借款人转账处理结束");
	}

	@SuppressWarnings("unused")
	@Override
	public void handleTenderAfterFullSuccess() {
		BorrowTenderDao tenderDao = (BorrowTenderDao) BeanUtil
				.getBean("borrowTenderDao");
		UserVipService userVipService = (UserVipService) BeanUtil
				.getBean("userVipService");
		BorrowCollectionDao borrowCollectionDao = (BorrowCollectionDao) BeanUtil
				.getBean("borrowCollectionDao");
		UserRedPacketDao userRedPacketDao = (UserRedPacketDao) BeanUtil
				.getBean("userRedPacketDao");
		UserRedPacketService userRedPacketService = (UserRedPacketService) BeanUtil
				.getBean("userRedPacketService");

		List<BorrowTender> tenderList = tenderDao.findByProperty("borrow.id",
				data.getId());
		IpsService ipsService = (IpsService) BeanUtil.getBean("ipsService");

		// 满标复审成功后转账担保收益
		if (data.getGuaranteeFee() > 0
				&& !StringUtil.isBlank(data.getGuaranteeNo())) {
			// 借款人给担保方 担保收益
			ipsService.transfer(data, tenderList, "5", "1");
		}
		User user;
		if (data.getType() != Borrow.TYPE_ENTRUST) {
			user = data.getUser();
		} else {
			user = new User(1L);
		}
		HashMap<Long, Double> moneyMap = new HashMap<Long, Double>();
		for (int i = 0; i < tenderList.size(); i++) {
			BorrowTender tender = (BorrowTender) tenderList.get(i);

			// //通过tender得到投资使用的红包金额
			double redMoney = userRedPacketDao
					.getTotalPacketMoneyByTender(tender.getId());

			User tenderUser = tender.getUser();
			/*
			 * if(moneyMap.containsKey(tenderUser.getUserId())) { double money =
			 * tender.getAccount() +
			 * (Double)moneyMap.get(tenderUser.getUserId());
			 * moneyMap.put(tenderUser.getUserId(), money); }else {
			 * moneyMap.put(tenderUser.getUserId(), tender.getAccount()); }
			 */
			Global.setTransfer("tender", tender);
			double account = tender.getAccount();

			double interest = tender.getInterest();
			// 扣除冻结/生产待收本金
			Global.setTransfer("money", account);
			Global.setTransfer("borrow", data);
			ProductBasicService productBasicService = (ProductBasicService) BeanUtil
					.getBean("productBasicService");
			ProductBasic productBasic = productBasicService
					.getProductBasicInfo(new Long(data.getType()), data.getId());
			Global.setTransfer("flag", productBasic.getProductTypeFlag());
			AbstractExecuter freeExecuter = ExecuterHelper
					.doExecuter("borrowDecuctFreezeExecuter");
			freeExecuter.execute(account, tenderUser, user);
			// 生产待收利息
			if (interest > 0) {
				Global.setTransfer("money", interest);
				Global.setTransfer("borrow", data);
				AbstractExecuter waitExecuter = ExecuterHelper
						.doExecuter("borrowWaitInterestExecuter");
				waitExecuter.execute(interest, tenderUser, user);
			}
			// 投标奖励
			double awardValue = calculateAward(account);
			if (awardValue > 0) {
				// 给予投资人奖励
				Global.setTransfer("award", awardValue);
				Global.setTransfer("borrow", data);
				Global.setTransfer("user", tenderUser);
				AbstractExecuter awardExecuter = ExecuterHelper
						.doExecuter("awardTenderAwardExecuter");
				awardExecuter.execute(awardValue, tenderUser, user);
			}

			// 生产待收利息--使用加息劵2015-01-03
			double interestRate = borrowCollectionDao.sumInterestRate(tender
					.getId());
			if (interestRate > 0) {
				Global.setTransfer("money", interestRate);
				Global.setTransfer("borrow", data);
				AbstractExecuter executer = ExecuterHelper
						.doExecuter("borrowWaitInterestRateExecuter");
				executer.execute(interestRate, tenderUser, user);
			}

			// 修改Tender表中的待收利息
			tender.setWaitAccount(tender.getRepaymentAccount());
			tender.setWaitInterest(tender.getInterest());
			tender.setStatus(1);
			tenderDao.update(tender);

			BorrowCollectionDao collectionDao = (BorrowCollectionDao) BeanUtil
					.getBean("borrowCollectionDao");
			List<BorrowCollection> clist = collectionDao.findByProperty(
					"tender.id", tender.getId());
			for (BorrowCollection c : clist) {
				c.setRepaymentTime(this.getRepayTime(c.getPeriod()));
			}

			// 投资积分处理
			BaseScoreLog bLog = new TenderInvestSuccessLog(
					tenderUser.getUserId(), data, tender);
			bLog.doEvent();
			collectionDao.update(clist);

			// 用户VIP处理
			userVipService.doUserVip(tender.getAccount(), tender.getUser());

			// 执行红包发放过程
			userRedPacketService.doTenderRedPacket(tenderUser, tender);
			userRedPacketService.doBorrowRedPacket(tenderUser, tender);
			//生成加息券
			userRedPacketService.doTenderCoupon(tenderUser, tender);

		}

		AccountLogDao theAccountLogDao = (AccountLogDao) BeanUtil
				.getBean("theAccountLogDao");
		UserDao theUserDao = (UserDao) BeanUtil.getBean("theUserDao");
		AccountDao accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		UserInviteDao theUserInviteDao = (UserInviteDao) BeanUtil
				.getBean("theUserInviteDao");
		RecommendRecordDao recommendRecordDao = (RecommendRecordDao) BeanUtil
				.getBean("recommendRecordDao");
		RecommendProfitDao recommendProfitDao = (RecommendProfitDao) BeanUtil
				.getBean("recommendProfitDao");
		RecommendProfitRecordDao recommendProfitRecordDao = (RecommendProfitRecordDao) BeanUtil
				.getBean("recommendProfitRecordDao");

		for (BorrowTender bt : tenderList) {
			User btUser = bt.getUser();

			UserInvite userInvite = theUserInviteDao
					.getByHql(" from UserInvite where user.userId = "
							+ btUser.getUserId() + "  ");

			System.out.println("执行是否有推荐收益: " + btUser.getUserId() + "  ");
			if (null != userInvite) {
				RecommendRecord recommendRecord = recommendRecordDao
						.findRecord(userInvite.getInviteUser(),
								userInvite.getUser());
				/**
				 * 如果有推荐人
				 */
				if (null != recommendRecord) {
					RecommendProfitRecord recommendProfitRecord = new RecommendProfitRecord();
					RecommendProfit recommendProfit = recommendProfitDao
							.findByMoney(bt.getBorrow().getAccount());

					recommendProfitRecord.setAccount(bt.getAccount());
					recommendProfitRecord.setAddTime(new Date());
					recommendProfitRecord.setBorrow(bt.getBorrow());
					recommendProfitRecord.setInviteUser(userInvite
							.getInviteUser());

					int days = 0;
					/**
					 * 计算给推荐人收益天数
					 */
					if (0 == bt.getBorrow().getBorrowTimeType()) {
						days = bt.getBorrow().getTimeLimit();
						recommendProfitRecord.setMoney(BigDecimalUtil.round(bt.getAccount()
								* recommendProfit.getRate() * days / 12 / 1000));
					} else {
						days = bt.getBorrow().getTimeLimit();
						recommendProfitRecord
								.setMoney(BigDecimalUtil.round(bt.getAccount()
										* recommendProfit.getRate() * days
										/ 365 / 1000));
					}

					recommendProfitRecord.setProfit(recommendProfit.getId());
					recommendProfitRecord.setTenderId(bt.getId());
					recommendProfitRecord.setUser(bt.getUser());

					if (0.01 < BigDecimalUtil.round(bt.getAccount() * recommendProfit.getRate()
							* days / 365 / 1000)) {
						recommendProfitRecord = recommendProfitRecordDao
								.save(recommendProfitRecord);

						Account account = accountDao
								.getAccountByUserId(userInvite.getInviteUser()
										.getUserId());
						account.setTotal(BigDecimalUtil.add(account.getTotal()
								, recommendProfitRecord.getMoney()));
						account.setUseMoney(BigDecimalUtil.add(account.getUseMoney()
								, recommendProfitRecord.getMoney()));
						accountDao.update(account);

						AccountLog accountLog = new AccountLog();
						String userName = bt.getUser().getUserName();
						String realName = userName.substring(
								0,
								userName.length()
										- (userName.substring(3)).length())
								+ "****" + userName.substring(7);
						accountLog.setAddTime(new Date());
						accountLog.setMoney(recommendProfitRecord.getMoney());
						accountLog.setCollection(account.getCollection());
						accountLog.setNoUseMoney(account.getNoUseMoney());
						accountLog.setPaymentsType((byte) 1);
						accountLog.setRemark("推荐收益来源用户" + realName + "投资项目:["
								+ bt.getBorrow().getName() + "]，金额:"
								+ bt.getAccount());
						accountLog.setTotal(account.getTotal());
						accountLog.setToUser(theUserDao.find(1l));
						accountLog.setType("recommend_profit");
						accountLog.setUseMoney(account.getUseMoney());
						accountLog.setUser(userInvite.getInviteUser());
						theAccountLogDao.save(accountLog);
					}
				}
			}

		}

	}

	@Override
	public void handleBorrowAfterFullSuccess() {
		BorrowRepaymentDao repaymentDao = (BorrowRepaymentDao) BeanUtil
				.getBean("borrowRepaymentDao");
		List<BorrowRepayment> list =  getRepayments();
		for(BorrowRepayment repayment : list)
		{
			repaymentDao.save(repayment);
		}

	}

	/**
	 * 根据rd_borrow_collection生成借款人还款计划
	 * 
	 * @return
	 */
	private List<BorrowRepayment> getRepayments() {
		logger.info("-----产生还款计划开始----");
		BorrowCollectionDao collectionDao = (BorrowCollectionDao) BeanUtil
				.getBean("borrowCollectionDao");
		int time;
		if (data.getStyle()==Borrow.STYLE_ONETIME_REPAYMENT && data.getBorrowTimeType()==1) {
			time = 1;
		}else if(data.getBorrowTimeType()==1){
			time = data.getPeriod();
		}else
		{
			time = data.getTimeLimit();
		}
		List<BorrowRepayment> repays = new ArrayList<BorrowRepayment>();
		for (int i = 0; i < time; i++) {
			logger.info("借款标id:" + data.getId() + "---借款期数：" + i);
			BorrowRepayment repayment = new BorrowRepayment();
			Object[] capitalAndInterest = collectionDao
					.getCapitalAndInterestByBorrowAndPeriod(data.getId(), i);
			double capital = (Double) (capitalAndInterest[0]);
			double interest = (Double) (capitalAndInterest[1]);
			repayment.setUser(data.getUser());
			repayment.setBorrow(data);
			repayment.setPeriod(i);
			if (data.getType() != Borrow.TYPE_FLOW) {
				repayment.setRepaymentTime(getRepayTime(repayment.getPeriod()));
			} else {
				repayment.setRepaymentTime(getFlowRepayTime(repayment
						.getPeriod()));
			}
			double repaymentAccount = BigDecimalUtil.add(capital, interest);
			repayment.setRepaymentAccount(repaymentAccount);
			repayment.setInterest(interest);
			repayment.setWebStatus(BorrowRepayment.WEB_STATUS_NORMAL);
			repayment.setCapital(capital);
			repayment.setAddTime(new Date());
			repayment.setAddIp(Global.getIP());
			repays.add(repayment);
		}
		logger.info("-----产生还款计划结束----");
		return repays;
	}

	@Override
	public double getManageFee() {
		double fee = 0.0;
		double account = data.getAccount();
		BorrowManageFeeRuleCheck borrowManageFeeRuleCheck = (BorrowManageFeeRuleCheck) Global
				.getRuleCheck("borrowManageFee");
		// 固定比例收取借款手续费
		int cal_style = borrowManageFeeRuleCheck.cal_style;
		if (cal_style == 1) {
			BorrowConfig cfg = this.config;
			if (cfg != null) {
				if (data.getBorrowTimeType() == 1) {
					fee = account * cfg.getDayManageFee() * data.getTimeLimit()
							/ 3000;
				} else {
					fee = account * cfg.getManageFee() * data.getTimeLimit()
							/ 100;
				}
			}
		}
		// 不固定比例收取借款手续费
		if (cal_style == 2) {
			double rate = 0.0;
			if (data.getBorrowTimeType() == 1
					|| (data.getBorrowTimeType() == 0 && data.getTimeLimit() == 1)) {
				// 获得一个月的利率
				rate = Double
						.parseDouble(borrowManageFeeRuleCheck.monthRate.month_rate
								.get("1"));
			} else {
				// 获得多个月的利率
				rate = Double
						.parseDouble(borrowManageFeeRuleCheck.monthRate.month_rate
								.get(data.getTimeLimit() + ""));
			}
			fee = BigDecimalUtil.round(BigDecimalUtil.mul(account, rate));
		}
		return fee;
	}

	@Override
	public void handleTenderAfterFullFail() {
		BorrowTenderDao tenderDao = (BorrowTenderDao) BeanUtil
				.getBean("borrowTenderDao");
		tenderDao.updateStatus(data.getId(), 2, 0);
		List<BorrowTender> tenderList = tenderDao.findByProperty("borrow.id",
				data.getId());
		UserRedPacketDao userRedPacketDao = (UserRedPacketDao) BeanUtil
				.getBean("userRedPacketDao");
		for (int i = 0; i < tenderList.size(); i++) {
			BorrowTender tender = (BorrowTender) tenderList.get(i);

			// 通过tender得到投资使用的红包金额
			double redMoney = userRedPacketDao
					.getTotalPacketMoneyByTender(tender.getId());
			List<UserRedPacket> list = userRedPacketDao
					.getListByTenderId(tender.getId());
			if (list != null && list.size() > 0) {
				for (UserRedPacket userRedPacket : list) {
					userRedPacket.setUsed(false);
					userRedPacket.setTender(null);
					userRedPacket.setUsedTime(null);
					userRedPacketDao.update(userRedPacket);
				}
			}
			double account = tender.getAccount();
			Global.setTransfer("money", account);
			Global.setTransfer("tenderAccount", account);
			Global.setTransfer("borrow", data);
			ProductBasicService productBasicService = (ProductBasicService) BeanUtil
					.getBean("productBasicService");
			ProductBasic productBasic = productBasicService
					.getProductBasicInfo(new Long(data.getType()), data.getId());
			Global.setTransfer("flag", productBasic.getProductTypeFlag());
			Global.setTransfer("tender", tender);
			Global.setTransfer("user", tender.getUser());
			User toUser = data.getUser();
			if (toUser == null) {
				toUser = new User(1);
			}
			AbstractExecuter deductExecuter = ExecuterHelper
					.doExecuter("borrowFailExecuter");
			deductExecuter.execute(account, tender.getUser(), toUser, 0);
			if (redMoney > 0) {
				// 红包使用资金日志
				if (redMoney > 0) {
					Global.setTransfer("user", tender.getUser());
					Global.setTransfer("money", redMoney);
					Global.setTransfer("borrow", data);
					AbstractExecuter redPacketExecuter = ExecuterHelper
							.doExecuter("tenderRedPacketFailExecuter");
					redPacketExecuter.execute(redMoney, tender.getUser(),
							new User(1));
				}
			}
		}
	}

	@Override
	public void immediateInterestAfterTender(BorrowTender tender) {

	}

	@Override
	public void immediateRepayAfterTender(BorrowTender tender) {

	}

	@Override
	public Borrow handleBorrowBeforePublish(Borrow borrow) {
		return borrow;
	}

	@Override
	public Borrow handleBorrowAfterPublish(Borrow borrow) {
		return borrow;
	}

	@Override
	public List<BorrowCollection> createCollectionList(BorrowTender tender,
			InterestCalculator ic) {
		List<BorrowCollection> collectList = new ArrayList<BorrowCollection>();
		List<EachPlan> eachPlan = ic.getEachPlan();
		// 拼装Collection对象 批量插入还款表
		int i = 0;
		double totalInterest = 0;
		double interestRate = 0;
		for (EachPlan e : eachPlan) {
			BorrowCollection c = fillCollection(e, tender, ic);
			if (i == eachPlan.size() - 1) { // 最后一期做减法
				double interest = tender.getInterest();
				double different = BigDecimalUtil.sub(interest, totalInterest);
				c.setInterest(different);
				c.setRepaymentAccount(BigDecimalUtil.add(c.getCapital(),
						different));
				if (tender.getInterestRateValue() > 0) {
					double interestRateValue = BigDecimalUtil.div(
							BigDecimalUtil.mul(tender.getInterestRateValue(),
									tender.getInterest()), tender.getBorrow()
									.getApr());
					c.setInterestRate(BigDecimalUtil.round(interestRateValue)
							- interestRate);
				} else {
					c.setInterestRate(0.00);
				}
			}
			totalInterest = BigDecimalUtil.add(totalInterest, e.getInterest());
			interestRate += c.getInterestRate();
			c.setPeriod(i++);
			Date repayTime = calCollectionRepayTime(tender,i-1);
			c.setRepaymentTime(DateUtil.getLastSecIntegralTime(repayTime));
			collectList.add(c);
		}
		this.collectionList = collectList;
		return collectList;
	}

	private BorrowCollection fillCollection(EachPlan e, BorrowTender t,
			InterestCalculator ic) {
		BorrowCollection c = new BorrowCollection();
		c.setBorrow(t.getBorrow());
		c.setTender(t);
		c.setInterest(e.getInterest());
		c.setCapital(e.getCapital());
		c.setRepaymentAccount(e.getTotal());
		c.setAddTime(new Date());
		c.setAddIp(Global.getIP());
		c.setStatus(0);
		c.setRepaymentYesAccount(0.00);
		c.setLateDays(0);
		c.setLateInterest(0.00);
		c.setUser(t.getUser());
		c.setManageFee(BigDecimalUtil.mul(c.getInterest(),
				Global.getDouble("borrow_fee")));

		if (t.getInterestRateValue() > 0 && t.getBorrow().getApr() > 0) {
			double interestRate = BigDecimalUtil.div(
					BigDecimalUtil.mul(t.getInterestRateValue(),
							e.getInterest()), t.getBorrow().getApr());
			c.setInterestRate(BigDecimalUtil.round(interestRate));
		} else {
			c.setInterestRate(0.00);
		}
		c.setInterestRateYes(0.00);

		return c;
	}

	public Date calCollectionRepayTime(BorrowTender tender, int period) {
		Date fullVerifyTime = new Date();
		Date repayDate = DateUtil.getLastSecIntegralTime(fullVerifyTime);
		if (data.getType() == Borrow.TYPE_SECOND) {
			return repayDate;
		} else if (data.getBorrowTimeType() == 1) {
			if(Borrow.STYLE_MIDDLEDAY_INTEREST==data.getStyle())
			{
				if(period==data.getPeriod()-1)//最后一期
				{
					repayDate = DateUtil.addDate(DateUtil.rollDay(fullVerifyTime,
							data.getTimeLimit()),-1);
				}else//不是最后一期
				{
					repayDate = DateUtil.addDate(DateUtil.rollDay(fullVerifyTime,
							(period+1)*data.getMiddleDay()),-1);
				}
			}else
			{
				repayDate = DateUtil.addDate(
						DateUtil.rollDay(repayDate, data.getTimeLimit()), -1);
			}
			return repayDate;
		} else {
			if (data.getStyle() == Borrow.STYLE_ONETIME_REPAYMENT) {// 一次性还款
				repayDate = DateUtil.addDate(
						DateUtil.rollMon(repayDate, data.getTimeLimit()), -1);
			} else if ("5".equals(data.getStyle())) {// 满标还息每月还息到期还本
				if (period == 0) {
					repayDate = DateUtil.rollMinute(new Date(), 5);// 5分钟后还款
				} else {
					repayDate = DateUtil.addDate(
							DateUtil.rollMon(repayDate, period), -1);
				}
			} else {
				repayDate = DateUtil.addDate(
						DateUtil.rollMon(repayDate, period + 1), -1);
			}
			return repayDate;
		}
		
	}

	@Override
	public List<BorrowRepayment> createFlowRepaymentList(
			List<BorrowCollection> clist) {
		List<BorrowRepayment> repayList = new ArrayList<BorrowRepayment>(
				clist.size());
		for (BorrowCollection c : clist) {
			BorrowRepayment repay = new BorrowRepayment();
			repay.setUser(data.getUser());
			repay.setBorrow(c.getBorrow());
			repay.setPeriod(c.getPeriod());
			repay.setStatus(0);
			repay.setWebStatus(BorrowRepayment.WEB_STATUS_NORMAL);
			repay.setRepaymentTime(c.getRepaymentTime());
			repay.setRepaymentAccount(c.getRepaymentAccount());
			repay.setInterest(c.getInterest());
			repay.setCapital(c.getCapital());
			repay.setAddTime(c.getAddTime());
			repay.setAddIp(Global.getIP());
			repay.setTender(c.getTender());
			repayList.add(repay);
		}
		this.repaymentList = repayList;
		return repayList;
	}

	@Override
	public void validBeforeRepayment(BorrowRepayment borrowRepayment,
			Account account) {
		if (borrowRepayment == null || borrowRepayment.getStatus() == 1) {
			throw new BorrowException("该期借款已经还款,请不要重复操作！", 1);
		}
		if (this.data.getStatus() != 6 && this.data.getStatus() != 7) {
			throw new BorrowException("当前借款标的状态不能进行还款操作！", 1);
		}
		// 还款+逾期+展期
		double repayMoney = BigDecimalUtil.add(BigDecimalUtil.add(
				borrowRepayment.getRepaymentAccount(),
				borrowRepayment.getLateInterest()), borrowRepayment
				.getExtensionInterest());
		if (repayMoney > account.getUseMoney()) {
			throw new BorrowException("您的可用余额不足，还款失败，请充值后再进行还款！", 1);
		}
		BorrowRepaymentDao borrowRepaymentDao = (BorrowRepaymentDao) BeanUtil
				.getBean("borrowRepaymentDao");
		boolean hasAhead = borrowRepaymentDao.hasRepaymentAhead(borrowRepayment
				.getPeriod(), borrowRepayment.getBorrow().getId());
		if (hasAhead) {
			throw new BorrowException("还有尚未还款的借款！", 1);
		}
	}

	@Override
	public void borrowRepayHandleBorrow(BorrowRepayment repay) {
		User toUser = new User();
		toUser.setUserId(1L);
		double capital = repay.getCapital();
		double interest = repay.getInterest();
		double extensionInterest = repay.getExtensionInterest();
		double lateInterest = repay.getLateInterest();
		Global.setTransfer("borrow", this.data);
		// 从用户冻结账户中扣除还款本金
		if (capital > 0) {
			Global.setTransfer("money", capital);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("borrowRepayCapitalExecuter");
			repayExecuter.execute(capital, data.getUser(), toUser);
		}
		// 扣除还款利息
		if (interest > 0) {
			Global.setTransfer("money", interest);
			AbstractExecuter interestExecuter = ExecuterHelper
					.doExecuter("borrowRepayInterestExecuter");
			interestExecuter.execute(interest, data.getUser(), toUser);
		}
		// 扣除展期利息
		if (extensionInterest > 0) {
			Global.setTransfer("money", extensionInterest);
			AbstractExecuter extensionExecuter = ExecuterHelper
					.doExecuter("borrowRepayExtensionInterestExecuter");
			extensionExecuter
					.execute(extensionInterest, data.getUser(), toUser);
		}
		// 逾期利息
		if (lateInterest > 0) {
			Global.setTransfer("money", lateInterest);
			AbstractExecuter lateExecuter = ExecuterHelper
					.doExecuter("borrowRepayLateInterestExecuter");
			lateExecuter.execute(lateInterest, data.getUser(), toUser);

			if (repay.getStatus() == 2) {
				double money = lateInterest;
				Global.setTransfer("money", money);
				AbstractExecuter systemExecuter = ExecuterHelper
						.doExecuter("borrowRepaySystemLateInterestExecuter");
				systemExecuter.execute(money, data.getUser(), toUser);
			}
		}
	}

	@Override
	public void borrowRepayHandleTender(BorrowRepayment repay) {
		BorrowTenderDao tenderDao = (BorrowTenderDao) BeanUtil
				.getBean("borrowTenderDao");
		BorrowCollectionDao collectionDao = (BorrowCollectionDao) BeanUtil
				.getBean("borrowCollectionDao");
		BorrowRepaymentDao borrowRepaymentDao = (BorrowRepaymentDao) BeanUtil.getBean("borrowRepaymentDao");
		List<BorrowCollection> list = this.getWaitColl(repay);
		ProductBasicService productBasicService = (ProductBasicService) BeanUtil
				.getBean("productBasicService");
		UserDao theUserDao = (UserDao) BeanUtil.getBean("theUserDao");
		WechatCacheService wechatCacheService = (WechatCacheService)BeanUtil
				.getBean("wechatCacheService");
		if (list != null && list.size() > 0) {
			Global.setTransfer("borrow", this.data);
			ProductBasic productBasic = productBasicService
					.getProductBasicInfo(new Long(data.getType()), data.getId());
			Global.setTransfer("flag", productBasic.getProductTypeFlag());
			double totalLateAwardValue = 0;
			QueryParam param = new QueryParam();
			param.addParam("borrow.id", this.data.getId());
			int repaymentCount = borrowRepaymentDao.countByCriteria(param);
			for (BorrowCollection borrowCollection : list) {
				BorrowTender tender = tenderDao.find(borrowCollection
						.getTender().getId());
				// 增加债权处理，待还本金需要扣除已经转出的债权价值
				double capital = BigDecimalUtil.sub(
						borrowCollection.getCapital(),
						borrowCollection.getBondCapital());
				// 增加债权处理，待还利息需要扣除已经转出的利息
				double conllectionInterest = 0;
				if ((data.getType() == 119 || data.getType() == 112)
						&& data.getBorrowTimeType() == 1) {
					conllectionInterest = BigDecimalUtil.sub(BigDecimalUtil
							.sub(borrowCollection.getInterest(),
									borrowCollection.getBondInterest()),
							borrowCollection.getRepaymentYesAccount());
				} else {
					conllectionInterest = BigDecimalUtil.sub(
							borrowCollection.getInterest(),
							borrowCollection.getBondInterest());
				}
				// 归还投资人本金
				if (capital > 0) {
					Global.setTransfer("money", capital);
					AbstractExecuter repayTenderExecuter = ExecuterHelper
							.doExecuter("borrowRepayTenderCapitalExecuter");
					repayTenderExecuter.execute(capital, tender.getUser(),
							this.data.getUser());

				}
				// 归还投资人利息
				borrowCollection = getInvestRepayInterest(borrowCollection,
						tender);
				// 根据投资比例归还逾期利息
				if (repay.getLateInterest() > 0) {
					double tenderLateInterest = 0;
					boolean isOpenApi = BaseTPPWay.isOpenApi();
					if (!isOpenApi) {// 如果是标准版，则逾期利息50%给平台
						tenderLateInterest = BigDecimalUtil.mul(
								repay.getLateInterest() / 2,
								(BigDecimalUtil.div(conllectionInterest,
										repay.getInterest())));
					} else {
						tenderLateInterest = BigDecimalUtil.mul(
								repay.getLateInterest(),
								(BigDecimalUtil.div(conllectionInterest,
										repay.getInterest())));
					}
					tenderLateInterest = BigDecimalUtil
							.round(tenderLateInterest);
					if (tenderLateInterest > 0) {
						Global.setTransfer("money", tenderLateInterest);
						AbstractExecuter repayTenderLateExecuter = ExecuterHelper
								.doExecuter("borrowRepayTenderLateInterestExecuter");
						repayTenderLateExecuter.execute(tenderLateInterest,
								tender.getUser(), this.data.getUser());
					}
					borrowCollection.setLateDays(repay.getLateDays());
					borrowCollection.setLateInterest(tenderLateInterest);
				}

				// 展期利息
				if (repay.getExtensionInterest() > 0) {
					double userTenderExtensionInterest = 0;
					double tenderExtensionInterest = BigDecimalUtil.mul(
							repay.getExtensionInterest(),
							(BigDecimalUtil.div(capital, repay.getCapital())));
					BorrowAprLimitRuleCheck aprLimitRuleCheck = (BorrowAprLimitRuleCheck) Global
							.getRuleCheck(EnumRuleNid.BORROW_APR_LIMIT
									.getValue());
					if (aprLimitRuleCheck != null
							&& aprLimitRuleCheck.extension.extension_enable) {
						userTenderExtensionInterest = BigDecimalUtil.mul(
								tenderExtensionInterest,
								aprLimitRuleCheck.extension.tender_apr);
						if (userTenderExtensionInterest > 0) {
							Global.setTransfer("money",
									userTenderExtensionInterest);
							AbstractExecuter extensionInterestExecuter = ExecuterHelper
									.doExecuter("borrowRepayTenderExtensionInterestExecuter");
							extensionInterestExecuter.execute(
									userTenderExtensionInterest,
									tender.getUser(), this.data.getUser());
						}
						borrowCollection
								.setExtensionInterest(userTenderExtensionInterest);
					}
				}

				// 归还投资人使用加息劵产生的利息2015-01-03
				double interestRate = borrowCollection.getInterestRate();
				if (interestRate > 0) {
					Global.setTransfer("money", interestRate);
					AbstractExecuter repayTenderExecuter = ExecuterHelper
							.doExecuter("borrowRepayTenderInterestRateExecuter");
					repayTenderExecuter.execute(interestRate, tender.getUser(),
							this.data.getUser());
				}

				// 归还投资人的浮动收益率
				double floatIncome = borrowCollection.getFloatIncome();
				if (floatIncome > 0) {
					Global.setTransfer("money", floatIncome);
					AbstractExecuter borrowRepayTenderFloatIncomeExecuter = ExecuterHelper
							.doExecuter("borrowRepayTenderFloatIncomeExecuter");
					borrowRepayTenderFloatIncomeExecuter.execute(floatIncome,
							tender.getUser(), this.data.getUser());
				}

				if ((data.getType() == 119 || data.getType() == 112)
						&& data.getBorrowTimeType() == 1) {
					tenderDao
							.updateRepayTender(
									borrowCollection.getRepaymentAccount()
											- borrowCollection
													.getRepaymentYesAccount(),
									conllectionInterest, tender.getId());
				} else {
					tenderDao.updateRepayTender(
							borrowCollection.getRepaymentAccount(),
							conllectionInterest, tender.getId());
				}
				// 更新collection
				borrowCollection.setStatus(1);
				borrowCollection.setRepaymentYesTime(new Date());
				borrowCollection.setRepaymentYesAccount(borrowCollection
						.getRepaymentAccount());
				borrowCollection.setInterestRateYes(borrowCollection
						.getInterestRate());

				// 增加债权处理，待还奖励需要扣除已经转出的奖励
				double awardValue = BigDecimalUtil.sub(
						borrowCollection.getRepayAward(),
						borrowCollection.getBondAward());
				if (awardValue > 0
						&& borrowCollection.getRepayAwardStatus() == Constant.REPAY_AWARD_STATUS_NORAML) {
					Global.setTransfer("award", awardValue);
					AbstractExecuter awardRepayExecuter = ExecuterHelper
							.doExecuter("awardRepayExecuter");
					awardRepayExecuter.execute(awardValue, tender.getUser(),
							this.data.getUser());
					borrowCollection
							.setRepayAwardStatus(Constant.REPAY_AWARD_STATUS_PAYED);
					totalLateAwardValue += borrowCollection.getRepayAward();
				}

				collectionDao.update(borrowCollection);
				// 向投资人发送还款成功通知，不做任何资金处理
				Global.setTransfer("collection", borrowCollection);
				Global.setTransfer("user", tender.getUser());
				repay.setRepaymentYesTime(new Date());
				Global.setTransfer("repay", repay);
				Global.setTransfer("borrow", this.data);
				if(this.data.getStyle()==Borrow.STYLE_ONETIME_REPAYMENT)//利随本清
				{
					BaseMsg msg = new BaseMsg(NoticeConstant.REPAY_SUCC);
					msg.doEvent();
				}else if(this.data.getStyle()==Borrow.STYLE_MONTHLY_INTEREST)//先息后本
				{
					if(repaymentCount-1==repay.getPeriod())
					{//利息本金都有
						BaseMsg msg = new BaseMsg(NoticeConstant.REPAY_SUCC_FILC_LAST);
						msg.doEvent();
					}else
					{//每月利息到期还本
						BaseMsg msg = new BaseMsg(NoticeConstant.REPAY_SUCC_FILC);
						msg.doEvent();
					}
				}else if(this.data.getStyle()==Borrow.STYLE_INSTALLMENT_REPAYMENT)//等额本息
				{//利息本金都有
					BaseMsg msg = new BaseMsg(NoticeConstant.REPAY_SUCC_ACPI);
					msg.doEvent();
				}
//				AbstractExecuter repaySuccessExecuter = ExecuterHelper
//						.doExecuter("tenderRepaySuccessExecuter");
//				repaySuccessExecuter.execute(0, tender.getUser(),
//						borrowCollection.getUser());
//				List<User>  userList = theUserDao.getByGroupId(tender.getUser().getBindId());
//				if (userList.size() > 1)
//				{
//					for (int i = 0; i < userList.size(); i++)
//					{
//						if (null != userList.get(i).getWechatOpenId()
//								&&!"".equals(userList.get(i).getWechatOpenId())
//								&&WechatMessageData.A_APP_ID.equals(userList.get(i).getWechatId()))
//						{
//
//							WechatMessage wechatMessage = new WechatMessage();
//							
//							wechatMessage.setAppId(WechatMessageData.A_APP_ID);
//							wechatMessage.setAppSecret(WechatMessageData.A_APP_SECRET);
//							wechatMessage.setType(WechatMessageData.Due_Income);
//							wechatMessage.setFirstData("产品"+this.data.getName()+"到期啦!");
//							wechatMessage.setProductName(this.data.getName());
//							wechatMessage.setUrl("http://www.800bank.com.cn/nb/wechat/account/main.html");
//							wechatMessage.setDateInfo(repay.getRepaymentTime());
//							wechatMessage.setOpenId(userList.get(i).getWechatOpenId());
//							wechatMessage.setProductProfit(this.data.getApr()+"%");
//							wechatMessage.setMoney(tender.getMoney());
//							wechatMessage.setProfit(borrowCollection.getInterest());
//							
////							wechatMessage.setProductRemark("投资金额:"+tender.getMoney()+"元");
//						
//							try {
//								wechatCacheService.sendWechatMessage(wechatMessage);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								logger.info("推送信息异常：" + e.getMessage());
//							} 
//						}
//					}
//				}
			}

			// 处理债权待还
			doBondCollection(repay);

			// 扣除发标人需要支付的还款奖励
			if (totalLateAwardValue > 0) {
				Global.setTransfer("money", totalLateAwardValue);
				AbstractExecuter lateAwardExecuter = ExecuterHelper
						.doExecuter("repayDeductAwardExecuter");
				lateAwardExecuter.execute(totalLateAwardValue, data.getUser(),
						new User(Constant.ADMIN_ID));
			}
		}
	}

	// 处理债权待还
	private void doBondCollection(BorrowRepayment repay) {

		BondTenderDao bondTenderDao = (BondTenderDao) BeanUtil
				.getBean("bondTenderDao");
		BondCollectionDao bondCollectionDao = (BondCollectionDao) BeanUtil
				.getBean("bondCollectionDao");
		List<BondCollection> list = bondCollectionDao
				.getBondCollectionList(repay.getId());
		Bond bond = null;

		if (list == null || list.size() == 0) {
			return;
		}

		for (BondCollection bondCollection : list) {
			BondTender bondTender = bondTenderDao.find(bondCollection
					.getBondTenderId());
			// 增加债权处理，待还本金需要扣除已经转出的债权价值
			double capital = BigDecimalUtil.sub(bondCollection.getCapital(),
					bondCollection.getBondCapital());
			// 增加债权处理，待还利息需要扣除已经转出的利息
			double conllectionInterest = BigDecimalUtil.sub(
					bondCollection.getInterest(),
					bondCollection.getBondInterest());
			// 归还投资人本金
			if (capital > 0) {
				Global.setTransfer("money", capital);
				AbstractExecuter repayTenderExecuter = ExecuterHelper
						.doExecuter("borrowRepayTenderCapitalExecuter");
				repayTenderExecuter.execute(capital, bondTender.getUser(),
						bondCollection.getBond().getUser());

			}
			// 归还投资人利息
			repayBondInterest(bondCollection, bondTender);

			// 根据投资比例归还逾期利息
			if (repay.getLateInterest() > 0) {
				double tenderLateInterest = 0;
				boolean isOpenApi = BaseTPPWay.isOpenApi();
				if (!isOpenApi) {// 如果是标准版，则逾期利息50%给平台
					tenderLateInterest = BigDecimalUtil.mul(
							repay.getLateInterest() / 2,
							(BigDecimalUtil.div(conllectionInterest,
									repay.getInterest())));
				} else {
					tenderLateInterest = BigDecimalUtil.mul(
							repay.getLateInterest(),
							(BigDecimalUtil.div(conllectionInterest,
									repay.getInterest())));
				}
				tenderLateInterest = BigDecimalUtil.round(tenderLateInterest);
				if (tenderLateInterest > 0) {
					Global.setTransfer("money", tenderLateInterest);
					AbstractExecuter repayTenderLateExecuter = ExecuterHelper
							.doExecuter("borrowRepayTenderLateInterestExecuter");
					repayTenderLateExecuter.execute(tenderLateInterest,
							bondTender.getUser(), bondCollection.getBond()
									.getUser());
				}
				bondCollection.setLateDays(repay.getLateDays());
				bondCollection.setLateInterest(tenderLateInterest);
			}

			bondTender.setReceivedAccount(BigDecimalUtil.add(
					bondTender.getReceivedAccount(), capital));
			bondTenderDao.update(bondTender);
			// 更新collection
			bondCollection.setStatus((byte) 1);
			bondCollection.setCollectionYesTime(new Date());
			bondCollection.setCollectionYesAccount(BigDecimalUtil.add(capital,
					conllectionInterest));

			// 增加债权处理，待还奖励需要扣除已经转出的奖励
			double awardValue = BigDecimalUtil.sub(bondCollection.getAward(),
					bondCollection.getBondAward());
			if (awardValue > 0) {
				Global.setTransfer("award", awardValue);
				AbstractExecuter awardRepayExecuter = ExecuterHelper
						.doExecuter("awardRepayExecuter");
				awardRepayExecuter.execute(awardValue, bondTender.getUser(),
						bondCollection.getBond().getUser());
			}
			bondCollectionDao.update(bondCollection);

			// 向投资人发送还款成功通知，不做任何资金处理
			Global.setTransfer("collection", bondCollection);
			Global.setTransfer("user", bondTender.getUser());
			Global.setTransfer("repay", repay);
			if (bond == null) {
				bond = bondCollection.getBond();
			}
			Global.setTransfer("bondName", getBondName(bond));
			AbstractExecuter repaySuccessExecuter = ExecuterHelper
					.doExecuter("bondTenderReceiveSuccessExecuter");
			repaySuccessExecuter.execute(0, bondTender.getUser(),
					bondCollection.getUser());
		}

	}

	private String getBondName(Bond bond) {
		long maxDayId = bond.getDayId();
		Date addTime = bond.getAddTime();
		String bondName = "";
		if (maxDayId < 10) {
			bondName = DateUtil.dateStr7(addTime).substring(4) + "0" + maxDayId;
		} else {
			bondName = DateUtil.dateStr7(addTime).substring(4) + maxDayId;
		}
		return bondName;
	}

	private void repayBondInterest(BondCollection bondCollection,
			BondTender tender) {
		// 增加债权处理，待还利息需要扣除已经转出的利息
		double cInterest = BigDecimalUtil.sub(bondCollection.getInterest(),
				bondCollection.getBondInterest());
		Global.setTransfer("borrow", this.data);
		// 归还投资人利息
		if (cInterest > 0) {
			double borrow_fee = Global.getDouble("borrow_fee");
			double borrowFee = BigDecimalUtil.mul(cInterest, borrow_fee);
			borrowFee = BigDecimalUtil.round(borrowFee);
			// 收回利息
			Global.setTransfer("money", cInterest);
			Global.setTransfer("borrowFee", borrowFee);
			AbstractExecuter repayTenderInterestExecuter = ExecuterHelper
					.doExecuter("borrowRepayTenderInterestExecuter");
			repayTenderInterestExecuter.execute(cInterest, tender.getUser(),
					this.data.getUser());
			// 扣除投资人利息管理费
			if (borrowFee > 0) {
				Global.setTransfer("money", borrowFee);
				AbstractExecuter manageFeeExecuter = ExecuterHelper
						.doExecuter("deductManageFeeExecuter");
				manageFeeExecuter.execute(borrowFee, tender.getUser(),
						new User(Constant.ADMIN_ID));
			}
		}

	}

	@Override
	public void borrowPriorRepayHandleBorrow(BorrowRepayment repay) {
		BorrowRepaymentDao borrowRepaymentDao = (BorrowRepaymentDao) BeanUtil
				.getBean("borrowRepaymentDao");
		double waitOldRpayCapital = borrowRepaymentDao
				.getRemainderCapital(repay.getBorrow().getId()); // 计算剩余待还本金
		double waitRepayInterest = borrowRepaymentDao.getwaitRpayInterest(repay
				.getBorrow().getId(), repay.getPeriod()); // 本次提前还款待还利息总和
		User toUser = new User();
		toUser.setUserId(1L);
		double extensionInterest = repay.getExtensionInterest();
		Global.setTransfer("borrow", this.data);
		// 从用户冻结账户中扣除剩余还款本金
		if (waitOldRpayCapital > 0) {
			Global.setTransfer("money", waitOldRpayCapital);
			AbstractExecuter repayExecuter = ExecuterHelper
					.doExecuter("borrowRepayCapitalExecuter");
			repayExecuter.execute(waitOldRpayCapital, data.getUser(), toUser);
		}
		// 扣除还款利息+罚息
		if (waitRepayInterest > 0
				&& (!(this.data.getBorrowTimeType() == 1 && this.data
						.getStyle() == 4))) {
			Global.setTransfer("money", waitRepayInterest);
			AbstractExecuter interestExecuter = ExecuterHelper
					.doExecuter("borrowRepayInterestExecuter");
			interestExecuter.execute(waitRepayInterest, data.getUser(), toUser);
		}
		// 扣除展期利息
		if (extensionInterest > 0) {
			Global.setTransfer("money", extensionInterest);
			AbstractExecuter extensionExecuter = ExecuterHelper
					.doExecuter("borrowRepayExtensionInterestExecuter");
			extensionExecuter
					.execute(extensionInterest, data.getUser(), toUser);
		}
	}

	@Override
	public void borrowPriorRepayHandleTender(BorrowRepayment repay) {
		BorrowTenderDao tenderDao = (BorrowTenderDao) BeanUtil
				.getBean("borrowTenderDao");
		BorrowCollectionDao collectionDao = (BorrowCollectionDao) BeanUtil
				.getBean("borrowCollectionDao");
		BorrowRepaymentDao borrowRepaymentDao = (BorrowRepaymentDao) BeanUtil
				.getBean("borrowRepaymentDao");
		AccountDao accountDao = (AccountDao) BeanUtil.getBean("accountDao");
		ProductBasicService productBasicService = (ProductBasicService) BeanUtil
				.getBean("productBasicService");
		// 获得提前还款剩余还款的本金
		double money = collectionDao.getRemainderMoney(repay.getBorrow()
				.getId());
		List<BorrowCollection> list = this.getWaitColl(repay);
		if (list != null && list.size() > 0) {
			Global.setTransfer("borrow", this.data);
			ProductBasic productBasic = productBasicService
					.getProductBasicInfo(new Long(data.getType()), data.getId());
			Global.setTransfer("flag", productBasic.getProductTypeFlag());
			for (BorrowCollection borrowCollection : list) {
				BorrowTender tender = tenderDao.find(borrowCollection
						.getTender().getId());
				double cCapital = collectionDao.getRemainderCapital(tender
						.getId()); // 计算投资人剩余待还本金
				double waitRepayInterest = borrowRepaymentDao
						.getwaitRpayInterest(repay.getBorrow().getId(),
								repay.getPeriod()); // 本次提前还款待还利息总和
				double nowInterest = 0; // 当期应收利息
				// 归还投资人本金
				if (cCapital > 0) {
					Global.setTransfer("money", cCapital);
					AbstractExecuter repayTenderExecuter = ExecuterHelper
							.doExecuter("borrowRepayTenderCapitalExecuter");
					repayTenderExecuter.execute(cCapital, tender.getUser(),
							this.data.getUser());
				}
				// 归还投资人利息+罚息
				if (waitRepayInterest > 0) {
					double repayInterest = BigDecimalUtil.mul(
							waitRepayInterest,
							BigDecimalUtil.div(cCapital, money));
					double borrow_fee = Global.getDouble("borrow_fee");
					double borrowFee = BigDecimalUtil.mul(repay.getInterest(),
							borrow_fee);
					// 收回利息
					Global.setTransfer("money", repayInterest);
					AbstractExecuter repayTenderInterestExecuter = ExecuterHelper
							.doExecuter("borrowRepayTenderInterestExecuter");
					repayTenderInterestExecuter.execute(repayInterest,
							tender.getUser(), this.data.getUser());
					double waitInterest = borrowRepaymentDao.getWaitInterest(
							repay.getBorrow().getId(), repay.getPeriod() + 2);
					accountDao.modify(-waitInterest, 0, 0, waitInterest, tender
							.getUser().getUserId());
					// 扣除投资人利息管理费
					if (borrowFee > 0) {
						Global.setTransfer("money", borrowFee);
						AbstractExecuter manageFeeExecuter = ExecuterHelper
								.doExecuter("deductManageFeeExecuter");
						manageFeeExecuter.execute(borrowFee, tender.getUser(),
								new User(Constant.ADMIN_ID));
						borrowCollection.setManageFee(borrowFee);
					}
				}

				tender.setStatus(1);
				tender.setWaitAccount(0);
				tender.setWaitInterest(0);
				tender.setRepaymentYesAccount(cCapital);
				tender.setRepaymentYesInterest(nowInterest);
				// 更新tender记录
				tenderDao.update(tender);

				// 向投资人发送还款成功通知，不做任何资金处理
				Global.setTransfer("collection", borrowCollection);
				AbstractExecuter repaySuccessExecuter = ExecuterHelper
						.doExecuter("tenderRepaySuccessExecuter");
				repaySuccessExecuter.execute(0, tender.getUser(),
						borrowCollection.getUser());
			}
		}
		collectionDao.updatePriorRepayStatus(repay.getBorrow().getId());

	}

	public BorrowCollection getInvestRepayInterest(
			BorrowCollection borrowCollection, BorrowTender tender) {
		// 增加债权处理，待还利息需要扣除已经转出的利息
		double cInterest = 0;
		if ((data.getType() == 119 || data.getType() == 112)
				&& data.getBorrowTimeType() == 1) {
			cInterest = BigDecimalUtil.sub(BigDecimalUtil.sub(
					borrowCollection.getInterest(),
					borrowCollection.getBondInterest()), borrowCollection
					.getRepaymentYesAccount());
		} else {
			cInterest = BigDecimalUtil.sub(borrowCollection.getInterest(),
					borrowCollection.getBondInterest());
		}
		ProductBasicService productBasicService = (ProductBasicService) BeanUtil
				.getBean("productBasicService");
		Global.setTransfer("borrow", this.data);
		ProductBasic productBasic = productBasicService.getProductBasicInfo(
				new Long(data.getType()), data.getId());
		Global.setTransfer("flag", productBasic.getProductTypeFlag());
		// 归还投资人利息
		if (cInterest > 0) {
			double borrow_fee = Global.getDouble("borrow_fee");
			double borrowFee = BigDecimalUtil.mul(cInterest, borrow_fee);
			borrowFee = BigDecimalUtil.round(borrowFee);
			// 收回利息
			Global.setTransfer("money", cInterest);
			Global.setTransfer("borrowFee", borrowFee);
			AbstractExecuter repayTenderInterestExecuter = ExecuterHelper
					.doExecuter("borrowRepayTenderInterestExecuter");
			repayTenderInterestExecuter.execute(cInterest, tender.getUser(),
					this.data.getUser());
			// 扣除投资人利息管理费
			if (borrowFee > 0) {
				Global.setTransfer("money", borrowFee);
				AbstractExecuter manageFeeExecuter = ExecuterHelper
						.doExecuter("deductManageFeeExecuter");
				manageFeeExecuter.execute(borrowFee, tender.getUser(),
						new User(Constant.ADMIN_ID));
				borrowCollection.setManageFee(borrowCollection.getManageFee());
			}
		}
		return borrowCollection;
	}

	@Override
	public void validBeforeCompensate(BorrowRepayment borrowRepayment) {
		if (borrowRepayment == null || borrowRepayment.getStatus() == 1) {
			throw new BorrowException("该期借款已经还款,请不要重复操作！", 1);
		}
		if (this.data.getStatus() != 6 && this.data.getStatus() != 7) {
			throw new BorrowException("当前借款标的状态不能进行还款操作！", 1);
		}
		BorrowRepaymentDao borrowRepaymentDao = (BorrowRepaymentDao) BeanUtil
				.getBean("borrowRepaymentDao");
		boolean hasAhead = borrowRepaymentDao.hasRepaymentAhead(borrowRepayment
				.getPeriod(), borrowRepayment.getBorrow().getId());
		if (hasAhead) {
			throw new BorrowException("还有尚未还款的借款！", 1);
		}
	}

	@Override
	public BorrowRepayment repay(BorrowModel model) {
		return null;
	}

	public List<BorrowCollection> getWaitColl(BorrowRepayment repay) {
		List<BorrowCollection> collList = new ArrayList<BorrowCollection>();
		if (repay.getMerBillNo() != null && BaseTPPWay.isOpenApi()) {
			int apiCode = TPPWay.API_CODE;
			switch (apiCode) {
			case 1:
				break;
			case 2:
				TppIpsPayDao tppIpsPayDao = (TppIpsPayDao) BeanUtil
						.getBean("tppIpsPayDao");
				collList = tppIpsPayDao.getCollByIpsNo(repay.getMerBillNo(),
						TppIpsPay.STATUS_SUCCESS);
				break;
			case 3:
				BorrowCollectionDao collectionDao = (BorrowCollectionDao) BeanUtil
						.getBean("borrowCollectionDao");
				collList = collectionDao.list(repay.getBorrow().getId(),
						repay.getPeriod());
				break;
			default:
				break;
			}
		} else {
			BorrowCollectionDao collectionDao = (BorrowCollectionDao) BeanUtil
					.getBean("borrowCollectionDao");
			collList = collectionDao.list(repay.getBorrow().getId(),
					repay.getPeriod());
		}
		return collList;
	}
}
