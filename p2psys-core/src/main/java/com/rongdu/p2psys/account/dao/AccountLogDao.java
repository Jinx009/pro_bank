package com.rongdu.p2psys.account.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.model.AccountLogModel;

/**
 * 资金记录Dao
 * 
 * @author xx
 * @version 2.0
 * @since 2014年4月10日
 */
public interface AccountLogDao extends BaseDao<AccountLog> {

	/**
	 * 资金记录列表
	 * 
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageDataList<AccountLogModel> accountLogList(AccountLogModel model, int pageNumber, int pageSize);

	/**
	 * 单个操作的资金收入或支出情况
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageDataList<AccountLogModel> accountLogSingleList(AccountLogModel model,
			int pageNumber, int pageSize);

	/**
	 * 统计单个资金数据
	 * @param model
	 * @return
	 */
	public double getAccountSingleTotal(AccountLogModel model);

	public double getAccountSingleTotal(String startTime, String endTime, String accountType);
	
}
