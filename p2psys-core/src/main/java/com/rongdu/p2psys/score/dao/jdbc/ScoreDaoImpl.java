package com.rongdu.p2psys.score.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.score.dao.ScoreDao;
import com.rongdu.p2psys.score.domain.Score;

@Repository("scoreDao")
public class ScoreDaoImpl extends BaseDaoImpl<Score> implements ScoreDao {

	@Override
	public boolean updateScore(long userId, int totalScore, int validScore, int expenseScore, int freezeScore) {
		StringBuffer sql = new StringBuffer("UPDATE Score SET total_score = total_score + :totalScore,");
		sql.append(" valid_score = valid_score + :validScore, expense_score = expense_score + :expenseScore,");
		sql.append(" freeze_score = freeze_score + :freezeScore WHERE user_id = :userId AND");
		sql.append(" valid_score + :valid >= 0 AND freeze_score + :freeze >= 0");
		int result = this.updateByJpql(sql.toString(), 
				new String[]{"totalScore", "validScore", "expenseScore", "freezeScore", "userId", "valid", "freeze"}, 
				new Object[] {totalScore, validScore, expenseScore, freezeScore, userId, validScore, freezeScore});
		if (result < 1) {
			return false;
		}
		// 更新缓存
		em.refresh(getScoreByUserId(userId));
		return true;
	}

	@Override
	public Score getScoreByUserId(long userId) {
		return this.findObjByProperty("user.userId", userId);
	}
}
