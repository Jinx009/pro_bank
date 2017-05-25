package com.rongdu.p2psys.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.borrow.domain.Borrow;
import com.rongdu.p2psys.core.dao.OperationLogDao;
import com.rongdu.p2psys.core.domain.OperationLog;
import com.rongdu.p2psys.core.service.OperationLogService;

@Service("operationLogService")
public class OperationLogServiceImpl implements OperationLogService {

	@Resource
	private OperationLogDao operationLogDao;

	public void save(OperationLog log) {
		operationLogDao.save(log);
	}

    @Override
    public void save(OperationLog log, Borrow borrow) {

        operationLogDao.save(log);
    }
}
