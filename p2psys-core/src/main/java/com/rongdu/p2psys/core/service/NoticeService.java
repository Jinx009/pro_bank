package com.rongdu.p2psys.core.service;

import java.util.Map;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Notice;
import com.rongdu.p2psys.core.domain.NoticeModel;

public interface NoticeService {

	/**
	 * 生成通知的对外接口，用于生成通知对象，事件生产者调用
	 * 
	 * @param noticeTypeNid
	 * @param sendData
	 */
	void sendNotice(String noticeTypeNid, Map<String, Object> sendData);

	/**
	 * 发送通知的对外接口，用于定时器调用，实际发送通知
	 * 
	 * @param notice
	 */
	void sendNotice(Notice notice);

	/**
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageDataList<NoticeModel> noticeList(NoticeModel model, int pageNumber, int pageSize);

	/**
	 * @param id
	 * @return
	 */
	Notice findById(long id);

	Notice findByOrderId(Integer id);

}
