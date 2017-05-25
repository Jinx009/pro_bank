package com.rongdu.p2psys.score.model.scorelog.tender;

import com.rongdu.p2psys.score.constant.ScoreTemplateConstant;
import com.rongdu.p2psys.score.constant.ScoreTypeConstant;
import com.rongdu.p2psys.score.model.scorelog.BaseTenderScoreLog;

/**
 * 视频认证获得积分
 */
public class TenderScoreVideoLog  extends BaseTenderScoreLog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logTemplateNid = ScoreTemplateConstant.SCORE_VIDEO;
	
	public TenderScoreVideoLog() {
		super();
	}

	public TenderScoreVideoLog(long userId, int score) {
		super(userId, score, ScoreTypeConstant.SCORE_VIDEO);
		this.setLogTemplateNid(logTemplateNid);
	}
}
