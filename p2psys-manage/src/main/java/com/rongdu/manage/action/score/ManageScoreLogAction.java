package com.rongdu.manage.action.score;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.model.ScoreLogModel;
import com.rongdu.p2psys.score.service.ScoreLogService;

public class ManageScoreLogAction extends BaseAction implements ModelDriven<ScoreLogModel> {

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
	@Action("/modules/user/score/scoreLogManager")
	public String scoreLogManager () throws Exception{
		return "scoreLogManager";
	}
	
	/**
	 * 积分日志分页
	 * @return
	 */
	@Action("/modules/user/score/scoreLogList")
	public void scoreLogList () throws Exception{
		PageDataList<ScoreLogModel> page = scoreLogService.getScoreLogPage(model);
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
