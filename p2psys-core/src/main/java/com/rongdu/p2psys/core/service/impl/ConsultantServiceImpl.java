package com.rongdu.p2psys.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.ConsultantDao;
import com.rongdu.p2psys.core.domain.Consultant;
import com.rongdu.p2psys.core.service.ConsultantService;

/**
 * 顾问专家
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年4月13日
 */
@Service("consultantService")
public class ConsultantServiceImpl implements ConsultantService {
	@Resource
	private ConsultantDao consultantDao;
	
	@Override
	public Consultant save(Consultant consultant) {

		return consultantDao.save(consultant);
	}

	@Override
	public PageDataList<Consultant> getAllList(int pageNumber, int pageSize,
			Consultant consultant) {
		QueryParam param = QueryParam.getInstance().addPage(pageNumber, pageSize).addParam("isDelete", 0);
		return consultantDao.findAllPageList(param);
	}

	@Override
	public Consultant update(Consultant consultant) {
		
		return consultantDao.update(consultant);
	}

	@Override
	public Consultant getByid(long id) {
		
		return consultantDao.find(id);
	}

}
