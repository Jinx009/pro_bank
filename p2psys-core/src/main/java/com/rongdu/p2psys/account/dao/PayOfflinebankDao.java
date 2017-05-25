package com.rongdu.p2psys.account.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.account.domain.PayOfflinebank;

/**
 * 支付方式-线下支付(收账银行账户)Dao
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月17日
 */
public interface PayOfflinebankDao extends BaseDao<PayOfflinebank> {

	/**
	 * 列表
	 * 
	 * @return
	 */
	List<PayOfflinebank> list(int status);

}
