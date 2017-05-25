package com.rongdu.p2psys.score.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.score.domain.Score;

public interface ScoreDao extends BaseDao<Score> {
	
	/**
	 * 修改用户积分
	 * @param userId 用户ID
	 * @param totalScore 总积分
	 * @param validScore 有效积分
	 * @param expenseScore 消费积分
	 * @param freezeScore 冻结积分
	 * @return 修改是否成功
	 */
	public boolean updateScore(long userId, int totalScore, int validScore, int expenseScore, int freezeScore);
	
	/**
	 * 根据用户ID查询用户积分信息
	 * @param userId 用户ID
	 * @return 用户积分信息
	 */
	public Score getScoreByUserId(long userId);
}
