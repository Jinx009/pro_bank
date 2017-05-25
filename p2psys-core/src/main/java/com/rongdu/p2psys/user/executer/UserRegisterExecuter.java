package com.rongdu.p2psys.user.executer;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.core.constant.NoticeConstant;
import com.rongdu.p2psys.core.executer.BaseExecuter;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.sms.sendMsg.BaseMsg;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.score.domain.Score;
import com.rongdu.p2psys.score.model.scorelog.BaseScoreLog;
import com.rongdu.p2psys.score.model.scorelog.tender.TenderUserRegisterLog;
import com.rongdu.p2psys.score.service.ScoreService;

/**
 * 用户注册；2.0标准业务处理
 * 
 * @author zxc
 */
public class UserRegisterExecuter extends BaseExecuter {

	Logger logger = Logger.getLogger(UserRegisterExecuter.class);
	NoticeService noticeService;
	ScoreService scoreService;

	@Override
	public void prepare() {
		// 初始化dao、service
		noticeService = (NoticeService) BeanUtil.getBean("noticeService");
		scoreService = (ScoreService) BeanUtil.getBean("scoreService");
	}

	@Override
	public void addAccountLog() {
	}

	@Override
	public void handleAccount() {

	}

	@Override
	public void handleAccountSum() {
	}

	@Override
	public void handlePoints() {
	    // 注册成功送积分
	    BaseScoreLog bLog = new TenderUserRegisterLog(user.getUserId());
	    Score score = scoreService.getScoreByUserId(user.getUserId()) ;
	    if (score.getTotalScore() == 0){
	        bLog.doEvent();
	    }
	}

	/**
	 * 重新写发送通知的方法
	 */
	@Override
	public void handleNotice() {
		BaseMsg msg = new BaseMsg(NoticeConstant.NOTICE_EMAIL_ACTIVE);
		msg.doEvent();
	}

	@Override
	public void addOperateLog() {
	}

	@Override
	public void handleInterface() {
	}

	@Override
	public void extend() {
	}

}
