package com.rongdu.p2psys.core.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Log;
import com.rongdu.p2psys.core.model.LogModel;

/**
 * 系统操作日志Service
 * 
 * @author wujing
 * @version 1.0
 * @since 2014-04-03
 */
public interface LogService {

	/**
	 * 查询系统操作日志
	 * 
	 * @param id 主键ID
	 * @return 系统操作日志
	 */
	Log getLogById(long id);

	/**
	 * 系统操作日志分页
	 * 
	 * @param model 查询参数
	 * @return 分页
	 */
	PageDataList<LogModel> list(LogModel model);

	/**
	 * 添加日志
	 * 
	 * @param log 日志信息
	 */
	void addLog(Log log);

}
