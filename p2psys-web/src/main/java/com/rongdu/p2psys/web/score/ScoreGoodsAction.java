package com.rongdu.p2psys.web.score;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.model.ScoreGoodsModel;
import com.rongdu.p2psys.score.service.GoodsService;
import com.rongdu.p2psys.score.service.ScoreGoodsService;
import com.rongdu.p2psys.score.service.ScoreService;
import com.rongdu.p2psys.user.domain.User;

/**
 * 积分兑换商品
 */
public class ScoreGoodsAction extends BaseAction implements ModelDriven<ScoreGoodsModel> {

	private ScoreGoodsModel model = new ScoreGoodsModel();
	
	@Override
	public ScoreGoodsModel getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	@Resource
	private GoodsService goodsService;
	@Resource
	private ScoreService scoreService;
	@Resource
	private ScoreGoodsService scoreGoodsService;
	
	private Map<String, Object> data;
	
	/**
	 * 跳转至积分兑换记录页
	 * @return
	 */
	@Action(value = "/scoreMall/recordPage", interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack")})
	public String recordPage(){
		Score userScore = scoreService.getScoreByUserId(this.getSessionUserId());
		request.setAttribute("userScore", userScore);
		return "recordPage";
	}
	
	/**
	 * 跳转至积分兑换记录页
	 * @return
	 */
	@Action(value = "/scoreMall/recordList", interceptorRefs = { @InterceptorRef("session"), @InterceptorRef("globalStack")})
	public void recordList() throws Exception{
		model.setStatus((byte)-99);
		PageDataList<ScoreGoodsModel> page = scoreGoodsService.getScoreGoodsPage(model);
		data = new HashMap<String, Object>();
		data.put("data", page);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 添加用户兑换信息
	 */
	@Action(value = "/scoreMall/addRecord", interceptorRefs = {@InterceptorRef("ajaxSafe"), @InterceptorRef("session"), @InterceptorRef("globalStack")})
	public void addRecord() throws Exception{
		model.checkUser(this.getSessionUserId());
		model.setUser(new User(this.getSessionUserId()));
		boolean result = scoreGoodsService.addScoreGoods(model);
		data = new HashMap<String, Object>();
		data.put("data", result);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 获取最新动态
	 * @throws Exception
	 */
	@Action(value = "/scoreMall/convertRecordList", interceptorRefs = {@InterceptorRef("ajaxSafe"), @InterceptorRef("session"), @InterceptorRef("globalStack")})
	public void convertRecordList() throws Exception{
		List<ScoreGoodsModel> modelList = scoreGoodsService.getScoreGoodsList(7);
		data = new HashMap<String, Object>();
		data.put("data", modelList);
		printWebJson(getStringOfJpaObj(data));
	}
}
