package com.rongdu.p2psys.nb.account.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.Account;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.account.model.AccountLogModel;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.user.domain.User;

public interface AccountLogService
{
	public AccountLog saveAccountLog(AccountLog accountLog);

	public void saveNewAccountLog(Ppfund ppfund,Account account, User user,String ip_address,double money);
	
	/**
	 * 列表(多身份对应)
	 * 
	 * @param accountModel
	 * @return
	 */
	PageDataList<AccountLogModel> multipleIdentities(AccountLogModel model);
	
	/**
	 * 资金明细（pc）
	 * @param model
	 * @return
	 */
	PageDataList<AccountLogModel> getAccountLog(AccountLogModel model);
	
	/**
	 * 添加资金记录
	 * @param model
	 */
	void addAccountLog(AccountLogModel model);
	
	public List<AccountLog> getByUserId(Long userId);
}
