package com.rongdu.p2psys.account.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.SearchParam;
import com.rongdu.p2psys.account.domain.AccountSum;

/**
 * AccountSum表的相关操作
 * 
 * @version 1.0
 * @since 2013-8-26
 */
public interface AccountSumDao extends BaseDao<AccountSum> {

	/**
	 * 根据用户ID修改信息
	 * 
	 * @param type
	 * @param value
	 * @param userId
	 */
	void update(String type, double value, long userId);

	/**
	 * 根据用户ID查询信息
	 * 
	 * @param userId
	 * @return
	 */
	AccountSum findByUserId(long userId);

	/**
	 * 资金合计分页汇总
	 * 
	 * @param p
	 * @return
	 */
	int count(SearchParam p);

	/**
	 * 根据搜索条件,获取资金合计列表
	 * 
	 * @param page
	 * @param max
	 * @param p
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List findByUserId(int page, int max, SearchParam p);
}
