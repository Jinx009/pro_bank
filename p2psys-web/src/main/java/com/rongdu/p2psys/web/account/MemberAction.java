package com.rongdu.p2psys.web.account;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.code.BASE64Encoder;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.model.AccountRechargeModel;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.account.service.AccountLogService;
import com.rongdu.p2psys.account.service.AccountRechargeService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.account.service.AccountSumService;
import com.rongdu.p2psys.bond.service.BondCollectionService;
import com.rongdu.p2psys.borrow.domain.BorrowInterestRate;
import com.rongdu.p2psys.borrow.model.BorrowCollectionModel;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.model.BorrowRepaymentModel;
import com.rongdu.p2psys.borrow.service.BorrowCollectionService;
import com.rongdu.p2psys.borrow.service.BorrowInterestRateService;
import com.rongdu.p2psys.borrow.service.BorrowRepaymentService;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.borrow.service.BorrowTenderService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.service.MessageService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.invest.service.FrozenUserService;
import com.rongdu.p2psys.ppfund.service.PpfundInService;
import com.rongdu.p2psys.tpp.BaseTPPWay;
import com.rongdu.p2psys.tpp.TPPFactory;
import com.rongdu.p2psys.tpp.TPPWay;
import com.rongdu.p2psys.user.dao.UserPromotDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.domain.UserVip;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserInviteModel;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserCreditService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserInviteService;
import com.rongdu.p2psys.user.service.UserRedPacketService;
import com.rongdu.p2psys.user.service.UserService;
import com.rongdu.p2psys.user.service.UserVipService;

/**
 * 账户中心资金相关处理
 * 
 * @author：xx
 * @version 1.0
 * @since 2014年6月17日
 */
@SuppressWarnings("rawtypes")
public class MemberAction extends BaseAction
{

	@Resource
	private UserCreditService userCreditService;
	@Resource
	private AccountService accountService;
	@Resource
	private MessageService messageService;
	@Resource
	private AccountSumService accountSumService;
	@Resource
	private BorrowCollectionService borrowCollectionService;
	@Resource
	private UserService userService;
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private AccountBankService accountBankService;
	@Resource
	private BorrowRepaymentService borrowRepaymentService;
	@Resource
	private BorrowService borrowService;
	@Resource
	private AccountRechargeService accountRechargeService;
	@Resource
	private AccountLogService accountLogService;
	@Resource
	private UserRedPacketService userRedPacketService;
	@Resource
	private UserInviteService userInviteService;
	@Resource
	private UserVipService userVipService;
	@Resource
	BorrowTenderService borrowTenderService;
	@Resource
	private BorrowInterestRateService borrowInterestRateService;
	@Resource
	private BondCollectionService bondCollectionService;
	@Resource
	private PpfundInService ppfundInService;
	@Resource
	private FrozenUserService frozenUserService;
	@Resource
	private UserPromotDao userPromotDao;

	private User user;

	private Map<String, Object> data;

	private String returnUrl;

	public String getReturnUrl()
	{
		return returnUrl;
	}

	/**
	 * 账户主页
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@Action(value = "/member/main", results =
	{
			@Result(name = "main", type = "ftl", location = "/member/main.html"),
			@Result(name = "main_firm", type = "ftl", location = "/member_borrow/main.html"),
			@Result(name = "main_vouch", type = "ftl", location = "/member_guarantee/main.html") 
	})
	public String main() throws Exception
	{
		user = getSessionUser();
		User u = userService.getUserById(user.getUserId());
		//获得资金详情
		Account account = accountService.findByUser(user.getUserId());
		request.setAttribute("account", account);
		//系统通知未读信息的总数
		int unreadCount = messageService.unreadCount(user.getUserId());
		request.setAttribute("unreadCount", unreadCount);

		//更新用户认证状态
		UserIdentify userIdentify = getSessionUserIdentify();
		userIdentify = userIdentifyService.findById(userIdentify.getId());
		session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);

		//银行卡数量
		int bankAccount = accountBankService.count(user.getUserId());
		request.setAttribute("bankAccount", bankAccount);
		UserCache uc = user.getUserCache();
		if (uc.getUserType() == 3&&paramInt("borrow") == 1||uc.getUserType() == 2||uc.getUserType() == 4)
		{
			return "main_firm";
		} 
		else if (uc.getUserNature() == 3)
		{
			return "main_vouch";
		}
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
		request.setAttribute("randTime", sim.format(new Date()));
		
		return "main";
	}

	/**
	 * 资金记录页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member_borrow/account/log")
	public String accountLog() throws Exception
	{
		return "log";
	}

	/**
	 * 推荐收益
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/account/recommendProfit")
	public String recommendProfit() throws Exception
	{
		return "recommendProfit";
	}
	
	/**
	 * 借款人修改头像页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/member_borrow/info/face", results =
	{ @Result(name = "face", type = "ftl", location = "/member_${returnUrl}/info/face.html") })
	public String face() throws Exception
	{
		int flag = paramInt("flag");
		
		if(flag == 1)
		{
			setReturnUrl("guarantee");
		} 
		else
		{
			setReturnUrl("borrow");
		}
		
		return "face";
	}

	/**
	 * 站内信息页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member_borrow/message/detail")
	public String detail() throws Exception
	{
		return "detail";
	}

	@Action("/member/infoJson")
	public void infoJson() throws Exception
	{
		user = getSessionUser();
		data = new HashMap<String, Object>();
		data.put("companyName", user.getUserCache().getCompanyName());
		data.put("companyRegNo", user.getUserCache().getCompanyRegNo());
		data.put("taxRegNo", user.getUserCache().getTaxRegNo());
		printWebJson(getStringOfJpaObj(data));
	}

	/***************************** 商家个人中心显示数据 start **********************************/

	/**
	 * 开通第三方账户页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member_borrow/security/realNameIdentify")
	public String realNameIdentify() throws Exception
	{
		user = userService.getUserById(getSessionUser().getUserId());
		session.put(Constant.SESSION_USER, user);
		boolean isOnlineConfig = BaseTPPWay.isOnlineConfig();
		request.setAttribute("isOnlineConfig", isOnlineConfig);
		return "realNameIdentify";
	}

	/**
	 * 开通第三方账号之后的数据
	 * 
	 * @throws Exception
	 */
	@Action("/member_borrow/security/realNameJson")
	public void realNameJson() throws Exception
	{
		user = getSessionUser();
		data = new HashMap<String, Object>();
		data.put("realName", user.getRealName());
		data.put("cardId", user.getCardId());
		data.put("mobilePhone", user.getMobilePhone());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 后台开户后，第一次登陆修改登陆密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member_borrow/changeLoginPwd")
	public String changeLoginPwd() throws Exception
	{
		return "changeLoginPwd";
	}

	/**
	 * 提现
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member_borrow/recharge/log")
	public String rechargeLog() throws Exception
	{
		AccountRechargeModel recharge = accountRechargeService
				.getRechargeSummary(getSessionUser().getUserId());
		Account account = accountService.findByUser(this.getSessionUserId());
		request.setAttribute("recharge", recharge);
		request.setAttribute("account", account);
		return "log";
	}

	/**
	 * 商家(借款人)招标中项目
	 * 
	 * @throws Exception
	 */
	@Action("/member_borrow/businessBid")
	public String businessBid() throws Exception
	{
		user = getSessionUser();
		data = new HashMap<String, Object>();
		BorrowModel model = new BorrowModel();
		model.setUser(user);
		model.setSize(4);
		model.setStatus(1);
		model.setIsNovice(99);
		PageDataList<BorrowModel> page = borrowService.getList(model);
		data.put("businessBidList", page.getList());
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 商家(借款人)还款中项目
	 * 
	 * @throws Exception
	 */
	@Action("/member_borrow/businessRepayment")
	public String businessRepayment() throws Exception
	{
		user = getSessionUser();
		data = new HashMap<String, Object>();
		List<BorrowModel> businessRepaymentList = borrowService
				.businessRepayment(user);
		data.put("businessRepaymentList", businessRepaymentList);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 商家(借款人)更新用户认证状态
	 * 
	 * @throws Exception
	 */
	@Action("/member_borrow/businessUserIdentify")
	public String businessUserIdentify() throws Exception
	{
		data = new HashMap<String, Object>();
		UserIdentify userIdentify = getSessionUserIdentify();
		userIdentify = userIdentifyService.findById(userIdentify.getId());
		session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
		user = getSessionUser();
		user = userService.getUserById(user.getUserId());
		UserCache uc = userCacheService.findByUserId(user.getUserId());
		if (userIdentify.getEmailStatus() == 1)
		{
			
		}
		// 系统通知未读信息的总数
		int unReadCount = messageService.unreadCount(user.getUserId());
		data.put("userId", user.getUserCache().getId());
		data.put("unReadCount", unReadCount);
		data.put("openApi", isOpenApi());
		data.put("emailStatus", userIdentify.getEmailStatus());
		data.put("mobilePhoneStatus", userIdentify.getMobilePhoneStatus());
		data.put("realNameStatus", userIdentify.getRealNameStatus());
		data.put("companyName", uc.getCompanyName());
		data.put("email", user.getHideEmail());
		data.put("realName", user.getRealName());
		data.put("phone", user.getHideMobilePhone());
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 商家(借款人)可提现余额 正在借款项目个数 正在借款金额 下一个待还日期信息
	 * 
	 * @throws Exception
	 */
	@Action("/member_borrow/businessHandleMoney")
	public String businessHandleMoney() throws Exception
	{
		data = new HashMap<String, Object>();
		user = getSessionUser();
		// 获得信用额度
		UserCredit amount = userCreditService.findByUserId(user.getUserId());
		data.put("amount", amount);
		// 可提现余额
		Account account = accountService.findByUser(user.getUserId());
		data.put("useMoney", account.getUseMoney());
		// 正在借款项目信息
		int count = borrowService.findByStatusAndUserId(user.getUserId(), 1, 0);
		data.put("count", count);
		double total = borrowService.findAccountTotalByStatus(user.getUserId(),1, 0);
		data.put("total", total);
		// 待还的统计
		BorrowRepaymentModel brm = borrowRepaymentService.getReapyStatistics(user.getUserId());
		data.put("borrowRepay", brm);
		// 待还总额 待还总项目
		int repaymentCount = borrowService.findByStatusAndUserId(user.getUserId(), 6, 7);
		data.put("repaymentCount", repaymentCount);
		double repaymentTotal = borrowRepaymentService.getUserBorrowRepayTotal(user.getUserId());
		data.put("repaymentTotal", repaymentTotal);
		// 信用额度
		UserCredit userCredit = userCreditService.findByUserId(user.getUserId());
		data.put("userCreditTotal", userCredit.getCredit());
		data.put("userCreditUseMoney", userCredit.getCreditUse());
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 交易记录
	 * 
	 * @throws Exception
	 */
	@Action("/member_borrow/accountTransactionLog")
	public String accountTransactionLog() throws Exception
	{
		user = getSessionUser();
		data = new HashMap<String, Object>();
		List<AccountLogModel> accountLogList = accountLogService.accountTransactionLog(user);
		data.put("accountLogList", accountLogList);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 公司主页
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member_borrow/home")
	public String home() throws Exception
	{
		String id = request.getParameter("id");
		request.setAttribute("id", id);
		user = getSessionUser();
		UserCache uc;
		if("undefined".equals(id) || id == null)
		{
			uc = userCacheService.findByUserId(user.getUserId());
		} 
		else
		{
			uc = userCacheService.findById(Long.parseLong(id));
		}
		if(uc == null || uc.getUserNature() != 2)
		{
			throw new UserException("用户不存在或不是企业用户");
		}
		return "home";
	}

	/**
	 * 公司主页动态数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member_borrow/companyHomeJson")
	public String companyHomeJson() throws Exception
	{
		data = new HashMap<String, Object>();
		user = getSessionUser();
		UserCache uc;
		String id = request.getParameter("id");
		if ("undefined".equals(id))
		{
			uc = userCacheService.findByUserId(user.getUserId());
			data.put("flag", true);
		} 
		else
		{
			uc = userCacheService.findById(Long.parseLong(id));
		}
		String companyName = uc.getCompanyName();
		if (companyName.length() > 5)
		{
			uc.setCompanyName(companyName.substring(0, 2) + "****"+ companyName.substring(companyName.length() - 4));
		}
		String companyRegNo = uc.getCompanyRegNo();
		uc.setCompanyRegNo(companyRegNo.charAt(0) + "****"+ companyRegNo.charAt(companyRegNo.length() - 1));
		
		String taxRegNo = uc.getTaxRegNo();
		uc.setTaxRegNo(taxRegNo.charAt(0) + "****"+ taxRegNo.charAt(taxRegNo.length() - 1));
		
		String companyPhone = uc.getCompanyPhone();
		if (companyPhone != null && companyPhone.length() > 5)
		{
			uc.setCompanyPhone(companyPhone.substring(0, 4) + "****"+ companyPhone.substring(companyRegNo.length() - 2));
		}
		String companyFax = uc.getCompanyFax();
		if (companyFax != null && companyFax.length() > 5)
		{
			uc.setCompanyFax(companyFax.substring(0, 4) + "****"+ companyFax.substring(companyFax.length() - 2));
		}
		String companyEmail = uc.getCompanyEmail();
		if (companyEmail != null && !"".equals(companyEmail))
		{
			uc.setCompanyFax("****"+ companyFax.substring(companyFax.lastIndexOf("@")));
		}
		User u = uc.getUser();
		String realName = u.getRealName();
		if (realName != null && realName.length() > 0)
		{
			u.setRealName(realName.charAt(0) + "**");
		}
		String mobilePhone = u.getMobilePhone();
		u.setMobilePhone(mobilePhone.substring(0, 2) + "****"+ mobilePhone.substring(mobilePhone.length() - 2));
		String email = u.getEmail();
		u.setEmail("****" + email.substring(email.lastIndexOf("@")));
		data.put("uc", uc);
		data.put("url", Global.getValue("adminurl"));
		data.put("realName", uc.getUser().getRealName());
		data.put("email", uc.getUser().getEmail());
		data.put("mobilePhone", uc.getUser().getMobilePhone());
		printWebJson(getStringOfJpaObj(data));
		
		return null;
	}

	/***************************** 商家个人中心显示数据 end **********************************/

	/***************************** 投资者个人中心显示数据 start **********************************/

	/**
	 * 投资人 用户认证状态
	 * 
	 * @throws Exception
	 */
	@Action("/member/investIdentify")
	public String investIdentify() throws Exception
	{
		data = new HashMap<String, Object>();
		UserIdentify userIdentify = getSessionUserIdentify();
		userIdentify = userIdentifyService.findById(userIdentify.getId());
		session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
		user = getSessionUser();
		user = userService.getUserById(user.getUserId());
		if (userIdentify.getEmailStatus() == 1)
		{
		}
		// VIP规则显示
		UserVip userVipRule = userVipService.getUserVipRuleByUser(user);
		if (userVipRule != null)
		{
			data.put("VIPName", userVipRule.getName());
			data.put("LastYearInvest", userVipRule.getLastYearInvest());
		}

		List<BorrowInterestRate> birList = borrowInterestRateService.findByStatusAndUser(1, user);
		if (birList != null && birList.size() > 0)
		{
			data.put("birTotal", birList.size());
		} 
		else
		{
			data.put("birTotal", 0);
		}

		// 系统通知未读信息的总数
		int unReadCount = messageService.unreadCount(user.getUserId());
		data.put("unReadCount", unReadCount);
		data.put("emailStatus", userIdentify.getEmailStatus());
		data.put("realNameStatus", userIdentify.getRealNameStatus());
		data.put("mobilePhoneStatus", userIdentify.getMobilePhoneStatus());
		data.put("vipStatus", userIdentify.getVipStatus());
		data.put("userName", user.getUserName());
		data.put("email", user.getHideEmail());
		data.put("realName", user.getRealName());
		data.put("openApi", isOpenApi());
		data.put("phone", user.getHideMobilePhone());
		printWebJson(getStringOfJpaObj(data));
		
		return null;
	}

	/**
	 * 资产统计页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/member/account/assets", results =
	{
			@Result(name = "assets", type = "ftl", location = "/member/account/assets.html"),
			@Result(name = "assets_standard", type = "ftl", location = "/member_standard/account/assets.html") 
	})
	public String assets() throws Exception
	{
		user = getSessionUser();
		Account account = accountService.findByUser(user.getUserId());
		request.setAttribute("account", account);
		// 统计标待收利息 - 已转出利息
		double interest = borrowCollectionService.getInterestByUser(user);
		double ppfundInterest = ppfundInService.getCollectionInterestByUser(user);
		interest += ppfundInterest;

		// 统计标待收本金 - 已转出本金
		double capital = borrowCollectionService.getCapitalByUser(user);
		double ppfundCapital = ppfundInService.getCollectionCapitalByUser(user);
		capital += ppfundCapital;
		// 统计债权待收本金、利息
		Object[] obj = bondCollectionService.getSumBondCollection(user.getUserId());
		double bondCapital = 0;
		double bondInterest = 0;
		if (obj != null && obj.length == 2)
		{
			if (obj[0] != null)
			{
				bondCapital = Double.parseDouble(obj[0].toString());
			}
			if (obj[1] != null)
			{
				bondInterest = Double.parseDouble(obj[1].toString());
			}
		}
		request.setAttribute("interest",BigDecimalUtil.add(interest, bondInterest));
		request.setAttribute("capital",BigDecimalUtil.add(capital, bondCapital));
		
		List<String> dates = borrowCollectionService.getCollectionDate(user);
		if (dates == null || dates.size() <= 0)
		{
			dates.add(DateUtil.dateStr(new Date(), "yyyy-MM"));
		}
		request.setAttribute("dates", dates);

		return "assets";
	}

	/**
	 * 待收利息JSON
	 * 
	 * @throws Exception
	 */
	@Action("/member/account/interestJson")
	public void interestJson() throws Exception
	{
		data = new HashMap<String, Object>();
		user = getSessionUser();
		List<Object[]> list = borrowCollectionService.getInterestByUserAndDate(user, paramString("date"));
		List<Object> days = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		for (Object[] o : list)
		{
			days.add(o[0]);
			values.add(o[1]);
		}
		List<String> numList = this.getDateNum(paramString("date"));
		for (int i = 0; i < numList.size(); i++)
		{
			String str = numList.get(i);
			if (days.contains(str))
			{
				continue;
			} 
			else
			{
				days.add(i, str);
				values.add(i, 0);
			}
		}
		data.put("day", days.toArray());
		data.put("value", values.toArray());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 待收本金JSON
	 * 
	 * @throws Exception
	 */
	@Action("/member/account/capitalJson")
	public void capitalJson() throws Exception
	{
		data = new HashMap<String, Object>();
		user = getSessionUser();
		List<Object[]> list = borrowCollectionService.getCapitalByUserAndDate(user, paramString("date"));
		List<Object> days = new ArrayList<Object>();
		List<Object> values = new ArrayList<Object>();
		for (Object[] o : list)
		{
			days.add(o[0]);
			values.add(o[1]);
		}
		List<String> numList = this.getDateNum(paramString("date"));
		for (int i = 0; i < numList.size(); i++)
		{
			String str = numList.get(i);
			if (days.contains(str))
			{
				continue;
			} 
			else
			{
				days.add(i, str);
				values.add(i, 0);
			}
		}
		data.put("day", days.toArray());
		data.put("value", values.toArray());
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 资金统计信息
	 * 
	 * @throws Exception
	 */
	@Action("/member/investHandleMoney")
	public String investHandleMoney() throws Exception
	{
		data = new HashMap<String, Object>();
		user = getSessionUser();
		// 多身份groupId(by:Chris)
//		List<User> users = userService.findByBindId(user.getBindId());
//		if (users.size() > 0)
//		{
//			double total = 0, useMoney = 0, noUseMoney = 0, collection = 0;
//			for (User user : users)
//			{
//				Account account_ = accountService.findByUser(user.getUserId());
//				total += account_.getTotal();
//				useMoney += account_.getUseMoney();
//				noUseMoney += account_.getNoUseMoney();
//				collection += account_.getCollection();
//			}
//			data.put("total", total);
//			data.put("useMoney", useMoney);
//			data.put("noUseMoney", noUseMoney);
//			data.put("collection", collection);
//		} 
//		else
//		{
//			Account account = accountService.findByUser(user.getUserId());
//			data.put("total", account.getTotal());
//			data.put("useMoney", account.getUseMoney());
//			data.put("noUseMoney", account.getNoUseMoney());
//		}
		
		
		// 累计净收益(多身份)
		double netProfit = borrowCollectionService.netProfit(user);
		data.put("netProfit", netProfit);

		
		
		Account account = accountService.findByUser(user.getUserId());
		data.put("account", account);
		
		//被锁住金额
		double lockUserMoney = frozenUserService.getLockUseMoney(Integer.parseInt(String.valueOf(user.getUserId())));
		//可用余额（当前账户余额减去被锁住金额）
		data.put("useMoney", (account.getUseMoney()-lockUserMoney));
		
		// 今日净收益
		double sumTodayInterest = borrowCollectionService.sumTodayInterest(user);
		data.put("sumTodayInterest", sumTodayInterest);
		// 累计净收益
		double sumInterest = borrowCollectionService.accumulatedNetIncome(user);
		data.put("sumInterest", sumInterest);
		// 在投金额
		double sumInMoney = borrowCollectionService.inInvestAmount(user, 0);
		data.put("sumInMoney", sumInMoney);
		// 累计投资
		double investTotal = borrowCollectionService.inInvestAmount(user, 1);
		data.put("investTotal", investTotal);
		// 回款中个数
		int repayCount = borrowCollectionService.countCollect(user.getUserId(),0);
		// 投标中个数
		int tenderCount = borrowTenderService.countTenderByUserAndStatus(user.getUserId(), 1);
		// 已结束个数
		int endCount = borrowCollectionService.countCollect(user.getUserId(), 1);
		// 待收的统计
		BorrowCollectionModel bcm = borrowCollectionService.getCollectStatistics(user.getUserId());
		data.put("borrowCollection", bcm);
		// 待收本金总额
		double collectionCapital = borrowCollectionService.getCapitalByUser(user);
		// 待收利息总额
		double collectionInterest = borrowCollectionService.getInterestByUser(user);
		// 待收总额
		double collectionTotal = collectionCapital + collectionInterest;
		
		data.put("collectionTotal", collectionTotal);
		data.put("repayCount", repayCount);
		data.put("tenderCount", tenderCount);
		data.put("endCount", endCount);
		data.put("investTotal", investTotal);
		data.put("isOpenApi", Global.getValue("is_open_deposit"));
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmss");
		request.setAttribute("randTime", sim.format(new Date()));
		printWebJson(getStringOfJpaObj(data));
		
		return null;
	}

	/**
	 * 可借款标
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/investList")
	public String investList() throws Exception
	{
		data = new HashMap<String, Object>();
		user = getSessionUser();
		List<BorrowModel> list = borrowService.investList(user);
		data.put("list", list);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 收款（3条）
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/investCollectionList")
	public String investCollectionList() throws Exception
	{
		user = getSessionUser();
		List<BorrowCollectionModel> list = borrowCollectionService.investCollectionList(user);
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 交易记录
	 * 
	 * @throws Exception
	 */
	@Action("/member/investTransactionLog")
	public String investTransactionLog() throws Exception
	{
		user = getSessionUser();
		data = new HashMap<String, Object>();
		List<AccountLogModel> accountLogList = accountLogService.accountTransactionLog(user);
		data.put("accountLogList", accountLogList);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 投资者通过投标详情跳转到借款人的公司主页
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/borrowHome")
	public String borrowHome() throws Exception
	{
		return "home";
	}

	/**
	 * 公司主页动态数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/companyHome")
	public String companyHome() throws Exception
	{
		data = new HashMap<String, Object>();
		long id = paramLong("id");
		UserCache uc = userCacheService.findById(id);
		data.put("uc", uc);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/***************************** 投资者个人中心显示数据 end **********************************/

	/**
	 * 待收 json数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/repayCollectJson")
	public String repayCollectJson() throws Exception
	{
		user = getSessionUser();
		data = new HashMap<String, Object>();
		// 待收的统计
		BorrowCollectionModel bcm = borrowCollectionService.getCollectStatistics(user.getUserId());
		data.put("borrowCollect", bcm);
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 用户待收金额信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/getUserCollectionAccount")
	public String getUserCollectionAccount() throws Exception
	{
		user = getSessionUser();
		
		data = new HashMap<String, Object>();
		// 用户待收金额信息
		AccountModel accountModel = accountService.getUserCollectionAccount(user.getUserId());
		data.put("accountModel", accountModel);
		printWebJson(getStringOfJpaObj(data));
		
		return null;
	}

	public void setReturnUrl(String returnUrl)
	{
		this.returnUrl = returnUrl;
	}

	/**
	 * 用户已赚金额信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/member/getUserEarnedAccount")
	public String getUserEarnedAccount() throws Exception
	{
		user = getSessionUser();
		
		data = new HashMap<String, Object>();
		long userId = user.getUserId();
		// 已赚利息
		double receivedInterestSum = borrowCollectionService.getReceivedInterestSum(userId);
		// 已赚奖励
		AccountSum accountSum = accountSumService.findByUserId(userId);
		data.put("receivedInterestSum", receivedInterestSum);
		data.put("award", accountSum.getAward());
		printWebJson(getStringOfJpaObj(data));
		
		return null;
	}

	/**
	 * 获取月份的所有天数
	 * 
	 * @param time
	 *            月份，格式必须为yyyy-MM
	 * @return 月份的天数
	 */
	private List<String> getDateNum(String time)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date date = sdf.parse(time);
			Calendar a = Calendar.getInstance();
			a.setTime(date);
			a.set(Calendar.DATE, 1);
			a.roll(Calendar.DATE, -1);
			int num = a.get(Calendar.DATE);
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < num; i++)
			{
				Date j = DateUtil.rollDay(date, i);
				list.add(DateUtil.dateStr8(j));
			}
			return list;
		} 
		catch (ParseException e)
		{
			return null;
		}
	}

	/**
	 * 借款账户详情信息
	 */
	@Action("/member_borrow/info/detail")
	public String detailInfo()
	{
		return "detail";
	}

	/**
	 * 邀请好友页面
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action("/member/friend/userInvite")
	public String userInvite() throws IOException
	{
		user = getSessionUser();
		UserPromot userPromot = userPromotDao.getUserPromotByUserId(user);
		byte[] bytes = new byte[0];
		if (null != userPromot)
		{
			bytes = userPromot.getCouponCode().getBytes("utf-8");
		}
		BASE64Encoder encoder = new BASE64Encoder();
		request.setAttribute("ui", encoder.encode(bytes));
		return "userInvite";
	}

	/**
	 * 邀请好友数据
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action("/member/friend/userInviteJSON")
	public void userInviteJSON() throws IOException
	{
		user = getSessionUser();
		data = new HashMap<String, Object>();
		PageDataList<UserInviteModel> list = userInviteService.findByInviteUser(user, paramInt("page"));
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 登录第三方托管账户平台
	 * 
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/member/tpp/apiLogin", results =
	{ @Result(name = "pnrLogin", type = "ftl", location = "/tpp/chinapnr/userLogin.html") })
	public String apiLogin()
	{
		User user = getSessionUser();
		user = userService.getUserById(user.getUserId());
	
		TPPWay way = TPPFactory.getTPPWay();
		Object object = way.apiLogin(user);
		if (null != object)
		{
			return madeApiLoginReturn(object);
		} else
		{
			throw new BussinessException("系统异常请联系管理员！");
		}
	}

	private String madeApiLoginReturn(Object object)
	{
		String name = Global.getValue("cooperation_interface");
		int apiType = TPPWay.API_CODE;
		switch (apiType)
		{
		case 1:// 易极付接口
		case 2:// 环讯接口
		case 3:// 汇付接口
			request.setAttribute(name, object);
			return name + "Login";
		default:
			throw new BussinessException("系统异常请联系管理员！");
		}
	}

}
