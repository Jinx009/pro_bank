package com.rongdu.p2psys.nb.recommend.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfit;


@Repository("recommendProfitDao")
public class RecommendProfitDaoImpl extends BaseDaoImpl<RecommendProfit> implements RecommendProfitDao
{

	@SuppressWarnings("unchecked")
	public RecommendProfit findByMoney(double account)
	{
		String hql = "  from RecommendProfit where startMoney < "+account+" and endMoney > "+account+" ";
		
		Query query = em.createQuery(hql);
		
		List<RecommendProfit> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			return list.get(0);
		}
		
		return null;
	}

}
