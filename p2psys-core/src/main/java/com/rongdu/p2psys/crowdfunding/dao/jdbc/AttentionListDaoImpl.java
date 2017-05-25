package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.AttentionListDao;
import com.rongdu.p2psys.crowdfunding.domain.AttentionList;

@Repository("attentionListDao")
public class AttentionListDaoImpl extends BaseDaoImpl<AttentionList> implements AttentionListDao{

	@SuppressWarnings("unchecked")
	public List<AttentionList> getByHql(String hql) {
		Query query = em.createQuery(hql);
		List<AttentionList> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

}
