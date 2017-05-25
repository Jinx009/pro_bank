package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.ProjectFlagDao;
import com.rongdu.p2psys.crowdfunding.domain.ProjectFlag;

@Repository("projectFlagDao")
public class ProjectFlagDaoImpl extends BaseDaoImpl<ProjectFlag> implements ProjectFlagDao{

	@SuppressWarnings("unchecked")
	public List<ProjectFlag> findByHql(String hql) {
		Query query = em.createQuery(hql);
		List<ProjectFlag> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

}
