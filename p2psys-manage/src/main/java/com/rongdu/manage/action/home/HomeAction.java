package com.rongdu.manage.action.home;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.rongdu.common.util.DateUtil;
import com.rongdu.p2psys.account.service.AccountCashService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.service.UserCertificationApplyService;
import com.rongdu.p2psys.user.service.UserCertificationService;
import com.rongdu.p2psys.user.service.UserCreditApplyService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 首页
 * 
 * @version 2.0
 * @since 2014-4-24
 */
@SuppressWarnings("rawtypes")
public class HomeAction extends BaseAction {

	@Resource
	private BorrowService borrowService;
	@Resource
	private BorrowTenderService borrowTenderService;
	@Resource
	private UserCreditApplyService userCreditApplyService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserCertificationService userCertificationService;
	@Resource
	private UserCertificationApplyService userCertificationApplyService;
	@Resource
	private AccountCashService accountCashService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private UserService userService;
	@Resource
	private BorrowCollectionService borrowCollectionService;
	@Resource
	private PpfundInService ppfundInService;
	
	private Map<String, Object> data;
	
	@Action("/index")
	public String index() throws Exception {
		return "loginPage";
	}

	@Action("/home")
	public String home() throws Exception {
		return "home";
	}

	/**
	 * 首页待办事项
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/home/todoManager")
	public String todoManager() throws Exception {
		// 发标待审
		int trialBorrowCount;
		if (isOpenApi() && apiCode() == TPPWay.API_CODE_IPS) { 
			trialBorrowCount = (int)borrowService.ipsTrialCount();
		} else {
			trialBorrowCount = borrowService.trialCount(0);
		}
		request.setAttribute("trialBorrowCount", trialBorrowCount);
		// 满标复审
		int fullBorrowCount = borrowService.fullCount(1);
		request.setAttribute("fullBorrowCount", fullBorrowCount);
		// 实名认证
		int realNameCount = userIdentifyService.countByRealName(2);
		request.setAttribute("realNameCount", realNameCount);
		// 信用额度申请
		int amountCount = userCreditApplyService.count(0, 2);
		request.setAttribute("amountCount", amountCount);
		// 上传资料待审核
		int userCertificationApplyCount = userCertificationApplyService.count();
		request.setAttribute("userCertificationApplyCount", userCertificationApplyCount);
		// 等待审核的提现
		int cashCount = accountCashService.count(0);
		request.setAttribute("cashCount", cashCount);
		// 等待审核的充值
		int rechargeCount = accountRechargeService.count(0);
		request.setAttribute("rechargeCount", rechargeCount);
		// 今日待还
		int repayCount = borrowRepaymentService.count();
		request.setAttribute("repayCount", repayCount);
		return "todoManager";
	}

	/**
	 * 通知信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/home/notices")
	public String notices() throws Exception {
//		QueryParam param = QueryParam.getInstance();
		/*if (isOpenApi()) {
			param.addParam("userCache.userType", 1);
		}*/
		int userCount = userIdentifyService.countByRealName(1);
		int borrowCount = borrowService.count();
		double borrowMoney = borrowService.getAllMomeny();
		double ppMoney = ppfundInService.getAllMoney();;
//		double tenderAccount = borrowTenderService.tenderAccount("","");
		double sumInterest = borrowCollectionService.sumInterest();
		request.setAttribute("userCount", userCount);
		request.setAttribute("borrowCount", borrowCount);
		request.setAttribute("tenderAccount", (borrowMoney + ppMoney));
		request.setAttribute("sumInterest", sumInterest);
		return "notices";
	}
	
	/**
	 * 最近7天投资人数页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/home/investmentStatisticsPage")
	public String investmentStatisticsPage() throws Exception {
		return "investmentStatisticsPage";
	}
	/**
	 * 最近7天投资人数
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/home/investmentStatistics")
	public void investmentStatistics() throws Exception {
		data = new HashMap<String, Object>();
		List<Integer> counts = new ArrayList<Integer>();
		List<String> dates = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		String time = paramString("time");
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		if (!"".equals(startTime)) {
			Date startDate = DateUtil.valueOf(startTime);
			Date endDate = DateUtil.valueOf(endTime);
			Calendar c1 = Calendar.getInstance();
			c.setTime(startDate);
			c1.setTime(endDate);
			while (!c.after(c1)) {
				String date = DateUtil.dateStr2(c1.getTime());
				int acount = borrowTenderService.getInvestCountByDate(date);
				c1.add(Calendar.DATE, -1);
				dates.add(date.substring(5, date.length()));
				counts.add(acount);
			}
		} else {
			if ("year".equals(time)) {
				for (int i=0; i<12;i++) {
					String date = DateUtil.dateStr(c.getTime(),"yyyy-MM");
					int acount = borrowTenderService.getInvestCountByDate(date);
					c.add(Calendar.MONTH, -1);
					dates.add(date);
					counts.add(acount);
				}
			} else if ("month".equals(time)) {
				for (int i=0; i<30;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					int acount = borrowTenderService.getInvestCountByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					counts.add(acount);
				}
			} else {
				for (int i=0; i<7;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					int acount = borrowTenderService.getInvestCountByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					counts.add(acount);
				}
			}
		}
		Collections.reverse(dates);
		Collections.reverse(counts);
		data.put("dates", dates.toArray());
		data.put("counts", counts.toArray());
		printJson(getStringOfJpaObj(data));
	}
	/**
	 * 最近7天借款金额页面
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/home/borrowingStatisticsPage")
	public String borrowingStatisticsPage() throws Exception {
		return "borrowingStatisticsPage";
	}
	/**
	 * 最近7天借款金额
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/home/borrowingStatistics")
	public void borrowingStatistics() throws Exception {
		data = new HashMap<String, Object>();
		List<Double> accounts = new ArrayList<Double>();
		List<String> dates = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		String time = paramString("time");
		String startTime = paramString("startTime");
		String endTime = paramString("endTime");
		if (!"".equals(startTime)) {
			Date startDate = DateUtil.valueOf(startTime);
			Date endDate = DateUtil.valueOf(endTime);
			Calendar c1 = Calendar.getInstance();
			c.setTime(startDate);
			c1.setTime(endDate);
			while (!c.after(c1)) {
				String date = DateUtil.dateStr2(c1.getTime());
				double acount = borrowService.getBorrowAccountByDate(date);
				c1.add(Calendar.DATE, -1);
				dates.add(date.substring(5, date.length()));
				accounts.add(acount);
			}
		} else {
			if ("year".equals(time)) {
				for (int i=0; i<12;i++) {
					String date = DateUtil.dateStr(c.getTime(),"yyyy-MM");
					double acount = borrowService.getBorrowAccountByDate(date);
					c.add(Calendar.MONTH, -1);
					dates.add(date);
					accounts.add(acount);
				}
			} else if ("month".equals(time)) {
				String firstDate = DateUtil.dateStr2(c.getTime());
				dates.add(firstDate.substring(5, firstDate.length()));
				accounts.add(borrowService.getBorrowAccountByDate(firstDate));
				for (int i=0; i<30;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double acount = borrowService.getBorrowAccountByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					accounts.add(acount);
				}
			} else {
				for (int i=0; i<7;i++) {
					String date = DateUtil.dateStr2(c.getTime());
					double acount = borrowService.getBorrowAccountByDate(date);
					c.add(Calendar.DATE, -1);
					dates.add(date.substring(5, date.length()));
					accounts.add(acount);
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
	 * 访问人数
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/home/visitor")
	public String visitor() throws Exception {
		return "visitor";
	}

	/**
	 * 其他通知
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/modules/home/otherNotices")
	public String otherNotices() throws Exception {
		return "otherNotices";
	}

}
