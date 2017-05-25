package com.rongdu.p2psys.wechat;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountBank;
import com.rongdu.p2psys.account.domain.AccountSum;
import com.rongdu.p2psys.account.model.accountlog.BaseAccountLog;
import com.rongdu.p2psys.account.model.accountlog.noac.GetCodeLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.account.service.AccountBankService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.account.service.AccountSumService;
import com.rongdu.p2psys.nb.ppfund.service.ExperienceGoldService;
import com.rongdu.p2psys.nb.recommend.domain.RecommendRecord;
import com.rongdu.p2psys.nb.recommend.service.RecommendRecordService;
import com.rongdu.p2psys.nb.recommend.service.RedPacketService;
import com.rongdu.p2psys.nb.score.service.ScoreService;
import com.rongdu.p2psys.nb.user.service.UserBaseInfoService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserCreditService;
import com.rongdu.p2psys.nb.user.service.UserIdentifyService;
import com.rongdu.p2psys.nb.user.service.UserInviteService;
import com.rongdu.p2psys.nb.user.service.UserPromotService;
import com.rongdu.p2psys.nb.user.service.UserRedPacketService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.user.service.UserVipService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserCredit;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.domain.UserInvite;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.domain.UserRedPacket;
import com.rongdu.p2psys.user.domain.UserVip;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.util.AgentUtil;
import com.rongdu.p2psys.util.WechatData;

/**
 * 
 * @author Jinx
 *
 */
public class WechatUserAction extends BaseAction<UserModel> implements ModelDriven<UserModel>
{
	private Map<String,Object> map;
	
	@Resource
	private UserService theUserService;
	@Resource
	private UserCacheService theUserCacheService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private UserIdentifyService theUserIdentifyService;
	@Resource
	private UserPromotService theUserPromotService;
	@Resource
	private UserInviteService theUserInviteService;
	@Resource
	private AccountBankService theAccountBankService;
	@Resource
	private WechatCacheService wechatCacheService;
	@Resource
	private ExperienceGoldService theExperienceGoldService;
	@Resource
	private UserVipService theUserVipService;
	@Resource
	private UserCreditService theUserCreditService;
	@Resource 
	private ScoreService theScoreService;
	@Resource
	private UserBaseInfoService theUserBaseInfoService;
	@Resource
	private AccountSumService theAccountSumService;
	@Resource
	private UserRedPacketService theUserRedPacketService;
	@Resource
	private RedPacketService theRedPacketService;
	@Resource
	private RecommendRecordService recommendRecordService;
	
	/**
	 * 非微信移动端登陆页面
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/land" )
	public String wechatIndex() throws IOException
	{
		String redirectURL = paramString("redirectURL");
		
		request.setAttribute("redirectURL",redirectURL);
		
		return "land";
	}
	
	/**
	 * 取消微信绑定
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/cacleBind" )
	public void wechatCacleBind() throws IOException
	{
		map = new HashMap<String, Object>();
		
		User user = getNBSessionUser();
		
		List<User> list = theUserService.getByGroupId(user.getBindId());
		
		for(int i = 0;i<list.size();i++)
		{
			if(!"".equals(list.get(i).getWechatOpenId())&&null!=list.get(i).getWechatOpenId())
			{
				list.get(i).setBindId(list.get(i).getUserId());
				
				theUserService.updateUser(list.get(i));
				
				setAttr(ConstantUtil.SESSION_USER, null);
				setAttr(Constant.SESSION_USER, null);
				session.put(Constant.SESSION_USER,null);	
				
				map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
				map.put(ConstantUtil.ERRORMSG,"解绑成功!");
			}
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 退出登陆
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/landOut" )
	public String loginOut() throws IOException
	{
		String redirectURL = paramString("redirectURL");
		
		request.setAttribute("redirectURL",redirectURL);
		
		setAttr(ConstantUtil.SESSION_USER, null);
		setAttr(Constant.SESSION_USER, null);
		session.put(Constant.SESSION_USER,null);	
		
		return "land";
	}
	
	/**
	 * 验证登陆
	 * 
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/executeLogin" )
	public void executeLogin() throws IOException
	{
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String userName = paramString("tel");
		String pwd = paramString("pwd");
		
		User user = theUserService.getByUserName(userName);
		
		if(null!=user)
		{
			UserCache userCache = theUserCacheService.getById(user.getUserCache().getId());
			
			if(1!=userCache.getStatus())
			{
				user = theUserService.doLogin(userName, pwd);
				
				if(null!=user)
				{
					if(1!=userCache.getStatus())
					{
						userCache.setLoginFailTimes(0);
						userCache.setStatus(0);
						userCache.setLockTime(new Date());
						userCache.setLoginTime(new Date());
						theUserCacheService.updateUserCache(userCache);
						
						map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
						map.put(ConstantUtil.ERRORMSG,"登陆成功!");
						
						setAttr(ConstantUtil.SESSION_USER, user);
						setAttr(Constant.SESSION_USER, user);
						session.put(Constant.SESSION_USER, user);
						session.put(Constant.SYSTEM, Constant.COOPERATE_TYPE__WECHAT);//从微信登陆
					}
					else
					{
						map.put(ConstantUtil.ERRORMSG,"您的账户已被锁定，请联系客服!");
					}
				}
				else
				{
					if(4==userCache.getLoginFailTimes())
					{
						userCache.setLoginFailTimes(5);
						userCache.setStatus(1);
						userCache.setLockTime(new Date());
						
						theUserCacheService.updateUserCache(userCache);
						
						map.put(ConstantUtil.ERRORMSG,"您的账户已被锁定，请联系客服!");
					}
					else
					{
						userCache.setLoginFailTimes(userCache.getLoginFailTimes()+1);
						theUserCacheService.updateUserCache(userCache);
						
						map.put(ConstantUtil.ERRORMSG,"账号或密码错误!");
					}
				}
			}
			else
			{
				map.put(ConstantUtil.ERRORMSG,"您的账户已被锁定，请联系客服!");
			}
		}
		else
		{
			map.put(ConstantUtil.ERRORMSG,"账号或密码错误!");
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 设置页面
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/setting" )
	public String wechatSetting() throws IOException
	{
		User user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		
		request.setAttribute("user_id",user.getUserId());
		request.setAttribute("promotCode","");
		request.setAttribute("wechatUrl",getWechatUrl());
		
		if(AgentUtil.judgeAgent(request))
		{
			request.setAttribute("wechat","success");
		}
		else
		{
			request.setAttribute("wechat","error");
		}
		
		UserPromot userPromot = theUserPromotService.findUserPromotByUserId(user.getUserId());
		if(null!=userPromot)
		{
			if(null!=userPromot.getCouponCode()&&!"".equals(userPromot.getCouponCode()))
			{
				request.setAttribute("promotCode",userPromot.getCouponCode());
			}
		}
		else
		{
			userPromot = new UserPromot();
			
			userPromot.setUser(user);
		    userPromot = theUserPromotService.saveWechatUserPromot(userPromot);
		    request.setAttribute("promotCode",userPromot.getCouponCode());
		}
		if(null!=user.getPayPwd()&&!"".equals(user.getPayPwd()))
		{
			request.setAttribute("payPwd","has");
		}
		else
		{
			request.setAttribute("payPwd","nothas");
		}
		
		List<AccountBank> bankList = theAccountBankService.list(user.getUserId());
		request.setAttribute("bankList", bankList);
		
		return "setting";
	}
	
	/**
	 * 手机账号列表
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/user" )
	public String userList() throws IOException
	{
		User user = getNBSessionUser();
		
		request.setAttribute("user_id",user.getUserId());
		
		return "user";
	}
	
	/**
	 * 设置
	 * 
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/settingJson" )
	public void wechatSettingDetail() throws IOException
	{
		User user = getNBSessionUser();
		
		map = new HashMap<String, Object>();
		
		map.put("bind_type",false);
		map.put("user",user);
		
		List<User> list = theUserService.getByGroupId(user.getBindId());
		
		int bank_num = 0;
		
		if(list.size()>1)
		{
			map.put("bind_type",true);
			map.put("user_list",list);
			
			for(int i = 0;i<list.size();i++)
			{
				List<AccountBank> bank_list = theAccountBankService.getByUserId(list.get(i).getUserId());
				
				if(null!=bank_list&&!bank_list.isEmpty())
				{
					bank_num++;
				}
			}
			if(0!=bank_num)
			{
				map.put("bank_num",1);
			}
			else
			{
				map.put("bank_num",0);
			}
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 跳转注册页面
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/register" )
	public String wechatRegister() throws IOException
	{
		String invite_code = paramString("invite_code");
		String redirectURL = paramString("redirectURL");
		
		request.setAttribute("invite_code",invite_code);
		request.setAttribute("redirectURL",redirectURL);
		
		return "register";
	}
	
	/**
	 * 执行修改交易密码
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/doChangePayPwd" )
	public void doChangeWechatPayPwd() throws IOException
	{
		User user = getNBSessionUser();
		
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String pwd = paramString("pwd");
		String newpwd = paramString("newpwd");
		
		if(!MD5.encode(pwd).equals(user.getPayPwd()))
		{
			map.put(ConstantUtil.ERRORMSG,"原始密码不正确!");
		}
		else if(pwd.equals(newpwd))
		{
			map.put(ConstantUtil.ERRORMSG,"新密码不能与旧密码相同!");
		}
		else
		{
			user.setPayPwd(MD5.encode(newpwd));
			theUserService.updateUser(user);
			
			UserCache userCache = user.getUserCache();
			userCache.setPayFailTimes(0);
			theUserCacheService.updateUserCache(userCache);
			
			setAttr(ConstantUtil.SESSION_USER, user);
			setAttr(Constant.SESSION_USER, user);
			session.put(Constant.SESSION_USER, user);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"修改成功!");

		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 执行修改密码
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/doChangePwd" )
	public void doChangeWechatPwd() throws IOException
	{
		User user = getNBSessionUser();
		
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String pwd = paramString("pwd");
		String newpwd = paramString("repwd");
		
		if(!MD5.encode(pwd).equals(user.getPwd()))
		{
			map.put(ConstantUtil.ERRORMSG,"原始密码不正确!");
		}
		else if(pwd.equals(newpwd))
		{
			map.put(ConstantUtil.ERRORMSG,"新密码不能与旧密码相同!");
		}
		else
		{
			user.setPwd(MD5.encode(newpwd));
			theUserService.updateUser(user);
			
			UserCache userCache = user.getUserCache();
			userCache.setLoginFailTimes(0);
			theUserCacheService.updateUserCache(userCache);
			
			setAttr(ConstantUtil.SESSION_USER, user);
			setAttr(Constant.SESSION_USER, user);
			session.put(Constant.SESSION_USER, user);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"修改成功!");
		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 修改密码
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/changePwd" )
	public String changeWechatPwd() throws IOException
	{
		String redirectURL = paramString("redirectURL");
		
		request.setAttribute("redirectURL",redirectURL);
		
		return "changePwd";
	}
	
	/**
	 * 修改交易密码
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/changePayPwd" )
	public String changeWechatPayPwd() throws IOException
	{
		String redirectURL = paramString("redirectURL");
		
		request.setAttribute("redirectURL",redirectURL);
		
		return "changePayPwd";
	}
	
	/**
	 * 设置交易密码
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/payPwdSetting" )
	public String wechatPayPwd() throws IOException
	{
		String redirectURL = paramString("redirectURL");
		
		request.setAttribute("redirectURL",redirectURL);
		
		return "payPwdSetting";
	}
	
	/**
	 * 执行设置交易密码
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/account/settingPayPwd" )
	public void settingPayPwd() throws IOException
	{
		User user = getNBSessionUser();
		
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String payPwd = paramString("payPwd");
		String rePayPwd = paramString("rePayPwd");
		if(null!=user.getPayPwd()&&!"".equals(user.getPayPwd()))
		{
			map.put(ConstantUtil.ERRORMSG,"此账号已经有交易密码!");
		}
		else if(!payPwd.equals(rePayPwd))
		{
			map.put(ConstantUtil.ERRORMSG,"两次密码不一致!");
		}
		else if(payPwd.length()!=6)
		{
			map.put(ConstantUtil.ERRORMSG,"交易密码长度不为6!");
		}
		else
		{
			user.setPayPwd(MD5.encode(payPwd));
			theUserService.updateUser(user);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"设置成功!");
		}
		
		printWebJson(getStringOfJpaObj(map));
		
	}
	
	/**
	 * 忘记密码
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/wechat/forgetPwd" )
	public String forgetPwd() throws IOException
	{
		request.setAttribute("redirectURL",paramString("redirectURL"));
		request.setAttribute("tel",paramString("tel"));
		
		return "forgetPwd";
	}
	
	/**
	 * 执行校验支付密码
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/checkPayPwd" )
	public void executecheckPayPwd() throws IOException
	{
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String key = paramString("key");
		
		User user = getNBSessionUser();
		UserCache userCache = theUserCacheService.getById(user.getUserCache().getId());
		if(0==userCache.getPayPwdStatus())
		{
			if(MD5.encode(key).equals(user.getPayPwd()))
			{
				userCache.setPayFailTimes(0);
				userCache.setPayPwdStatus(0);
				theUserCacheService.updateUserCache(userCache);
				
				map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
				map.put(ConstantUtil.ERRORMSG,"验证成功!");
			}
			else
			{
				if(3==userCache.getPayFailTimes())
				{
					userCache.setPayFailTimes(4);
					theUserCacheService.updateUserCache(userCache);
					
					map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
					map.put(ConstantUtil.ERRORMSG,"交易输入错误4次，再次输错交易密码将会被锁定!");
				}
				else if(4==userCache.getPayFailTimes())
				{
					userCache.setPayFailTimes(5);
					userCache.setPayPwdStatus(1);
					theUserCacheService.updateUserCache(userCache);
					
					map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
					map.put(ConstantUtil.ERRORMSG,"您的交易密码已经被锁定，请联系客服!");
				}
				else
				{
					userCache.setPayFailTimes(userCache.getPayFailTimes()+1);
					theUserCacheService.updateUserCache(userCache);
					
					map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
					map.put(ConstantUtil.ERRORMSG,"交易密码错误!");
				}
			}
		}
		else
		{
			map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
			map.put(ConstantUtil.ERRORMSG,"您的交易密码已被锁定，请联系客服!");
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	
	/**
	 * 执行找回密码
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/getPwd" )
	public void executeGetPwd() throws IOException
	{
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String tel = paramString("tel");
		String pwd = MD5.encode(paramString("pwd"));
		
		UserModel userModel = UserModel.instance(model);
		userModel.setCode(paramString("tel_code"));
		userModel.setMobilePhone(tel);
		
		String valid_tel_code_result = userModel.validGetPwdCode();
		
		if(!ConstantUtil.SUCCESS.equals(valid_tel_code_result))
		{
			map.put(ConstantUtil.ERRORMSG,valid_tel_code_result);
		}
		else
		{
			User user = new User();
			user = theUserService.getByUserName(tel);
			user.setPwd(pwd);
			theUserService.updateUser(user);
			
			UserCache userCache = user.getUserCache();
			userCache.setPayPwdModifyTime(new Date());
			theUserCacheService.updateUserCache(userCache);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"重置密码成功!");
			
			setAttr(ConstantUtil.SESSION_USER, null);
			
			String todo = "password_update";
			
			BaseAccountLog blog = new GetCodeLog(user, user.getUserName(),todo);
			blog.initCode(todo);
			blog.doEvent();
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 执行注册
	 * @throws IOException 
	 */
	@SuppressWarnings("static-access")
	@Action(value = "/nb/wechat/executeRegister" )
	public void executeRegister() throws IOException
	{
		map  = new HashMap<String, Object>();
		
		String invite_code = paramString("invite_code");
		String tel = paramString("tel");
		String pwd = paramString("pwd");
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		UserModel userModel = UserModel.instance(model);
		userModel.setValidCode(paramString("valid_code"));
		userModel.setCode(paramString("tel_code"));
		userModel.setMobilePhone(tel);
		
		int valid_code_result = userModel.validWechatRegRule();
		String valid_tel_code_result = userModel.validRegisterTelCode();
		
		
		User testUser = theUserService.getByUserName(tel);
		
		if(valid_code_result>=0)
		{
			map.put(ConstantUtil.ERRORMSG,"验证码错误,点击验证码图片刷新验证码!");
		}
		else if(!ConstantUtil.SUCCESS.equals(valid_tel_code_result))
		{
			map.put(ConstantUtil.ERRORMSG,valid_tel_code_result);
		}
		else if(null!=testUser)
		{
			map.put(ConstantUtil.ERRORMSG,"账号已存在!");
		}
		else
		{
			User user = new User();
			
			user.setAddTime(new Date());
			user.setMobilePhone(tel);
			user.setPwd(MD5.encode(pwd));
			user.setUserName(tel);
			
			user =  theUserService.saveUser(user);
			
			user.setBindId(user.getUserId());
			
			theUserService.updateUser(user);
			
			UserCache userCache = new UserCache();
			userCache.setUser(user);
			userCache.setAddIp(Global.getIP());
			userCache =  theUserCacheService.saveWechatUserCache(userCache);
			
			Account account = new Account();
			account.setUser(user);
			theAccountService.save(account);
			
			UserIdentify userIdentify = new UserIdentify();
			userIdentify.setUser(user);
			theUserIdentifyService.saveWechatUserIdentify(userIdentify);
			
			UserPromot userPromot = new UserPromot();
			userPromot.setUser(user);
			theUserPromotService.saveWechatUserPromot(userPromot);
			
			UserVip userVip = new UserVip();
			userVip.setUser(user);
			theUserVipService.saveWechatUserVip(userVip);
			
			theUserBaseInfoService.saveUserBaseInfo(userCache);
			theUserCreditService.saveUserCredit(new UserCredit(user));
			theAccountSumService.saveAccountSum(new AccountSum(user));
			theScoreService.saveScore(new Score(user));
			
			System.out.println("invite code is:"+invite_code+", new user's id is"+user.getUserId());
			/**
			 * 如果含有邀请码
			 */
			if(!"".equals(invite_code)&&null!=invite_code)
			{
				UserPromot invite = theUserPromotService.getUserPromotByCode(invite_code.toLowerCase());
				
				User invite_user = new User();
				
				if(null!=invite)
				{
					invite_user = invite.getUser();
					/**
					 * 有邀请码的注册
					 */
					if(null!=invite_user)
					{
						/**
						 * 推荐码使用记录
						 */
						UserInvite userInvite = new UserInvite();
						userInvite.setInviteTime(new Date());
						userInvite.setInviteUser(invite_user);
						userInvite.setUser(user);
						userInvite.setGift(false);
						
						theUserInviteService.saveUserInvite(userInvite);
						/**
						 * 邀请码
						 */
						UserPromot userPromot2 = theUserPromotService.findUserPromotByUserId(invite_user.getUserId());
						userPromot2.setUsedTimes(userPromot2.getUsedTimes()+1);
						userPromot2.setCanUseTimes(userPromot2.getCanUseTimes()-1);
						
						theUserPromotService.updateUserPromot(userPromot2);
						
						Date date=new Date();
						Calendar calendar = new GregorianCalendar(); 
						calendar.setTime(date); 
						
						RedPacket redPacket = theRedPacketService.getByServiceType(ConstantUtil.RED_PACKET_RECOMMEND);
						
						calendar.add(calendar.DATE,redPacket.getDay());
						date=calendar.getTime();  
						/**
						 * 红包是否开启
						 */
						if(1==redPacket.getStatus())
						{
							/**
							 * 添加推荐红包
							 */
							UserRedPacket userRedPacket = new UserRedPacket();
							userRedPacket.setAddTime(new Date());
							userRedPacket.setAmount(redPacket.getMoney());
							userRedPacket.setExpiredTime(date);
							userRedPacket.setRedPacketType(2);
							userRedPacket.setType(redPacket);
							userRedPacket.setUsed(false);
							userRedPacket.setUser(invite_user);
							
							userRedPacket =  theUserRedPacketService.saveUserRedPacket(userRedPacket);
							/**
							 * 添加推荐记录
							 */
							RecommendRecord recommendRecord = new RecommendRecord();
							recommendRecord.setUserRedPacket(userRedPacket);
							recommendRecord.setAccount(userRedPacket.getAmount());
							recommendRecord.setInviteUser(invite_user);
							recommendRecord.setName(userRedPacket.getType().getServiceName());
							recommendRecord.setUser(user);
							
							recommendRecordService.saveRecommendRecord(recommendRecord);
						}
					}
				}
			}
			
			setAttr(ConstantUtil.SESSION_USER, user);
			setAttr(Constant.SESSION_USER, user);
			session.put(Constant.SESSION_USER, user);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"注册成功!");
			
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 判断账号是否存在
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/userExists")
	public void userExists() throws IOException
	{
		map = new HashMap<String, Object>();
		String tel = paramString("tel");
		
		User user = theUserService.getByUserName(tel);
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		if(null!=user)
		{
			map.put(ConstantUtil.ERRORMSG,"号码已存在");
		}
		else
		{
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"验证成功!");
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 写数据
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/writeBindData")
	public void writeBindData() throws IOException
	{
		map = new HashMap<String, Object>();
		
		System.out.println("bindData:"+paramString("bindData"));
		
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 发送注册手机验证码
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/getRegisterCode")
	public void sendRegisterCode() throws IOException
	{
		map = new HashMap<String, Object>();
		String tel = paramString("tel");
		
		BaseAccountLog blog = new GetCodeLog(null,"",null,tel,NoticeConstant.REGISTER_GET_CODE);
		blog.initCode("registerGetCode");
		blog.doEvent();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 发送找回密码手机验证码
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/getPwdCode")
	public void sendGetPwdCode() throws IOException
	{
		map = new HashMap<String, Object>();
		String tel = paramString("tel");
		
		User user = new User();
		String todo = "get_pwd_phone";
		
		user = theUserService.getByUserName(tel);
		
		if(null!=user)
		{
			BaseAccountLog blog = new GetCodeLog(user, user.getUserName(), todo);
			blog.initCode(todo);
			blog.doEvent();
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		}
		else
		{
			map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
			map.put(ConstantUtil.ERRORMSG,"用户不存在");
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	
	/**
	 * 发送注册手机验证码
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/getBindCode")
	public void sendBindCode() throws IOException
	{
		map = new HashMap<String, Object>();
		
		String mobilePhone = paramString("tel");
		
		BaseAccountLog blog = new GetCodeLog(null, "", null, mobilePhone,NoticeConstant.BIND_CODE);
		blog.initCode("bindPhoneCode");
		blog.doEvent();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 绑定账号
	 * @throws IOException 
	 */
	@SuppressWarnings("static-access")
	@Action(value = "/nb/wechat/bindUser")
	public void bindUser() throws IOException
	{
		map = new HashMap<String, Object>();
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String bind_type = paramString("bind_status");
		String tel = paramString("tel");
		String pwd = paramString("pwd");
		String yzm = paramString("yzm");
		String couponCode = StringUtil.replaceBlank(paramString("couponCode"));
		
		User user =  theUserService.getByUserName(tel);
		User mainUser = getNBSessionUser();
		
		UserModel userModel = UserModel.instance(model);
		userModel.setCode(yzm);
		userModel.setMobilePhone(tel);
		
		String valid_tel_code_result = userModel.validBindTelCode();

		if(null==tel||"".equals(tel))
		{
			map.put(ConstantUtil.ERRORMSG,"手机号不合法");
		}
		else if(null==mainUser.getWechatOpenId()||"".equals(mainUser.getWechatOpenId()))
		{
			map.put(ConstantUtil.ERRORMSG,"noopenid");
		}
		else if(null==pwd||"".equals(pwd))
		{
			map.put(ConstantUtil.ERRORMSG,"密码不能为空");
		}
		else if("false".equals(bind_type))
		{
			if(!ConstantUtil.SUCCESS.equals(valid_tel_code_result))
			{
				map.put(ConstantUtil.ERRORMSG,valid_tel_code_result);
			}
			else
			{
					/**
					 * 新注册账号
					 */
					User newUser = new User();
					newUser.setUserName(tel);
					newUser.setPwd(MD5.encode(pwd));
					newUser.setAddTime(new Date());
					newUser.setBindTime(new Date());
					newUser.setMobilePhone(tel);
					newUser =  theUserService.saveUser(newUser);
					newUser.setBindId(newUser.getUserId());
					theUserService.updateUser(newUser);
					
					if(null!=mainUser)
					{
						mainUser.setBindId(newUser.getUserId());
						theUserService.updateUser(mainUser);
					}
					
					UserCache userCache = new UserCache();
					userCache.setUser(newUser);
					userCache.setAddIp(Global.getIP());
					userCache = theUserCacheService.saveWechatUserCache(userCache);
					
					Account account = new Account();
					account.setUser(newUser);
					theAccountService.save(account);
					
					UserIdentify userIdentify = new UserIdentify();
					userIdentify.setUser(newUser);
					theUserIdentifyService.saveWechatUserIdentify(userIdentify);
					
					UserPromot userPromot = new UserPromot();
					userPromot.setUser(newUser);
					theUserPromotService.saveWechatUserPromot(userPromot);
					
					UserVip userVip = new UserVip();
					userVip.setUser(newUser);
					theUserVipService.saveWechatUserVip(userVip);
					
					theUserBaseInfoService.saveUserBaseInfo(userCache);
					theUserCreditService.saveUserCredit(new UserCredit(newUser));
					theAccountSumService.saveAccountSum(new AccountSum(newUser));
					theScoreService.saveScore(new Score(newUser));
					
					/**
					 * 新注册账号且拥有邀请码
					 */
					UserPromot userPromot3 = null;
					
					if(!"".equals(couponCode)&&null!=couponCode)
					{
						userPromot3 = theUserPromotService.getUserPromotByCode(couponCode.toLowerCase());
					}
					
					System.out.println("invite code is:"+couponCode+", new user's id is"+newUser.getUserId());
					
					if(null!=userPromot3)
					{
						User invite_user = userPromot3.getUser();
						
						if(null!=invite_user)
						{
							UserInvite userInvite = new UserInvite();
							userInvite.setInviteTime(new Date());
							userInvite.setInviteUser(invite_user);
							userInvite.setUser(newUser);
							userInvite.setGift(false);
							theUserInviteService.saveUserInvite(userInvite);
							
							UserPromot userPromot2 = theUserPromotService.findUserPromotByUserId(invite_user.getUserId());
							userPromot2.setUsedTimes(userPromot2.getUsedTimes()+1);
							userPromot2.setCanUseTimes(userPromot2.getCanUseTimes()-1);
							
							theUserPromotService.updateUserPromot(userPromot2);
							
							Date date=new Date();
							Calendar calendar = new GregorianCalendar(); 
							calendar.setTime(date); 
							
							RedPacket redPacket = theRedPacketService.getByServiceType(ConstantUtil.RED_PACKET_RECOMMEND);
							
							calendar.add(calendar.DATE,redPacket.getDay());
							date=calendar.getTime();  
							
							if(1==redPacket.getStatus())
							{
								UserRedPacket userRedPacket = new UserRedPacket();
								userRedPacket.setAddTime(new Date());
								userRedPacket.setAmount(redPacket.getMoney());
								userRedPacket.setExpiredTime(date);
								userRedPacket.setRedPacketType(2);
								userRedPacket.setType(redPacket);
								userRedPacket.setUsed(false);
								userRedPacket.setUser(invite_user);
								
								userRedPacket =  theUserRedPacketService.saveUserRedPacket(userRedPacket);
								
								RecommendRecord recommendRecord = new RecommendRecord();
								recommendRecord.setAddTime(new Date());
								recommendRecord.setUserRedPacket(userRedPacket);
								recommendRecord.setAccount(userRedPacket.getAmount());
								recommendRecord.setInviteUser(invite_user);
								recommendRecord.setName(userRedPacket.getType().getServiceName());
								recommendRecord.setUser(newUser);
								
								recommendRecordService.saveRecommendRecord(recommendRecord);
								/*
								List<User> list = new ArrayList<User>();
								list = theUserService.getByGroupId(invite_user.getBindId());

								if (list.size() > 1)
								{
									for (int i = 0; i < list.size(); i++)
									{
										if (null != list.get(i).getWechatOpenId()
												&&!"".equals(list.get(i).getWechatOpenId())
												&&WechatData.A_APP_ID.equals(list.get(i).getWechatId()))
										{
											WechatMessage wechatMessage = new WechatMessage();
											
											wechatMessage.setAppId(WechatData.A_APP_ID);
											wechatMessage.setAppSecret(WechatData.A_APP_SECRET);
											wechatMessage.setType(WechatMessageData.Receive_A_Red_Envelope);
											wechatMessage.setFirstData("您的好友"+tel+"通过您的注册码注册，您获得一个推荐红包!");
											wechatMessage.setMoney(redPacket.getMoney());
											wechatMessage.setUrl("http://jinx-wechat.dsyplus.com/nb/wechat/account/redPacket.action");
											wechatMessage.setOpenId(list.get(i).getWechatOpenId());
											
											wechatCacheService.sendWechatMessage(wechatMessage);
										}
									}
								}
								*/
							}
							
						}
						
					}
					
					setAttr(ConstantUtil.SESSION_USER, newUser);
					setAttr(Constant.SESSION_USER, newUser);
					session.put(Constant.SESSION_USER, newUser);
					
					map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
					map.put(ConstantUtil.ERRORMSG,"恭喜您注册成功!");
					
				}
		}
		else
		{
			if(null!=user)
			{
				User change_user = theUserService.doLogin(tel,pwd);
				UserCache userCache = theUserCacheService.getById(user.getUserCache().getId());
				
				if(1==userCache.getStatus())
				{
					map.put(ConstantUtil.ERRORMSG,"您的账户已被锁定，请联系客服!");
				}
				else
				{
					if(null!=change_user)
					{
						if(null!=change_user.getRealName()&&null!=mainUser.getRealName())
						{
							if(!change_user.getRealName().equals(mainUser.getRealName()))
							{
								map.put(ConstantUtil.ERRORMSG,"账号身份不统一!");
							}
						}
						else
						{
							if(null!=mainUser)
							{
								mainUser.setBindId(change_user.getBindId());
								theUserService.updateUser(mainUser);
							}
							
							theUserPromotService.checkUserPromot(mainUser);
							
							userCache.setLoginFailTimes(0);
							userCache.setLockTime(new Date());
							theUserCacheService.updateUserCache(userCache);
							
							request.getSession().setAttribute(ConstantUtil.SESSION_USER,change_user);
							request.getSession().setAttribute(Constant.SESSION_USER,change_user);
							session.put(Constant.SESSION_USER,change_user);
							
							map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
							map.put(ConstantUtil.ERRORMSG,"恭喜您绑定成功!");
						}
					}
					else
					{
						if(3==userCache.getLoginFailTimes())
						{
							userCache.setLoginFailTimes(4);
							theUserCacheService.updateUserCache(userCache);
								
							map.put(ConstantUtil.ERRORMSG,"您的账号密码错误4次，再次输错账号将被锁定!");
						}
						else if(4==userCache.getLoginFailTimes())
						{
							userCache.setLoginFailTimes(5);
							userCache.setStatus(1);
							userCache.setLockTime(new Date());
							theUserCacheService.updateUserCache(userCache);
								
							map.put(ConstantUtil.ERRORMSG,"您的登录密码已被锁定，请联系客服!");
						}
						else
						{
							userCache.setLoginFailTimes(userCache.getLoginFailTimes()+1);
							theUserCacheService.updateUserCache(userCache);
							map.put(ConstantUtil.ERRORMSG,"账号密码错误!");
						}
						
					}
				}
			}
			else
			{
				map.put(ConstantUtil.ERRORMSG,"该账号不存在!");
			}
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	
	
	
	
	
	
	
	
	
	
	public static String getWechatUrl()
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(WechatData.OAUTH_URL_ONE);
		buffer.append(WechatData.OAUTH_URL_TWO);
		buffer.append("/nb/wechat/invite.action");
		buffer.append(WechatData.OAUTH_URL_FIVE);
		
		return buffer.toString();
	}
	public static boolean getTimestamp(int timestamp)
	{
		int currentTimestamp = (int) (System.currentTimeMillis() / 1000);
		
		if ((currentTimestamp - timestamp) >= 7000)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * getter & setter
	 * @return
	 */
	public Map<String, Object> getMap()
	{
		return map;
	}

	public void setMap(Map<String, Object> map)
	{
		this.map = map;
	}
}
