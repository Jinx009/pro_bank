package com.rongdu.p2psys.crowdfunding.dao;

import java.util.List;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;

public interface LeaderFactoryDao extends BaseDao<LeaderFactory>{

	public List<LeaderFactory> getByHql(String hql);

}
