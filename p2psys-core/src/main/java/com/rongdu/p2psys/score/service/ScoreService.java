package com.rongdu.p2psys.score.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.model.ScoreModel;

public interface ScoreService {

	/**
	 * 会员积分查询分页.
	 * @param model 查询条件分装类
	 * @return page
	 */
	public PageDataList<ScoreModel> getScorePage(ScoreModel model);

	/**
	 * 根据会员ID修改会员有效积分
	 * @param userId 会员ID 必传参数
	 * @param value 操作积分，为正数，可选参数，不传，则根据NID类型操作积分
	 * @param nid 积分类型(rd_score_type)必传参数
	 * 注：value 和 status可选参数，两者同时传或同时不传
	 */
	public Boolean updateScore(long userId, int value, String nid);

	/**
	 * 根据会员ID查询会员的积分信息
	 * @param user_id 会员ID
	 * @return Score
	 */
	public Score getScoreByUserId(long userId);

	/**
	 * 根据会员ID查询该会员的积分
	 * @param user_id 会员ID
	 * @param type 查询积分类型：1投标积分,2借款积分,3赠送积分,4消费积分,5可用积分,6总积分
	 * @return 积分
	 */
	public int getCreditValueByUserId(long userId , Byte type);
	
	/**
	 * 用户积分修改
	 * @param Score
	 * @return
	 */
	public Boolean updateScore(Score Score);
}
