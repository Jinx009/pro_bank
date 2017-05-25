package com.rongdu.p2psys.account.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.user.domain.User;

/**
 * 资金记录Service
 * 
 * @author xx
 * @version 2.0
 * @since 2014年3月19日
 */
public interface AccountLogService {
	/**
	 * 新增
	 * 
	 * @param log
	 */
	void add(AccountLog log);

	/**
	 * 列表
	 * 
	 * @param accountModel
	 * @return
	 */
	PageDataList<AccountLogModel> list(AccountLogModel model);

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
     * 首页交易记录
     * @param user
     * @return
     */
    public List<AccountLogModel> accountTransactionLog(User user);

	/**
	 * 单个操作的资金收入或支出情况
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public PageDataList<AccountLogModel> accountLogSingleList(AccountLogModel model,
			int pageNumber, int pageSize);

	/**
	 * 统计单个资金数据
	 * @param model
	 * @return
	 */
	public double getAccountSingleTotal(AccountLogModel model);

	public double getAccountSingleTotal(String startTime, String endTime, String accountType);

}
