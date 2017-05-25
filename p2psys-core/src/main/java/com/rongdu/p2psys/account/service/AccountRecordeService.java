package com.rongdu.p2psys.account.service;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.AccountRecorde;
import com.rongdu.p2psys.account.model.AccountRecordeModel;

/**
 * 资金汇总记录
 * 
 * @author yl
 * @version 2.0
 * @date 2015年5月4日
 */
public interface AccountRecordeService {
	/**
	 * 统计数据
	 */
	void doStatistics();
	
	/**
	 * 分页查询
	 * 
	 * @param model
	 * @return
	 */
	PageDataList<AccountRecorde> pageDataList(AccountRecordeModel model);
}
