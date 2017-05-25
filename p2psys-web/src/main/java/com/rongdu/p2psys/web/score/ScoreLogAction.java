package com.rongdu.p2psys.web.score;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.model.ScoreLogModel;
import com.rongdu.p2psys.score.service.ScoreLogService;
import com.rongdu.p2psys.user.domain.User;

public class ScoreLogAction extends BaseAction implements ModelDriven<ScoreLogModel> {

	private ScoreLogModel model = new ScoreLogModel();
	
	private Map<String, Object> data;
	
	@Override
	public ScoreLogModel getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	@Resource
	private ScoreLogService scoreLogService;
	
	/**
	 * 跳转积分日志列表页
	 * @return
	 */
	@Action("/member/score/scoreLog")
	public String scoreLog () throws Exception{
		return "scoreLog";
	}
	
	/**
	 * 积分日志分页
	 * @return
	 */
	@Action("/member/score/scoreLogList")
	public void scoreLogList () throws Exception{
		model.setUser(new User(this.getSessionUserId()));
		PageDataList<ScoreLogModel> list = scoreLogService.getScoreLogPage(model);
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}
	

}
