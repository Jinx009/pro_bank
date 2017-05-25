package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.LeaderFlagDao;
import com.rongdu.p2psys.crowdfunding.domain.LeaderFlag;

@Repository("leaderFlagDao")
public class LeaderFlagDaoImpl extends BaseDaoImpl<LeaderFlag> implements LeaderFlagDao{

	@SuppressWarnings("unchecked")
	public List<LeaderFlag> getByHql(String hql) {
		Query query = em.createQuery(hql);
		List<LeaderFlag> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}
}
