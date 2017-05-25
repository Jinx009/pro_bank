package com.rongdu.p2psys.web.score;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;

import com.opensymphony.xwork2.ModelDriven;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.rule.ScoreConvertMoneyRuleCheck;
import com.rongdu.p2psys.core.rule.ScoreConvertVipRuleCheck;
import com.rongdu.p2psys.core.web.BaseAction;
import com.rongdu.p2psys.score.constant.ScoreTypeConstant;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.domain.ScoreType;
import com.rongdu.p2psys.score.model.ScoreConvertModel;
import com.rongdu.p2psys.score.service.ScoreConvertService;
import com.rongdu.p2psys.score.service.ScoreService;
import com.rongdu.p2psys.score.service.ScoreTypeService;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;
import com.rongdu.p2psys.user.service.UserIdentifyService;

public class ScoreConvertAction extends BaseAction implements ModelDriven<ScoreConvertModel>{
	
	private static Logger logger = Logger.getLogger(ScoreConvertAction.class);
	
	private ScoreConvertModel model = new ScoreConvertModel();
	
	private Map<String, Object> data;

	@Override
	public ScoreConvertModel getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	@Resource
	private ScoreService scoreService;
	@Resource
	private ScoreTypeService scoreTypeService;
	@Resource
	private ScoreConvertService scoreConvertService;
	@Resource
	private UserIdentifyService userIdentifyService;
	
	@Action("/member/score/convertMoney")
	public String scoreConvertMoney() {
		User user = this.getSessionUser();
		Score userScore = scoreService.getScoreByUserId(user.getUserId());
		request.setAttribute("userScore", userScore);
		request.setAttribute("user", user);
		this.saveToken("convertToken");
		return "convertMoney";
	}
	
	/**
	 * 积分兑换
	 * @return
	 */
	@Action("/member/score/doConvertMoney")
	public String doConvertMoney()throws Exception{
		checkToken("convertToken");
		// 兑换规则验证
		model.checkUser(this.getSessionUserId());
		boolean result = scoreConvertService.scoreConvertMoney(model.prototype());
		String msg = "积分兑换成功！";
		if(!result){
			msg = "积分兑换失败！";
		}
		request.setAttribute("msg", msg);
		User user = this.getSessionUser();
		Score userScore = scoreService.getScoreByUserId(user.getUserId());
		request.setAttribute("userScore", userScore);
		request.setAttribute("user", user);
		this.saveToken("convertToken");
		return "convertMoney";
	}
	
	/**
	 * 积分兑换Vip
	 * @return
	 */
	@Action("/member/score/convertVip")
	public String convertVip(){
		Score userScore = scoreService.getScoreByUserId(this.getSessionUserId());
		request.setAttribute("result", this.checkConvertVip());
		request.setAttribute("userScore", userScore);
		request.setAttribute("user", this.getSessionUser());
		saveToken("convertToken");
		return "convertVip";
		
	}
	
	/**
	 * 积分兑换VIP
	 * @return
	 */
	@Action("/member/score/doConvertVip")
	public String doConvertVip () throws Exception{
		checkToken("convertToken");
		model.checkUser(this.getSessionUserId());
		boolean result = scoreConvertService.scoreConvertVIP(model.prototype());
		String msg = "积分兑换VIP成功！";
		if(!result){
			msg = "积分兑换VIP失败！";
		}
		request.setAttribute("msg", msg);
		User user = this.getSessionUser();
		Score userScore = scoreService.getScoreByUserId(user.getUserId());
		request.setAttribute("userScore", userScore);
		request.setAttribute("user", user);
		request.setAttribute("result", this.checkConvertVip());
		this.saveToken("convertToken");
		return "convertVip";
	}
	
	/**
	 * 跳转积分兑换列表页
	 * @return
	 */
	@Action("/member/score/convert")
	public String convert () throws Exception{
		return "convert";
	}
	
	/**
	 * 积分兑换分页
	 * @return
	 */
	@Action("/member/score/convertList")
	public void convertList () throws Exception{
		model.setUser(new User(this.getSessionUserId()));
		PageDataList<ScoreConvertModel> list = scoreConvertService.getCreditConvertPage(model);
		data = new HashMap<String, Object>();
		data.put("data", list);
		printWebJson(getStringOfJpaObj(data));
	}
	
	/**
	 * 积分兑换现金Ajax验证
	 * @return
	 */
	@Action("/member/score/convertMoneyValidate")
	public void creditCashValidate ()throws Exception{
		User user = this.getSessionUser();
		// 提取积分兑换规则信息
		ScoreType type = scoreTypeService.getScoreTypeByNid(ScoreTypeConstant.SCORE_CONVERT_MONEY);
		// 提取积分兑换规则信息
		ScoreConvertMoneyRuleCheck rule = (ScoreConvertMoneyRuleCheck) Global.getRuleCheck(type.getRuleNid());
		int convertBasic = rule.convert_basic;//积分兑换基数
		int convertMin = rule.convert_min;//最小兑换基数
		Score score = scoreService.getScoreByUserId(user.getUserId());
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("item", score);
		map.put("convertBasic", convertBasic);
		map.put("convertMin", convertMin);
		Map<String,Object> map2=new HashMap<String,Object>();
		map2.put("msg", "success");
		map2.put("infoMap", map);
		this.printWebJson(this.getStringOfJpaObj(map2));
	}
	
	/**
	 * 积分兑换VIP约束
	 * @return 是否通过
	 */
	public Boolean checkConvertVip() {
		Boolean result = true;
		User user = this.getSessionUser();
		ScoreType type = scoreTypeService.getScoreTypeByNid(ScoreTypeConstant.SCORE_CONVERT_VIP);
		// 如果积分类型为空，或者积分类型的积分种类为空，则return
		if( type == null ) {
			logger.info("积分类型为空！");
			result = false;
		}
		
		ScoreConvertVipRuleCheck rule = (ScoreConvertVipRuleCheck) Global.getRuleCheck(type.getRuleNid());
		// 兑换VIP规则验证
		Boolean check = scoreConvertService.checkConvertVipRule(type.getRuleNid(), user.getUserId(), rule.convert_score);
		if(!check){
			result = false;
		}
		
		UserIdentify userIdentify = userIdentifyService.getUserIdentifyByUserId(user.getUserId());
		// 如果vip正在等待审核中，则不能申请
		if(userIdentify.getVipStatus() == 2){
			result = false;
		}
		request.setAttribute("convertScore", rule.convert_score);
		return result;
	}
}
