package com.rongdu.p2psys.user.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.user.domain.UserRedPacketType;

/**
 * 红包类型Dao
 * @author zf
 *
 */
public interface UserRedPacketTypeDao extends BaseDao<UserRedPacketType>{

	UserRedPacketType findByNid(String nid);
	
}
