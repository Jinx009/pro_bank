package com.rongdu.p2psys.core.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.NoticeConfig;

public interface NoticeConfigService {

	/**
	 * @param page
	 * @return
	 */
	PageDataList<NoticeConfig> list(int page);

	/**
	 * 新增
	 * 
	 * @param noticeConfig
	 */
	void save(NoticeConfig noticeConfig);

}
