package com.rongdu.p2psys.score.model.scorelog.tender;

import com.rongdu.p2psys.score.constant.ScoreTemplateConstant;
import com.rongdu.p2psys.score.constant.ScoreTypeConstant;
import com.rongdu.p2psys.score.model.scorelog.BaseTenderScoreLog;


public class TenderScoreSceneLog  extends BaseTenderScoreLog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logTemplateNid = ScoreTemplateConstant.SCORE_SCENE;
	
	public TenderScoreSceneLog() {
		super();
	}

	public TenderScoreSceneLog(long userId, int score) {
		super(userId, score, ScoreTypeConstant.SCORE_SCENE);
		this.setLogTemplateNid(logTemplateNid);
	}
}
