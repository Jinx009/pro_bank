package com.rongdu.p2psys.nb.recommend.service;

import com.rongdu.p2psys.core.domain.RedPacket;

public interface RedPacketService
{
	public RedPacket getById(long id);
	
	public RedPacket getByServiceType(String type);
}
