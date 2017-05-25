package com.rongdu.p2psys.crowdfunding.dao.jdbc;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.crowdfunding.dao.VirtualAccountDao;
import com.rongdu.p2psys.crowdfunding.domain.VirtualAccount;

@Repository("virtualAccountDao")
public class VirtualAccountDaoImpl extends BaseDaoImpl<VirtualAccount> implements VirtualAccountDao{

	@SuppressWarnings("unchecked")
	public List<VirtualAccount> getByHql(String hql) {
		Query query = em.createQuery(hql);
		List<VirtualAccount> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

}
