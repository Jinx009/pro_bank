package com.rongdu.manage.action.operatingStatistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.domain.AccountRecharge;
import com.rongdu.p2psys.account.model.AccountMoneyModel;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

@SuppressWarnings("rawtypes")
public class OperatingInfoAction extends BaseAction {
	
	@Resource
	private UserService userService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private BorrowService borrowService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private BorrowTenderService borrowTenderService;
	@Resource
	private AccountCashService accountCashService;
	@Resource
	private AccountService accountService;
	@Resource
	private BorrowCollectionService borrowCollectionService;
	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private PpfundInService ppfundInService;
	
	/**
	 * 用户分析页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/operatingStatistics/userAnalysis")
	public String userAnalysis() throws Exception {
		return "userAnalysis";
	}

	/**
	 * 用户分析信息
	 * 
	 * @throws Exception
	 */
	@Action("/modules/operatingStatistics/userAnalysisInfo")
	public void userAnalysisInfo() throws Exception {
		// 转换JSON字符串用map
		Map<String, Object> data = new HashMap<String, Object>();
		String startTime = paramString("startTime");//开始时间
		String endTime = paramString("endTime");//结束时间
		
		// 成功注册用户总数
		int userCount = userService.count(startTime, endTime);
		data.put("userCount", userCount);
		
		// 实名认证成功用户数
		int userRealNamedCount = userIdentifyService.countByRealName(1, startTime, endTime);
		data.put("userRealNamedCount", userRealNamedCount);
		
		// 成功充值一次（含）以上的用户数
		int rechargedUserCount = accountRechargeService.rechargedUserCount(startTime, endTime);
		data.put("rechargedUserCount", rechargedUserCount);
		
		// 成功投资一次（含）以上的用户数
		int investUserCount = borrowTenderService.getInvestUserCount(startTime, endTime);
		data.put("investUserCount", investUserCount);
		
		// 已注册但未实名认证用户数
		int userRealNamelessCount = userIdentifyService.countByRealName(0, startTime, endTime);
		data.put("userRealNamelessCount", userRealNamelessCount);
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 金额分析页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/operatingStatistics/momenyAnalysis")
	public String momenyAnalysis() throws Exception {
		return "momenyAnalysis";
	}

	/**
	 * 金额分析信息
	 * 
	 * @throws Exception
	 */
	@Action("/modules/operatingStatistics/momenyAnalysisInfo")
	public void momenyAnalysisInfo() throws Exception {
		// 转换JSON字符串用map
		Map<String, Object> data = new HashMap<String, Object>();
		String startTime = paramString("startTime");//开始时间
		String endTime = paramString("endTime");//结束时间
		
		// 投资人累计充值金额总和
		double rechargedAllMomeny = accountRechargeService.rechargedAllMomeny(startTime, endTime);
		data.put("rechargedAllMomeny", rechargedAllMomeny);
		
		// 投资人累计投资金额总和
		double tenderAccount = borrowTenderService.tenderAccount(startTime, endTime);
		data.put("tenderAccount", tenderAccount);
		
		// 投资人累计收到利息总和（实际收到）
		double tenderAllInterest = borrowCollectionService.getRepaymentYesInterest(startTime, endTime);
		data.put("tenderAllInterest", tenderAllInterest);
		
		// 投资人累计提现金额总和
		double allCashMomeny = accountCashService.allCashMomeny(startTime, endTime);
		data.put("allCashMomeny", allCashMomeny);
		
		// 投资人账户可用余额总和，账户余额无法按照某一个时间段进行统计
		double allUseMoney = accountService.getAllUseMoney();
		data.put("allUseMoney", allUseMoney);

		printJson(getStringOfJpaObj(data));
	}
	
	
	/**
	 * 项目数量分析页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/operatingStatistics/itemCountAnalysis")
	public String itemCountAnalysis() throws Exception {
		return "itemCountAnalysis";
	}

	/**
	 * 项目数量分析信息
	 * 
	 * @throws Exception
	 */
	@Action("/modules/operatingStatistics/itemCountAnalysisInfo")
	public void itemCountAnalysisInfo() throws Exception {
		// 转换JSON字符串用map
		Map<String, Object> data = new HashMap<String, Object>();
		
		String startTime = paramString("startTime");//开始时间
		String endTime = paramString("endTime");//结束时间
		
		// 项目总数
		int allCount = borrowService.getAllCount(99, startTime, endTime);
		data.put("allCount", allCount);
		
		// 逾期项目数
		int allOverduedCount = borrowService.getAllOverduedCount(startTime, endTime);
		data.put("allOverduedCount", allOverduedCount);
		
		// 正常项目数
		int allNormalCount  = borrowService.getBorrowCount(startTime, endTime, 0,1,3,6,7,8,19);
		data.put("allNormalCount", allNormalCount);
		
		//失败项目数
		int failureCount = allCount - allNormalCount;
		data.put("failureCount", failureCount);
		
		//所有已还款项目数
		int repayCount = borrowService.getBorrowCount(startTime, endTime, 8);
		data.put("repayCount", repayCount);
		
		//按时还款数
		int timeLyRepayCount = repayCount - allOverduedCount;
		data.put("timeLyRepayCount", timeLyRepayCount);
		
		//所有未完成项目数
		int unfinishedCount = borrowService.getBorrowCount(startTime, endTime, 0,1,3,6,7,19);
		data.put("unfinishedCount", unfinishedCount);
		
		// 待登记项目数(环迅为待登记，其余为待初审)
		int registerelessCount = borrowService.getBorrowCount(startTime, endTime, 0);
		data.put("registerelessCount", registerelessCount);
		
		
		// 待初审项目数（为环迅待初审）
		int registeredCount = borrowService.getBorrowCount(startTime, endTime, 9);
		data.put("registeredCount", registeredCount);
		
		// 投资中项目数
		int investingCount = borrowService.getInviteCount(startTime, endTime);
		data.put("investingCount", investingCount);
		
		// 待满标复审项目数
		int verifyFullCount = borrowService.getVerifyFullCount(startTime, endTime);
		data.put("verifyFullCount", verifyFullCount);
		
		// 还款中项目数
		int repayingCount6 = borrowService.getBorrowCount(startTime, endTime, 6);
		int repayingCount7 = borrowService.getBorrowCount(startTime, endTime, 7);
		int repayingCount = repayingCount6 + repayingCount7;
		data.put("repayingCount", repayingCount);
		
		// 逾期中项目数
		int allOverdueingCount = borrowService.getAllOverdueingCount(startTime, endTime);
		data.put("allOverdueingCount", allOverdueingCount);

		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 项目金额分析页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/operatingStatistics/itemMomenyAnalysis")
	public String itemMomenyAnalysis() throws Exception {
		return "itemMomenyAnalysis";
	}

	/**
	 * 项目金额分析信息
	 * 
	 * @throws Exception
	 */
	@Action("/modules/operatingStatistics/itemMomenyAnalysisInfo")
	public void itemMomenyAnalysisInfo() throws Exception {
		// 转换JSON字符串用map
		Map<String, Object> data = new HashMap<String, Object>();
		
		String startTime = paramString("startTime");//开始时间
		String endTime = paramString("endTime");//结束时间
		
		// 项目总金额
		double allMomeny = borrowService.getBorrowAccount(startTime, endTime, 99);
		data.put("allMomeny", allMomeny);
		
		// 逾期项目金额
		double allOverduedMomeny = borrowService.getAllOverduedMomeny(startTime, endTime);
		data.put("allOverduedMomeny", allOverduedMomeny);
		
		// 正常项目金额
		double allNormalMomeny  = borrowService.getBorrowAccount(startTime, endTime, 0,1,3,6,7,8,19);
		data.put("allNormalMomeny", allNormalMomeny);
		
		//失败项目金额
		double failureAccount = allMomeny - allNormalMomeny;
		data.put("failureAccount", failureAccount);
				
		//所有已还款金额
		double repayAccount = borrowService.getBorrowAccount(startTime, endTime, 8);
		data.put("repayAccount", repayAccount);
		
		//按时还款金额
		double timeLyRepayAccount = repayAccount - allOverduedMomeny;
		data.put("timeLyRepayAccount", timeLyRepayAccount);
		
		//所有未完成项目金额
		double unfinishedAccount = borrowService.getBorrowAccount(startTime, endTime, 0,1,3,6,7,19);
		data.put("unfinishedAccount", unfinishedAccount);
		
		// 待登记项目金额
		double registerelessMomeny = borrowService.getBorrowAccount(startTime, endTime, 0);
		data.put("registerelessMomeny", registerelessMomeny);
		
		// 待初审项目金额
		double registeredMomeny = borrowService.getBorrowAccount(startTime, endTime, 9);
		data.put("registeredMomeny", registeredMomeny);
		
		// 投资中项目金额
		double investingMomeny = borrowService.getInviteMoney(startTime, endTime);
		data.put("investingMomeny", investingMomeny);
		
		// 待满标复审项目金额
		double verifyFullMomeny = borrowService.getVerifyFullMoney(startTime, endTime);
		data.put("verifyFullMomeny", verifyFullMomeny);
		
		// 还款中项目金额
		double repayingMomeny6 = borrowService.getBorrowAccount(startTime, endTime, 6);
		double repayingMomeny7 = borrowService.getBorrowAccount(startTime, endTime, 7);
		double repayingMomeny = BigDecimalUtil.add(repayingMomeny6, repayingMomeny7);
		data.put("repayingMomeny", repayingMomeny);
		
		// 逾期中项目金额
		double allOverdueingMomeny = borrowService.getAllOverdueingMoney(startTime, endTime);
		data.put("allOverdueingMomeny", allOverdueingMomeny);

		printJson(getStringOfJpaObj(data));
		
	}
	
	/**
	 * 充值统计页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeStaticsPage")
	public String accountRechargeStaticsPage() throws Exception {
		
		return "accountRechargeStaticsPage";
	}
	
	/**
	 * 充值统计页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeStaticsPage2")
	public String accountRechargeStaticsPage2() throws Exception {
		
		return "accountRechargeStaticsPage2";
	}
	
	/**
	 * 充值统计
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accountRechargeStatics")
	public void accountRechargeStatics() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Double> accounts = new ArrayList<Double>();
		List<String> dates = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		String time = paramString("time");
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		if (StringUtil.isNotBlank(startTime)) {
			Date startDate = DateUtil.valueOf(startTime);
			Date endDate = DateUtil.valueOf(endTime);
			Calendar c1 = Calendar.getInstance();
			c.setTime(startDate);
			c1.setTime(endDate);
			while (!c.after(c1)) {
				String date = DateUtil.dateStr2(c1.getTime());
				double account = accountRechargeService.getAccountRechargeSumByDate(date);
				c1.add(Calendar.DATE, -1);
				dates.add(date.substring(5, date.length()));
				accounts.add(account);
			}
		}else {
			if ("year".equals(time)) {
				for (int i=0; i<12;i++) {
					String date = DateUtil.dateStr(c.getTime(),"yyyy-MM");
					double account = accountRechargeService.getAccountRechargeSumByDate(date);
					c.add(Calendar.MONTH, -1);
					dates.add(date);
					accounts.add(account);
				}
			} else if ("month".equals(time)) {
				String firstDate = DateUtil.dateStr2(c.getTime());
				for (int i=0; i<30;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double account = accountRechargeService.getAccountRechargeSumByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					accounts.add(account);
				}
			} else if ("nowMonth".equals(time)) {//当月
				c.add(Calendar.MONTH, 1);
				c.set(Calendar.DAY_OF_MONTH, 0);
				String firstDate = DateUtil.dateStr2(c.getTime());
				accounts.add(accountRechargeService.getAccountRechargeSumByDate(firstDate));
				int days = c.getActualMaximum(Calendar.DATE);
				for (int i=0; i<days;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double account = accountRechargeService.getAccountRechargeSumByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					accounts.add(account);
				}
			} else if ("nowYear".equals(time)) {//当年
				 int currentYear = c.get(Calendar.YEAR);
				 	c.clear();
				 	c.set(Calendar.YEAR, currentYear);  
			        c.roll(Calendar.DAY_OF_YEAR, -1);  
				for (int i=0; i<12;i++) {
					String date = DateUtil.dateStr(c.getTime(),"yyyy-MM");
					double account = accountRechargeService.getAccountRechargeSumByDate(date);
					c.add(Calendar.MONTH, -1);
					dates.add(date);
					accounts.add(account);
				}
			} else {
				for (int i=0; i<7;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double account = accountRechargeService.getAccountRechargeSumByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					accounts.add(account);
				}
			}
		}
		Collections.reverse(dates);
		Collections.reverse(accounts);
		data.put("dates", dates.toArray());
		data.put("accounts", accounts.toArray());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 充值统计
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/newAccountRechargeStatics")
	public void newAccountRechargeStatics() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
//		Map<String, Object> accountData = new HashMap<String, Object>();
		List<AccountRecharge> accounts = new ArrayList<AccountRecharge>();
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		double account=0.0;
//		if (StringUtil.isNotBlank(startTime)) {
		  account = accountRechargeService.getNewAccountRechargeSumByDate(startTime,endTime);
//		}
//		accountData.put("accountIn", account);
//		accounts.add(accountData);
		AccountRecharge ar=new AccountRecharge();
		ar.setMoney(account);
		accounts.add(ar);
		data.put("total", 1);
		data.put("rows", accounts);
		System.out.println(getStringOfJpaObj(data));
		printJson(getStringOfJpaObj(data));
//		data.put("accounts", accounts.toArray());
//		printJson(getStringOfJpaObj(data));
	}
	@Action("/modules/account/accountRecharge/accountGetMoneyPage")
	public String accountGetMoneyPage() throws Exception {
		
		return "accountGetMoneyPage";
	}
	@Action("/modules/account/accountRecharge/borrowCollectionMoneyPage")
	public String borrowCollectionMoneyPage() throws Exception {
		
		return "borrowCollectionMoneyPage";
	}
	/**
	 * 充值统计
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/accessAccountMoney")
	public void accessAccountMoney() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
//		Map<String, Object> accountData = new HashMap<String, Object>();
		List<AccountRecharge> accounts = new ArrayList<AccountRecharge>();
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		double account=0.0;
//		if (StringUtil.isNotBlank(startTime)) {
		  account = accountRechargeService.getAccessAccontMoney(startTime,endTime);
//		}
//		accountData.put("accountIn", account);
//		accounts.add(accountData);
		AccountRecharge ar=new AccountRecharge();
		ar.setMoney(account);
		accounts.add(ar);
		data.put("total", 1);
		data.put("rows", accounts);
		System.out.println(getStringOfJpaObj(data));
		printJson(getStringOfJpaObj(data));
//		data.put("accounts", accounts.toArray());
//		printJson(getStringOfJpaObj(data));
	}
	
	
	/**
	 * 提现统计页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/accountCashStaticsPage")
	public String accountCashStaticsPage() throws Exception {
		
		return "accountCashStaticsPage";
	}
	
	/**
	 * 提现统计
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/cash/accountcash/accountCashStatics")
	public void accountCashStatics() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Double> accounts = new ArrayList<Double>();
		List<String> dates = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		String time = paramString("time");
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		if (StringUtil.isNotBlank(startTime)) {
			Date startDate = DateUtil.valueOf(startTime);
			Date endDate = DateUtil.valueOf(endTime);
			Calendar c1 = Calendar.getInstance();
			c.setTime(startDate);
			c1.setTime(endDate);
			while (!c.after(c1)) {
				String date = DateUtil.dateStr2(c1.getTime());
				double account = accountCashService.getAccountCashSumByDate(date);
				c1.add(Calendar.DATE, -1);
				dates.add(date.substring(5, date.length()));
				accounts.add(account);
			}
		}else {
			if ("year".equals(time)) {
				for (int i=0; i<12;i++) {
					String date = DateUtil.dateStr(c.getTime(),"yyyy-MM");
					double account = accountCashService.getAccountCashSumByDate(date);
					c.add(Calendar.MONTH, -1);
					dates.add(date);
					accounts.add(account);
				}
			} else if ("month".equals(time)) {
				String firstDate = DateUtil.dateStr2(c.getTime());
				for (int i=0; i<30;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double account = accountCashService.getAccountCashSumByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					accounts.add(account);
				}
			} else if ("nowMonth".equals(time)) {//当月
				c.add(Calendar.MONTH, 1);
				c.set(Calendar.DAY_OF_MONTH, 0);
				int days = c.getActualMaximum(Calendar.DATE);
				for (int i=0; i<days;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double account = accountCashService.getAccountCashSumByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					accounts.add(account);
				}
			} else if ("nowYear".equals(time)) {//当年
				 int currentYear = c.get(Calendar.YEAR);
				 	c.clear();
				 	c.set(Calendar.YEAR, currentYear);  
			        c.roll(Calendar.DAY_OF_YEAR, -1);  
				for (int i=0; i<12;i++) {
					String date = DateUtil.dateStr(c.getTime(),"yyyy-MM");
					double account = accountCashService.getAccountCashSumByDate(date);
					c.add(Calendar.MONTH, -1);
					dates.add(date);
					accounts.add(account);
				}
			} else {
				for (int i=0; i<7;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double account = accountCashService.getAccountCashSumByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					accounts.add(account);
				}
			}
		}
		Collections.reverse(dates);
		Collections.reverse(accounts);
		data.put("dates", dates.toArray());
		data.put("accounts", accounts.toArray());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 还款统计页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/repayment/borrowRepaymentStaticsPage")
	public String borrowRepaymentStaticsPage() throws Exception {
		double rate = Global.getDouble("repayment_rate");
		request.setAttribute("rate", rate);
		return "borrowRepaymentStaticsPage";
	}
	
	/**
	 * 还款监管
	 * 
	 * @throws Exception
	 */
	@Action("/modules/loan/repayment/borrowRepaymentStatics")
	public void borrowRepaymentStatics() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Double> noRepayments = new ArrayList<Double>();
		List<Double> repayments = new ArrayList<Double>();
		List<Double> noRepaymentsRateValue = new ArrayList<Double>();
		List<Double> repaymentsRateValue = new ArrayList<Double>();
		double rate = Global.getDouble("repayment_rate");
		List<String> dates = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		String time = paramString("time");
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		if (StringUtil.isNotBlank(startTime)) {
			Date startDate = DateUtil.valueOf(startTime);
			Date endDate = DateUtil.valueOf(endTime);
			Calendar c1 = Calendar.getInstance();
			c.setTime(startDate);
			c1.setTime(endDate);
			while (!c.after(c1)) {
				String date = DateUtil.dateStr2(c1.getTime());
				double noRepayment = borrowRepaymentService.getRepaymentNoByDate(date);
				double repaymet = borrowRepaymentService.getRepaymentYesByDate(date);
				c1.add(Calendar.DATE, -1);
				dates.add(date.substring(5, date.length()));
				noRepayments.add(noRepayment);
				repayments.add(repaymet);
				noRepaymentsRateValue.add(BigDecimalUtil.mul(noRepayment,rate));
				repaymentsRateValue.add(BigDecimalUtil.mul(repaymet,rate));
			}
		}else {
			if ("year".equals(time)) {
				for (int i=0; i<12;i++) {
					String date = DateUtil.dateStr(c.getTime(),"yyyy-MM");
					double noRepayment = borrowRepaymentService.getRepaymentNoByDate(date);
					double repaymet = borrowRepaymentService.getRepaymentYesByDate(date);
					c.add(Calendar.MONTH, -1);
					dates.add(date);
					noRepayments.add(noRepayment);
					repayments.add(repaymet);
					noRepaymentsRateValue.add(BigDecimalUtil.mul(noRepayment,rate));
					repaymentsRateValue.add(BigDecimalUtil.mul(repaymet,rate));
				}
			} else if ("month".equals(time)) {
				String firstDate = DateUtil.dateStr2(c.getTime());
				for (int i=0; i<30;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double noRepayment = borrowRepaymentService.getRepaymentNoByDate(date);
					double repaymet = borrowRepaymentService.getRepaymentYesByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					noRepayments.add(noRepayment);
					repayments.add(repaymet);
					noRepaymentsRateValue.add(BigDecimalUtil.mul(noRepayment,rate));
					repaymentsRateValue.add(BigDecimalUtil.mul(repaymet,rate));
				}
			} else if ("nowMonth".equals(time)) {//当月
				c.add(Calendar.MONTH, 1);
				c.set(Calendar.DAY_OF_MONTH, 0);
				String firstDate = DateUtil.dateStr2(c.getTime());
				noRepayments.add(borrowRepaymentService.getRepaymentNoByDate(firstDate));
				repayments.add(borrowRepaymentService.getRepaymentYesByDate(firstDate));
				noRepaymentsRateValue.add(BigDecimalUtil.mul(borrowRepaymentService.getRepaymentNoByDate(firstDate),rate));
				repaymentsRateValue.add(BigDecimalUtil.mul(borrowRepaymentService.getRepaymentYesByDate(firstDate),rate));
				int days = c.getActualMaximum(Calendar.DATE);
				for (int i=0; i<days;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double noRepayment = borrowRepaymentService.getRepaymentNoByDate(date);
					double repaymet = borrowRepaymentService.getRepaymentYesByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					noRepayments.add(noRepayment);
					repayments.add(repaymet);
					noRepaymentsRateValue.add(BigDecimalUtil.mul(noRepayment,rate));
					repaymentsRateValue.add(BigDecimalUtil.mul(repaymet,rate));
				}
			} else if ("nowYear".equals(time)) {//当年
				 int currentYear = c.get(Calendar.YEAR);
				 	c.clear();
				 	c.set(Calendar.YEAR, currentYear);  
			        c.roll(Calendar.DAY_OF_YEAR, -1);  
				for (int i=0; i<12;i++) {
					String date = DateUtil.dateStr(c.getTime(),"yyyy-MM");
					double noRepayment = borrowRepaymentService.getRepaymentNoByDate(date);
					double repaymet = borrowRepaymentService.getRepaymentYesByDate(date);
					c.add(Calendar.MONTH, -1);
					dates.add(date);
					noRepayments.add(noRepayment);
					repayments.add(repaymet);
					noRepaymentsRateValue.add(BigDecimalUtil.mul(noRepayment,rate));
					repaymentsRateValue.add(BigDecimalUtil.mul(repaymet,rate));
				}
			} else {
				for (int i=0; i<7;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double noRepayment = borrowRepaymentService.getRepaymentNoByDate(date);
					double repaymet = borrowRepaymentService.getRepaymentYesByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					noRepayments.add(noRepayment);
					repayments.add(repaymet);
					noRepaymentsRateValue.add(BigDecimalUtil.mul(noRepayment,rate));
					repaymentsRateValue.add(BigDecimalUtil.mul(repaymet,rate));
				}
			}
		}
		Collections.reverse(dates);
		Collections.reverse(noRepayments);
		Collections.reverse(repayments);
		Collections.reverse(repaymentsRateValue);
		Collections.reverse(noRepaymentsRateValue);
		data.put("dates", dates.toArray());
		data.put("noRepayments", noRepayments.toArray());
		data.put("repayments", repayments.toArray());
		data.put("repaymentsRateValue", repaymentsRateValue.toArray());
		data.put("noRepaymentsRateValue", noRepaymentsRateValue.toArray());
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 现金管理转出监管 页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/loan/ppfund/ppfundOutStaticsPage")
	public String ppfundOutStaticsPage() throws Exception {
		double rate = Global.getDouble("ppfund_rate");
		request.setAttribute("rate", rate);
		return "ppfundOutStaticsPage";
	}
	
	/**
	 * 现金管理转出监管
	 * 
	 * @throws Exception
	 */
	@Action("/modules/loan/ppfund/ppfundOutStatics")
	public void ppfundOutStatics() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Double> outAmounts = new ArrayList<Double>();
		List<Double> inAmounts = new ArrayList<Double>();
		List<Double> outAmountRateValue = new ArrayList<Double>();
		List<Double> inAmountRateValue = new ArrayList<Double>();
		//财务计算比例
		double rate = Global.getDouble("ppfund_rate");
		
		List<String> dates = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		String time = paramString("time");
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		if (StringUtil.isNotBlank(startTime)) {
			Date startDate = DateUtil.valueOf(startTime);
			Date endDate = DateUtil.valueOf(endTime);
			Calendar c1 = Calendar.getInstance();
			c.setTime(startDate);
			c1.setTime(endDate);
			while (!c.after(c1)) {
				String date = DateUtil.dateStr2(c1.getTime());
				double outAmount = ppfundInService.getOutAmountByDate(date);
				double inAmount = ppfundInService.getInAmountByDate(date);
				c1.add(Calendar.DATE, -1);
				dates.add(date.substring(5, date.length()));
				outAmounts.add(outAmount);
				inAmounts.add(inAmount);
				outAmountRateValue.add(BigDecimalUtil.mul(outAmount,rate));
				inAmountRateValue.add(BigDecimalUtil.mul(inAmount,rate));
			}
		}else {
			if ("year".equals(time)) {
				for (int i=0; i<12;i++) {
					String date = DateUtil.dateStr(c.getTime(),"yyyy-MM");
					double outAmount = ppfundInService.getOutAmountByDate(date);
					double inAmount = ppfundInService.getInAmountByDate(date);
					c.add(Calendar.MONTH, -1);
					dates.add(date);
					outAmounts.add(outAmount);
					inAmounts.add(inAmount);
					outAmountRateValue.add(BigDecimalUtil.mul(outAmount,rate));
					inAmountRateValue.add(BigDecimalUtil.mul(inAmount,rate));
				}
			} else if ("month".equals(time)) {
				String firstDate = DateUtil.dateStr2(c.getTime());
				for (int i=0; i<30;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double outAmount = ppfundInService.getOutAmountByDate(date);
					double inAmount = ppfundInService.getInAmountByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					outAmounts.add(outAmount);
					inAmounts.add(inAmount);
					outAmountRateValue.add(BigDecimalUtil.mul(outAmount,rate));
					inAmountRateValue.add(BigDecimalUtil.mul(inAmount,rate));
				}
			} else if ("nowMonth".equals(time)) {//当月
				c.add(Calendar.MONTH, 1);
				c.set(Calendar.DAY_OF_MONTH, 0);
				String firstDate = DateUtil.dateStr2(c.getTime());
				outAmounts.add(ppfundInService.getOutAmountByDate(firstDate));
				inAmounts.add(ppfundInService.getInAmountByDate(firstDate));
				outAmountRateValue.add(BigDecimalUtil.mul(ppfundInService.getOutAmountByDate(firstDate),rate));
				inAmountRateValue.add(BigDecimalUtil.mul(ppfundInService.getInAmountByDate(firstDate),rate));
				int days = c.getActualMaximum(Calendar.DATE);
				for (int i=0; i<days;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double outAmount = ppfundInService.getOutAmountByDate(date);
					double inAmount = ppfundInService.getInAmountByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					outAmounts.add(outAmount);
					inAmounts.add(inAmount);
					outAmountRateValue.add(BigDecimalUtil.mul(outAmount,rate));
					inAmountRateValue.add(BigDecimalUtil.mul(inAmount,rate));
				}
			} else if ("nowYear".equals(time)) {//当年
				 int currentYear = c.get(Calendar.YEAR);
				 	c.clear();
				 	c.set(Calendar.YEAR, currentYear);  
			        c.roll(Calendar.DAY_OF_YEAR, -1);  
				for (int i=0; i<12;i++) {
					String date = DateUtil.dateStr(c.getTime(),"yyyy-MM");
					double outAmount = ppfundInService.getOutAmountByDate(date);
					double inAmount = ppfundInService.getInAmountByDate(date);
					c.add(Calendar.MONTH, -1);
					dates.add(date);
					outAmounts.add(outAmount);
					inAmounts.add(inAmount);
					outAmountRateValue.add(BigDecimalUtil.mul(outAmount,rate));
					inAmountRateValue.add(BigDecimalUtil.mul(inAmount,rate));
				}
			} else {
				for (int i=0; i<7;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double outAmount = ppfundInService.getOutAmountByDate(date);
					double inAmount = ppfundInService.getInAmountByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					outAmounts.add(outAmount);
					inAmounts.add(inAmount);
					outAmountRateValue.add(BigDecimalUtil.mul(outAmount,rate));
					inAmountRateValue.add(BigDecimalUtil.mul(inAmount,rate));
				}
			}
		}
		Collections.reverse(dates);
		Collections.reverse(outAmounts);
		Collections.reverse(inAmounts);
		Collections.reverse(outAmountRateValue);
		Collections.reverse(inAmountRateValue);
		data.put("dates", dates.toArray());
		data.put("outAmounts", outAmounts.toArray());
		data.put("inAmounts", inAmounts.toArray());
		data.put("outAmountRateValue", outAmountRateValue.toArray());
		data.put("inAmountRateValue", inAmountRateValue.toArray());
		printJson(getStringOfJpaObj(data));
	}
	@Action("/modules/account/accountRecharge/borrowCollectionMoneyPage2")
	public String borrowCollectionMoneyPage2() throws Exception {
		
		return "borrowCollectionMoneyPage2";
	}
	@Action("/modules/account/accountRecharge/ppfundCollectionMoneyPage")
	public String ppfundCollectionMoneyPage() throws Exception {
		
		return "ppfundCollectionMoneyPage";
	}
	/**
	 * 充值统计
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/borrowCollectionAccountMoney")
	public void borrowCollectionAccountMoney() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		int pageNo = paramInt("page");
		int rowCount = paramInt("rows");
		PageDataList<AccountMoneyModel> pageDataList_ = accountRechargeService.getBorrowCollectionMoney(startTime,endTime,pageNo,rowCount);
		data.put("total", pageDataList_.getPage().getTotal());
		data.put("rows", pageDataList_.getList());
		printJson(getStringOfJpaObj(data));
	}
	/**
	 * 充值统计
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/borrowCollectionAccountMoney2")
	public void borrowCollectionAccountMoney2() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		int pageNo = paramInt("page");
		int rowCount = paramInt("rows");
		PageDataList<AccountMoneyModel> pageDataList_ = accountRechargeService.getBorrowCollectionMoney2(startTime,endTime,pageNo,rowCount);
		data.put("total", pageDataList_.getPage().getTotal());
		data.put("rows", pageDataList_.getList());
		printJson(getStringOfJpaObj(data));
	}
	/**
	 * 充值统计
	 * 
	 * @throws Exception
	 */
	@Action("/modules/account/accountRecharge/ppfundCollectionAccountMoney")
	public void ppfundCollectionAccountMoney() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		int pageNo = paramInt("page");
		int rowCount = paramInt("rows");
		PageDataList<AccountMoneyModel> pageDataList_ = accountRechargeService.getPpfundCollectionMoney(startTime, endTime, pageNo, rowCount);
		data.put("total", pageDataList_.getPage().getTotal());
		data.put("rows", pageDataList_.getList());
		printJson(getStringOfJpaObj(data));
	}
	@Action("/modules/account/accountRecharge/redPacketAccountMoneyPage")
	public String redPacketAccountMoneyPage() throws Exception {
		
		return "redPacketAccountMoneyPage";
	}
	
	@Action("/modules/account/accountRecharge/redPacketAccountMoney")
	public void redPacketAccountMoney() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		int pageNo = paramInt("page");
		int rowCount = paramInt("rows");
		PageDataList<AccountMoneyModel> pageDataList_ = accountRechargeService.getRedPacketMoney(startTime, endTime, pageNo, rowCount);
		data.put("total", pageDataList_.getPage().getTotal());
		data.put("rows", pageDataList_.getList());
		printJson(getStringOfJpaObj(data));
	}
	
	@Action("/modules/account/accountRecharge/recommendMoneyPage")
	public String recommendMoneyPage() throws Exception {
		
		return "recommendMoneyPage";
	}
	
	@Action("/modules/account/accountRecharge/recommendMoney")
	public void recommendMoney() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		int pageNo = paramInt("page");
		int rowCount = paramInt("rows");
		PageDataList<AccountMoneyModel> pageDataList_ = accountRechargeService.getRecommendMoney(startTime, endTime, pageNo, rowCount);
		data.put("total", pageDataList_.getPage().getTotal());
		data.put("rows", pageDataList_.getList());
		printJson(getStringOfJpaObj(data));
	}
}
