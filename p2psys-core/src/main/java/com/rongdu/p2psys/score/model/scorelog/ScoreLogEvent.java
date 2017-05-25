package com.rongdu.p2psys.score.model.scorelog;


/**
 * 
 * 实现系统积分日志监听操作
 * 
 * @author zhangyz
 * @version 1.0
 */
public interface ScoreLogEvent {

	/**
	 * 日志事件执行
	 */
	void doEvent();
	
	/**
	 * 修改积分操作
	 */
	void modifyScore();
	
	/**
	 * 添加操作日志
	 */
	void addOperateLog();
	
	/**
	 * 添加积分日志
	 */
	void addScoreLog();
	
}
