package com.rongdu.p2psys.nb.protocol.dao.impl;


import org.springframework.stereotype.Repository;

import com.rongdu.common.dao.jpa.BaseDaoImpl;
import com.rongdu.p2psys.nb.protocol.dao.ProtocolDao;
import com.rongdu.p2psys.nb.protocol.domain.Protocol;

@Repository("protocolDao")
public class ProtocolDaoImpl extends BaseDaoImpl<Protocol> implements ProtocolDao {


}
