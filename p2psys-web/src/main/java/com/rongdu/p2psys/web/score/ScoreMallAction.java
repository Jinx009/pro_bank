package com.rongdu.p2psys.web.score;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.domain.Goods;
import com.rongdu.p2psys.score.domain.GoodsCategory;
import com.rongdu.p2psys.score.domain.GoodsPic;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.model.GoodsModel;
import com.rongdu.p2psys.score.service.GoodsCategoryService;
import com.rongdu.p2psys.score.service.GoodsPicService;
import com.rongdu.p2psys.score.service.GoodsService;
import com.rongdu.p2psys.score.service.ScoreGoodsService;
import com.rongdu.p2psys.score.service.ScoreService;

public class ScoreMallAction extends BaseAction implements ModelDriven<GoodsModel> {

	private GoodsModel model = new GoodsModel();
	
	@Override
	public GoodsModel getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	@Resource
	private GoodsService goodsService;
	@Resource
	private ScoreService scoreService;
	@Resource
	private GoodsCategoryService goodsCategoryService;
	@Resource
	private GoodsPicService goodsPicService;
	@Resource
	private ScoreGoodsService scoreGoodsService;
	
	private Map<String, Object> data;
	
	/**
	 * 跳转商品列表页
	 * @return
	 */
	@Action(value = "/scoreMall/goodsPage", interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack")})
	public String goodsPage(){
		Score userScore = scoreService.getScoreByUserId(this.getSessionUserId());
		List<GoodsCategory> itemList = goodsCategoryService.getCategoryAll();
		request.setAttribute("itemList", itemList);
		request.setAttribute("userScore", userScore);
		return "goodsPage";
	}
	
	/**
	 * 商品列表分页
	 * @throws Exception
	 */
	@Action(value = "/scoreMall/goodsList", interceptorRefs = {@InterceptorRef("ajaxSafe"), @InterceptorRef("session"), @InterceptorRef("globalStack")})
	public void goodsList() throws Exception{
		model.setStatus(GoodsModel.PASS_AUDIT);
		model.setType(GoodsModel.UP_SHELVES);
		PageDataList<GoodsModel> page = goodsService.getGoodsPage(model);
		data = new HashMap<String, Object>();
		data.put("data", page);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 商品详情页
	 * @return
	 */
	@Action(value = "/scoreMall/detail_*", interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack")})
	public String goodsDetails(){
		long goodsId = this.getRequestGoodsId(request.getServletPath());
		Goods goods = goodsService.getGoodsById(goodsId);
		List<GoodsPic> picList = goodsPicService.getGoodsPicByGoodsId(goodsId);
		Score userScore = scoreService.getScoreByUserId(this.getSessionUserId());
		request.setAttribute("userScore", userScore);
		request.setAttribute("picList", picList);
		request.setAttribute("goods", goods);
		request.setAttribute("category", goods.getGoodsCategory());
		return "detailPage";
	}
	
	/**
	 * 根据动态的URL获取商品ID
	 * @param url
	 * @return
	 */
	private long getRequestGoodsId(String url){
		String goodsId = url.substring(18).replace(".action","");
		return StringUtil.toLong(goodsId);
	}
}
