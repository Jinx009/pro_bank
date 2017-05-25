package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.OperatorRoleDao;
import com.rongdu.p2psys.core.domain.Operator;
import com.rongdu.p2psys.core.domain.OperatorRole;
import com.rongdu.p2psys.core.domain.Role;
import com.rongdu.p2psys.core.service.OperatorRoleService;

@Service("OperatorRoleService")
public class OperatorRoleServiceImpl implements OperatorRoleService {

	@Resource
	private OperatorRoleDao userRoleDao;

	@Override
	public List<OperatorRole> getUserRoleList(long userId) {
		Operator operator = new Operator(userId);
		QueryParam param = QueryParam.getInstance().addParam("operator", operator);
		return userRoleDao.findByCriteria(param);
	}

	@Override
	public List<OperatorRole> getKefuList() {
		Role role = new Role(6);
		QueryParam param = QueryParam.getInstance().addParam("role", role);
		return userRoleDao.findByCriteria(param);
	}
	
	@Override
	public OperatorRole getOperatorRole(int customerUserId) {
		return userRoleDao.findObjByProperty("operator.id", customerUserId);
	}

	@Override
	public PageDataList<OperatorRole> getOperatorRole(QueryParam param) {
		return userRoleDao.findPageList(param);
	}

}
