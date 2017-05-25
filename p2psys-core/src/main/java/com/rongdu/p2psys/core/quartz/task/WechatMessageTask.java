package com.rongdu.p2psys.core.quartz.task;

import org.apache.log4j.Logger;

import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.quartz.AbstractLoanTask;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.service.NoticeService;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.nb.wechat.service.WechatCacheService;
import com.rongdu.p2psys.nb.wechat.util.WechatMessage;
/**
 * 
* @Title: WetchatMessageTask.java
* @Package com.rongdu.p2psys.core.quartz.task
* @Description: 微信推送消息task业务处理类
* @author Adolf Ye
* @date 2015年11月4日
* @version V2.0
 */
public class WechatMessageTask extends AbstractLoanTask {
	private Logger logger = Logger.getLogger(WechatMessageTask.class);


	public WechatMessageTask() {
		super();
		task.setName("WechatMessageTask");
	}

	@Override
	public void doLoan() {
		logger.debug("WechatMessageTask start");
		JobQueue <WechatMessage> wechatMessageQueue =JobQueue.getWechatMessageInstance();
		final  WechatCacheService wechatCacheService = (WechatCacheService)BeanUtil.getBean("wechatCacheService");
		while (!wechatMessageQueue.isEmpty()) {
			WechatMessage wechatMessage = wechatMessageQueue.peek();
			if (wechatMessage != null) {
				try {
					wechatCacheService.sendWechatMessage(wechatMessage);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					wechatMessageQueue.remove(wechatMessage);
				}
			}
		}
	}

	@Override
	public Object getLock() {
		return WechatMessageTask.MESSAGE_STATUS;
	}

}
