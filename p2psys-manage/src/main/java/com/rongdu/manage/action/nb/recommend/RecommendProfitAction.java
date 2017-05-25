package com.rongdu.manage.action.nb.recommend;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.p2psys.core.constant.Constant;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfit;
import com.rongdu.p2psys.nb.recommend.model.RecommendProfitModel;
import com.rongdu.p2psys.nb.recommend.service.RecommedProfitService;

public class RecommendProfitAction extends BaseAction<RecommendProfitModel>  implements ModelDriven<RecommendProfitModel>
{
	@Resource 
	private RecommedProfitService recommedProfitService;
	
	private Map<String,Object> map;

	/***
	 * 推荐受益规则列表
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/recommend/recommendProfitList")
	public void dataList() throws Exception
	{
		map = new HashMap<String, Object>();
		
		List<RecommendProfit> list = recommedProfitService.getList();
		
		map.put("data",list);
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	
	/**
	 * 首页
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/recommend/recommendProfit")
	public String recommendProfit() throws Exception
	{
		return "recommendProfit";
	}
	
	/***
	 * 修改
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/recommend/recommendProfitUpdate")
	public void update() throws Exception
	{
		map = new HashMap<String, Object>();
		
		RecommendProfit recommendProfit = model.protoType();
		
		Operator operator = (Operator) session.get(Constant.SESSION_OPERATOR);
		
		recommendProfit.setOperator(operator);
		recommendProfit.setUpdateTime(new Date());
		
		recommedProfitService.updateRecommednProfit(recommendProfit);
		
		map.put("result","success");
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	/***
	 * 新增
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/recommend/recommendProfitAdd")
	public void add() throws Exception
	{
		map = new HashMap<String, Object>();
		
		RecommendProfit recommendProfit = model.protoType();
		
		Operator operator = (Operator) session.get(Constant.SESSION_OPERATOR);
		
		recommendProfit.setOperator(operator);
		recommendProfit.setUpdateTime(new Date());
		
		recommedProfitService.saveRecommendProfit(recommendProfit);
		
		map.put("result","success");
		
		printWebJson(getStringOfJpaObj(map));
	}
	
	
	/***
	 * 删除
	 * @throws Exception
	 */
	@Action(value = "/modules/nb/recommend/recommendProfitDelete")
	public void delete() throws Exception
	{
		map = new HashMap<String, Object>();
		
		RecommendProfit recommendProfit = model.protoType();
		
		recommedProfitService.deleteRecommendProfit(recommendProfit.getId());
		
		map.put("result","success");
		
		printWebJson(getStringOfJpaObj(map));
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
