package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.core.dao.VerifyLogDao;
import com.rongdu.p2psys.core.domain.VerifyLog;
import com.rongdu.p2psys.core.model.VerifyLogModel;
import com.rongdu.p2psys.core.service.VerifyLogService;

@Service("verifyLogService")
public class VerifyLogServiceImpl implements VerifyLogService {

	@Resource
	private VerifyLogDao verifyLogDao;

	@Override
	public void save(VerifyLog verifyLog) {
		verifyLogDao.save(verifyLog);
	}

	@Override
	public VerifyLog findByType(long fid, String type, int verifyType) {
		return verifyLogDao.findByType(fid, type, verifyType);
	}

	@Override
	public List<VerifyLogModel> list(String type, long fid) {
		
		return verifyLogDao.list(type, fid);
	}

}
