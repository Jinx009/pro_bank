package com.rongdu.manage.action.score;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.domain.ScoreGoods;
import com.rongdu.p2psys.score.model.ScoreGoodsModel;
import com.rongdu.p2psys.score.service.GoodsService;
import com.rongdu.p2psys.score.service.ScoreGoodsService;
import com.rongdu.p2psys.score.service.ScoreService;

/**
 * 积分兑换商品
 */
public class ManageScoreGoodsAction extends BaseAction implements ModelDriven<ScoreGoodsModel> {

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
	@Action(value = "/modules/user/score/goodsRecordManager")
	public String goodsRecordManager(){
		return "goodsRecordManager";
	}
	
	/**
	 * 跳转至积分兑换记录页
	 * @return
	 */
	@Action(value = "/modules/user/score/goodsRecordList")
	public void recordList() throws Exception{
		String status = this.paramString("status");
		if(status.length() <= 0){
			model.setStatus((byte)-99);
		}
		PageDataList<ScoreGoodsModel> page = scoreGoodsService.getScoreGoodsPage(model);
		data = new HashMap<String, Object>();
		if(page.getPage() == null){
			data.put("total", 0); // 总行数
		}else {
			data.put("total", page.getPage().getTotal()); // 总行数
		}
		data.put("rows", page.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	@Action("/modules/user/score/verifyGoodsRecordPage")
	public String verifyGoodsRecordPage(){
		this.saveToken("verifyToken");
		ScoreGoods item = scoreGoodsService.getScoreGoodsById(model.getId());
		request.setAttribute("item", item);
		request.setAttribute("goods", item.getGoods());
		request.setAttribute("user", item.getUser());
		return "verifyGoodsRecordPage";
	}
	
	@Action("/modules/user/score/verifyGoodsRecord")
	public void verifyGoodsRecord() throws Exception{
		checkToken("verifyToken");
		model.setVerifyUser(this.getOperatorUserName());
		model.setVerifyUserId(this.getOperatorId());
		Boolean result = scoreGoodsService.verifyScoreGoods(model);
		if (result) {
			printResult(MessageUtil.getMessage("I10009"), result);
		} else {
			printResult(MessageUtil.getMessage("I10019"), result);
		}
	}
}
