package com.rongdu.p2psys.pc.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.model.accountlog.BaseAccountLog;
import com.rongdu.p2psys.account.model.accountlog.noac.GetCodeLog;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserPromotService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.util.StringUtil;
import com.rongdu.p2psys.pc.util.ShortUrlUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.domain.UserPromot;
import com.rongdu.p2psys.user.model.UserInviteModel;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserInviteService;
import com.rongdu.p2psys.util.HttpData;
import com.rongdu.p2psys.util.HttpUtil;
import com.rongdu.p2psys.util.WechatData;

public class UserAction extends BaseAction<UserModel> implements ModelDriven<UserModel>
{
	@Resource
	private UserService theUserService;
	@Resource
	private UserCacheService theUserCacheService;
	@Resource
	private UserInviteService userInviteService;
	@Resource
	private UserPromotService theUserPromotService;
	@Resource
	private NoticeService noticeService;
	
	private Map<String,Object> map;
	
	/**
	 * session共享
	 */
	@Action(value = "/app/setSession")
	public void setSession(){
		String userName = paramString("userName");
		User user = theUserService.getByUserName(userName);
		if(null!=user){
			setAttr(ConstantUtil.SESSION_USER,user);
		}
	}
	
	
	/**
	 * 邀请好友
	 * @return
	 */
	@Action(value = "/nb/pc/member/userInvite")
	public String userInvite()
	{
		return "userInvite";
	}
	
	/**
	 * 判断账号是否存在
	 * @throws IOException 
	 */
	@Action(value = "/nb/pc/userExists")
	public void userExists() throws IOException
	{
		map = new HashMap<String, Object>();
		String mobilePhone = paramString("mobilePhone");
		
		User user = theUserService.getByUserName(mobilePhone);
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		if(null!=user)
		{
			map.put(ConstantUtil.ERRORMSG,"号码已存在!");
		}
		else
		{
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"验证成功!");
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 登陆
	 * @throws IOException 
	 */
	@Action(value = "/app/doLogin" )
	public void appDoLogin() throws IOException
	{
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String userName = paramString("userName");
		String pwd = paramString("pwd");
		
		User user = theUserService.getByUserName(userName);
		
		if(null!=user)
		{
			user = theUserService.doLogin(userName, pwd);
			
			if(null!=user)
			{
				map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
				map.put(ConstantUtil.ERRORMSG,"登陆成功!");
				setAttr(ConstantUtil.SESSION_USER,user);
			}
			else
			{
				map.put(ConstantUtil.ERRORMSG,"密码错误!");
			}
		}
		else
		{
			map.put(ConstantUtil.ERRORMSG,"账号不存在!");
		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 众筹注册
	 * @throws IOException 
	 */
	@Action(value = "/app/doRegister" )
	public void appRegister() throws IOException{
		map  = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String userName = paramString("userName");
		String pwd = paramString("pwd");
		
		if(StringUtil.isPhone(userName)){
			User user = theUserService.getByUserName(userName);
			if(null!=user){
				map.put(ConstantUtil.ERRORMSG,"账号已存在!");
			}else{
				user = new User();
				user.setMobilePhone(userName);
				user.setUserName(userName);
				user.setPwd(MD5.encode(pwd));
				user.setAddTime(new Date());
				user = theUserService.savePcUser(user,"");
				
				map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
				map.put(ConstantUtil.ERRORMSG,"注册成功!");
			}
		}else{
			map.put(ConstantUtil.ERRORMSG,"手机号码格式不正确!");
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 邀请好友二维码
	 * @return
	 * @throws IOException 
	 */
	@Action(value = "/nb/QRcode")
	public void qrcode() throws IOException
	{
		map = new HashMap<String, Object>();
		
		if(hasSessionUser())
		{
			User user = getNBSessionUser();
			
			UserPromot userPromot = theUserPromotService.findUserPromotByUserId(user.getUserId());
			
			String couponCode = "";
			
			if(null!=userPromot)
			{
				couponCode = userPromot.getCouponCode();
			}
			
			String longUrl = WechatData.SHARE_URL_ONE+userPromot.getCouponCode()+WechatData.SHARE_URL_TWO;
			
			String shortUrlJson = ShortUrlUtil.getShortUrl(longUrl);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.DATA,shortUrlJson);
			map.put("counponCode",couponCode);
		}
		else
		{
			map = getErrorMap();
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 推荐好友数据
	 * @throws IOException 
	 */
	@Action(value = "/nb/userInviteList")
	public void userInviteList() throws IOException
	{
		map = new HashMap<String, Object>();
		
		if(hasSessionUser())
		{
			User user = getNBSessionUser();
			UserPromot userPromot = theUserPromotService.findUserPromotByUserId(user.getUserId());
			
			PageDataList<UserInviteModel> list =  userInviteService.findByInviteUser(user,paramInt("page"));
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.DATA,list);
			map.put(ConstantUtil.ERRORMSG,userPromot.getCouponCode());
		}
		else
		{
			map = getErrorMap();
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	
	/**
	 * 注册
	 * @throws IOException
	 */
	@Action(value = "/nb/doRegister" )
	public void doRegister() throws IOException
	{
		map  = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String mobilePhone = paramString("username");
		String pwd = paramString("pwd");
		UserModel userModel = UserModel.instance(model);
		userModel.setCode(paramString("code"));
		userModel.setMobilePhone(mobilePhone);
		
		String validTelCodeResult = userModel.validCfRegisterTelCode();
		
		
		User testUser = theUserService.getByUserName(mobilePhone);
		
		if(!mobilePhone.equals(getSessionString(ConstantUtil.SESSION_MOBILE_PHONE)))
		{
			map.put(ConstantUtil.ERRORMSG,"获取验证码手机与输入手机号不一致!");
		}
		else if(!ConstantUtil.SUCCESS.equals(validTelCodeResult))
		{
			map.put(ConstantUtil.ERRORMSG,validTelCodeResult);
		}
		else if(null!=testUser)
		{
			map.put(ConstantUtil.ERRORMSG,"账号已存在!");
		}
		else
		{
			User user = new User();
			user.setAddTime(new Date());
			user.setMobilePhone(mobilePhone);
			user.setPwd(MD5.encode(pwd));
			user.setUserName(mobilePhone);
			theUserService.savePcUser(user,"");
			
			setAttr(ConstantUtil.SESSION_USER,user);
			setSessionHideUserName(user);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"注册成功!");
			
		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 登陆
	 * @throws Exception 
	 */
	@Action(value = "/nb/doLogin" )
	public void doLogin() throws Exception
	{
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String userName = paramString("mobilePhone");
		String pwd = paramString("pwd");
		
		User user = theUserService.getByUserName(userName);
		
		if(null!=user)
		{
			user = theUserService.doLogin(userName, pwd);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"登陆成功!");
			
			setAttr(ConstantUtil.SESSION_USER,user);
			setSessionHideUserName(user);
		}
		else
		{
			String postUrl = HttpData.HOST_URL_800BANK_TEST+"app/doLogin.html?userName="+userName+"&pwd="+pwd;
			String resultStr = HttpUtil.postParams(postUrl);
			JSONObject jsonObject = JSONObject.fromObject(resultStr);
			
			if(null!=jsonObject.getString("result")){
				String result = jsonObject.getString("result");
				if(ConstantUtil.SUCCESS.equals(result)){
					map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
					map.put(ConstantUtil.ERRORMSG,"登陆成功!");
					
					user = new User();
					user.setMobilePhone(userName);
					user.setUserName(userName);
					user.setPwd(MD5.encode(pwd));
					user.setWebsite(2);
					user.setAddTime(new Date());
					user =  theUserService.savePcUser(user,"");
					
					setAttr(ConstantUtil.SESSION_USER,user);
					setSessionHideUserName(user);
				}
			}else{
				map.put(ConstantUtil.ERRORMSG,"账号或密码错误!");
			}
		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 登出
	 * @throws IOException
	 */
	@Action(value = "/nb/loginOut" )
	public void loginOut() throws IOException
	{
		map = new HashMap<String, Object>();
		
		setAttr(ConstantUtil.SESSION_USER, null);
		setAttr(ConstantUtil.HIDE_SESSION_USERNAME,null);
		
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		map.put(ConstantUtil.ERRORMSG,"退出账号成功!");
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 执行修改交易密码
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/doChangePayPwd" )
	public void doChangePayPwd() throws IOException
	{
		User user = getNBSessionUser();
		
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String pwd = paramString("pwd");
		String newPwd = paramString("newPwd");
		
		if(!MD5.encode(pwd).equals(user.getPayPwd()))
		{
			map.put(ConstantUtil.ERRORMSG,"原始交易密码不正确!");
		}
		else if(pwd.equals(newPwd))
		{
			map.put(ConstantUtil.ERRORMSG,"新交易密码不能与旧密码相同!");
		}
		else
		{
			user.setPayPwd(MD5.encode(newPwd));
			theUserService.updateUser(user);
			
			UserCache userCache = user.getUserCache();
			userCache.setPayFailTimes(0);
			theUserCacheService.updateUserCache(userCache);
			
			setAttr(ConstantUtil.SESSION_USER,user);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"修改成功!");

		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 执行设置交易密码
	 * @return
	 * @throws IOException
	 */
	@Action(value = "/nb/settingPayPwd" )
	public void settingPayPwd() throws IOException
	{
		User user = getNBSessionUser();
		
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String payPwd = paramString("payPwd");
		String rePayPwd = paramString("rePayPwd");
		
		if(!payPwd.equals(rePayPwd))
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
			
			UserCache userCache = user.getUserCache();
			userCache.setPayFailTimes(0);
			theUserCacheService.updateUserCache(userCache);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"设置成功!");
		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 执行校验支付密码
	 * @throws IOException 
	 */
	@Action(value = "/nb/checkPayPwd" )
	public void executecheckPayPwd() throws IOException
	{
		map = new HashMap<String, Object>();
		
		if(hasSessionUser())
		{
			map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
			
			String key = paramString("key");
			
			User user = getNBSessionUser();
			UserCache userCache = theUserCacheService.getById(user.getUserCache().getId());
			
			if(0==userCache.getPayPwdStatus())
			{
				if(MD5.encode(key).equals(user.getPayPwd()))
				{
					userCache.setPayFailTimes(0);
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
						
						map.put(ConstantUtil.ERRORMSG,"交易输入错误4次，再次输错交易密码将会被锁定!");
						map.put(ConstantUtil.ERROR_CODE,4);
					}
					else if(4==userCache.getPayFailTimes())
					{
						userCache.setPayFailTimes(5);
						userCache.setPayPwdStatus(1);
						theUserCacheService.updateUserCache(userCache);
						
						map.put(ConstantUtil.ERRORMSG,"您的交易密码已被锁定，请联系客服!");
						map.put(ConstantUtil.ERROR_CODE,5);
					}
					else
					{
						userCache.setPayFailTimes(userCache.getPayFailTimes()+1);
						theUserCacheService.updateUserCache(userCache);
						
						map.put(ConstantUtil.ERRORMSG,"您的交易密码输错"+userCache.getPayFailTimes()+"次，连续输错5次交易密码将会锁定!");
						map.put(ConstantUtil.ERROR_CODE,userCache.getPayFailTimes());
					}
				}
			}
			else
			{
				map.put(ConstantUtil.ERRORMSG,"您的交易密码已被锁定，请联系客服!");
				map.put(ConstantUtil.ERROR_CODE, 5);
			}
		}
		else
		{
			map = getErrorMap();
		}
		printWebJson(getStringOfJpaObj(map));
	}
	/**
	 * 发送注册手机验证码
	 * @throws IOException 
	 */
	@Action(value = "/nb/registerCode")
	public void sendRegisterCode() throws IOException
	{
		map = new HashMap<String, Object>();
		String mobilePhone = paramString("mobilePhone");
		setAttr(ConstantUtil.SESSION_MOBILE_PHONE,mobilePhone);
		
		BaseAccountLog blog = new GetCodeLog(null,"",null,mobilePhone,NoticeConstant.REGISTER_GET_CODE);
		blog.initCode("registerGetCode");
		blog.doEvent();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 发送注册手机验证码
	 * @throws IOException 
	 */
	@Action(value = "/cf/user/registerCode")
	public void sendCfRegisterCode() throws IOException
	{
		map = new HashMap<String, Object>();
		String mobilePhone = paramString("username");
		int code = (int)(Math.random()*899998+100000);
		
		setAttr("cfSessionRegisterMobilePhone",mobilePhone);
		setAttr("cfRegisterCode",code);
		
		Global.setTransfer("cf_code",code);
		NoticeType smsType = Global.getNoticeType("cf_register", NoticeConstant.NOTICE_SMS);
		if(smsType.getSend() == 1 && StringUtil.isNotBlank(mobilePhone)) {
			Map<String, Object> sendData = new HashMap<String, Object>();
			sendData.put("addTime",new Date());
			sendData.put("cf_code",code);
			sendData.put("realName",mobilePhone);
			Notice sms = new Notice();
			sms.setType(NoticeConstant.NOTICE_SMS + "");
			sms.setReceiveAddr(mobilePhone);
			sms.setNid("cf_register");
			sms.setContent(StringUtil.fillTemplet(smsType.getTemplet(), sendData));
			noticeService.sendNotice(sms);
		}
		
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 校验忘记密码基础信息
	 * @throws IOException
	 */
	@Action(value = "/nb/checkForgetMsg")
	public void checkForgetMsg() throws IOException
	{
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		model.setValidCode(paramString("code"));
		model.setMobilePhone(paramString("mobilePhone"));
		model.setCode(paramString("mobileCode"));

		int codeCheckResult = model.validWechatRegRule();
		String mobileCodeResult = model.validMobileCode("fogetMobileCode_code");
		
		if(codeCheckResult>=0)
		{
			map.put(ConstantUtil.ERRORMSG,"验证码错误,点击验证码图片刷新验证码!");
		}
		else if(!"success".equals(mobileCodeResult))
		{
			map.put(ConstantUtil.ERRORMSG,mobileCodeResult);
		}
		else if(!paramString("mobilePhone").equals(getSessionString(ConstantUtil.SESSION_MOBILE_PHONE)))
		{
			map.put(ConstantUtil.ERRORMSG,"手机号码与接收验证码手机号码不一致!");
		}
		else
		{
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"校验成功!");
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 校验忘记密码基础信息
	 * @throws IOException
	 */
	@Action(value = "/nb/checkPayPwdForgetMsg")
	public void checkPayPwdForgetMsg() throws IOException
	{
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		model.setMobilePhone(paramString("mobilePhone"));
		model.setCode(paramString("mobileCode"));

		String mobileCodeResult = model.validMobileCode("fogetMobileCode_code");
		
		User user = getNBSessionUser();
		
		if(null==user)
		{
			map.put(ConstantUtil.ERRORMSG,"您还未登陆!");
		}
		else if(null!=user&&!user.getUserName().equals(paramString("mobilePhone")))
		{
			map.put(ConstantUtil.ERRORMSG,"您输入的手机号码不正确!");
		}
		else if(!"success".equals(mobileCodeResult))
		{
			map.put(ConstantUtil.ERRORMSG,mobileCodeResult);
		}
		else if(!paramString("mobilePhone").equals(getSessionString(ConstantUtil.SESSION_MOBILE_PHONE)))
		{
			map.put(ConstantUtil.ERRORMSG,"手机号码与接收验证码手机号码不一致!");
		}
		else
		{
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"校验成功!");
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 修改密码
	 * @throws IOException
	 */
	@Action(value = "/nb/changePwd")
	public void changePwd() throws IOException
	{
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String pwd = paramString("pwd");
		
		User user = theUserService.getByUserName(getSessionString(ConstantUtil.SESSION_MOBILE_PHONE));
		
		if(null!=user)
		{
			user.setPwd(MD5.encode(pwd));
			theUserService.updateUser(user);
			
			UserCache userCache = user.getUserCache();
			userCache.setLoginFailTimes(0);
			theUserCacheService.updateUserCache(userCache);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,user.getUserName().substring(0, 3)+"****"+ user.getUserName().substring(7, user.getUserName().length()));
		}
		else
		{
			map.put(ConstantUtil.ERRORMSG,"该手机非注册账户!");
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 发送忘记密码手机验证码
	 * @throws IOException 
	 */
	@Action(value = "/nb/forgetPwdCode")
	public void sendForgetPwdCode() throws IOException
	{
		map = new HashMap<String, Object>();
		String tel = paramString("mobilePhone");
		setAttr(ConstantUtil.SESSION_MOBILE_PHONE,tel);
		
		String todo = "get_pwd_phone";
		
		User user = new User();
		user = theUserService.getByUserName(tel);
		
		if(null!=user)
		{
			BaseAccountLog blog = new GetCodeLog(user, user.getUserName(), todo);
			blog.initCode("fogetMobileCode");
			blog.doEvent();
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"发送成功!");
		}
		else
		{
			map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
			map.put(ConstantUtil.ERRORMSG,"用户不存在");
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 获取登陆信息
	 * @throws IOException 
	 */
	@Action(value = "/nb/sessionUser")
	public void getHideSessionUser() throws IOException
	{
		map = new HashMap<String, Object>();
		
		map.put(ConstantUtil.RESULT,getSessionString(ConstantUtil.HIDE_SESSION_USERNAME));
		
		printWebJson(getStringOfJpaObj(map));
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
