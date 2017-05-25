package com.rongdu.p2psys.core.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.LogTemplate;

/**
 * 日志模板处理DAO
 * 
 * @author zhangyz
 * @version 1.0
 * @since 2013-10-11
 */
public interface LogTemplateDao extends BaseDao<LogTemplate> {

	/**
	 * 获得日志模板列表数据
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param logTemplate
	 * @return
	 */
	PageDataList<LogTemplate> logTemplateList(int pageNumber, int pageSize, LogTemplate logTemplate);

}
