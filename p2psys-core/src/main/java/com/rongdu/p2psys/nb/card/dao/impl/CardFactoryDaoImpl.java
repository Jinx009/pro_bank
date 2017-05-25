package com.rongdu.p2psys.nb.card.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.card.dao.CardFactoryDao;
import com.rongdu.p2psys.nb.card.domain.CardFactory;

@Repository("cardFactoryDao")
public class CardFactoryDaoImpl extends BaseDaoImpl<CardFactory> implements CardFactoryDao
{

	@SuppressWarnings("unchecked")
	public List<CardFactory> getUnusedCard()
	{
		String hql = " from CardFactory where status = 0 ";
		Query query = em.createQuery(hql);
		List<CardFactory> list = query.getResultList();
		if(null!=list&&!list.isEmpty())
		{
			return list;
		}
		return null;
	}

}
