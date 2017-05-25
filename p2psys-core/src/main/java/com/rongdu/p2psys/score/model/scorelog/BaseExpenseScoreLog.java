package com.rongdu.p2psys.score.model.scorelog;

/**
 * 消费积分父类
 * @author zhangyz
 * @version 1.0
 */
public class BaseExpenseScoreLog  extends BaseScoreLog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -141858044155521465L;

	
	public BaseExpenseScoreLog() {
		super();
	}
	
	public BaseExpenseScoreLog(long userId,int score, String scoreTypeNid) {
		super(userId, score, scoreTypeNid);
	}
	
}
