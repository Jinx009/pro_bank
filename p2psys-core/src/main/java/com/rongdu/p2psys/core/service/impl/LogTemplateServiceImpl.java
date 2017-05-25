package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.LogTemplateDao;
import com.rongdu.p2psys.core.domain.LogTemplate;
import com.rongdu.p2psys.core.service.LogTemplateService;

@Service("logTemplateService")
public class LogTemplateServiceImpl implements LogTemplateService {

	@Resource
	private LogTemplateDao logTemplateDao;

	@Override
	public PageDataList<LogTemplate> logTemplateList(int pageNumber, int pageSize, LogTemplate logTemplate) {
		return logTemplateDao.logTemplateList(pageNumber, pageSize, logTemplate);
	}

	@Override
	public void logTemplateAdd(LogTemplate logTemplate) {
		logTemplateDao.save(logTemplate);
	}

	@Override
	public LogTemplate logTemplateEditPage(long id) {
		return logTemplateDao.find(id);
	}

	@Override
	public LogTemplate logTemplateEdit(LogTemplate logTemplate) {
		return logTemplateDao.update(logTemplate);
	}
	
	@Override
	public List logTemplateList(QueryParam param) {
		return logTemplateDao.findByCriteria(param);
	}

}
