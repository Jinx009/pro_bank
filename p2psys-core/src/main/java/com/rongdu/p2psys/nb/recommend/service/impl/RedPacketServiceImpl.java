package com.rongdu.p2psys.nb.recommend.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.rongdu.p2psys.core.domain.RedPacket;
import com.rongdu.p2psys.nb.recommend.dao.RedPacketDao;
import com.rongdu.p2psys.nb.recommend.service.RedPacketService;

@Service("theRedPacketService")
public class RedPacketServiceImpl implements RedPacketService
{

	@Resource
	private RedPacketDao theRedPacketDao;
	
	public RedPacket getById(long id)
	{
		return theRedPacketDao.find(id);
	}

	public RedPacket getByServiceType(String type)
	{
		String hql = " from RedPacket where serviceType = '"+type+"'  ";
		
		return theRedPacketDao.findByHql(hql);
	}

}
