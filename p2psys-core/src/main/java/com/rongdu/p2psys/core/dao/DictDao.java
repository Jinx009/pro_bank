package com.rongdu.p2psys.core.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.QueryParam;
import com.rongdu.p2psys.core.domain.Dict;

/**
 * 数据字典Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月8日
 */
public interface DictDao extends BaseDao<Dict> {

	/**
	 * @param param
	 * @return
	 */
	PageDataList<Dict> list(QueryParam param);

	/**
	 * 获取
	 * 
	 * @param nid
	 * @param value
	 * @return
	 */
	Dict find(String nid, String value);

	/**
	 * 通过name获取
	 * @param name
	 * @param value
	 * @return
	 */
	Dict findByName(String name);
	
	/**
	 * 分页
	 * 
	 * @param nid
	 * @return
	 */
	List<Dict> list(String nid);

	/**
	 * 删除/禁用
	 * 
	 * @param id
	 */
	void delete(long id);

}
