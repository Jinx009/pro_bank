package com.rongdu.p2psys.cf.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.code.MD5;
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
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.user.service.UserInviteService;
import com.rongdu.p2psys.util.HttpData;
import com.rongdu.p2psys.util.HttpUtil;

public class UserAction extends BaseAction<UserModel> implements
		ModelDriven<UserModel> {
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

	private Map<String, Object> map;
	
	private User user;

	/**
	 * 设置交易密码页面
	 * 
	 * @return
	 */
	@Action(value="/cf/user/set-pay",results = { @Result(name = "set-pay-pwd", type = "ftl", location = "/nb/cf/user/set-pay-pwd.html")})
	public String settingPayPwd() {
		request.setAttribute("redirectUrl",paramString("redirectUrl"));
		
		return "set-pay-pwd";
	}
	
	/**
	 * 判断账号是否存在
	 * @throws Exception 
	 */
	@Action(value = "/cf/userExists")
	public void userExists() throws Exception{
		map = new HashMap<String, Object>();
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String mobilePhone = paramString("mobilePhone");
		User user = theUserService.getByUserName(mobilePhone);
		
		
		if(null!=user){
			map.put(ConstantUtil.ERRORMSG,"用户名已存在!");
		}else{
			String postUrl = HttpData.userExists(mobilePhone);
			String resultStr = HttpUtil.postParams(postUrl);
			JSONObject jsonObject = JSONObject.fromObject(resultStr);
			String result = jsonObject.getString(ConstantUtil.RESULT);
			if(!ConstantUtil.SUCCESS.equals(result)){
				map.put(ConstantUtil.ERRORMSG,"用户名已在800Bank平台存在!");
			}
			else{
				map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
				map.put(ConstantUtil.ERRORMSG,"验证成功!");
			}
		}
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	
	/**
	 * 成为合格投资人
	 * 
	 * @return
	 * @throws IOException 
	 */
	@Action(value="/cf/user/meet",results = { @Result(name = "meet", type = "ftl", location = "/nb/cf/user/meet.html")})
	public String meet() throws IOException {
		User user = getNBSessionUser();
		if(null!=user){
			UserCache userCache = user.getUserCache();
			request.setAttribute("redirectUrl","");
			request.setAttribute("invester",userCache.getInvestStatus());
		}
		return "meet";
	}
	
	/**
	 * 成为合格投资人
	 * 
	 * @return
	 * @throws IOException 
	 */
	@Action(value = "/cf/user/beInvester")
	public void doBeInvester() throws IOException {
		User user = getNBSessionUser();
		UserCache userCache = user.getUserCache();
		userCache.setInvestStatus(1);
		theUserCacheService.updateUserCache(userCache);
		user.setUserCache(userCache);
		
		setAttr(ConstantUtil.SESSION_USER,user);
		
		map = new HashMap<String, Object>();
		map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 设置交易密码页面
	 * 
	 * @return
	 * @throws IOException 
	 */
	@Action(value = "/cf/set/pay-pwd")
	public void doSettingPayPwd() throws IOException {
		map = new HashMap<String, Object>();
		map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);

		String pwd = paramString("pwd");
		String rePwd = paramString("rePwd");

		if (!model.isNumeric(pwd)) {
			map.put(ConstantUtil.ERRORMSG, "交易密码必须为6位数字!");
		}else if(!pwd.equals(rePwd)) {
			map.put(ConstantUtil.ERRORMSG, "两次交易密码不一致!");
		}else if(pwd.length() != 6) {
			map.put(ConstantUtil.ERRORMSG, "交易密码必须为6位数字!");
		}else {
			User user = getNBSessionUser();
			if(StringUtil.isNotBlank(user.getPayPwd())){
				map.put(ConstantUtil.ERRORMSG,"交易密码已设置!");
			}else{
				user.setPayPwd(MD5.encode(pwd));
				theUserService.updateUser(user);
				
				setAttr(ConstantUtil.SESSION_USER,user);
			}
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"设置成功!");
		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 登陆
	 * 
	 * @throws Exception
	 */
	@Action(value = "/cf/doLogin")
	public void doLogin() throws Exception {
		map = new HashMap<String, Object>();

		map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);

		String userName = paramString("mobilePhone");
		String pwd = paramString("pwd");

		User user = theUserService.getByUserName(userName);

		if (null!=user) {
			user = theUserService.doLogin(userName, pwd);
			if(null==user){
				map.put(ConstantUtil.ERRORMSG,"账号密码错误!");
			}else{
				map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
				map.put(ConstantUtil.ERRORMSG, "登陆成功!");

				setAttr(ConstantUtil.SESSION_USER, user);
				setSessionHideUserName(user);
			}
		} else {
			String postUrl = HttpData.getLoginUrl(userName, pwd);
			String resultStr = HttpUtil.postParams(postUrl);
			JSONObject jsonObject = JSONObject.fromObject(resultStr);

			if (null != jsonObject.getString(ConstantUtil.RESULT)) {
				String result = jsonObject.getString(ConstantUtil.RESULT);
				if (ConstantUtil.SUCCESS.equals(result)) {
					map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
					map.put(ConstantUtil.ERRORMSG, "登陆成功!");

					user = new User();
					user.setMobilePhone(userName);
					user.setUserName(userName);
					user.setPwd(MD5.encode(pwd));
					user.setWebsite(2);
					user.setAddTime(new Date());
					user = theUserService.savePcUser(user, "");

					setAttr(ConstantUtil.SESSION_USER, user);
					setSessionHideUserName(user);
					
				}else {
					map.put(ConstantUtil.ERRORMSG, "账号或密码错误!");
				}
			} else {
				map.put(ConstantUtil.ERRORMSG, "账号或密码错误!");
			}
		}
		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 注册
	 * @throws Exception
	 */
	@Action(value = "/cf/doRegister")
	public void doRegister() throws Exception{
		map = new HashMap<String, Object>();
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		map.put(ConstantUtil.ERRORMSG,"提交信息失败，请检查网络设置。");
		
		String mobilePhone = paramString("mobilePhone");
		String pwd = paramString("pwd");
		String code = paramString("code");
		String postUrl = HttpData.userExists(mobilePhone);
		String resultStr = HttpUtil.postParams(postUrl);
		JSONObject jsonObject = JSONObject.fromObject(resultStr);
		String result = jsonObject.getString(ConstantUtil.RESULT);
		
		UserModel userModel = UserModel.instance(model);
		userModel.setCode(code);
		userModel.setMobilePhone(mobilePhone);
		
		String validTelCodeResult = userModel.validCfRegisterTelCode();
		
		User testUser = theUserService.getByUserName(mobilePhone);
		
		if(!mobilePhone.equals(getSessionString("cfSessionRegisterMobilePhone"))){
			map.put(ConstantUtil.ERRORMSG,"获取验证码手机与输入手机号不一致!");
		}
		else if(!ConstantUtil.SUCCESS.equals(validTelCodeResult)){
			map.put(ConstantUtil.ERRORMSG,validTelCodeResult);
		}
		else if(null!=testUser){
			map.put(ConstantUtil.ERRORMSG,"用户名已存在!");
		}else if(!ConstantUtil.SUCCESS.equals(result)){
			map.put(ConstantUtil.ERRORMSG,"用户名已在800Bank平台存在!");
		}
		else{
			User user = new User();
			user.setAddTime(new Date());
			user.setMobilePhone(mobilePhone);
			user.setPwd(MD5.encode(pwd));
			user.setUserName(mobilePhone);
			theUserService.savePcUser(user,"");
			
			setAttr(ConstantUtil.SESSION_USER,user);
			setSessionHideUserName(user);
			
			String registerUrl = HttpData.registerUrl(mobilePhone,pwd);
			HttpUtil.postParams(registerUrl);
			
			map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
			map.put(ConstantUtil.ERRORMSG,"注册成功!");
			
		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * e
	 * 
	 * @throws IOException
	 */
	@Action(value="/app/center",results={ 
		@Result(name = "main",type="ftl", location = "/nb/pc/member/main.html"), 
		@Result(name = "index",type="ftl", location = "/nb/pc/index.html")
	})
	public String appDoLogin() throws IOException {
		String userName = paramString("x");
		String pwd = paramString("y");
		
		User user = theUserService.doLogin(userName, pwd);
		
		if(null!=user){
			return "main";
		}
		
		return "index";
	}
	
	/**
	 * 发送注册手机验证码
	 * @throws IOException 
	 */
	@Action(value = "/cf/registerCode")
	public void sendCfRegisterCode() throws IOException
	{
		map = new HashMap<String, Object>();
		String mobilePhone = paramString("mobilePhone");
		int code = (int)(Math.random()*899998+100000);
		System.out.println("code:"+code+"---"+mobilePhone);
		setAttr("cfSessionRegisterMobilePhone",mobilePhone);
		setAttr("cfRegisterCode",String.valueOf(code));
		Global.setTransfer("cf_code",String.valueOf(code));
		NoticeType smsType = Global.getNoticeType("cf_register", NoticeConstant.NOTICE_SMS);
		if(smsType.getSend() == 1 && StringUtil.isNotBlank(mobilePhone)) {
			Map<String, Object> sendData = new HashMap<String, Object>();
			sendData.put("addTime",new Date());
			sendData.put("cf_code",String.valueOf(code));
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
	 * 发送注册手机验证码
	 * @throws IOException 
	 */
	@Action(value = "/cf/forgetPwdCode")
	public void sendCfforget() throws IOException
	{
		map = new HashMap<String, Object>();
		String mobilePhone = paramString("mobilePhone");
		int code = (int)(Math.random()*899998+100000);
		System.out.println("code:"+code+"---"+mobilePhone);
		setAttr("cfSessionRegisterMobilePhone",mobilePhone);
		setAttr("cfRegisterCode",String.valueOf(code));
		if(StringUtil.isNotBlank(mobilePhone)) {
			Map<String, Object> sendData = new HashMap<String, Object>();
			sendData.put("addTime",new Date());
			sendData.put("cf_code",String.valueOf(code));
			sendData.put("realName",mobilePhone);
			Notice sms = new Notice();
			sms.setType(NoticeConstant.NOTICE_SMS + "");
			sms.setReceiveAddr(mobilePhone);
			sms.setNid("cf_register");
			sms.setContent("您的800众服验证码为"+String.valueOf(code)+"，您正在进行登录密码重设，若非本人操作，请致电：400-6366-800");
			noticeService.sendNotice(sms);
		}
		
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		printWebJson(getStringOfJpaObj(map));
	}

	
	/**
	 * 众筹注册
	 * 
	 * @throws IOException
	 */
	@Action(value = "/app/doRegister")
	public void appRegister() throws IOException {
		map = new HashMap<String, Object>();

		map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);

		String userName = paramString("userName");
		String pwd = paramString("pwd");

		if (StringUtil.isPhone(userName)) {
			User user = theUserService.getByUserName(userName);
			if (null != user) {
				map.put(ConstantUtil.ERRORMSG, "账号已存在!");
			} else {
				user = new User();
				user.setMobilePhone(userName);
				user.setUserName(userName);
				user.setPwd(MD5.encode(pwd));
				user.setAddTime(new Date());
				user = theUserService.savePcUser(user, "");
				map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
				map.put(ConstantUtil.ERRORMSG, "注册成功!");
			}
		} else {
			map.put(ConstantUtil.ERRORMSG, "手机号码格式不正确!");
		}

		printWebJson(getStringOfJpaObj(map));
	}

	/**
	 * 登出
	 * 
	 * @throws IOException
	 */
	@Action(value = "/cf/loginOut")
	public void loginOut() throws IOException {
		setAttr(ConstantUtil.SESSION_USER, null);
		setAttr(ConstantUtil.HIDE_SESSION_USERNAME, null);

		map = new HashMap<String, Object>();
		map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
		map.put(ConstantUtil.ERRORMSG, "退出账号成功!");

		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 获取我的资料
	 * @throws IOException 
	 */
	@Action(value="/cf/userInfor")
	public void loadInfor() throws IOException{
		user = getNBSessionUser();
		map = new HashMap<String, Object>();
		if(null != user){
			User  theUser = theUserService.getByUserId(user.getUserId());
			map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
			map.put("theUser", theUser);
		}else{
			map.put(ConstantUtil.RESULT, ConstantUtil.NO_LOGIN_USER);
			map.put(ConstantUtil.ERRORMSG, "请重新登录!");
		}

		printWebJson(getStringOfJpaObj(map));
	}
	
	/**
	 * 修改我的资料
	 * @throws IOException 
	 */
	@Action(value="/cf/set/infor")
	public void setInfor() throws IOException{
		user = getNBSessionUser();
		map = new HashMap<String, Object>();
		if(null != user){
			User  theUser = theUserService.getByUserId(model.getId());
			if(null != theUser){
				if(1==model.getFlag() && 1!=model.getPayFlag()){
					map = changePwd(model);
					if(map.get(ConstantUtil.RESULT).equals(ConstantUtil.SUCCESS)){
						map = modifyUserInfo(theUser);
					}
				}
				if(1==model.getPayFlag() && 1!=model.getFlag()){
					map = changePayPwd(model);
					if(map.get(ConstantUtil.RESULT).equals(ConstantUtil.SUCCESS)){
						map =modifyUserInfo(theUser);
					}
				}
				if(1!=model.getFlag() && 1!= model.getPayFlag()){
					map =modifyUserInfo(theUser);
				}
				
			}else{
				map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
				map.put(ConstantUtil.ERRORMSG, "数据加载出错,请重新刷新页面,或退出重新登录!");
			}
			
		}else{
			map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
			map.put(ConstantUtil.ERRORMSG, "请重新登录!");
		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	@Action(value="/cf/user/uploadHeadImge")
	public void uploadCardPositive(){
		user = getNBSessionUser();
		
		if(null != user){
			User theUser = theUserService.getByUserId(model.getUserId());
			UserCache userCache = theUser.getUserCache();  
			if(null != userCache){
				userCache.setCardPositive(model.getHeaderImg());
				theUserCacheService.saveUserCache(userCache);
			}else{
				userCache = theUserCacheService.getByUserId(model.getUserId());
				userCache.setCardPositive(model.getHeaderImg());
				theUserCacheService.saveUserCache(userCache);
			}
		}
	}

	//忘记密码
	@SuppressWarnings("unused")
	@Action(value="/cf/forgetpwd")
	public void modifyLoginPwd() throws Exception{
		map = new HashMap<String, Object>();
		map.put(ConstantUtil.RESULT,ConstantUtil.FAILURE);
		
		String mobilePhone = paramString("mobilePhone");
		String pwd = paramString("pwd");
		String code = paramString("code");
		String postUrl_ = HttpData.userExists(mobilePhone);
		String resultStr_ = HttpUtil.postParams(postUrl_);
		JSONObject jsonObject_ = JSONObject.fromObject(resultStr_);
		String result = jsonObject_.getString(ConstantUtil.RESULT);
		
		UserModel userModel = UserModel.instance(model);
		userModel.setCode(code);
		userModel.setMobilePhone(mobilePhone);
		
		String validTelCodeResult = userModel.validCfRegisterTelCode();
		User testUser = theUserService.getByUserName(mobilePhone);
		
		if(!mobilePhone.equals(getSessionString("cfSessionRegisterMobilePhone"))){
			map.put(ConstantUtil.ERRORMSG,"获取验证码手机与输入手机号不一致!");
		}else if(!ConstantUtil.SUCCESS.equals(validTelCodeResult)){
			map.put(ConstantUtil.ERRORMSG,validTelCodeResult);
		}else{
			if(null!= pwd || ""!=pwd){
				if(pwd.length() >7 && pwd.length() <=32){
					String postUrl = HttpData.forgetPwd(mobilePhone,MD5.encode(pwd));
					String resultStr = HttpUtil.postParams(postUrl);
					JSONObject jsonObject = JSONObject.fromObject(resultStr);
					if(null != testUser){
						testUser.setPwd(MD5.encode(pwd));
						theUserService.updateUser(testUser);
						map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
						map.put(ConstantUtil.ERRORMSG,"修改成功！");
						setAttr(ConstantUtil.SESSION_USER,null);
						setAttr(ConstantUtil.HIDE_SESSION_USERNAME,null);
					}else{
						if(!ConstantUtil.SUCCESS.equals(result)){
							User user = new User();
							user.setAddTime(new Date());
							user.setMobilePhone(mobilePhone);
							user.setPwd(MD5.encode(pwd));
							user.setUserName(mobilePhone);
							theUserService.savePcUser(user,"");
							map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
							map.put(ConstantUtil.ERRORMSG,"操作成功！");
							setAttr(ConstantUtil.SESSION_USER,null);
							setAttr(ConstantUtil.HIDE_SESSION_USERNAME,null);
						}else{
							map.put(ConstantUtil.ERRORMSG,"账号未在800Bank或800众服注册！");
						}
					}
				}else{
					map.put(ConstantUtil.ERRORMSG, "新密码位数不能小于7位数!");
				}
			}else{
				map.put(ConstantUtil.ERRORMSG, "新密码不能为空!");
			}
		}
		printWebJson(getStringOfJpaObj(map));
	}
	
	public Map<String, Object> changePwd(UserModel model) throws IOException{
		map = new HashMap<String, Object>();
		if(null != user){
			User  theUser = theUserService.getByUserId(model.getId());
			if(null != theUser){
				String oldPwd = MD5.encode(theUser.getPwd());
				if(theUser.getPwd().equals(MD5.encode(model.getPwd()))){
					if(model.getNewPwd().equals(model.getConfirmNewPwd())){
						theUser.setPwd(MD5.encode(model.getNewPwd()));
						theUserService.updateUser(theUser);
						try {
							String postUrl = HttpData.changePwd(theUser.getUserName(),MD5.encode(model.getPwd()), MD5.encode(model.getNewPwd()));
							String resultStr = HttpUtil.postParams(postUrl);
							 JSONObject jsonObject = JSONObject.fromObject(resultStr);
                              if (null != jsonObject.getString(ConstantUtil.RESULT)) {
                            	  		String result = jsonObject.getString(ConstantUtil.RESULT);
									if (ConstantUtil.SUCCESS.equals(result)) {
										map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
										map.put(ConstantUtil.MSG, "保存成功");
										setAttr(ConstantUtil.SESSION_USER, null);
										setAttr(ConstantUtil.HIDE_SESSION_USERNAME, null);
									}else{
										String errormsg = jsonObject.getString(ConstantUtil.ERRORMSG);
										map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
										map.put(ConstantUtil.ERRORMSG,errormsg);
										//执行手动撤回
										theUser.setPwd(oldPwd);
										theUserService.updateUser(theUser);
									}
                              }
									
						} catch (Exception e) {
							//执行手动撤回
							theUser.setPwd(oldPwd);
							theUserService.updateUser(theUser);
						}
					}else{
						map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
						map.put(ConstantUtil.ERRORMSG, "两次输入登陆密码不一致");
					}
				}else{
					map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
					map.put(ConstantUtil.ERRORMSG, "原始登陆密码错误");
				}
			}else{
				map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
				map.put(ConstantUtil.ERRORMSG, "数据加载出错,请重新刷新页面,或退出重新登录!");
			}
		}
//		printWebJson(getStringOfJpaObj(map));
		return map;
	}
	
	public Map<String, Object> changePayPwd(UserModel model) throws IOException{
		map = new HashMap<String, Object>();
		if(null != user){
			User  theUser = theUserService.getByUserId(model.getId());
			if(null != theUser){
				if(theUser.getPayPwd().equals(MD5.encode(model.getPayPwd()))){
					if(model.getNewPayPwd().equals(model.getConfirmNewPayPwd())){
						theUser.setPayPwd(MD5.encode(model.getNewPayPwd()));
						theUserService.updateUser(theUser);
						map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
						map.put(ConstantUtil.MSG, "保存成功");
						setAttr(ConstantUtil.SESSION_USER, theUser);
					}else{
						map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
						map.put(ConstantUtil.ERRORMSG, "两次输入支付密码不一致");
					}
				}else{
					map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
					map.put(ConstantUtil.ERRORMSG, "原始支付密码错误");
				}
			}else{
				map.put(ConstantUtil.RESULT, ConstantUtil.FAILURE);
				map.put(ConstantUtil.ERRORMSG, "数据加载出错,请重新刷新页面,或退出重新登录!");
			}
		}
//		printWebJson(getStringOfJpaObj(map));
		return map;
	}
	
	//基本信息修改
	public Map<String, Object> modifyUserInfo(User  theUser){
		theUser.setEmail(model.getEmail());
		theUser.setMobilePhone(model.getMobilePhone());
		theUser.setRealName(model.getRealName());
		theUserService.saveUser(theUser);
		UserCache useCache = theUser.getUserCache();
		useCache.setNickName(model.getNickName());
		useCache.setBirthday(model.getBirthday());
		useCache.setSex(model.getSex());
		theUserCacheService.saveUserCache(useCache);
		map.put(ConstantUtil.RESULT, ConstantUtil.SUCCESS);
		map.put(ConstantUtil.MSG, "保存成功");
		return map;
	}

	/**
	 * getter & setter
	 * 
	 * @return
	 */
	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public static void main(String[] args) {
		System.out.println(MD5.encode("a1111111"));
	}
}
