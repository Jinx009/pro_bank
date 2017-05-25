package com.rongdu.p2psys.nb.recommend.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfit;

public interface RecommendProfitDao extends BaseDao<RecommendProfit>
{
	public RecommendProfit findByMoney(double account);
}
