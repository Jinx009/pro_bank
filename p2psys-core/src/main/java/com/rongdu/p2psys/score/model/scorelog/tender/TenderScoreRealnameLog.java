package com.rongdu.p2psys.score.model.scorelog.tender;

import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.score.constant.ScoreTemplateConstant;
import com.rongdu.p2psys.score.constant.ScoreTypeConstant;
import com.rongdu.p2psys.score.exception.ScoreException;
import com.rongdu.p2psys.score.model.scorelog.BaseTenderScoreLog;


/**
 * 实名认证通过
 */
public class TenderScoreRealnameLog extends BaseTenderScoreLog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logTemplateNid = ScoreTemplateConstant.SCORE_REAL_NAME;
	
	public TenderScoreRealnameLog() {
		super();
	}

	public TenderScoreRealnameLog(long userId) {
		super(userId, 0, ScoreTypeConstant.SCORE_REAL_NAME);
		int score = this.getScoreValue(ScoreTypeConstant.SCORE_REAL_NAME);
		this.setScore(score);
		Global.setTransfer("score", score);
		this.setLogTemplateNid(logTemplateNid);
	}
	
	@Override
	public void modifyScore() {
		Boolean result = scoreDao.updateScore(this.getUser().getUserId(), this.getScore(), this.getScore(), 0, 0);
		if(!result){
			throw new ScoreException("更新用户积分失败！", 1);
		}
	}
}
