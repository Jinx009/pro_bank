package com.rongdu.p2psys.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.core.dao.ParentOperatorDao;
import com.rongdu.p2psys.core.domain.ParentOperator;
import com.rongdu.p2psys.core.service.ParentOperatorService;

@Service("parentOperatorService")
public class ParentOperatorServiceImpl implements ParentOperatorService{
	@Resource
	private ParentOperatorDao parentOperatorDao;
	
	@Override
	public ParentOperator getParentOperator(long c_operator_id) {
		return parentOperatorDao.findObjByProperty("c_operator.id", c_operator_id);
	}

	@Override
	public void modify(ParentOperator operator) {
		parentOperatorDao.merge(operator);
	}

	@Override
	public void add(ParentOperator operator) {
		parentOperatorDao.save(operator);
	}
	
}
