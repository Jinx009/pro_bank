package com.rongdu.p2psys.nb.account.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.account.domain.AccountLog;
import com.rongdu.p2psys.nb.account.dao.AccountLogDao;

@Repository("theAccountLogDao")
public class AccountLogDaoImpl extends BaseDaoImpl<AccountLog> implements AccountLogDao
{

	@SuppressWarnings("unchecked")
	public List<AccountLog> findByHql(String hql) {
		Query query = em.createQuery(hql);
		List<AccountLog> list = query.getResultList();
		if(null!=list&&!list.isEmpty()){
			return list;
		}
		return null;
	}

}
