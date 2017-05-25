package com.rongdu.p2psys.user.service;

import java.util.List;

import com.rongdu.p2psys.user.domain.CertificationType;

public interface CertificationTypeService {

	List<CertificationType> findAll();

	CertificationType findById(long id);
}
