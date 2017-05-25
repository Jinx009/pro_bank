package com.rongdu.p2psys.score.model.scorelog.convert;

import com.rongdu.p2psys.score.constant.ScoreTemplateConstant;
import com.rongdu.p2psys.score.constant.ScoreTypeConstant;
import com.rongdu.p2psys.score.exception.ScoreException;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreConvertLog;

public class ScoreConvertVipSuccessLog extends BaseScoreConvertLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876174264156514032L;
	
	private String logTemplateNid = ScoreTemplateConstant.CONVERT_VIP_SUCCESS;
	
	public ScoreConvertVipSuccessLog() {
		super();
	}

	public ScoreConvertVipSuccessLog(long userId, int score) {
		super(userId, score, ScoreTypeConstant.SCORE_CONVERT_VIP);
		this.setLogTemplateNid(logTemplateNid);
	}

	@Override
	public void modifyScore() {
		Boolean result = scoreDao.updateScore(this.getUser().getUserId(), 0, this.getScore(), 0, -this.getScore());
		if(!result){
			throw new ScoreException("更新用户积分失败！", 1);
		}
	}
	
}
