package com.rongdu.p2psys.score.model.scorelog;

import com.rongdu.p2psys.score.domain.ScoreType;

/**
 * 投资积分父类
 */
public class BaseTenderScoreLog extends BaseScoreLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -141858044155521465L;

	
	public BaseTenderScoreLog() {
		super();
	}
	
	public BaseTenderScoreLog(long userId,int score, String scoreTypeNid) {
		super(userId, score, scoreTypeNid);
	}
	
	/**
	 * 根据ScoreType配置信息，得到认证获得的积分
	 * @param typeNid
	 * @return
	 */
	public int getScoreValue(String typeNid){
		ScoreType type = scoreTypeDao.getScoreTypeByNid(typeNid);
		return type.getValue();
	}
}
