package com.rongdu.p2psys.nb.recommend.dao;

import com.rongdu.common.dao.BaseDao;
import com.rongdu.p2psys.core.domain.RedPacket;

public interface RedPacketDao extends BaseDao<RedPacket>
{

	public RedPacket findByHql(String hql);

}
