package com.rongdu.common.dao.jpa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.common.util.Page;
import com.rongdu.common.util.ReflectUtil;
import com.rongdu.common.util.StringUtil;

public class BaseDaoImpl<T> implements BaseDao<T> {

	private static final Logger logger = Logger.getLogger(BaseDaoImpl.class);

	@PersistenceContext
	protected EntityManager em;
	@Resource(name = "entityManagerFactory")
	protected EntityManagerFactory emf;

	@SuppressWarnings("unchecked")
	protected Class<T> entityClass = ReflectUtil.getSuperClassGenricType(super.getClass());

	@Override
	public T update(T entity) {
		em.merge(entity);
		return entity;
	}

	@Override
	public boolean update(Collection<T> ts) {
		try {
			for (T t : ts) {
				if (t != null) {
					em.merge(t);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			String message = "数据库更新对象出错！";
			logger.error(message);
		}
		return false;
	}

	@Override
	public T save(T entity) {
		em.persist(entity);
		return entity;
	}

	@Override
	public void save(Collection<T> ts) {
		for (T t : ts) {
			save(t);
		}
	}

	@Override
	public T merge(T entity) {
		T t = em.merge(entity);
		return t;

	}

	@Override
	public void updateWithRefresh(T entity) {
		em.merge(entity);
	}

	@Override
	public void refresh(T entity) {
		em.refresh(entity);
	}

	@Override
	public void lock(T entity) {
		em.lock(entity, LockModeType.PESSIMISTIC_WRITE);
	}

	@Override
	public void delete(Serializable entityids) {
		delete(new Serializable[] { entityids });
	}

	@Override
	public void delete(Serializable[] entityids) {
		for (Object id : entityids)
			em.remove(em.getReference(this.entityClass, id));
	}

	@Override
	public void delete(Collection<T> ts) {
		for (T c : ts)
			em.remove(c);
	}

	@Override
	public void clear() {
		em.clear();
	}

	@Override
	public void detach(Collection<T> ts) {
		for (T c : ts)
			em.detach(c);
	}

	@Override
	public void detach(Serializable entityid) {
		em.detach(entityid);
	}

	public void flush() {
		em.flush();
	}

	@Override
	public T find(Class<T> entityClass, Object id) {
		return em.find(entityClass, id);
	}

	@Override
	public T find(Serializable entityId) {
		if (entityId == null)
			throw new RuntimeException(this.entityClass.getName() + ":传入的实体id不能为空");
		return em.find(this.entityClass, entityId);
	}

	@Override
	public T findWithLock(Serializable entityId) {
		T o = find(entityId);
		em.lock(o, LockModeType.PESSIMISTIC_WRITE);
		return null;
	}

	@Override
	public List<T> findAll() {
		CriteriaQuery<T> cq = (CriteriaQuery<T>) em.getCriteriaBuilder().createQuery(entityClass);
		cq.select((Selection<? extends T>) cq.from(entityClass));
		return em.createQuery(cq).getResultList();
	}

	public List<T> findByCriteria(QueryParam param) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = (CriteriaQuery<T>) builder.createQuery(entityClass);
		Root<T> from = (Root<T>) query.from(entityClass);
		Predicate[] p = param.bySearchFilter(entityClass, builder, query, from);
		Order[] orders = param.orderBy(builder, from);
		query.select(from).where(p);
		if (orders != null && orders.length > 0) {
			query.orderBy(orders);
		}
		TypedQuery<T> typedQuery = em.createQuery(query);
		List<T> results = typedQuery.getResultList();
		return results;
	}

	@Override
	public T findByCriteriaForUnique(QueryParam param) {
		List<T> list = findByCriteria(param);
		if (list == null || list.size() < 1) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<T> findByCriteria(QueryParam param, int start, int limit) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(entityClass);
		Root<T> from = (Root<T>) query.from(entityClass);
		Predicate[] p = param.bySearchFilter(entityClass, builder, query, from);
		Order[] orders = param.orderBy(builder, from);
		query.select(from).where(p);
		if (orders != null && orders.length > 0) {
			query.orderBy(orders);
		}
		TypedQuery<T> typedQuery = em.createQuery(query);
		typedQuery.setFirstResult(start).setMaxResults(limit);
		List<T> results = typedQuery.getResultList();
		return results;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int countByCriteria(QueryParam param) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root root = query.from(entityClass);
		query.select(root);
		Predicate[] p = param.bySearchFilter(entityClass, builder, query, root);
		query.where(p);
		query.select(builder.countDistinct(root));
		int count = em.createQuery(query).getSingleResult().intValue();
		return count;
	}

	@Override
	public List<T> findByProperty(String property, Object value) {
		QueryParam param = new QueryParam();
		param.addParam(property, value);
		return findByCriteria(param);
	}

	@Override
	public T findObjByProperty(String property, Object value) {
		List<T> list = findByProperty(property, value);
		if (list == null || list.size() < 1) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public PageDataList<T> findPageList(QueryParam param) {
		int count = countByCriteria(param);
		if (param.getPage() == null) {
			param.addPage(count, 1, Page.ROWS);
		}
		param.addPage(count, param.getPage().getCurrentPage(), param.getPage().getPernum());
		List<T> list = findByCriteria(param, param.getPage().getStart(), param.getPage().getPernum());
		return new PageDataList<T>(param.getPage(), list);
	}

	@Override
	public PageDataList<T> findAllPageList(QueryParam param) {
		List<T> list = findByCriteria(param);
		return new PageDataList<T>(param.getPage(), list);
	}

	@Override
	public PageDataList<T> findPageListBySql(String sql, QueryParam param) {
		return findPageListBySql(sql, param, entityClass);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <G> List<G> findBySql(String sql, QueryParam param, Class<G> clazz) {
		String dataSql = "select * " + sql + param.bySearchSqlFilter().toString() + param.byOrderSqlFilter().toString();
		Query query = em.createNativeQuery(dataSql, clazz);
		List list = query.getResultList();
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <G> PageDataList<G> findPageListBySql(String sql, QueryParam param, Class<G> clazz) {
		if (sql.indexOf("where") < 0) {
			sql = sql + " where 1=1 ";
		}
		String countSql = "select count(1) " + sql + param.bySearchSqlFilter().toString();
		Query query = em.createNativeQuery(countSql);
		int count = StringUtil.toInt(query.getSingleResult().toString());
		if (param.getPage() == null) {
			param.addPage(count, 1, Page.ROWS);
		}
		param.addPage(count, param.getPage().getCurrentPage(), param.getPage().getPernum());
		String dataSql = "select * " + sql + param.bySearchSqlFilter().toString() + param.byOrderSqlFilter().toString();
		query = em.createNativeQuery(dataSql, clazz);
		query.setFirstResult(param.getPage().getStart()).setMaxResults(param.getPage().getPernum());
		List list = query.getResultList();
		return new PageDataList<G>(param.getPage(), list);
	}

	@SuppressWarnings("rawtypes")
	/**
	 * 
	 * @param selectSql  查询参数sql
	 * @param formSql  from 后的基础参数
	 * @param param    param where 后的参数
	 * @param clazz    
	 * @return
	 */
	@Override
	public <G> PageDataList<G> findPageListBySql(String selectSql, String formSql, QueryParam param, Class<G> clazz) {
		if (formSql.indexOf("where") < 0) {
			formSql = formSql + " where 1=1 ";
		}
		String countSql = "select count(1) " + formSql + param.bySearchSqlFilter().toString();
		Query query = em.createNativeQuery(countSql);
		int count = StringUtil.toInt(query.getSingleResult().toString());
		if (param.getPage() == null) {
			param.addPage(count, 1, Page.ROWS);
		}
		param.addPage(count, param.getPage().getCurrentPage(), param.getPage().getPernum());
		String dataSql = selectSql + formSql + param.bySearchSqlFilter().toString()
				+ param.byOrderSqlFilter().toString();
		query = em.createNativeQuery(dataSql, clazz);
		query.setFirstResult(param.getPage().getStart()).setMaxResults(param.getPage().getPernum());
		List list = query.getResultList();
		return new PageDataList<G>(param.getPage(), list);
	}

	@Override
	public int updateBySql(String sql, String[] names, Object[] values) {
		Query query = em.createNativeQuery(sql);
		for (int i = 0; i < names.length; i++) {
			query.setParameter(names[i], values[i]);
		}
		return query.executeUpdate();
	}

	@Override
	public int updateByJpql(String jpql, String[] names, Object[] values) {
		Query query = em.createQuery(jpql);
		for (int i = 0; i < names.length; i++) {
			query.setParameter(names[i], values[i]);
		}
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> listByJpql(String jpql, String[] names, Object[] values) {
		Query query = em.createQuery(jpql);
		for (int i = 0; i < names.length; i++) {
			query.setParameter(names[i], values[i]);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> listMapBySql(String sql, String[] names, Object[] values) {
		Query query = em.createNativeQuery(sql);
		for (int i = 0; i < names.length; i++) {
			query.setParameter(names[i], values[i]);
		}
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> rows = query.getResultList();
		List<Map<String, Object>> newRows = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> row : rows) {
			Map<String, Object> newRow = new HashMap<String, Object>();
			for (String key : row.keySet()) {
				String newKey = StringUtil.clearUnderline(key);
				newRow.put(newKey, row.get(key));
			}
			newRows.add(newRow);
			row.clear();
		}
		rows.clear();
		return newRows;
	}

	private void setEntityProperty(Map<String, Object> rowMap, Map<String, Field> fieldMap, Object o) {
		Field field = null;
		for (String key : fieldMap.keySet()) {
			field = fieldMap.get(key);
			field.setAccessible(true);
			Object value = rowMap.get(key);
			try {
				field.set(o, value);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public <G> List<G> listBySql(String sql, String[] names, Object[] values, Class<G> clazz) {
		List<Map<String, Object>> rows = this.listMapBySql(sql, names, values);
		List<G> newRows = new ArrayList<G>(rows.size());
		for (int n = 0; n < rows.size(); n++) {
			G o = null;
			try {
				o = clazz.newInstance();
			} catch (Exception e) {
				logger.error(clazz.getName() + " instance fail.");
			}
			if (o == null)
				return null;
			Map<String, Object> rowMap = (Map<String, Object>) rows.get(n);
			Map<String, Field> fieldMap = ReflectUtil.getClassField(clazz);
			setEntityProperty(rowMap, fieldMap, o);
			newRows.add(o);
		}
		return newRows;
	}

	@SuppressWarnings("unchecked")
	public List<T> listBySql(String sql, String[] names, Object[] values) {
		Query query = em.createNativeQuery(sql, this.entityClass);
		for (int i = 0; i < names.length; i++) {
			query.setParameter(names[i], values[i]);
		}
		List<T> list = query.getResultList();
		return list;
	}

	@Override
	public <G> G findForUniqueBySql(String sql, String[] names, Object[] values, Class<G> clazz) {
		List<G> list = listBySql(sql, names, values, clazz);
		if (list == null || list.size() < 1) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public T findForUniqueBySql(String sql, String[] names, Object[] values) {
		List<T> list = listBySql(sql, names, values);
		if (list == null || list.size() < 1) {
			return null;
		}
		return list.get(0);
	}

}
