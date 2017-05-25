package com.rongdu.p2psys.ppfund.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.ppfund.domain.PpfundEarnings;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.user.domain.User;

/**
 * PPfund资金管理产品
 * 
 * @author yinliang
 * @version 2.0
 * @Date 2015年3月21日
 */
public interface PpfundEarningsDao extends BaseDao<PpfundEarnings> {
	/**
	 * 获取收益总和
	 * 
	 * @param in
	 * @return
	 */
	double getEarningsSum(PpfundIn in);

	/**
	 * 获取最后一次收益
	 * 
	 * @param ppfundInId
	 * @return
	 */
	double getUserEarningsSum(PpfundIn in);
	
	/**
	 * 获取最后一次收益
	 * 
	 * @param ppfundInId
	 * @return
	 */
	double getLastEarningsInterest(long ppfundInId);
	
	
}
