package com.rongdu.p2psys.score.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.score.domain.ScoreConvert;
import com.rongdu.p2psys.score.model.ScoreConvertModel;

public interface ScoreConvertService {

	/**
	 * 积分兑换现金方法
	 * @param scoreConvert 兑换参数
	 */
	public boolean scoreConvertMoney(ScoreConvert scoreConvert);
	
	/**
	 * 积分兑换VIP方法
	 * @param scoreConvert 兑换参数
	 */
	public boolean scoreConvertVIP(ScoreConvert scoreConvert);
	
	/**
	 * 积分兑换分页接口
	 * @param page分页开始页
	 * @param p查询参数
	 * @param status 查询信息状态
	 * @param user_id 会员ID
	 * @return
	 */
	public PageDataList<ScoreConvertModel> getCreditConvertPage(ScoreConvertModel model);
	
	
	/**
	 * 新增积分兑换信息
	 * @param scoreConvert
	 * @return
	 */
	public void insertScoreConvert(ScoreConvert scoreConvert);
	
	/**
	 * 根据主键ID查询积分兑换信息
	 * @param id
	 * @return
	 */
	public ScoreConvert getScoreConvertById(Long id);
	
	
	/**
	 * 积分兑换审核
	 * @param model
	 * @return
	 */
	public boolean auditScoreConvertMoney(ScoreConvertModel model);
	
	/**
	 * 积分兑换VIP审核
	 * @param scoreConvert
	 * @return
	 */
	public boolean auditScoreConvertVip(ScoreConvertModel model);
	
	/**
	 * 根据主键userId和类型查询积分兑换信息
	 * @param userId
	 * @param typeNid
	 * @return
	 */
	public List<ScoreConvertModel> getScoreConvert(long userId , String typeNid);
	
	/**
	 * 积分兑换VIP验证
	 * @param ruleNid 规则代码
	 * @param userId 用户ID
	 * @param value 兑换VIP积分
	 * @return
	 */
	public boolean checkConvertVipRule(String ruleNid, long userId, int value);
}
