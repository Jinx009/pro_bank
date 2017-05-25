package com.rongdu.p2psys.core.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Log;
import com.rongdu.p2psys.core.model.LogModel;

/**
 * 系统操作日志DAO接口
 * 
 * @author wujing
 * @version 1.0
 * @since 2014-04-03
 */
public interface LogDao extends BaseDao<Log> {

	/**
	 * 查询系统操作日志
	 * 
	 * @param id 主键ID
	 * @return系统操作日志实体
	 */
	Log getLogById(long id);

	/**
	 * 系统操作日志分页
	 * 
	 * @param model 查询参数
	 * @return 分页
	 */
	public PageDataList<LogModel> list(LogModel model);
}
