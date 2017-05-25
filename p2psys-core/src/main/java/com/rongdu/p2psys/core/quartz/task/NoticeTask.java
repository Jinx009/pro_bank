package com.rongdu.p2psys.core.quartz.task;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.quartz.AbstractLoanTask;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.util.BeanUtil;
/**
 * 
* @Title: NoticeTask.java
* @Package com.rongdu.p2psys.core.quartz.task
* @Description: 短信，邮件，站内信task业务处理类
* @author Moon.Liu
* @date 2014年12月2日 上午10:55:31
* @version V2.0
 */
public class NoticeTask extends AbstractLoanTask {
	private Logger logger = Logger.getLogger(NoticeTask.class);


	public NoticeTask() {
		super();
		task.setName("NoticeTask");
	}

	@Override
	public void doLoan() {
		logger.debug("NoticeTask start");
		JobQueue <Notice> noticeQueue =JobQueue.getNoticeInstance();
		final  NoticeService noticeService = (NoticeService)BeanUtil.getBean("noticeService");
		while (!noticeQueue.isEmpty()) {
			Notice notice = noticeQueue.peek();
			if (notice != null) {
				try {
					noticeService.sendNotice(notice);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					noticeQueue.remove(notice);
				}
			}
		}
	}

	@Override
	public Object getLock() {
		return NoticeTask.MESSAGE_STATUS;
	}

}
