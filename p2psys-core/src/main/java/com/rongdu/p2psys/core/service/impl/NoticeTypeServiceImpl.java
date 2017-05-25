package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.dao.NoticeTypeDao;
import com.rongdu.p2psys.core.domain.NoticeType;
import com.rongdu.p2psys.core.service.NoticeTypeService;

@Service("noticeTypeService")
public class NoticeTypeServiceImpl implements NoticeTypeService {

	@Resource
	private NoticeTypeDao noticeTypeDao;

	@Override
	public PageDataList<NoticeType> noticeTypeList(int page) {
		return noticeTypeDao.noticeTypeList(page);
	}

	@Override
	public void update(NoticeType noticeType) {
		noticeTypeDao.update(noticeType);
	}

	@Override
	public List<NoticeType> getAllSendNoticeType() {
		return noticeTypeDao.findAllSend();
	}

	@Override
	public NoticeType getNoticeTypeByNid(String nid, int notice_type) {
		return noticeTypeDao.findByNid(nid, notice_type);
	}

	@Override
	public List<NoticeType> list() {
		return noticeTypeDao.list();
	}

	@Override
	public PageDataList<NoticeType> list(int page, int rows, NoticeType noticeType) {
		return noticeTypeDao.list(page, rows, noticeType);
	}

	@Override
	public NoticeType findById(long id) {
		return noticeTypeDao.find(id);
	}

	@Override
	public void add(NoticeType noticeType) {
		noticeTypeDao.save(noticeType);
	}

}
