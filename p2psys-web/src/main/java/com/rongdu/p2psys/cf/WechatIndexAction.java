 package com.rongdu.p2psys.cf;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.model.ProjectBaseinfoModel;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.nb.homepage.domain.HomePageBanner;
import com.rongdu.p2psys.nb.homepage.service.HomePageBannerService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatJSSign;

/**
 * 微信首页
 * @author Jinx
 *
 */
public class WechatIndexAction extends BaseAction<ProjectBaseinfoModel> implements ModelDriven<ProjectBaseinfoModel> {

	private Map<String,Object> data;
	
	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private HomePageBannerService homePageBannerService; 
	@Resource
	private WechatCacheService wechatCacheService;
	
	
	/**
	 * 微信首页 -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/pro/index",results = { @Result(name = "index", type = "ftl", location = "/nb/cf/wechat/pro/index.html")})
	public String listPage(){
		Integer type = paramInt("id");
		request.setAttribute("id",type);
		return "index";
	}
	
	/**
	 * 微信登录页面 -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/login",results = {@Result(name="login",type="ftl",location="/nb/cf/wechat/login.html")})
	public String login(){
		request.setAttribute("redirectUrl",paramString("redirectUrl"));
		return "login";
	}

	/**
	 * 微信注册页面 -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/register",results = {@Result(name="register",type="ftl",location="/nb/cf/wechat/register.html")})
	public String register(){
		request.setAttribute("redirectUrl",paramString("redirectUrl"));
		return "register";
	}
	
	/**
	 * 合格投资人 -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/user/meet",results = {@Result(name="meet",type="ftl",location="/nb/cf/wechat/user/meet.html")})
	public String meet(){
		request.setAttribute("redirectUrl",paramString("redirectUrl"));
		return "meet";
	}
	
	/**
	 * 合格投资人 -- 数据
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/user/meetData")
	public void meetData() throws IOException{
		User user = getNBSessionUser();
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.MSG,"合格投资人数据");
		data.put(ConstantUtil.DATA,user.getUserCache().getInvestStatus());
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 发起梦想 -- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/dream",results = {@Result(name = "dream",type="ftl",location="/nb/cf/wechat/dream.html")})
	public String dream(){
		return "dream";
	}
	
	/**
	 * 帮助中心-- 页面
	 * @return
	 */
	@Action(value = "/cf/wechat/help",results = {@Result(name = "help",type="ftl",location="/nb/cf/wechat/help.html")})
	public String help(){
		return "help";
	}
	
	
	/**
	 * 获取微信首页图片 -- 数据
	 * 
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/banner")
	public void getIndexBanner() throws IOException {
		List<HomePageBanner> list = homePageBannerService.findAllWechat();

		data = new HashMap<String, Object>();
		data.put("data", list);

		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * forget page
	 * 
	 * @return
	 */
	@Action(value = "/cf/wechat/forget", results = { @Result(name = "forget", type = "ftl", location = "/nb/cf/wechat/forget.html") })
	public String forgetPwdPage() {
		request.setAttribute("redirectUrl", paramString("redirectUrl"));

		return "forget";
	}
	
	/**
	 * 注册成功
	 * 
	 * @return
	 */
	@Action(value = "/cf/wechat/success", results = { @Result(name = "success", type = "ftl", location = "/nb/cf/wechat/success.html") })
	public String registerSuccessPage() {
		request.setAttribute("redirectUrl", paramString("redirectUrl"));
		return "success";
	}
	
	/**
	 * 新手指南
	 * 
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Action(value = "/cf/wechat/newUser", results = { @Result(name = "success", type = "ftl", location = "/nb/cf/wechat/newUser.html") })
	public String newUser() throws ClientProtocolException, IOException {
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");

		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		String appId = WechatData.A_APP_ID;
		String appSecret = WechatData.A_APP_SECRET;

		if (null != queryString && !"".equals(queryString)) {
			url = url + "?" + queryString;
		}

		WechatCache wechatCache = wechatCacheService.getByAppId(appId, "JsApi");
		WechatJSSign wechatJSSign = new WechatJSSign();
		String jsapi_ticket = wechatJSSign.checkWechatCache(appId, appSecret,wechatCache);
		Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket, url,appId, appSecret);

		request.setAttribute("appId", appId);
		request.setAttribute("timestamp", ret.get("timestamp").toString());
		request.setAttribute("nonceStr", ret.get("nonceStr").toString());
		request.setAttribute("signature", ret.get("signature").toString());
		return "success";
	}
	
	
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
