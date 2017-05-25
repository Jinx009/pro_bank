package com.rongdu.p2psys.account.service;

import java.util.List;

import com.rongdu.common.model.jpa.PageDataList;
import com.rongdu.p2psys.account.domain.PayOfflinebank;

/**
 * 支付方式-线下支付(线下银行/收款账户)Service
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月17日
 */
public interface PayOfflinebankService {

	/**
	 * 列表
	 * 
	 * @return
	 */
	List<PayOfflinebank> list();

	/**
	 * 线下充值银行账户管理列表
	 * 
	 * @param payOfflinebank
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	PageDataList<PayOfflinebank> payOfflinebankList(PayOfflinebank payOfflinebank, int pageNumber, int pageSize, String searchName);

	/**
	 * 保存
	 * 
	 * @param payOfflinebank
	 */
	void offlinebankAdd(PayOfflinebank payOfflinebank);

	/**
	 * 根据id查询实体
	 * 
	 * @param id
	 * @return
	 */
	PayOfflinebank find(long id);

	/**
	 * 修改保存
	 * 
	 * @param payOfflinebank
	 */
	void offlinebankEdit(PayOfflinebank payOfflinebank);

}
