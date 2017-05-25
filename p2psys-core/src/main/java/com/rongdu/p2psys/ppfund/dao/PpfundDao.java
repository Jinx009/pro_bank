package com.rongdu.p2psys.ppfund.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;
import com.rongdu.p2psys.ppfund.model.PpfundModel;

/**
 * PPfund（资金管理产品)
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月16日
 */
public interface PpfundDao extends BaseDao<Ppfund> {
	/**
	 * 首页PPfund数据
	 * 
	 * @return
	 */
	PpfundModel getLastPpfund();
	
	/**
	 * 已成交Ppfund
	 * 
	 * @return
	 */
	Object[] countByFinish();
	/**
	 * 生成投资序号 
	 */
	public int getUserPpfundInOrder(long ppfundInId,long ppfundId);
	/**
	 * 
	 */
	public double getAllMoney();
}
