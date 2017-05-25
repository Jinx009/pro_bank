package com.rongdu.p2psys.core.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.OperationLog;

/**
 * 操作日志
 * 
 * @author cx
 * @version 2.0
 * @since 2014-4-22
 */
public interface OperationLogDao extends BaseDao<OperationLog> {
	/**
	 * 根据订单查询操作日志信息
	 * 
	 * @param orderNo
	 */
	OperationLog getOperationLogInfo(String orderNo);

}
