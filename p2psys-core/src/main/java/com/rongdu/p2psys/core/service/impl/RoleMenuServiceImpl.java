package com.rongdu.p2psys.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.dao.RoleMenuDao;
import com.rongdu.p2psys.core.domain.RoleMenu;
import com.rongdu.p2psys.core.service.RoleMenuService;

@Service("roleMenuService")
public class RoleMenuServiceImpl implements RoleMenuService {

	@Resource
	private RoleMenuDao roleMenuDao;

	public List<RoleMenu> getRoleMenuList(long roleId) {
		QueryParam param = QueryParam.getInstance().addParam("roleId", roleId);
		return roleMenuDao.findByCriteria(param);
	}

}
