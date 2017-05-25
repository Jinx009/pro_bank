package com.rongdu.p2psys.nb.recommend.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.recommend.domain.RecommendProfitRecord;

public interface RecommendProfitRecordDao extends BaseDao<RecommendProfitRecord>
{
	public List<RecommendProfitRecord> findByHql(String hql);
}
