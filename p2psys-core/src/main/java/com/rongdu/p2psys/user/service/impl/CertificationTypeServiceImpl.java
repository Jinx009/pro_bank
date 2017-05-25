package com.rongdu.p2psys.user.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.user.dao.CertificationTypeDao;
import com.rongdu.p2psys.user.domain.CertificationType;
import com.rongdu.p2psys.user.service.CertificationTypeService;

@Service
public class CertificationTypeServiceImpl implements CertificationTypeService {

	@Resource
	private CertificationTypeDao certificationTypeDao;

	@Override
	public List<CertificationType> findAll() {
		QueryParam param = QueryParam.getInstance().addParam("status", 1)
				.addOrder("sort");
		return certificationTypeDao.findByCriteria(param, 1, 5);
	}

	@Override
	public CertificationType findById(long id) {
		return certificationTypeDao.find(id);
	}
	
}
