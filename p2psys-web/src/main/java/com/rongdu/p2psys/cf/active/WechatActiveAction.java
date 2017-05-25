package com.rongdu.p2psys.cf.active;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.crowdfunding.domain.WechatActiveFactory;
import com.rongdu.p2psys.crowdfunding.domain.WechatUser;
import com.rongdu.p2psys.crowdfunding.service.WechatActiveFactoryService;
import com.rongdu.p2psys.crowdfunding.service.WechatUserService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatJSSign;
import com.rongdu.p2psys.util.WechatUtil;

/**
 * 微信推广活动
 * @author Jinx
 *
 */
public class WechatActiveAction extends BaseAction<WechatActiveFactory> implements ModelDriven<WechatActiveFactory>{

	private Map<String,Object> data;
	
	@Resource
	private WechatUserService wechatUserService;
	@Resource
	private WechatActiveFactoryService wechatActiveFactoryService;
	@Resource
	private WechatCacheService wechatCacheService;
	
	
	/**
	 * 微信推广首页 -- 页面
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/active",results = { @Result(name = "active", type = "ftl", location = "/nb/cf/wechat/active/index.html")})
	public String projectDtail() throws ClientProtocolException, IOException{
		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		String appId = WechatData.A_APP_ID;
		String appSecret = WechatData.A_APP_SECRET;

		if (null != queryString && !"".equals(queryString)){
			url = url + "?" + queryString;
		}

		WechatCache wechatCache = wechatCacheService.getByAppId(appId, "JsApi");
		WechatJSSign wechatJSSign = new WechatJSSign();
		String jsapi_ticket = wechatJSSign.checkWechatCache(appId, appSecret,wechatCache);
		Map<String, String> ret = WechatJSSign.createSign(jsapi_ticket,url,appId, appSecret);

		request.setAttribute("appId",appId);
		request.setAttribute("timestamp",getString(ret.get("timestamp").toString()));
		request.setAttribute("nonceStr",getString(ret.get("nonceStr").toString()));
		request.setAttribute("signature",getString(ret.get("signature").toString()));
		
		String code = paramString("code");
		String openid = WechatUtil.getOpenid(code, WechatData.A_APP_ID,WechatData.A_APP_SECRET);
		WechatUser wechatUser = wechatUserService.getByOpenIdAndAppId(openid, WechatData.A_APP_ID);
		request.setAttribute("times",0);
		request.setAttribute("money","");
		if(null!=wechatUser){
			request.setAttribute("wechatId",wechatUser.getId());
			WechatActiveFactory wechatActiveFactory = wechatActiveFactoryService.getByWechatId(wechatUser.getId());
			if(null!=wechatActiveFactory){
				request.setAttribute("times",1);
				request.setAttribute("money",wechatActiveFactory.getMoney());
			}
		}else{
			WechatUser wechatUser2 = new WechatUser();
			wechatUser2.setAddTime(new Date());
			wechatUser2.setLoginTime(new Date());
			wechatUser2.setAppId(WechatData.A_APP_ID);
			wechatUser2.setOpenid(openid);
			wechatUser2.setUser(null);
			wechatUser2 = wechatUserService.save(wechatUser2);
			request.setAttribute("wechatId",wechatUser2.getId());
		}
		
		return "active";
	}
	
	/**
	 * 微信转发获取800币
	 * @throws IOException
	 */
	public void saveActive() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		
		Integer wechatId = paramInt("wechatId");
		List<WechatActiveFactory> list = wechatActiveFactoryService.findAll();
		WechatActiveFactory wechatActiveFactory = new WechatActiveFactory();
		wechatActiveFactory.setActiveTimes(0);
		wechatActiveFactory.setUpdateTime(new Date());
		wechatActiveFactory.setWechatId(wechatId);
		if(null!=list){
			if(list.size()<4){
				wechatActiveFactory.setMoney(8000.00);
			}else if(list.size()>=4&&list.size()<11){
				wechatActiveFactory.setMoney(5000.00);
			}else if(list.size()>=11&&list.size()<21){
				wechatActiveFactory.setMoney(2000.00);
			}else{
				wechatActiveFactory.setMoney(0.00);
			}
		}
		else{
			wechatActiveFactory.setMoney(8000.00);
		}
		wechatActiveFactoryService.save(wechatActiveFactory);
		
		data.put(ConstantUtil.DATA,"转发成功！");
		data.put(ConstantUtil.MSG,"微信推广相关！");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 处理字符串
	 * @param obj
	 * @return
	 */
	private String getString(Object obj){
		if(null==obj){
			return "";
		}else{
			String str = obj.toString();
			if(null!=str&&!"".equals(str)){
				return str;
			}else{
				return "";
			}
		}
	}
	
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
