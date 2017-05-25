package com.rongdu.p2psys.account.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.account.domain.PayOnlinebank;
import com.rongdu.p2psys.account.model.PayOnlinebankModel;

/**
 * 支付方式-网银直联(线上银行)Dao
 * 
 * @author sj
 * @version 2.0
 * @since 2014年3月17日
 */
public interface PayOnlinebankDao extends BaseDao<PayOnlinebank> {

	/**
	 * 列表
	 * 
	 * @param pay_nid
	 * @return
	 */
	List<PayOnlinebankModel> list(long pay_id);

}
