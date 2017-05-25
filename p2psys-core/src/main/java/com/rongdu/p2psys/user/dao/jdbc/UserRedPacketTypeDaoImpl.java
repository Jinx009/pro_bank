package com.rongdu.p2psys.user.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.user.dao.UserRedPacketTypeDao;
import com.rongdu.p2psys.user.domain.UserRedPacketType;

@Repository("userRedPacketTypeDao")
public class UserRedPacketTypeDaoImpl extends BaseDaoImpl<UserRedPacketType> implements UserRedPacketTypeDao {

	@Override
	public UserRedPacketType findByNid(String nid) {
	
		return findObjByProperty("nid", nid);
	}
}
