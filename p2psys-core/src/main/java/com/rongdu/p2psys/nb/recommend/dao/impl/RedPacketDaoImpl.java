package com.rongdu.p2psys.nb.recommend.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.nb.recommend.dao.RedPacketDao;

@Repository("theRedPacketDao")
public class RedPacketDaoImpl extends BaseDaoImpl<RedPacket> implements RedPacketDao
{

	@SuppressWarnings("unchecked")
	public RedPacket findByHql(String hql)
	{
		Query query = em.createQuery(hql);
		
		List<RedPacket> list = query.getResultList();
		
		if(list.size()>0&&!list.isEmpty())
		{
			return list.get(0);
		}
		
		return null;
	}

}
