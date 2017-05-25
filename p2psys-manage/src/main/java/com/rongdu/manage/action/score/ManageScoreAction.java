package com.rongdu.manage.action.score;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.model.ScoreModel;
import com.rongdu.p2psys.score.service.ScoreService;

public class ManageScoreAction extends BaseAction implements ModelDriven<ScoreModel> {

	private ScoreModel model = new ScoreModel();
	
	private Map<String, Object> data;
	
	@Override
	public ScoreModel getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	@Resource
	private ScoreService scoreService;
	
	@Action("/modules/user/score/scoreManager")
	public String scoreManager(){
		return "scoreManager";
	}

	/**
	 * 用户积分分页
	 * @return
	 */
	@Action("/modules/user/score/scoreList")
	public void scoreList () throws Exception{
		PageDataList<ScoreModel> page = scoreService.getScorePage(model);
		data = new HashMap<String, Object>();
		if(page.getPage() == null){
			data.put("total", 0); // 总行数
		}else {
			data.put("total", page.getPage().getTotal()); // 总行数
		}
		data.put("rows", page.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	
}
