package com.rongdu.p2psys.score.model.scorelog;

import java.util.Date;

import com.rongdu.common.util.FreemarkerUtil;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.dao.RuleDao;
import com.rongdu.p2psys.core.domain.LogTemplate;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.score.dao.ScoreDao;
import com.rongdu.p2psys.score.dao.ScoreLogDao;
import com.rongdu.p2psys.score.dao.ScoreTypeDao;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.domain.ScoreLog;
import com.rongdu.p2psys.score.domain.ScoreType;
import com.rongdu.p2psys.user.dao.UserDao;
import com.rongdu.p2psys.user.domain.User;

/**
 * 抽象积分日志类
 * @version 1.0
 */
public class BaseScoreLog extends ScoreLog implements ScoreLogEvent {
	
	private static final long serialVersionUID = 8585423859178082248L;

	protected UserDao userDao;
	
	protected RuleDao ruleDao;
	
	protected ScoreDao scoreDao;
	
	protected ScoreLogDao scoreLogDao;
	
	protected ScoreTypeDao scoreTypeDao;
	
	// 积分日志模板编码
	protected String logTemplateNid;
	
	public BaseScoreLog() {
		super();
		userDao = (UserDao)BeanUtil.getBean("userDao");
		ruleDao = (RuleDao)BeanUtil.getBean("ruleDao");
		scoreDao = (ScoreDao)BeanUtil.getBean("scoreDao");
		scoreLogDao = (ScoreLogDao)BeanUtil.getBean("scoreLogDao");
		scoreTypeDao = (ScoreTypeDao)BeanUtil.getBean("scoreTypeDao");
	}
	
	public BaseScoreLog(long userId,int score, String scoreTypeNid) {
		this();
		this.setScore(score);
		this.setUser(new User(userId));
		this.setScoreTypeNid(scoreTypeNid);
	}
	
	/**
	 * 默认的事件执行
	 */
	@Override
	public void doEvent() {
		// 修改积分
		this.modifyScore();
		// 添加积分日志
		this.addScoreLog();
		//操作日志
		this.addOperateLog();
	}

	/**
	 * 添加积分日志
	 */
	@Override
	public void addScoreLog() {
		//积分日志模板
		Score item = scoreDao.getScoreByUserId(this.getUser().getUserId());
		ScoreType type = scoreTypeDao.getScoreTypeByNid(this.getScoreTypeNid());
		ScoreLog log = new ScoreLog(item);
		log.setRemark(this.getLogRemark());
		log.setScoreTypeName(type.getName());
		log.setType(this.getLogType());
		log.setScoreTypeNid(this.getScoreTypeNid());
		log.setUser(new User(this.getUser().getUserId()));
		log.setScore(this.getScore());
		log.setAddTime(new Date());
		scoreLogDao.save(log);
	}
	
	public String getLogRemark(){
		try {
			String logRemarkTemplate = Global.getLogTempValue(LogTemplate.SCORE_LOG, logTemplateNid);
			return FreemarkerUtil.renderTemplate(logRemarkTemplate, Global.getTransfer());
		} catch (Exception e) {
		}
		return "";
	}
	
	public String getLogType() {
		return Global.getLogType(LogTemplate.SCORE_LOG, logTemplateNid);
	}
	
	public void setLogTemplateNid(String logTemplateNid) {
		this.logTemplateNid = logTemplateNid;
	}

	@Override
	public void addOperateLog() {
		// TODO Auto-generated method stub
	}

	@Override
	public void modifyScore() {
		// TODO Auto-generated method stub
		
	}
}