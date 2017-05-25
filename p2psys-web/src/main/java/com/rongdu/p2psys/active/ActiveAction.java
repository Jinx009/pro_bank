package com.rongdu.p2psys.active;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;
import com.rongdu.p2psys.crowdfunding.service.LeaderFactoryService;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatJSSign;
import com.rongdu.p2psys.util.WechatUtil;

/**
 * 各种h5活动页面
 * @author Jinx
 *
 */
@SuppressWarnings("rawtypes")
public class ActiveAction extends BaseAction{
	
	@Autowired
	private LeaderFactoryService leaderFactoryService;
	@Autowired
	private WechatCacheService wechatCacheService;
	
	
	private Map<String,Object> data;

	@Action(value = "/active/index",results = { @Result(name = "index", type = "ftl", location = "/nb/active/cf-index.htm")})
	public String index(){
		return "index";
	}
	
	@Action(value = "/active/wechat/index",results = { @Result(name = "index", type = "ftl", location = "/nb/active/cf-wechat-index.htm"),@Result(name = "pro", type = "ftl", location = "/nb/cf/wechat/pro/index.html")})
	public String wechatindex() throws ParseException{
		request.setAttribute("id",1);
		Date now = new Date();
		String myString = "2016-03-22 16:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = sdf.parse(myString);
		if(d.after(now)){
			return "index";
		}
		return "pro";
	}
	
	@Action(value = "/active/wechat/invite",results = { @Result(name = "invite", type = "ftl", location = "/nb/active/cf-wechat-invite.html")})
	public String wechatInvite(){
		return "invite";
	}
	
	
	@Action(value = "/active/wechat/info",results = { @Result(name = "info", type = "ftl", location = "/nb/active/cf-wechat-info.html")})
	public String info(){
		return "info";
	}
	
	@Action(value = "/active/wechat/result",results = { @Result(name = "result", type = "ftl", location = "/nb/active/cf-wechat-result.html")})
	public String result() throws ClientProtocolException, IOException{
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

		int r = paramInt("r");
		int id = paramInt("id");
		if(0==r){
			r = 1+new Random().nextInt(5);
			LeaderFactory leaderFactory = leaderFactoryService.find(id);
			leaderFactory.setResult(r);
			leaderFactoryService.doUpdate(leaderFactory);
		}
		request.setAttribute("r",r);
		return "result";
	}

	@Action(value = "/active/wechat/box",results = { @Result(name = "box", type = "ftl", location = "/nb/active/cf-wechat-box.html")})
	public String wechatBox() throws ClientProtocolException, IOException{
		request.setAttribute("timestamp", "");
		request.setAttribute("nonceStr", "");
		request.setAttribute("signature", "");
		String code = request.getParameter("code");
		request.setAttribute("nick","");
		if (null != code && !"".equals(code)) {
			JSONObject result = WechatUtil.getUserInfoFirst(code,WechatData.A_APP_ID, WechatData.A_APP_SECRET);
			if(null!=result){
				System.out.println("第一次微信授权获取信息："+result.toString());
				String accessToken = result.get("access_token").toString();
				String openid = result.get("openid").toString();
				JSONObject json = WechatUtil.getRealUserInfo(accessToken, openid);
				request.setAttribute("nick",getRealNickName(json.getString("nickname").toString()));
			}
		} 
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
	/*	
		https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxa37b227117def502&redirect_uri=http://www.800zf.cn/active/wechat/box.action&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect
		*/return "box";
	}
	
	public static String getRealNickName(String nick){
		StringBuilder nicksb = new StringBuilder();
		int l = nick.length();
		for (int i = 0; i < l; i++) {
			char _s = nick.charAt(i);
			if (isEmojiCharacter(_s)) {
				nicksb.append(_s);
			}
		}
		return nicksb.toString();
	}
	 
	@Action(value = "/active/wechat/bubble",results = { @Result(name = "bubble", type = "ftl", location = "/nb/active/cf-wechat-bubble.html")}) 
	public String bubble(){
		return "bubble";
	}
	
	@Action(value = "/active/swf",results = { @Result(name = "swf", type = "ftl", location = "/nb/active/cf-swf.html")}) 
	public String swf(){
		return "swf";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static boolean isEmojiCharacter(char codePoint){
	     return (codePoint==0x0)||(codePoint==0x9)||
	    		(codePoint==0xA)||(codePoint==0xD)||
	    		((codePoint>=0x20)&&(codePoint<= 0xD7FF))||
	    		((codePoint>=0xE000)&&(codePoint<= 0xFFFD))||
	    		((codePoint>=0x10000)&&(codePoint<=0x10FFFF));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
