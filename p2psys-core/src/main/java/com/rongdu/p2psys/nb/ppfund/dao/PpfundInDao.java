package com.rongdu.p2psys.nb.ppfund.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.ppfund.domain.PpfundIn;
import com.rongdu.p2psys.user.domain.User;


public interface PpfundInDao extends BaseDao<PpfundIn>
{
	public PpfundIn findByUser(User user, long ppfundId);
	
	/**
	 * 获取该用户本日投资现金产品总额
	 * 
	 * @param userId
	 * @return Double
	 */
	Double getTotalDayInvestMoney(Long userId);

}
