package com.rongdu.p2psys.core.service;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.core.domain.OperationLog;

/**
 * 操作日志
 * 
 * @author cx
 * @version 1.0
 * @since 2014-5-15
 */
public interface OperationLogService {

	/**
	 * @param log
	 */
	void save(OperationLog log);

    void save(OperationLog log, Borrow borrow);

}
