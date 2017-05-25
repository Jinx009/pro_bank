package com.rongdu.p2psys.core.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.dao.NoticeConfigDao;
import com.rongdu.p2psys.core.domain.NoticeConfig;
import com.rongdu.p2psys.core.service.NoticeConfigService;

/**
 * description 借款标到期提醒
 * 
 * @author zxc
 * @date 2013-4-12
 * @version <b>Copyright (c)</b> 2012-51融都-版权所有<br/>
 */
@Service("noticeConfigService")
public class NoticeConfigServiceImpl implements NoticeConfigService {
	private static Logger logger = Logger.getLogger(NoticeConfigServiceImpl.class);

	@Resource
	private NoticeConfigDao noticeConfigDao;

	/**
	 * 通知配置列表
	 */
	@Override
	public PageDataList<NoticeConfig> list(int page) {
		return noticeConfigDao.list(page);
	}

	@Override
	public void save(NoticeConfig noticeConfig) {
		noticeConfigDao.save(noticeConfig);
	}
}
