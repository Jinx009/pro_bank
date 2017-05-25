package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.NoticeType;

/**
 * 通知配置dao
 * 
 * @author cx
 * @version 2.0
 * @since 2014-4-24
 */
public interface NoticeTypeDao extends BaseDao<NoticeType> {

	/**
	 * 
	 * @return
	 */
	List<NoticeType> list();

	/**
	 * 
	 * @param page
	 * @return
	 */
	PageDataList<NoticeType> noticeTypeList(int page);

	/**
	 * 
	 * @param noticeType
	 * @return
	 */
	int modify(NoticeType noticeType);

	/**
	 * 
	 * @return
	 */
	List<NoticeType> findAllSend();

	/**
	 * 
	 * @param nid
	 * @param notice_type
	 * @return
	 */
	NoticeType findByNid(String nid, int notice_type);

	/**
	 * 后台通知列表
	 * 
	 * @param noticeType
	 * @return
	 */
	PageDataList<NoticeType> list(int page, int rows, NoticeType noticeType);
}
