package com.rongdu.p2psys.core.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.dao.MenuDao;
import com.rongdu.p2psys.core.domain.Menu;

@Repository
public class MenuDaoImpl extends BaseDaoImpl<Menu> implements MenuDao {

	public List<Menu> menuList(boolean isDelete) {
		String jpql = "from Menu where open_type = " + Menu.getCurrOpenType() + " AND isDelete = ?1";
		Query query = em.createQuery(jpql);
		query.setParameter(1, isDelete);
		List list = query.getResultList();
		return list;
	}

	public List<Menu> menuFindByPid(long pid) {
		String jpql = "from Menu where open_type = " + Menu.getCurrOpenType() + " AND parent_id = ?1";
		Query query = em.createQuery(jpql);
		query.setParameter(1, pid);
		List list = query.getResultList();
		return list;
	}

	public void update(long pid) {
		Query query = em.createNativeQuery("update s_menu set is_delete=1 where open_type = " + Menu.getCurrOpenType() + " AND  parent_id=?1").setParameter(1, pid);
		int num = query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Menu> menuUseList(long userId, boolean isMenu) {
		StringBuffer sql = new StringBuffer("SELECT p4.* FROM s_menu p4 WHERE EXISTS( SELECT NULL from");
		sql.append(" s_operator_role p1,s_role p2, s_role_menu p3 where p1.role_id = p2.id AND p1.role_id = p3.role_id");
		sql.append(" AND p2.id = p3.role_id AND p3.menu_id = p4.id AND p2.is_delete = 0 AND p4.is_delete = 0 AND p4.open_type = " + Menu.getCurrOpenType());
		sql.append(" AND p1.operator_id = ?1)");
		if(isMenu){
		    sql.append(" AND p4.is_menu = ?2");
		}
		sql.append(" order by sort asc");
		Query query = em.createNativeQuery(sql.toString(), Menu.class);
		query.setParameter(1, userId);
		if(isMenu){
		    query.setParameter(2, isMenu);
		}
		List<Menu> list = query.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Menu getMenuPermission(long operatorId, String href) {
		StringBuffer sql = new StringBuffer("SELECT p4.* FROM s_menu p4 WHERE EXISTS( SELECT NULL from");
		sql.append(" s_operator_role p1,s_role p2,s_role_menu p3 where p1.role_id = p2.id AND p1.role_id = p3.role_id");
		sql.append(" AND p2.id = p3.role_id AND p3.menu_id = p4.id AND p2.is_delete = 0 AND p4.is_delete = 0 AND p4.open_type = " + Menu.getCurrOpenType());
		sql.append(" AND p1.operator_id = ?1)");
		if (href != null && href.length() > 0) {
			sql.append(" AND p4.href = ?2");
		}
		sql.append(" order by sort asc");
		Query query = em.createNativeQuery(sql.toString(), Menu.class);
		query.setParameter(1, operatorId);
		if (href != null && href.length() > 0) {
			query.setParameter(2, href);
		}
		List<Menu> menuList = query.getResultList();
		if (menuList != null && menuList.size() > 0) {
			return menuList.get(0);
		}
		return null;
	}

	@Override
	public List<Menu> getMenuUseList(long userId, long parentId, boolean isMenu) {
		StringBuffer sql = new StringBuffer("SELECT p4.* FROM s_menu p4 WHERE EXISTS( SELECT NULL from");
		sql.append(" s_operator_role p1,s_role p2, s_role_menu p3 where p1.role_id = p2.id AND p1.role_id = p3.role_id");
		sql.append(" AND p2.id = p3.role_id AND p3.menu_id = p4.id AND p2.is_delete = 0 AND p4.is_delete = 0 AND p4.open_type = " + Menu.getCurrOpenType());
		sql.append(" AND p1.operator_id = ?1 AND p4.parent_id = ?2 )");
		if(isMenu){
		    sql.append(" AND p4.is_menu = ?3");
		}
		sql.append(" order by sort asc");
		Query query = em.createNativeQuery(sql.toString(), Menu.class);
		query.setParameter(1, userId);
		query.setParameter(2, parentId);
		if(isMenu){
		    query.setParameter(3, isMenu);
		}
		
		List<Menu> list = query.getResultList();
		return list;
	}
}
