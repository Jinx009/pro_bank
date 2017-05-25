package com.rongdu.p2psys.wechat;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.recommend.model.RecommendProfitRecordModel;
import com.rongdu.p2psys.nb.recommend.service.RecommendProfitRecordService;
import com.rongdu.p2psys.nb.util.ConstantUtil;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.util.WechatData;

public class WechatRecommendAction extends BaseAction<RecommendProfitRecordModel> implements ModelDriven<RecommendProfitRecordModel>
{
	private Map<String,Object> map;
	
	@Resource
	private RecommendProfitRecordService recommendProfitRecordService;

	/**
	 * 判断账号是否存在
	 * @throws IOException 
	 */
	@Action(value = "/nb/wechat/account/recommendProfitRecordData")
	public void recommendProfitRecordData() throws IOException
	{
		map =new HashMap<String, Object>();
		User user = getNBSessionUser();
		
		int pageNumber = paramInt("page");
		int pageSize = 8;
		
		model.setPage(pageNumber);
		model.setSize(pageSize);
		
		PageDataList<RecommendProfitRecordModel> list = recommendProfitRecordService.getProfitRecord(model,user);
		
		map.put(ConstantUtil.ERRORMSG,list);
		map.put(ConstantUtil.RESULT,ConstantUtil.SUCCESS);
		
		printJson(getStringOfJpaObj(map));
	}
	
	@Action(value = "/nb/wechat/account/recommendProfitRecord")
	public String recommendProfitRecord() throws IOException
	{
		User user = getNBSessionUser();
		double money = 0;
		
		List<RecommendProfitRecordModel> list2 = recommendProfitRecordService.getSelfRecommendProfit(user.getUserId());
		for(int i = 0;i<list2.size();i++)
		{
			money += list2.get(i).getMoney();
		}
		NumberFormat nf = new DecimalFormat(",###.##");
		
		request.setAttribute("total",nf.format(money));
		
		request.setAttribute("userId",user.getUserId());
		request.setAttribute("wechatUrl",getWechatUrl());
		
		return "recommendProfitRecord";
	}
	
	
	  /**
     * 推荐人收益记录列表
     * @throws IOException 
     */
	@Action("/nb/recommend/recommendProfitRecordList")
	public void recommendProfitRecordList() throws IOException{
		User user = (User) request.getSession().getAttribute(ConstantUtil.SESSION_USER);
		int pageNumber = paramInt("page");
		int pageSize = 12;
		model.setPage(pageNumber);
		model.setSize(pageSize);
		PageDataList<RecommendProfitRecordModel> list = recommendProfitRecordService.getProfitRecord(model,user);
		map =new HashMap<String, Object>();
		map.put("data",list);
		printJson(getStringOfJpaObj(map));
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
	
	public Map<String, Object> getMap()
	{
		return map;
	}
	public void setMap(Map<String, Object> map)
	{
		this.map = map;
	}
	
}
