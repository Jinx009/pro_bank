package com.rongdu.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;

public interface BaseDao<T> {

	/**
	 * 计数
	 * 
	 * @param param
	 * @return
	 */
	public int countByCriteria(QueryParam param);

	/**
	 * 保存
	 * 
	 * @param entity
	 * @return
	 */
	public T save(T entity);

	/**
	 * 保存集合
	 * 
	 * @param ts
	 */
	public void save(Collection<T> ts);

	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public T update(T entity);

	/**
	 * 更新集合
	 * 
	 * @param ts
	 * @return
	 */
	public boolean update(Collection<T> ts);

	/**
	 * 更新
	 * 
	 * @param entity
	 */
	public void updateWithRefresh(T entity);

	public void refresh(T entity);

	/**
	 * 获取
	 * 
	 * @param entityId
	 * @return
	 */
	public T find(Serializable entityId);

	/**
	 * 获取
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public T find(Class<T> entityClass, Object id);

	/**
	 * 获取并锁定
	 * 
	 * @param entityId
	 * @return
	 */
	public T findWithLock(Serializable entityId);

	/**
	 * 删除
	 * 
	 * @param entityid
	 */
	public void delete(Serializable entityid);

	/**
	 * 批量删除
	 * 
	 * @param entityids
	 */
	public void delete(Serializable[] entityids);

	/**
	 * 删除集合
	 * 
	 * @param c
	 */
	public void delete(Collection<T> c);

	/**
	 * 插入或者更新，并返回对象 不推荐调用merge，优先调用update
	 * 
	 * @param entity
	 * @return
	 */
	public T merge(T entity);

	public void lock(T entity);

	public void clear();

	public void detach(Collection<T> ts);

	public void detach(Serializable entityid);

	public void flush();

	/**
	 * 获取集合(所有数据)
	 */
	public List<T> findAll();

	public List<T> findByCriteria(QueryParam param);

	public T findByCriteriaForUnique(QueryParam param);

	public List<T> findByCriteria(QueryParam param, int start, int limit);

	/**
	 * 根据属性查找对象列表
	 * 
	 * @param value
	 * @return
	 */
	public List<T> findByProperty(String property, Object value);

	/**
	 * 根据属性查找对象
	 * 
	 * @param value
	 * @return
	 */
	public T findObjByProperty(String property, Object value);

	/**
	 * 分页
	 * 
	 * @param param
	 * @return
	 */
	public PageDataList<T> findPageList(QueryParam param);

	/**
	 * 分页-根据sql查询
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public PageDataList<T> findPageListBySql(String sql, QueryParam param);

	/**
	 * 分页-根据sql查询
	 * 
	 * @param sql
	 * @param param
	 * @param clazz
	 * @return
	 */
	public <G> PageDataList<G> findPageListBySql(String sql, QueryParam param, Class<G> clazz);

	public <G> List<G> findBySql(String sql, QueryParam param, Class<G> clazz);

	/**
	 * 分页-查询所有
	 * 
	 * @param param
	 * @return
	 */
	public PageDataList<T> findAllPageList(QueryParam param);

	public int updateByJpql(String jpql, String[] names, Object[] values);

	public List<T> listByJpql(String sql, String[] names, Object[] values);

	public int updateBySql(String nativeSql, String[] names, Object[] values);

	public List<Map<String, Object>> listMapBySql(String sql, String[] names, Object[] values);

	public <G> List<G> listBySql(String sql, String[] names, Object[] values, Class<G> clazz);

	public <G> G findForUniqueBySql(String sql, String[] names, Object[] values, Class<G> clazz);

	public List<T> listBySql(String sql, String[] names, Object[] values);

	public T findForUniqueBySql(String sql, String[] names, Object[] values);

	public <G> PageDataList<G> findPageListBySql(String selectSql, String formSql, QueryParam param, Class<G> clazz);

}
