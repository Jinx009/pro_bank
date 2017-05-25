package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.LeaderFactoryDao;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFactory;
import com.rongdu.p2psys.nb.util.StringUtil;

@Repository("leaderFactoryDao")
public class LeaderFactoryDaoImpl extends BaseDaoImpl<LeaderFactory> implements LeaderFactoryDao{

	@SuppressWarnings("unchecked")
	public List<LeaderFactory> getByHql(String hql) {
		List<LeaderFactory> list = null;
		if(StringUtil.isNotBlank(hql)){
			Query query = em.createQuery(hql);
			list = query.getResultList();
		}else{
			list =  findAll();
		}
		return list;
	}

}
