package com.rongdu.p2psys.score.model.scorelog;



/**
 * 积分兑换父类
 * @author zhangyz
 * @version 1.0
 */
public class BaseScoreConvertLog extends BaseScoreLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -141858044155521465L;

	
	public BaseScoreConvertLog() {
		super();
	}
	
	public BaseScoreConvertLog(long userId,int score, String scoreTypeNid) {
		super(userId, score, scoreTypeNid);
	}
	
}
