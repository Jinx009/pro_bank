package com.rongdu.p2psys.score.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.OrderFilter.OrderType;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.BigDecimalUtil;
import com.rongdu.common.util.DateUtil;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.account.dao.AccountDao;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.dao.OperatorDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.executer.AbstractExecuter;
import com.rongdu.p2psys.core.executer.ExecuterHelper;
import com.rongdu.p2psys.core.rule.ScoreConvertMoneyRuleCheck;
import com.rongdu.p2psys.core.rule.ScoreConvertVipRuleCheck;
import com.rongdu.p2psys.score.constant.ScoreConstant;
import com.rongdu.p2psys.score.constant.ScoreTypeConstant;
import com.rongdu.p2psys.score.dao.ScoreConvertDao;
import com.rongdu.p2psys.score.dao.ScoreDao;
import com.rongdu.p2psys.score.dao.ScoreTypeDao;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.domain.ScoreConvert;
import com.rongdu.p2psys.score.domain.ScoreType;
import com.rongdu.p2psys.score.exception.ScoreException;
import com.rongdu.p2psys.score.model.ScoreConvertModel;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.convert.ScoreConvertMoneyApplyLog;
import com.rongdu.p2psys.score.model.scorelog.convert.ScoreConvertMoneyFailLog;
import com.rongdu.p2psys.score.model.scorelog.convert.ScoreConvertMoneySuccessLog;
import com.rongdu.p2psys.score.model.scorelog.convert.ScoreConvertVipApplyLog;
import com.rongdu.p2psys.score.model.scorelog.convert.ScoreConvertVipFailLog;
import com.rongdu.p2psys.score.model.scorelog.convert.ScoreConvertVipSuccessLog;
import com.rongdu.p2psys.score.service.ScoreConvertService;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.dao.UserIdentifyDao;
import com.rongdu.p2psys.user.domain.User;
import com.rongdu.p2psys.user.domain.UserIdentify;

@Service("scoreConvertService")
public class ScoreConvertServiceImpl implements ScoreConvertService {

	private static Logger logger = Logger.getLogger(ScoreConvertServiceImpl.class);
	
	@Resource
	private ScoreConvertDao scoreConvertDao;

	@Resource
	private ScoreTypeDao scoreTypedao;
	
	@Resource
	private ScoreDao scoreDao;
	
	@Resource
	private UserDao userDao;
	
	@Resource
	private AccountDao accountDao;
	
	@Resource
	private OperatorDao operatorDao;
	
	@Resource
	private UserIdentifyDao userIdentifyDao;
	
	public boolean scoreConvertMoney(ScoreConvert scoreConvert) {
		
		long userId = scoreConvert.getUser().getUserId();
		int value = scoreConvert.getScore();
		
		// 如果积分兑换实体为空，或兑换积分小于等于零，或user_id为空，则return
		if (scoreConvert == null || value <= 0 || userId <= 0) {
			return false;
		}
		
		ScoreType type = scoreTypedao.getScoreTypeByNid(ScoreTypeConstant.SCORE_CONVERT_MONEY);
		// 如果积分类型为空，或者积分类型的积分种类为空，则return
		if (type == null) {
			logger.info("积分类型为空！");
			return false;
		}
		
		// 提取积分兑换规则信息
		ScoreConvertMoneyRuleCheck rule = (ScoreConvertMoneyRuleCheck) Global.getRuleCheck(type.getRuleNid());
		if(rule == null || rule.status != ScoreConstant.STATUS_YES) return false;
		
		int convertBasic = rule.convert_basic;//积分兑换基数
		int convertMin = rule.convert_min;//最小兑换基数
		
		Score userScore =  scoreDao.findObjByProperty("user.userId", scoreConvert.getUser().getUserId());
		
		// 如果兑换积分小于最小兑换基数，或兑换积分小于有效积分，则return
		if(value < convertMin || userScore.getValidScore() < value){
			return false;
		}
		
		// 数据转换，防止计算错误
		double useValue = Double.parseDouble(value+"");
		double basicValue = Double.parseDouble(convertBasic+"");
		double money = BigDecimalUtil.div(useValue, basicValue);
		// 如果兑换金额小于等于零，则return
		if(money <= 0){
			return false;
		}
		//兑换信息封装
		scoreConvert.setAddTime(new Date());;
		scoreConvert.setMoney(money);
		scoreConvert.setStatus(ScoreConvert.WAIT_AUDIT);
		scoreConvert.setScoreTypeNid(type.getNid());
		scoreConvert.setScoreTypeName(type.getName());
		scoreConvertDao.save(scoreConvert);
		//会员积分日志记录信息
		Global.setTransfer("score", value);
		Global.setTransfer("money", money);
		BaseScoreLog bLog = new ScoreConvertMoneyApplyLog(userId, value);
		bLog.doEvent();
		return true;
	}

	public boolean scoreConvertVIP(ScoreConvert scoreConvert) {
		
		long userId = scoreConvert.getUser().getUserId();
		int value = scoreConvert.getScore();
		
		// 如果积分兑换实体为空，或兑换积分小于等于零，或user_id为空，则return
		if (scoreConvert == null || value <= 0 || userId <= 0) {
			return false;
		}
		
		ScoreType type = scoreTypedao.getScoreTypeByNid(ScoreTypeConstant.SCORE_CONVERT_VIP);
		// 如果积分类型为空，或者积分类型的积分种类为空，则return
		if (type == null) {
			logger.info("积分类型为空！");
			return false;
		}
		// 兑换VIP规则约束
		Boolean check = this.checkConvertVipRule(type.getRuleNid(), userId, value);
		if (!check) {
			return false;
		}
		
		//兑换信息封装
		scoreConvert.setAddTime(new Date());
		scoreConvert.setMoney(Global.getDouble("vip_fee"));
		scoreConvert.setStatus(ScoreConvert.WAIT_AUDIT);
		scoreConvert.setScoreTypeNid(type.getNid());
		scoreConvert.setScoreTypeName(type.getName());
		scoreConvertDao.save(scoreConvert);
		//会员积分日志记录信息
		Global.setTransfer("score", value);
		BaseScoreLog bLog = new ScoreConvertVipApplyLog(userId, value);
		bLog.doEvent();
		return true;
	}

	public PageDataList<ScoreConvertModel> getCreditConvertPage(ScoreConvertModel model) {
		QueryParam param = QueryParam.getInstance();
		if (StringUtil.isNotBlank(model.getSearchName())) {
			param.addParam("user.userName", Operators.LIKE, model.getSearchName());
		}else{
			if(model.getUser() != null && model.getUser().getUserId() > 0){
				param.addParam("user.userId", model.getUser().getUserId());
			}
			if(model.getUserName() != null && model.getUserName().length() > 0){
				param.addParam("user.userName",Operators.EQ , model.getUserName());
			}
			if (StringUtil.isNotBlank(model.getStartTime())) {
				Date start = DateUtil.valueOf(model.getStartTime() + " 00:00:00");
				param.addParam("addTime", Operators.GTE, start);
			}
			if (StringUtil.isNotBlank(model.getEndTime())) {
				Date end = DateUtil.valueOf(model.getEndTime() + " 23:59:59");
				param.addParam("addTime", Operators.LTE, end);
			}
			if (StringUtil.isNotBlank(model.getScoreTypeNid()) && !("").equals(model.getScoreTypeNid())) {
				param.addParam("scoreTypeNid", model.getScoreTypeNid());
			}
			if (model.getStatus() > -99) {
				param.addParam("status", model.getStatus());
			}
		}
		param.addOrder(OrderType.DESC, "id");
		if(model.getRows() == 0){
			param.addPage(model.getPage());
		}else{
			param.addPage(model.getPage(), model.getRows());
		}
		
		PageDataList<ScoreConvert> itemPage = scoreConvertDao.findPageList(param);
		List<ScoreConvertModel> modelList = new ArrayList<ScoreConvertModel>();
		PageDataList<ScoreConvertModel> modelPage = new PageDataList<ScoreConvertModel>();
		if(itemPage != null && itemPage.getList() != null && itemPage.getList().size() > 0){
			modelPage.setPage(itemPage.getPage());
			for (int i = 0 ; i < itemPage.getList().size(); i++) {
				ScoreConvert item = itemPage.getList().get(i);
				ScoreConvertModel model_ = ScoreConvertModel.instance(item);
				model_.setUserName(item.getUser().getUserName());
				modelList.add(model_);
			}
		}
		modelPage.setList(modelList);
		return modelPage;
	}

	public void insertScoreConvert(ScoreConvert scoreConvert) {
		// TODO Auto-generated method stub

	}

	public ScoreConvert getScoreConvertById(Long id) {
		// TODO Auto-generated method stub
		return scoreConvertDao.find(id);
	}

	public boolean auditScoreConvertMoney(ScoreConvertModel model) {
		
		// 如果积分兑换信息为空,或ID小于等于零,或状态小于等于零，则return
		if(model == null || model.getId() <= 0 || model.getStatus() <= 0){
			return false;
		}
		ScoreConvert item = scoreConvertDao.find(model.getId());
		User user = item.getUser();
		if(user == null) {
			return false;
		}
		int value = item.getScore();
		double money = item.getMoney();
		
		// 如果兑换信息不等于未审核，则return
		if(item.getStatus() != ScoreConvert.WAIT_AUDIT) {
			return false;
		}
		
		Global.setTransfer("score", value);
		Global.setTransfer("money", money);
		item.setVerifyTime(new Date());
		// 审核通过
		if(model.getStatus() == ScoreConvert.PASS_AUDIT){
			//会员积分日志记录信息
			BaseScoreLog bLog = new ScoreConvertMoneySuccessLog(user.getUserId(), value);
			bLog.doEvent();
			//资金日志处理
			Operator op = operatorDao.find(model.getVerifyUserId());
			AbstractExecuter awardRepayExecuter = ExecuterHelper.doExecuter("scoreConvertAwardExecuter");
			awardRepayExecuter.execute(money, user, op, null);
		} else if (model.getStatus() == ScoreConvert.NOT_PASS_AUDIT) { // 审核失败或非法操作，返回兑换积分：消费积分减，有效积分加
			BaseScoreLog bLog = new ScoreConvertMoneyFailLog(user.getUserId(), value);
			bLog.doEvent();
		} else {
			throw new ScoreException("积分兑换现金信息有误！", 1);
		}
		item.setVerifyRemark(model.getVerifyRemark());
		item.setVerifyUser(user.getUserName());
		item.setVerifyUserId(user.getUserId());
		item.setVerifyTime(new Date());
		item.setStatus(model.getStatus());
		scoreConvertDao.update(item);
		return true;
	}

	public boolean auditScoreConvertVip(ScoreConvertModel model) {
		
		ScoreType type = scoreTypedao.getScoreTypeByNid(ScoreTypeConstant.SCORE_CONVERT_VIP);
		// 如果积分类型为空，或者积分类型的积分种类为空，则return
		if (type == null) {
			return false;
		}
		
		// 提取积分兑换规则信息
		ScoreConvertVipRuleCheck rule = (ScoreConvertVipRuleCheck) Global.getRuleCheck(type.getRuleNid());
		if (rule == null) {
			return false;
		}
		int vipTime = rule.vip_time; // 兑换VIP有效时间（月）
		
		ScoreConvert item = scoreConvertDao.find(model.getId());
		User user = item.getUser();
		if (user == null) {
			return false;
		}
		int value = item.getScore();
		// 如果兑换信息不等于未审核，则return
		if (item.getStatus() != ScoreConvert.WAIT_AUDIT) {
			return false;
		}
		
		Global.setTransfer("score", value);
		// 审核通过
		if (model.getStatus() == ScoreConvert.PASS_AUDIT) {
			UserIdentify userIdentify = userIdentifyDao.findObjByProperty("userId", user.getUserId());
			// 如果vip正在等待审核中，则不能申请
			if (userIdentify.getVipStatus() == 2 || userIdentify == null) {
				return false;
			}
			// 更新结束时间
			Date vipEndTime = new Date();
			long vipEndtime_ = 0;
			if (userIdentify.getVipEndTime() != null) {
				vipEndtime_ = userIdentify.getVipEndTime().getTime() / 1000;
			}
			long nowTime = new Date().getTime() / 1000;
			// 没有申请vip或申请vip失败,或过期VIP
			if (userIdentify.getVipStatus() == 0 || userIdentify.getVipStatus() == -1 || nowTime >= vipEndtime_) {
				userIdentify.setVipStatus(1);
				// vipTime 兑换时间     -转换  vipTime
				vipEndTime = DateUtil.rollMon(new Date(), vipTime);
			} else if (userIdentify.getVipStatus() == 1) { // vip现在状态就是vip
				vipEndTime = DateUtil.rollMon(userIdentify.getVipEndTime(), vipTime);
			} else { // 如果vip正在等待审核中，则不能申请
				return false;
			}
			userIdentify.setVipEndTime(vipEndTime);
			userIdentify.setVipVerifyTime(new Date());
			userIdentifyDao.update(userIdentify);
			//会员积分日志记录信息
			BaseScoreLog bLog = new ScoreConvertVipSuccessLog(user.getUserId(), value);
			bLog.doEvent();
		} else { // 审核失败或非法操作，返回兑换积分
			BaseScoreLog bLog = new ScoreConvertVipFailLog(user.getUserId(), value);
			bLog.doEvent();
		}
		item.setVerifyRemark(model.getVerifyRemark());
		item.setVerifyUser(user.getUserName());
		item.setVerifyUserId(user.getUserId());
		item.setVerifyTime(new Date());
		item.setStatus(model.getStatus());
		scoreConvertDao.update(item);
		return true;
	}

	public List<ScoreConvertModel> getScoreConvert(long userId, String typeNid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 积分兑换VIP验证
	 * @param ruleNid
	 * @param userId
	 * @param value
	 * @return
	 */
	public boolean checkConvertVipRule(String ruleNid, long userId, int value){
		// 提取积分兑换规则信息
		ScoreConvertVipRuleCheck rule = (ScoreConvertVipRuleCheck) Global.getRuleCheck(ruleNid);
		if(rule == null || rule.status != ScoreConstant.STATUS_YES) return false;
		
		int score = rule.convert_score;//兑换需要的积分
		int isCheck = rule.is_check;// 兑换vip间隔时间是否启用
		int checkTime = rule.check_time;// 兑换vip间隔多长时间（月）
		
		// 判断用户申请的间隔时间，是否可以再次申请兑换
		if(isCheck > 0){
			QueryParam param = QueryParam.getInstance();
			param.addParam("user.userId", userId);
			param.addParam("scoreTypeNid", ScoreTypeConstant.SCORE_CONVERT_VIP);
			List<ScoreConvert> convertList = scoreConvertDao.findByCriteria(param);
			for(ScoreConvert item : convertList){
				if(item != null && item.getStatus() == ScoreConstant.WAIT_AUDIT){
					return false;
				}else if(item != null && item.getStatus() == ScoreConstant.PASS_AUDIT){// 如果已经有申请通过的，判断审核时间是否在规则限定时间外
					// 现在时间像后退约束的月份，得到VIP可以申请的开始时间
					String vipStartTime = DateUtil.getTimeStr(DateUtil.rollMon(DateUtil.getDate(DateUtil.getNowTimeStr()),-checkTime));
					long startTime = StringUtil.toLong(vipStartTime);
					if(item.getVerifyTime() == null){// 如果已审核，审核时间为空，则返回false
						return false;	
					}
					
					long verifyTime = item.getVerifyTime().getTime() / 1000;
					if(verifyTime >= startTime) {// 如果有VIP审核时间是大于VIP可以申请的开始时间，则本次申请无效
						return false;
					}
				}
			}
		}
		Score userScore =  scoreDao.findObjByProperty("user.userId",	userId);
		// 如果兑换积分小于有效积分,或者有效积分小于兑换VIP规则配置需要的积分，或者规则需要积分和实际积分不相等，则return
		if(userScore.getValidScore() < value || userScore.getValidScore() < score || value != score){
			return false;
		}
		return true;
	}
}
