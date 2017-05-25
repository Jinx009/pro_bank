package com.rongdu.p2psys.core.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.dao.LogDao;
import com.rongdu.p2psys.core.domain.Log;
import com.rongdu.p2psys.core.model.LogModel;
import com.rongdu.p2psys.core.service.LogService;

/**
 * 系统操作日志Service
 * 
 * @author wujing
 * @version 1.0
 * @since 2014-04-03
 */
@Service("logService")
@Transactional
public class LogServiceImpl implements LogService {

	@Resource
	private LogDao logDao;

	@Override
	public Log getLogById(long id) {
		return logDao.getLogById(id);
	}

	@Override
	public PageDataList<LogModel> list(LogModel model) {
		return logDao.list(model);
	}

	@Override
	public void addLog(Log log) {
		log.setAddTime(new Date());
		logDao.save(log);
	}
}
