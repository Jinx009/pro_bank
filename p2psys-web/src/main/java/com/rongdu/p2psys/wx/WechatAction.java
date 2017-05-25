package com.rongdu.p2psys.wx;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.exception.BussinessException;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.NumberUtil;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.model.AccountModel;
import com.rongdu.p2psys.account.service.AccountBankService;
import com.rongdu.p2psys.account.service.AccountService;
import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.borrow.model.BorrowModel;
import com.rongdu.p2psys.borrow.service.BorrowService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ConcurrentUtil;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.model.PpfundInModel;
import com.rongdu.p2psys.ppfund.model.PpfundModel;
import com.rongdu.p2psys.ppfund.service.PpfundService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.model.getpwd.BaseGetPwd;
import com.rongdu.p2psys.user.model.getpwd.GetPwdPhone;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 微信
 * 
 * @author shou
 * @version 2.0
 * @since 2015年3月24日
 */
@SuppressWarnings("rawtypes")
public class WechatAction extends BaseAction implements
		ModelDriven<PayDetailReq>
{
	private Logger logger = Logger.getLogger(WechatAction.class);
	private PayDetailReq payDetailReq = new PayDetailReq();
	@Resource
	private AccountService accountService;

	@Override
	public PayDetailReq getModel()
	{
		return payDetailReq;
	}

	@Resource
	private AccountBankService accountBankService;
	@Resource
	private UserService userService;
	@Resource
	private PpfundService ppfundService;
	@Resource
	private BorrowService borrowService;

	/**
	 * 支付详情
	 * 
	 * @return
	 */
	@Action(value = "/wx/account/pay", results =
	{ @Result(name = "payDetail", type = "ftl", location = "/wechat/payDetail.html") })
	public String payDetail() throws Exception
	{
		User user = getSessionUser();
		Account acc = accountService.findByUser(user.getUserId());
		AccountModel account =  AccountModel.instance(acc);
		account.setUseMoney(accountService.getSumAccount(user.getUserId()));
		request.setAttribute("account", account);
		System.out.println(payDetailReq);

		System.out.println(payDetailReq.getProjectName());

		List<AccountBank> bankList = accountBankService.list(user.getUserId());
		request.setAttribute("bank", bankList.get(0));

		if ("ppfund".equals(payDetailReq.getProjectType()))
		{
			Ppfund ppfund = ppfundService.getPpfundById(NumberUtil
					.getLong(payDetailReq.getId()));
			payDetailReq.setProjectName(ppfund.getName());
			this.saveToken("ppfundTenderToken");
			String ppfundTenderToken = (String) session
					.get("ppfundTenderToken");
			request.setAttribute("ppfundTenderToken", ppfundTenderToken);
		}

		if ("borrow".equals(payDetailReq.getProjectType()))
		{
			this.saveToken("tenderToken");
			Borrow borrow = borrowService.getBorrowById(NumberUtil.getLong(payDetailReq.getId()));
			payDetailReq.setProjectName(borrow.getName());
			String ppfundTenderToken = (String) session.get("tenderToken");
			request.setAttribute("tenderToken", ppfundTenderToken);
		}
		payDetailReq.setInvestPrice(String.valueOf(payDetailReq.getMoney()));
		return "payDetail";
	}

	/**
	 * 帐号支付
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "/wx/account/dopay", results =
	{ @Result(name = "result", type = "ftl", location = "/wechat/result.html") })
	public String dopay() throws Exception
	{
		User user = getSessionUser();
		this.checkToken("payToken");
		System.out.println(request.getParameter("projectType"));
		System.out.println(payDetailReq.getProjectType());
		if ("ppfund".equals(payDetailReq.getProjectType()))
		{
			Ppfund ppfund = ppfundService.getPpfundById(payDetailReq
					.getProjectId());
			try
			{
				this.checkToken("ppfundTenderToken");
				if (user.getUserCache().getUserType() == 2)
				{
					throw new BussinessException("借款账户不能投资");
				}
				if (payDetailReq.getOutTime() != null)
				{
					int days = DateUtil.daysBetween(new Date(),
							DateUtil.getDate(payDetailReq.getOutTime()));
					if (days % ppfund.getCycle() != 0)
					{
						throw new BussinessException("转出时间选择不正确", 2);
					}
				}
			} 
			catch (BussinessException e)
			{
				throw new BussinessException(e.getMessage(), 2);
			}

			PpfundInModel ppfundIn = new PpfundInModel();
			ppfund.setAccount(NumberUtil.getDouble(payDetailReq
					.getInvestPrice()));
			ppfund.setAccount(NumberUtil.getDouble(payDetailReq
					.getInvestPrice()));
			ppfundIn.setUser(user);
			ppfundIn.setPpfund(ppfund);
			ConcurrentUtil.ppfundTender(ppfundIn);

			String resultFlag = System.currentTimeMillis() + "" + Math.random()*10000;
			request.setAttribute("resultFlag", resultFlag);
			Global.RESULT_MAP.put(resultFlag, "success");
		}
		return "result";
	}

	/**
	 * 手机验证码校验 wx/phoneCode.html?phone=param1&type=check&code=code
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/wx/phoneCode")
	public String sentActivationPhone() throws Exception
	{
		BaseGetPwd getpwd = new GetPwdPhone();
		String phone = paramString("phone");
		String type = paramString("type");
		User userAll = new User();
		if ("check".equals(type))
		{
			userAll = userService.getUserByMobilePhone(phone);
			getpwd.getPwdStep2(userAll, paramString("code"));
		}
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("result", true);
		data.put("username", userAll.getUserName());
		printWebJson(getStringOfJpaObj(data));
		return null;
	}

	/**
	 * 推荐
	 */
	@Action("/wx/recommend")
	public void recommend() throws Exception
	{
		Map<String,Object> data = new HashMap<String,Object>();
		String type = paramString("type");
		PpfundModel ppfundModel = this.ppfundService.getLastPpfund();
		BorrowModel estate = this.borrowService.getLastBorrow(119);
		BorrowModel entrust = this.borrowService.getLastBorrow(122);
		BorrowModel invest = this.borrowService.getLastBorrow(103);
		if ("index".equals(type))
		{
			data.put("ppfund", ppfundModel);
		} 
		else
		{
			data.put("ppfund", ppfundModel);
			data.put("estate", estate);
			data.put("entrust", entrust);
			data.put("invest", invest);
		}
		data.put("url", Global.getValue("adminurl"));
		printWebJson(getStringOfJpaObj(data));
	}

	@Action(value = "/wx/plist", results =
	{ @Result(name = "result", type = "ftl", location = "/wechat/detail.html") })
	public String plist()
	{
		return "result";
	}

	@Action(value = "/wx/account/rList", results =
	{ @Result(name = "result", type = "ftl", location = "/wechat/wx_p2/html/recommend.html") })
	public String rList()
	{
		return "result";
	}

	@Action(value = "/wx/account/toResetPayPwd", results =
	{ @Result(name = "result", type = "ftl", location = "/wechat/getpaycode.html") })
	public String toResetPayPwd()
	{
		User user = getSessionUser();
		request.setAttribute("payPwd", user.getPayPwd());
		logger.info(user.getPayPwd());
		return "result";
	}

	@Action(value = "/wx/rechargeSuccess", results =
	{ @Result(name = "result", type = "ftl", location = "/wechat/wx_p2/html/recharge_success.html") })
	public String rechargeSuccess()
	{
		return "result";
	}

	@Action(value = "/wx/mentionSuccess", results =
	{ @Result(name = "result", type = "ftl", location = "/wechat/wx_p2/html/mention_success.html") })
	public String mentionSuccess()
	{
		return "result";
	}

	@Action(value = "/wx/logOut", results =
	{ @Result(name = "result", type = "redirect", location = "/wx/login.html") })
	public String logOut()
	{
		session.put(Constant.SESSION_USER, null);
		session.put("logintime", null);
		Cookie cookie = new Cookie("jforumUserInfo", "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		return "result";
	}

	@Action(value = "/wx/elect", results =
	{ @Result(name = "elect", type = "ftl", location = "/wechat/wx_p2/html/elect.html") })
	public String elect()
	{
		String redirectUrl = paramString("redirectUrl");

		request.setAttribute("url", redirectUrl);

		return "elect";
	}
}
