package com.rongdu.p2psys.nb.protocol.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolConfigDao;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolDao;
import com.rongdu.p2psys.nb.protocol.domain.ProtocolConfig;

@Repository("protocolConfigDao")
public class ProtocolConfigDaoImpl extends BaseDaoImpl<ProtocolConfig> implements ProtocolConfigDao {

	@Override
	public Long getNextProtocolType() {
		// TODO Auto-generated method stub
		String sql = " select max(protocol_type) from nb_protocol_config ";
		Query query = em.createNativeQuery(sql);
		Object obj = query.getSingleResult();
		if(obj!=null)
		{
			return Long.parseLong(obj.toString()) + 1;
		}
		return (long) 1;
	}

}
