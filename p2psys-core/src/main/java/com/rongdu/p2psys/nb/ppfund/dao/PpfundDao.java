package com.rongdu.p2psys.nb.ppfund.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.ppfund.domain.Ppfund;

public interface PpfundDao extends BaseDao<Ppfund> {

	/**
	 * 获取投资总额
	 * 
	 * @param ppfund
	 * @return Double
	 */
	Double getTotalInvestMoney(Ppfund ppfund);

	/**
	 * 获取投资总收益
	 * 
	 * @param ppfund
	 * @return Double
	 */
	Double getTotalProfitMoney(Ppfund ppfund);
}
