package com.rongdu.manage.action.score;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.util.MessageUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.rule.ScoreConvertVipRuleCheck;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.constant.ScoreTypeConstant;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.domain.ScoreConvert;
import com.rongdu.p2psys.score.domain.ScoreType;
import com.rongdu.p2psys.score.model.ScoreConvertModel;
import com.rongdu.p2psys.score.service.ScoreConvertService;
import com.rongdu.p2psys.score.service.ScoreService;
import com.rongdu.p2psys.score.service.ScoreTypeService;
import com.rongdu.p2psys.user.domain.User;

public class ManageScoreConvertAction extends BaseAction implements ModelDriven<ScoreConvertModel>{
	
	private ScoreConvertModel model = new ScoreConvertModel();
	
	private Map<String, Object> data;

	@Override
	public ScoreConvertModel getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	@Resource
	private ScoreConvertService scoreConvertService;
	@Resource
	private ScoreService scoreService;
	@Resource
	private ScoreTypeService scoreTypeService;
	
	/**
	 * 跳转积分兑换列表页
	 * @return
	 */
	@Action("/modules/user/score/scoreConvertManager")
	public String scoreConvertManager () throws Exception{
		return "scoreConvertManager";
	}
	
	/**
	 * 积分兑换分页
	 * @return
	 */
	@Action("/modules/user/score/scoreConvertList")
	public void convertList () throws Exception{
		PageDataList<ScoreConvertModel> page = scoreConvertService.getCreditConvertPage(model);
		data = new HashMap<String, Object>();
		if(page.getPage() == null){
			data.put("total", 0); // 总行数
		}else {
			data.put("total", page.getPage().getTotal()); // 总行数
		}
		data.put("rows", page.getList()); // 集合对象
		printJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 跳转积分兑换审核页
	 * @return
	 */
	@Action("/modules/user/score/verifyScoreConvertPage")
	public String verifyScoreConvertPage () throws Exception{
		ScoreConvert convert = scoreConvertService.getScoreConvertById(model.getId());
		User user = convert.getUser();
		Score userScore = scoreService.getScoreByUserId(user.getUserId());
		if(convert.getScoreTypeNid() != null && convert.getScoreTypeNid().equals(ScoreTypeConstant.SCORE_CONVERT_VIP)){
			// 提取积分兑换规则信息
			ScoreType type = scoreTypeService.getScoreTypeByNid(ScoreTypeConstant.SCORE_CONVERT_VIP);
			// 提取积分兑换规则信息
			ScoreConvertVipRuleCheck rule = (ScoreConvertVipRuleCheck) Global.getRuleCheck(type.getRuleNid());
			request.setAttribute("vipTime", rule.vip_time);
		}
		request.setAttribute("convert", convert);
		request.setAttribute("user", user);
		request.setAttribute("userScore", userScore);
		this.saveToken("convertToken");
		return "verifyScoreConvertPage";
	}
	
	/**
	 * 跳转积分兑换审核
	 * @return
	 */
	@Action("/modules/user/score/verifyScoreConvert")
	public void verifyScoreConvert () throws Exception{
		checkToken("convertToken");
		Boolean result = true;
		ScoreConvert convert = scoreConvertService.getScoreConvertById(model.getId());
		if(convert.getScoreTypeNid() != null && convert.getScoreTypeNid().equals(ScoreTypeConstant.SCORE_CONVERT_VIP)){
			result = scoreConvertService.auditScoreConvertVip(model);
		}else if(convert.getScoreTypeNid() != null && convert.getScoreTypeNid().equals(ScoreTypeConstant.SCORE_CONVERT_MONEY)){
			result = scoreConvertService.auditScoreConvertMoney(model);
		}
		printResult(MessageUtil.getMessage("I10009"), result);
	}
}
