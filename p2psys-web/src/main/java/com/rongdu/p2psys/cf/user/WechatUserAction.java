package com.rongdu.p2psys.cf.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.code.MD5;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.model.AttentionListModel;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;
import com.rongdu.p2psys.crowdfunding.service.AttentionListService;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.account.service.AccountService;
import com.rongdu.p2psys.nb.user.service.UserCacheService;
import com.rongdu.p2psys.nb.user.service.UserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.user.model.UserModel;
import com.rongdu.p2psys.util.HttpData;
import com.rongdu.p2psys.util.HttpUtil;

/**
 * 微信用户中心
 * @author Jinx
 *
 */
public class WechatUserAction extends BaseAction<UserModel> implements ModelDriven<UserModel>{

	@Resource
	private UserService theUserService;
	@Resource
	private AttentionListService attentionListService;
	@Resource
	private AccountService theAccountService;
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private OrderService cfOrderService;
	@Resource
	private UserCacheService theUserCacheService;
	
	
	private Map<String,Object>  data;

	/**
	 * 自动登录
	 * @throws IOException
	 */
	@Action(value="/cf/checkStatus")
	public void checkLoginStatus() throws IOException{
		data = new HashMap<String, Object>();
		String mobile = paramString("mobile");
		User user = theUserService.getByUserName(mobile);
		data.put(ConstantUtil.CODE,400);
		if(null!=user){
			setAttr(ConstantUtil.SESSION_USER,user);
			setSessionHideUserName(user);
			data.put(ConstantUtil.CODE,200);
		}
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 爱康国宾-- 页面
	 * @return
	 */
	@Action(value = "/ikang",results = { @Result(name = "index", type = "ftl", location = "/nb/cf/wechat/ikang.html")})
	public String iIndex(){
		return "index";
	}
	
	/**
	 * 用户中心  -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/index",results = { @Result(name = "index", type = "ftl", location = "/nb/cf/wechat/user/main.html")})
	public String userIndex(){
		return "index";
	}
	
	/**
	 * 设置交易密码-- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/set-pay",results = { @Result(name = "set-pay", type = "ftl", location = "/nb/cf/wechat/user/set-pay.html")})
	public String setPay(){
		request.setAttribute("url",paramString("redirectUrl"));
		return "set-pay";
	}
	
	/**
	 * 设置交易密码微信 --操作
	 * @throws IOException
	 */
	@Action(value="/cf/wechat/user/setPayPwd")
	public void setPayPwd() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.MSG,"设置交易密码相关");
		
		String pwd = paramString("pwd");
		User user = getNBSessionUser();
		user.setPayPwd(MD5.encode(pwd));
		setAttr(ConstantUtil.SESSION_USER,user);
		theUserService.updateUser(user);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 微信账户中心首页  --  数据
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/user/main")
	public void mainData() throws IOException{
		data = new HashMap<String, Object>();
		User user = getNBSessionUser();
		List<AttentionListModel> attentionList = attentionListService.getByUserId(user.getUserId());
		Account account = theAccountService.getAccountByUserId(user.getUserId());
		List<OrderModel> orderList = cfOrderService.getBuyList(user);
		Double investMoeny = cfOrderService.getInvestMoney(user.getUserId());
		
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put("account",account);
		//我投资过的项目
		if(null!=orderList&&!orderList.isEmpty()){
			data.put("order",orderList.size());
		}else{
			data.put("order",0);
		}
		//我关注的产品
		if(null!=attentionList&&!attentionList.isEmpty()){
			data.put("attention",attentionList.size());
		}else{
			data.put("attention",0);
		}
		//投资总金额
		data.put("money",investMoeny);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 我的钱包  --  页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/wallet",results = { @Result(name = "wallet", type = "ftl", location = "/nb/cf/wechat/user/wallet.html")})
	public String wallet(){
		return "wallet";
	}
	
	/**
	 * 我的钱包  -- 数据
	 * @throws IOException 
	 */
	@Action(value = "/cf/wechat/user/walletData")
	public void walletData() throws IOException{
		User user = getNBSessionUser();
		Account account = theAccountService.getAccountByUserId(user.getUserId());
		Double investMoeny = cfOrderService.getInvestMoney(user.getUserId());
		account.setNoUseMoney(investMoeny);
		account.setTotal(BigDecimalUtil.add(account.getUseMoney(),investMoeny));
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.MSG,"我的钱包数据");
		data.put(ConstantUtil.DATA,account);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 修改交易密码 -- 数据
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/changePayPwd")
	public void changePayPwd() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,400);
		data.put(ConstantUtil.MSG,"修改交易密码。");
		User user = getNBSessionUser();
		if(null!=user){
			String pwd = paramString("pwd");
			String newPwd = paramString("newPwd");
			String rePwd = paramString("rePwd");
			
			if(!MD5.encode(pwd).equals(user.getPayPwd())){
				data.put(ConstantUtil.DATA,"原始交易密码不正确！");
			}else if(!newPwd.equals(rePwd)){
				data.put(ConstantUtil.DATA,"确认密码不一致！");
			}else{
				user.setPayPwd(MD5.encode(newPwd));
				theUserService.updateUser(user);
				setAttr(ConstantUtil.SESSION_USER,user);
				data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
				data.put(ConstantUtil.DATA,"修改交易密码成功！");
			}
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 微信端修改密码  -- 数据
	 * @throws Exception
	 */
	@Action(value = "/cf/wechat/changePwd")
	public void changePwdData() throws Exception{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,400);
		data.put(ConstantUtil.MSG,"修改密码返回数据！");
		User user = getNBSessionUser();
		if(null != user){
			String pwd = paramString("pwd");
			String newPwd = paramString("newPwd");
			String rePwd = paramString("rePwd");
			
			if(!MD5.encode(pwd).equals(user.getPwd())){
				data.put(ConstantUtil.DATA,"原始密码不正确！");
			}else if(!rePwd.equals(newPwd)){
				data.put(ConstantUtil.DATA,"确认密码不一致！");
			}else{
				String postUrl = HttpData.changePwd(user.getUserName(),MD5.encode(pwd), MD5.encode(newPwd));
				String resultStr = HttpUtil.postParams(postUrl);
				 JSONObject jsonObject = JSONObject.fromObject(resultStr);
				 if (null != jsonObject.getString(ConstantUtil.RESULT)) {
         	  		String result = jsonObject.getString(ConstantUtil.RESULT);
					if (ConstantUtil.SUCCESS.equals(result)) {
						user.setPwd(MD5.encode(newPwd));
						theUserService.updateUser(user);
						setAttr(ConstantUtil.SESSION_USER,user);
						data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
						data.put(ConstantUtil.DATA,"修改成功！");
					}else{
						data.put(ConstantUtil.DATA,"远程服务器操作失败！");
					}
				 }else{
					 data.put(ConstantUtil.DATA,"远程服务器连接异常！");
				 }
			}
		}else{
			data.put(ConstantUtil.DATA,"用户未登录！");
		}
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 我的设置 --页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/setting",results = { @Result(name = "setting", type = "ftl", location = "/nb/cf/wechat/user/setting.html")})
	public String setting(){
		User user = getNBSessionUser();
		UserCache userCache = theUserCacheService.getByUserId(user.getUserId());
		int status = userCache.getInvestStatus();
		request.setAttribute("investor",status);
		return "setting";
	}
	
	/**
	 * 帮助中心 -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/help",results = { @Result(name = "help", type = "ftl", location = "/nb/cf/wechat/user/help.html")})
	public String help(){
		return "help";
	}
	
	/**
	 * 修改交易密码 -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/changePay",results = { @Result(name = "change", type = "ftl", location = "/nb/cf/wechat/user/change-pay.html")})
	public String changePay(){
		return "change";
	}
	
	/**
	 * 修改密码 -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/changePwd",results = { @Result(name = "change", type = "ftl", location = "/nb/cf/wechat/user/change-pwd.html")})
	public String changePwd(){
		return "change";
	}
	
	/**
	 * 微信注册 -- 操作
	 * @throws Exception
	 */
	@Action(value = "/cf/wechat/doRegister")
	public void doRegister() throws Exception{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,400);
		data.put(ConstantUtil.DATA,"提交信息失败！");
		String mobilePhone = paramString("mobilePhone");
		String pwd = paramString("pwd");
		String code = paramString("code");
		String inviteCode = paramString("inviteCode");
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
			data.put(ConstantUtil.DATA,"获取验证码手机与输入手机号不一致!");
		}
		else if(!ConstantUtil.SUCCESS.equals(validTelCodeResult)){
			data.put(ConstantUtil.DATA,validTelCodeResult);
		}
		else if(null!=testUser){
			data.put(ConstantUtil.DATA,"用户名已存在!");
		}else if(!ConstantUtil.SUCCESS.equals(result)){
			data.put(ConstantUtil.DATA,"用户名已在800Bank平台存在!");
		}
		else{
			User user = new User();
			user.setAddTime(new Date());
			user.setMobilePhone(mobilePhone);
			user.setPwd(MD5.encode(pwd));
			user.setUserName(mobilePhone);
			user = theUserService.savePcUser(user,"");
			user.setQqId(inviteCode);
			theUserService.updateUser(user);
			setAttr(ConstantUtil.SESSION_USER,user);
			setSessionHideUserName(user);
			
			String registerUrl = HttpData.registerUrl(mobilePhone,pwd);
			HttpUtil.postParams(registerUrl);
			
			data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
			data.put(ConstantUtil.ERRORMSG,"注册成功!");
			
		}
		printWebJson(getStringOfJpaObj(data));
	}

	@Action(value = "/cf/wechat/user/forget",results = { @Result(name = "forget", type = "ftl", location = "/nb/cf/wechat/forget.html")})
	public String forgetWechatPwd(){
		return "forget";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
