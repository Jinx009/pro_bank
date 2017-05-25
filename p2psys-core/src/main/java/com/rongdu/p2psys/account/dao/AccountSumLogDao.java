package com.rongdu.p2psys.account.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.SearchParam;
import com.rongdu.p2psys.account.domain.AccountSumLog;

/**
 * 用户资金合计日志
 * 
 * @author zhangyz
 * @version 1.0
 * @since 2013-9-5
 */
public interface AccountSumLogDao extends BaseDao<AccountSumLog> {

	/**
	 * 根据搜索条件,获取资金合计日志列表
	 * 
	 * @param page 开始条数
	 * @param max 结束
	 * @param userId 用户ID
	 * @param p 查询条数
	 * @return 资金合计日志列表
	 */
	List<AccountSumLog> getAccountSumLogPage(int page, int max, long userId, SearchParam p);

	/**
	 * 资金合计日志分页汇总
	 * 
	 * @param p
	 * @return
	 */
	int count(SearchParam p, long userId);

}
