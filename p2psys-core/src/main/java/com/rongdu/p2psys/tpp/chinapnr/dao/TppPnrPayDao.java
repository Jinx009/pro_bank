package com.rongdu.p2psys.tpp.chinapnr.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.tpp.domain.TppPnrPay;

/**
 * 汇付调度DAO
 * 
 * @author wzh
 * @version 2.0
 * @since 2014-11-20
 */
public interface TppPnrPayDao extends BaseDao<TppPnrPay> {
	
	public TppPnrPay findChinapnrModelByOrd(String ordNo);
}
