package com.rongdu.p2psys.nb.recommend.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.recommend.dao.RecommendProfitRecordDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfitRecord;

@Repository("recommendProfitRecordDao")
public class RecommendProfitRecordDaoImpl extends BaseDaoImpl<RecommendProfitRecord> implements RecommendProfitRecordDao {

	@SuppressWarnings("unchecked")
	public List<RecommendProfitRecord> findByHql(String hql)
	{
		Query query = em.createQuery(hql);
		
		List<RecommendProfitRecord> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			return list;
		}
		
		return null;
	}

}
