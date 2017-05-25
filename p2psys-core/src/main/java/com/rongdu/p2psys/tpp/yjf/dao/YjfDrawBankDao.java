package com.rongdu.p2psys.tpp.yjf.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.tpp.domain.YjfDrawBank;

/**
 * 
 * 易极付提供的银行dao
 * 
 * @author lx
 * @version 2.0
 * @since 2014年7月31日
 */
public interface YjfDrawBankDao extends BaseDao<YjfDrawBank> {
	/**
	 * 
	 * @param bankCode 
	 * @return
	 */
	public YjfDrawBank getYjfDrawBankByBankCode(String bankCode);
	/**
	 * 通过银行名称查询
	 * @param bankName 银行名称
	 * @return 银行
	 */
	YjfDrawBank getYjfDrawBankByBankName(String bankName);
}
