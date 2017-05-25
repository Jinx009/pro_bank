package com.rongdu.p2psys.web.user;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.StringUtil;
import com.rongdu.common.util.code.BASE64Decoder;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.model.accountlog.BaseAccountLog;
import com.rongdu.p2psys.account.model.accountlog.noac.GetCodeLog;
import com.rongdu.p2psys.cooperation.service.CooperationLoginService;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.rule.LoginRuleCheck;
import com.rongdu.p2psys.core.rule.RegisterRuleCheck;
import com.rongdu.p2psys.core.rule.SafetyRuleCheck;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.core.util.mail.Mail;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.exception.UserException;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.model.getpwd.BaseGetPwd;
import com.rongdu.p2psys.user.model.getpwd.GetPwdEmail;
import com.rongdu.p2psys.user.model.getpwd.GetPwdPhone;
import com.rongdu.p2psys.user.service.UserCacheService;
import com.rongdu.p2psys.user.service.UserIdentifyService;
import com.rongdu.p2psys.user.service.UserPromotService;
import com.rongdu.p2psys.user.service.UserService;

/**
 * 用户账户
 * 
 * @author ZhuJunjie
 * @version 2.0
 * @since 2015-06-01
 */
public class UserAction extends BaseAction<User> implements ModelDriven<User>
{

	@Resource
	private UserService userService;

	@Resource
	private UserCacheService userCacheService;

	@Resource
	private UserIdentifyService userIdentifyService;

	@Resource
	private CooperationLoginService cooperationLoginService;

	@Resource
	private UserPromotService userPromotService;

	/**
	 * 平台安全性规则
	 **/
	private SafetyRuleCheck safetyRuleCheck = (SafetyRuleCheck) Global
			.getRuleCheck("safety");

	/**
	 * 注册规则
	 **/
	private RegisterRuleCheck registerRuleCheck = (RegisterRuleCheck) Global
			.getRuleCheck("register");

	/**
	 * 登录规则
	 **/
	private LoginRuleCheck loginRuleCheck = (LoginRuleCheck) Global
			.getRuleCheck("login");

	private Map<String, Object> data;

	/**
	 * 用户注册页面（个人）
	 * 
	 * @return "register"
	 * @throws Exception
	 */
	@Action(value="/user/register",results={@Result(name="index",type="ftl",location="/nb/pc/index.html")})
	public String register() throws Exception
	{
		// 邀请码
		String ui = paramString("ui");
		BASE64Decoder decoder = new BASE64Decoder();
		ui = decoder.decodeStr(ui);
		request.setAttribute("regUi",ui);
		return "index";
	}

	/**
	 * 用户注册页面（企业）
	 * 
	 * @return "borrowRegister"
	 * @throws Exception
	 */
	@Deprecated
	@Action("/user/borrowRegister")
	public String companyRegister() throws Exception
	{
		// 邀请码
		request.setAttribute("ui", paramString("ui"));

		// 是否需要验证码
		request.setAttribute("enable_codeCheck",
				registerRuleCheck.enable_codeCheck);

		// 是否区分借款人与投资人
		request.setAttribute("distinction_type",
				Global.getInt("distinction_type"));

		// RSA初始化
		initRSAME();

		return "borrowRegister";
	}

	/**
	 * 判断用户名是否已存在
	 * 
	 * @return null
	 * @throws Exception
	 */
	@Action(value = "/user/checkUserName", interceptorRefs =
	{ @InterceptorRef("ajaxSafe"), @InterceptorRef("globalStack") })
	public String checkUserName() throws Exception
	{
		model.setUserName(StringUtil.isNull(model.getUserName()));
		try
		{
			// 安全规则-禁止使用敏感用户名
			safetyRuleCheck.hasFontUnallowedUsername(model);
		} catch (UserException e)
		{
			printWebJson(getStringOfJpaObj(false));
			return null;
		}
		int count = userService.countByUserName(model.getUserName());
		boolean result = count > 0 ? false : true;
		printWebJson(getStringOfJpaObj(result));
		return null;
	}

	/**
	 * 判断邮箱是否已被使用
	 * 
	 * @return null
	 * @throws Exception
	 */
	@Action(value = "/user/checkEmail", interceptorRefs =
	{ @InterceptorRef("ajaxSafe"), @InterceptorRef("globalStack") })
	public String checkEmail() throws Exception
	{
		int count = userService.countByEmail(model.getEmail());
		boolean result = count > 0 ? false : true;
		printWebJson(getStringOfJpaObj(result));
		return null;
	}

	/**
	 * 判断身份证号码是否已被使用
	 * 
	 * @return null
	 * @throws Exception
	 */
	@Action(value = "/user/checkCardId", interceptorRefs =
	{ @InterceptorRef("ajaxSafe"), @InterceptorRef("globalStack") })
	public String checkCardId() throws Exception
	{
		int count = userService.countByCardId(model.getCardId());
		boolean result = count > 0 ? false : true;
		printWebJson(getStringOfJpaObj(result));
		return null;
	}

	/**
	 * 判断手机号码是否已被使用
	 * 
	 * @return null
	 * @throws Exception
	 */
	@Action(value = "/user/checkMobilePhone", interceptorRefs =
	{ @InterceptorRef("ajaxSafe"), @InterceptorRef("globalStack") })
	public String checkMobilePhone() throws Exception
	{
		boolean result = true;
		if (getSessionUser() != null)
		{
			if (!getSessionUser().getMobilePhone().equals(
					model.getMobilePhone()))
			{
				int count = userService.countByMobilePhone(model
						.getMobilePhone());
				if (count > 0)
				{
					result = false;
				}
			}
		} else
		{
			int count = userService.countByMobilePhone(model.getMobilePhone());
			if (count > 0)
			{
				result = false;
			}
		}
		printWebJson(getStringOfJpaObj(result));
		return null;
	}

	/**
	 * 注册时获取手机验证码
	 * 
	 * @throws Exception
	 */
	@Action("/user/getPhoneCode")
	public void getPhoneCode() throws Exception
	{
		UserModel userModel = UserModel.instance(model);
		userModel.setValidCode(paramString("validCode"));
		userModel.validRegRule();
		String mobilePhone = paramString("mobilePhone");
		BaseAccountLog blog = new GetCodeLog(null, "", null, mobilePhone,
				NoticeConstant.REGISTER_GET_CODE);
		blog.initCode("registerGetCode");
		blog.doEvent();
		printWebSuccess();
	}

	/**
	 * 进行用户注册
	 * 
	 * @return null
	 * @throws Exception
	 */
	@Action("/user/doRegister")
	public String doRegister() throws Exception
	{
		data = new HashMap<String, Object>();
		if (paramInt("agree") != 1)
		{
			throw new UserException("必须同意协议！", 1);
		}
		UserModel userModel = UserModel.instance(model);
		// 用户类型
		userModel.setUserType(paramInt("userType"));

		// 校验手机验证码
		userModel.setCode(paramString("code"));
		userModel.validRegisterPhone();

		// 校验密码和确认密码
		userModel.setConfirmNewPwd(paramString("confirmPassword"));
		userModel.validRegPwdModel();

		// 推荐人信息
		String ui = paramString("ui");
		if (!"".equals(ui))
		{
			UserPromot promot = userPromotService.getUserPromotByCode(ui);
			if (promot != null)
			{
				User inviteUser = promot.getUser();
				if (inviteUser != null)
				{
					userModel.setInviteUser(inviteUser);
				}
			}
		}

		// 进行注册
		User u = userService.doRegister(userModel);

		// 注册成功，发送短信
		if (u != null)
		{
			data.put("result", true);
			data.put("msg", "注册成功！");
			data.put("userId", u.getUserId());
			data.put("userName", u.getUserName());
			data.put("email", u.getEmail());
			Global.setTransfer("user", u);
			AbstractExecuter executer = ExecuterHelper
					.doExecuter("registerSuccExecuter");
			executer.execute(0, u);
			printWebJson(getStringOfJpaObj(data));
		} else
		{
			throw new UserException("注册失败！", 1);
		}
		return null;
	}

	/************************* 整理线 *************************/

	/**
	 * 第二部进行邮箱认证
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/doRegisterStep1")
	public String doRegisterStep1() throws Exception
	{
		model = userService.activationEmail(paramString("id"));
		request.setAttribute("msg", "邮箱激活成功，注册成功！");
		request.setAttribute("pwd", model.getPwd());
		request.setAttribute("uId", model.getUserId());
		return "regresult";
	}

	/**
	 * 初始化密码页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/initLoginPwd")
	public String initLoginPwd() throws Exception
	{
		User user = userService.getUserById(model.getUserId());
		String referer = request.getHeader("Referer");
		if (referer == null)
		{
			throw new UserException("为了确保信息安全，请通过发送给您的邮箱点击访问");
		}
		if (user.getPwd() != null && !"".equals(user.getPwd()))
		{
			throw new UserException("已经初始化过密码");
		}
		request.setAttribute("companyName", user.getUserCache()
				.getCompanyName());
		request.setAttribute("uId", model.getUserId());
		return "initLoginPwd";
	}

	/**
	 * 初始化密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/initPwd")
	public void initPwd() throws Exception
	{
		if (!model.getPwd().equals(paramString("confirmPassword")))
		{
			throw new UserException("两次输入的密码不一致！");
		}
		User user = userService.getUserById(model.getUserId());
		if (user.getPwd() != null && !"".equals(user.getPwd()))
		{
			throw new UserException("您已经初始化过密码！");
		}
		user.setPwd(model.getPwd());
		userService.modifyPwd(user);
		printWebSuccess();
	}

	/**
	 * 用户登录页面
	 * 
	 * @return String
	 * @throws Exception
	 *             if has error
	 */
	@Action("/user/login")
	public String login() throws Exception
	{
		initRSAME();
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				String name = cookie.getName();
				if ("count".equals(name))
				{
					String value = cookie.getValue();
					if (value != null)
					{
						int count = Integer.parseInt(value);
						if (count == loginRuleCheck.max_fail_times_code)
						{
							request.setAttribute("count", 1);
						}
					}
					break;
				}
			}
		}
		request.setAttribute("redirectURL", request.getParameter("redirectURL"));

		String openType = this.paramString("openType");
		String openId = this.paramString("openId");
		request.setAttribute("openType", openType);
		request.setAttribute("openId", openId);
		return "login";
	}

	/**
	 * 登录
	 * 
	 * @throws Exception
	 *             if has error
	 */
	@Action("/user/doLogin")
	public void doLogin() throws Exception
	{
		data = new HashMap<String, Object>();
		User u = new User();
		// 联合登陆用户
		User openUser = (User) request.getAttribute("openUser");
		if (openUser != null && openUser.getUserName() != null)
		{
			u = openUser;
		} else
		{
			model.setUserName(model.getUserName().trim());
			model.setPwd(model.getPwd().trim());
			UserModel userModel = UserModel.instance(model);
			String validCode = request.getParameter("validCode");
			if (validCode != null)
			{
				userModel.setValidCode(validCode);
				userModel.validRegRule();
			}
			userModel.validLoginModel();
			String openType = this.paramString("openType");
			String openId = this.paramString("openId");
			if (openType != null && openType.length() > 0 && openId != null
					&& openId.length() > 0)
			{// 判断数据类型是否正确
				u = cooperationLoginService.doQQLogin(userModel.prototype(),
						paramInt("encrypt"), openType, openId);
			} else
			{
				u = userService.doLogin(userModel.prototype(),
						paramInt("encrypt"));
			}
			// 论坛信息处理
			if (u != null && u.getUserName() != null)
			{
				Cookie cookie = new Cookie("jforumUserInfo", URLEncoder.encode(
						u.getUserName(), "utf-8"));
				cookie.setMaxAge(-1);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			if (u == null)
			{
				if (validCode == null)
				{
					// 记录用户错误次数到Cookie
					addCookie();
				} else
				{
					// 若一个用户名多次登录失败，则记录失败次数，超过一定值就锁定
					recordFailLogin(model.getUserName());
				}
				data.put("result", false);
				data.put("msg", "用户名或密码错误");
			} else
			{
				// 用户不为空，则清除cookie,且判断是否邮箱是否为激活或账号被锁定
				clearCookie(u);
			}
		}
		request.getSession().setAttribute("user", u);
		request.getSession().setAttribute(ConstantUtil.SESSION_USER, u);
		session.put(Constant.SYSTEM, Constant.COOPERATE_TYPE__PC);//从PC登陆
		printWebJson(getStringOfJpaObj(data));
	}

	/**
	 * 记录用户错误次数到Cookie
	 */
	private void addCookie()
	{
		int count = 0;
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				String name = cookie.getName();
				if ("count".equals(name))
				{
					String value = cookie.getValue();
					if (value != null)
					{
						count = Integer.parseInt(value);
						count++;
						cookie.setValue(count + "");
						response.addCookie(cookie);
					}
					break;
				}
			}
		}
		if (count == 0)
		{
			Cookie ck = new Cookie("count", "1");
			ck.setMaxAge(24 * 3600);
			response.addCookie(ck);
		} else if (count == loginRuleCheck.max_fail_times_code)
		{
			data.put("count", true);
		}
	}

	/**
	 * 若一个用户名多次登录失败，则记录失败次数，超过一定值就锁定
	 * 
	 * @param userName
	 *            用户名
	 */
	private void recordFailLogin(String userName)
	{
		model = userService.getUserByUserName(userName);
		long timeRange = loginRuleCheck.time_range;
		int loginFailMaxTimes = loginRuleCheck.max_login_fail_times;
		if (model != null)
		{
			if (request.getSession().getAttribute(userName) == null)
			{
				request.getSession().setAttribute(userName,
						System.currentTimeMillis());
			}
			UserCache uc = userCacheService.findByUserId(model.getUserId());
			long firstTime = (Long) request.getSession().getAttribute(userName);
			if ((System.currentTimeMillis() - firstTime < timeRange * 1000)
					&& (uc.getLoginFailTimes() + 1 == loginFailMaxTimes))
			{
				uc.setRemark("多次登陆失败系统自动锁定");
				uc.setStatus(1);
				uc.setLockTime(new Date());
			} else if (System.currentTimeMillis() - firstTime > timeRange * 1000)
			{
				uc.setLoginFailTimes(1);
				request.getSession().setAttribute(userName,
						System.currentTimeMillis());
			}
			uc.setLoginFailTimes(uc.getLoginFailTimes() + 1);
			userCacheService.update(uc);
		}
	}

	/**
	 * 用户不为空，则清除cookie,且判断是否邮箱是否为激活或账号被锁定
	 * 
	 * @param u
	 *            User对象
	 */
	private void clearCookie(User u)
	{
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				String name = cookie.getName();
				if ("count".equals(name))
				{
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					break;
				}
			}
		}
		UserCache userCache = userCacheService.findById(u.getUserCache().getId());
		UserIdentify userIdentify = userIdentifyService.findByUserId(u
				.getUserId());
	
		if (userCache.getStatus() == 1)
		{
			data.put("result", false);
			data.put("msg", "该账户" + u.getUserName() + "已经被锁定！");
		} else
		{
			session.put(Constant.SESSION_USER, u);
			session.put(Constant.SESSION_USER_IDENTIFY, userIdentify);
			session.put("logintime", System.currentTimeMillis());
			userCache.setLoginTime(new Date());
			userCache.setLoginFailTimes(0);
			userCacheService.update(userCache);
			data.put("result", true);
			data.put("msg", "登录成功！");
			data.put("userName", u.getUserName());
			data.put("userNature", u.getUserCache().getUserNature());
			data.put("imgurl", "/avatar/" + u.getUserId() + ".jpg");
			data.put("redirectURL", paramString("redirectURL"));
		}
	}

	/**
	 * 退出
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/logout")
	public String logout() throws Exception
	{
		session.put(Constant.SESSION_USER, null);
		session.put("logintime", null);
		// 清空cookie
		Cookie cookie = new Cookie("jforumUserInfo", "");
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		frontRedirect("/user/login.html");
		return null;
	}

	/**
	 * 邮箱激活 重新发送激活邮件
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/sentActivationEmail")
	public String sentActivationEmail() throws Exception
	{
		if (model == null || model.getUserId() == 0)
		{
			throw new UserException("用户不存在，激活邮件发送失败！", 1);
		}
		model = userService.getUserById(model.getUserId());

		Global.setTransfer("user", model);
		Global.setTransfer("activeUrl", "/user/doRegisterStep1.html?id="
				+ Mail.getInstance().getdecodeIdStr(model));

		BaseMsg msg = new BaseMsg(NoticeConstant.NOTICE_EMAIL_ACTIVE);
		msg.doEvent();

		printWebSuccess();
		return null;
	}

	/**
	 * 找回密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/getpwd")
	public String getPwd() throws Exception
	{

		int showType = paramInt("showType");
		int getType = paramInt("getType");
		request.setAttribute("getType", getType);
		if (showType == 1)
		{
			return "getpwd_email1";
		} else if (showType == 2)
		{
			return "getpwd_phone1";
		}
		int step = paramInt("step");
		BaseGetPwd getpwd = null;
		if (getType == 1)
		{
			getpwd = new GetPwdEmail();
			switch (step)
			{
			case 1:
				try
				{// 步骤一
					getpwd.getPwdStep1(request, model, paramString("valicode"));
				} catch (UserException e)
				{
					throw new UserException(e.getMessage(), 1);
				} catch (Exception e)
				{
					throw new UserException("操作异常，请联系管理员", 1);
				}
				return null;
			case 2:
				try
				{// 步骤二
					User userAll = userService.getUserByEmail(model.getEmail());
					getpwd.getPwdStep2(userAll, paramString("code"));
					String getPwdSign = userAll.getEmail();
					session.put("getPwdSign", getPwdSign);
					request.setAttribute("userName", userAll.getUserName());
					request.setAttribute("email", getPwdSign);
					request.setAttribute("type", getType);
				} catch (Exception e)
				{
					throw new UserException(e.getMessage());
				}
				return "getpwd_reset";
			case 3:
				try
				{// 密码重置
					getpwd.getPwdReset(session, model,
							paramString("confirmNewPwd"));
					User userAll = userService.getUserByEmail(model.getEmail());
					request.setAttribute("userName", userAll.getUserName());
				} catch (Exception e)
				{
					throw new UserException(e.getMessage());
				}
				return "getpwd_success";
			default:
				break;
			}
		} else if (getType == 2)
		{
			getpwd = new GetPwdPhone();
			switch (step)
			{
			case 1:
				try
				{// 步骤一
					getpwd.getPwdStep1(request, model, paramString("valicode"));

				} catch (UserException e)
				{
					throw new UserException(e.getMessage(), 1);
				} catch (Exception e)
				{
					throw new UserException("操作异常，请联系管理员", 1);
				}
				return null;
			case 2:
				try
				{// 步骤二
					User userAll = userService.getUserByMobilePhone(model
							.getMobilePhone());
					getpwd.getPwdStep2(userAll, paramString("code"));
					String getPwdSign = userAll.getMobilePhone();
					session.put("getPwdSign", getPwdSign);
					request.setAttribute("userName", userAll.getUserName());
					request.setAttribute("mobilePhone", getPwdSign);
					request.setAttribute("type", getType);
				} catch (Exception e)
				{
					throw new UserException(e.getMessage());
				}
				return "getpwd_reset";
			case 3:
				try
				{// 密码重置

					User userAll = userService.getUserByMobilePhone(model
							.getMobilePhone());
					session.put("getPwdSign", userAll.getMobilePhone());
					getpwd.getPwdReset(session, model,
							paramString("confirmNewPwd"));
				} catch (Exception e)
				{
					throw new UserException(e.getMessage());
				}
				return "getpwd_success";
			default:
				break;
			}
		}
		return "getpwd_email1";
	}

	/**
	 * 进入第二步邮箱验证
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/getpwd_email2")
	public String getpwd_email2() throws Exception
	{
		User user = userService.getUserByEmail(model.getEmail());
		request.setAttribute("user", user);
		return "getpwd_email2";
	}

	/**
	 * 进入第二步手机验证
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/getpwd_phone2")
	public String getpwd_phone2() throws Exception
	{
		User user = userService.getUserByMobilePhone(model.getMobilePhone());
		request.setAttribute("userInfo", user);
		return "getpwd_phone2";
	}

	/**
	 * 找回密码-重新获取校验码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/getPwdCode")
	public String getPwdCode() throws Exception
	{
		// String todo = paramString("todo");
		User u;
		String todo;
		if (model.getEmail() != null)
		{
			todo = "get_pwd_email";
			u = userService.getUserByEmail(model.getEmail());
		} else
		{
			todo = "get_pwd_phone";
			u = userService.getUserByMobilePhone(model.getMobilePhone());
		}
		BaseAccountLog blog = new GetCodeLog(u, u.getUserName(), todo);
		blog.initCode(todo);
		blog.doEvent();
		printWebSuccess();
		return null;
	}

	/**
	 * 找回交易密码-重新获取校验码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/getPayPwdCode")
	public String getPayPwdCode() throws Exception
	{
		User u;
		String todo;
		if (model.getEmail() != null)
		{
			todo = "get_pay_pwd_email";
			u = userService.getUserByEmail(model.getEmail());
		} else
		{
			todo = "get_pay_pwd_phone";
			u = userService.getUserByMobilePhone(model.getMobilePhone());
		}
		BaseAccountLog blog = new GetCodeLog(u, u.getUserName(), todo);
		blog.initCode(todo);
		blog.doEvent();
		printWebSuccess();
		return null;
	}

	/**
	 * 找回交易密码
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "/user/getPayPwd", interceptorRefs =
	{ @InterceptorRef("session"), @InterceptorRef("globalStack") })
	public String getPayPwd() throws Exception
	{
		User user = getSessionUser();
		user = userService.getUserById(user.getUserId());
		request.setAttribute("userInfo", user);
		int showType = paramInt("showType");
		int getType = paramInt("getType");
		request.setAttribute("getType", getType);

		BaseGetPwd getpwd = null;
		if (showType == 1)
		{
			getpwd = new GetPwdEmail();
			try
			{// 步骤一
				getpwd.getPayPwdStep1(request, user, paramString("valicode"));
			} catch (UserException e)
			{
				throw new UserException(e.getMessage(), 1);
			} catch (Exception e)
			{
				throw new UserException("操作异常，请联系管理员", 1);
			}
			return "getpaypwd_email";
		} else if (showType == 2)
		{
			getpwd = new GetPwdPhone();
			try
			{// 步骤一
				getpwd.getPayPwdStep1(request, user, paramString("valicode"));
			} catch (UserException e)
			{
				throw new UserException(e.getMessage(), 1);
			} catch (Exception e)
			{
				throw new UserException("操作异常，请联系管理员", 1);
			}
			return "getpaypwd_phone";
		}
		int step = paramInt("step");
		if (getType == 1)
		{
			getpwd = new GetPwdEmail();
			switch (step)
			{
			case 1:
				try
				{// 步骤二
					User userAll = userService.getUserByEmail(model.getEmail());
					getpwd.getPayPwdStep2(userAll, paramString("code"));
					request.setAttribute("userName", userAll.getUserName());
					request.setAttribute("email", userAll.getEmail());
					request.setAttribute("type", getType);
				} catch (Exception e)
				{
					throw new UserException(e.getMessage());
				}
				return "getpaypwd_reset";
			case 2:
				try
				{// 密码重置
					getpwd.getPayPwdReset(session, model,
							paramString("confirmNewPwd"));
				} catch (Exception e)
				{
					throw new UserException(e.getMessage());
				}
				return "getpaypwd_success";
			default:
				break;
			}
		} else if (getType == 2)
		{
			getpwd = new GetPwdPhone();
			switch (step)
			{
			case 1:
				try
				{// 步骤二
					User userAll = userService.getUserByMobilePhone(model
							.getMobilePhone());
					getpwd.getPayPwdStep2(userAll, paramString("code"));
					request.setAttribute("userName", userAll.getUserName());
					request.setAttribute("mobilePhone",
							userAll.getMobilePhone());
					request.setAttribute("type", getType);
				} catch (Exception e)
				{
					throw new UserException(e.getMessage());
				}
				return "getpaypwd_reset";
			case 2:
				try
				{// 密码重置
					getpwd.getPayPwdReset(session, model,
							paramString("confirmNewPwd"));
				} catch (Exception e)
				{
					throw new UserException(e.getMessage());
				}
				return "getpaypwd_success";
			default:
				break;
			}
		}
		return "getpaypwd_email1";
	}

	/**
	 * 进入第二步邮箱验证
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/getpaypwd_email2")
	public String getpaypwd_email2() throws Exception
	{
		User user = userService.getUserByEmail(model.getEmail());
		request.setAttribute("user", user);
		return "getpaypwd_email2";
	}

	/**
	 * 进入第二步手机验证
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/getpaypwd_phone2")
	public String getpaypwd_phone2() throws Exception
	{
		User user = userService.getUserByMobilePhone(model.getMobilePhone());
		request.setAttribute("userInfo", user);
		return "getpaypwd_phone2";
	}

	/**
	 * 个人借款注册页面
	 */
	@Action("/user/personalBorrowRegister")
	public String personalBorrowRegister() throws Exception
	{
		return "personalBorrowRegister";
	}

	/**
	 * 个人投资注册页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/personalRegister")
	public String personalRegister() throws Exception
	{
		request.setAttribute("ui", paramString("ui"));
		request.setAttribute("enable_codeCheck",
				registerRuleCheck.enable_codeCheck);// 是否需要验证码
		request.setAttribute("distinction_type",
				Global.getInt("distinction_type")); // 是否区分借款人与投资人
		initRSAME();// RSA初始化
		return "personalRegister";
	}

	/**
	 * 注册时邀请码添加时必须存在
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("/user/inviteRegister")
	public void inviteRegister() throws Exception
	{
		data = new HashMap<String, Object>();
		UserPromot promot = userPromotService
				.getUserPromotByCode(paramString("ui"));
		boolean result = true;
		if (promot != null)
		{
			result = true;
		} else
		{
			result = false;
		}
		printWebJson(getStringOfJpaObj(result));
	}

	/**
	 * 校验交易密码
	 * 
	 * @throws Exception
	 */
	@Action(value = "/user/checkPayPwd", interceptorRefs =
	{ @InterceptorRef("ajaxSafe"), @InterceptorRef("globalStack") })
	public void checkPayPwd() throws Exception
	{
		User user = userService.getUserById(getSessionUserId());

		if (!MD5.encode(paramString("payPwd")).equals(user.getPayPwd()))
		{
			printWebResult("支付密码错误", false);
		} else
		{
			printWebSuccess();
		}
	}

	public RegisterRuleCheck getRegisterRuleCheck()
	{
		return registerRuleCheck;
	}

	public void setRegisterRuleCheck(RegisterRuleCheck registerRuleCheck)
	{
		this.registerRuleCheck = registerRuleCheck;
	}

	public LoginRuleCheck getLoginRuleCheck()
	{
		return loginRuleCheck;
	}

	public void setLoginRuleCheck(LoginRuleCheck loginRuleCheck)
	{
		this.loginRuleCheck = loginRuleCheck;
	}

	public SafetyRuleCheck getSafetyRuleCheck()
	{
		return safetyRuleCheck;
	}

	public void setSafetyRuleCheck(SafetyRuleCheck safetyRuleCheck)
	{
		this.safetyRuleCheck = safetyRuleCheck;
	}

	public User getUser()
	{
		return model;
	}

	public void setUser(User user)
	{
		this.model = user;
	}

}
