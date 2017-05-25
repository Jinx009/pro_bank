package com.rongdu.p2psys.core.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.core.domain.Dict;
import com.rongdu.p2psys.core.model.DictModel;

/**
 * 数据字典Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月8日
 */
public interface DictService {

	/**
	 * 数据字典列表
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<Dict> list(DictModel model);

	/**
	 * 获取
	 * 
	 * @param id
	 * @return
	 */
	Dict find(long id);

	/**
	 * 获取
	 * 
	 * @param nid
	 * @param value
	 * @return
	 */
	Dict find(String nid, String value);

	/**
	 * 分页
	 * 
	 * @param nid
	 * @return
	 */
	List<Dict> list(String nid);

	/**
	 * 更新
	 * 
	 * @param dict
	 */
	Dict update(Dict dict);

	/**
	 * 删除/禁用
	 * 
	 * @param id
	 */
	void delete(long id);

	/**
	 * 新增
	 * 
	 * @param dict
	 */
	void add(Dict dict);

}
