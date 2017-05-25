package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.model.jpa.SearchFilter.Operators;
import com.rongdu.common.util.StringUtil;
import com.rongdu.p2psys.core.dao.RoleDao;
import com.rongdu.p2psys.core.domain.Role;
import com.rongdu.p2psys.core.model.RoleModel;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {

	public PageDataList<Role> list(RoleModel model) {
		QueryParam param = QueryParam.getInstance().addPage(model.getPage(), model.getRows()).addParam("isDelete", 0);
		if (StringUtil.isNotBlank(model.getName())) {
			param.addParam("name", Operators.LIKE, model.getName());
		}
		return findPageList(param);
	}

	@SuppressWarnings("unchecked")
	public List<Role> roleList() {
		String jpql = "from Role where isDelete = ?1";
		Query query = em.createQuery(jpql);
		query.setParameter(1, false);
		List<Role> list = query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	public Role roleFind(long id) {
		String jpql = "from Role where isDelete = ?1 and id=?2";
		Query query = em.createQuery(jpql);
		query.setParameter(1, false);
		query.setParameter(2, id);
		List<Role> list = query.getResultList();
		if (list != null && list.size() >= 0) {
			return (Role) list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Role> getRoleList(long userId) {
		StringBuffer sql = new StringBuffer("SELECT p1.* from s_role p1 , s_operator_role p2 where");
		sql.append(" p1.id = p2.role_id and p2.operator_id = ?1 and p1.is_delete = ?2");
		Query query = em.createNativeQuery(sql.toString(), Role.class);
		query.setParameter(1, userId);
		query.setParameter(2, false);
		List<Role> list = query.getResultList();
		return list;
	}
}
