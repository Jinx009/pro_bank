package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.RoleMenuDao;
import com.rongdu.p2psys.core.domain.RoleMenu;

@Repository
public class RoleMenuDaoImpl extends BaseDaoImpl<RoleMenu> implements RoleMenuDao {

	public void deleteByRoleId(long roleId) {
		Query query = em.createNativeQuery("delete from s_role_menu where role_id=?1").setParameter(1, roleId);
		query.executeUpdate();
	}

	public List<RoleMenu> getRoleMenuList(long roleId) {
		return null;
	}
}
