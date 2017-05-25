package com.rongdu.p2psys.account.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.common.model.jpa.SearchParam;
import com.rongdu.p2psys.account.domain.AccountSum;

/**
 * 用户资金合计信息处理
 * 
 * @author zhangyz
 * @version 1.0
 * @since 2013-9-6
 */
public interface AccountSumService {

	/**
	 * 添加信息
	 * 
	 * @param accountSum 实体
	 */
	void add(AccountSum accountSum);

	/**
	 * 用户资金合计查询分页.
	 * 
	 * @param page 分页信息
	 * @param param 查询条件分装类
	 * @return page
	 */
	@SuppressWarnings("rawtypes")
	PageDataList getAccountSumPage(int page, SearchParam param);

	/**
	 * 用户资金合计日志查询分页.
	 * 
	 * @param page 分页信息
	 * @param userId 用户ID
	 * @param param 查询条件分装类
	 * @return page
	 */
	@SuppressWarnings("rawtypes")
	PageDataList getAccountSumLogPage(int page, long userId, SearchParam param);

	/**
	 * 已赚奖励
	 * 
	 * @param userId
	 * @return
	 */
	AccountSum findByUserId(long userId);

}
