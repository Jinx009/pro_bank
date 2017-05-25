package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.NoticeType;

/**
 * 通知配置类型service
 * 
 * @author cx
 * @version 2.0
 * @since 2014-4-24
 */
public interface NoticeTypeService {

	/**
	 * @param page
	 * @return
	 */
	PageDataList<NoticeType> noticeTypeList(int page);

	/**
	 * 修改保存
	 * 
	 * @param noticeType
	 */
	void update(NoticeType noticeType);

	/**
	 * @return
	 */
	List<NoticeType> getAllSendNoticeType();

	/**
	 * @param nid
	 * @param notice_type
	 * @return
	 */
	NoticeType getNoticeTypeByNid(String nid, int notice_type);

	/**
	 * @return
	 */
	List<NoticeType> list();

	/**
	 * 后台通知列表
	 * 
	 * @param noticeType
	 * @return
	 */
	PageDataList<NoticeType> list(int page, int rows, NoticeType noticeType);

	/**
	 * 通过Id查找
	 * 
	 * @param id
	 * @return
	 */
	NoticeType findById(long id);

	/**
	 * 新增
	 * 
	 * @param noticeType
	 */
	void add(NoticeType noticeType);

}
