package com.rongdu.p2psys.core.quartz.task;

import org.apache.log4j.Logger;

import com.rongdu.common.exception.BussinessException;
import com.rongdu.p2psys.core.Global;
import com.rongdu.p2psys.core.concurrent.ValueEvent;
import com.rongdu.p2psys.core.quartz.AbstractLoanTask;
import com.rongdu.p2psys.core.quartz.JobQueue;
import com.rongdu.p2psys.core.util.BeanUtil;
import com.rongdu.p2psys.ppfund.service.PpfundInService;

/**
 * PPfund资金管理产品队列
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月19日
 */
public class PpfundTask extends AbstractLoanTask {
	private Logger logger = Logger.getLogger(PpfundTask.class);

	public PpfundTask() {
		super();
		task.setName("ppfund.task");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doLoan() {

		JobQueue<ValueEvent> queue = JobQueue.getPpfundInstance();
		PpfundInService ppfundInService = (PpfundInService) BeanUtil.getBean("ppfundInService");

		while (!queue.isEmpty()) {
			ValueEvent event = queue.peek();
			if (event != null) {
				String result = "success";
				try {
					ppfundInService.ppfundIn(event.getPpfundInModel());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					if (e instanceof BussinessException) {// 业务异常，保存业务处理信息
						result = e.getMessage();
					} else {
						result = "系统异常，业务处理失败";
					}
				} finally {
					queue.remove(event);
				}
				if (event.getResultFlag() != null) {// 在需要保存系统处理信息的地方直接保存进来
					Global.RESULT_MAP.put(event.getResultFlag(), result);
				}
			}
		}
	}

	@Override
	public Object getLock() {
		return PpfundTask.PPFUND_STATUS;
	}
}
