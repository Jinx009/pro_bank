package com.rongdu.p2psys.nb.protocol.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;
import com.rongdu.p2psys.nb.protocol.domain.ProtocolConfig;

/**
 * 协议模板Dao
 * 
 * @author qj
 * @version 2.0
 * @since 2014年5月22日
 */
public interface ProtocolConfigDao extends BaseDao<ProtocolConfig> {
	
     Long  getNextProtocolType();
}
