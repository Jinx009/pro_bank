package com.rongdu.p2psys.cf.project;

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
import com.rongdu.p2psys.crowdfunding.domain.AttentionList;
import com.rongdu.p2psys.crowdfunding.domain.Leader;
import com.rongdu.p2psys.crowdfunding.domain.Materials;
import com.rongdu.p2psys.crowdfunding.domain.ProjectBaseinfo;
import com.rongdu.p2psys.crowdfunding.domain.VirtualAccount;
import com.rongdu.p2psys.crowdfunding.model.AttentionListModel;
import com.rongdu.p2psys.crowdfunding.model.OrderModel;
import com.rongdu.p2psys.crowdfunding.model.ProjectBaseinfoModel;
import com.rongdu.p2psys.crowdfunding.service.AttentionListService;
import com.rongdu.p2psys.crowdfunding.service.LeaderService;
import com.rongdu.p2psys.crowdfunding.service.MaterialsService;
import com.rongdu.p2psys.crowdfunding.service.OrderService;
import com.rongdu.p2psys.crowdfunding.service.ProjectBaseinfoService;
import com.rongdu.p2psys.crowdfunding.service.VirtualAccountService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.nb.wechat.domain.WechatCache;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserCache;
import com.rongdu.p2psys.util.WechatData;
import com.rongdu.p2psys.util.WechatJSSign;

/**
 * 微信产品问题
 * @author Jinx
 *
 */
public class WechatProjectAction extends BaseAction<ProjectBaseinfoModel> implements ModelDriven<ProjectBaseinfoModel>{

	@Resource
	private ProjectBaseinfoService projectBaseinfoService;
	@Resource
	private WechatCacheService wechatCacheService;
	@Resource
	private AttentionListService attentionListService;
	@Resource
	private OrderService cfOrderService;
	@Resource
	private LeaderService leaderService;
	@Resource
	private VirtualAccountService virtualAccountService;
	@Resource
	private MaterialsService materialsService;
	
	private Map<String,Object> data;

	
	/**
	 * 微信产品列表 -- 页面
	 * @throws IOException
	 */
	@Action(value = "/wechat/p/list")
	public void listPc() throws IOException{
		List<ProjectBaseinfoModel> list = projectBaseinfoService.getWechatByType(paramInt("id"));
		data = new HashMap<String,Object>();
		data.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		data.put(ConstantUtil.ERRORMSG, list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 我的关注页面 -- 页面
	 * @return
	 */
	@Action(value="/cf/wechat/user/attention",results={@Result(name="attention",type="ftl",location="/nb/cf/wechat/user/attention.html")})
	public String myAttentionPage(){
		return "attention";
	}
	
	/**
	 * 添加关注 -- 数据
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/pro/attentionData")
	public void myAttention() throws IOException{
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		
		User user = getNBSessionUser();
		List<AttentionListModel> list = attentionListService.getByUserId(user.getUserId());
		data.put(ConstantUtil.DATA,list) ;
		data.put(ConstantUtil.MSG,"我关注的");
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 微信产品详情  -- 页面
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Action(value="/wechat/pro/detail",results={@Result(name="detail",type="ftl",location="/nb/cf/wechat/pro/detail.html"),
												@Result(name="meet",type="ftl",location="/nb/cf/wechat/user/meet.html")})
	public String wechatDetail() throws ClientProtocolException, IOException{
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
		request.setAttribute("desc","众筹，让天下没有沉睡的资源！");
		request.setAttribute("appId", appId);
		request.setAttribute("timestamp", ret.get("timestamp").toString());
		request.setAttribute("nonceStr", ret.get("nonceStr").toString());
		request.setAttribute("signature", ret.get("signature").toString());
		Long projectId = paramLong("id");
		request.setAttribute("id",projectId);
		ProjectBaseinfo projectBaseinfo = projectBaseinfoService.find(projectId);
		List<Materials> list = materialsService.findByProjectId(projectId);
		if(null!=list&&!list.isEmpty()){
			for(int  i = 0 ;i<list.size();i++){
				if("wechat_desc".equals(list.get(i).getMaterialCode())){
					request.setAttribute("desc",list.get(i).getMaterialContent());
				}
			}
		}
		User user = getNBSessionUser();
		if(null!=user){
			UserCache userCache = user.getUserCache();
			if(1!=userCache.getInvestStatus()){
				request.setAttribute("redirectUrl","/wechat/pro/detail.action?id="+projectBaseinfo.getId());
				return "meet";
			}
		}
		return "detail";
	}
	
	/**
	 * 微信产品详情  -- 页面
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Action(value="/wechat/pro/del",results={@Result(name="detail",type="ftl",location="/nb/cf/wechat/pro/del.html")})
	public String wechatDel() throws ClientProtocolException, IOException{
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
		request.setAttribute("desc","众筹，让天下没有沉睡的资源！");
		Long projectId = paramLong("id");
		request.setAttribute("id",projectId);
		List<Materials> list = materialsService.findByProjectId(projectId);
		if(null!=list&&!list.isEmpty()){
			for(int  i = 0 ;i<list.size();i++){
				if("wechat_desc".equals(list.get(i).getMaterialCode())){
					request.setAttribute("desc",list.get(i).getMaterialContent());
				}
			}
		}
		return "detail";
	}
	
	/**
	 * 微信产品详情 -- 数据
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/p/detail")
	public void wechatProjectDetail() throws IOException{
		Long id= paramLong("id");
		User user = getNBSessionUser();
		ProjectBaseinfo project = projectBaseinfoService.getById(id);
		List<AttentionList> atteLists = null;
		List<Leader> leaderList = null;
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("attention",-1);
		map.put("leaderText",-1);
		if(null!=user){
			atteLists = attentionListService.getByDoubleId(id,user.getUserId());
			leaderList = leaderService.getByProjectAndUserId(user.getUserId(),id);
			//用户是否已经关注此项目
			if(null!=atteLists&&!atteLists.isEmpty()){
				map.put("attention",1);
			}else{
				map.put("attention",0);
			}
			if(null!=leaderList&&!leaderList.isEmpty()){
				map.put("leaderText",1);
			}else{
				map.put("leaderText",0);
			}
		}
		List<OrderModel> orderList = cfOrderService.getList(id);
		
		if(null!=project.getLeader()){
			map.put("leaderStatus",1);
		}else{
			map.put("leaderStatus",0);
		}
		
		map.put("project",project);
		map.put("order",orderList);
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.MSG,"产品详情相关");
		data.put(ConstantUtil.DATA,map);
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 微信购买产品时产品数据 -- 数据
	 * @throws IOException
	 */
	@Action(value = "/cf/wechat/buyProjectDetail")
	public void wechatBuyDetail() throws IOException{
		Long id = paramLong("id");
		ProjectBaseinfoModel projectBaseinfo = projectBaseinfoService.getById(id);
		User user = getNBSessionUser();
		VirtualAccount virtualAccount = virtualAccountService.findByUserId(user.getUserId());
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("pro",projectBaseinfo);
		map.put("virtual",0);
		if(null!=virtualAccount){
			map.put("virtual",virtualAccount.getAccount());
		}
		
		data = new HashMap<String, Object>();
		data.put(ConstantUtil.CODE,ConstantUtil.OK_CODE);
		data.put(ConstantUtil.DATA,map);
		data.put(ConstantUtil.MSG,"微信购买产品时产品数据。");
		
		printWebJson(getStringOfJpaObj(data));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
